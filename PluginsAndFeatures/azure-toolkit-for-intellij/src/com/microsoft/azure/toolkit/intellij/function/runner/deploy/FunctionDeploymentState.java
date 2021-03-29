/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.intellij.function.runner.deploy;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import com.microsoft.azure.common.exceptions.AzureExecutionException;
import com.microsoft.azure.common.function.configurations.FunctionConfiguration;
import com.microsoft.azure.common.utils.AppServiceUtils;
import com.microsoft.azure.management.appservice.AppServicePlan;
import com.microsoft.azure.management.appservice.FunctionApp;
import com.microsoft.azure.management.appservice.WebAppBase;
import com.microsoft.azure.toolkit.intellij.common.AzureRunProfileState;
import com.microsoft.azure.toolkit.intellij.function.runner.core.FunctionUtils;
import com.microsoft.azure.toolkit.intellij.function.runner.library.function.CreateFunctionHandler;
import com.microsoft.azure.toolkit.intellij.function.runner.library.function.DeployFunctionHandler;
import com.microsoft.azure.toolkit.lib.common.exception.AzureToolkitRuntimeException;
import com.microsoft.azure.toolkit.lib.common.operation.AzureOperation;
import com.microsoft.azure.toolkit.lib.common.task.AzureTaskManager;
import com.microsoft.azuretools.core.mvp.model.function.AzureFunctionMvpModel;
import com.microsoft.azuretools.telemetry.TelemetryConstants;
import com.microsoft.azuretools.telemetrywrapper.Operation;
import com.microsoft.azuretools.telemetrywrapper.TelemetryManager;
import com.microsoft.azuretools.utils.AzureUIRefreshCore;
import com.microsoft.azuretools.utils.AzureUIRefreshEvent;
import com.microsoft.intellij.RunProcessHandler;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import static com.microsoft.intellij.ui.messages.AzureBundle.message;

public class FunctionDeploymentState extends AzureRunProfileState<WebAppBase> {

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
    public WebAppBase executeSteps(@NotNull RunProcessHandler processHandler, @NotNull Operation operation) throws Exception {
        // Update run time information by function app
        final FunctionApp functionApp;
        if (deployModel.isNewResource()) {
            functionApp = createFunctionApp(processHandler, operation);
            functionDeployConfiguration.setFunctionId(functionApp.id());
        } else {
            functionApp = AzureFunctionMvpModel.getInstance()
                                               .getFunctionById(functionDeployConfiguration.getSubscriptionId(), functionDeployConfiguration.getFunctionId());
        }
        final AppServicePlan appServicePlan = AppServiceUtils.getAppServicePlanByAppService(functionApp);
        functionDeployConfiguration.setOs(appServicePlan.operatingSystem().name());
        functionDeployConfiguration.setPricingTier(appServicePlan.pricingTier().toSkuDescription().size());
        // Deploy function to Azure
        stagingFolder = FunctionUtils.getTempStagingFolder();
        deployModel.setDeploymentStagingDirectoryPath(stagingFolder.getPath());
        prepareStagingFolder(stagingFolder, processHandler, operation);
        final DeployFunctionHandler deployFunctionHandler = new DeployFunctionHandler(deployModel, message -> {
            if (processHandler.isProcessRunning()) {
                processHandler.setText(message);
            }
        }, operation);
        return deployFunctionHandler.execute();
    }

    private FunctionApp createFunctionApp(@NotNull RunProcessHandler processHandler, @NotNull Operation operation) {
        FunctionApp functionApp =
                AzureFunctionMvpModel.getInstance().getFunctionByName(functionDeployConfiguration.getSubscriptionId(),
                                                                      functionDeployConfiguration.getResourceGroup(),
                                                                      functionDeployConfiguration.getAppName());
        operation.trackProperty("isCreateNewApp", String.valueOf(functionApp == null));
        if (functionApp != null) {
            functionDeployConfiguration.setNewResource(false);
            return functionApp;
        }
        processHandler.setText(message("function.create.hint.creating", functionDeployConfiguration.getAppName()));
        final CreateFunctionHandler createFunctionHandler = new CreateFunctionHandler(functionDeployConfiguration.getModel(), operation);
        functionApp = createFunctionHandler.execute();
        processHandler.setText(message("function.create.hint.created", functionDeployConfiguration.getAppName()));
        return functionApp;
    }

    @AzureOperation(
        name = "function.prepare_staging_folder_detail",
        params = {"stagingFolder.getName()", "this.deployModel.getAppName()"},
        type = AzureOperation.Type.TASK
    )
    private void prepareStagingFolder(File stagingFolder,
                                      RunProcessHandler processHandler,
                                      final @NotNull Operation operation) {
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
    protected void onSuccess(WebAppBase result, @NotNull RunProcessHandler processHandler) {
        processHandler.setText(message("appService.deploy.hint.succeed"));
        processHandler.notifyComplete();
        FunctionUtils.cleanUpStagingFolder(stagingFolder);
        if (functionDeployConfiguration.isNewResource() && AzureUIRefreshCore.listeners != null) {
            AzureUIRefreshCore.execute(new AzureUIRefreshEvent(AzureUIRefreshEvent.EventType.REFRESH, result));
        }
    }

    @Override
    protected void onFail(@NotNull Throwable error, @NotNull RunProcessHandler processHandler) {
        super.onFail(error, processHandler);
        FunctionUtils.cleanUpStagingFolder(stagingFolder);
    }

    @Override
    protected Map<String, String> getTelemetryMap() {
        return functionDeployConfiguration.getModel().getTelemetryProperties();
    }
}
