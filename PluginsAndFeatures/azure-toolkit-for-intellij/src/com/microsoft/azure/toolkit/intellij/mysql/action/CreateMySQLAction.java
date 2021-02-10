/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.intellij.mysql.action;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.microsoft.azure.management.mysql.v2020_01_01.Server;
import com.microsoft.azure.toolkit.intellij.mysql.creation.MySQLCreationDialog;
import com.microsoft.azure.toolkit.lib.common.operation.AzureOperation;
import com.microsoft.azure.toolkit.lib.common.task.AzureTask;
import com.microsoft.azure.toolkit.lib.common.task.AzureTaskManager;
import com.microsoft.azure.toolkit.lib.mysql.AzureMySQLConfig;
import com.microsoft.azure.toolkit.lib.mysql.AzureMySQLService;
import com.microsoft.azuretools.authmanage.AuthMethodManager;
import com.microsoft.intellij.actions.AzureSignInAction;
import com.microsoft.azuretools.utils.AzureUIRefreshCore;
import com.microsoft.azuretools.utils.AzureUIRefreshEvent;
import com.microsoft.intellij.AzurePlugin;
import com.microsoft.intellij.util.AzureLoginHelper;
import com.microsoft.tooling.msservices.components.DefaultLoader;
import com.microsoft.tooling.msservices.helpers.Name;
import com.microsoft.tooling.msservices.serviceexplorer.AzureActionEnum;
import com.microsoft.tooling.msservices.serviceexplorer.Node;
import com.microsoft.tooling.msservices.serviceexplorer.NodeActionEvent;
import com.microsoft.tooling.msservices.serviceexplorer.NodeActionListener;
import com.microsoft.tooling.msservices.serviceexplorer.azure.mysql.MySQLModule;

import static com.microsoft.intellij.ui.messages.AzureBundle.message;

@Name("Create")
public class CreateMySQLAction extends NodeActionListener {

    private final MySQLModule model;

    public CreateMySQLAction(MySQLModule model) {
        super();
        this.model = model;
    }

    @Override
    public AzureActionEnum getAction() {
        return AzureActionEnum.CREATE;
    }

    @Override
    public void actionPerformed(NodeActionEvent e) {
        final Project project = (Project) model.getProject();
        AzureSignInAction.doSignIn(AuthMethodManager.getInstance(), project).subscribe((isSuccess) -> {
            this.doActionPerformed(e, isSuccess, project);
        });
    }

    private void doActionPerformed(NodeActionEvent e, boolean isLoggedIn, Project project) {
        try {
            if (!isLoggedIn ||
                !AzureLoginHelper.isAzureSubsAvailableOrReportError(message("common.error.signIn"))) {
                return;
            }
        } catch (final Exception ex) {
            AzurePlugin.log(message("common.error.signIn"), ex);
            DefaultLoader.getUIHelper().showException(message("common.error.signIn"), ex, message("common.error.signIn"), false, true);
        }
        final MySQLCreationDialog dialog = new MySQLCreationDialog(project);
        dialog.setOkActionListener((data) -> this.createAzureMySQL(data, project, dialog));
        dialog.show();
    }

    private void createAzureMySQL(final AzureMySQLConfig config, final Project project, MySQLCreationDialog dialog) {
        final Runnable runnable = () -> {
            final ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
            indicator.setIndeterminate(true);
            DefaultLoader.getIdeHelper().invokeLater(dialog::close);
            Server server = AzureMySQLService.getInstance().createMySQL(config);
            refreshAzureExplorer(server);
        };
        String progressMessage = Node.getProgressMessage(AzureActionEnum.CREATE.getDoingName(), MySQLModule.MODULE_NAME, config.getServerName());
        final AzureTask task = new AzureTask(null, progressMessage, false, runnable);
        AzureTaskManager.getInstance().runInBackground(task);
    }

    @AzureOperation(name = "common|explorer.refresh", type = AzureOperation.Type.TASK)
    private void refreshAzureExplorer(Server server) {
        AzureTaskManager.getInstance().runLater(() -> {
            if (AzureUIRefreshCore.listeners != null) {
                AzureUIRefreshCore.execute(new AzureUIRefreshEvent(AzureUIRefreshEvent.EventType.REFRESH, server));
            }
        });
    }
}
