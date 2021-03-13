/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.intellij.azuresdk.referencebook;

import com.intellij.ui.HyperlinkLabel;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTabbedPane;
import com.microsoft.azure.toolkit.intellij.azuresdk.model.AzureSdkFeatureEntity;
import com.microsoft.azure.toolkit.intellij.azuresdk.model.AzureSdkPackageEntity;
import com.microsoft.azure.toolkit.intellij.common.SwingUtils;
import com.microsoft.azure.toolkit.lib.common.task.AzureTaskManager;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import java.util.List;

public class AzureSdkFeatureDetailPanel {
    private JBLabel titleLabel;
    private JPanel descPanel;
    @Getter
    private JPanel contentPanel;
    private JBTabbedPane tabPane;
    private HyperlinkLabel featureDocLink;
    private HyperlinkLabel featureAuthLink;
    private JBLabel descLabel;
    private AzureSdkFeatureEntity feature;

    public void setData(final AzureSdkFeatureEntity feature) {
        this.feature = feature;
        AzureTaskManager.getInstance().runLater(() -> {
            this.titleLabel.setText(feature.getName());
            SwingUtils.setTextAndEnableAutoWrap(this.descLabel,
                "To get started with a specific service library, " +
                    "see the README.md file located in the library's project folder. " +
                    "You can find service libraries in the /sdk directory. " +
                    "For a list of all the services we support access our list of all existing libraries.");

            this.featureDocLink.setHyperlinkText("Product documentation");
            this.featureDocLink.setHyperlinkTarget("https://azure.github.io/azure-sdk-for-java/index.html");
            this.featureAuthLink.setHyperlinkText("Setup authentication for your application");
            this.featureDocLink.setHyperlinkTarget("https://github.com/Azure/azure-sdk-for-java/wiki/Set-up-Your-Environment-for-Authentication");

            final List<AzureSdkPackageEntity> clientPackages = feature.getClientPackages();
            final List<AzureSdkPackageEntity> managementPackages = feature.getManagementPackages();
            this.tabPane.removeAll();
            if (CollectionUtils.isNotEmpty(clientPackages)) {
                final AzureSdkPackageGroupPanel clientPanel = new AzureSdkPackageGroupPanel();
                this.tabPane.insertTab("Client SDK", null, clientPanel.getContentPanel(), "", this.tabPane.getTabCount());
                clientPanel.setData(clientPackages);
            }
            if (CollectionUtils.isNotEmpty(managementPackages)) {
                final AzureSdkPackageGroupPanel managementSdkPanel = new AzureSdkPackageGroupPanel();
                this.tabPane.insertTab("Management SDK", null, managementSdkPanel.getContentPanel(), "", this.tabPane.getTabCount());
                managementSdkPanel.setData(managementPackages);
            }
        });
    }
}
