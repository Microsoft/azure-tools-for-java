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

package com.microsoft.azure.toolkit.intellij.webapp.runner.webappconfig;

import com.microsoft.azuretools.core.mvp.model.webapp.WebAppSettingModel;
import com.microsoft.intellij.ui.components.AzureArtifactType;

public class IntelliJWebAppSettingModel extends WebAppSettingModel {

    private AzureArtifactType azureArtifactType;
    private boolean openBrowserAfterDeployment = true;
    private boolean slotPanelVisible = false;
    private String artifactIdentifier;
    private String packaging;

    public String getArtifactIdentifier() {
        return artifactIdentifier;
    }

    public void setArtifactIdentifier(final String artifactIdentifier) {
        this.artifactIdentifier = artifactIdentifier;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(final String packaging) {
        this.packaging = packaging;
    }

    public boolean isOpenBrowserAfterDeployment() {
        return openBrowserAfterDeployment;
    }

    public void setOpenBrowserAfterDeployment(boolean openBrowserAfterDeployment) {
        this.openBrowserAfterDeployment = openBrowserAfterDeployment;
    }

    public boolean isSlotPanelVisible() {
        return slotPanelVisible;
    }

    public void setSlotPanelVisible(boolean slotPanelVisible) {
        this.slotPanelVisible = slotPanelVisible;
    }

    public AzureArtifactType getAzureArtifactType() {
        return azureArtifactType;
    }

    public void setAzureArtifactType(final AzureArtifactType azureArtifactType) {
        this.azureArtifactType = azureArtifactType;
    }
}
