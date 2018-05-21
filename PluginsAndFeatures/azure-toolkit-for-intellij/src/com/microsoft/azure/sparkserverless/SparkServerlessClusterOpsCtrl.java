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

package com.microsoft.azure.sparkserverless;

import com.intellij.openapi.diagnostic.Logger;
import com.microsoft.azure.sparkserverless.serverexplore.ui.SparkServerlessClusterDestoryDialog;
import com.microsoft.azure.sparkserverless.serverexplore.ui.SparkServerlessProvisionDialog;
import com.microsoft.azuretools.azurecommons.helpers.NotNull;
import com.microsoft.tooling.msservices.components.DefaultLoader;
import com.microsoft.tooling.msservices.serviceexplorer.Node;
import com.microsoft.tooling.msservices.serviceexplorer.azure.sparkserverless.SparkServerlessClusterOps;

public class SparkServerlessClusterOpsCtrl {
    @NotNull
    private final SparkServerlessClusterOps sparkServerlessClusterOps;
    private static Logger LOG = Logger.getInstance(SparkServerlessClusterOpsCtrl.class.getName());

    public SparkServerlessClusterOpsCtrl(@NotNull SparkServerlessClusterOps sparkServerlessClusterOps) {
        this.sparkServerlessClusterOps = sparkServerlessClusterOps;

        this.sparkServerlessClusterOps.getDestroyAction().subscribe(triplet -> {
            LOG.info(String.format("Destroy message received. AdlAccount: %s, clusterName: %s, currentNode: %s",
                    triplet.getLeft(), triplet.getMiddle(), triplet.getRight()));

            DefaultLoader.getIdeHelper().invokeLater(() -> {
                SparkServerlessClusterDestoryDialog destroyDialog = new SparkServerlessClusterDestoryDialog(
                        triplet.getRight());
                destroyDialog.show();
            });
        }, ex -> LOG.error(ex.getMessage(), ex));

        this.sparkServerlessClusterOps.getProvisionAction().subscribe(pair -> {
            LOG.info(String.format("Provision message received. AdlAccount: %s, node: %s",
                    pair.getLeft(), pair.getRight()));

            DefaultLoader.getIdeHelper().invokeLater(() -> {
                SparkServerlessProvisionDialog provisionDialog = new SparkServerlessProvisionDialog(pair.getRight());
                provisionDialog.show();
            });
        }, ex -> LOG.error(ex.getMessage(), ex));
    }
}
