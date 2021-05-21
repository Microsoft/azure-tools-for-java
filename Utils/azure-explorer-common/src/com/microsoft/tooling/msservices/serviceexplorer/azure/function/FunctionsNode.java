/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */
package com.microsoft.tooling.msservices.serviceexplorer.azure.function;

import com.microsoft.azure.management.appservice.FunctionApp;
import com.microsoft.azure.toolkit.lib.common.operation.AzureOperation;
import com.microsoft.azure.toolkit.lib.common.utils.Utils;
import com.microsoft.azuretools.azurecommons.helpers.AzureCmdException;
import com.microsoft.azuretools.core.mvp.model.function.AzureFunctionMvpModel;
import com.microsoft.tooling.msservices.serviceexplorer.Node;
import com.microsoft.tooling.msservices.serviceexplorer.RefreshableNode;
import org.apache.commons.collections4.CollectionUtils;

public class FunctionsNode extends RefreshableNode {

    private static final String ID = FunctionsNode.class.getName();
    private static final String NAME = "Functions";
    private static final String ICON_PATH = "azure-functions-small.png";
    private static final String EMPTY_POSTFIX = " (Empty)";

    private FunctionApp functionApp;
    private String subscriptionId;

    public FunctionsNode(final Node parent, FunctionApp functionApp) {
        super(ID, NAME, parent, ICON_PATH);
        this.functionApp = functionApp;
        this.subscriptionId = Utils.getSubscriptionId(functionApp.id());
    }

    public FunctionApp getFunctionApp() {
        return functionApp;
    }

    @Override
    @AzureOperation(name = "function.refresh", type = AzureOperation.Type.ACTION)
    protected void refreshItems() throws AzureCmdException {
        AzureFunctionMvpModel.getInstance()
                .listFunctionEnvelopeInFunctionApp(subscriptionId, functionApp.id())
                .stream()
                .map(envelope -> new FunctionNode(envelope, this))
                .forEach(this::addChildNode);
        if (CollectionUtils.isEmpty(childNodes)) {
            setName(NAME + EMPTY_POSTFIX);
        } else {
            setName(NAME);
        }
    }

    @Override
    public int getPriority() {
        return HIGH_PRIORITY;
    }
}
