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

package com.microsoft.azure.toolkit.intellij.appservice.resourcegroup;

import com.microsoft.azure.management.resources.Subscription;
import com.microsoft.azure.toolkit.intellij.common.AzureDialog;
import com.microsoft.azure.toolkit.intellij.common.ValidationDebouncedTextInput;
import com.microsoft.azure.toolkit.lib.appservice.ResourceGroupMock;
import com.microsoft.azure.toolkit.lib.common.form.AzureForm;
import com.microsoft.azure.toolkit.lib.common.form.AzureFormInput;
import com.microsoft.azure.toolkit.lib.common.form.AzureValidationInfo;
import com.microsoft.azure.toolkit.lib.common.form.AzureValidationInfo.AzureValidationInfoBuilder;
import com.microsoft.azuretools.azurecommons.helpers.Nullable;
import com.microsoft.intellij.util.ValidationUtils;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class ResourceGroupCreationDialog extends AzureDialog<ResourceGroupMock>
    implements AzureForm<ResourceGroupMock> {
    public static final String DESCRIPTION =
        "A resource group is a container that holds related resources for an Azure solution.";
    public static final String DIALOG_TITLE = "New Resource Group";
    private final Subscription subscription;
    private JPanel contentPanel;
    private ValidationDebouncedTextInput textName;
    private JLabel labelDescription;

    public ResourceGroupCreationDialog(Subscription subscription) {
        super();
        this.init();
        this.subscription = subscription;
        this.textName.setValidator(() -> validateName(subscription));
    }

    private AzureValidationInfo validateName(final Subscription subscription) {
        try {
            ValidationUtils.validateResourceGroupName(subscription.subscriptionId(), this.textName.getValue());
        } catch (final IllegalArgumentException e) {
            final AzureValidationInfoBuilder builder = AzureValidationInfo.builder();
            return builder.input(this.textName).type(AzureValidationInfo.Type.ERROR).message(e.getMessage()).build();
        }
        return AzureValidationInfo.OK;
    }

    @Override
    public AzureForm<ResourceGroupMock> getForm() {
        return this;
    }

    @Override
    protected String getDialogTitle() {
        return DIALOG_TITLE;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return this.contentPanel;
    }

    @Override
    public ResourceGroupMock getData() {
        final ResourceGroupMock.ResourceGroupMockBuilder builder = ResourceGroupMock.builder();
        builder.subscription(this.subscription)
               .name(this.textName.getValue());
        return builder.build();
    }

    @Override
    public List<AzureFormInput<?>> getInputs() {
        return Collections.singletonList(this.textName);
    }

    private void createUIComponents() {
        this.labelDescription = new JLabel("<html><body><p>" + DESCRIPTION + "</p></body></html");
    }
}
