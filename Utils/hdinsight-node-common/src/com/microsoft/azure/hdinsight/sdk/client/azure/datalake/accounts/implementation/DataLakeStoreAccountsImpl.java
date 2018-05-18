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
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.DataLakeStoreAccounts;
import com.google.common.reflect.TypeToken;
import com.microsoft.azure.AzureServiceFuture;
import com.microsoft.azure.CloudException;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.AddDataLakeStoreParameters;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.DataLakeStoreAccountInformation;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.PageImpl;
import com.microsoft.azure.ListOperationCallback;
import com.microsoft.azure.Page;
import com.microsoft.azure.PagedList;
import com.microsoft.rest.ServiceCallback;
import com.microsoft.rest.ServiceFuture;
import com.microsoft.rest.ServiceResponse;
import java.io.IOException;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.HTTP;
import retrofit2.http.Path;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;
import retrofit2.Response;
import rx.functions.Func1;
import rx.Observable;

/**
 * An instance of this class provides access to all the operations defined
 * in DataLakeStoreAccounts.
 */
public class DataLakeStoreAccountsImpl implements DataLakeStoreAccounts {
    /** The Retrofit service to perform REST calls. */
    private DataLakeStoreAccountsService service;
    /** The service client containing this operation class. */
    private DataLakeAnalyticsAccountManagementClientImpl client;

    /**
     * Initializes an instance of DataLakeStoreAccountsImpl.
     *
     * @param retrofit the Retrofit instance built from a Retrofit Builder.
     * @param client the instance of the service client containing this operation class.
     */
    public DataLakeStoreAccountsImpl(Retrofit retrofit, DataLakeAnalyticsAccountManagementClientImpl client) {
        this.service = retrofit.create(DataLakeStoreAccountsService.class);
        this.client = client;
    }

    /**
     * The interface defining all the services for DataLakeStoreAccounts to be
     * used by Retrofit to perform actually REST calls.
     */
    interface DataLakeStoreAccountsService {
        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.DataLakeStoreAccounts listByAccount" })
        @GET("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/dataLakeStoreAccounts")
        Observable<Response<ResponseBody>> listByAccount(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Query("$filter") String filter, @Query("$top") Integer top, @Query("$skip") Integer skip, @Query("$select") String select, @Query("$orderby") String orderby, @Query("$count") Boolean count, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.DataLakeStoreAccounts add" })
        @PUT("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/dataLakeStoreAccounts/{dataLakeStoreAccountName}")
        Observable<Response<ResponseBody>> add(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Path("dataLakeStoreAccountName") String dataLakeStoreAccountName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Body AddDataLakeStoreParameters parameters, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.DataLakeStoreAccounts get" })
        @GET("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/dataLakeStoreAccounts/{dataLakeStoreAccountName}")
        Observable<Response<ResponseBody>> get(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Path("dataLakeStoreAccountName") String dataLakeStoreAccountName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.DataLakeStoreAccounts delete" })
        @HTTP(path = "subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/dataLakeStoreAccounts/{dataLakeStoreAccountName}", method = "DELETE", hasBody = true)
        Observable<Response<ResponseBody>> delete(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Path("dataLakeStoreAccountName") String dataLakeStoreAccountName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.DataLakeStoreAccounts listByAccountNext" })
        @GET
        Observable<Response<ResponseBody>> listByAccountNext(@Url String nextUrl, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

    }

    /**
     * Gets the first page of Data Lake Store accounts linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;DataLakeStoreAccountInformation&gt; object if successful.
     */
    public PagedList<DataLakeStoreAccountInformation> listByAccount(final String resourceGroupName, final String accountName) {
        ServiceResponse<Page<DataLakeStoreAccountInformation>> response = listByAccountSinglePageAsync(resourceGroupName, accountName).toBlocking().single();
        return new PagedList<DataLakeStoreAccountInformation>(response.body()) {
            @Override
            public Page<DataLakeStoreAccountInformation> nextPage(String nextPageLink) {
                return listByAccountNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Gets the first page of Data Lake Store accounts linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<DataLakeStoreAccountInformation>> listByAccountAsync(final String resourceGroupName, final String accountName, final ListOperationCallback<DataLakeStoreAccountInformation> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listByAccountSinglePageAsync(resourceGroupName, accountName),
            new Func1<String, Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>> call(String nextPageLink) {
                    return listByAccountNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Gets the first page of Data Lake Store accounts linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeStoreAccountInformation&gt; object
     */
    public Observable<Page<DataLakeStoreAccountInformation>> listByAccountAsync(final String resourceGroupName, final String accountName) {
        return listByAccountWithServiceResponseAsync(resourceGroupName, accountName)
            .map(new Func1<ServiceResponse<Page<DataLakeStoreAccountInformation>>, Page<DataLakeStoreAccountInformation>>() {
                @Override
                public Page<DataLakeStoreAccountInformation> call(ServiceResponse<Page<DataLakeStoreAccountInformation>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Gets the first page of Data Lake Store accounts linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeStoreAccountInformation&gt; object
     */
    public Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>> listByAccountWithServiceResponseAsync(final String resourceGroupName, final String accountName) {
        return listByAccountSinglePageAsync(resourceGroupName, accountName)
            .concatMap(new Func1<ServiceResponse<Page<DataLakeStoreAccountInformation>>, Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>> call(ServiceResponse<Page<DataLakeStoreAccountInformation>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listByAccountNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Gets the first page of Data Lake Store accounts linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;DataLakeStoreAccountInformation&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>> listByAccountSinglePageAsync(final String resourceGroupName, final String accountName) {
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
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<DataLakeStoreAccountInformation>> result = listByAccountDelegate(response);
                        return Observable.just(new ServiceResponse<Page<DataLakeStoreAccountInformation>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    /**
     * Gets the first page of Data Lake Store accounts linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param filter OData filter. Optional.
     * @param top The number of items to return. Optional.
     * @param skip The number of items to skip over before returning elements. Optional.
     * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
     * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
     * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;DataLakeStoreAccountInformation&gt; object if successful.
     */
    public PagedList<DataLakeStoreAccountInformation> listByAccount(final String resourceGroupName, final String accountName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count) {
        ServiceResponse<Page<DataLakeStoreAccountInformation>> response = listByAccountSinglePageAsync(resourceGroupName, accountName, filter, top, skip, select, orderby, count).toBlocking().single();
        return new PagedList<DataLakeStoreAccountInformation>(response.body()) {
            @Override
            public Page<DataLakeStoreAccountInformation> nextPage(String nextPageLink) {
                return listByAccountNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Gets the first page of Data Lake Store accounts linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param filter OData filter. Optional.
     * @param top The number of items to return. Optional.
     * @param skip The number of items to skip over before returning elements. Optional.
     * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
     * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
     * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<DataLakeStoreAccountInformation>> listByAccountAsync(final String resourceGroupName, final String accountName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count, final ListOperationCallback<DataLakeStoreAccountInformation> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listByAccountSinglePageAsync(resourceGroupName, accountName, filter, top, skip, select, orderby, count),
            new Func1<String, Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>> call(String nextPageLink) {
                    return listByAccountNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Gets the first page of Data Lake Store accounts linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param filter OData filter. Optional.
     * @param top The number of items to return. Optional.
     * @param skip The number of items to skip over before returning elements. Optional.
     * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
     * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
     * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeStoreAccountInformation&gt; object
     */
    public Observable<Page<DataLakeStoreAccountInformation>> listByAccountAsync(final String resourceGroupName, final String accountName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count) {
        return listByAccountWithServiceResponseAsync(resourceGroupName, accountName, filter, top, skip, select, orderby, count)
            .map(new Func1<ServiceResponse<Page<DataLakeStoreAccountInformation>>, Page<DataLakeStoreAccountInformation>>() {
                @Override
                public Page<DataLakeStoreAccountInformation> call(ServiceResponse<Page<DataLakeStoreAccountInformation>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Gets the first page of Data Lake Store accounts linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param filter OData filter. Optional.
     * @param top The number of items to return. Optional.
     * @param skip The number of items to skip over before returning elements. Optional.
     * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
     * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
     * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeStoreAccountInformation&gt; object
     */
    public Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>> listByAccountWithServiceResponseAsync(final String resourceGroupName, final String accountName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count) {
        return listByAccountSinglePageAsync(resourceGroupName, accountName, filter, top, skip, select, orderby, count)
            .concatMap(new Func1<ServiceResponse<Page<DataLakeStoreAccountInformation>>, Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>> call(ServiceResponse<Page<DataLakeStoreAccountInformation>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listByAccountNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Gets the first page of Data Lake Store accounts linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
    ServiceResponse<PageImpl<DataLakeStoreAccountInformation>> * @param resourceGroupName The name of the Azure resource group.
    ServiceResponse<PageImpl<DataLakeStoreAccountInformation>> * @param accountName The name of the Data Lake Analytics account.
    ServiceResponse<PageImpl<DataLakeStoreAccountInformation>> * @param filter OData filter. Optional.
    ServiceResponse<PageImpl<DataLakeStoreAccountInformation>> * @param top The number of items to return. Optional.
    ServiceResponse<PageImpl<DataLakeStoreAccountInformation>> * @param skip The number of items to skip over before returning elements. Optional.
    ServiceResponse<PageImpl<DataLakeStoreAccountInformation>> * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
    ServiceResponse<PageImpl<DataLakeStoreAccountInformation>> * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
    ServiceResponse<PageImpl<DataLakeStoreAccountInformation>> * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;DataLakeStoreAccountInformation&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>> listByAccountSinglePageAsync(final String resourceGroupName, final String accountName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count) {
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
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<DataLakeStoreAccountInformation>> result = listByAccountDelegate(response);
                        return Observable.just(new ServiceResponse<Page<DataLakeStoreAccountInformation>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<DataLakeStoreAccountInformation>> listByAccountDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<DataLakeStoreAccountInformation>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<DataLakeStoreAccountInformation>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Updates the specified Data Lake Analytics account to include the additional Data Lake Store account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param dataLakeStoreAccountName The name of the Data Lake Store account to add.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     */
    public void add(String resourceGroupName, String accountName, String dataLakeStoreAccountName) {
        addWithServiceResponseAsync(resourceGroupName, accountName, dataLakeStoreAccountName).toBlocking().single().body();
    }

    /**
     * Updates the specified Data Lake Analytics account to include the additional Data Lake Store account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param dataLakeStoreAccountName The name of the Data Lake Store account to add.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<Void> addAsync(String resourceGroupName, String accountName, String dataLakeStoreAccountName, final ServiceCallback<Void> serviceCallback) {
        return ServiceFuture.fromResponse(addWithServiceResponseAsync(resourceGroupName, accountName, dataLakeStoreAccountName), serviceCallback);
    }

    /**
     * Updates the specified Data Lake Analytics account to include the additional Data Lake Store account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param dataLakeStoreAccountName The name of the Data Lake Store account to add.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<Void> addAsync(String resourceGroupName, String accountName, String dataLakeStoreAccountName) {
        return addWithServiceResponseAsync(resourceGroupName, accountName, dataLakeStoreAccountName).map(new Func1<ServiceResponse<Void>, Void>() {
            @Override
            public Void call(ServiceResponse<Void> response) {
                return response.body();
            }
        });
    }

    /**
     * Updates the specified Data Lake Analytics account to include the additional Data Lake Store account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param dataLakeStoreAccountName The name of the Data Lake Store account to add.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<ServiceResponse<Void>> addWithServiceResponseAsync(String resourceGroupName, String accountName, String dataLakeStoreAccountName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (dataLakeStoreAccountName == null) {
            throw new IllegalArgumentException("Parameter dataLakeStoreAccountName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        final String suffix = null;
        AddDataLakeStoreParameters parameters = new AddDataLakeStoreParameters();
        parameters.withSuffix(null);
        return service.add(this.client.subscriptionId(), resourceGroupName, accountName, dataLakeStoreAccountName, this.client.apiVersion(), this.client.acceptLanguage(), parameters, this.client.userAgent())
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

    /**
     * Updates the specified Data Lake Analytics account to include the additional Data Lake Store account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param dataLakeStoreAccountName The name of the Data Lake Store account to add.
     * @param suffix The optional suffix for the Data Lake Store account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     */
    public void add(String resourceGroupName, String accountName, String dataLakeStoreAccountName, String suffix) {
        addWithServiceResponseAsync(resourceGroupName, accountName, dataLakeStoreAccountName, suffix).toBlocking().single().body();
    }

    /**
     * Updates the specified Data Lake Analytics account to include the additional Data Lake Store account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param dataLakeStoreAccountName The name of the Data Lake Store account to add.
     * @param suffix The optional suffix for the Data Lake Store account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<Void> addAsync(String resourceGroupName, String accountName, String dataLakeStoreAccountName, String suffix, final ServiceCallback<Void> serviceCallback) {
        return ServiceFuture.fromResponse(addWithServiceResponseAsync(resourceGroupName, accountName, dataLakeStoreAccountName, suffix), serviceCallback);
    }

    /**
     * Updates the specified Data Lake Analytics account to include the additional Data Lake Store account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param dataLakeStoreAccountName The name of the Data Lake Store account to add.
     * @param suffix The optional suffix for the Data Lake Store account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<Void> addAsync(String resourceGroupName, String accountName, String dataLakeStoreAccountName, String suffix) {
        return addWithServiceResponseAsync(resourceGroupName, accountName, dataLakeStoreAccountName, suffix).map(new Func1<ServiceResponse<Void>, Void>() {
            @Override
            public Void call(ServiceResponse<Void> response) {
                return response.body();
            }
        });
    }

    /**
     * Updates the specified Data Lake Analytics account to include the additional Data Lake Store account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param dataLakeStoreAccountName The name of the Data Lake Store account to add.
     * @param suffix The optional suffix for the Data Lake Store account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<ServiceResponse<Void>> addWithServiceResponseAsync(String resourceGroupName, String accountName, String dataLakeStoreAccountName, String suffix) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (dataLakeStoreAccountName == null) {
            throw new IllegalArgumentException("Parameter dataLakeStoreAccountName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        AddDataLakeStoreParameters parameters = null;
        if (suffix != null) {
            parameters = new AddDataLakeStoreParameters();
            parameters.withSuffix(suffix);
        }
        return service.add(this.client.subscriptionId(), resourceGroupName, accountName, dataLakeStoreAccountName, this.client.apiVersion(), this.client.acceptLanguage(), parameters, this.client.userAgent())
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
     * Gets the specified Data Lake Store account details in the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param dataLakeStoreAccountName The name of the Data Lake Store account to retrieve
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the DataLakeStoreAccountInformation object if successful.
     */
    public DataLakeStoreAccountInformation get(String resourceGroupName, String accountName, String dataLakeStoreAccountName) {
        return getWithServiceResponseAsync(resourceGroupName, accountName, dataLakeStoreAccountName).toBlocking().single().body();
    }

    /**
     * Gets the specified Data Lake Store account details in the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param dataLakeStoreAccountName The name of the Data Lake Store account to retrieve
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<DataLakeStoreAccountInformation> getAsync(String resourceGroupName, String accountName, String dataLakeStoreAccountName, final ServiceCallback<DataLakeStoreAccountInformation> serviceCallback) {
        return ServiceFuture.fromResponse(getWithServiceResponseAsync(resourceGroupName, accountName, dataLakeStoreAccountName), serviceCallback);
    }

    /**
     * Gets the specified Data Lake Store account details in the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param dataLakeStoreAccountName The name of the Data Lake Store account to retrieve
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeStoreAccountInformation object
     */
    public Observable<DataLakeStoreAccountInformation> getAsync(String resourceGroupName, String accountName, String dataLakeStoreAccountName) {
        return getWithServiceResponseAsync(resourceGroupName, accountName, dataLakeStoreAccountName).map(new Func1<ServiceResponse<DataLakeStoreAccountInformation>, DataLakeStoreAccountInformation>() {
            @Override
            public DataLakeStoreAccountInformation call(ServiceResponse<DataLakeStoreAccountInformation> response) {
                return response.body();
            }
        });
    }

    /**
     * Gets the specified Data Lake Store account details in the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param dataLakeStoreAccountName The name of the Data Lake Store account to retrieve
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeStoreAccountInformation object
     */
    public Observable<ServiceResponse<DataLakeStoreAccountInformation>> getWithServiceResponseAsync(String resourceGroupName, String accountName, String dataLakeStoreAccountName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (dataLakeStoreAccountName == null) {
            throw new IllegalArgumentException("Parameter dataLakeStoreAccountName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        return service.get(this.client.subscriptionId(), resourceGroupName, accountName, dataLakeStoreAccountName, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<DataLakeStoreAccountInformation>>>() {
                @Override
                public Observable<ServiceResponse<DataLakeStoreAccountInformation>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<DataLakeStoreAccountInformation> clientResponse = getDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<DataLakeStoreAccountInformation> getDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<DataLakeStoreAccountInformation, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<DataLakeStoreAccountInformation>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Updates the Data Lake Analytics account specified to remove the specified Data Lake Store account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param dataLakeStoreAccountName The name of the Data Lake Store account to remove
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     */
    public void delete(String resourceGroupName, String accountName, String dataLakeStoreAccountName) {
        deleteWithServiceResponseAsync(resourceGroupName, accountName, dataLakeStoreAccountName).toBlocking().single().body();
    }

    /**
     * Updates the Data Lake Analytics account specified to remove the specified Data Lake Store account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param dataLakeStoreAccountName The name of the Data Lake Store account to remove
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<Void> deleteAsync(String resourceGroupName, String accountName, String dataLakeStoreAccountName, final ServiceCallback<Void> serviceCallback) {
        return ServiceFuture.fromResponse(deleteWithServiceResponseAsync(resourceGroupName, accountName, dataLakeStoreAccountName), serviceCallback);
    }

    /**
     * Updates the Data Lake Analytics account specified to remove the specified Data Lake Store account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param dataLakeStoreAccountName The name of the Data Lake Store account to remove
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<Void> deleteAsync(String resourceGroupName, String accountName, String dataLakeStoreAccountName) {
        return deleteWithServiceResponseAsync(resourceGroupName, accountName, dataLakeStoreAccountName).map(new Func1<ServiceResponse<Void>, Void>() {
            @Override
            public Void call(ServiceResponse<Void> response) {
                return response.body();
            }
        });
    }

    /**
     * Updates the Data Lake Analytics account specified to remove the specified Data Lake Store account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param dataLakeStoreAccountName The name of the Data Lake Store account to remove
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<ServiceResponse<Void>> deleteWithServiceResponseAsync(String resourceGroupName, String accountName, String dataLakeStoreAccountName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (dataLakeStoreAccountName == null) {
            throw new IllegalArgumentException("Parameter dataLakeStoreAccountName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        return service.delete(this.client.subscriptionId(), resourceGroupName, accountName, dataLakeStoreAccountName, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
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
     * Gets the first page of Data Lake Store accounts linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;DataLakeStoreAccountInformation&gt; object if successful.
     */
    public PagedList<DataLakeStoreAccountInformation> listByAccountNext(final String nextPageLink) {
        ServiceResponse<Page<DataLakeStoreAccountInformation>> response = listByAccountNextSinglePageAsync(nextPageLink).toBlocking().single();
        return new PagedList<DataLakeStoreAccountInformation>(response.body()) {
            @Override
            public Page<DataLakeStoreAccountInformation> nextPage(String nextPageLink) {
                return listByAccountNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Gets the first page of Data Lake Store accounts linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @param serviceFuture the ServiceFuture object tracking the Retrofit calls
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<DataLakeStoreAccountInformation>> listByAccountNextAsync(final String nextPageLink, final ServiceFuture<List<DataLakeStoreAccountInformation>> serviceFuture, final ListOperationCallback<DataLakeStoreAccountInformation> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listByAccountNextSinglePageAsync(nextPageLink),
            new Func1<String, Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>> call(String nextPageLink) {
                    return listByAccountNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Gets the first page of Data Lake Store accounts linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeStoreAccountInformation&gt; object
     */
    public Observable<Page<DataLakeStoreAccountInformation>> listByAccountNextAsync(final String nextPageLink) {
        return listByAccountNextWithServiceResponseAsync(nextPageLink)
            .map(new Func1<ServiceResponse<Page<DataLakeStoreAccountInformation>>, Page<DataLakeStoreAccountInformation>>() {
                @Override
                public Page<DataLakeStoreAccountInformation> call(ServiceResponse<Page<DataLakeStoreAccountInformation>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Gets the first page of Data Lake Store accounts linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeStoreAccountInformation&gt; object
     */
    public Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>> listByAccountNextWithServiceResponseAsync(final String nextPageLink) {
        return listByAccountNextSinglePageAsync(nextPageLink)
            .concatMap(new Func1<ServiceResponse<Page<DataLakeStoreAccountInformation>>, Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>> call(ServiceResponse<Page<DataLakeStoreAccountInformation>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listByAccountNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Gets the first page of Data Lake Store accounts linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
    ServiceResponse<PageImpl<DataLakeStoreAccountInformation>> * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;DataLakeStoreAccountInformation&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>> listByAccountNextSinglePageAsync(final String nextPageLink) {
        if (nextPageLink == null) {
            throw new IllegalArgumentException("Parameter nextPageLink is required and cannot be null.");
        }
        String nextUrl = String.format("%s", nextPageLink);
        return service.listByAccountNext(nextUrl, this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeStoreAccountInformation>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<DataLakeStoreAccountInformation>> result = listByAccountNextDelegate(response);
                        return Observable.just(new ServiceResponse<Page<DataLakeStoreAccountInformation>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<DataLakeStoreAccountInformation>> listByAccountNextDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<DataLakeStoreAccountInformation>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<DataLakeStoreAccountInformation>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

}
