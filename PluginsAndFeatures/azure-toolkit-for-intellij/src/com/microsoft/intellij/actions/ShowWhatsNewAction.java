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

package com.microsoft.intellij.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.microsoft.azuretools.ijidea.utility.AzureAnAction;
import com.microsoft.azuretools.telemetrywrapper.ErrorType;
import com.microsoft.azuretools.telemetrywrapper.EventUtil;
import com.microsoft.azuretools.telemetrywrapper.Operation;
import com.microsoft.intellij.helpers.WhatsNewManager;
import com.microsoft.intellij.util.PluginUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.microsoft.azuretools.telemetry.TelemetryConstants.SHOW_WHATS_NEW;
import static com.microsoft.azuretools.telemetry.TelemetryConstants.SYSTEM;

public class ShowWhatsNewAction extends AzureAnAction {

    private static final String FAILED_TO_LOAD_WHATS_NEW = "Failed to load what's new document";

    @Override
    public boolean onActionPerformed(@NotNull final AnActionEvent anActionEvent, @Nullable final Operation operation) {
        final Project project = anActionEvent.getProject();
        try {
            WhatsNewManager.INSTANCE.showWhatsNew(true, project);
        } catch (Exception e) {
            EventUtil.logError(operation, ErrorType.systemError, e, null, null);
            PluginUtil.showInfoNotificationProject(project, FAILED_TO_LOAD_WHATS_NEW, e.getMessage());
        }
        return true;
    }

    protected String getServiceName(AnActionEvent event) {
        return SYSTEM;
    }

    protected String getOperationName(AnActionEvent event) {
        return SHOW_WHATS_NEW;
    }
}
