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

package com.microsoft.intellij.runner.webapp.webappconfig;

import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.microsoft.azure.common.exceptions.AzureExecutionException;
import com.microsoft.azure.management.appservice.DeploymentSlot;
import com.microsoft.azure.management.appservice.WebApp;
import com.microsoft.azure.management.appservice.WebAppBase;
import com.microsoft.azure.toolkit.lib.common.operation.AzureOperation;
import com.microsoft.azuretools.core.mvp.model.webapp.AzureWebAppMvpModel;
import com.microsoft.azuretools.telemetry.TelemetryConstants;
import com.microsoft.azuretools.telemetrywrapper.Operation;
import com.microsoft.azuretools.telemetrywrapper.TelemetryManager;
import com.microsoft.azuretools.utils.AzureUIRefreshCore;
import com.microsoft.azuretools.utils.AzureUIRefreshEvent;
import com.microsoft.azuretools.utils.WebAppUtils;
import com.microsoft.intellij.runner.AzureRunProfileState;
import com.microsoft.intellij.runner.RunProcessHandler;
import com.microsoft.intellij.runner.webapp.Constants;
import com.microsoft.intellij.ui.components.AzureArtifact;
import com.microsoft.intellij.ui.components.AzureArtifactManager;
import com.microsoft.intellij.util.MavenRunTaskUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.maven.model.MavenConstants;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import static com.microsoft.intellij.ui.messages.AzureBundle.message;

public class WebAppRunState extends AzureRunProfileState<WebAppBase> {
    private WebAppConfiguration webAppConfiguration;
    private final IntelliJWebAppSettingModel webAppSettingModel;

    /**
     * Place to execute the Web App deployment task.
     */
    public WebAppRunState(Project project, WebAppConfiguration webAppConfiguration) {
        super(project);
        this.webAppConfiguration = webAppConfiguration;
        this.webAppSettingModel = webAppConfiguration.getModel();
    }

    @Nullable
    @Override
    @AzureOperation(
        value = "run web app[%s] in Azure",
        params = {"@webAppConfiguration.getName()"},
        type = AzureOperation.Type.ACTION
    )
    public WebAppBase executeSteps(@NotNull RunProcessHandler processHandler
        , @NotNull Map<String, String> telemetryMap) throws Exception {
        File file = new File(getTargetPath());
        if (!file.exists()) {
            throw new FileNotFoundException(String.format(message("webapp.deploy.error.noTargetFile"), file.getAbsolutePath()));
        }
        webAppConfiguration.setTargetName(file.getName());
        WebAppBase deployTarget = getDeployTargetByConfiguration(processHandler);
        WebAppUtils.deployArtifactsToAppService(deployTarget, file,
                webAppConfiguration.isDeployToRoot(), processHandler);
        return deployTarget;
    }

    private boolean isDeployToSlot() {
        return !webAppSettingModel.isCreatingNew() && webAppSettingModel.isDeployToSlot();
    }

    @AzureOperation(
        value = "open url of web app[%s] in local browser",
        params = {"@webAppConfiguration.getName()"},
        type = AzureOperation.Type.ACTION
    )
    private void openWebAppInBrowser(String url, RunProcessHandler processHandler) {
        try {
            Desktop.getDesktop().browse(new URL(url).toURI());
        } catch (final IOException | URISyntaxException e) {
            processHandler.println(e.getMessage(), ProcessOutputTypes.STDERR);
        }
    }

    @Override
    protected Operation createOperation() {
        return TelemetryManager.createOperation(TelemetryConstants.WEBAPP, TelemetryConstants.DEPLOY_WEBAPP);
    }

    @Override
    @AzureOperation(
        value = "update local configuration and open url of web app[%s] in local browser",
        params = {"@webAppConfiguration.getName()"},
        type = AzureOperation.Type.ACTION
    )
    protected void onSuccess(WebAppBase result, @NotNull RunProcessHandler processHandler) {
        if (webAppSettingModel.isCreatingNew() && AzureUIRefreshCore.listeners != null) {
            AzureUIRefreshCore.execute(new AzureUIRefreshEvent(AzureUIRefreshEvent.EventType.REFRESH, null));
        }
        updateConfigurationDataModel(result);
        int indexOfDot = webAppSettingModel.getTargetName().lastIndexOf(".");
        final String fileName = webAppSettingModel.getTargetName().substring(0, indexOfDot);
        final String fileType = webAppSettingModel.getTargetName().substring(indexOfDot + 1);
        final String url = getUrl(result, fileName, fileType);
        processHandler.setText(message("appService.deploy.hint.succeed"));
        processHandler.setText("URL: " + url);
        if (webAppSettingModel.isOpenBrowserAfterDeployment()) {
            openWebAppInBrowser(url, processHandler);
        }
        processHandler.notifyComplete();
    }

    @Override
    protected void onFail(@NotNull String errMsg, @NotNull RunProcessHandler processHandler) {
        processHandler.println(errMsg, ProcessOutputTypes.STDERR);
        processHandler.notifyComplete();
    }

    @Override
    protected String getDeployTarget() {
        return isDeployToSlot() ? "DeploymentSlot" : "WebApp";
    }

    @Override
    protected void updateTelemetryMap(@NotNull Map<String, String> telemetryMap) {
        telemetryMap.put("SubscriptionId", webAppSettingModel.getSubscriptionId());
        telemetryMap.put("CreateNewApp", String.valueOf(webAppSettingModel.isCreatingNew()));
        telemetryMap.put("CreateNewSP", String.valueOf(webAppSettingModel.isCreatingAppServicePlan()));
        telemetryMap.put("CreateNewRGP", String.valueOf(webAppSettingModel.isCreatingResGrp()));
        telemetryMap.put("FileType", MavenRunTaskUtil.getFileType(webAppSettingModel.getTargetName()));
    }

    @NotNull
    private WebAppBase getDeployTargetByConfiguration(@NotNull RunProcessHandler processHandler) throws Exception {
        if (webAppSettingModel.isCreatingNew()) {
            final WebApp webapp = AzureWebAppMvpModel.getInstance().getWebAppByName(webAppSettingModel.getSubscriptionId(),
                                                                                    webAppSettingModel.getResourceGroup(),
                                                                                    webAppSettingModel.getWebAppName());
            if (webapp == null) {
                return createWebApp(processHandler);
            }
        }

        final WebApp webApp = AzureWebAppMvpModel.getInstance()
            .getWebAppById(webAppSettingModel.getSubscriptionId(), webAppSettingModel.getWebAppId());
        if (webApp == null) {
            processHandler.setText(message("appService.deploy.hint.failed"));
            throw new Exception(message("webapp.deploy.error.noWebApp"));
        }

        if (isDeployToSlot()) {
            if (webAppSettingModel.getSlotName() == Constants.CREATE_NEW_SLOT) {
                return createDeploymentSlot(processHandler);
            } else {
                return webApp.deploymentSlots().getByName(webAppSettingModel.getSlotName());
            }
        } else {
            return webApp;
        }
    }

    @AzureOperation(
        value = "get the artifact to be deployed to web app[%s]",
        params = {"@webAppConfiguration.getName()"},
        type = AzureOperation.Type.SERVICE
    )
    private String getTargetPath() throws AzureExecutionException {
        final AzureArtifact azureArtifact =
                AzureArtifactManager.getInstance(project).getAzureArtifactById(webAppConfiguration.getAzureArtifactType(),
                                                                               webAppConfiguration.getArtifactIdentifier());
        if (Objects.isNull(azureArtifact)) {
            throw new AzureExecutionException(String.format("The artifact '%s' you selected doesn't exists", webAppConfiguration.getArtifactIdentifier()));
        }
        return AzureArtifactManager.getInstance(project).getFileForDeployment(azureArtifact);
    }

    @AzureOperation(
        value = "create web app[%s]",
        params = {"@webAppConfiguration.getName()"},
        type = AzureOperation.Type.SERVICE
    )
    private WebApp createWebApp(@NotNull RunProcessHandler processHandler) throws Exception {
        processHandler.setText(message("webapp.deploy.hint.creatingWebApp"));
        try {
            return AzureWebAppMvpModel.getInstance().createWebApp(webAppSettingModel);
        } catch (Exception e) {
            processHandler.setText(message("webapp.deploy.error.noWebApp"));
            throw new Exception(String.format(message("webapp.deploy.error.createFailed"), e.getMessage()));
        }
    }

    @AzureOperation(
        value = "create deployment slot for web app[%s]",
        params = {"@webAppConfiguration.getName()"},
        type = AzureOperation.Type.SERVICE
    )
    private DeploymentSlot createDeploymentSlot(@NotNull RunProcessHandler processHandler) throws Exception {
        processHandler.setText(message("webapp.deploy.hint.creatingDeploymentSlot"));
        try {
            return AzureWebAppMvpModel.getInstance().createDeploymentSlot(webAppSettingModel);
        } catch (Exception e) {
            processHandler.setText(message("webapp.deploy.error.noWebApp"));
            throw new Exception(String.format(message("webapp.deploy.error.createSlotFailed"), e.getMessage()));
        }
    }

    @NotNull
    private String getUrl(@NotNull WebAppBase webApp, @NotNull String fileName, @NotNull String fileType) {
        String url = "https://" + webApp.defaultHostName();
        if (Comparing.equal(fileType, MavenConstants.TYPE_WAR) && !webAppSettingModel.isDeployToRoot()) {
            url += "/" + WebAppUtils.encodeURL(fileName.replaceAll("#", StringUtils.EMPTY)).replaceAll("\\+", "%20");
        }
        return url;
    }

    private void updateConfigurationDataModel(@NotNull WebAppBase app) {
        webAppSettingModel.setCreatingNew(false);
        // todo: add flag to indicate create new slot or not
        if (app instanceof DeploymentSlot) {
            webAppSettingModel.setSlotName(app.name());
            webAppSettingModel.setNewSlotConfigurationSource(Constants.DO_NOT_CLONE_SLOT_CONFIGURATION);
            webAppSettingModel.setNewSlotName("");
            webAppSettingModel.setWebAppId(((DeploymentSlot) app).parent().id());
        } else {
            webAppSettingModel.setWebAppId(app.id());
        }
        webAppSettingModel.setWebAppName("");
        webAppSettingModel.setResourceGroup("");
        webAppSettingModel.setAppServicePlanName("");
    }
}
