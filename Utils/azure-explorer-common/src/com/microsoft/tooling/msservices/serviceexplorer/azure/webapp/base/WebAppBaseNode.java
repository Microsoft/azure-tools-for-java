/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.tooling.msservices.serviceexplorer.azure.webapp.base;

import com.microsoft.azuretools.azurecommons.helpers.NotNull;
import com.microsoft.azuretools.telemetry.AppInsightsConstants;
import com.microsoft.azuretools.telemetry.TelemetryProperties;
import com.microsoft.tooling.msservices.serviceexplorer.AzureRefreshableNode;
import com.microsoft.tooling.msservices.serviceexplorer.NodeAction;
import com.microsoft.tooling.msservices.serviceexplorer.RefreshableNode;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class WebAppBaseNode extends RefreshableNode implements TelemetryProperties, WebAppBaseNodeView {
    protected static final String ACTION_START = "Start";
    protected static final String ACTION_STOP = "Stop";
    protected static final String ACTION_DELETE = "Delete";
    protected static final String ACTION_RESTART = "Restart";
    protected static final String ACTION_OPEN_IN_BROWSER = "Open In Browser";
    protected static final String ACTION_SHOW_PROPERTY = "Show Properties";
    protected static final String ICON_RUNNING_POSTFIX = "Running_16.png";
    protected static final String ICON_STOPPED_POSTFIX = "Stopped_16.png";
    protected static final String OS_LINUX = "Linux";

    protected final String subscriptionId;
    protected final String hostName;
    protected final String os;
    protected final String label;
    protected WebAppBaseState state;

    // todo: refactor constructor after function track2 migration
    public WebAppBaseNode(final String id, final String name, final String label, final AzureRefreshableNode parent,
                          final String subscriptionId, final String hostName, final String os, final String state) {
        super(id, name, parent, getIcon(os, label, WebAppBaseState.fromString(state)), true);
        this.state = WebAppBaseState.fromString(state);
        this.label = label;
        this.subscriptionId = subscriptionId;
        this.os = StringUtils.capitalize(os.toLowerCase());
        this.hostName = hostName;
    }

    protected static String getIcon(final String os, final String label, final WebAppBaseState state) {
        return StringUtils.capitalize(os.toLowerCase())
            + label + (state == WebAppBaseState.RUNNING ? ICON_RUNNING_POSTFIX : ICON_STOPPED_POSTFIX);
    }

    @Override
    public List<NodeAction> getNodeActions() {
        boolean running = this.state == WebAppBaseState.RUNNING;
        getNodeActionByName(ACTION_START).setEnabled(!running);
        getNodeActionByName(ACTION_STOP).setEnabled(running);
        getNodeActionByName(ACTION_RESTART).setEnabled(running);

        return super.getNodeActions();
    }

    @Override
    public void renderNode(@NotNull WebAppBaseState state) {
        switch (state) {
            case RUNNING:
                this.state = state;
                this.setIconPath(getIcon(this.os, this.label, WebAppBaseState.RUNNING));
                break;
            case STOPPED:
                this.state = state;
                this.setIconPath(getIcon(this.os, this.label, WebAppBaseState.STOPPED));
                break;
            default:
                break;
        }
    }

    @Override
    public Map<String, String> toProperties() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(AppInsightsConstants.SubscriptionId, this.subscriptionId);
        // todo: track region name
        return properties;
    }

    public String getSubscriptionId() {
        return this.subscriptionId;
    }

    public String getOs() {
        return this.os;
    }
}
