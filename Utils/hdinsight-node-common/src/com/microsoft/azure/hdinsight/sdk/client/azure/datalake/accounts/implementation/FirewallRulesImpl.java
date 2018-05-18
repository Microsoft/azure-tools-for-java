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
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.FirewallRules;
import com.google.common.reflect.TypeToken;
import com.microsoft.azure.AzureServiceFuture;
import com.microsoft.azure.CloudException;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.CreateOrUpdateFirewallRuleParameters;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.FirewallRule;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.PageImpl;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.UpdateFirewallRuleParameters;
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
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;
import retrofit2.Response;
import rx.functions.Func1;
import rx.Observable;

/**
 * An instance of this class provides access to all the operations defined
 * in FirewallRules.
 */
public class FirewallRulesImpl implements FirewallRules {
    /** The Retrofit service to perform REST calls. */
    private FirewallRulesService service;
    /** The service client containing this operation class. */
    private DataLakeAnalyticsAccountManagementClientImpl client;

    /**
     * Initializes an instance of FirewallRulesImpl.
     *
     * @param retrofit the Retrofit instance built from a Retrofit Builder.
     * @param client the instance of the service client containing this operation class.
     */
    public FirewallRulesImpl(Retrofit retrofit, DataLakeAnalyticsAccountManagementClientImpl client) {
        this.service = retrofit.create(FirewallRulesService.class);
        this.client = client;
    }

    /**
     * The interface defining all the services for FirewallRules to be
     * used by Retrofit to perform actually REST calls.
     */
    interface FirewallRulesService {
        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.FirewallRules listByAccount" })
        @GET("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/firewallRules")
        Observable<Response<ResponseBody>> listByAccount(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.FirewallRules createOrUpdate" })
        @PUT("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/firewallRules/{firewallRuleName}")
        Observable<Response<ResponseBody>> createOrUpdate(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Path("firewallRuleName") String firewallRuleName, @Body CreateOrUpdateFirewallRuleParameters parameters, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.FirewallRules get" })
        @GET("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/firewallRules/{firewallRuleName}")
        Observable<Response<ResponseBody>> get(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Path("firewallRuleName") String firewallRuleName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.FirewallRules update" })
        @PATCH("subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/firewallRules/{firewallRuleName}")
        Observable<Response<ResponseBody>> update(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Path("firewallRuleName") String firewallRuleName, @Body UpdateFirewallRuleParameters parameters, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.FirewallRules delete" })
        @HTTP(path = "subscriptions/{subscriptionId}/resourceGroups/{resourceGroupName}/providers/Microsoft.DataLakeAnalytics/accounts/{accountName}/firewallRules/{firewallRuleName}", method = "DELETE", hasBody = true)
        Observable<Response<ResponseBody>> delete(@Path("subscriptionId") String subscriptionId, @Path("resourceGroupName") String resourceGroupName, @Path("accountName") String accountName, @Path("firewallRuleName") String firewallRuleName, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.FirewallRules listByAccountNext" })
        @GET
        Observable<Response<ResponseBody>> listByAccountNext(@Url String nextUrl, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

    }

    /**
     * Lists the Data Lake Analytics firewall rules within the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;FirewallRule&gt; object if successful.
     */
    public PagedList<FirewallRule> listByAccount(final String resourceGroupName, final String accountName) {
        ServiceResponse<Page<FirewallRule>> response = listByAccountSinglePageAsync(resourceGroupName, accountName).toBlocking().single();
        return new PagedList<FirewallRule>(response.body()) {
            @Override
            public Page<FirewallRule> nextPage(String nextPageLink) {
                return listByAccountNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Lists the Data Lake Analytics firewall rules within the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<FirewallRule>> listByAccountAsync(final String resourceGroupName, final String accountName, final ListOperationCallback<FirewallRule> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listByAccountSinglePageAsync(resourceGroupName, accountName),
            new Func1<String, Observable<ServiceResponse<Page<FirewallRule>>>>() {
                @Override
                public Observable<ServiceResponse<Page<FirewallRule>>> call(String nextPageLink) {
                    return listByAccountNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Lists the Data Lake Analytics firewall rules within the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;FirewallRule&gt; object
     */
    public Observable<Page<FirewallRule>> listByAccountAsync(final String resourceGroupName, final String accountName) {
        return listByAccountWithServiceResponseAsync(resourceGroupName, accountName)
            .map(new Func1<ServiceResponse<Page<FirewallRule>>, Page<FirewallRule>>() {
                @Override
                public Page<FirewallRule> call(ServiceResponse<Page<FirewallRule>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Lists the Data Lake Analytics firewall rules within the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;FirewallRule&gt; object
     */
    public Observable<ServiceResponse<Page<FirewallRule>>> listByAccountWithServiceResponseAsync(final String resourceGroupName, final String accountName) {
        return listByAccountSinglePageAsync(resourceGroupName, accountName)
            .concatMap(new Func1<ServiceResponse<Page<FirewallRule>>, Observable<ServiceResponse<Page<FirewallRule>>>>() {
                @Override
                public Observable<ServiceResponse<Page<FirewallRule>>> call(ServiceResponse<Page<FirewallRule>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listByAccountNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Lists the Data Lake Analytics firewall rules within the specified Data Lake Analytics account.
     *
    ServiceResponse<PageImpl<FirewallRule>> * @param resourceGroupName The name of the Azure resource group.
    ServiceResponse<PageImpl<FirewallRule>> * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;FirewallRule&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<FirewallRule>>> listByAccountSinglePageAsync(final String resourceGroupName, final String accountName) {
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
        return service.listByAccount(this.client.subscriptionId(), resourceGroupName, accountName, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<FirewallRule>>>>() {
                @Override
                public Observable<ServiceResponse<Page<FirewallRule>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<FirewallRule>> result = listByAccountDelegate(response);
                        return Observable.just(new ServiceResponse<Page<FirewallRule>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<FirewallRule>> listByAccountDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<FirewallRule>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<FirewallRule>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Creates or updates the specified firewall rule. During update, the firewall rule with the specified name will be replaced with this new firewall rule.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to create or update.
     * @param parameters Parameters supplied to create or update the firewall rule.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the FirewallRule object if successful.
     */
    public FirewallRule createOrUpdate(String resourceGroupName, String accountName, String firewallRuleName, CreateOrUpdateFirewallRuleParameters parameters) {
        return createOrUpdateWithServiceResponseAsync(resourceGroupName, accountName, firewallRuleName, parameters).toBlocking().single().body();
    }

    /**
     * Creates or updates the specified firewall rule. During update, the firewall rule with the specified name will be replaced with this new firewall rule.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to create or update.
     * @param parameters Parameters supplied to create or update the firewall rule.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<FirewallRule> createOrUpdateAsync(String resourceGroupName, String accountName, String firewallRuleName, CreateOrUpdateFirewallRuleParameters parameters, final ServiceCallback<FirewallRule> serviceCallback) {
        return ServiceFuture.fromResponse(createOrUpdateWithServiceResponseAsync(resourceGroupName, accountName, firewallRuleName, parameters), serviceCallback);
    }

    /**
     * Creates or updates the specified firewall rule. During update, the firewall rule with the specified name will be replaced with this new firewall rule.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to create or update.
     * @param parameters Parameters supplied to create or update the firewall rule.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the FirewallRule object
     */
    public Observable<FirewallRule> createOrUpdateAsync(String resourceGroupName, String accountName, String firewallRuleName, CreateOrUpdateFirewallRuleParameters parameters) {
        return createOrUpdateWithServiceResponseAsync(resourceGroupName, accountName, firewallRuleName, parameters).map(new Func1<ServiceResponse<FirewallRule>, FirewallRule>() {
            @Override
            public FirewallRule call(ServiceResponse<FirewallRule> response) {
                return response.body();
            }
        });
    }

    /**
     * Creates or updates the specified firewall rule. During update, the firewall rule with the specified name will be replaced with this new firewall rule.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to create or update.
     * @param parameters Parameters supplied to create or update the firewall rule.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the FirewallRule object
     */
    public Observable<ServiceResponse<FirewallRule>> createOrUpdateWithServiceResponseAsync(String resourceGroupName, String accountName, String firewallRuleName, CreateOrUpdateFirewallRuleParameters parameters) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (firewallRuleName == null) {
            throw new IllegalArgumentException("Parameter firewallRuleName is required and cannot be null.");
        }
        if (parameters == null) {
            throw new IllegalArgumentException("Parameter parameters is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        Validator.validate(parameters);
        return service.createOrUpdate(this.client.subscriptionId(), resourceGroupName, accountName, firewallRuleName, parameters, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<FirewallRule>>>() {
                @Override
                public Observable<ServiceResponse<FirewallRule>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<FirewallRule> clientResponse = createOrUpdateDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<FirewallRule> createOrUpdateDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<FirewallRule, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<FirewallRule>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Gets the specified Data Lake Analytics firewall rule.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to retrieve.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the FirewallRule object if successful.
     */
    public FirewallRule get(String resourceGroupName, String accountName, String firewallRuleName) {
        return getWithServiceResponseAsync(resourceGroupName, accountName, firewallRuleName).toBlocking().single().body();
    }

    /**
     * Gets the specified Data Lake Analytics firewall rule.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to retrieve.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<FirewallRule> getAsync(String resourceGroupName, String accountName, String firewallRuleName, final ServiceCallback<FirewallRule> serviceCallback) {
        return ServiceFuture.fromResponse(getWithServiceResponseAsync(resourceGroupName, accountName, firewallRuleName), serviceCallback);
    }

    /**
     * Gets the specified Data Lake Analytics firewall rule.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to retrieve.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the FirewallRule object
     */
    public Observable<FirewallRule> getAsync(String resourceGroupName, String accountName, String firewallRuleName) {
        return getWithServiceResponseAsync(resourceGroupName, accountName, firewallRuleName).map(new Func1<ServiceResponse<FirewallRule>, FirewallRule>() {
            @Override
            public FirewallRule call(ServiceResponse<FirewallRule> response) {
                return response.body();
            }
        });
    }

    /**
     * Gets the specified Data Lake Analytics firewall rule.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to retrieve.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the FirewallRule object
     */
    public Observable<ServiceResponse<FirewallRule>> getWithServiceResponseAsync(String resourceGroupName, String accountName, String firewallRuleName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (firewallRuleName == null) {
            throw new IllegalArgumentException("Parameter firewallRuleName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        return service.get(this.client.subscriptionId(), resourceGroupName, accountName, firewallRuleName, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<FirewallRule>>>() {
                @Override
                public Observable<ServiceResponse<FirewallRule>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<FirewallRule> clientResponse = getDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<FirewallRule> getDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<FirewallRule, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<FirewallRule>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Updates the specified firewall rule.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to update.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the FirewallRule object if successful.
     */
    public FirewallRule update(String resourceGroupName, String accountName, String firewallRuleName) {
        return updateWithServiceResponseAsync(resourceGroupName, accountName, firewallRuleName).toBlocking().single().body();
    }

    /**
     * Updates the specified firewall rule.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to update.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<FirewallRule> updateAsync(String resourceGroupName, String accountName, String firewallRuleName, final ServiceCallback<FirewallRule> serviceCallback) {
        return ServiceFuture.fromResponse(updateWithServiceResponseAsync(resourceGroupName, accountName, firewallRuleName), serviceCallback);
    }

    /**
     * Updates the specified firewall rule.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to update.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the FirewallRule object
     */
    public Observable<FirewallRule> updateAsync(String resourceGroupName, String accountName, String firewallRuleName) {
        return updateWithServiceResponseAsync(resourceGroupName, accountName, firewallRuleName).map(new Func1<ServiceResponse<FirewallRule>, FirewallRule>() {
            @Override
            public FirewallRule call(ServiceResponse<FirewallRule> response) {
                return response.body();
            }
        });
    }

    /**
     * Updates the specified firewall rule.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to update.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the FirewallRule object
     */
    public Observable<ServiceResponse<FirewallRule>> updateWithServiceResponseAsync(String resourceGroupName, String accountName, String firewallRuleName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (firewallRuleName == null) {
            throw new IllegalArgumentException("Parameter firewallRuleName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        final UpdateFirewallRuleParameters parameters = null;
        return service.update(this.client.subscriptionId(), resourceGroupName, accountName, firewallRuleName, parameters, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<FirewallRule>>>() {
                @Override
                public Observable<ServiceResponse<FirewallRule>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<FirewallRule> clientResponse = updateDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    /**
     * Updates the specified firewall rule.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to update.
     * @param parameters Parameters supplied to update the firewall rule.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the FirewallRule object if successful.
     */
    public FirewallRule update(String resourceGroupName, String accountName, String firewallRuleName, UpdateFirewallRuleParameters parameters) {
        return updateWithServiceResponseAsync(resourceGroupName, accountName, firewallRuleName, parameters).toBlocking().single().body();
    }

    /**
     * Updates the specified firewall rule.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to update.
     * @param parameters Parameters supplied to update the firewall rule.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<FirewallRule> updateAsync(String resourceGroupName, String accountName, String firewallRuleName, UpdateFirewallRuleParameters parameters, final ServiceCallback<FirewallRule> serviceCallback) {
        return ServiceFuture.fromResponse(updateWithServiceResponseAsync(resourceGroupName, accountName, firewallRuleName, parameters), serviceCallback);
    }

    /**
     * Updates the specified firewall rule.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to update.
     * @param parameters Parameters supplied to update the firewall rule.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the FirewallRule object
     */
    public Observable<FirewallRule> updateAsync(String resourceGroupName, String accountName, String firewallRuleName, UpdateFirewallRuleParameters parameters) {
        return updateWithServiceResponseAsync(resourceGroupName, accountName, firewallRuleName, parameters).map(new Func1<ServiceResponse<FirewallRule>, FirewallRule>() {
            @Override
            public FirewallRule call(ServiceResponse<FirewallRule> response) {
                return response.body();
            }
        });
    }

    /**
     * Updates the specified firewall rule.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to update.
     * @param parameters Parameters supplied to update the firewall rule.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the FirewallRule object
     */
    public Observable<ServiceResponse<FirewallRule>> updateWithServiceResponseAsync(String resourceGroupName, String accountName, String firewallRuleName, UpdateFirewallRuleParameters parameters) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (firewallRuleName == null) {
            throw new IllegalArgumentException("Parameter firewallRuleName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        Validator.validate(parameters);
        return service.update(this.client.subscriptionId(), resourceGroupName, accountName, firewallRuleName, parameters, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<FirewallRule>>>() {
                @Override
                public Observable<ServiceResponse<FirewallRule>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<FirewallRule> clientResponse = updateDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<FirewallRule> updateDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<FirewallRule, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<FirewallRule>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Deletes the specified firewall rule from the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to delete.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     */
    public void delete(String resourceGroupName, String accountName, String firewallRuleName) {
        deleteWithServiceResponseAsync(resourceGroupName, accountName, firewallRuleName).toBlocking().single().body();
    }

    /**
     * Deletes the specified firewall rule from the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to delete.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<Void> deleteAsync(String resourceGroupName, String accountName, String firewallRuleName, final ServiceCallback<Void> serviceCallback) {
        return ServiceFuture.fromResponse(deleteWithServiceResponseAsync(resourceGroupName, accountName, firewallRuleName), serviceCallback);
    }

    /**
     * Deletes the specified firewall rule from the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to delete.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<Void> deleteAsync(String resourceGroupName, String accountName, String firewallRuleName) {
        return deleteWithServiceResponseAsync(resourceGroupName, accountName, firewallRuleName).map(new Func1<ServiceResponse<Void>, Void>() {
            @Override
            public Void call(ServiceResponse<Void> response) {
                return response.body();
            }
        });
    }

    /**
     * Deletes the specified firewall rule from the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param firewallRuleName The name of the firewall rule to delete.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    public Observable<ServiceResponse<Void>> deleteWithServiceResponseAsync(String resourceGroupName, String accountName, String firewallRuleName) {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (resourceGroupName == null) {
            throw new IllegalArgumentException("Parameter resourceGroupName is required and cannot be null.");
        }
        if (accountName == null) {
            throw new IllegalArgumentException("Parameter accountName is required and cannot be null.");
        }
        if (firewallRuleName == null) {
            throw new IllegalArgumentException("Parameter firewallRuleName is required and cannot be null.");
        }
        if (this.client.apiVersion() == null) {
            throw new IllegalArgumentException("Parameter this.client.apiVersion() is required and cannot be null.");
        }
        return service.delete(this.client.subscriptionId(), resourceGroupName, accountName, firewallRuleName, this.client.apiVersion(), this.client.acceptLanguage(), this.client.userAgent())
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
                .register(204, new TypeToken<Void>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Lists the Data Lake Analytics firewall rules within the specified Data Lake Analytics account.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;FirewallRule&gt; object if successful.
     */
    public PagedList<FirewallRule> listByAccountNext(final String nextPageLink) {
        ServiceResponse<Page<FirewallRule>> response = listByAccountNextSinglePageAsync(nextPageLink).toBlocking().single();
        return new PagedList<FirewallRule>(response.body()) {
            @Override
            public Page<FirewallRule> nextPage(String nextPageLink) {
                return listByAccountNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Lists the Data Lake Analytics firewall rules within the specified Data Lake Analytics account.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @param serviceFuture the ServiceFuture object tracking the Retrofit calls
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<FirewallRule>> listByAccountNextAsync(final String nextPageLink, final ServiceFuture<List<FirewallRule>> serviceFuture, final ListOperationCallback<FirewallRule> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listByAccountNextSinglePageAsync(nextPageLink),
            new Func1<String, Observable<ServiceResponse<Page<FirewallRule>>>>() {
                @Override
                public Observable<ServiceResponse<Page<FirewallRule>>> call(String nextPageLink) {
                    return listByAccountNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Lists the Data Lake Analytics firewall rules within the specified Data Lake Analytics account.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;FirewallRule&gt; object
     */
    public Observable<Page<FirewallRule>> listByAccountNextAsync(final String nextPageLink) {
        return listByAccountNextWithServiceResponseAsync(nextPageLink)
            .map(new Func1<ServiceResponse<Page<FirewallRule>>, Page<FirewallRule>>() {
                @Override
                public Page<FirewallRule> call(ServiceResponse<Page<FirewallRule>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Lists the Data Lake Analytics firewall rules within the specified Data Lake Analytics account.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;FirewallRule&gt; object
     */
    public Observable<ServiceResponse<Page<FirewallRule>>> listByAccountNextWithServiceResponseAsync(final String nextPageLink) {
        return listByAccountNextSinglePageAsync(nextPageLink)
            .concatMap(new Func1<ServiceResponse<Page<FirewallRule>>, Observable<ServiceResponse<Page<FirewallRule>>>>() {
                @Override
                public Observable<ServiceResponse<Page<FirewallRule>>> call(ServiceResponse<Page<FirewallRule>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listByAccountNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Lists the Data Lake Analytics firewall rules within the specified Data Lake Analytics account.
     *
    ServiceResponse<PageImpl<FirewallRule>> * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;FirewallRule&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<FirewallRule>>> listByAccountNextSinglePageAsync(final String nextPageLink) {
        if (nextPageLink == null) {
            throw new IllegalArgumentException("Parameter nextPageLink is required and cannot be null.");
        }
        String nextUrl = String.format("%s", nextPageLink);
        return service.listByAccountNext(nextUrl, this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<FirewallRule>>>>() {
                @Override
                public Observable<ServiceResponse<Page<FirewallRule>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<FirewallRule>> result = listByAccountNextDelegate(response);
                        return Observable.just(new ServiceResponse<Page<FirewallRule>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<FirewallRule>> listByAccountNextDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<FirewallRule>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<FirewallRule>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

}
