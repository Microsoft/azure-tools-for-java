/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.intellij.function.runner.deploy;

import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.toolkit.intellij.common.AzureRunProfileState;
import com.microsoft.azure.toolkit.intellij.common.messager.IntellijAzureMessager;
import com.microsoft.azure.toolkit.intellij.function.runner.core.FunctionUtils;
import com.microsoft.azure.toolkit.lib.Azure;
import com.microsoft.azure.toolkit.lib.appservice.AzureAppService;
import com.microsoft.azure.toolkit.lib.appservice.entity.FunctionEntity;
import com.microsoft.azure.toolkit.lib.appservice.service.IFunctionApp;
import com.microsoft.azure.toolkit.lib.common.exception.AzureExecutionException;
import com.microsoft.azure.toolkit.lib.common.exception.AzureToolkitRuntimeException;
import com.microsoft.azure.toolkit.lib.common.messager.AzureMessager;
import com.microsoft.azure.toolkit.lib.common.operation.AzureOperation;
import com.microsoft.azure.toolkit.lib.common.task.AzureTaskManager;
import com.microsoft.azure.toolkit.lib.common.telemetry.AzureTelemetry;
import com.microsoft.azure.toolkit.lib.function.FunctionAppConfig;
import com.microsoft.azure.toolkit.lib.function.FunctionAppService;
import com.microsoft.azure.toolkit.lib.legacy.function.configurations.FunctionConfiguration;
import com.microsoft.azuretools.telemetry.TelemetryConstants;
import com.microsoft.azuretools.telemetrywrapper.Operation;
import com.microsoft.azuretools.telemetrywrapper.TelemetryManager;
import com.microsoft.azuretools.utils.AzureUIRefreshCore;
import com.microsoft.azuretools.utils.AzureUIRefreshEvent;
import com.microsoft.intellij.RunProcessHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.microsoft.intellij.ui.messages.AzureBundle.message;

public class FunctionDeploymentState extends AzureRunProfileState<IFunctionApp> {

    private static final int LIST_TRIGGERS_MAX_RETRY = 3;
    private static final int LIST_TRIGGERS_RETRY_PERIOD_IN_SECONDS = 10;
    private static final String AUTH_LEVEL = "authLevel";
    private static final String HTTP_TRIGGER = "httpTrigger";
    private static final String HTTP_TRIGGER_URLS = "HTTP Trigger Urls:";
    private static final String NO_ANONYMOUS_HTTP_TRIGGER = "No anonymous HTTP Triggers found in deployed function app, skip list triggers.";
    private static final String UNABLE_TO_LIST_NONE_ANONYMOUS_HTTP_TRIGGERS = "Some http trigger urls cannot be displayed " +
            "because they are non-anonymous. To access the non-anonymous triggers, please refer https://aka.ms/azure-functions-key.";
    private static final String FAILED_TO_LIST_TRIGGERS = "Deployment succeeded, but failed to list http trigger urls.";
    private static final String SYNCING_TRIGGERS_AND_FETCH_FUNCTION_INFORMATION = "Syncing triggers and fetching function information...";

    private final FunctionDeployConfiguration functionDeployConfiguration;
    private final FunctionDeployModel deployModel;
    private File stagingFolder;

    /**
     * Place to execute the Web App deployment task.
     */
    public FunctionDeploymentState(Project project, FunctionDeployConfiguration functionDeployConfiguration) {
        super(project);
        this.functionDeployConfiguration = functionDeployConfiguration;
        this.deployModel = functionDeployConfiguration.getModel();
    }

    @Nullable
    @Override
    @AzureOperation(name = "function.deploy.state", type = AzureOperation.Type.ACTION)
    public IFunctionApp executeSteps(@NotNull RunProcessHandler processHandler, @NotNull Operation operation) throws IOException {
        final FunctionDeploymentMessenger messenger = new FunctionDeploymentMessenger(processHandler);
        AzureMessager.getContext().setMessager(messenger);
        final IFunctionApp functionApp;
        if (StringUtils.isEmpty(functionDeployConfiguration.getFunctionId())) {
            functionApp = createFunctionApp(processHandler);
        } else {
            functionApp = Azure.az(AzureAppService.class).subscription(functionDeployConfiguration.getSubscriptionId())
                    .functionApp(functionDeployConfiguration.getFunctionId());
        }
        stagingFolder = FunctionUtils.getTempStagingFolder();
        prepareStagingFolder(stagingFolder, processHandler, operation);
        // deploy function to Azure
        FunctionAppService.getInstance().deployFunctionApp(functionApp, stagingFolder);
        // list triggers after deployment
        listHTTPTriggerUrls(functionApp);
        operation.trackProperties(AzureTelemetry.getActionContext().getProperties());
        return functionApp;
    }

    private IFunctionApp createFunctionApp(@NotNull RunProcessHandler processHandler) {
        IFunctionApp functionApp = Azure.az(AzureAppService.class)
                .subscription(functionDeployConfiguration.getSubscriptionId())
                .functionApp(functionDeployConfiguration.getConfig().getResourceGroup().getName(), functionDeployConfiguration.getAppName());
        if (functionApp.exists()) {
            return functionApp;
        }
        processHandler.setText(message("function.create.hint.creating", functionDeployConfiguration.getAppName()));
        // Load app settings from security storage
        final FunctionAppConfig config = deployModel.getFunctionAppConfig();
        config.setAppSettings(FunctionUtils.loadAppSettingsFromSecurityStorage(functionDeployConfiguration.getAppSettingsKey()));
        // create function app
        functionApp = FunctionAppService.getInstance().createFunctionApp(config);
        // update run configuration
        functionDeployConfiguration.setFunctionId(functionApp.id());
        // Notify explorer refresh
        AzureUIRefreshCore.execute(new AzureUIRefreshEvent(AzureUIRefreshEvent.EventType.REFRESH, functionApp));
        processHandler.setText(message("function.create.hint.created", functionDeployConfiguration.getAppName()));
        return functionApp;
    }

    @AzureOperation(
            name = "function.prepare_staging_folder_detail",
            params = {"stagingFolder.getName()", "this.deployModel.getAppName()"},
            type = AzureOperation.Type.TASK
    )
    private void prepareStagingFolder(File stagingFolder, RunProcessHandler processHandler, final @NotNull Operation operation) {
        AzureTaskManager.getInstance().read(() -> {
            final Path hostJsonPath = FunctionUtils.getDefaultHostJson(project);
            final PsiMethod[] methods = FunctionUtils.findFunctionsByAnnotation(functionDeployConfiguration.getModule());
            final Path folder = stagingFolder.toPath();
            try {
                final Map<String, FunctionConfiguration> configMap =
                        FunctionUtils.prepareStagingFolder(folder, hostJsonPath, functionDeployConfiguration.getModule(), methods);
                operation.trackProperty(TelemetryConstants.TRIGGER_TYPE, StringUtils.join(FunctionUtils.getFunctionBindingList(configMap), ","));
            } catch (final AzureExecutionException | IOException e) {
                final String error = String.format("failed prepare staging folder[%s]", folder);
                throw new AzureToolkitRuntimeException(error, e);
            }
        });
    }

    @Override
    protected Operation createOperation() {
        return TelemetryManager.createOperation(TelemetryConstants.FUNCTION, TelemetryConstants.DEPLOY_FUNCTION_APP);
    }

    @Override
    @AzureOperation(
            name = "function.complete_deployment",
            params = {"this.deployModel.getAppName()"},
            type = AzureOperation.Type.TASK
    )
    protected void onSuccess(IFunctionApp result, @NotNull RunProcessHandler processHandler) {
        processHandler.setText(message("appService.deploy.hint.succeed"));
        processHandler.notifyComplete();
        FunctionUtils.cleanUpStagingFolder(stagingFolder);
    }

    @Override
    protected void onFail(@NotNull Throwable error, @NotNull RunProcessHandler processHandler) {
        super.onFail(error, processHandler);
        FunctionUtils.cleanUpStagingFolder(stagingFolder);
    }

    @Override
    protected Map<String, String> getTelemetryMap() {
        return deployModel.getTelemetryProperties();
    }

    // todo: create shared run state messenger for all run states
    @RequiredArgsConstructor
    private static class FunctionDeploymentMessenger extends IntellijAzureMessager {
        private final RunProcessHandler processHandler;

        @Override
        public void info(@Nonnull String message, String title) {
            processHandler.setText(message);
        }

        @Override
        public void success(@Nonnull String message, String title) {
            processHandler.println(message, ProcessOutputTypes.SYSTEM);
            super.success(message, title);
        }

        @Override
        public void error(@Nonnull String message, String title) {
            processHandler.println(message, ProcessOutputTypes.STDERR);
            super.error(message, title);
        }

        @Override
        public void warning(@Nonnull String message, String title) {
            processHandler.setText(message);
        }
    }

    private void listHTTPTriggerUrls(IFunctionApp target) {
        try {
            final List<FunctionEntity> triggers = listFunctions(target);
            final List<FunctionEntity> httpFunction = triggers.stream()
                    .filter(function -> function.getTrigger() != null &&
                            org.apache.commons.lang3.StringUtils.equalsIgnoreCase(function.getTrigger().getType(), HTTP_TRIGGER))
                    .collect(Collectors.toList());
            final List<FunctionEntity> anonymousTriggers = httpFunction.stream()
                    .filter(bindingResource -> bindingResource.getTrigger() != null &&
                            org.apache.commons.lang3.StringUtils.equalsIgnoreCase(bindingResource.getTrigger().getProperty(AUTH_LEVEL), AuthorizationLevel.ANONYMOUS.toString()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(httpFunction) || CollectionUtils.isEmpty(anonymousTriggers)) {
                AzureMessager.getMessager().info(NO_ANONYMOUS_HTTP_TRIGGER);
                return;
            }
            AzureMessager.getMessager().info(HTTP_TRIGGER_URLS);
            anonymousTriggers.forEach(trigger -> AzureMessager.getMessager().info(String.format("\t %s : %s", trigger.getName(), trigger.getTriggerUrl())));
            if (anonymousTriggers.size() < httpFunction.size()) {
                AzureMessager.getMessager().info(UNABLE_TO_LIST_NONE_ANONYMOUS_HTTP_TRIGGERS);
            }
        } catch (final RuntimeException e) {
            // show warning instead of exception for list triggers
            AzureMessager.getMessager().warning(FAILED_TO_LIST_TRIGGERS);
        }
    }

    private List<FunctionEntity> listFunctions(final IFunctionApp functionApp) {
        AzureMessager.getMessager().info(SYNCING_TRIGGERS_AND_FETCH_FUNCTION_INFORMATION);
        return Mono.fromCallable(() -> {
            functionApp.syncTriggers();
            return functionApp.listFunctions();
        }).retryWhen(Retry.withThrowable(flux ->
                flux.zipWith(Flux.range(1, LIST_TRIGGERS_MAX_RETRY + 1), (throwable, count) -> {
                    if (count < LIST_TRIGGERS_MAX_RETRY) {
                        return count;
                    } else {
                        return Exceptions.propagate(throwable);
                    }
                }).flatMap(i -> Mono.delay(Duration.ofSeconds((long) i * 10))))).block();
    }
}
