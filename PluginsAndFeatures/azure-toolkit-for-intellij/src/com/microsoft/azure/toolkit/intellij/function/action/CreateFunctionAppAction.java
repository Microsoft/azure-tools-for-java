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

package com.microsoft.azure.toolkit.intellij.function.action;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.microsoft.azure.management.appservice.FunctionApp;
import com.microsoft.azure.toolkit.intellij.function.FunctionAppCreationDialog;
import com.microsoft.azure.toolkit.lib.common.operation.AzureOperation;
import com.microsoft.azure.toolkit.lib.common.task.AzureTask;
import com.microsoft.azure.toolkit.lib.common.task.AzureTaskManager;
import com.microsoft.azure.toolkit.lib.function.FunctionAppConfig;
import com.microsoft.azure.toolkit.lib.function.FunctionAppService;
import com.microsoft.azuretools.authmanage.AuthMethodManager;
import com.microsoft.azuretools.ijidea.actions.AzureSignInAction;
import com.microsoft.azuretools.utils.AzureUIRefreshCore;
import com.microsoft.azuretools.utils.AzureUIRefreshEvent;
import com.microsoft.intellij.AzurePlugin;
import com.microsoft.intellij.util.AzureLoginHelper;
import com.microsoft.tooling.msservices.components.DefaultLoader;
import com.microsoft.tooling.msservices.helpers.Name;
import com.microsoft.tooling.msservices.serviceexplorer.NodeActionEvent;
import com.microsoft.tooling.msservices.serviceexplorer.NodeActionListener;
import com.microsoft.tooling.msservices.serviceexplorer.azure.function.FunctionModule;

import static com.microsoft.intellij.ui.messages.AzureBundle.message;

@Name("Create Function App")
public class CreateFunctionAppAction extends NodeActionListener {
    private final FunctionAppService functionAppService;
    private final FunctionModule functionModule;

    public CreateFunctionAppAction(FunctionModule functionModule) {
        super();
        this.functionModule = functionModule;
        this.functionAppService = FunctionAppService.getInstance();
    }

    @Override
    public void actionPerformed(NodeActionEvent e) {
        final Project project = (Project) functionModule.getProject();
        try {
            if (!AzureSignInAction.doSignIn(AuthMethodManager.getInstance(), project) ||
                !AzureLoginHelper.isAzureSubsAvailableOrReportError(message("common.error.signIn"))) {
                return;
            }
        } catch (final Exception ex) {
            AzurePlugin.log(message("common.error.signIn"), ex);
            DefaultLoader.getUIHelper().showException(message("common.error.signIn"), ex, message("common.error.signIn"), false, true);
        }
        final FunctionAppCreationDialog dialog = new FunctionAppCreationDialog(project);
        dialog.setOkActionListener((data) -> this.createFunctionApp(data, () -> DefaultLoader.getIdeHelper().invokeLater(dialog::close), project));
        dialog.show();
    }

    @AzureOperation(
        value = "create function app[%s, os=%s, rg=%s, sp=%s] in subscription[%s]",
        params = {
            "$config.getName()",
            "$config.getPlatform().getOs()",
            "$config.getResourceGroup().name()",
            "$config.getServicePlan().name()",
            "$config.getSubscription().displayName()"
        },
        type = AzureOperation.Type.ACTION
    )
    private void createFunctionApp(final FunctionAppConfig config, Runnable callback, final Project project) {
        final AzureTask task = new AzureTask(null, message("function.create.task.title"), true, () -> {
            final ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
            indicator.setIndeterminate(true);
            try {
                final FunctionApp functionApp = functionAppService.createFunctionApp(config);
                callback.run();
                refreshAzureExplorer(functionApp);
            } catch (final Exception ex) {
                // TODO: @wangmi show error with balloon notification instead of dialog
                final String title = message("function.create.error.title") + ex.getMessage();
                final String description = message("function.create.error.createFailed");
                DefaultLoader.getUIHelper().showError(title, description);
            }
        });
        AzureTaskManager.getInstance().runInModal(task);
    }

    @AzureOperation(
        value = "refresh azure explorer",
        type = AzureOperation.Type.ACTION
    )
    private void refreshAzureExplorer(FunctionApp functionApp) {
        AzureTaskManager.getInstance().runLater(() -> {
            if (AzureUIRefreshCore.listeners != null) {
                AzureUIRefreshCore.execute(new AzureUIRefreshEvent(AzureUIRefreshEvent.EventType.REFRESH, functionApp));
            }
        });
    }
}
