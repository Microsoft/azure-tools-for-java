/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.sqljdbc4.ui.activator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "com.microsoft.sqljdbc4.ui"; //$NON-NLS-1$

    // The shared instance
    private static Activator plugin;
    public static boolean dplyState;

    /**
     * The constructor.
     */
    public Activator() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance.
     *
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    /**
     * Logs a message and exception.
     *
     * @param excp
     *            : exception.
     */
    public void log(String message, Exception excp) {
        getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, message, excp));
    }

    /**
     * Save the Deployment Assembly check box state which will be used in case of immediate edit.
     */
    public static void setDeploymentAssemblyState(boolean state) {
        dplyState = state;
    }

    /**
     * Returns Deployment Assembly check box state which will be used in case of immediate edit.
     */
    public static boolean geteploymentAssemblyState() {
        return dplyState;
    }
}
