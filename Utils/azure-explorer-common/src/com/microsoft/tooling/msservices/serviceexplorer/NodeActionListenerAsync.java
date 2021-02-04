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

package com.microsoft.tooling.msservices.serviceexplorer;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.azure.toolkit.lib.common.task.AzureTask;
import com.microsoft.azure.toolkit.lib.common.task.AzureTaskManager;
import com.microsoft.azuretools.azurecommons.helpers.AzureCmdException;
import com.microsoft.azuretools.azurecommons.helpers.NotNull;

import java.util.concurrent.Callable;

public abstract class NodeActionListenerAsync extends NodeActionListener {
    private final String progressMessage;

    public NodeActionListenerAsync(@NotNull String progressMessage) {
        this.progressMessage = progressMessage;
    }

    /**
     * Async action.
     *
     * @param actionEvent event object.
     * @return ListenableFuture object.
     */
    public ListenableFuture<Void> actionPerformedAsync(final NodeActionEvent actionEvent) {
        Callable<Boolean> booleanCallable = beforeAsyncActionPerformed();

        boolean shouldRun = true;

        try {
            shouldRun = booleanCallable.call();
        } catch (Exception ignored) {
            // ignore
        }

        final SettableFuture<Void> future = SettableFuture.create();

        if (shouldRun) {
            final Object project = actionEvent.getAction().getNode().getProject();
            AzureTaskManager.getInstance().runInBackground(new AzureTask(project, progressMessage, false, () -> {
                try {
                    actionPerformed(actionEvent);
                    future.set(null);
                } catch (AzureCmdException e) {
                    future.setException(e);
                }
            }));
        } else {
            future.set(null);
        }

        return future;
    }

    @NotNull
    protected abstract Callable<Boolean> beforeAsyncActionPerformed();
}
