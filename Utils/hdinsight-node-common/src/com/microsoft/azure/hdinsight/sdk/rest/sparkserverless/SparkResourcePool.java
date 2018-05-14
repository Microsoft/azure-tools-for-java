/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.hdinsight.sdk.rest.sparkserverless.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Full definition of the spark resource pool entity.
 */
public class SparkResourcePool extends AnalyticsActivity {
    /**
     * The spark resource pool specific properties.
     */
    @JsonProperty(value = "properties")
    private SparkResourcePoolProperties properties;

    /**
     * Get the properties value.
     *
     * @return the properties value
     */
    public SparkResourcePoolProperties properties() {
        return this.properties;
    }

    /**
     * Set the properties value.
     *
     * @param properties the properties value to set
     * @return the SparkResourcePool object itself.
     */
    public SparkResourcePool withProperties(SparkResourcePoolProperties properties) {
        this.properties = properties;
        return this;
    }

}
