/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.hdinsight.sdk.rest.azure.datalake.analytics.job.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * U-SQL job properties used when retrieving U-SQL jobs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("USql")
public class USqlJobProperties extends JobProperties {
    /**
     * The list of resources that are required by the job.
     */
    @JsonProperty(value = "resources", access = JsonProperty.Access.WRITE_ONLY)
    private List<JobResource> resources;

    /**
     * The job specific statistics.
     */
    @JsonProperty(value = "statistics")
    private JobStatistics statistics;

    /**
     * The job specific debug data locations.
     */
    @JsonProperty(value = "debugData")
    private JobDataPath debugData;

    /**
     * The diagnostics for the job.
     */
    @JsonProperty(value = "diagnostics", access = JsonProperty.Access.WRITE_ONLY)
    private List<Diagnostics> diagnostics;

    /**
     * The algebra file path after the job has completed.
     */
    @JsonProperty(value = "algebraFilePath", access = JsonProperty.Access.WRITE_ONLY)
    private String algebraFilePath;

    /**
     * The total time this job spent compiling. This value should not be set by the user and will be ignored if it is.
     */
    @JsonProperty(value = "totalCompilationTime", access = JsonProperty.Access.WRITE_ONLY)
    private String totalCompilationTime;

    /**
     * The total time this job spent queued. This value should not be set by the user and will be ignored if it is.
     */
    @JsonProperty(value = "totalQueuedTime", access = JsonProperty.Access.WRITE_ONLY)
    private String totalQueuedTime;

    /**
     * The total time this job spent executing. This value should not be set by the user and will be ignored if it is.
     */
    @JsonProperty(value = "totalRunningTime", access = JsonProperty.Access.WRITE_ONLY)
    private String totalRunningTime;

    /**
     * The total time this job spent paused. This value should not be set by the user and will be ignored if it is.
     */
    @JsonProperty(value = "totalPausedTime", access = JsonProperty.Access.WRITE_ONLY)
    private String totalPausedTime;

    /**
     * The ID used to identify the job manager coordinating job execution. This value should not be set by the user and
     * will be ignored if it is.
     */
    @JsonProperty(value = "rootProcessNodeId", access = JsonProperty.Access.WRITE_ONLY)
    private String rootProcessNodeId;

    /**
     * The ID used to identify the yarn application executing the job. This value should not be set by the user and
     * will be ignored if it is.
     */
    @JsonProperty(value = "yarnApplicationId", access = JsonProperty.Access.WRITE_ONLY)
    private String yarnApplicationId;

    /**
     * The timestamp (in ticks) for the yarn application executing the job. This value should not be set by the user
     * and will be ignored if it is.
     */
    @JsonProperty(value = "yarnApplicationTimeStamp", access = JsonProperty.Access.WRITE_ONLY)
    private Long yarnApplicationTimeStamp;

    /**
     * The specific compilation mode for the job used during execution. If this is not specified during submission, the
     * server will determine the optimal compilation mode. Possible values include: 'Semantic', 'Full', 'SingleBox'.
     */
    @JsonProperty(value = "compileMode", access = JsonProperty.Access.WRITE_ONLY)
    private CompileMode compileMode;

    /**
     * Get the list of resources that are required by the job.
     *
     * @return the resources value
     */
    public List<JobResource> resources() {
        return this.resources;
    }

    /**
     * Get the job specific statistics.
     *
     * @return the statistics value
     */
    public JobStatistics statistics() {
        return this.statistics;
    }

    /**
     * Set the job specific statistics.
     *
     * @param statistics the statistics value to set
     * @return the USqlJobProperties object itself.
     */
    public USqlJobProperties withStatistics(JobStatistics statistics) {
        this.statistics = statistics;
        return this;
    }

    /**
     * Get the job specific debug data locations.
     *
     * @return the debugData value
     */
    public JobDataPath debugData() {
        return this.debugData;
    }

    /**
     * Set the job specific debug data locations.
     *
     * @param debugData the debugData value to set
     * @return the USqlJobProperties object itself.
     */
    public USqlJobProperties withDebugData(JobDataPath debugData) {
        this.debugData = debugData;
        return this;
    }

    /**
     * Get the diagnostics for the job.
     *
     * @return the diagnostics value
     */
    public List<Diagnostics> diagnostics() {
        return this.diagnostics;
    }

    /**
     * Get the algebra file path after the job has completed.
     *
     * @return the algebraFilePath value
     */
    public String algebraFilePath() {
        return this.algebraFilePath;
    }

    /**
     * Get the total time this job spent compiling. This value should not be set by the user and will be ignored if it is.
     *
     * @return the totalCompilationTime value
     */
    public String totalCompilationTime() {
        return this.totalCompilationTime;
    }

    /**
     * Get the total time this job spent queued. This value should not be set by the user and will be ignored if it is.
     *
     * @return the totalQueuedTime value
     */
    public String totalQueuedTime() {
        return this.totalQueuedTime;
    }

    /**
     * Get the total time this job spent executing. This value should not be set by the user and will be ignored if it is.
     *
     * @return the totalRunningTime value
     */
    public String totalRunningTime() {
        return this.totalRunningTime;
    }

    /**
     * Get the total time this job spent paused. This value should not be set by the user and will be ignored if it is.
     *
     * @return the totalPausedTime value
     */
    public String totalPausedTime() {
        return this.totalPausedTime;
    }

    /**
     * Get the ID used to identify the job manager coordinating job execution. This value should not be set by the user and will be ignored if it is.
     *
     * @return the rootProcessNodeId value
     */
    public String rootProcessNodeId() {
        return this.rootProcessNodeId;
    }

    /**
     * Get the ID used to identify the yarn application executing the job. This value should not be set by the user and will be ignored if it is.
     *
     * @return the yarnApplicationId value
     */
    public String yarnApplicationId() {
        return this.yarnApplicationId;
    }

    /**
     * Get the timestamp (in ticks) for the yarn application executing the job. This value should not be set by the user and will be ignored if it is.
     *
     * @return the yarnApplicationTimeStamp value
     */
    public Long yarnApplicationTimeStamp() {
        return this.yarnApplicationTimeStamp;
    }

    /**
     * Get the specific compilation mode for the job used during execution. If this is not specified during submission, the server will determine the optimal compilation mode. Possible values include: 'Semantic', 'Full', 'SingleBox'.
     *
     * @return the compileMode value
     */
    public CompileMode compileMode() {
        return this.compileMode;
    }

}
