/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.intellij.mysql.action;

import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.microsoft.azure.toolkit.lib.common.exception.AzureToolkitRuntimeException;
import com.microsoft.azuretools.authmanage.AuthMethodManager;
import com.microsoft.intellij.actions.AzureSignInAction;
import com.microsoft.intellij.AzurePlugin;
import com.microsoft.intellij.util.AzureLoginHelper;
import com.microsoft.tooling.msservices.components.DefaultLoader;
import com.microsoft.tooling.msservices.helpers.Name;
import com.microsoft.tooling.msservices.serviceexplorer.AzureIconSymbol;
import com.microsoft.tooling.msservices.serviceexplorer.NodeActionEvent;
import com.microsoft.tooling.msservices.serviceexplorer.NodeActionListener;
import com.microsoft.tooling.msservices.serviceexplorer.azure.mysql.MySQLNode;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static com.microsoft.intellij.ui.messages.AzureBundle.message;

@Name(MySQLConnectToServerAction.ACTION_NAME)
public class MySQLConnectToServerAction extends NodeActionListener {

    private static final String DATABASE_TOOLS_PLUGIN_ID = "com.intellij.database";
    public static final String ACTION_NAME = "Open by Database Tools";
    private static final String MYSQL_PATTERN_NAME = "Azure Database for MySQL - %s";
    private static final String MYSQL_DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String MYSQL_PATTERN_URL = "jdbc:mysql://%s:3306?serverTimezone=UTC&useSSL=true&requireSSL=false";
    private static final String NOT_SUPPORT_ERROR_MESSAGE = "detect database plugin in your IDE.";
    private static final String NOT_SUPPORT_ERROR_ACTION = "note this action is only supported in Intellij Ultimate edition.";
    private static final String NOT_SUPPORT_IU_DIALOG_TITLE = "Azure Toolkit Error";
    private static final String ERROR_MESSAGE_PATTERN = "Failed to open datasource management dialog for %s";
    private static final String ERROR_ACTION = "please try again.";

    private final MySQLNode node;
    private final Project project;

    public MySQLConnectToServerAction(MySQLNode node) {
        super();
        this.node = node;
        this.project = (Project) node.getProject();
    }

    @Override
    public AzureIconSymbol getIconSymbol() {
        return AzureIconSymbol.MySQL.CONNECT_TO_SERVER;
    }

    @Override
    public void actionPerformed(NodeActionEvent e) {
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
        if (PluginManagerCore.getPlugin(PluginId.findId(DATABASE_TOOLS_PLUGIN_ID)) == null) {
            throw new AzureToolkitRuntimeException(NOT_SUPPORT_ERROR_MESSAGE, NOT_SUPPORT_ERROR_ACTION);
        } else {
            this.openDataSourceManagerDialog(project);
        }
    }

    private void openDataSourceManagerDialog(Project project) {
        Object registry = getDataSourceRegistry(project);
        Object dbPsiFacade = getDbPsiFacade(project);
        try {
            Object builder = MethodUtils.invokeMethod(registry, "getBuilder");
            MethodUtils.invokeMethod(builder, true, "withName", String.format(MYSQL_PATTERN_NAME, node.getServer().name()));
            MethodUtils.invokeMethod(builder, true, "withDriverClass", MYSQL_DEFAULT_DRIVER);
            MethodUtils.invokeMethod(builder, true, "withUrl", String.format(MYSQL_PATTERN_URL, node.getServer().fullyQualifiedDomainName()));
            MethodUtils.invokeMethod(builder, true, "withUser", node.getServer().administratorLogin() + "@" + node.getServer().name());
            MethodUtils.invokeMethod(builder, true, "commit");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new AzureToolkitRuntimeException(String.format(ERROR_MESSAGE_PATTERN, node.getServer().name()), ERROR_ACTION);
        }
        showDataSourceManagerDialog(dbPsiFacade, registry);
    }

    private Object getDataSourceRegistry(Project project) {
        try {
            Class[] parameterTypes = {Project.class};
            Class dataSourceRegistryClazz = Class.forName("com.intellij.database.autoconfig.DataSourceRegistry");
            Constructor constructor = dataSourceRegistryClazz.getConstructor(parameterTypes);
            return constructor.newInstance(project);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new AzureToolkitRuntimeException(String.format(ERROR_MESSAGE_PATTERN, node.getServer().name()), ERROR_ACTION);
        }
    }

    private Object getDbPsiFacade(Object project) {
        try {
            Class dbPsiFacadeClass = Class.forName("com.intellij.database.psi.DbPsiFacade");
            return MethodUtils.invokeStaticMethod(dbPsiFacadeClass, "getInstance", project);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new AzureToolkitRuntimeException(String.format(ERROR_MESSAGE_PATTERN, node.getServer().name()), ERROR_ACTION);
        }
    }

    private void showDataSourceManagerDialog(Object dbPsiFacade, Object registry) {
        try {
            Class dataSourceManagerDialogClazz = Class.forName("com.intellij.database.view.ui.DataSourceManagerDialog");
            MethodUtils.invokeStaticMethod(dataSourceManagerDialogClazz, "showDialog", dbPsiFacade, registry);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new AzureToolkitRuntimeException(String.format(ERROR_MESSAGE_PATTERN, node.getServer().name()), ERROR_ACTION);
        }
    }
}
