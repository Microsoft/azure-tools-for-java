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
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.Accounts;
import com.google.common.reflect.TypeToken;
import com.microsoft.azure.AzureServiceFuture;
import com.microsoft.azure.CloudException;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.CheckNameAvailabilityParameters;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.CreateDataLakeAnalyticsAccountParameters;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.DataLakeAnalyticsAccount;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.DataLakeAnalyticsAccountBasic;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.NameAvailabilityInformation;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.PageImpl;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.UpdateDataLakeAnalyticsAccountParameters;
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
 * in Accounts.
 */
public class AccountsImpl implements Accounts {
    /** The Retrofit service to perform REST calls. */
    private AccountsService service;
    /** The service client containing this operation class. */
    private DataLakeAnalyticsAccountManagementClientImpl client;

    /**
     * Initializes an instance of AccountsImpl.
     *
     * @param retrofit the Retrofit instance built from a Retrofit Builder.
     * @param client the instance of the service client containing this operation class.
     */
    public AccountsImpl(Retrofit retrofit, DataLakeAnalyticsAccountManagementClientImpl client) {
        this.service = retrofit.create(AccountsService.class);
        this.client = client;
    }

    /**
     * The interface defining all the services for Accounts to be
     * used by Retrofit to perform actually REST calls.
     */
    interface AccountsService {
        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.Accounts list" })
        @GET("subscriptions/{subscriptionId}/providers/Microsoft.DataLakeAnalytics/accounts")
        Observable<Response<ResponseBody>> list(@Path("subscriptionId") String subscriptionId, @Query("$filter") String filter, @Query("$top") Integer top, @Query("$skip") Integer skip, @Query("$select") String select, @Query("$orderby") String orderby, @Query("$count") Boolean count, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.Accounts listByResourceGroup" })
        @GET("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts")
        Observable<Response<ResponseBody>> listByResourceGroup(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Query("$filter") String filter, @Query("$top") Integer top, @Query("$skip") Integer skip, @Query("$select") String select, @Query("$orderby") String orderby, @Query("$count") Boolean count, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.Accounts create" })
        @PUT("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}")
        Observable<Response<ResponseBody>> create(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Body CreateDataLakeAnalyticsAccountParameters parameters, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.Accounts beginCreate" })
        @PUT("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}")
        Observable<Response<ResponseBody>> beginCreate(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Body CreateDataLakeAnalyticsAccountParameters parameters, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.Accounts get" })
        @GET("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}")
        Observable<Response<ResponseBody>> get(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.Accounts update" })
        @PATCH("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}")
        Observable<Response<ResponseBody>> update(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Body UpdateDataLakeAnalyticsAccountParameters parameters, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.Accounts beginUpdate" })
        @PATCH("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}")
        Observable<Response<ResponseBody>> beginUpdate(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Body UpdateDataLakeAnalyticsAccountParameters parameters, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.Accounts delete" })
        @HTTP(path = "subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}", method = "DELETE", hasBody = true)
        Observable<Response<ResponseBody>> delete(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.Accounts beginDelete" })
        @HTTP(path = "subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}", method = "DELETE", hasBody = true)
        Observable<Response<ResponseBody>> beginDelete(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.Accounts checkNameAvailability" })
        @POST("subscriptions/{subscriptionId}/providers/Microsoft.DataLakeAnalytics/locations/{location}/checkNameAvailability")
        Observable<Response<ResponseBody>> checkNameAvailability(@Path("subscriptionId") String subscriptionId, @Path("location") String location, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Body CheckNameAvailabilityParameters parameters, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.Accounts listNext" })
        @GET
        Observable<Response<ResponseBody>> listNext(@Url String nextUrl, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.Accounts listByResourceGroupNext" })
        @GET
        Observable<Response<ResponseBody>> listByResourceGroupNext(@Url String nextUrl, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object if successful.
     */
    public PagedList<DataLakeAnalyticsAccountBasic> list() {
        ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> response = listSinglePageAsync().toBlocking().single();
        return new PagedList<DataLakeAnalyticsAccountBasic>(response.body()) {
            @Override
            public Page<DataLakeAnalyticsAccountBasic> nextPage(String nextPageLink) {
                return listNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<DataLakeAnalyticsAccountBasic>> listAsync(final ListOperationCallback<DataLakeAnalyticsAccountBasic> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listSinglePageAsync(),
            new Func1<String, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(String nextPageLink) {
                    return listNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    public Observable<Page<DataLakeAnalyticsAccountBasic>> listAsync() {
        return listWithServiceResponseAsync()
            .map(new Func1<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>, Page<DataLakeAnalyticsAccountBasic>>() {
                @Override
                public Page<DataLakeAnalyticsAccountBasic> call(ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listWithServiceResponseAsync() {
        return listSinglePageAsync()
            .concatMap(new Func1<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listSinglePageAsync() {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
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
        return service.list(this.client.subscriptionId(), filter, top, skip, select, orderby, count, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> result = listDelegate(response);
                        return Observable.just(new ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @param filter OData filter. Optional.
     * @param top The number of items to return. Optional.
     * @param skip The number of items to skip over before returning elements. Optional.
     * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
     * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
     * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object if successful.
     */
    public PagedList<DataLakeAnalyticsAccountBasic> list(final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count) {
        ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> response = listSinglePageAsync(filter, top, skip, select, orderby, count).toBlocking().single();
        return new PagedList<DataLakeAnalyticsAccountBasic>(response.body()) {
            @Override
            public Page<DataLakeAnalyticsAccountBasic> nextPage(String nextPageLink) {
                return listNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
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
    public ServiceFuture<List<DataLakeAnalyticsAccountBasic>> listAsync(final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count, final ListOperationCallback<DataLakeAnalyticsAccountBasic> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listSinglePageAsync(filter, top, skip, select, orderby, count),
            new Func1<String, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(String nextPageLink) {
                    return listNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @param filter OData filter. Optional.
     * @param top The number of items to return. Optional.
     * @param skip The number of items to skip over before returning elements. Optional.
     * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
     * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
     * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    public Observable<Page<DataLakeAnalyticsAccountBasic>> listAsync(final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count) {
        return listWithServiceResponseAsync(filter, top, skip, select, orderby, count)
            .map(new Func1<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>, Page<DataLakeAnalyticsAccountBasic>>() {
                @Override
                public Page<DataLakeAnalyticsAccountBasic> call(ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @param filter OData filter. Optional.
     * @param top The number of items to return. Optional.
     * @param skip The number of items to skip over before returning elements. Optional.
     * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
     * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
     * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listWithServiceResponseAsync(final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count) {
        return listSinglePageAsync(filter, top, skip, select, orderby, count)
            .concatMap(new Func1<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
    ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> * @param filter OData filter. Optional.
    ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> * @param top The number of items to return. Optional.
    ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> * @param skip The number of items to skip over before returning elements. Optional.
    ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
    ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
    ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listSinglePageAsync(final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        return service.list(this.client.subscriptionId(), filter, top, skip, select, orderby, count, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> result = listDelegate(response);
                        return Observable.just(new ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> listDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<DataLakeAnalyticsAccountBasic>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<DataLakeAnalyticsAccountBasic>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object if successful.
     */
    public PagedList<DataLakeAnalyticsAccountBasic> listByResourceGroup(final String resourceGroupName) {
        ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> response = listByResourceGroupSinglePageAsync(resourceGroupName).toBlocking().single();
        return new PagedList<DataLakeAnalyticsAccountBasic>(response.body()) {
            @Override
            public Page<DataLakeAnalyticsAccountBasic> nextPage(String nextPageLink) {
                return listByResourceGroupNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<DataLakeAnalyticsAccountBasic>> listByResourceGroupAsync(final String resourceGroupName, final ListOperationCallback<DataLakeAnalyticsAccountBasic> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listByResourceGroupSinglePageAsync(resourceGroupName),
            new Func1<String, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(String nextPageLink) {
                    return listByResourceGroupNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    public Observable<Page<DataLakeAnalyticsAccountBasic>> listByResourceGroupAsync(final String resourceGroupName) {
        return listByResourceGroupWithServiceResponseAsync(resourceGroupName)
            .map(new Func1<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>, Page<DataLakeAnalyticsAccountBasic>>() {
                @Override
                public Page<DataLakeAnalyticsAccountBasic> call(ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listByResourceGroupWithServiceResponseAsync(final String resourceGroupName) {
        return listByResourceGroupSinglePageAsync(resourceGroupName)
            .concatMap(new Func1<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listByResourceGroupNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listByResourceGroupSinglePageAsync(final String resourceGroupName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
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
        return service.listByResourceGroup(this.client.subscriptionId(), resourceGroupName, filter, top, skip, select, orderby, count, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> result = listByResourceGroupDelegate(response);
                        return Observable.just(new ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param filter OData filter. Optional.
     * @param top The number of items to return. Optional.
     * @param skip The number of items to skip over before returning elements. Optional.
     * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
     * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
     * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object if successful.
     */
    public PagedList<DataLakeAnalyticsAccountBasic> listByResourceGroup(final String resourceGroupName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count) {
        ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> response = listByResourceGroupSinglePageAsync(resourceGroupName, filter, top, skip, select, orderby, count).toBlocking().single();
        return new PagedList<DataLakeAnalyticsAccountBasic>(response.body()) {
            @Override
            public Page<DataLakeAnalyticsAccountBasic> nextPage(String nextPageLink) {
                return listByResourceGroupNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
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
    public ServiceFuture<List<DataLakeAnalyticsAccountBasic>> listByResourceGroupAsync(final String resourceGroupName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count, final ListOperationCallback<DataLakeAnalyticsAccountBasic> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listByResourceGroupSinglePageAsync(resourceGroupName, filter, top, skip, select, orderby, count),
            new Func1<String, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(String nextPageLink) {
                    return listByResourceGroupNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param filter OData filter. Optional.
     * @param top The number of items to return. Optional.
     * @param skip The number of items to skip over before returning elements. Optional.
     * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
     * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
     * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    public Observable<Page<DataLakeAnalyticsAccountBasic>> listByResourceGroupAsync(final String resourceGroupName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count) {
        return listByResourceGroupWithServiceResponseAsync(resourceGroupName, filter, top, skip, select, orderby, count)
            .map(new Func1<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>, Page<DataLakeAnalyticsAccountBasic>>() {
                @Override
                public Page<DataLakeAnalyticsAccountBasic> call(ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param filter OData filter. Optional.
     * @param top The number of items to return. Optional.
     * @param skip The number of items to skip over before returning elements. Optional.
     * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
     * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
     * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listByResourceGroupWithServiceResponseAsync(final String resourceGroupName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count) {
        return listByResourceGroupSinglePageAsync(resourceGroupName, filter, top, skip, select, orderby, count)
            .concatMap(new Func1<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listByResourceGroupNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
    ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> * @param resourceGroupName The name of the Azure resource group.
    ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> * @param filter OData filter. Optional.
    ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> * @param top The number of items to return. Optional.
    ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> * @param skip The number of items to skip over before returning elements. Optional.
    ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> * @param select OData Select statement. Limits the properties on each entry to just those requested, e.g. Categories?$select=CategoryName,Description. Optional.
    ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> * @param orderby OrderBy clause. One or more comma-separated expressions with an optional "asc" (the default) or "desc" depending on the order you'd like the values sorted, e.g. Categories?$orderby=CategoryName desc. Optional.
    ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> * @param count The Boolean value of true or false to request a count of the matching resources included with the resources in the response, e.g. Categories?$count=true. Optional.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listByResourceGroupSinglePageAsync(final String resourceGroupName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        return service.listByResourceGroup(this.client.subscriptionId(), resourceGroupName, filter, top, skip, select, orderby, count, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> result = listByResourceGroupDelegate(response);
                        return Observable.just(new ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> listByResourceGroupDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<DataLakeAnalyticsAccountBasic>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<DataLakeAnalyticsAccountBasic>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Creates the specified Data Lake Analytics account. This supplies the user with computation services for Data Lake Analytics workloads.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to create a new Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the DataLakeAnalyticsAccount object if successful.
     */
    public DataLakeAnalyticsAccount create(String resourceGroupName, String accountName, CreateDataLakeAnalyticsAccountParameters parameters) {
        return createWithServiceResponseAsync(resourceGroupName, accountName, parameters).toBlocking().last().body();
    }

    /**
     * Creates the specified Data Lake Analytics account. This supplies the user with computation services for Data Lake Analytics workloads.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to create a new Data Lake Analytics account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<DataLakeAnalyticsAccount> createAsync(String resourceGroupName, String accountName, CreateDataLakeAnalyticsAccountParameters parameters, final ServiceCallback<DataLakeAnalyticsAccount> serviceCallback) {
        return ServiceFuture.fromResponse(createWithServiceResponseAsync(resourceGroupName, accountName, parameters), serviceCallback);
    }

    /**
     * Creates the specified Data Lake Analytics account. This supplies the user with computation services for Data Lake Analytics workloads.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to create a new Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    public Observable<DataLakeAnalyticsAccount> createAsync(String resourceGroupName, String accountName, CreateDataLakeAnalyticsAccountParameters parameters) {
        return createWithServiceResponseAsync(resourceGroupName, accountName, parameters).map(new Func1<ServiceResponse<DataLakeAnalyticsAccount>, DataLakeAnalyticsAccount>() {
            @Override
            public DataLakeAnalyticsAccount call(ServiceResponse<DataLakeAnalyticsAccount> response) {
                return response.body();
            }
        });
    }

    /**
     * Creates the specified Data Lake Analytics account. This supplies the user with computation services for Data Lake Analytics workloads.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to create a new Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    public Observable<ServiceResponse<DataLakeAnalyticsAccount>> createWithServiceResponseAsync(String resourceGroupName, String accountName, CreateDataLakeAnalyticsAccountParameters parameters) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (parameters == null) {
            throw new IllegalArgumentException("Parameter parameters is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        Validator.validate(parameters);
        Observable<Response<ResponseBody>> observable = service.create(this.client.subscriptionId(), resourceGroupName, accountName, parameters, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent());
        return client.getAzureClient().getPutOrPatchResultAsync(observable, new TypeToken<DataLakeAnalyticsAccount>() { }.getType());
    }

    /**
     * Creates the specified Data Lake Analytics account. This supplies the user with computation services for Data Lake Analytics workloads.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to create a new Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the DataLakeAnalyticsAccount object if successful.
     */
    public DataLakeAnalyticsAccount beginCreate(String resourceGroupName, String accountName, CreateDataLakeAnalyticsAccountParameters parameters) {
        return beginCreateWithServiceResponseAsync(resourceGroupName, accountName, parameters).toBlocking().single().body();
    }

    /**
     * Creates the specified Data Lake Analytics account. This supplies the user with computation services for Data Lake Analytics workloads.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to create a new Data Lake Analytics account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<DataLakeAnalyticsAccount> beginCreateAsync(String resourceGroupName, String accountName, CreateDataLakeAnalyticsAccountParameters parameters, final ServiceCallback<DataLakeAnalyticsAccount> serviceCallback) {
        return ServiceFuture.fromResponse(beginCreateWithServiceResponseAsync(resourceGroupName, accountName, parameters), serviceCallback);
    }

    /**
     * Creates the specified Data Lake Analytics account. This supplies the user with computation services for Data Lake Analytics workloads.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to create a new Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    public Observable<DataLakeAnalyticsAccount> beginCreateAsync(String resourceGroupName, String accountName, CreateDataLakeAnalyticsAccountParameters parameters) {
        return beginCreateWithServiceResponseAsync(resourceGroupName, accountName, parameters).map(new Func1<ServiceResponse<DataLakeAnalyticsAccount>, DataLakeAnalyticsAccount>() {
            @Override
            public DataLakeAnalyticsAccount call(ServiceResponse<DataLakeAnalyticsAccount> response) {
                return response.body();
            }
        });
    }

    /**
     * Creates the specified Data Lake Analytics account. This supplies the user with computation services for Data Lake Analytics workloads.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to create a new Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    public Observable<ServiceResponse<DataLakeAnalyticsAccount>> beginCreateWithServiceResponseAsync(String resourceGroupName, String accountName, CreateDataLakeAnalyticsAccountParameters parameters) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (parameters == null) {
            throw new IllegalArgumentException("Parameter parameters is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        Validator.validate(parameters);
        return service.beginCreate(this.client.subscriptionId(), resourceGroupName, accountName, parameters, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<DataLakeAnalyticsAccount>>>() {
                @Override
                public Observable<ServiceResponse<DataLakeAnalyticsAccount>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<DataLakeAnalyticsAccount> clientResponse = beginCreateDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<DataLakeAnalyticsAccount> beginCreateDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<DataLakeAnalyticsAccount, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<DataLakeAnalyticsAccount>() { }.getType())
                .register(201, new TypeToken<DataLakeAnalyticsAccount>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Gets details of the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the DataLakeAnalyticsAccount object if successful.
     */
    public DataLakeAnalyticsAccount get(String resourceGroupName, String accountName) {
        return getWithServiceResponseAsync(resourceGroupName, accountName).toBlocking().single().body();
    }

    /**
     * Gets details of the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<DataLakeAnalyticsAccount> getAsync(String resourceGroupName, String accountName, final ServiceCallback<DataLakeAnalyticsAccount> serviceCallback) {
        return ServiceFuture.fromResponse(getWithServiceResponseAsync(resourceGroupName, accountName), serviceCallback);
    }

    /**
     * Gets details of the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    public Observable<DataLakeAnalyticsAccount> getAsync(String resourceGroupName, String accountName) {
        return getWithServiceResponseAsync(resourceGroupName, accountName).map(new Func1<ServiceResponse<DataLakeAnalyticsAccount>, DataLakeAnalyticsAccount>() {
            @Override
            public DataLakeAnalyticsAccount call(ServiceResponse<DataLakeAnalyticsAccount> response) {
                return response.body();
            }
        });
    }

    /**
     * Gets details of the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    public Observable<ServiceResponse<DataLakeAnalyticsAccount>> getWithServiceResponseAsync(String resourceGroupName, String accountName) {
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
        return service.get(this.client.subscriptionId(), resourceGroupName, accountName, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<DataLakeAnalyticsAccount>>>() {
                @Override
                public Observable<ServiceResponse<DataLakeAnalyticsAccount>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<DataLakeAnalyticsAccount> clientResponse = getDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<DataLakeAnalyticsAccount> getDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<DataLakeAnalyticsAccount, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<DataLakeAnalyticsAccount>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the DataLakeAnalyticsAccount object if successful.
     */
    public DataLakeAnalyticsAccount update(String resourceGroupName, String accountName) {
        return updateWithServiceResponseAsync(resourceGroupName, accountName).toBlocking().last().body();
    }

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<DataLakeAnalyticsAccount> updateAsync(String resourceGroupName, String accountName, final ServiceCallback<DataLakeAnalyticsAccount> serviceCallback) {
        return ServiceFuture.fromResponse(updateWithServiceResponseAsync(resourceGroupName, accountName), serviceCallback);
    }

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    public Observable<DataLakeAnalyticsAccount> updateAsync(String resourceGroupName, String accountName) {
        return updateWithServiceResponseAsync(resourceGroupName, accountName).map(new Func1<ServiceResponse<DataLakeAnalyticsAccount>, DataLakeAnalyticsAccount>() {
            @Override
            public DataLakeAnalyticsAccount call(ServiceResponse<DataLakeAnalyticsAccount> response) {
                return response.body();
            }
        });
    }

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    public Observable<ServiceResponse<DataLakeAnalyticsAccount>> updateWithServiceResponseAsync(String resourceGroupName, String accountName) {
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
        final UpdateDataLakeAnalyticsAccountParameters parameters = null;
        Observable<Response<ResponseBody>> observable = service.update(this.client.subscriptionId(), resourceGroupName, accountName, parameters, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent());
        return client.getAzureClient().getPutOrPatchResultAsync(observable, new TypeToken<DataLakeAnalyticsAccount>() { }.getType());
    }
    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to the update Data Lake Analytics account operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the DataLakeAnalyticsAccount object if successful.
     */
    public DataLakeAnalyticsAccount update(String resourceGroupName, String accountName, UpdateDataLakeAnalyticsAccountParameters parameters) {
        return updateWithServiceResponseAsync(resourceGroupName, accountName, parameters).toBlocking().last().body();
    }

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to the update Data Lake Analytics account operation.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<DataLakeAnalyticsAccount> updateAsync(String resourceGroupName, String accountName, UpdateDataLakeAnalyticsAccountParameters parameters, final ServiceCallback<DataLakeAnalyticsAccount> serviceCallback) {
        return ServiceFuture.fromResponse(updateWithServiceResponseAsync(resourceGroupName, accountName, parameters), serviceCallback);
    }

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to the update Data Lake Analytics account operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    public Observable<DataLakeAnalyticsAccount> updateAsync(String resourceGroupName, String accountName, UpdateDataLakeAnalyticsAccountParameters parameters) {
        return updateWithServiceResponseAsync(resourceGroupName, accountName, parameters).map(new Func1<ServiceResponse<DataLakeAnalyticsAccount>, DataLakeAnalyticsAccount>() {
            @Override
            public DataLakeAnalyticsAccount call(ServiceResponse<DataLakeAnalyticsAccount> response) {
                return response.body();
            }
        });
    }

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to the update Data Lake Analytics account operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    public Observable<ServiceResponse<DataLakeAnalyticsAccount>> updateWithServiceResponseAsync(String resourceGroupName, String accountName, UpdateDataLakeAnalyticsAccountParameters parameters) {
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
        Validator.validate(parameters);
        Observable<Response<ResponseBody>> observable = service.update(this.client.subscriptionId(), resourceGroupName, accountName, parameters, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent());
        return client.getAzureClient().getPutOrPatchResultAsync(observable, new TypeToken<DataLakeAnalyticsAccount>() { }.getType());
    }

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the DataLakeAnalyticsAccount object if successful.
     */
    public DataLakeAnalyticsAccount beginUpdate(String resourceGroupName, String accountName) {
        return beginUpdateWithServiceResponseAsync(resourceGroupName, accountName).toBlocking().single().body();
    }

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<DataLakeAnalyticsAccount> beginUpdateAsync(String resourceGroupName, String accountName, final ServiceCallback<DataLakeAnalyticsAccount> serviceCallback) {
        return ServiceFuture.fromResponse(beginUpdateWithServiceResponseAsync(resourceGroupName, accountName), serviceCallback);
    }

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    public Observable<DataLakeAnalyticsAccount> beginUpdateAsync(String resourceGroupName, String accountName) {
        return beginUpdateWithServiceResponseAsync(resourceGroupName, accountName).map(new Func1<ServiceResponse<DataLakeAnalyticsAccount>, DataLakeAnalyticsAccount>() {
            @Override
            public DataLakeAnalyticsAccount call(ServiceResponse<DataLakeAnalyticsAccount> response) {
                return response.body();
            }
        });
    }

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    public Observable<ServiceResponse<DataLakeAnalyticsAccount>> beginUpdateWithServiceResponseAsync(String resourceGroupName, String accountName) {
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
        final UpdateDataLakeAnalyticsAccountParameters parameters = null;
        return service.beginUpdate(this.client.subscriptionId(), resourceGroupName, accountName, parameters, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<DataLakeAnalyticsAccount>>>() {
                @Override
                public Observable<ServiceResponse<DataLakeAnalyticsAccount>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<DataLakeAnalyticsAccount> clientResponse = beginUpdateDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to the update Data Lake Analytics account operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the DataLakeAnalyticsAccount object if successful.
     */
    public DataLakeAnalyticsAccount beginUpdate(String resourceGroupName, String accountName, UpdateDataLakeAnalyticsAccountParameters parameters) {
        return beginUpdateWithServiceResponseAsync(resourceGroupName, accountName, parameters).toBlocking().single().body();
    }

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to the update Data Lake Analytics account operation.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<DataLakeAnalyticsAccount> beginUpdateAsync(String resourceGroupName, String accountName, UpdateDataLakeAnalyticsAccountParameters parameters, final ServiceCallback<DataLakeAnalyticsAccount> serviceCallback) {
        return ServiceFuture.fromResponse(beginUpdateWithServiceResponseAsync(resourceGroupName, accountName, parameters), serviceCallback);
    }

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to the update Data Lake Analytics account operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    public Observable<DataLakeAnalyticsAccount> beginUpdateAsync(String resourceGroupName, String accountName, UpdateDataLakeAnalyticsAccountParameters parameters) {
        return beginUpdateWithServiceResponseAsync(resourceGroupName, accountName, parameters).map(new Func1<ServiceResponse<DataLakeAnalyticsAccount>, DataLakeAnalyticsAccount>() {
            @Override
            public DataLakeAnalyticsAccount call(ServiceResponse<DataLakeAnalyticsAccount> response) {
                return response.body();
            }
        });
    }

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to the update Data Lake Analytics account operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    public Observable<ServiceResponse<DataLakeAnalyticsAccount>> beginUpdateWithServiceResponseAsync(String resourceGroupName, String accountName, UpdateDataLakeAnalyticsAccountParameters parameters) {
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
        Validator.validate(parameters);
        return service.beginUpdate(this.client.subscriptionId(), resourceGroupName, accountName, parameters, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<DataLakeAnalyticsAccount>>>() {
                @Override
                public Observable<ServiceResponse<DataLakeAnalyticsAccount>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<DataLakeAnalyticsAccount> clientResponse = beginUpdateDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<DataLakeAnalyticsAccount> beginUpdateDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<DataLakeAnalyticsAccount, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<DataLakeAnalyticsAccount>() { }.getType())
                .register(201, new TypeToken<DataLakeAnalyticsAccount>() { }.getType())
                .register(202, new TypeToken<DataLakeAnalyticsAccount>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Begins the delete process for the Data Lake Analytics account object specified by the account name.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     */
    public void delete(String resourceGroupName, String accountName) {
        deleteWithServiceResponseAsync(resourceGroupName, accountName).toBlocking().last().body();
    }

    /**
     * Begins the delete process for the Data Lake Analytics account object specified by the account name.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<Void> deleteAsync(String resourceGroupName, String accountName, final ServiceCallback<Void> serviceCallback) {
        return ServiceFuture.fromResponse(deleteWithServiceResponseAsync(resourceGroupName, accountName), serviceCallback);
    }

    /**
     * Begins the delete process for the Data Lake Analytics account object specified by the account name.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    public Observable<Void> deleteAsync(String resourceGroupName, String accountName) {
        return deleteWithServiceResponseAsync(resourceGroupName, accountName).map(new Func1<ServiceResponse<Void>, Void>() {
            @Override
            public Void call(ServiceResponse<Void> response) {
                return response.body();
            }
        });
    }

    /**
     * Begins the delete process for the Data Lake Analytics account object specified by the account name.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable for the request
     */
    public Observable<ServiceResponse<Void>> deleteWithServiceResponseAsync(String resourceGroupName, String accountName) {
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
        Observable<Response<ResponseBody>> observable = service.delete(this.client.subscriptionId(), resourceGroupName, accountName, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent());
        return client.getAzureClient().getPostOrDeleteResultAsync(observable, new TypeToken<Void>() { }.getType());
    }

    /**
     * Begins the delete process for the Data Lake Analytics account object specified by the account name.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     */
    public void beginDelete(String resourceGroupName, String accountName) {
        beginDeleteWithServiceResponseAsync(resourceGroupName, accountName).toBlocking().single().body();
    }

    /**
     * Begins the delete process for the Data Lake Analytics account object specified by the account name.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<Void> beginDeleteAsync(String resourceGroupName, String accountName, final ServiceCallback<Void> serviceCallback) {
        return ServiceFuture.fromResponse(beginDeleteWithServiceResponseAsync(resourceGroupName, accountName), serviceCallback);
    }

    /**
     * Begins the delete process for the Data Lake Analytics account object specified by the account name.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<Void> beginDeleteAsync(String resourceGroupName, String accountName) {
        return beginDeleteWithServiceResponseAsync(resourceGroupName, accountName).map(new Func1<ServiceResponse<Void>, Void>() {
            @Override
            public Void call(ServiceResponse<Void> response) {
                return response.body();
            }
        });
    }

    /**
     * Begins the delete process for the Data Lake Analytics account object specified by the account name.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<ServiceResponse<Void>> beginDeleteWithServiceResponseAsync(String resourceGroupName, String accountName) {
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
        return service.beginDelete(this.client.subscriptionId(), resourceGroupName, accountName, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Void>>>() {
                @Override
                public Observable<ServiceResponse<Void>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<Void> clientResponse = beginDeleteDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<Void> beginDeleteDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<Void, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<Void>() { }.getType())
                .register(202, new TypeToken<Void>() { }.getType())
                .register(204, new TypeToken<Void>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Checks whether the specified account name is available or taken.
     *
     * @param location The resource location without whitespace.
     * @param name The Data Lake Analytics name to check availability for.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the NameAvailabilityInformation object if successful.
     */
    public NameAvailabilityInformation checkNameAvailability(String location, String name) {
        return checkNameAvailabilityWithServiceResponseAsync(location, name).toBlocking().single().body();
    }

    /**
     * Checks whether the specified account name is available or taken.
     *
     * @param location The resource location without whitespace.
     * @param name The Data Lake Analytics name to check availability for.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<NameAvailabilityInformation> checkNameAvailabilityAsync(String location, String name, final ServiceCallback<NameAvailabilityInformation> serviceCallback) {
        return ServiceFuture.fromResponse(checkNameAvailabilityWithServiceResponseAsync(location, name), serviceCallback);
    }

    /**
     * Checks whether the specified account name is available or taken.
     *
     * @param location The resource location without whitespace.
     * @param name The Data Lake Analytics name to check availability for.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the NameAvailabilityInformation object
     */
    public Observable<NameAvailabilityInformation> checkNameAvailabilityAsync(String location, String name) {
        return checkNameAvailabilityWithServiceResponseAsync(location, name).map(new Func1<ServiceResponse<NameAvailabilityInformation>, NameAvailabilityInformation>() {
            @Override
            public NameAvailabilityInformation call(ServiceResponse<NameAvailabilityInformation> response) {
                return response.body();
            }
        });
    }

    /**
     * Checks whether the specified account name is available or taken.
     *
     * @param location The resource location without whitespace.
     * @param name The Data Lake Analytics name to check availability for.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the NameAvailabilityInformation object
     */
    public Observable<ServiceResponse<NameAvailabilityInformation>> checkNameAvailabilityWithServiceResponseAsync(String location, String name) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (location == null) {
            throw new IllegalArgumentException("Parameter location is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        if (name == null) {
            throw new IllegalArgumentException("Parameter name is required and cannot be null.");
        }
        CheckNameAvailabilityParameters parameters = new CheckNameAvailabilityParameters();
        parameters.withName(name);
        return service.checkNameAvailability(this.client.subscriptionId(), location, this.client.apiVersion(), this.client.acceptLanguage(), parameters, this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<NameAvailabilityInformation>>>() {
                @Override
                public Observable<ServiceResponse<NameAvailabilityInformation>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<NameAvailabilityInformation> clientResponse = checkNameAvailabilityDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<NameAvailabilityInformation> checkNameAvailabilityDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<NameAvailabilityInformation, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<NameAvailabilityInformation>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object if successful.
     */
    public PagedList<DataLakeAnalyticsAccountBasic> listNext(final String nextPageLink) {
        ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> response = listNextSinglePageAsync(nextPageLink).toBlocking().single();
        return new PagedList<DataLakeAnalyticsAccountBasic>(response.body()) {
            @Override
            public Page<DataLakeAnalyticsAccountBasic> nextPage(String nextPageLink) {
                return listNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @param serviceFuture the ServiceFuture object tracking the Retrofit calls
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<DataLakeAnalyticsAccountBasic>> listNextAsync(final String nextPageLink, final ServiceFuture<List<DataLakeAnalyticsAccountBasic>> serviceFuture, final ListOperationCallback<DataLakeAnalyticsAccountBasic> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listNextSinglePageAsync(nextPageLink),
            new Func1<String, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(String nextPageLink) {
                    return listNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    public Observable<Page<DataLakeAnalyticsAccountBasic>> listNextAsync(final String nextPageLink) {
        return listNextWithServiceResponseAsync(nextPageLink)
            .map(new Func1<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>, Page<DataLakeAnalyticsAccountBasic>>() {
                @Override
                public Page<DataLakeAnalyticsAccountBasic> call(ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listNextWithServiceResponseAsync(final String nextPageLink) {
        return listNextSinglePageAsync(nextPageLink)
            .concatMap(new Func1<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
    ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listNextSinglePageAsync(final String nextPageLink) {
        if (nextPageLink == null) {
            throw new IllegalArgumentException("Parameter nextPageLink is required and cannot be null.");
        }
        String nextUrl = String.format("%s", nextPageLink);
        return service.listNext(nextUrl, this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> result = listNextDelegate(response);
                        return Observable.just(new ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> listNextDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<DataLakeAnalyticsAccountBasic>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<DataLakeAnalyticsAccountBasic>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object if successful.
     */
    public PagedList<DataLakeAnalyticsAccountBasic> listByResourceGroupNext(final String nextPageLink) {
        ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> response = listByResourceGroupNextSinglePageAsync(nextPageLink).toBlocking().single();
        return new PagedList<DataLakeAnalyticsAccountBasic>(response.body()) {
            @Override
            public Page<DataLakeAnalyticsAccountBasic> nextPage(String nextPageLink) {
                return listByResourceGroupNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @param serviceFuture the ServiceFuture object tracking the Retrofit calls
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<DataLakeAnalyticsAccountBasic>> listByResourceGroupNextAsync(final String nextPageLink, final ServiceFuture<List<DataLakeAnalyticsAccountBasic>> serviceFuture, final ListOperationCallback<DataLakeAnalyticsAccountBasic> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listByResourceGroupNextSinglePageAsync(nextPageLink),
            new Func1<String, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(String nextPageLink) {
                    return listByResourceGroupNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    public Observable<Page<DataLakeAnalyticsAccountBasic>> listByResourceGroupNextAsync(final String nextPageLink) {
        return listByResourceGroupNextWithServiceResponseAsync(nextPageLink)
            .map(new Func1<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>, Page<DataLakeAnalyticsAccountBasic>>() {
                @Override
                public Page<DataLakeAnalyticsAccountBasic> call(ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listByResourceGroupNextWithServiceResponseAsync(final String nextPageLink) {
        return listByResourceGroupNextSinglePageAsync(nextPageLink)
            .concatMap(new Func1<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(ServiceResponse<Page<DataLakeAnalyticsAccountBasic>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listByResourceGroupNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
    ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listByResourceGroupNextSinglePageAsync(final String nextPageLink) {
        if (nextPageLink == null) {
            throw new IllegalArgumentException("Parameter nextPageLink is required and cannot be null.");
        }
        String nextUrl = String.format("%s", nextPageLink);
        return service.listByResourceGroupNext(nextUrl, this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>>>() {
                @Override
                public Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> result = listByResourceGroupNextDelegate(response);
                        return Observable.just(new ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<DataLakeAnalyticsAccountBasic>> listByResourceGroupNextDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<DataLakeAnalyticsAccountBasic>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<DataLakeAnalyticsAccountBasic>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

}
