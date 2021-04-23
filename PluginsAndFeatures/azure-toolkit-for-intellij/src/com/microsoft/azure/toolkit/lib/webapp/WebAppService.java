/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.lib.webapp;

import com.microsoft.azure.toolkit.intellij.common.Draft;
import com.microsoft.azure.toolkit.lib.appservice.MonitorConfig;
import com.microsoft.azure.toolkit.lib.appservice.Platform;
import com.microsoft.azure.toolkit.lib.appservice.model.Runtime;
import com.microsoft.azure.toolkit.lib.appservice.model.WebContainer;
import com.microsoft.azure.toolkit.lib.appservice.service.IWebApp;
import com.microsoft.azure.toolkit.lib.common.operation.AzureOperation;
import com.microsoft.azuretools.azurecommons.helpers.NotNull;
import com.microsoft.azuretools.core.mvp.model.webapp.AzureWebAppMvpModel;
import com.microsoft.azuretools.core.mvp.model.webapp.WebAppSettingModel;
import com.microsoft.azuretools.telemetrywrapper.ErrorType;
import com.microsoft.azuretools.telemetrywrapper.EventUtil;
import com.microsoft.azuretools.telemetrywrapper.Operation;
import com.microsoft.azuretools.telemetrywrapper.TelemetryManager;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static com.microsoft.azuretools.telemetry.TelemetryConstants.CREATE_WEBAPP;
import static com.microsoft.azuretools.telemetry.TelemetryConstants.WEBAPP;

public class WebAppService {
    private static final WebAppService instance = new WebAppService();

    public static WebAppService getInstance() {
        return WebAppService.instance;
    }

    @AzureOperation(name = "webapp.create_detail", params = {"config.getName()"}, type = AzureOperation.Type.SERVICE)
    public IWebApp createWebApp(final WebAppConfig config) {
        final WebAppSettingModel settings = convertConfig2Settings(config);
        settings.setCreatingNew(true);
        final Map<String, String> properties = settings.getTelemetryProperties(null);
        final Operation operation = TelemetryManager.createOperation(WEBAPP, CREATE_WEBAPP);
        try {
            operation.start();
            operation.trackProperties(properties);
            return AzureWebAppMvpModel.getInstance().createWebAppFromSettingModel(settings);
        } catch (final RuntimeException e) {
            EventUtil.logError(operation, ErrorType.userError, e, properties, null);
            throw e;
        } finally {
            operation.complete();
        }
    }

    @AzureOperation(
        name = "webapp.init_config",
        type = AzureOperation.Type.TASK
    )
    public static WebAppSettingModel convertConfig2Settings(final WebAppConfig config) {
        final WebAppSettingModel settings = new WebAppSettingModel();
        settings.setSubscriptionId(config.getSubscription().subscriptionId());
        // creating if id is empty
        settings.setCreatingResGrp(config.getResourceGroup() instanceof Draft || StringUtils.isEmpty(config.getResourceGroup().id()));
        settings.setResourceGroup(config.getResourceGroup().name());
        settings.setWebAppName(config.getName());
        settings.setRegion(config.getRegion().name());
        settings.saveRuntime(getRuntimeFromWebAppConfig(config.getPlatform()));
        // creating if id is empty
        settings.setCreatingAppServicePlan(config.getServicePlan() instanceof Draft || StringUtils.isEmpty(config.getServicePlan().id()));
        if (settings.isCreatingAppServicePlan()) {
            settings.setAppServicePlanName(config.getServicePlan().name());
        } else {
            settings.setAppServicePlanId(config.getServicePlan().id());
        }
        settings.setPricing(config.getServicePlan().pricingTier().toString());
        final MonitorConfig monitorConfig = config.getMonitorConfig();
        if (monitorConfig != null) {
            settings.setEnableApplicationLog(monitorConfig.isEnableApplicationLog());
            settings.setApplicationLogLevel(monitorConfig.getApplicationLogLevel() == null ? null :
                                            monitorConfig.getApplicationLogLevel().toString());
            settings.setEnableWebServerLogging(monitorConfig.isEnableWebServerLogging());
            settings.setWebServerLogQuota(monitorConfig.getWebServerLogQuota());
            settings.setWebServerRetentionPeriod(monitorConfig.getWebServerRetentionPeriod());
            settings.setEnableDetailedErrorMessage(monitorConfig.isEnableDetailedErrorMessage());
            settings.setEnableFailedRequestTracing(monitorConfig.isEnableFailedRequestTracing());
        }
        settings.setTargetName(config.getApplication() == null ? null : config.getApplication().toFile().getName());
        settings.setTargetPath(config.getApplication() == null ? null : config.getApplication().toString());
        return settings;
    }

    private static Runtime getRuntimeFromWebAppConfig(@NotNull final Platform platform) {
        final com.microsoft.azure.toolkit.lib.appservice.model.OperatingSystem operatingSystem =
            com.microsoft.azure.toolkit.lib.appservice.model.OperatingSystem.fromString(platform.getOs().name());
        if (operatingSystem == com.microsoft.azure.toolkit.lib.appservice.model.OperatingSystem.LINUX) {
            return Runtime.getRuntimeFromLinuxFxVersion(String.join(" ", platform.getStackOrWebContainer(), platform.getStackVersionOrJavaVersion()));
        }
        final WebContainer webContainer = WebContainer.fromString(platform.getStackOrWebContainer());
        final com.microsoft.azure.toolkit.lib.appservice.model.JavaVersion javaVersion =
            com.microsoft.azure.toolkit.lib.appservice.model.JavaVersion.fromString(platform.getStackVersionOrJavaVersion());
        return Runtime.getRuntime(com.microsoft.azure.toolkit.lib.appservice.model.OperatingSystem.WINDOWS, webContainer, javaVersion);
    }
}
