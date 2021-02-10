/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.hdinsight.sdk.rest.azure.serverless.spark.models.api.activityTypes.spark.resourcePools;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import com.microsoft.azure.hdinsight.sdk.rest.azure.serverless.spark.models.SparkResourcePool;
import com.microsoft.azuretools.azurecommons.helpers.NotNull;

import java.util.Map;

/**
 * response of 'get /activityTypes/spark/resourcePools/{resourcePoolId}'
 */
public class GetResourcePoolIdResponse {
    public static final Map<Integer, String> successfulResponses = ImmutableMap.of(
            200, "Successfully retrieved details of the specified resource pool");

    @NotNull
    @JsonProperty(value = "sparkResourcePool")
    private SparkResourcePool sparkResourcePool;

    /**
     * get the sparkResourcePool value
     * @return the sparkResourcePool value
     */
    @NotNull
    public SparkResourcePool sparkResourcePool() {
        return sparkResourcePool;
    }
}
