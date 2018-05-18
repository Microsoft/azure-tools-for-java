/**
 * Copyright (c) Microsoft Corporation
 *
 * All rights reserved.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.implementation;

import retrofit2.Retrofit;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.StorageAccounts;
import com.google.common.reflect.TypeToken;
import com.microsoft.azure.AzureServiceFuture;
import com.microsoft.azure.CloudException;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.AddStorageAccountParameters;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.PageImpl;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.SasTokenInformation;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.StorageAccountInformation;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.StorageContainer;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.UpdateStorageAccountParameters;
import com.microsoft.azure.ListOperationCallback;
import com.microsoft.azure.Page;
import com.microsoft.azure.PagedList;
import com.microsoft.rest.ServiceCallback;
import com.microsoft.rest.ServiceFuture;
import com.microsoft.rest.ServiceResponse;
import com.microsoft.rest.Validator;
import java.io.IOException;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.HTTP;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;
import retrofit2.Response;
import rx.functions.Func1;
import rx.Observable;

/**
 * An instance of this class provides access to all the operations defined
 * in StorageAccounts.
 */
public class StorageAccountsImpl implements StorageAccounts {
    /** The Retrofit service to perform REST calls. */
    private StorageAccountsService service;
    /** The service client containing this operation class. */
    private DataLakeAnalyticsAccountManagementClientImpl client;

    /**
     * Initializes an instance of StorageAccountsImpl.
     *
     * @param retrofit the Retrofit instance built from a Retrofit Builder.
     * @param client the instance of the service client containing this operation class.
     */
    public StorageAccountsImpl(Retrofit retrofit, DataLakeAnalyticsAccountManagementClientImpl client) {
        this.service = retrofit.create(StorageAccountsService.class);
        this.client = client;
    }

    /**
     * The interface defining all the services for StorageAccounts to be
     * used by Retrofit to perform actually REST calls.
     */
    interface StorageAccountsService {
        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.StorageAccounts listByAccount" })
        @GET("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/storageAccounts")
        Observable<Response<ResponseBody>> listByAccount(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Query("$filter") String filter, @Query("$top") Integer top, @Query("$skip") Integer skip, @Query("$select") String select, @Query("$orderby") String orderby, @Query("$count") Boolean count, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.StorageAccounts add" })
        @PUT("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/storageAccounts/{storageAccountName}")
        Observable<Response<ResponseBody>> add(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Path("storageAccountName") String storageAccountName, @Body AddStorageAccountParameters parameters, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.StorageAccounts get" })
        @GET("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/storageAccounts/{storageAccountName}")
        Observable<Response<ResponseBody>> get(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Path("storageAccountName") String storageAccountName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.StorageAccounts update" })
        @PATCH("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/storageAccounts/{storageAccountName}")
        Observable<Response<ResponseBody>> update(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Path("storageAccountName") String storageAccountName, @Body UpdateStorageAccountParameters parameters, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.StorageAccounts delete" })
        @HTTP(path = "subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/storageAccounts/{storageAccountName}", method = "DELETE", hasBody = true)
        Observable<Response<ResponseBody>> delete(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Path("storageAccountName") String storageAccountName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.StorageAccounts listStorageContainers" })
        @GET("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/storageAccounts/{storageAccountName}/containers")
        Observable<Response<ResponseBody>> listStorageContainers(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Path("storageAccountName") String storageAccountName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.StorageAccounts getStorageContainer" })
        @GET("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/storageAccounts/{storageAccountName}/containers/{containerName}")
        Observable<Response<ResponseBody>> getStorageContainer(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Path("storageAccountName") String storageAccountName, @Path("containerName") String containerName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.StorageAccounts listSasTokens" })
        @POST("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/storageAccounts/{storageAccountName}/containers/{containerName}/listSasTokens")
        Observable<Response<ResponseBody>> listSasTokens(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Path("storageAccountName") String storageAccountName, @Path("containerName") String containerName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.StorageAccounts listByAccountNext" })
        @GET
        Observable<Response<ResponseBody>> listByAccountNext(@Url String nextUrl, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.StorageAccounts listStorageContainersNext" })
        @GET
        Observable<Response<ResponseBody>> listStorageContainersNext(@Url String nextUrl, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.StorageAccounts listSasTokensNext" })
        @GET
        Observable<Response<ResponseBody>> listSasTokensNext(@Url String nextUrl, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

    }

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;StorageAccountInformation&gt; object if successful.
     */
    public PagedList<StorageAccountInformation> listByAccount(final String resourceGroupName, final String accountName) {
        ServiceResponse<Page<StorageAccountInformation>> response = listByAccountSinglePageAsync(resourceGroupName, accountName).toBlocking().single();
        return new PagedList<StorageAccountInformation>(response.body()) {
            @Override
            public Page<StorageAccountInformation> nextPage(String nextPageLink) {
                return listByAccountNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<StorageAccountInformation>> listByAccountAsync(final String resourceGroupName, final String accountName, final ListOperationCallback<StorageAccountInformation> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listByAccountSinglePageAsync(resourceGroupName, accountName),
            new Func1<String, Observable<ServiceResponse<Page<StorageAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<StorageAccountInformation>>> call(String nextPageLink) {
                    return listByAccountNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageAccountInformation&gt; object
     */
    public Observable<Page<StorageAccountInformation>> listByAccountAsync(final String resourceGroupName, final String accountName) {
        return listByAccountWithServiceResponseAsync(resourceGroupName, accountName)
            .map(new Func1<ServiceResponse<Page<StorageAccountInformation>>, Page<StorageAccountInformation>>() {
                @Override
                public Page<StorageAccountInformation> call(ServiceResponse<Page<StorageAccountInformation>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageAccountInformation&gt; object
     */
    public Observable<ServiceResponse<Page<StorageAccountInformation>>> listByAccountWithServiceResponseAsync(final String resourceGroupName, final String accountName) {
        return listByAccountSinglePageAsync(resourceGroupName, accountName)
            .concatMap(new Func1<ServiceResponse<Page<StorageAccountInformation>>, Observable<ServiceResponse<Page<StorageAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<StorageAccountInformation>>> call(ServiceResponse<Page<StorageAccountInformation>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listByAccountNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;StorageAccountInformation&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<StorageAccountInformation>>> listByAccountSinglePageAsync(final String resourceGroupName, final String accountName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        final String filter = null;
        final Integer top = null;
        final Integer skip = null;
        final String select = null;
        final String orderby = null;
        final Boolean count = null;
        return service.listByAccount(this.client.subscriptionId(), resourceGroupName, accountName, filter, top, skip, select, orderby, count, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<StorageAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<StorageAccountInformation>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<StorageAccountInformation>> result = listByAccountDelegate(response);
                        return Observable.just(new ServiceResponse<Page<StorageAccountInformation>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param filter The OData filter. Optional.
     * @param top The number of items to return. Optional.
     * @param skip The number of items to skip over before returning elements. Optional.
     * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
     * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
     * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;StorageAccountInformation&gt; object if successful.
     */
    public PagedList<StorageAccountInformation> listByAccount(final String resourceGroupName, final String accountName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count) {
        ServiceResponse<Page<StorageAccountInformation>> response = listByAccountSinglePageAsync(resourceGroupName, accountName, filter, top, skip, select, orderby, count).toBlocking().single();
        return new PagedList<StorageAccountInformation>(response.body()) {
            @Override
            public Page<StorageAccountInformation> nextPage(String nextPageLink) {
                return listByAccountNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param filter The OData filter. Optional.
     * @param top The number of items to return. Optional.
     * @param skip The number of items to skip over before returning elements. Optional.
     * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
     * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
     * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<StorageAccountInformation>> listByAccountAsync(final String resourceGroupName, final String accountName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count, final ListOperationCallback<StorageAccountInformation> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listByAccountSinglePageAsync(resourceGroupName, accountName, filter, top, skip, select, orderby, count),
            new Func1<String, Observable<ServiceResponse<Page<StorageAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<StorageAccountInformation>>> call(String nextPageLink) {
                    return listByAccountNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param filter The OData filter. Optional.
     * @param top The number of items to return. Optional.
     * @param skip The number of items to skip over before returning elements. Optional.
     * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
     * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
     * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageAccountInformation&gt; object
     */
    public Observable<Page<StorageAccountInformation>> listByAccountAsync(final String resourceGroupName, final String accountName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count) {
        return listByAccountWithServiceResponseAsync(resourceGroupName, accountName, filter, top, skip, select, orderby, count)
            .map(new Func1<ServiceResponse<Page<StorageAccountInformation>>, Page<StorageAccountInformation>>() {
                @Override
                public Page<StorageAccountInformation> call(ServiceResponse<Page<StorageAccountInformation>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param filter The OData filter. Optional.
     * @param top The number of items to return. Optional.
     * @param skip The number of items to skip over before returning elements. Optional.
     * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
     * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
     * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageAccountInformation&gt; object
     */
    public Observable<ServiceResponse<Page<StorageAccountInformation>>> listByAccountWithServiceResponseAsync(final String resourceGroupName, final String accountName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count) {
        return listByAccountSinglePageAsync(resourceGroupName, accountName, filter, top, skip, select, orderby, count)
            .concatMap(new Func1<ServiceResponse<Page<StorageAccountInformation>>, Observable<ServiceResponse<Page<StorageAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<StorageAccountInformation>>> call(ServiceResponse<Page<StorageAccountInformation>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listByAccountNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
    ServiceResponse<PageImpl<StorageAccountInformation>> * @param resourceGroupName The name of the Azure resource group.
    ServiceResponse<PageImpl<StorageAccountInformation>> * @param accountName The name of the Data Lake Analytics account.
    ServiceResponse<PageImpl<StorageAccountInformation>> * @param filter The OData filter. Optional.
    ServiceResponse<PageImpl<StorageAccountInformation>> * @param top The number of items to return. Optional.
    ServiceResponse<PageImpl<StorageAccountInformation>> * @param skip The number of items to skip over before returning elements. Optional.
    ServiceResponse<PageImpl<StorageAccountInformation>> * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
    ServiceResponse<PageImpl<StorageAccountInformation>> * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
    ServiceResponse<PageImpl<StorageAccountInformation>> * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;StorageAccountInformation&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<StorageAccountInformation>>> listByAccountSinglePageAsync(final String resourceGroupName, final String accountName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        return service.listByAccount(this.client.subscriptionId(), resourceGroupName, accountName, filter, top, skip, select, orderby, count, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<StorageAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<StorageAccountInformation>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<StorageAccountInformation>> result = listByAccountDelegate(response);
                        return Observable.just(new ServiceResponse<Page<StorageAccountInformation>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<StorageAccountInformation>> listByAccountDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<StorageAccountInformation>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<StorageAccountInformation>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Updates the specified Data Lake Analytics account to add an Azure Storage account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure Storage account to add
     * @param parameters The parameters containing the access key and optional suffix for the Azure Storage Account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     */
    public void add(String resourceGroupName, String accountName, String storageAccountName, AddStorageAccountParameters parameters) {
        addWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName, parameters).toBlocking().single().body();
    }

    /**
     * Updates the specified Data Lake Analytics account to add an Azure Storage account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure Storage account to add
     * @param parameters The parameters containing the access key and optional suffix for the Azure Storage Account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<Void> addAsync(String resourceGroupName, String accountName, String storageAccountName, AddStorageAccountParameters parameters, final ServiceCallback<Void> serviceCallback) {
        return ServiceFuture.fromResponse(addWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName, parameters), serviceCallback);
    }

    /**
     * Updates the specified Data Lake Analytics account to add an Azure Storage account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure Storage account to add
     * @param parameters The parameters containing the access key and optional suffix for the Azure Storage Account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<Void> addAsync(String resourceGroupName, String accountName, String storageAccountName, AddStorageAccountParameters parameters) {
        return addWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName, parameters).map(new Func1<ServiceResponse<Void>, Void>() {
            @Override
            public Void call(ServiceResponse<Void> response) {
                return response.body();
            }
        });
    }

    /**
     * Updates the specified Data Lake Analytics account to add an Azure Storage account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure Storage account to add
     * @param parameters The parameters containing the access key and optional suffix for the Azure Storage Account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<ServiceResponse<Void>> addWithServiceResponseAsync(String resourceGroupName, String accountName, String storageAccountName, AddStorageAccountParameters parameters) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (storageAccountName == null) {
            throw new IllegalArgumentException("Parameter storageAccountName is required and cannot be null.");
        }
        if (parameters == null) {
            throw new IllegalArgumentException("Parameter parameters is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        Validator.validate(parameters);
        return service.add(this.client.subscriptionId(), resourceGroupName, accountName, storageAccountName, parameters, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Void>>>() {
                @Override
                public Observable<ServiceResponse<Void>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<Void> clientResponse = addDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<Void> addDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<Void, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<Void>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Gets the specified Azure Storage account linked to the given Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure Storage account for which to retrieve the details.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the StorageAccountInformation object if successful.
     */
    public StorageAccountInformation get(String resourceGroupName, String accountName, String storageAccountName) {
        return getWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName).toBlocking().single().body();
    }

    /**
     * Gets the specified Azure Storage account linked to the given Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure Storage account for which to retrieve the details.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<StorageAccountInformation> getAsync(String resourceGroupName, String accountName, String storageAccountName, final ServiceCallback<StorageAccountInformation> serviceCallback) {
        return ServiceFuture.fromResponse(getWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName), serviceCallback);
    }

    /**
     * Gets the specified Azure Storage account linked to the given Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure Storage account for which to retrieve the details.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the StorageAccountInformation object
     */
    public Observable<StorageAccountInformation> getAsync(String resourceGroupName, String accountName, String storageAccountName) {
        return getWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName).map(new Func1<ServiceResponse<StorageAccountInformation>, StorageAccountInformation>() {
            @Override
            public StorageAccountInformation call(ServiceResponse<StorageAccountInformation> response) {
                return response.body();
            }
        });
    }

    /**
     * Gets the specified Azure Storage account linked to the given Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure Storage account for which to retrieve the details.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the StorageAccountInformation object
     */
    public Observable<ServiceResponse<StorageAccountInformation>> getWithServiceResponseAsync(String resourceGroupName, String accountName, String storageAccountName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (storageAccountName == null) {
            throw new IllegalArgumentException("Parameter storageAccountName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        return service.get(this.client.subscriptionId(), resourceGroupName, accountName, storageAccountName, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<StorageAccountInformation>>>() {
                @Override
                public Observable<ServiceResponse<StorageAccountInformation>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<StorageAccountInformation> clientResponse = getDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<StorageAccountInformation> getDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<StorageAccountInformation, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<StorageAccountInformation>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Updates the Data Lake Analytics account to replace Azure Storage blob account details, such as the access key and/or suffix.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The Azure Storage account to modify
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     */
    public void update(String resourceGroupName, String accountName, String storageAccountName) {
        updateWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName).toBlocking().single().body();
    }

    /**
     * Updates the Data Lake Analytics account to replace Azure Storage blob account details, such as the access key and/or suffix.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The Azure Storage account to modify
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<Void> updateAsync(String resourceGroupName, String accountName, String storageAccountName, final ServiceCallback<Void> serviceCallback) {
        return ServiceFuture.fromResponse(updateWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName), serviceCallback);
    }

    /**
     * Updates the Data Lake Analytics account to replace Azure Storage blob account details, such as the access key and/or suffix.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The Azure Storage account to modify
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<Void> updateAsync(String resourceGroupName, String accountName, String storageAccountName) {
        return updateWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName).map(new Func1<ServiceResponse<Void>, Void>() {
            @Override
            public Void call(ServiceResponse<Void> response) {
                return response.body();
            }
        });
    }

    /**
     * Updates the Data Lake Analytics account to replace Azure Storage blob account details, such as the access key and/or suffix.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The Azure Storage account to modify
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<ServiceResponse<Void>> updateWithServiceResponseAsync(String resourceGroupName, String accountName, String storageAccountName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (storageAccountName == null) {
            throw new IllegalArgumentException("Parameter storageAccountName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        final UpdateStorageAccountParameters parameters = null;
        return service.update(this.client.subscriptionId(), resourceGroupName, accountName, storageAccountName, parameters, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Void>>>() {
                @Override
                public Observable<ServiceResponse<Void>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<Void> clientResponse = updateDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    /**
     * Updates the Data Lake Analytics account to replace Azure Storage blob account details, such as the access key and/or suffix.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The Azure Storage account to modify
     * @param parameters The parameters containing the access key and suffix to update the storage account with, if any. Passing nothing results in no change.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     */
    public void update(String resourceGroupName, String accountName, String storageAccountName, UpdateStorageAccountParameters parameters) {
        updateWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName, parameters).toBlocking().single().body();
    }

    /**
     * Updates the Data Lake Analytics account to replace Azure Storage blob account details, such as the access key and/or suffix.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The Azure Storage account to modify
     * @param parameters The parameters containing the access key and suffix to update the storage account with, if any. Passing nothing results in no change.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<Void> updateAsync(String resourceGroupName, String accountName, String storageAccountName, UpdateStorageAccountParameters parameters, final ServiceCallback<Void> serviceCallback) {
        return ServiceFuture.fromResponse(updateWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName, parameters), serviceCallback);
    }

    /**
     * Updates the Data Lake Analytics account to replace Azure Storage blob account details, such as the access key and/or suffix.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The Azure Storage account to modify
     * @param parameters The parameters containing the access key and suffix to update the storage account with, if any. Passing nothing results in no change.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<Void> updateAsync(String resourceGroupName, String accountName, String storageAccountName, UpdateStorageAccountParameters parameters) {
        return updateWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName, parameters).map(new Func1<ServiceResponse<Void>, Void>() {
            @Override
            public Void call(ServiceResponse<Void> response) {
                return response.body();
            }
        });
    }

    /**
     * Updates the Data Lake Analytics account to replace Azure Storage blob account details, such as the access key and/or suffix.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The Azure Storage account to modify
     * @param parameters The parameters containing the access key and suffix to update the storage account with, if any. Passing nothing results in no change.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<ServiceResponse<Void>> updateWithServiceResponseAsync(String resourceGroupName, String accountName, String storageAccountName, UpdateStorageAccountParameters parameters) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (storageAccountName == null) {
            throw new IllegalArgumentException("Parameter storageAccountName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        Validator.validate(parameters);
        return service.update(this.client.subscriptionId(), resourceGroupName, accountName, storageAccountName, parameters, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Void>>>() {
                @Override
                public Observable<ServiceResponse<Void>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<Void> clientResponse = updateDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<Void> updateDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<Void, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<Void>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Updates the specified Data Lake Analytics account to remove an Azure Storage account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure Storage account to remove
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     */
    public void delete(String resourceGroupName, String accountName, String storageAccountName) {
        deleteWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName).toBlocking().single().body();
    }

    /**
     * Updates the specified Data Lake Analytics account to remove an Azure Storage account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure Storage account to remove
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<Void> deleteAsync(String resourceGroupName, String accountName, String storageAccountName, final ServiceCallback<Void> serviceCallback) {
        return ServiceFuture.fromResponse(deleteWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName), serviceCallback);
    }

    /**
     * Updates the specified Data Lake Analytics account to remove an Azure Storage account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure Storage account to remove
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<Void> deleteAsync(String resourceGroupName, String accountName, String storageAccountName) {
        return deleteWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName).map(new Func1<ServiceResponse<Void>, Void>() {
            @Override
            public Void call(ServiceResponse<Void> response) {
                return response.body();
            }
        });
    }

    /**
     * Updates the specified Data Lake Analytics account to remove an Azure Storage account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure Storage account to remove
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<ServiceResponse<Void>> deleteWithServiceResponseAsync(String resourceGroupName, String accountName, String storageAccountName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (storageAccountName == null) {
            throw new IllegalArgumentException("Parameter storageAccountName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        return service.delete(this.client.subscriptionId(), resourceGroupName, accountName, storageAccountName, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Void>>>() {
                @Override
                public Observable<ServiceResponse<Void>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<Void> clientResponse = deleteDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<Void> deleteDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<Void, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<Void>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Lists the Azure Storage containers, if any, associated with the specified Data Lake Analytics and Azure Storage account combination. The response includes a link to the next page of results, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure storage account from which to list blob containers.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;StorageContainer&gt; object if successful.
     */
    public PagedList<StorageContainer> listStorageContainers(final String resourceGroupName, final String accountName, final String storageAccountName) {
        ServiceResponse<Page<StorageContainer>> response = listStorageContainersSinglePageAsync(resourceGroupName, accountName, storageAccountName).toBlocking().single();
        return new PagedList<StorageContainer>(response.body()) {
            @Override
            public Page<StorageContainer> nextPage(String nextPageLink) {
                return listStorageContainersNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Lists the Azure Storage containers, if any, associated with the specified Data Lake Analytics and Azure Storage account combination. The response includes a link to the next page of results, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure storage account from which to list blob containers.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<StorageContainer>> listStorageContainersAsync(final String resourceGroupName, final String accountName, final String storageAccountName, final ListOperationCallback<StorageContainer> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listStorageContainersSinglePageAsync(resourceGroupName, accountName, storageAccountName),
            new Func1<String, Observable<ServiceResponse<Page<StorageContainer>>>>() {
                @Override
                public Observable<ServiceResponse<Page<StorageContainer>>> call(String nextPageLink) {
                    return listStorageContainersNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Lists the Azure Storage containers, if any, associated with the specified Data Lake Analytics and Azure Storage account combination. The response includes a link to the next page of results, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure storage account from which to list blob containers.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageContainer&gt; object
     */
    public Observable<Page<StorageContainer>> listStorageContainersAsync(final String resourceGroupName, final String accountName, final String storageAccountName) {
        return listStorageContainersWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName)
            .map(new Func1<ServiceResponse<Page<StorageContainer>>, Page<StorageContainer>>() {
                @Override
                public Page<StorageContainer> call(ServiceResponse<Page<StorageContainer>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Lists the Azure Storage containers, if any, associated with the specified Data Lake Analytics and Azure Storage account combination. The response includes a link to the next page of results, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure storage account from which to list blob containers.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageContainer&gt; object
     */
    public Observable<ServiceResponse<Page<StorageContainer>>> listStorageContainersWithServiceResponseAsync(final String resourceGroupName, final String accountName, final String storageAccountName) {
        return listStorageContainersSinglePageAsync(resourceGroupName, accountName, storageAccountName)
            .concatMap(new Func1<ServiceResponse<Page<StorageContainer>>, Observable<ServiceResponse<Page<StorageContainer>>>>() {
                @Override
                public Observable<ServiceResponse<Page<StorageContainer>>> call(ServiceResponse<Page<StorageContainer>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listStorageContainersNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Lists the Azure Storage containers, if any, associated with the specified Data Lake Analytics and Azure Storage account combination. The response includes a link to the next page of results, if any.
     *
    ServiceResponse<PageImpl<StorageContainer>> * @param resourceGroupName The name of the Azure resource group.
    ServiceResponse<PageImpl<StorageContainer>> * @param accountName The name of the Data Lake Analytics account.
    ServiceResponse<PageImpl<StorageContainer>> * @param storageAccountName The name of the Azure storage account from which to list blob containers.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;StorageContainer&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<StorageContainer>>> listStorageContainersSinglePageAsync(final String resourceGroupName, final String accountName, final String storageAccountName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (storageAccountName == null) {
            throw new IllegalArgumentException("Parameter storageAccountName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        return service.listStorageContainers(this.client.subscriptionId(), resourceGroupName, accountName, storageAccountName, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<StorageContainer>>>>() {
                @Override
                public Observable<ServiceResponse<Page<StorageContainer>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<StorageContainer>> result = listStorageContainersDelegate(response);
                        return Observable.just(new ServiceResponse<Page<StorageContainer>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<StorageContainer>> listStorageContainersDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<StorageContainer>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<StorageContainer>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Gets the specified Azure Storage container associated with the given Data Lake Analytics and Azure Storage accounts.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure storage account from which to retrieve the blob container.
     * @param containerName The name of the Azure storage container to retrieve
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the StorageContainer object if successful.
     */
    public StorageContainer getStorageContainer(String resourceGroupName, String accountName, String storageAccountName, String containerName) {
        return getStorageContainerWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName, containerName).toBlocking().single().body();
    }

    /**
     * Gets the specified Azure Storage container associated with the given Data Lake Analytics and Azure Storage accounts.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure storage account from which to retrieve the blob container.
     * @param containerName The name of the Azure storage container to retrieve
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<StorageContainer> getStorageContainerAsync(String resourceGroupName, String accountName, String storageAccountName, String containerName, final ServiceCallback<StorageContainer> serviceCallback) {
        return ServiceFuture.fromResponse(getStorageContainerWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName, containerName), serviceCallback);
    }

    /**
     * Gets the specified Azure Storage container associated with the given Data Lake Analytics and Azure Storage accounts.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure storage account from which to retrieve the blob container.
     * @param containerName The name of the Azure storage container to retrieve
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the StorageContainer object
     */
    public Observable<StorageContainer> getStorageContainerAsync(String resourceGroupName, String accountName, String storageAccountName, String containerName) {
        return getStorageContainerWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName, containerName).map(new Func1<ServiceResponse<StorageContainer>, StorageContainer>() {
            @Override
            public StorageContainer call(ServiceResponse<StorageContainer> response) {
                return response.body();
            }
        });
    }

    /**
     * Gets the specified Azure Storage container associated with the given Data Lake Analytics and Azure Storage accounts.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure storage account from which to retrieve the blob container.
     * @param containerName The name of the Azure storage container to retrieve
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the StorageContainer object
     */
    public Observable<ServiceResponse<StorageContainer>> getStorageContainerWithServiceResponseAsync(String resourceGroupName, String accountName, String storageAccountName, String containerName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (storageAccountName == null) {
            throw new IllegalArgumentException("Parameter storageAccountName is required and cannot be null.");
        }
        if (containerName == null) {
            throw new IllegalArgumentException("Parameter containerName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        return service.getStorageContainer(this.client.subscriptionId(), resourceGroupName, accountName, storageAccountName, containerName, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<StorageContainer>>>() {
                @Override
                public Observable<ServiceResponse<StorageContainer>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<StorageContainer> clientResponse = getStorageContainerDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<StorageContainer> getStorageContainerDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<StorageContainer, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<StorageContainer>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Gets the SAS token associated with the specified Data Lake Analytics and Azure Storage account and container combination.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure storage account for which the SAS token is being requested.
     * @param containerName The name of the Azure storage container for which the SAS token is being requested.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;SasTokenInformation&gt; object if successful.
     */
    public PagedList<SasTokenInformation> listSasTokens(final String resourceGroupName, final String accountName, final String storageAccountName, final String containerName) {
        ServiceResponse<Page<SasTokenInformation>> response = listSasTokensSinglePageAsync(resourceGroupName, accountName, storageAccountName, containerName).toBlocking().single();
        return new PagedList<SasTokenInformation>(response.body()) {
            @Override
            public Page<SasTokenInformation> nextPage(String nextPageLink) {
                return listSasTokensNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Gets the SAS token associated with the specified Data Lake Analytics and Azure Storage account and container combination.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure storage account for which the SAS token is being requested.
     * @param containerName The name of the Azure storage container for which the SAS token is being requested.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<SasTokenInformation>> listSasTokensAsync(final String resourceGroupName, final String accountName, final String storageAccountName, final String containerName, final ListOperationCallback<SasTokenInformation> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listSasTokensSinglePageAsync(resourceGroupName, accountName, storageAccountName, containerName),
            new Func1<String, Observable<ServiceResponse<Page<SasTokenInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<SasTokenInformation>>> call(String nextPageLink) {
                    return listSasTokensNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Gets the SAS token associated with the specified Data Lake Analytics and Azure Storage account and container combination.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure storage account for which the SAS token is being requested.
     * @param containerName The name of the Azure storage container for which the SAS token is being requested.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;SasTokenInformation&gt; object
     */
    public Observable<Page<SasTokenInformation>> listSasTokensAsync(final String resourceGroupName, final String accountName, final String storageAccountName, final String containerName) {
        return listSasTokensWithServiceResponseAsync(resourceGroupName, accountName, storageAccountName, containerName)
            .map(new Func1<ServiceResponse<Page<SasTokenInformation>>, Page<SasTokenInformation>>() {
                @Override
                public Page<SasTokenInformation> call(ServiceResponse<Page<SasTokenInformation>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Gets the SAS token associated with the specified Data Lake Analytics and Azure Storage account and container combination.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure storage account for which the SAS token is being requested.
     * @param containerName The name of the Azure storage container for which the SAS token is being requested.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;SasTokenInformation&gt; object
     */
    public Observable<ServiceResponse<Page<SasTokenInformation>>> listSasTokensWithServiceResponseAsync(final String resourceGroupName, final String accountName, final String storageAccountName, final String containerName) {
        return listSasTokensSinglePageAsync(resourceGroupName, accountName, storageAccountName, containerName)
            .concatMap(new Func1<ServiceResponse<Page<SasTokenInformation>>, Observable<ServiceResponse<Page<SasTokenInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<SasTokenInformation>>> call(ServiceResponse<Page<SasTokenInformation>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listSasTokensNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Gets the SAS token associated with the specified Data Lake Analytics and Azure Storage account and container combination.
     *
    ServiceResponse<PageImpl<SasTokenInformation>> * @param resourceGroupName The name of the Azure resource group.
    ServiceResponse<PageImpl<SasTokenInformation>> * @param accountName The name of the Data Lake Analytics account.
    ServiceResponse<PageImpl<SasTokenInformation>> * @param storageAccountName The name of the Azure storage account for which the SAS token is being requested.
    ServiceResponse<PageImpl<SasTokenInformation>> * @param containerName The name of the Azure storage container for which the SAS token is being requested.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;SasTokenInformation&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<SasTokenInformation>>> listSasTokensSinglePageAsync(final String resourceGroupName, final String accountName, final String storageAccountName, final String containerName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (storageAccountName == null) {
            throw new IllegalArgumentException("Parameter storageAccountName is required and cannot be null.");
        }
        if (containerName == null) {
            throw new IllegalArgumentException("Parameter containerName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        return service.listSasTokens(this.client.subscriptionId(), resourceGroupName, accountName, storageAccountName, containerName, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<SasTokenInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<SasTokenInformation>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<SasTokenInformation>> result = listSasTokensDelegate(response);
                        return Observable.just(new ServiceResponse<Page<SasTokenInformation>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<SasTokenInformation>> listSasTokensDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<SasTokenInformation>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<SasTokenInformation>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;StorageAccountInformation&gt; object if successful.
     */
    public PagedList<StorageAccountInformation> listByAccountNext(final String nextPageLink) {
        ServiceResponse<Page<StorageAccountInformation>> response = listByAccountNextSinglePageAsync(nextPageLink).toBlocking().single();
        return new PagedList<StorageAccountInformation>(response.body()) {
            @Override
            public Page<StorageAccountInformation> nextPage(String nextPageLink) {
                return listByAccountNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @param serviceFuture the ServiceFuture object tracking the Retrofit calls
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<StorageAccountInformation>> listByAccountNextAsync(final String nextPageLink, final ServiceFuture<List<StorageAccountInformation>> serviceFuture, final ListOperationCallback<StorageAccountInformation> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listByAccountNextSinglePageAsync(nextPageLink),
            new Func1<String, Observable<ServiceResponse<Page<StorageAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<StorageAccountInformation>>> call(String nextPageLink) {
                    return listByAccountNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageAccountInformation&gt; object
     */
    public Observable<Page<StorageAccountInformation>> listByAccountNextAsync(final String nextPageLink) {
        return listByAccountNextWithServiceResponseAsync(nextPageLink)
            .map(new Func1<ServiceResponse<Page<StorageAccountInformation>>, Page<StorageAccountInformation>>() {
                @Override
                public Page<StorageAccountInformation> call(ServiceResponse<Page<StorageAccountInformation>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageAccountInformation&gt; object
     */
    public Observable<ServiceResponse<Page<StorageAccountInformation>>> listByAccountNextWithServiceResponseAsync(final String nextPageLink) {
        return listByAccountNextSinglePageAsync(nextPageLink)
            .concatMap(new Func1<ServiceResponse<Page<StorageAccountInformation>>, Observable<ServiceResponse<Page<StorageAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<StorageAccountInformation>>> call(ServiceResponse<Page<StorageAccountInformation>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listByAccountNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
    ServiceResponse<PageImpl<StorageAccountInformation>> * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;StorageAccountInformation&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<StorageAccountInformation>>> listByAccountNextSinglePageAsync(final String nextPageLink) {
        if (nextPageLink == null) {
            throw new IllegalArgumentException("Parameter nextPageLink is required and cannot be null.");
        }
        String nextUrl = String.format("%s", nextPageLink);
        return service.listByAccountNext(nextUrl, this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<StorageAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<StorageAccountInformation>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<StorageAccountInformation>> result = listByAccountNextDelegate(response);
                        return Observable.just(new ServiceResponse<Page<StorageAccountInformation>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<StorageAccountInformation>> listByAccountNextDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<StorageAccountInformation>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<StorageAccountInformation>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Lists the Azure Storage containers, if any, associated with the specified Data Lake Analytics and Azure Storage account combination. The response includes a link to the next page of results, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;StorageContainer&gt; object if successful.
     */
    public PagedList<StorageContainer> listStorageContainersNext(final String nextPageLink) {
        ServiceResponse<Page<StorageContainer>> response = listStorageContainersNextSinglePageAsync(nextPageLink).toBlocking().single();
        return new PagedList<StorageContainer>(response.body()) {
            @Override
            public Page<StorageContainer> nextPage(String nextPageLink) {
                return listStorageContainersNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Lists the Azure Storage containers, if any, associated with the specified Data Lake Analytics and Azure Storage account combination. The response includes a link to the next page of results, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @param serviceFuture the ServiceFuture object tracking the Retrofit calls
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<StorageContainer>> listStorageContainersNextAsync(final String nextPageLink, final ServiceFuture<List<StorageContainer>> serviceFuture, final ListOperationCallback<StorageContainer> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listStorageContainersNextSinglePageAsync(nextPageLink),
            new Func1<String, Observable<ServiceResponse<Page<StorageContainer>>>>() {
                @Override
                public Observable<ServiceResponse<Page<StorageContainer>>> call(String nextPageLink) {
                    return listStorageContainersNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Lists the Azure Storage containers, if any, associated with the specified Data Lake Analytics and Azure Storage account combination. The response includes a link to the next page of results, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageContainer&gt; object
     */
    public Observable<Page<StorageContainer>> listStorageContainersNextAsync(final String nextPageLink) {
        return listStorageContainersNextWithServiceResponseAsync(nextPageLink)
            .map(new Func1<ServiceResponse<Page<StorageContainer>>, Page<StorageContainer>>() {
                @Override
                public Page<StorageContainer> call(ServiceResponse<Page<StorageContainer>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Lists the Azure Storage containers, if any, associated with the specified Data Lake Analytics and Azure Storage account combination. The response includes a link to the next page of results, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageContainer&gt; object
     */
    public Observable<ServiceResponse<Page<StorageContainer>>> listStorageContainersNextWithServiceResponseAsync(final String nextPageLink) {
        return listStorageContainersNextSinglePageAsync(nextPageLink)
            .concatMap(new Func1<ServiceResponse<Page<StorageContainer>>, Observable<ServiceResponse<Page<StorageContainer>>>>() {
                @Override
                public Observable<ServiceResponse<Page<StorageContainer>>> call(ServiceResponse<Page<StorageContainer>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listStorageContainersNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Lists the Azure Storage containers, if any, associated with the specified Data Lake Analytics and Azure Storage account combination. The response includes a link to the next page of results, if any.
     *
    ServiceResponse<PageImpl<StorageContainer>> * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;StorageContainer&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<StorageContainer>>> listStorageContainersNextSinglePageAsync(final String nextPageLink) {
        if (nextPageLink == null) {
            throw new IllegalArgumentException("Parameter nextPageLink is required and cannot be null.");
        }
        String nextUrl = String.format("%s", nextPageLink);
        return service.listStorageContainersNext(nextUrl, this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<StorageContainer>>>>() {
                @Override
                public Observable<ServiceResponse<Page<StorageContainer>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<StorageContainer>> result = listStorageContainersNextDelegate(response);
                        return Observable.just(new ServiceResponse<Page<StorageContainer>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<StorageContainer>> listStorageContainersNextDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<StorageContainer>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<StorageContainer>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Gets the SAS token associated with the specified Data Lake Analytics and Azure Storage account and container combination.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;SasTokenInformation&gt; object if successful.
     */
    public PagedList<SasTokenInformation> listSasTokensNext(final String nextPageLink) {
        ServiceResponse<Page<SasTokenInformation>> response = listSasTokensNextSinglePageAsync(nextPageLink).toBlocking().single();
        return new PagedList<SasTokenInformation>(response.body()) {
            @Override
            public Page<SasTokenInformation> nextPage(String nextPageLink) {
                return listSasTokensNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Gets the SAS token associated with the specified Data Lake Analytics and Azure Storage account and container combination.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @param serviceFuture the ServiceFuture object tracking the Retrofit calls
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<SasTokenInformation>> listSasTokensNextAsync(final String nextPageLink, final ServiceFuture<List<SasTokenInformation>> serviceFuture, final ListOperationCallback<SasTokenInformation> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listSasTokensNextSinglePageAsync(nextPageLink),
            new Func1<String, Observable<ServiceResponse<Page<SasTokenInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<SasTokenInformation>>> call(String nextPageLink) {
                    return listSasTokensNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Gets the SAS token associated with the specified Data Lake Analytics and Azure Storage account and container combination.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;SasTokenInformation&gt; object
     */
    public Observable<Page<SasTokenInformation>> listSasTokensNextAsync(final String nextPageLink) {
        return listSasTokensNextWithServiceResponseAsync(nextPageLink)
            .map(new Func1<ServiceResponse<Page<SasTokenInformation>>, Page<SasTokenInformation>>() {
                @Override
                public Page<SasTokenInformation> call(ServiceResponse<Page<SasTokenInformation>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Gets the SAS token associated with the specified Data Lake Analytics and Azure Storage account and container combination.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;SasTokenInformation&gt; object
     */
    public Observable<ServiceResponse<Page<SasTokenInformation>>> listSasTokensNextWithServiceResponseAsync(final String nextPageLink) {
        return listSasTokensNextSinglePageAsync(nextPageLink)
            .concatMap(new Func1<ServiceResponse<Page<SasTokenInformation>>, Observable<ServiceResponse<Page<SasTokenInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<SasTokenInformation>>> call(ServiceResponse<Page<SasTokenInformation>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listSasTokensNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Gets the SAS token associated with the specified Data Lake Analytics and Azure Storage account and container combination.
     *
    ServiceResponse<PageImpl<SasTokenInformation>> * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;SasTokenInformation&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<SasTokenInformation>>> listSasTokensNextSinglePageAsync(final String nextPageLink) {
        if (nextPageLink == null) {
            throw new IllegalArgumentException("Parameter nextPageLink is required and cannot be null.");
        }
        String nextUrl = String.format("%s", nextPageLink);
        return service.listSasTokensNext(nextUrl, this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<SasTokenInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<SasTokenInformation>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<SasTokenInformation>> result = listSasTokensNextDelegate(response);
                        return Observable.just(new ServiceResponse<Page<SasTokenInformation>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<SasTokenInformation>> listSasTokensNextDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<SasTokenInformation>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<SasTokenInformation>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

}
