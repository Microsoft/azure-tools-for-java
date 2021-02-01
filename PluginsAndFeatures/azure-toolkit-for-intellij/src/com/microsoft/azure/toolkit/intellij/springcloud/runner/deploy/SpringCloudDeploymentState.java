/*
 * Copyright (c) Microsoft Corporation
 *
 * All rights reserved.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.microsoft.azure.toolkit.intellij.springcloud.runner.deploy;

import com.intellij.openapi.project.Project;
import com.microsoft.azure.management.appplatform.v2020_07_01.implementation.AppPlatformManager;
import com.microsoft.azure.management.appplatform.v2020_07_01.implementation.AppResourceInner;
import com.microsoft.azure.management.appplatform.v2020_07_01.implementation.DeploymentResourceInner;
import com.microsoft.azure.toolkit.intellij.common.AzureRunProfileState;
import com.microsoft.azure.toolkit.intellij.springcloud.SpringCloudUtils;
import com.microsoft.azure.toolkit.lib.common.task.AzureTask;
import com.microsoft.azure.toolkit.lib.springcloud.*;
import com.microsoft.azure.toolkit.lib.springcloud.config.SpringCloudDeploymentConfig;
import com.microsoft.azure.toolkit.lib.springcloud.model.ScaleSettings;
import com.microsoft.azure.tools.utils.RxUtils;
import com.microsoft.azuretools.authmanage.AuthMethodManager;
import com.microsoft.azuretools.telemetry.TelemetryConstants;
import com.microsoft.azuretools.telemetrywrapper.Operation;
import com.microsoft.azuretools.telemetrywrapper.TelemetryManager;
import com.microsoft.intellij.RunProcessHandler;
import com.microsoft.tooling.msservices.components.DefaultLoader;
import com.microsoft.tooling.msservices.serviceexplorer.azure.springcloud.SpringCloudStateManager;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.microsoft.azure.toolkit.lib.springcloud.AzureSpringCloudConfigUtils.DEFAULT_DEPLOYMENT_NAME;

public class SpringCloudDeploymentState extends AzureRunProfileState<AppResourceInner> {
    private static final int GET_URL_TIMEOUT = 60;
    private static final int GET_STATUS_TIMEOUT = 180;
    private static final String UPDATE_APP_WARNING = "It may take some moments for the configuration to be applied at server side!";
    private static final String GET_DEPLOYMENT_STATUS_TIMEOUT = "Deployment succeeded but the app is still starting, " +
        "you can check the app status from Azure Portal.";
    private static final String NOTIFICATION_TITLE = "Deploy Spring Cloud App";

    private final SpringCloudDeployConfiguration config;

    public SpringCloudDeploymentState(Project project, SpringCloudDeployConfiguration configuration) {
        super(project);
        this.config = configuration;
    }

    @Nullable
    @Override
    public AppResourceInner executeSteps(@NotNull RunProcessHandler processHandler, @NotNull Map<String, String> telemetryMap) throws Exception {
        // prepare the jar to be deployed
        updateTelemetryMap(telemetryMap);
        final File artifact = SpringCloudUtils.getArtifact(config.getArtifactIdentifier(), project);
        final boolean enableDisk = config.getDeployment() != null && config.getDeployment().isEnablePersistentStorage();
        final String clusterName = config.getClusterName();
        final String clusterId = config.getClusterId();
        final String appName = config.getAppName();

        final SpringCloudDeploymentConfig deploymentConfig = config.getDeployment();
        final Map<String, String> env = deploymentConfig.getEnvironment();
        final String jvmOptions = deploymentConfig.getJvmOptions();
        final ScaleSettings scaleSettings = deploymentConfig.getScaleSettings();
        final String runtimeVersion = deploymentConfig.getJavaVersion();

        final AzureSpringCloud az = AzureSpringCloud.az(this.getAppPlatformManager());
        final SpringCloudCluster cluster = az.cluster(clusterName);
        final SpringCloudApp app = cluster.app(appName);
        final String deploymentName = StringUtils.firstNonBlank(
            deploymentConfig.getDeploymentName(),
            app.getActiveDeploymentName(),
            DEFAULT_DEPLOYMENT_NAME
                                                               );
        final SpringCloudDeployment deployment = app.deployment(deploymentName);

        final boolean toCreateApp = !app.exists();
        final boolean toCreateDeployment = !deployment.exists();
        final List<AzureTask<?>> tasks = new ArrayList<>();
        if (toCreateApp) {
            setText(processHandler, String.format("Creating app(%s)...", appName));
            app.create().commit();
            SpringCloudStateManager.INSTANCE.notifySpringAppUpdate(clusterId, getInner(app.entity()), null);
            setText(processHandler, "Successfully created the app.");
        }
        setText(processHandler, String.format("Uploading artifact(%s) to Azure...", artifact.getPath()));
        final SpringCloudApp.Uploader artifactUploader = app.uploadArtifact(artifact.getPath());
        artifactUploader.commit();
        setText(processHandler, "Successfully uploaded the artifact.");

        final SpringCloudDeployment.Updater deploymentModifier = (toCreateDeployment ? deployment.create() : deployment.update())
            .configEnvironmentVariables(env)
            .configJvmOptions(jvmOptions)
            .configScaleSettings(scaleSettings)
            .configRuntimeVersion(runtimeVersion)
            .configArtifact(artifactUploader.getArtifact());
        setText(processHandler, String.format(toCreateDeployment ? "Creating deployment(%s)..." : "Updating deployment(%s)...", deploymentName));
        deploymentModifier.commit();
        setText(processHandler, toCreateDeployment ? "Successfully created the deployment" : "Successfully updated the deployment");
        SpringCloudStateManager.INSTANCE.notifySpringAppUpdate(clusterId, getInner(app.entity()), getInner(deployment.entity()));

        final SpringCloudApp.Updater appUpdater = app.update()
            .activate(StringUtils.firstNonBlank(app.getActiveDeploymentName(), deploymentName))
            .setPublic(config.isPublic())
            .enablePersistentDisk(enableDisk);
        if (!appUpdater.isSkippable()) {
            setText(processHandler, String.format("Updating app(%s)...", appName));
            appUpdater.commit();
            setText(processHandler, "Successfully updated the app.");
            DefaultLoader.getUIHelper().showErrorNotification(NOTIFICATION_TITLE, UPDATE_APP_WARNING);
        }

        SpringCloudStateManager.INSTANCE.notifySpringAppUpdate(clusterId, getInner(app.entity()), getInner(deployment.entity()));
        if (!deployment.waitUntilReady(GET_STATUS_TIMEOUT)) {
            DefaultLoader.getUIHelper().showErrorNotification(NOTIFICATION_TITLE, GET_DEPLOYMENT_STATUS_TIMEOUT);
        }
        printPublicUrl(app, processHandler);
        return getInner(app.entity());
    }

    @Override
    protected Operation createOperation() {
        return TelemetryManager.createOperation(TelemetryConstants.SPRING_CLOUD, TelemetryConstants.CREATE_SPRING_CLOUD_APP);
    }

    @Override
    protected void onSuccess(AppResourceInner result, @NotNull RunProcessHandler processHandler) {
        setText(processHandler, "Deploy succeed");
        processHandler.notifyComplete();
    }

    @Override
    protected String getDeployTarget() {
        return "SPRING_CLOUD";
    }

    @Override
    protected void updateTelemetryMap(@NotNull Map<String, String> telemetryMap) {
        telemetryMap.putAll(config.getModel().getTelemetryProperties());
    }

    private void printPublicUrl(final SpringCloudApp app, @NotNull RunProcessHandler processHandler) {
        if (!app.entity().isPublic()) {
            return;
        }
        setText(processHandler, String.format("Getting public url of app(%s)...", app.name()));
        String publicUrl = app.entity().getApplicationUrl();
        if (StringUtils.isEmpty(publicUrl)) {
            publicUrl = RxUtils.pollUntil(() -> app.refresh().entity().getApplicationUrl(), StringUtils::isNotBlank, GET_URL_TIMEOUT);
        }
        if (StringUtils.isEmpty(publicUrl)) {
            DefaultLoader.getUIHelper().showErrorNotification(NOTIFICATION_TITLE, "Failed to get application url");
        } else {
            setText(processHandler, String.format("Application url: %s", publicUrl));
        }
    }

    @SneakyThrows
    private static AppResourceInner getInner(final SpringCloudAppEntity app) {
        final Field inner = SpringCloudAppEntity.class.getDeclaredField("inner");
        inner.setAccessible(true);
        return (AppResourceInner) inner.get(app);
    }

    @SneakyThrows
    private static DeploymentResourceInner getInner(final SpringCloudDeploymentEntity deployment) {
        final Field inner = SpringCloudDeploymentEntity.class.getDeclaredField("inner");
        inner.setAccessible(true);
        return (DeploymentResourceInner) inner.get(deployment);
    }

    private AppPlatformManager getAppPlatformManager() {
        return AuthMethodManager.getInstance().getAzureSpringCloudClient(this.config.getSubscriptionId());
    }
}
