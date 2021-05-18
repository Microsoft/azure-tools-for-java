/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.tooling.msservices.serviceexplorer.azure.storage;

import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.storage.StorageAccount;
import com.microsoft.azuretools.authmanage.AuthMethodManager;
import com.microsoft.azuretools.authmanage.SubscriptionManager;
import com.microsoft.azuretools.azurecommons.helpers.Nullable;
import com.microsoft.azuretools.sdkmanage.AzureManager;
import com.microsoft.tooling.msservices.components.DefaultLoader;
import com.microsoft.tooling.msservices.helpers.ExternalStorageHelper;
import com.microsoft.azuretools.azurecommons.helpers.AzureCmdException;
import com.microsoft.tooling.msservices.helpers.azure.sdk.StorageClientSDKManager;
import com.microsoft.tooling.msservices.model.storage.ClientStorageAccount;
import com.microsoft.tooling.msservices.serviceexplorer.AzureIconSymbol;
import com.microsoft.tooling.msservices.serviceexplorer.AzureRefreshableNode;
import com.microsoft.tooling.msservices.serviceexplorer.Node;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StorageModule extends AzureRefreshableNode {
    private static final String STORAGE_MODULE_ID = com.microsoft.tooling.msservices.serviceexplorer.azure.storage.StorageModule.class.getName();
    private static final String ICON_PATH = "StorageAccount_16.png";
    private static final String BASE_MODULE_NAME = "Storage Accounts";
    public static final String MODULE_NAME = "Storage Account";

    public StorageModule(Node parent) {
        super(STORAGE_MODULE_ID, BASE_MODULE_NAME, parent, ICON_PATH);
    }

    @Override
    public @Nullable AzureIconSymbol getIconSymbol() {
        return AzureIconSymbol.StorageAccount.MODULE;
    }

    @Override
    protected void refreshItems()
            throws AzureCmdException {
        List<Pair<String, String>> failedSubscriptions = new ArrayList<>();

        try {
            AzureManager azureManager = AuthMethodManager.getInstance().getAzureManager();
            // not signed in
            if (azureManager == null) {
                return;
            }

            List<String> sidList = azureManager.getSelectedSubscriptionIds();
            for (String sid : sidList) {
                try {
                    Azure azure = azureManager.getAzure(sid);
                    List<com.microsoft.azure.management.storage.StorageAccount> storageAccounts = azure.storageAccounts().list();
                    for (StorageAccount sm : storageAccounts) {
                        addChildNode(new StorageNode(this, sid, sm));
                    }

                } catch (Exception ex) {
                    failedSubscriptions.add(new ImmutablePair<>(sid, ex.getMessage()));
                    continue;
                }
            }
        } catch (Exception ex) {
            DefaultLoader.getUIHelper().logError("An error occurred when trying to load Storage Accounts\n\n" + ex.getMessage(), ex);
        }

        //TODO
        // load External Accounts
        for (ClientStorageAccount clientStorageAccount : ExternalStorageHelper.getList(getProject())) {
            ClientStorageAccount storageAccount = StorageClientSDKManager.getManager().getStorageAccount(clientStorageAccount.getConnectionString());
        }
        if (!failedSubscriptions.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("An error occurred when trying to load Storage Accounts for the subscriptions:\n\n");
            for (Pair error : failedSubscriptions) {
                errorMessage.append(error.getKey()).append(": ").append(error.getValue()).append("\n");
            }
            DefaultLoader.getUIHelper().logError("An error occurred when trying to load Storage Accounts\n\n" + errorMessage.toString(), null);
        }
    }
}
