/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */
package com.microsoft.azure.toolkit.intellij.appservice;

import com.google.gson.JsonObject;
import com.intellij.openapi.project.Project;
import com.microsoft.azure.management.applicationinsights.v2015_05_01.ApplicationInsightsComponent;
import com.microsoft.azure.management.appservice.*;
import com.microsoft.azure.management.resources.fluentcore.arm.ResourceUtils;
import com.microsoft.azure.toolkit.lib.Azure;
import com.microsoft.azure.toolkit.lib.appservice.AzureAppService;
import com.microsoft.azure.toolkit.lib.appservice.model.DiagnosticConfig;
import com.microsoft.azure.toolkit.lib.appservice.service.*;
import com.microsoft.azure.toolkit.lib.common.exception.AzureToolkitRuntimeException;
import com.microsoft.azure.toolkit.lib.common.operation.AzureOperation;
import com.microsoft.azure.toolkit.lib.common.operation.AzureOperationBundle;
import com.microsoft.azure.toolkit.lib.common.operation.IAzureOperationTitle;
import com.microsoft.azure.toolkit.lib.common.task.AzureTask;
import com.microsoft.azure.toolkit.lib.common.task.AzureTaskManager;
import com.microsoft.azuretools.authmanage.AuthMethodManager;
import com.microsoft.azuretools.authmanage.models.SubscriptionDetail;
import com.microsoft.azuretools.core.mvp.model.AzureMvpModel;
import com.microsoft.azuretools.core.mvp.model.function.AzureFunctionMvpModel;
import com.microsoft.azuretools.sdkmanage.AzureManager;
import com.microsoft.intellij.util.PluginUtil;
import com.microsoft.tooling.msservices.components.DefaultLoader;
import com.microsoft.tooling.msservices.helpers.azure.sdk.AzureSDKManager;
import hu.akarnokd.rxjava3.interop.RxJavaInterop;
import org.apache.commons.lang3.StringUtils;
import reactor.adapter.rxjava.RxJava3Adapter;
import rx.Observable;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.microsoft.intellij.ui.messages.AzureBundle.message;


public enum AppServiceStreamingLogManager {
    INSTANCE;

    private static final String STREAMING_LOG_NOT_STARTED = message("appService.logStreaming.hint.notStart");
    private static final String FAILED_TO_START_STREAMING_LOG = message("appService.logStreaming.error.startFailed");
    private static final String FAILED_TO_CLOSE_STREAMING_LOG = message("appService.logStreaming.error.closeFailed");
    private static final String ENABLE_LOGGING = "Enable logging";
    private static final String NOT_SUPPORTED = "Not supported";
    private static final String SITES = "sites";
    private static final String SLOTS = "slots";
    private static final String SUBSCRIPTIONS = "subscriptions";
    private static final String[] YES_NO = {"Yes", "No"};

    private Map<String, AppServiceStreamingLogConsoleView> consoleViewMap = new HashMap<>();

    public void showWebAppDeploymentSlotStreamingLog(Project project, String slotId) {
        showAppServiceStreamingLog(project, slotId, new WebAppSlotLogStreaming(slotId));
    }

    public void showWebAppStreamingLog(Project project, String webAppId) {
        showAppServiceStreamingLog(project, webAppId, new WebAppLogStreaming(webAppId));
    }

    public void showFunctionStreamingLog(Project project, String functionId) {
        showAppServiceStreamingLog(project, functionId, new FunctionLogStreaming(functionId));
    }

    @AzureOperation(name = "appservice|log_stream.close", params = {"nameFromResourceId(appId)"}, type = AzureOperation.Type.SERVICE)
    public void closeStreamingLog(Project project, String appId) {
        final IAzureOperationTitle title = AzureOperationBundle.title("appservice|log_stream.close", ResourceUtils.nameFromResourceId(appId));
        AzureTaskManager.getInstance().runInBackground(new AzureTask(project, title, false, () -> {
            if (consoleViewMap.containsKey(appId) && consoleViewMap.get(appId).isActive()) {
                consoleViewMap.get(appId).closeStreamingLog();
            } else {
                DefaultLoader.getIdeHelper().invokeLater(() -> PluginUtil.displayErrorDialog(
                        FAILED_TO_CLOSE_STREAMING_LOG, STREAMING_LOG_NOT_STARTED));
            }
        }));
    }

    @AzureOperation(name = "appservice|log_stream.open", params = {"nameFromResourceId(resourceId)"}, type = AzureOperation.Type.SERVICE)
    private void showAppServiceStreamingLog(Project project, String resourceId, ILogStreaming logStreaming) {
        final IAzureOperationTitle title = AzureOperationBundle.title("appservice|log_stream.open", ResourceUtils.nameFromResourceId(resourceId));
        AzureTaskManager.getInstance().runInBackground(new AzureTask(project, title, false, () -> {
            try {
                final String name = logStreaming.getTitle();
                final AppServiceStreamingLogConsoleView consoleView = getOrCreateConsoleView(project, resourceId);
                if (!consoleView.isActive()) {
                    if (!logStreaming.isLogStreamingSupported()) {
                        DefaultLoader.getIdeHelper().invokeLater(() -> PluginUtil.displayInfoDialog(
                                NOT_SUPPORTED, message("appService.logStreaming.hint.notSupport", name)));
                        return;
                    }
                    if (!logStreaming.isLogStreamingEnabled()) {
                        // Enable Log Streaming if log streaming of target is not enabled
                        final boolean userInput = DefaultLoader.getUIHelper().showConfirmation(
                            message("appService.logStreaming.hint.enablePrompt", name), ENABLE_LOGGING, YES_NO, null);
                        if (userInput) {
                            logStreaming.enableLogStreaming();
                        } else {
                            return;
                        }
                    }
                    final Observable<String> log = logStreaming.getStreamingLogContent();
                    if (log == null) {
                        return;
                    }
                    consoleView.startStreamingLog(log);
                }
                StreamingLogsToolWindowManager.getInstance().showStreamingLogConsole(
                        project, resourceId, logStreaming.getTitle(), consoleView);
            } catch (Throwable e) {
                throw new AzureToolkitRuntimeException("failed to open streaming log", e);
            }
        }));
    }

    private AppServiceStreamingLogConsoleView getOrCreateConsoleView(Project project, String resourceId) {
        return consoleViewMap.compute(resourceId, (id, view) -> {
            return (view == null || view.isDisposed()) ? new AppServiceStreamingLogConsoleView(project, id) : view;
        });
    }

    interface ILogStreaming {
        default boolean isLogStreamingSupported() throws IOException {
            return true;
        }

        boolean isLogStreamingEnabled() throws IOException;

        void enableLogStreaming() throws IOException;

        String getTitle() throws IOException;

        Observable<String> getStreamingLogContent() throws IOException;
    }

    static class FunctionLogStreaming implements ILogStreaming {

        private static final String APPINSIGHTS_INSTRUMENTATIONKEY = "APPINSIGHTS_INSTRUMENTATIONKEY";
        private static final String APPLICATION_INSIGHT_PATTERN = "%s/#blade/AppInsightsExtension/QuickPulseBladeV2/ComponentId/%s/ResourceId/%s";
        private static final String MUST_CONFIGURE_APPLICATION_INSIGHTS = message("appService.logStreaming.error.noApplicationInsights");

        private String resourceId;
        private FunctionApp functionApp;

        FunctionLogStreaming(final String resourceId) {
            this.resourceId = resourceId;
        }

        @Override
        public boolean isLogStreamingEnabled() throws IOException {
            return getFunctionApp().operatingSystem() == OperatingSystem.LINUX ?
                   true : AzureFunctionMvpModel.isApplicationLogEnabled(getFunctionApp());
        }

        @Override
        public void enableLogStreaming() throws IOException {
            AzureFunctionMvpModel.enableApplicationLog(getFunctionApp());
        }

        @Override
        public String getTitle() {
            return AzureMvpModel.getSegment(resourceId, SITES);
        }

        @Override
        public Observable<String> getStreamingLogContent() throws IOException {
            if (getFunctionApp().operatingSystem() == OperatingSystem.LINUX) {
                // For linux function, we will just open the "Live Metrics Stream" view in the portal
                openLiveMetricsStream();
                return null;
            }
            return getFunctionApp().streamAllLogsAsync();
        }

        // Refers https://github.com/microsoft/vscode-azurefunctions/blob/v0.22.0/src/
        // commands/logstream/startStreamingLogs.ts#L53
        private void openLiveMetricsStream() throws IOException {
            final AppSetting aiAppSettings = functionApp.getAppSettings().get(APPINSIGHTS_INSTRUMENTATIONKEY);
            if (aiAppSettings == null) {
                throw new IOException(MUST_CONFIGURE_APPLICATION_INSIGHTS);
            }
            final String aiKey = aiAppSettings.value();
            final String subscriptionId = AzureMvpModel.getSegment(resourceId, SUBSCRIPTIONS);
            final AzureManager azureManager = AuthMethodManager.getInstance().getAzureManager();
            final SubscriptionDetail subscriptionDetail = azureManager.getSubscriptionManager()
                                                                      .getSubscriptionIdToSubscriptionDetailsMap()
                                                                      .get(subscriptionId);
            final List<ApplicationInsightsComponent> insightsResources =
                    AzureSDKManager.getInsightsResources(subscriptionDetail);
            final ApplicationInsightsComponent target = insightsResources
                    .stream()
                    .filter(aiResource -> StringUtils.equals(aiResource.instrumentationKey(), aiKey))
                    .findFirst()
                    .orElseThrow(() -> new IOException(message("appService.logStreaming.error.aiNotFound", subscriptionId)));
            final String aiUrl = getApplicationInsightLiveMetricsUrl(target, azureManager.getPortalUrl());
            DefaultLoader.getIdeHelper().openLinkInBrowser(aiUrl);
        }

        private String getApplicationInsightLiveMetricsUrl(ApplicationInsightsComponent target, String portalUrl)
                throws UnsupportedEncodingException {
            final JsonObject componentObject = new JsonObject();
            componentObject.addProperty("Name", target.name());
            componentObject.addProperty("SubscriptionId", AzureMvpModel.getSegment(target.id(), SUBSCRIPTIONS));
            componentObject.addProperty("ResourceGroup", target.resourceGroupName());
            final String componentId = URLEncoder.encode(componentObject.toString(), "utf-8");
            final String aiResourceId = URLEncoder.encode(target.id(), "utf-8");
            return String.format(APPLICATION_INSIGHT_PATTERN, portalUrl, componentId, aiResourceId);
        }

        private FunctionApp getFunctionApp() throws IOException {
            if (functionApp == null) {
                functionApp = AzureFunctionMvpModel.getInstance().getFunctionById(
                        AzureMvpModel.getSegment(resourceId, SUBSCRIPTIONS), resourceId);
            }
            return functionApp;
        }
    }

    static class WebAppLogStreaming implements ILogStreaming {
        private final IWebApp webApp;

        public WebAppLogStreaming(String resourceId) {
            this.webApp = Azure.az(AzureAppService.class).webapp(resourceId);
        }

        @Override
        public boolean isLogStreamingEnabled() {
            return webApp.getDiagnosticConfig().isEnableWebServerLogging();
        }

        @Override
        public void enableLogStreaming() {
            final DiagnosticConfig diagnosticConfig = webApp.getDiagnosticConfig();
            diagnosticConfig.setEnableWebServerLogging(true);
            webApp.update().withDiagnosticConfig(diagnosticConfig).commit();
        }

        @Override
        public String getTitle() {
            return AzureMvpModel.getSegment(webApp.id(), SITES);
        }

        @Override
        public Observable<String> getStreamingLogContent() {
            return RxJavaInterop.toV1Observable(RxJava3Adapter.fluxToFlowable(webApp.streamAllLogsAsync()));
        }
    }

    static class WebAppSlotLogStreaming implements ILogStreaming {
        private IWebAppDeploymentSlot deploymentSlot;

        public WebAppSlotLogStreaming(String resourceId) {
            this.deploymentSlot = Azure.az(AzureAppService.class).deploymentSlot(resourceId);
        }

        @Override
        public boolean isLogStreamingEnabled() {
            return deploymentSlot.getDiagnosticConfig().isEnableWebServerLogging();
        }

        @Override
        public void enableLogStreaming() {
            final DiagnosticConfig diagnosticConfig = deploymentSlot.getDiagnosticConfig();
            diagnosticConfig.setEnableWebServerLogging(true);
            deploymentSlot.update().withDiagnosticConfig(diagnosticConfig).commit();
        }

        @Override
        public String getTitle() {
            return AzureMvpModel.getSegment(deploymentSlot.id(), SLOTS);
        }

        @Override
        public Observable<String> getStreamingLogContent() {
            return RxJavaInterop.toV1Observable(RxJava3Adapter.fluxToFlowable(deploymentSlot.streamAllLogsAsync()));
        }
    }
}
