/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.intellij.link.mysql;

import com.microsoft.azure.management.mysql.v2020_01_01.Server;
import com.microsoft.azure.management.mysql.v2020_01_01.implementation.DatabaseInner;
import com.microsoft.azure.management.resources.Subscription;
import com.microsoft.azure.toolkit.intellij.link.BaseLinkConfig;
import com.microsoft.azure.toolkit.intellij.link.base.ServiceType;
import com.microsoft.azuretools.core.mvp.model.AzureMvpModel;
import com.microsoft.tooling.msservices.serviceexplorer.azure.mysql.MySQLNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
public class MySQLLinkConfig extends BaseLinkConfig {

    // related to Azure MySQL
    private Subscription subscription;
    private Server server;
    private DatabaseInner database;
    private String username;
    private PasswordConfig passwordConfig;
    // ext
    private String url;

    public static MySQLLinkConfig getDefaultConfig(MySQLNode node) {
        MySQLLinkConfig config = new MySQLLinkConfig();
        if (Objects.nonNull(node)) {
            Subscription subscription = AzureMvpModel.getInstance().getSubscriptionById(node.getSubscriptionId());
            config.setSubscription(subscription);
            config.setServer(node.getServer());
            config.setUsername(node.getServer().administratorLogin() + "@" + node.getServer().name());
        }
        return config;
    }

    @Override
    public ServiceType getType() {
        return ServiceType.AZURE_DATABASE_FOR_MYSQL;
    }
}
