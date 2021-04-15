/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.intellij.ui;

import com.azure.identity.DeviceCodeInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.microsoft.aad.adal4j.AuthenticationCallback;
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.DeviceCode;
import com.microsoft.azuretools.adauth.IDeviceLoginUI;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

public class DeviceLoginUI implements IDeviceLoginUI {
    private DeviceLoginWindow deviceLoginWindow;

    @Setter
    private Disposable disposable;

    @Nullable
    @Override
    public AuthenticationResult authenticate(@NotNull final AuthenticationContext ctx,
                                             @NotNull final DeviceCode deviceCode,
                                             final AuthenticationCallback<AuthenticationResult> callback) {
        return null;
    }
    private static void runTask(@Nonnull Runnable runnable) {
        if (ApplicationManager.getApplication().isDispatchThread()) {
            runnable.run();
        } else {
            ApplicationManager.getApplication().invokeLater(runnable);
        }
    }
    public void promptDeviceCode(DeviceCodeInfo challenge) {
        runTask(() -> {
            deviceLoginWindow = new DeviceLoginWindow(challenge, this);
            deviceLoginWindow.show();
            System.out.println("xx");
        });
    }

    @Override
    public void closePrompt() {
        if (deviceLoginWindow != null) {
            deviceLoginWindow.closeDialog();
        }
    }

    @Override
    public void cancel() {
        if (disposable != null) {
            this.disposable.dispose();
        }
    }
}
