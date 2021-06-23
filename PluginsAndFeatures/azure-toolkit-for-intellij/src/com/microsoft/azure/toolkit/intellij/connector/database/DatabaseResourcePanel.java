/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.intellij.connector.database;


import com.intellij.icons.AllIcons;
import com.intellij.ui.AnimatedIcon;
import com.microsoft.azure.management.mysql.v2020_01_01.Server;
import com.microsoft.azure.management.mysql.v2020_01_01.implementation.DatabaseInner;
import com.microsoft.azure.management.resources.fluentcore.arm.ResourceId;
import com.microsoft.azure.toolkit.intellij.appservice.subscription.SubscriptionComboBox;
import com.microsoft.azure.toolkit.intellij.common.AzureComboBox;
import com.microsoft.azure.toolkit.intellij.common.AzureFormJPanel;
import com.microsoft.azure.toolkit.intellij.connector.Password;
import com.microsoft.azure.toolkit.intellij.connector.database.component.DatabaseComboBox;
import com.microsoft.azure.toolkit.intellij.connector.database.component.PasswordSaveComboBox;
import com.microsoft.azure.toolkit.intellij.connector.database.component.ServerComboBox;
import com.microsoft.azure.toolkit.intellij.connector.database.component.TestConnectionActionPanel;
import com.microsoft.azure.toolkit.intellij.connector.database.component.UsernameComboBox;
import com.microsoft.azure.toolkit.lib.Azure;
import com.microsoft.azure.toolkit.lib.common.database.JdbcUrl;
import com.microsoft.azure.toolkit.lib.common.exception.AzureToolkitRuntimeException;
import com.microsoft.azure.toolkit.lib.common.form.AzureFormInput;
import com.microsoft.azure.toolkit.lib.common.model.Subscription;
import com.microsoft.azure.toolkit.lib.common.task.AzureTaskManager;
import com.microsoft.azure.toolkit.lib.sqlserver.model.SqlDatabaseEntity;
import com.microsoft.azure.toolkit.lib.sqlserver.service.AzureSqlServer;
import com.microsoft.azure.toolkit.lib.sqlserver.service.ISqlServer;
import com.microsoft.azure.toolkit.lib.sqlserver.service.impl.SqlServer;
import com.microsoft.azuretools.authmanage.AuthMethodManager;
import com.microsoft.azuretools.azurecommons.util.Utils;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * TODO(Qianjin): refactor "if (DatabaseResource.Definition.SQL_SERVER == definition)" codes once we migrate MySQL to track2 into common-msql-lib.
 */
public class DatabaseResourcePanel implements AzureFormJPanel<DatabaseResource> {
    @Getter
    private JPanel contentPanel;
    private SubscriptionComboBox subscriptionComboBox;
    private ServerComboBox serverComboBox;
    private DatabaseComboBox databaseComboBox;
    private UsernameComboBox usernameComboBox;
    private JPasswordField inputPasswordField;
    private JTextField envPrefixTextField;
    private PasswordSaveComboBox passwordSaveComboBox;
    private JTextField urlTextField;
    private JButton testConnectionButton;
    private JTextPane testResultTextPane;
    private TestConnectionActionPanel testConnectionActionPanel;

    private JdbcUrl jdbcUrl = null;
    private DatabaseResource.Definition definition;

    public DatabaseResourcePanel(DatabaseResource.Definition definition) {
        super();
        this.definition = definition;
        init();
        initListeners();
    }

    private void init() {
        final Dimension lastColumnSize = new Dimension(106, 30);
        passwordSaveComboBox.setPreferredSize(lastColumnSize);
        passwordSaveComboBox.setMaximumSize(lastColumnSize);
        passwordSaveComboBox.setSize(lastColumnSize);
        envPrefixTextField.setPreferredSize(lastColumnSize);
        envPrefixTextField.setMaximumSize(lastColumnSize);
        envPrefixTextField.setSize(lastColumnSize);
        testConnectionActionPanel.setVisible(false);
        testResultTextPane.setEditable(false);
        testConnectionButton.setEnabled(false);
        envPrefixTextField.setText(DatabaseResource.Definition.SQL_SERVER == definition ? "AZURE_SQL_" : "AZURE_MYSQL_");
    }

    protected void initListeners() {
        this.subscriptionComboBox.addItemListener(this::onSubscriptionChanged);
        this.serverComboBox.addItemListener(this::onServerChanged);
        this.databaseComboBox.addItemListener(this::onDatabaseChanged);
        this.inputPasswordField.addKeyListener(this.onPasswordChanged());
        this.urlTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                onUrlEdited(e);
            }
        });
        this.testConnectionButton.addActionListener(this::onTestConnectionButtonClicked);
        this.testConnectionActionPanel.getCopyButton().addActionListener(this::onCopyButtonClicked);
    }

    private void onTestConnectionButtonClicked(ActionEvent e) {
        testConnectionButton.setEnabled(false);
        testConnectionButton.setIcon(new AnimatedIcon.Default());
        testConnectionButton.setDisabledIcon(new AnimatedIcon.Default());
        final String username = (String) usernameComboBox.getValue();
        final String password = String.valueOf(inputPasswordField.getPassword());
        final String title = String.format("Connecting to Azure Database for MySQL (%s)...", jdbcUrl.getServerHost());
        AzureTaskManager.getInstance().runInBackground(title, false, () -> {
            final DatabaseConnectionUtils.ConnectResult connectResult = DatabaseConnectionUtils.connectWithPing(this.jdbcUrl, username, password);
            // show result info
            testConnectionActionPanel.setVisible(true);
            testResultTextPane.setText(getConnectResultMessage(connectResult));
            final Icon icon = connectResult.isConnected() ? AllIcons.General.InspectionsOK : AllIcons.General.BalloonError;
            testConnectionActionPanel.getIconLabel().setIcon(icon);
            testConnectionButton.setIcon(null);
            testConnectionButton.setEnabled(true);
        });
    }

    private String getConnectResultMessage(DatabaseConnectionUtils.ConnectResult result) {
        final StringBuilder messageBuilder = new StringBuilder();
        if (result.isConnected()) {
            messageBuilder.append("Connected successfully.").append(System.lineSeparator());
            messageBuilder.append("Version: ").append(result.getServerVersion()).append(System.lineSeparator());
            messageBuilder.append("Ping cost: ").append(result.getPingCost()).append("ms");
        } else {
            messageBuilder.append("Failed to connect with above parameters.").append(System.lineSeparator());
            messageBuilder.append("Message: ").append(result.getMessage());
        }
        return messageBuilder.toString();
    }

    private void onSubscriptionChanged(final ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.DESELECTED) {
            final Subscription subscription = (Subscription) e.getItem();
            this.serverComboBox.setSubscription(subscription);
        }
    }

    private void onServerChanged(final ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.DESELECTED) {
            this.databaseComboBox.setServer(e.getItem());
            this.usernameComboBox.setServer(e.getItem());
        }
    }

    private void onDatabaseChanged(final ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.DESELECTED) {
            if (DatabaseResource.Definition.SQL_SERVER == definition) {
                String server = Optional.ofNullable((SqlServer) this.databaseComboBox.getServer()).map(sql -> sql.entity().getFullyQualifiedDomainName()).orElse(null);
                String database = Optional.ofNullable((SqlDatabaseEntity) e.getItem()).map(SqlDatabaseEntity::getName).orElse(null);
                this.jdbcUrl = Objects.isNull(this.jdbcUrl) ? JdbcUrl.sqlserver(server, database) : this.jdbcUrl.setServerHost(server).setDatabase(database);
            } else {
                String server = Optional.ofNullable((Server) this.databaseComboBox.getServer()).map(Server::fullyQualifiedDomainName).orElse(null);
                String database = Optional.ofNullable((DatabaseInner) e.getItem()).map(DatabaseInner::name).orElse(null);
                this.jdbcUrl = Objects.isNull(this.jdbcUrl) ? JdbcUrl.mysql(server, database) : this.jdbcUrl.setServerHost(server).setDatabase(database);
            }
            this.urlTextField.setText(this.jdbcUrl.toString());
            this.urlTextField.setCaretPosition(0);
        }
    }

    private void onUrlEdited(FocusEvent e) {
        try {
            this.jdbcUrl = JdbcUrl.from(this.urlTextField.getText());
            if (DatabaseResource.Definition.SQL_SERVER == definition) {
                this.serverComboBox.setValue(new AzureComboBox.ItemReference<>(this.jdbcUrl.getServerHost(), sql -> ((ISqlServer) sql).entity().getFullyQualifiedDomainName()));
                this.databaseComboBox.setValue(new AzureComboBox.ItemReference<>(this.jdbcUrl.getDatabase(), SqlDatabaseEntity::getName));
            } else {
                this.serverComboBox.setValue(new AzureComboBox.ItemReference<>(this.jdbcUrl.getServerHost(), Server::fullyQualifiedDomainName));
                this.databaseComboBox.setValue(new AzureComboBox.ItemReference<>(this.jdbcUrl.getDatabase(), DatabaseInner::name));
            }
        } catch (final Exception exception) {
            // TODO: messager.warning(...)
        }
    }

    private void onCopyButtonClicked(ActionEvent e) {
        try {
            Utils.copyToSystemClipboard(testResultTextPane.getText());
        } catch (final Exception exception) {
            final String error = "copy test result error";
            final String action = "try again later.";
            throw new AzureToolkitRuntimeException(error, action);
        }
    }

    private KeyListener onPasswordChanged() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (ArrayUtils.isNotEmpty(inputPasswordField.getPassword())) {
                    testConnectionButton.setEnabled(true);
                }
            }
        };
    }

    @Override
    public DatabaseResource getData() {
        final Password password = new Password();
        password.password(inputPasswordField.getPassword());
        password.saveType(passwordSaveComboBox.getValue());

        final DatabaseResource resource = new DatabaseResource(definition.getType(),
                DatabaseResource.Definition.SQL_SERVER == definition ?
                ((SqlDatabaseEntity) databaseComboBox.getValue()).getId() : ((DatabaseInner) databaseComboBox.getValue()).id());
        resource.setPassword(password);
        resource.setUsername((String) usernameComboBox.getValue());
        resource.setJdbcUrl(this.jdbcUrl);
        resource.setEnvPrefix(envPrefixTextField.getText());
        return resource;
    }

    @Override
    public void setData(DatabaseResource resource) {
        Optional.ofNullable(resource.getServerId()).ifPresent((serverId -> {
            Optional.ofNullable(serverId.subscriptionId()).ifPresent(subscriptionId -> {
                this.subscriptionComboBox.setValue(new AzureComboBox.ItemReference<>(subscriptionId, Subscription::getId), true);
            });
            Optional.ofNullable(serverId.name()).ifPresent(name -> {
                if (DatabaseResource.Definition.SQL_SERVER == definition) {
                    this.serverComboBox.setValue(new AzureComboBox.ItemReference<>(name, server -> ((ISqlServer) server).entity().getName()), true);
                } else {
                    this.serverComboBox.setValue(new AzureComboBox.ItemReference<>(name, Server::name), true);
                }
            });
        }));
        Optional.ofNullable(resource.getPassword()).ifPresent(config -> {
            this.inputPasswordField.setText(String.valueOf(config.password()));
            this.passwordSaveComboBox.setValue(config.saveType());
        });
        Optional.ofNullable(resource.getDatabaseName()).ifPresent((dbName -> {
            this.databaseComboBox.setValue(new AzureComboBox.ItemReference<>(dbName, DatabaseInner::name), true);
        }));
        Optional.ofNullable(resource.getUsername())
            .ifPresent((username -> this.usernameComboBox.setValue(username)));
        Optional.ofNullable(resource.getEnvPrefix())
            .ifPresent(p -> this.envPrefixTextField.setText(p));
    }

    @Override
    public List<AzureFormInput<?>> getInputs() {
        final AzureFormInput<?>[] inputs = {
            this.subscriptionComboBox,
            this.serverComboBox,
            this.databaseComboBox,
            this.usernameComboBox
        };
        return Arrays.asList(inputs);
    }

    private void createUIComponents() {
        if (DatabaseResource.Definition.SQL_SERVER == definition) {
            // server
            this.serverComboBox = new ServerComboBox<SqlServer>();
            this.serverComboBox.setItemsLoader(() -> Objects.isNull(this.serverComboBox.getSubscription()) ? Collections.emptyList() :
                    Azure.az(AzureSqlServer.class).subscription(this.serverComboBox.getSubscription().getId()).sqlServers().stream().map(e -> (SqlServer)e).collect(Collectors.toList()));
            this.serverComboBox.setItemTextFunc((Function<SqlServer, String>) server -> server.entity().getName());
            // database
            this.databaseComboBox = new DatabaseComboBox<SqlServer, SqlDatabaseEntity>();
            this.databaseComboBox.setItemsLoader(() -> Objects.isNull(this.databaseComboBox.getServer()) ? Collections.emptyList() : ((SqlServer) this.databaseComboBox.getServer()).databases());
            this.databaseComboBox.setItemTextFunc((Function<SqlDatabaseEntity, String>) databaseEntity -> databaseEntity.getName());
            // username
            this.usernameComboBox = new UsernameComboBox<SqlServer>();
            this.usernameComboBox.setItemsLoader(() -> Objects.isNull(this.databaseComboBox.getServer()) ? Collections.emptyList() :
                    Arrays.asList(((SqlServer) this.databaseComboBox.getServer()).entity().getAdministratorLoginName() + "@" + ((SqlServer) this.databaseComboBox.getServer()).entity().getName()));
        } else {
            // server
            this.serverComboBox = new ServerComboBox<Server>();
            this.serverComboBox.setItemsLoader(() -> Objects.isNull(this.serverComboBox.getSubscription()) ? Collections.emptyList() :
                    AuthMethodManager.getInstance().getAzureManager().getMySQLManager(this.serverComboBox.getSubscription().getId()).servers().list());
            this.serverComboBox.setItemTextFunc((Function<Server, String>) server -> server.name());
            // database
            this.databaseComboBox = new DatabaseComboBox<Server, DatabaseInner>();
            this.databaseComboBox.setItemsLoader(() -> {
                if (Objects.isNull(this.databaseComboBox.getServer())) {
                    return Collections.emptyList();
                }
                final Server server = (Server) this.databaseComboBox.getServer();
                final String sid = ResourceId.fromString(server.id()).subscriptionId();
                return AuthMethodManager.getInstance().getAzureManager().getMySQLManager(sid).databases().inner().listByServer(server.resourceGroupName(), server.name());
            });
            this.databaseComboBox.setItemTextFunc((Function<DatabaseInner, String>) databaseEntity -> databaseEntity.name());
            // username
            this.usernameComboBox = new UsernameComboBox<Server>();
            this.usernameComboBox.setItemsLoader(() -> Objects.isNull(this.databaseComboBox.getServer()) ? Collections.emptyList() :
                    Arrays.asList(((Server) this.databaseComboBox.getServer()).administratorLogin() + "@" + ((Server) this.databaseComboBox.getServer()).name()));
        }
    }
}
