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

package com.microsoft.tooling.msservices.serviceexplorer.azure.webapp;

import com.microsoft.azuretools.core.mvp.model.webapp.AzureWebAppMvpModel;
import com.microsoft.azuretools.core.mvp.ui.base.MvpPresenter;
import com.microsoft.tooling.msservices.serviceexplorer.azure.webapp.base.WebAppBaseState;
import java.io.IOException;

public class WebAppNodePresenter<V extends WebAppNodeView> extends MvpPresenter<V> {
    public void onStartWebApp(String subscriptionId, String webAppId) throws IOException {
        AzureWebAppMvpModel.getInstance().startWebApp(subscriptionId, webAppId);
        final WebAppNodeView view = getMvpView();
        if (view == null) {
            return;
        }
        view.renderNode(WebAppBaseState.RUNNING);
    }

    public void onRestartWebApp(String subscriptionId, String webAppId) throws IOException {
        AzureWebAppMvpModel.getInstance().restartWebApp(subscriptionId, webAppId);
        final WebAppNodeView view = getMvpView();
        if (view == null) {
            return;
        }
        view.renderNode(WebAppBaseState.RUNNING);
    }

    public void onStopWebApp(String subscriptionId, String webAppId) throws IOException {
        AzureWebAppMvpModel.getInstance().stopWebApp(subscriptionId, webAppId);
        final WebAppNodeView view = getMvpView();
        if (view == null) {
            return;
        }
        view.renderNode(WebAppBaseState.STOPPED);
    }

    public void onNodeRefresh() {
        final WebAppNodeView view = getMvpView();
        if (view != null) {
            view.renderSubModules();
        }
    }
}
