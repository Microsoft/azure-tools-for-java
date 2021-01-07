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

package com.microsoft.intellij.serviceexplorer.azure.rediscache;

import com.intellij.openapi.project.Project;
import com.microsoft.azuretools.authmanage.AuthMethodManager;
import com.microsoft.azuretools.ijidea.actions.AzureSignInAction;
import com.microsoft.intellij.AzurePlugin;
import com.microsoft.intellij.forms.CreateRedisCacheForm;
import com.microsoft.intellij.util.AzureLoginHelper;
import com.microsoft.tooling.msservices.components.DefaultLoader;
import com.microsoft.tooling.msservices.serviceexplorer.AzureActionEnum;
import com.microsoft.tooling.msservices.serviceexplorer.NodeActionEvent;
import com.microsoft.tooling.msservices.serviceexplorer.NodeActionListener;
import com.microsoft.tooling.msservices.serviceexplorer.azure.rediscache.RedisCacheModule;
import com.microsoft.tooling.msservices.serviceexplorer.listener.ActionBasicable;

public class CreateRedisCacheAction extends NodeActionListener implements ActionBasicable {
    private static final String ERROR_CREATING_REDIS_CACHE = "Error creating Redis cache";
    private RedisCacheModule redisCacheModule;

    public CreateRedisCacheAction(RedisCacheModule redisModule) {
        this.redisCacheModule = redisModule;
    }

    @Override
    public AzureActionEnum getAction() {
        return AzureActionEnum.CREATE;
    }

    @Override
    public void actionPerformed(NodeActionEvent e) {
        Project project = (Project) redisCacheModule.getProject();
        AzureSignInAction.doSignIn(AuthMethodManager.getInstance(), project).subscribe((isSuccess) -> {
            this.doActionPerformed(e, isSuccess, project);
        });
    }

    private void doActionPerformed(NodeActionEvent e, boolean isLoggedIn, Project project) {
        try {
            if (!isLoggedIn) {
                return;
            }
            if (!AzureLoginHelper.isAzureSubsAvailableOrReportError(ERROR_CREATING_REDIS_CACHE)) {
                return;
            }
            CreateRedisCacheForm createRedisCacheForm = new CreateRedisCacheForm(project);
            createRedisCacheForm.setOnCreate(new Runnable() {
                @Override
                public void run() {
                    if (redisCacheModule != null) {
                        redisCacheModule.load(false);
                    }
                }
            });
            createRedisCacheForm.show();
        } catch (Exception ex) {
            AzurePlugin.log(ERROR_CREATING_REDIS_CACHE, ex);
            DefaultLoader.getUIHelper().showException(ERROR_CREATING_REDIS_CACHE, ex, "Error creating Redis Cache", false, true);
        }
    }
}
