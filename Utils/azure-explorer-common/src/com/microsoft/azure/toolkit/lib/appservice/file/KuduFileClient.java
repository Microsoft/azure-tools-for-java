/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.lib.appservice.file;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.*;
import rx.Observable;

import java.util.List;

public interface KuduFileClient extends AppServiceFileClient {
    @Headers({
                 "Content-Type: application/json; charset=utf-8",
                 "x-ms-logging-context: com.microsoft.azure.management.appservice.WebApps getFile"
             })
    @GET("api/vfs/{path}")
    @Streaming
    Observable<ResponseBody> getFileContent(@Path("path") String path);

    @Headers({
                 "Content-Type: application/json; charset=utf-8",
                 "x-ms-logging-context: com.microsoft.azure.management.appservice.WebApps getFilesInDirectory"
             })
    @GET("api/vfs/{path}/")
    Observable<List<AppServiceFile>> getFilesInDirectory(@Path("path") String path);

    @Headers({
            "Content-Type: application/json; charset=utf-8",
            "x-ms-logging-context: com.microsoft.azure.management.appservice.WebApps saveFile",
            "If-Match: *"
    })
    @PUT("api/vfs/{path}")
    Observable<Void> saveFile(@Path("path") String path, @Body RequestBody requestBody);

    @Headers({
                 "Content-Type: application/json; charset=utf-8",
                 "x-ms-logging-context: com.microsoft.azure.management.appservice.WebApps createDirectory"
             })
    @PUT("api/vfs/{path}/")
    Observable<ResponseBody> createDirectory(@Path("path") String path);

    @Headers({
                 "Content-Type: application/json; charset=utf-8",
                 "x-ms-logging-context: com.microsoft.azure.management.appservice.WebApps deleteFile"
             })
    @DELETE("api/vfs/{path}")
    Observable<ResponseBody> deleteFile(@Path("path") String path);
}
