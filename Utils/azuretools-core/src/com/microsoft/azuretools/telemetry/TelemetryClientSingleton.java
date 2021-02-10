/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azuretools.telemetry;
import com.microsoft.applicationinsights.TelemetryClient;

public final class TelemetryClientSingleton {
    private TelemetryClient telemetry = null;

    private static final class SingletonHolder {
        private static final TelemetryClientSingleton INSTANCE = new TelemetryClientSingleton();
    }

    public static TelemetryClient getTelemetry(){
        return SingletonHolder.INSTANCE.telemetry;
    }

    private TelemetryClientSingleton(){
        telemetry = new TelemetryClient();
    }
}
