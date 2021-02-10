/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.intellij.springcloud.runner.deploy;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.microsoft.azure.management.appplatform.v2020_07_01.ProvisioningState;
import com.microsoft.azure.management.appplatform.v2020_07_01.RuntimeVersion;
import com.microsoft.azure.management.appplatform.v2020_07_01.ServiceResource;
import com.microsoft.azuretools.core.mvp.model.AzureMvpModel;
import com.microsoft.azuretools.core.mvp.model.springcloud.AzureSpringCloudMvpModel;
import com.microsoft.azuretools.core.mvp.model.springcloud.SpringCloudIdHelper;
import com.microsoft.azuretools.utils.JsonUtils;
import com.microsoft.azure.toolkit.intellij.common.AzureRunConfigurationBase;
import com.microsoft.azure.toolkit.intellij.springcloud.runner.SpringCloudModel;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SpringCloudDeployConfiguration extends AzureRunConfigurationBase<SpringCloudModel> {
    private static final String NEED_SPECIFY_ARTIFACT = "Please select an artifact";
    private static final String NEED_SPECIFY_SUBSCRIPTION = "Please select your subscription.";
    private static final String NEED_SPECIFY_CLUSTER = "Please select a target cluster.";
    private static final String NEED_SPECIFY_APP_NAME = "Please select a target app.";
    private static final String SERVICE_IS_NOT_READY = "Service is not ready for deploy, current status is ";
    private static final String TARGET_CLUSTER_DOES_NOT_EXISTS = "Target cluster does not exists.";
    private static final String TARGET_CLUSTER_IS_NOT_AVAILABLE = "Target cluster cannot be found in current subscription";

    private static LoadingCache<String, ServiceResource> clusterStatusCache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<String, ServiceResource>() {
                public ServiceResource load(String clusterId) throws IOException {
                    final String subscriptionId = AzureMvpModel.getSegment(clusterId, "subscriptions");
                    return AzureSpringCloudMvpModel.getClusterById(subscriptionId, clusterId);
                }
            });

    private final SpringCloudModel springCloudModel;

    public SpringCloudDeployConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, String name) {
        super(project, factory, name);
        springCloudModel = new SpringCloudModel();
    }

    protected SpringCloudDeployConfiguration(@NotNull SpringCloudDeployConfiguration source) {
        super(source);
        this.springCloudModel = JsonUtils.deepCopyWithJson(source.getModel());
    }

    public String getArtifactIdentifier() {
        return springCloudModel.getArtifactIdentifier();
    }

    public void setArtifactIdentifier(final String artifactIdentifier) {
        springCloudModel.setArtifactIdentifier(artifactIdentifier);
    }

    @Override
    public SpringCloudModel getModel() {
        return this.springCloudModel;
    }

    @Override
    public String getTargetName() {
        return null;
    }

    @Override
    public String getTargetPath() {
        // we need the jar built by <spring-boot-maven-plugin>, not the default output jar
        return null;
    }

    @Override
    public String getSubscriptionId() {
        return springCloudModel.getSubscriptionId();
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new SpringCloudDeploymentSettingEditor(getProject(), this);
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) {
        return new SpringCloudDeploymentState(getProject(), new SpringCloudDeployConfiguration(this));
    }

    public boolean isPublic() {
        return springCloudModel.isPublic();
    }

    public String getClusterId() {
        return springCloudModel.getClusterId();
    }

    public boolean isCreateNewApp() {
        return springCloudModel.isCreateNewApp();
    }

    public String getAppName() {
        return springCloudModel.getAppName();
    }

    public RuntimeVersion getRuntimeVersion() {
        final String runtimeString = springCloudModel.getRuntimeVersion();
        return StringUtils.isEmpty(runtimeString) ? null : RuntimeVersion.fromString(runtimeString);
    }

    public Integer getCpu() {
        return springCloudModel.getCpu();
    }

    public Integer getMemoryInGB() {
        return springCloudModel.getMemoryInGB();
    }

    public Integer getInstanceCount() {
        return springCloudModel.getInstanceCount();
    }

    public String getJvmOptions() {
        return springCloudModel.getJvmOptions();
    }

    public String getResourceGroup() {
        final String clusterId = getClusterId();
        return StringUtils.isEmpty(clusterId) ? null : SpringCloudIdHelper.getResourceGroup(clusterId);
    }

    public String getClusterName() {
        final String clusterId = getClusterId();
        return StringUtils.isEmpty(clusterId) ? null : SpringCloudIdHelper.getClusterName(clusterId);
    }

    public boolean isEnablePersistentStorage() {
        return springCloudModel.isEnablePersistentStorage();
    }

    public Map<String, String> getEnvironment() {
        return springCloudModel.getEnvironment();
    }

    public void setPublic(boolean isPublic) {
        springCloudModel.setPublic(isPublic);
    }

    public void setSubscriptionId(String subscriptionId) {
        springCloudModel.setSubscriptionId(subscriptionId);
    }

    public void setClusterId(String clusterId) {
        springCloudModel.setClusterId(clusterId);
    }

    public void setAppName(String appName) {
        springCloudModel.setAppName(appName);
    }

    public void setCreateNewApp(boolean isNewApp) {
        springCloudModel.setCreateNewApp(isNewApp);
    }

    public void saveRuntimeVersion(RuntimeVersion runtimeVersion) {
        springCloudModel.setRuntimeVersion(Objects.toString(runtimeVersion, null));
    }

    public void setCpu(Integer cpu) {
        springCloudModel.setCpu(cpu);
    }

    public void setMemoryInGB(Integer memoryInGB) {
        springCloudModel.setMemoryInGB(memoryInGB);
    }

    public void setInstanceCount(Integer instanceCount) {
        springCloudModel.setInstanceCount(instanceCount);
    }

    public void setJvmOptions(String jvmOptions) {
        springCloudModel.setJvmOptions(jvmOptions);
    }

    public void setEnablePersistentStorage(boolean enablePersistentStorage) {
        springCloudModel.setEnablePersistentStorage(enablePersistentStorage);
    }

    public void setEnvironment(Map<String, String> environment) {
        springCloudModel.setEnvironment(environment);
    }

    @Override
    public void validate() throws ConfigurationException {
        checkAzurePreconditions();
        if (StringUtils.isEmpty(getArtifactIdentifier())) {
            throw new ConfigurationException(NEED_SPECIFY_ARTIFACT);
        }
        if (StringUtils.isEmpty(getSubscriptionId())) {
            throw new ConfigurationException(NEED_SPECIFY_SUBSCRIPTION);
        }
        if (StringUtils.isEmpty(getClusterId())) {
            throw new ConfigurationException(NEED_SPECIFY_CLUSTER);
        }
        if (StringUtils.isEmpty(getAppName())) {
            throw new ConfigurationException(NEED_SPECIFY_APP_NAME);
        }
        final ServiceResource serviceResource;
        try {
            serviceResource = clusterStatusCache.get(getClusterId());
            if (serviceResource == null) {
                throw new ConfigurationException(TARGET_CLUSTER_DOES_NOT_EXISTS);
            }
            // SDK will return null inner service object if cluster exists in other subscription
            if (serviceResource.inner() == null) {
                throw new ConfigurationException(TARGET_CLUSTER_IS_NOT_AVAILABLE);
            }
            final ProvisioningState provisioningState = serviceResource.properties().provisioningState();
            if (provisioningState != ProvisioningState.SUCCEEDED) {
                throw new ConfigurationException(SERVICE_IS_NOT_READY + provisioningState.toString());
            }
        } catch (ExecutionException e) {
            // swallow validation exceptions
        }
    }

}
