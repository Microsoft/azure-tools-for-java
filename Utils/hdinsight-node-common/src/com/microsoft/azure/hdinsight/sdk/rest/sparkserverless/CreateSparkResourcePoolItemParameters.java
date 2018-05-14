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
 * Data Lake Analytics Spark Resource Pool creation request.
 */
public class CreateSparkResourcePoolItemParameters {
    /**
     * Name for the spark master or spark workers.
     */
    @JsonProperty(value = "name")
    private String name;

    /**
     * Number of instances of spark master or spark worker.
     */
    @JsonProperty(value = "targetInstanceCount")
    private Integer targetInstanceCount;

    /**
     * Number of cores in each started instance of spark master or spark
     * workers.
     */
    @JsonProperty(value = "perInstanceCoreCount")
    private Integer perInstanceCoreCount;

    /**
     * Allocated memory in GB for each started instance of spark master or
     * spark workers.
     */
    @JsonProperty(value = "perInstanceMemoryInGB")
    private Integer perInstanceMemoryInGB;

    /**
     * Get the name value.
     *
     * @return the name value
     */
    public String name() {
        return this.name;
    }

    /**
     * Set the name value.
     *
     * @param name the name value to set
     * @return the CreateSparkResourcePoolItemParameters object itself.
     */
    public CreateSparkResourcePoolItemParameters withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get the targetInstanceCount value.
     *
     * @return the targetInstanceCount value
     */
    public Integer targetInstanceCount() {
        return this.targetInstanceCount;
    }

    /**
     * Set the targetInstanceCount value.
     *
     * @param targetInstanceCount the targetInstanceCount value to set
     * @return the CreateSparkResourcePoolItemParameters object itself.
     */
    public CreateSparkResourcePoolItemParameters withTargetInstanceCount(Integer targetInstanceCount) {
        this.targetInstanceCount = targetInstanceCount;
        return this;
    }

    /**
     * Get the perInstanceCoreCount value.
     *
     * @return the perInstanceCoreCount value
     */
    public Integer perInstanceCoreCount() {
        return this.perInstanceCoreCount;
    }

    /**
     * Set the perInstanceCoreCount value.
     *
     * @param perInstanceCoreCount the perInstanceCoreCount value to set
     * @return the CreateSparkResourcePoolItemParameters object itself.
     */
    public CreateSparkResourcePoolItemParameters withPerInstanceCoreCount(Integer perInstanceCoreCount) {
        this.perInstanceCoreCount = perInstanceCoreCount;
        return this;
    }

    /**
     * Get the perInstanceMemoryInGB value.
     *
     * @return the perInstanceMemoryInGB value
     */
    public Integer perInstanceMemoryInGB() {
        return this.perInstanceMemoryInGB;
    }

    /**
     * Set the perInstanceMemoryInGB value.
     *
     * @param perInstanceMemoryInGB the perInstanceMemoryInGB value to set
     * @return the CreateSparkResourcePoolItemParameters object itself.
     */
    public CreateSparkResourcePoolItemParameters withPerInstanceMemoryInGB(Integer perInstanceMemoryInGB) {
        this.perInstanceMemoryInGB = perInstanceMemoryInGB;
        return this;
    }

}
