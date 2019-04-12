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

package com.microsoft.azure.hdinsight.serverexplore.ui

import com.intellij.openapi.project.Project
import com.microsoft.azuretools.ijidea.ui.WarningMessageForm
import com.microsoft.tooling.msservices.components.DefaultLoader

class RefreshHdiReaderStorageAccountsWarningForm(project: Project, private val aseDeepLink: String): WarningMessageForm(project) {
    init {
        title = "Storage Accounts Unavailable Warning"
        warningMsgLabel.text = "<html><pre>You only have READ ONLY permission for this cluster.<br>Would you like to see storage accounts in Azure Storage Explorer?</pre></html>"
        setOKButtonText("Open Azure Storage Explorer")
    }

    override fun doOKAction() {
        super.doOKAction()

        try {
            DefaultLoader.getIdeHelper().openLinkInBrowser(aseDeepLink)
        } catch (exception: Exception) {
            DefaultLoader.getUIHelper().showError(exception.message, "HDInsight Explorer")
        }
    }
}