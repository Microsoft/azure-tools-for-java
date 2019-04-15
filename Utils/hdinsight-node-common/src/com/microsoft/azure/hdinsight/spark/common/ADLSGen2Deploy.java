/**
 * Copyright (c) Microsoft Corporation
 * <p>
 * <p>
 * All rights reserved.
 * <p>
 * <p>
 * MIT License
 * <p>
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p>
 * <p>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 * <p>
 * <p>
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.microsoft.azure.hdinsight.spark.common;

import com.microsoft.azure.hdinsight.common.MessageInfoType;
import com.microsoft.azure.hdinsight.common.logger.ILogger;
import com.microsoft.azure.hdinsight.sdk.cluster.IClusterDetail;
import com.microsoft.azure.hdinsight.sdk.common.HttpObservable;
import com.microsoft.azure.hdinsight.sdk.common.SharedKeyHttpObservable;
import com.microsoft.azure.hdinsight.sdk.storage.ADLSGen2StorageAccount;
import com.microsoft.azure.hdinsight.sdk.storage.HDStorageAccount;
import com.microsoft.azure.hdinsight.sdk.storage.adlsgen2.ADLSGen2FSOperation;
import com.microsoft.azure.hdinsight.spark.jobs.JobUtils;
import com.microsoft.azuretools.azurecommons.helpers.NotNull;
import com.microsoft.azuretools.azurecommons.helpers.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import rx.Observable;
import rx.Observer;
import rx.exceptions.Exceptions;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.microsoft.azure.hdinsight.common.MessageInfoType.Info;

public class ADLSGen2Deploy implements Deployable, ILogger {
    @NotNull
    IClusterDetail cluster;

    @Nullable
    public HttpObservable http;

    public ADLSGen2Deploy(IClusterDetail cluster, HttpObservable http) {
        this.cluster = cluster;
        this.http = http;
        if (http == null) {
            throw new IllegalArgumentException("Cluster type doesen't support adls gen2 storage type.");
        }
    }

    @Override
    public URI getUploadDir(@NotNull String rootPath) {
        return URI.create(rootPath)
                .resolve(JobUtils.getFormatPathByDate() + "/");
    }

    @NotNull
    public Observable<String> deploy(@NotNull File src, @NotNull URI destURI) {
        // four steps to upload via adls gen2 rest api
        // 1.put request to create new dir
        // 2.put request to create new file(artifact) which is empty
        // 3.patch request to append data to file
        // 4.patch request to flush data to file

        //remove request / end otherwise invalid url response
        String destStr = destURI.toString();
        String dirPath = destStr.endsWith("/") ? destStr.substring(0, destStr.length() - 1) : destStr;
        String filePath = String.format("%s/%s", dirPath, src.getName());

        ADLSGen2FSOperation op = new ADLSGen2FSOperation(this.http);
        return op.createDir(dirPath)
                .onErrorReturn(err -> {
                    if (err.getMessage().contains(String.valueOf(HttpStatus.SC_FORBIDDEN))) {
                        throw new IllegalArgumentException("Failed to upload Spark application artifacts.The access key is invalid.");
                    } else {
                        throw Exceptions.propagate(err);
                    }
                })
                .doOnNext(ignore -> log().info(String.format("Create filesystem %s successfully.", dirPath)))
                .flatMap(ignore -> op.createFile(filePath))
                .flatMap(ignore -> op.uploadData(filePath, src))
                .doOnNext(ignore -> log().info(String.format("Append data to file %s successfully.", filePath)))
                .map(ignored -> {
                    try {
                        return getArtifactUploadedPath(filePath);
                    } catch (URISyntaxException ex) {
                        throw new RuntimeException(new IllegalArgumentException("Can not get valid artifact upload path" + ex.toString()));
                    }
                });
    }

    @Override
    @Nullable
    public String getArtifactUploadedPath(String rootPath) throws URISyntaxException {
        //convert https://fullAccountName/fileSystem/sparksubmission/guid/artifact.jar to /SparkSubmission/xxxx
        int index = rootPath.indexOf("SparkSubmission");
        return String.format("/%s", rootPath.substring(index));
    }
}