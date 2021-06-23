/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.tooling.msservices.serviceexplorer.azure.webapp;

import com.microsoft.azure.toolkit.lib.appservice.service.IWebApp;
import com.microsoft.azure.toolkit.lib.common.operation.AzureOperation;
import com.microsoft.azuretools.azurecommons.helpers.NotNull;
import com.microsoft.azuretools.azurecommons.helpers.Nullable;
import com.microsoft.azuretools.core.mvp.model.webapp.AzureWebAppMvpModel;
import com.microsoft.azuretools.utils.AzureUIRefreshCore;
import com.microsoft.azuretools.utils.AzureUIRefreshEvent;
import com.microsoft.azuretools.utils.AzureUIRefreshListener;
import com.microsoft.azuretools.utils.WebAppUtils;
import com.microsoft.tooling.msservices.components.DefaultLoader;
import com.microsoft.tooling.msservices.serviceexplorer.AzureIconSymbol;
import com.microsoft.tooling.msservices.serviceexplorer.AzureRefreshableNode;
import com.microsoft.tooling.msservices.serviceexplorer.Node;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class WebAppModule extends AzureRefreshableNode implements WebAppModuleView {
    private static final String REDIS_SERVICE_MODULE_ID = WebAppModule.class.getName();
    private static final String ICON_PATH = "WebApp_16.png";
    private static final String BASE_MODULE_NAME = "Web Apps";
    private final WebAppModulePresenter<WebAppModule> webAppModulePresenter;

    public static final String MODULE_NAME = "Web App";

    /**
     * Create the node containing all the Web App resources.
     *
     * @param parent The parent node of this node
     */
    public WebAppModule(Node parent) {
        super(REDIS_SERVICE_MODULE_ID, BASE_MODULE_NAME, parent, ICON_PATH);
        webAppModulePresenter = new WebAppModulePresenter<>();
        webAppModulePresenter.onAttachView(WebAppModule.this);
        createListener();
    }

    @Override
    public @Nullable AzureIconSymbol getIconSymbol() {
        return AzureIconSymbol.WebApp.MODULE;
    }

    @Override
    @AzureOperation(name = "webapp.reload", type = AzureOperation.Type.ACTION)
    protected void refreshItems() {
        webAppModulePresenter.onModuleRefresh();
    }

    @Override
    @AzureOperation(name = "webapp.delete", params = {"nameFromResourceId(id)"}, type = AzureOperation.Type.ACTION)
    public void removeNode(String sid, String id, Node node) {
        webAppModulePresenter.onDeleteWebApp(sid, id);
        removeDirectChildNode(node);
    }

    private void createListener() {
        String id = "WebAppModule";
        AzureUIRefreshListener listener = new AzureUIRefreshListener() {
            @Override
            public void run() {
                if (event.opsType == AzureUIRefreshEvent.EventType.SIGNIN || event.opsType == AzureUIRefreshEvent
                        .EventType.SIGNOUT) {
                    removeAllChildNodes();
                } else if (event.object == null && (event.opsType == AzureUIRefreshEvent.EventType.UPDATE || event
                        .opsType == AzureUIRefreshEvent.EventType.REMOVE)) {
                    if (hasChildNodes()) {
                        load(true);
                    }
                } else if (event.object == null && event.opsType == AzureUIRefreshEvent.EventType.REFRESH) {
                    load(true);
                }
            }
        };
        AzureUIRefreshCore.addListener(id, listener);
    }

    @Override
    public void renderChildren(@NotNull final List<IWebApp> resourceExes) {
        resourceExes.stream()
                .filter(webApp -> StringUtils.isNotEmpty(webApp.id()))
                .map(webApp -> new WebAppNode(this, webApp))
                .forEach(this::addChildNode);
    }
}
