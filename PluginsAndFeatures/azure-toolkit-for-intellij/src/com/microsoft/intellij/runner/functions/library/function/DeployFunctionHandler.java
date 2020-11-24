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

package com.microsoft.intellij.runner.functions.library.function;

import com.google.common.base.Preconditions;
import com.microsoft.azure.common.Utils;
import com.microsoft.azure.common.appservice.DeployTargetType;
import com.microsoft.azure.common.appservice.DeploymentType;
import com.microsoft.azure.common.appservice.OperatingSystemEnum;
import com.microsoft.azure.common.deploytarget.DeployTarget;
import com.microsoft.azure.common.exceptions.AzureExecutionException;
import com.microsoft.azure.common.function.configurations.RuntimeConfiguration;
import com.microsoft.azure.common.function.handlers.artifact.DockerArtifactHandler;
import com.microsoft.azure.common.function.handlers.artifact.MSDeployArtifactHandlerImpl;
import com.microsoft.azure.common.function.handlers.artifact.RunFromBlobArtifactHandlerImpl;
import com.microsoft.azure.common.function.handlers.artifact.RunFromZipArtifactHandlerImpl;
import com.microsoft.azure.common.function.model.FunctionResource;
import com.microsoft.azure.common.handlers.ArtifactHandler;
import com.microsoft.azure.common.handlers.artifact.ArtifactHandlerBase;
import com.microsoft.azure.common.handlers.artifact.FTPArtifactHandlerImpl;
import com.microsoft.azure.common.handlers.artifact.ZIPArtifactHandlerImpl;
import com.microsoft.azure.common.utils.AppServiceUtils;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.management.appservice.FunctionApp;
import com.microsoft.azure.management.appservice.FunctionApp.Update;
import com.microsoft.intellij.runner.functions.deploy.FunctionDeployModel;
import com.microsoft.intellij.runner.functions.library.IPrompter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.microsoft.azure.common.appservice.DeploymentType.*;
import static com.microsoft.intellij.ui.messages.AzureBundle.message;

/**
 * Deploy artifacts to target Azure Functions in Azure.
 * Todo: Move the handler to tools-common
 */
public class DeployFunctionHandler {
    private static final int LIST_TRIGGERS_MAX_RETRY = 3;
    private static final int LIST_TRIGGERS_RETRY_PERIOD_IN_SECONDS = 10;
    private static final String FUNCTIONS_WORKER_RUNTIME_NAME = "FUNCTIONS_WORKER_RUNTIME";
    private static final String FUNCTIONS_WORKER_RUNTIME_VALUE = "java";
    private static final String FUNCTIONS_EXTENSION_VERSION_NAME = "FUNCTIONS_EXTENSION_VERSION";
    private static final String FUNCTIONS_EXTENSION_VERSION_VALUE = "~3";
    private static final String AUTH_LEVEL = "authLevel";
    private static final String HTTP_TRIGGER = "httpTrigger";

    private static final OperatingSystemEnum DEFAULT_OS = OperatingSystemEnum.Windows;
    private FunctionDeployModel model;
    private IPrompter prompter;

    public DeployFunctionHandler(FunctionDeployModel model, IPrompter prompter) {
        Preconditions.checkNotNull(model);
        this.model = model;
        this.prompter = prompter;
    }

    public FunctionApp execute() throws Exception {
        final FunctionApp app = getFunctionApp();
        updateFunctionAppSettings(app);
        final DeployTarget deployTarget = new DeployTarget(app, DeployTargetType.FUNCTION);
        prompt(message("function.deploy.hint.startDeployFunction"));
        getArtifactHandler().publish(deployTarget);
        prompt(String.format(message("function.deploy.hint.deployDone"), model.getAppName()));
        listHTTPTriggerUrls();
        return (FunctionApp) deployTarget.getApp();
    }

    private void updateFunctionAppSettings(final FunctionApp app) throws AzureExecutionException {
        prompt(message("function.deploy.hint.updateFunctionApp"));
        // Work around of https://github.com/Azure/azure-sdk-for-java/issues/1755
        final Update update = app.update();
        configureAppSettings(update::withAppSettings, getAppSettingsWithDefaultValue());
        update.apply();
        prompt(String.format(message("function.deploy.hint.updateDone"), model.getAppName()));
    }

    private void configureAppSettings(final Consumer<Map> withAppSettings, final Map appSettings) {
        if (appSettings != null && !appSettings.isEmpty()) {
            withAppSettings.accept(appSettings);
        }
    }

    /**
     * List anonymous HTTP Triggers url after deployment
     */
    private void listHTTPTriggerUrls() {
        try {
            final List<FunctionResource> triggers = listFunctions();
            final List<FunctionResource> httpFunction =
                    triggers.stream()
                            .filter(function -> function.getTrigger() != null &&
                                    StringUtils.equalsIgnoreCase(function.getTrigger().getType(), HTTP_TRIGGER))
                            .collect(Collectors.toList());
            final List<FunctionResource> anonymousTriggers =
                    httpFunction.stream()
                                .filter(bindingResource -> bindingResource.getTrigger() != null &&
                                        StringUtils.equalsIgnoreCase(
                                                (CharSequence) bindingResource.getTrigger().getProperty(AUTH_LEVEL),
                                                AuthorizationLevel.ANONYMOUS.toString()))
                                .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(httpFunction) || CollectionUtils.isEmpty(anonymousTriggers)) {
                prompt(message("function.deploy.hint.noAnonymousHttpTrigger"));
                return;
            }
            prompt(message("function.deploy.hint.httpTriggerUrls"));
            anonymousTriggers.forEach(trigger -> prompt(String.format("\t %s : %s", trigger.getName(), trigger.getTriggerUrl())));
            if (anonymousTriggers.size() < httpFunction.size()) {
                prompt(message("function.deploy.error.listHttpTriggerFailed"));
            }
        } catch (InterruptedException | IOException e) {
            prompt(message("function.deploy.error.listTriggerFailed"));
        } catch (AzureExecutionException e) {
            prompt(e.getMessage());
        }
    }

    /**
     * Sync triggers and return function list of deployed function app
     * Will retry when get empty result, the max retry times is LIST_TRIGGERS_MAX_RETRY
     * @return List of functions in deployed function app
     * @throws AzureExecutionException Throw if get empty result after LIST_TRIGGERS_MAX_RETRY times retry
     * @throws IOException Throw if meet IOException while getting Azure client
     * @throws InterruptedException Throw when thread was interrupted while sleeping between retry
     */
    private List<FunctionResource> listFunctions() throws AzureExecutionException, InterruptedException, IOException {
        final FunctionApp functionApp = getFunctionApp();
        for (int i = 0; i < LIST_TRIGGERS_MAX_RETRY; i++) {
            Thread.sleep(LIST_TRIGGERS_RETRY_PERIOD_IN_SECONDS * 1000);
            prompt(String.format(message("function.deploy.hint.syncTriggers"), i + 1, LIST_TRIGGERS_MAX_RETRY));
            try {
                functionApp.syncTriggers();
                final List<FunctionResource> triggers =
                        model.getAzureClient().appServices().functionApps()
                             .listFunctions(model.getResourceGroup(), model.getAppName()).stream()
                             .map(envelope -> FunctionResource.parseFunction(envelope))
                             .filter(function -> function != null)
                             .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(triggers)) {
                    return triggers;
                }
            } catch (RuntimeException exception) {
                // swallow sdk request runtime exception
            }
        }
        throw new AzureExecutionException(message("function.deploy.error.noTriggers"));
    }

    private OperatingSystemEnum getOsEnum() throws AzureExecutionException {
        final RuntimeConfiguration runtime = model.getRuntime();
        if (runtime != null && StringUtils.isNotBlank(runtime.getOs())) {
            return Utils.parseOperationSystem(runtime.getOs());
        }
        return DEFAULT_OS;
    }

    private DeploymentType getDeploymentType() throws AzureExecutionException {
        final DeploymentType deploymentType = DeploymentType.fromString(model.getDeploymentType());
        return deploymentType == DeploymentType.EMPTY ? getDeploymentTypeByRuntime() : deploymentType;
    }

    private DeploymentType getDeploymentTypeByRuntime() throws AzureExecutionException {
        final OperatingSystemEnum operatingSystemEnum = getOsEnum();
        switch (operatingSystemEnum) {
            case Docker:
                return DOCKER;
            case Linux:
                return isDedicatedPricingTier() ? RUN_FROM_ZIP : RUN_FROM_BLOB;
            default:
                return RUN_FROM_ZIP;
        }
    }

    private boolean isDedicatedPricingTier() {
        return AppServiceUtils.getPricingTierFromString(model.getPricingTier()) != null;
    }

    private FunctionApp getFunctionApp() {
        return model.getAzureClient().appServices().functionApps().getById(model.getFunctionId());
    }

    // region get App Settings
    private Map getAppSettingsWithDefaultValue() {
        final Map settings = model.getAppSettings();
        overrideDefaultAppSetting(settings, FUNCTIONS_WORKER_RUNTIME_NAME, message("function.hint.setFunctionWorker"),
                                  FUNCTIONS_WORKER_RUNTIME_VALUE, message("function.hint.changeFunctionWorker"));
        setDefaultAppSetting(settings, FUNCTIONS_EXTENSION_VERSION_NAME, message("function.hint.setFunctionVersion"),
                             FUNCTIONS_EXTENSION_VERSION_VALUE);
        return settings;
    }

    private void setDefaultAppSetting(Map result, String settingName, String settingIsEmptyMessage,
                                      String settingValue) {

        final String setting = (String) result.get(settingName);
        if (StringUtils.isEmpty(setting)) {
            prompt(settingIsEmptyMessage);
            result.put(settingName, settingValue);
        }
    }

    private void overrideDefaultAppSetting(Map result, String settingName, String settingIsEmptyMessage,
                                           String settingValue, String changeSettingMessage) {

        final String setting = (String) result.get(settingName);
        if (StringUtils.isEmpty(setting)) {
            prompt(settingIsEmptyMessage);
        } else if (!setting.equals(settingValue)) {
            prompt(String.format(changeSettingMessage, setting));
        }
        result.put(settingName, settingValue);
    }

    private ArtifactHandler getArtifactHandler() throws AzureExecutionException {
        final ArtifactHandlerBase.Builder builder;

        final DeploymentType deploymentType = getDeploymentType();
        switch (deploymentType) {
            case MSDEPLOY:
                builder = new MSDeployArtifactHandlerImpl.Builder().functionAppName(this.model.getAppName());
                break;
            case FTP:
                builder = new FTPArtifactHandlerImpl.Builder();
                break;
            case ZIP:
                builder = new ZIPArtifactHandlerImpl.Builder();
                break;
            case RUN_FROM_BLOB:
                builder = new RunFromBlobArtifactHandlerImpl.Builder();
                break;
            case DOCKER:
                builder = new DockerArtifactHandler.Builder();
                break;
            case EMPTY:
            case RUN_FROM_ZIP:
                builder = new RunFromZipArtifactHandlerImpl.Builder();
                break;
            default:
                throw new AzureExecutionException(message("function.deploy.error.unknownType"));
        }
        return builder
                .stagingDirectoryPath(this.model.getDeploymentStagingDirectoryPath())
                .build();
    }

    private void prompt(String promptMessage) {
        prompter.prompt(promptMessage);
    }
}
