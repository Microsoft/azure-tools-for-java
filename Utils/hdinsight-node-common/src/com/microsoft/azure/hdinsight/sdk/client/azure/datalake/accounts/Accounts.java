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

package com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts;

import com.microsoft.azure.CloudException;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.CreateDataLakeAnalyticsAccountParameters;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.DataLakeAnalyticsAccount;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.DataLakeAnalyticsAccountBasic;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.NameAvailabilityInformation;
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.UpdateDataLakeAnalyticsAccountParameters;
import com.microsoft.azure.ListOperationCallback;
import com.microsoft.azure.Page;
import com.microsoft.azure.PagedList;
import com.microsoft.rest.ServiceCallback;
import com.microsoft.rest.ServiceFuture;
import com.microsoft.rest.ServiceResponse;
import java.io.IOException;
import java.util.List;
import rx.Observable;

/**
 * An instance of this class provides access to all the operations defined
 * in Accounts.
 */
public interface Accounts {
    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object if successful.
     */
    PagedList<DataLakeAnalyticsAccountBasic> list();

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<List<DataLakeAnalyticsAccountBasic>> listAsync(final ListOperationCallback<DataLakeAnalyticsAccountBasic> serviceCallback);

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    Observable<Page<DataLakeAnalyticsAccountBasic>> listAsync();

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listWithServiceResponseAsync();
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
    PagedList<DataLakeAnalyticsAccountBasic> list(final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count);

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
    ServiceFuture<List<DataLakeAnalyticsAccountBasic>> listAsync(final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count, final ListOperationCallback<DataLakeAnalyticsAccountBasic> serviceCallback);

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
    Observable<Page<DataLakeAnalyticsAccountBasic>> listAsync(final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count);

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
    Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listWithServiceResponseAsync(final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count);

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object if successful.
     */
    PagedList<DataLakeAnalyticsAccountBasic> listByResourceGroup(final String resourceGroupName);

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<List<DataLakeAnalyticsAccountBasic>> listByResourceGroupAsync(final String resourceGroupName, final ListOperationCallback<DataLakeAnalyticsAccountBasic> serviceCallback);

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    Observable<Page<DataLakeAnalyticsAccountBasic>> listByResourceGroupAsync(final String resourceGroupName);

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listByResourceGroupWithServiceResponseAsync(final String resourceGroupName);
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
    PagedList<DataLakeAnalyticsAccountBasic> listByResourceGroup(final String resourceGroupName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count);

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
    ServiceFuture<List<DataLakeAnalyticsAccountBasic>> listByResourceGroupAsync(final String resourceGroupName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count, final ListOperationCallback<DataLakeAnalyticsAccountBasic> serviceCallback);

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
    Observable<Page<DataLakeAnalyticsAccountBasic>> listByResourceGroupAsync(final String resourceGroupName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count);

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
    Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listByResourceGroupWithServiceResponseAsync(final String resourceGroupName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count);

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
    DataLakeAnalyticsAccount create(String resourceGroupName, String accountName, CreateDataLakeAnalyticsAccountParameters parameters);

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
    ServiceFuture<DataLakeAnalyticsAccount> createAsync(String resourceGroupName, String accountName, CreateDataLakeAnalyticsAccountParameters parameters, final ServiceCallback<DataLakeAnalyticsAccount> serviceCallback);

    /**
     * Creates the specified Data Lake Analytics account. This supplies the user with computation services for Data Lake Analytics workloads.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to create a new Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    Observable<DataLakeAnalyticsAccount> createAsync(String resourceGroupName, String accountName, CreateDataLakeAnalyticsAccountParameters parameters);

    /**
     * Creates the specified Data Lake Analytics account. This supplies the user with computation services for Data Lake Analytics workloads.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to create a new Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    Observable<ServiceResponse<DataLakeAnalyticsAccount>> createWithServiceResponseAsync(String resourceGroupName, String accountName, CreateDataLakeAnalyticsAccountParameters parameters);

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
    DataLakeAnalyticsAccount beginCreate(String resourceGroupName, String accountName, CreateDataLakeAnalyticsAccountParameters parameters);

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
    ServiceFuture<DataLakeAnalyticsAccount> beginCreateAsync(String resourceGroupName, String accountName, CreateDataLakeAnalyticsAccountParameters parameters, final ServiceCallback<DataLakeAnalyticsAccount> serviceCallback);

    /**
     * Creates the specified Data Lake Analytics account. This supplies the user with computation services for Data Lake Analytics workloads.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to create a new Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    Observable<DataLakeAnalyticsAccount> beginCreateAsync(String resourceGroupName, String accountName, CreateDataLakeAnalyticsAccountParameters parameters);

    /**
     * Creates the specified Data Lake Analytics account. This supplies the user with computation services for Data Lake Analytics workloads.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to create a new Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    Observable<ServiceResponse<DataLakeAnalyticsAccount>> beginCreateWithServiceResponseAsync(String resourceGroupName, String accountName, CreateDataLakeAnalyticsAccountParameters parameters);

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
    DataLakeAnalyticsAccount get(String resourceGroupName, String accountName);

    /**
     * Gets details of the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<DataLakeAnalyticsAccount> getAsync(String resourceGroupName, String accountName, final ServiceCallback<DataLakeAnalyticsAccount> serviceCallback);

    /**
     * Gets details of the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    Observable<DataLakeAnalyticsAccount> getAsync(String resourceGroupName, String accountName);

    /**
     * Gets details of the specified Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    Observable<ServiceResponse<DataLakeAnalyticsAccount>> getWithServiceResponseAsync(String resourceGroupName, String accountName);

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
    DataLakeAnalyticsAccount update(String resourceGroupName, String accountName);

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<DataLakeAnalyticsAccount> updateAsync(String resourceGroupName, String accountName, final ServiceCallback<DataLakeAnalyticsAccount> serviceCallback);

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    Observable<DataLakeAnalyticsAccount> updateAsync(String resourceGroupName, String accountName);

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    Observable<ServiceResponse<DataLakeAnalyticsAccount>> updateWithServiceResponseAsync(String resourceGroupName, String accountName);
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
    DataLakeAnalyticsAccount update(String resourceGroupName, String accountName, UpdateDataLakeAnalyticsAccountParameters parameters);

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
    ServiceFuture<DataLakeAnalyticsAccount> updateAsync(String resourceGroupName, String accountName, UpdateDataLakeAnalyticsAccountParameters parameters, final ServiceCallback<DataLakeAnalyticsAccount> serviceCallback);

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to the update Data Lake Analytics account operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    Observable<DataLakeAnalyticsAccount> updateAsync(String resourceGroupName, String accountName, UpdateDataLakeAnalyticsAccountParameters parameters);

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to the update Data Lake Analytics account operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    Observable<ServiceResponse<DataLakeAnalyticsAccount>> updateWithServiceResponseAsync(String resourceGroupName, String accountName, UpdateDataLakeAnalyticsAccountParameters parameters);

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
    DataLakeAnalyticsAccount beginUpdate(String resourceGroupName, String accountName);

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<DataLakeAnalyticsAccount> beginUpdateAsync(String resourceGroupName, String accountName, final ServiceCallback<DataLakeAnalyticsAccount> serviceCallback);

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    Observable<DataLakeAnalyticsAccount> beginUpdateAsync(String resourceGroupName, String accountName);

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    Observable<ServiceResponse<DataLakeAnalyticsAccount>> beginUpdateWithServiceResponseAsync(String resourceGroupName, String accountName);
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
    DataLakeAnalyticsAccount beginUpdate(String resourceGroupName, String accountName, UpdateDataLakeAnalyticsAccountParameters parameters);

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
    ServiceFuture<DataLakeAnalyticsAccount> beginUpdateAsync(String resourceGroupName, String accountName, UpdateDataLakeAnalyticsAccountParameters parameters, final ServiceCallback<DataLakeAnalyticsAccount> serviceCallback);

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to the update Data Lake Analytics account operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    Observable<DataLakeAnalyticsAccount> beginUpdateAsync(String resourceGroupName, String accountName, UpdateDataLakeAnalyticsAccountParameters parameters);

    /**
     * Updates the Data Lake Analytics account object specified by the accountName with the contents of the account object.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param parameters Parameters supplied to the update Data Lake Analytics account operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the DataLakeAnalyticsAccount object
     */
    Observable<ServiceResponse<DataLakeAnalyticsAccount>> beginUpdateWithServiceResponseAsync(String resourceGroupName, String accountName, UpdateDataLakeAnalyticsAccountParameters parameters);

    /**
     * Begins the delete process for the Data Lake Analytics account object specified by the account name.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     */
    void delete(String resourceGroupName, String accountName);

    /**
     * Begins the delete process for the Data Lake Analytics account object specified by the account name.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<Void> deleteAsync(String resourceGroupName, String accountName, final ServiceCallback<Void> serviceCallback);

    /**
     * Begins the delete process for the Data Lake Analytics account object specified by the account name.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    Observable<Void> deleteAsync(String resourceGroupName, String accountName);

    /**
     * Begins the delete process for the Data Lake Analytics account object specified by the account name.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    Observable<ServiceResponse<Void>> deleteWithServiceResponseAsync(String resourceGroupName, String accountName);

    /**
     * Begins the delete process for the Data Lake Analytics account object specified by the account name.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     */
    void beginDelete(String resourceGroupName, String accountName);

    /**
     * Begins the delete process for the Data Lake Analytics account object specified by the account name.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<Void> beginDeleteAsync(String resourceGroupName, String accountName, final ServiceCallback<Void> serviceCallback);

    /**
     * Begins the delete process for the Data Lake Analytics account object specified by the account name.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    Observable<Void> beginDeleteAsync(String resourceGroupName, String accountName);

    /**
     * Begins the delete process for the Data Lake Analytics account object specified by the account name.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    Observable<ServiceResponse<Void>> beginDeleteWithServiceResponseAsync(String resourceGroupName, String accountName);

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
    NameAvailabilityInformation checkNameAvailability(String location, String name);

    /**
     * Checks whether the specified account name is available or taken.
     *
     * @param location The resource location without whitespace.
     * @param name The Data Lake Analytics name to check availability for.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<NameAvailabilityInformation> checkNameAvailabilityAsync(String location, String name, final ServiceCallback<NameAvailabilityInformation> serviceCallback);

    /**
     * Checks whether the specified account name is available or taken.
     *
     * @param location The resource location without whitespace.
     * @param name The Data Lake Analytics name to check availability for.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the NameAvailabilityInformation object
     */
    Observable<NameAvailabilityInformation> checkNameAvailabilityAsync(String location, String name);

    /**
     * Checks whether the specified account name is available or taken.
     *
     * @param location The resource location without whitespace.
     * @param name The Data Lake Analytics name to check availability for.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the NameAvailabilityInformation object
     */
    Observable<ServiceResponse<NameAvailabilityInformation>> checkNameAvailabilityWithServiceResponseAsync(String location, String name);

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object if successful.
     */
    PagedList<DataLakeAnalyticsAccountBasic> listNext(final String nextPageLink);

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @param serviceFuture the ServiceFuture object tracking the Retrofit calls
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<List<DataLakeAnalyticsAccountBasic>> listNextAsync(final String nextPageLink, final ServiceFuture<List<DataLakeAnalyticsAccountBasic>> serviceFuture, final ListOperationCallback<DataLakeAnalyticsAccountBasic> serviceCallback);

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    Observable<Page<DataLakeAnalyticsAccountBasic>> listNextAsync(final String nextPageLink);

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within the current subscription. This includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listNextWithServiceResponseAsync(final String nextPageLink);

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object if successful.
     */
    PagedList<DataLakeAnalyticsAccountBasic> listByResourceGroupNext(final String nextPageLink);

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @param serviceFuture the ServiceFuture object tracking the Retrofit calls
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<List<DataLakeAnalyticsAccountBasic>> listByResourceGroupNextAsync(final String nextPageLink, final ServiceFuture<List<DataLakeAnalyticsAccountBasic>> serviceFuture, final ListOperationCallback<DataLakeAnalyticsAccountBasic> serviceCallback);

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    Observable<Page<DataLakeAnalyticsAccountBasic>> listByResourceGroupNextAsync(final String nextPageLink);

    /**
     * Gets the first page of Data Lake Analytics accounts, if any, within a specific resource group. This includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;DataLakeAnalyticsAccountBasic&gt; object
     */
    Observable<ServiceResponse<Page<DataLakeAnalyticsAccountBasic>>> listByResourceGroupNextWithServiceResponseAsync(final String nextPageLink);

}
