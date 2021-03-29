/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azuretools.authmanage;


import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.appplatform.v2020_07_01.implementation.AppPlatformManager;
import com.microsoft.azure.management.mysql.v2020_01_01.implementation.MySQLManager;
import com.microsoft.azure.toolkit.lib.common.cache.CacheEvict;
import com.microsoft.azure.toolkit.lib.common.exception.AzureToolkitRuntimeException;
import com.microsoft.azure.toolkit.lib.common.operation.AzureOperation;
import com.microsoft.azuretools.adauth.JsonHelper;
import com.microsoft.azuretools.authmanage.models.AuthMethodDetails;
import com.microsoft.azuretools.azurecommons.helpers.NotNull;
import com.microsoft.azuretools.azurecommons.helpers.Nullable;
import com.microsoft.azuretools.sdkmanage.AzureManager;
import com.microsoft.azuretools.sdkmanage.ServicePrincipalAzureManager;
import com.microsoft.azuretools.telemetrywrapper.ErrorType;
import com.microsoft.azuretools.telemetrywrapper.EventType;
import com.microsoft.azuretools.telemetrywrapper.EventUtil;
import com.microsoft.azuretools.utils.AzureUIRefreshCore;
import com.microsoft.azuretools.utils.AzureUIRefreshEvent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

import static com.microsoft.azuretools.Constants.FILE_NAME_AUTH_METHOD_DETAILS;
import static com.microsoft.azuretools.telemetry.TelemetryConstants.*;

public class AuthMethodManager {
    private static final Logger LOGGER = Logger.getLogger(AuthMethodManager.class.getName());
    private static final String CANNOT_GET_AZURE_MANAGER = "Cannot get Azure Manager. " +
            "Please check if you have already signed in.";
    private static final String CANNOT_GET_AZURE_BY_SID = "Cannot get Azure with Subscription ID: %s. " +
            "Please check if you have already signed in with this Subscription.";
    private static final String FAILED_TO_GET_AZURE_MANAGER_INSTANCE = "Failed to get an AzureManager instance " +
            "for AuthMethodDetails: %s with error %s";

    private AuthMethodDetails authMethodDetails;
    private volatile AzureManager azureManager;
    private final Set<Runnable> signInEventListeners = new HashSet<>();
    private final Set<Runnable> signOutEventListeners = new HashSet<>();

    private AuthMethodManager(AuthMethodDetails authMethodDetails) {
        this.authMethodDetails = authMethodDetails;
        final AzureManager manager = getAzureManager();
        // initialize subscription manager when restore authentication
        if (this.authMethodDetails.getAuthMethod() != null && Objects.nonNull(manager)) {
            manager.getSubscriptionManager().updateSubscriptionDetailsIfNull();
        }
    }

    public static AuthMethodManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    @NotNull
    @AzureOperation(
        name = "common|rest_client.create",
        params = {"sid"},
        type = AzureOperation.Type.TASK
    )
    public Azure getAzureClient(String sid) {
        final AzureManager manager = getAzureManager();
        if (manager != null) {
            final Azure azure = manager.getAzure(sid);
            if (azure != null) {
                return azure;
            }
        }
        final String error = "Failed to connect Azure service with current account";
        final String action = "Confirm you have already signed in with subscription: " + sid;
        final String errorCode = "001";
        throw new AzureToolkitRuntimeException(error, null, action, errorCode);
    }

    @AzureOperation(
        name = "common|rest_client.create_asc",
        params = {"sid"},
        type = AzureOperation.Type.TASK
    )
    public AppPlatformManager getAzureSpringCloudClient(String sid) {
        final AzureManager manager = getAzureManager();
        if (manager != null) {
            return getAzureManager().getAzureSpringCloudClient(sid);
        }
        final String error = "Failed to connect Azure service with current account";
        final String action = "Confirm you have already signed in with subscription: " + sid;
        throw new AzureToolkitRuntimeException(error, action);
    }

    @AzureOperation(
        name = "common|rest_client.create_mysql",
        params = {"sid"},
        type = AzureOperation.Type.TASK
    )
    public MySQLManager getMySQLManager(String sid) {
        final AzureManager manager = getAzureManager();
        if (manager != null) {
            return manager.getMySQLManager(sid);
        }
        final String error = "Failed to get manager of Azure Database for MySQL with current account";
        final String action = "Confirm you have already signed in with subscription: " + sid;
        throw new AzureToolkitRuntimeException(error, action);
    }

    public void addSignInEventListener(Runnable l) {
        signInEventListeners.add(l);
    }

    public void removeSignInEventListener(Runnable l) {
        signInEventListeners.remove(l);
    }

    public void addSignOutEventListener(Runnable l) {
        signOutEventListeners.add(l);
    }

    public void removeSignOutEventListener(Runnable l) {
        signOutEventListeners.remove(l);
    }

    public void notifySignInEventListener() {
        for (Runnable l : signInEventListeners) {
            l.run();
        }
        if (AzureUIRefreshCore.listeners != null) {
            AzureUIRefreshCore.execute(new AzureUIRefreshEvent(AzureUIRefreshEvent.EventType.SIGNIN, null));
        }
    }

    private void notifySignOutEventListener() {
        for (Runnable l : signOutEventListeners) {
            l.run();
        }
        if (AzureUIRefreshCore.listeners != null) {
            AzureUIRefreshCore.execute(new AzureUIRefreshEvent(AzureUIRefreshEvent.EventType.SIGNOUT, null));
        }
    }

    @Nullable
    public AzureManager getAzureManager() {
        return getAzureManager(getAuthMethod());
    }

    @AzureOperation(name = "account.sign_out", type = AzureOperation.Type.TASK)
    @CacheEvict(CacheEvict.ALL) // evict all caches on signing out
    public void signOut() {
        cleanAll();
        notifySignOutEventListener();
    }

    public boolean isSignedIn() {
        return azureManager != null;
    }

    public AuthMethod getAuthMethod() {
        return authMethodDetails == null ? null : authMethodDetails.getAuthMethod();
    }

    public AuthMethodDetails getAuthMethodDetails() {
        return this.authMethodDetails;
    }

    @AzureOperation(name = "account|auth_setting.update", type = AzureOperation.Type.TASK)
    public synchronized void setAuthMethodDetails(AuthMethodDetails authMethodDetails) {
        cleanAll();
        this.authMethodDetails = authMethodDetails;
        try {
            persistAuthMethodDetails();
        } catch (final IOException e) {
            final String error = "Failed to persist auth method settings while updating";
            final String action = "Retry later";
            throw new AzureToolkitRuntimeException(error, e, action);
        }
    }

    private synchronized @Nullable AzureManager getAzureManager(final AuthMethod authMethod) {
        if (authMethod == null) {
            return null;
        }
        if (azureManager == null) {
            try {
                azureManager = authMethod.createAzureManager(getAuthMethodDetails());
            } catch (RuntimeException ex) {
                LOGGER.info(String.format(FAILED_TO_GET_AZURE_MANAGER_INSTANCE, getAuthMethodDetails(), ex.getMessage()));
                cleanAll();
            }
        }
        return azureManager;
    }

    private synchronized void cleanAll() {
        if (azureManager != null) {
            azureManager.drop();
            azureManager.getSubscriptionManager().cleanSubscriptions();
            azureManager = null;
        }
        ServicePrincipalAzureManager.cleanPersist();
        authMethodDetails.setAccountEmail(null);
        authMethodDetails.setAzureEnv(null);
        authMethodDetails.setAuthMethod(null);
        authMethodDetails.setCredFilePath(null);
        try {
            persistAuthMethodDetails();
        } catch (final IOException e) {
            final String error = "Failed to persist local auth method settings while cleaning";
            final String action = "Retry later";
            throw new AzureToolkitRuntimeException(error, e, action);
        }
    }

    @AzureOperation(name = "account|auth_setting.persist", type = AzureOperation.Type.TASK)
    private void persistAuthMethodDetails() throws IOException {
        System.out.println("saving authMethodDetails...");
        String sd = JsonHelper.serialize(authMethodDetails);
        FileStorage fs = new FileStorage(FILE_NAME_AUTH_METHOD_DETAILS, CommonSettings.getSettingsBaseDir());
        fs.write(sd.getBytes(StandardCharsets.UTF_8));
    }

    private static class LazyHolder {
        static final AuthMethodManager INSTANCE = initAuthMethodManagerFromSettings();
    }

    private static AuthMethodManager initAuthMethodManagerFromSettings() {
        return EventUtil.executeWithLog(ACCOUNT, RESIGNIN, operation -> {
            try {
                final AuthMethodDetails savedAuthMethodDetails = loadSettings();
                final AuthMethodDetails authMethodDetails = savedAuthMethodDetails.getAuthMethod() == null ?
                        new AuthMethodDetails() : savedAuthMethodDetails.getAuthMethod().restoreAuth(savedAuthMethodDetails);
                final String authMethod = authMethodDetails.getAuthMethod() == null ? "Empty" : authMethodDetails.getAuthMethod().name();
                final Map<String, String> telemetryProperties = new HashMap<String, String>() {{
                        put(SIGNIN_METHOD, authMethod);
                        put(AZURE_ENVIRONMENT, CommonSettings.getEnvironment().getName());
                    }};
                EventUtil.logEvent(EventType.info, operation, telemetryProperties);
                return new AuthMethodManager(authMethodDetails);
            } catch (RuntimeException ignore) {
                EventUtil.logError(operation, ErrorType.systemError, ignore, null, null);
                return new AuthMethodManager(new AuthMethodDetails());
            }
        });
    }

    @AzureOperation(name = "account|auth_setting.load", type = AzureOperation.Type.TASK)
    private static AuthMethodDetails loadSettings() {
        System.out.println("loading authMethodDetails...");
        try {
            FileStorage fs = new FileStorage(FILE_NAME_AUTH_METHOD_DETAILS, CommonSettings.getSettingsBaseDir());
            byte[] data = fs.read();
            String json = new String(data);
            if (json.isEmpty()) {
                System.out.println(FILE_NAME_AUTH_METHOD_DETAILS + " is empty");
                return new AuthMethodDetails();
            }
            return JsonHelper.deserialize(AuthMethodDetails.class, json);
        } catch (IOException ignored) {
            System.out.println("Failed to loading authMethodDetails settings. Use defaults.");
            return new AuthMethodDetails();
        }
    }
}
