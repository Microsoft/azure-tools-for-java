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

package com.microsoft.tooling.msservices.serviceexplorer.azure.arm.deployments;

import com.microsoft.azure.management.resources.Deployment;
import com.microsoft.azuretools.azurecommons.helpers.AzureCmdException;
import com.microsoft.tooling.msservices.components.DefaultLoader;
import com.microsoft.tooling.msservices.serviceexplorer.Node;
import com.microsoft.tooling.msservices.serviceexplorer.NodeActionEvent;
import com.microsoft.tooling.msservices.serviceexplorer.NodeActionListener;
import com.microsoft.tooling.msservices.serviceexplorer.azure.arm.ResourceManagementNode;

public class DeploymentNode extends Node implements DeploymentNodeView {

    public static final String ICON_PATH = "WebApp_16.png";
    private static final String EXPORT_TEMPLATE_SUCCESS = "Export resource manager template saved";
    private static final String EXPORT_TEMPLATE_FAIL = "MS Services - Error Export resource manager template";
    private static final String SHOW_PROPERTY_ACTION = "Show Properties";
    private final Deployment deployment;
    private final DeploymentNodePresenter deploymentNodePresenter;
    private final String subscriptionId;

    public DeploymentNode(ResourceManagementNode parent, String subscriptionId, Deployment deployment) {
        super(deployment.id(), deployment.name(), parent, ICON_PATH, true);
        this.deployment = deployment;
        this.subscriptionId = subscriptionId;
        deploymentNodePresenter = new DeploymentNodePresenter();
        deploymentNodePresenter.onAttachView(this);
        loadActions();
    }

    @Override
    public void showExportTemplateResult(boolean isSuccess, Throwable t) {
        if (isSuccess) {
            DefaultLoader.getUIHelper().showInfo(this, EXPORT_TEMPLATE_SUCCESS);
        } else {
            DefaultLoader.getUIHelper().showException(t.getMessage(), t, EXPORT_TEMPLATE_FAIL, false, true);
        }
    }

    @Override
    protected void loadActions() {
        addAction(SHOW_PROPERTY_ACTION, null, new ShowDeploymentPropertyAction());
        super.loadActions();
    }

    public Deployment getDeployment() {
        return deployment;
    }

    public DeploymentNodePresenter getDeploymentNodePresenter() {
        return deploymentNodePresenter;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    // Show property action class
    private class ShowDeploymentPropertyAction extends NodeActionListener {
        @Override
        protected void actionPerformed(NodeActionEvent e) throws AzureCmdException {
            DefaultLoader.getUIHelper().openDeploymentPropertyView(DeploymentNode.this);
        }
    }
}
