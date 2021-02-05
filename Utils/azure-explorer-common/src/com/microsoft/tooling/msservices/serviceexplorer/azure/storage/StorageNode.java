/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.tooling.msservices.serviceexplorer.azure.storage;

import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.storage.StorageAccount;
import com.microsoft.azure.toolkit.lib.common.operation.AzureOperation;
import com.microsoft.azuretools.ActionConstants;
import com.microsoft.azuretools.authmanage.AuthMethodManager;
import com.microsoft.azuretools.azurecommons.helpers.Nullable;
import com.microsoft.azuretools.sdkmanage.AzureManager;
import com.microsoft.azuretools.telemetry.AppInsightsConstants;
import com.microsoft.azuretools.telemetry.TelemetryProperties;
import com.microsoft.tooling.msservices.components.DefaultLoader;
import com.microsoft.tooling.msservices.serviceexplorer.AzureActionEnum;
import com.microsoft.tooling.msservices.serviceexplorer.AzureIconSymbol;
import com.microsoft.tooling.msservices.serviceexplorer.BasicActionBuilder;
import com.microsoft.tooling.msservices.serviceexplorer.Node;
import com.microsoft.tooling.msservices.serviceexplorer.NodeActionListener;

import java.util.HashMap;
import java.util.Map;

public class StorageNode extends Node implements TelemetryProperties {
    private static final String STORAGE_ACCOUNT_ICON_PATH = "StorageAccount_16.png";

    private final StorageAccount storageAccount;
    private String subscriptionId;

    public StorageNode(Node parent, String subscriptionId, StorageAccount storageAccount) {
        super(storageAccount.name(), storageAccount.name(), parent, STORAGE_ACCOUNT_ICON_PATH, true);

        this.subscriptionId = subscriptionId;
        this.storageAccount = storageAccount;

        loadActions();
    }

    @Override
    public @Nullable AzureIconSymbol getIconSymbol() {
        return AzureIconSymbol.StorageAccount.MODULE;
    }

    @Override
    public Map<String, String> toProperties() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(AppInsightsConstants.SubscriptionId, this.subscriptionId);
        properties.put(AppInsightsConstants.Region, this.storageAccount.regionName());
        return properties;
    }

    @AzureOperation(name = ActionConstants.StorageAccount.OPEN_IN_PORTAL, type = AzureOperation.Type.ACTION)
    private void openInPortal() {
        openResourcesInPortal(subscriptionId, storageAccount.id());
    }

    @AzureOperation(name = ActionConstants.StorageAccount.DELETE, type = AzureOperation.Type.ACTION)
    private void delete() {
        AzureManager azureManager = AuthMethodManager.getInstance().getAzureManager();
        // not signed in
        if (azureManager == null) {
            return;
        }
        Azure azure = azureManager.getAzure(subscriptionId);
        azure.storageAccounts().deleteByResourceGroup(storageAccount.resourceGroupName(), storageAccount.name());
        DefaultLoader.getIdeHelper().invokeLater(() -> getParent().removeDirectChildNode(StorageNode.this));
    }

    @Override
    protected Map<String, Class<? extends NodeActionListener>> initActions() {
        addAction(initActionBuilder(this::openInPortal).withAction(AzureActionEnum.OPEN_IN_PORTAL).withBackgroudable(true).build());
        addAction(initActionBuilder(this::delete).withAction(AzureActionEnum.DELETE).withBackgroudable(true).withPromptable(true).build());
        return super.initActions();
    }

    protected final BasicActionBuilder initActionBuilder(Runnable runnable) {
        return new BasicActionBuilder(runnable)
                .withModuleName(StorageModule.MODULE_NAME)
                .withInstanceName(name);
    }

    public StorageAccount getStorageAccount() {
        return storageAccount;
    }

    @Override
    public String getToolTip() {
        return storageAccount.name() + "\n" + storageAccount.regionName()
                + "<br>" + storageAccount.resourceGroupName();
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }
}
