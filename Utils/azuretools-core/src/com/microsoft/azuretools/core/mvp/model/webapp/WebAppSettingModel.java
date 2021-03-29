/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azuretools.core.mvp.model.webapp;

import com.microsoft.azure.management.appservice.JavaVersion;
import com.microsoft.azure.management.appservice.LogLevel;
import com.microsoft.azure.management.appservice.OperatingSystem;
import com.microsoft.azure.management.appservice.RuntimeStack;
import com.microsoft.azure.toolkit.lib.appservice.model.Runtime;
import com.microsoft.azure.toolkit.lib.appservice.model.WebContainer;
import com.microsoft.azuretools.telemetry.TelemetryConstants;
import com.microsoft.azuretools.utils.WebAppUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
public class WebAppSettingModel {

    // common settings
    private boolean creatingNew = false;
    private String subscriptionId = "";
    // deploy related
    private String webAppId = "";
    private String targetPath = "";
    private String targetName = "";
    private String projectBase = "";
    private String projectType = "";
    private boolean deployToRoot = true;
    private boolean deployToSlot = false;
    private String slotName;
    private String newSlotName;
    private String newSlotConfigurationSource;
    // create related
    private String webAppName = "";
    private boolean creatingResGrp = false;
    private String resourceGroup = "";
    private boolean creatingAppServicePlan = false;
    private String appServicePlanName = "";
    private String appServicePlanId = "";
    private String region = "";
    private String pricing = "";
    @Deprecated
    private String webContainer = "";
    @Deprecated
    private OperatingSystem os = OperatingSystem.LINUX;
    @Deprecated
    private JavaVersion jdkVersion = JavaVersion.JAVA_8_NEWEST;
    @Deprecated
    private String stack = "TOMCAT";
    @Deprecated
    private String version = "8.5-jre8";

    // todo: change string values to app service library model
    private String operatingSystem;
    private String webAppContainer;
    private String webAppJavaVersion;

    // web server log
    private boolean enableWebServerLogging = false;
    private Integer webServerLogQuota = 35;
    private Integer webServerRetentionPeriod = null;
    private boolean enableDetailedErrorMessage = false;
    private boolean enableFailedRequestTracing = false;
    // application log
    private boolean enableApplicationLog = false;
    private String applicationLogLevel = LogLevel.ERROR.toString();

    @Deprecated
    public RuntimeStack getLinuxRuntime() {
        return new RuntimeStack(this.stack, this.version);
    }

    @Deprecated
    public OperatingSystem getOS() {
        return this.os;
    }

    @Deprecated
    public void setOS(OperatingSystem os) {
        this.os = os;
    }

    public Runtime getRuntime() {
        if (StringUtils.isAllEmpty(operatingSystem, webAppContainer, webAppJavaVersion)) {
            return parseRuntimeFromDeprecatedConfiguration();
        }
        final com.microsoft.azure.toolkit.lib.appservice.model.OperatingSystem system =
                com.microsoft.azure.toolkit.lib.appservice.model.OperatingSystem.fromString(operatingSystem);
        final WebContainer container = WebContainer.fromString(webAppContainer);
        final com.microsoft.azure.toolkit.lib.appservice.model.JavaVersion javaVersion =
                com.microsoft.azure.toolkit.lib.appservice.model.JavaVersion.fromString(webAppJavaVersion);
        return Runtime.getRuntime(system, container, javaVersion);
    }

    public void saveRuntime(Runtime runtime) {
        this.operatingSystem = runtime.getOperatingSystem().getValue();
        this.webAppContainer = runtime.getWebContainer().getValue();
        this.webAppJavaVersion = runtime.getJavaVersion().getValue();
    }

    private Runtime parseRuntimeFromDeprecatedConfiguration() {
        final com.microsoft.azure.toolkit.lib.appservice.model.OperatingSystem system =
                com.microsoft.azure.toolkit.lib.appservice.model.OperatingSystem.fromString(this.os.name());
        if (system == com.microsoft.azure.toolkit.lib.appservice.model.OperatingSystem.LINUX) {
            return Runtime.getRuntimeFromLinuxFxVersion(getLinuxRuntime().toString());
        }
        final com.microsoft.azure.toolkit.lib.appservice.model.JavaVersion javaVersion =
                com.microsoft.azure.toolkit.lib.appservice.model.JavaVersion.fromString(getJdkVersion().toString());
        final com.microsoft.azure.toolkit.lib.appservice.model.WebContainer container =
                com.microsoft.azure.toolkit.lib.appservice.model.WebContainer.fromString(getWebContainer());
        return Runtime.getRuntime(com.microsoft.azure.toolkit.lib.appservice.model.OperatingSystem.WINDOWS, container, javaVersion);
    }

    public Map<String, String> getTelemetryProperties(Map<String, String> properties) {
        Map<String, String> result = new HashMap<>();
        try {
            if (properties != null) {
                result.putAll(properties);
            }
            result.put(TelemetryConstants.RUNTIME, os == OperatingSystem.LINUX ?
                    "linux-" + getLinuxRuntime().toString() : "windows-" + getWebContainer() + (jdkVersion == null ? "" : "-" + jdkVersion.toString()));
            result.put(TelemetryConstants.WEBAPP_DEPLOY_TO_SLOT, String.valueOf(isDeployToSlot()));
            result.put(TelemetryConstants.SUBSCRIPTIONID, getSubscriptionId());
            result.put(TelemetryConstants.CREATE_NEWWEBAPP, String.valueOf(isCreatingNew()));
            result.put(TelemetryConstants.CREATE_NEWASP, String.valueOf(isCreatingAppServicePlan()));
            result.put(TelemetryConstants.CREATE_NEWRG, String.valueOf(isCreatingResGrp()));
            result.put(TelemetryConstants.FILETYPE, WebAppUtils.getFileType(getTargetName()));
            result.put(TelemetryConstants.PRICING_TIER, pricing);
        } catch (final Exception ignore) {
        }
        return result;
    }

}
