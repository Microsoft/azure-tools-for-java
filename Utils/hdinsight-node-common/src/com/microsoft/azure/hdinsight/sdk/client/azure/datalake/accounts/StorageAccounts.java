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
import com.microsoft.azure.hdinsight.sdk.client.azure.datalake.accounts.models.AddStorageAccountParameters;
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
import java.io.IOException;
import java.util.List;
import rx.Observable;

/**
 * An instance of this class provides access to all the operations defined
 * in StorageAccounts.
 */
public interface StorageAccounts {
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
    PagedList<StorageAccountInformation> listByAccount(final String resourceGroupName, final String accountName);

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<List<StorageAccountInformation>> listByAccountAsync(final String resourceGroupName, final String accountName, final ListOperationCallback<StorageAccountInformation> serviceCallback);

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageAccountInformation&gt; object
     */
    Observable<Page<StorageAccountInformation>> listByAccountAsync(final String resourceGroupName, final String accountName);

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageAccountInformation&gt; object
     */
    Observable<ServiceResponse<Page<StorageAccountInformation>>> listByAccountWithServiceResponseAsync(final String resourceGroupName, final String accountName);
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
    PagedList<StorageAccountInformation> listByAccount(final String resourceGroupName, final String accountName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count);

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
    ServiceFuture<List<StorageAccountInformation>> listByAccountAsync(final String resourceGroupName, final String accountName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count, final ListOperationCallback<StorageAccountInformation> serviceCallback);

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
    Observable<Page<StorageAccountInformation>> listByAccountAsync(final String resourceGroupName, final String accountName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count);

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
    Observable<ServiceResponse<Page<StorageAccountInformation>>> listByAccountWithServiceResponseAsync(final String resourceGroupName, final String accountName, final String filter, final Integer top, final Integer skip, final String select, final String orderby, final Boolean count);

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
    void add(String resourceGroupName, String accountName, String storageAccountName, AddStorageAccountParameters parameters);

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
    ServiceFuture<Void> addAsync(String resourceGroupName, String accountName, String storageAccountName, AddStorageAccountParameters parameters, final ServiceCallback<Void> serviceCallback);

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
    Observable<Void> addAsync(String resourceGroupName, String accountName, String storageAccountName, AddStorageAccountParameters parameters);

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
    Observable<ServiceResponse<Void>> addWithServiceResponseAsync(String resourceGroupName, String accountName, String storageAccountName, AddStorageAccountParameters parameters);

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
    StorageAccountInformation get(String resourceGroupName, String accountName, String storageAccountName);

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
    ServiceFuture<StorageAccountInformation> getAsync(String resourceGroupName, String accountName, String storageAccountName, final ServiceCallback<StorageAccountInformation> serviceCallback);

    /**
     * Gets the specified Azure Storage account linked to the given Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure Storage account for which to retrieve the details.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the StorageAccountInformation object
     */
    Observable<StorageAccountInformation> getAsync(String resourceGroupName, String accountName, String storageAccountName);

    /**
     * Gets the specified Azure Storage account linked to the given Data Lake Analytics account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure Storage account for which to retrieve the details.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the StorageAccountInformation object
     */
    Observable<ServiceResponse<StorageAccountInformation>> getWithServiceResponseAsync(String resourceGroupName, String accountName, String storageAccountName);

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
    void update(String resourceGroupName, String accountName, String storageAccountName);

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
    ServiceFuture<Void> updateAsync(String resourceGroupName, String accountName, String storageAccountName, final ServiceCallback<Void> serviceCallback);

    /**
     * Updates the Data Lake Analytics account to replace Azure Storage blob account details, such as the access key and/or suffix.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The Azure Storage account to modify
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    Observable<Void> updateAsync(String resourceGroupName, String accountName, String storageAccountName);

    /**
     * Updates the Data Lake Analytics account to replace Azure Storage blob account details, such as the access key and/or suffix.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The Azure Storage account to modify
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    Observable<ServiceResponse<Void>> updateWithServiceResponseAsync(String resourceGroupName, String accountName, String storageAccountName);
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
    void update(String resourceGroupName, String accountName, String storageAccountName, UpdateStorageAccountParameters parameters);

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
    ServiceFuture<Void> updateAsync(String resourceGroupName, String accountName, String storageAccountName, UpdateStorageAccountParameters parameters, final ServiceCallback<Void> serviceCallback);

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
    Observable<Void> updateAsync(String resourceGroupName, String accountName, String storageAccountName, UpdateStorageAccountParameters parameters);

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
    Observable<ServiceResponse<Void>> updateWithServiceResponseAsync(String resourceGroupName, String accountName, String storageAccountName, UpdateStorageAccountParameters parameters);

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
    void delete(String resourceGroupName, String accountName, String storageAccountName);

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
    ServiceFuture<Void> deleteAsync(String resourceGroupName, String accountName, String storageAccountName, final ServiceCallback<Void> serviceCallback);

    /**
     * Updates the specified Data Lake Analytics account to remove an Azure Storage account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure Storage account to remove
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    Observable<Void> deleteAsync(String resourceGroupName, String accountName, String storageAccountName);

    /**
     * Updates the specified Data Lake Analytics account to remove an Azure Storage account.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure Storage account to remove
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceResponse} object if successful.
     */
    Observable<ServiceResponse<Void>> deleteWithServiceResponseAsync(String resourceGroupName, String accountName, String storageAccountName);

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
    PagedList<StorageContainer> listStorageContainers(final String resourceGroupName, final String accountName, final String storageAccountName);

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
    ServiceFuture<List<StorageContainer>> listStorageContainersAsync(final String resourceGroupName, final String accountName, final String storageAccountName, final ListOperationCallback<StorageContainer> serviceCallback);

    /**
     * Lists the Azure Storage containers, if any, associated with the specified Data Lake Analytics and Azure Storage account combination. The response includes a link to the next page of results, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure storage account from which to list blob containers.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageContainer&gt; object
     */
    Observable<Page<StorageContainer>> listStorageContainersAsync(final String resourceGroupName, final String accountName, final String storageAccountName);

    /**
     * Lists the Azure Storage containers, if any, associated with the specified Data Lake Analytics and Azure Storage account combination. The response includes a link to the next page of results, if any.
     *
     * @param resourceGroupName The name of the Azure resource group.
     * @param accountName The name of the Data Lake Analytics account.
     * @param storageAccountName The name of the Azure storage account from which to list blob containers.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageContainer&gt; object
     */
    Observable<ServiceResponse<Page<StorageContainer>>> listStorageContainersWithServiceResponseAsync(final String resourceGroupName, final String accountName, final String storageAccountName);

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
    StorageContainer getStorageContainer(String resourceGroupName, String accountName, String storageAccountName, String containerName);

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
    ServiceFuture<StorageContainer> getStorageContainerAsync(String resourceGroupName, String accountName, String storageAccountName, String containerName, final ServiceCallback<StorageContainer> serviceCallback);

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
    Observable<StorageContainer> getStorageContainerAsync(String resourceGroupName, String accountName, String storageAccountName, String containerName);

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
    Observable<ServiceResponse<StorageContainer>> getStorageContainerWithServiceResponseAsync(String resourceGroupName, String accountName, String storageAccountName, String containerName);

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
    PagedList<SasTokenInformation> listSasTokens(final String resourceGroupName, final String accountName, final String storageAccountName, final String containerName);

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
    ServiceFuture<List<SasTokenInformation>> listSasTokensAsync(final String resourceGroupName, final String accountName, final String storageAccountName, final String containerName, final ListOperationCallback<SasTokenInformation> serviceCallback);

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
    Observable<Page<SasTokenInformation>> listSasTokensAsync(final String resourceGroupName, final String accountName, final String storageAccountName, final String containerName);

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
    Observable<ServiceResponse<Page<SasTokenInformation>>> listSasTokensWithServiceResponseAsync(final String resourceGroupName, final String accountName, final String storageAccountName, final String containerName);

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;StorageAccountInformation&gt; object if successful.
     */
    PagedList<StorageAccountInformation> listByAccountNext(final String nextPageLink);

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @param serviceFuture the ServiceFuture object tracking the Retrofit calls
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<List<StorageAccountInformation>> listByAccountNextAsync(final String nextPageLink, final ServiceFuture<List<StorageAccountInformation>> serviceFuture, final ListOperationCallback<StorageAccountInformation> serviceCallback);

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageAccountInformation&gt; object
     */
    Observable<Page<StorageAccountInformation>> listByAccountNextAsync(final String nextPageLink);

    /**
     * Gets the first page of Azure Storage accounts, if any, linked to the specified Data Lake Analytics account. The response includes a link to the next page, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageAccountInformation&gt; object
     */
    Observable<ServiceResponse<Page<StorageAccountInformation>>> listByAccountNextWithServiceResponseAsync(final String nextPageLink);

    /**
     * Lists the Azure Storage containers, if any, associated with the specified Data Lake Analytics and Azure Storage account combination. The response includes a link to the next page of results, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;StorageContainer&gt; object if successful.
     */
    PagedList<StorageContainer> listStorageContainersNext(final String nextPageLink);

    /**
     * Lists the Azure Storage containers, if any, associated with the specified Data Lake Analytics and Azure Storage account combination. The response includes a link to the next page of results, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @param serviceFuture the ServiceFuture object tracking the Retrofit calls
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<List<StorageContainer>> listStorageContainersNextAsync(final String nextPageLink, final ServiceFuture<List<StorageContainer>> serviceFuture, final ListOperationCallback<StorageContainer> serviceCallback);

    /**
     * Lists the Azure Storage containers, if any, associated with the specified Data Lake Analytics and Azure Storage account combination. The response includes a link to the next page of results, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageContainer&gt; object
     */
    Observable<Page<StorageContainer>> listStorageContainersNextAsync(final String nextPageLink);

    /**
     * Lists the Azure Storage containers, if any, associated with the specified Data Lake Analytics and Azure Storage account combination. The response includes a link to the next page of results, if any.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;StorageContainer&gt; object
     */
    Observable<ServiceResponse<Page<StorageContainer>>> listStorageContainersNextWithServiceResponseAsync(final String nextPageLink);

    /**
     * Gets the SAS token associated with the specified Data Lake Analytics and Azure Storage account and container combination.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;SasTokenInformation&gt; object if successful.
     */
    PagedList<SasTokenInformation> listSasTokensNext(final String nextPageLink);

    /**
     * Gets the SAS token associated with the specified Data Lake Analytics and Azure Storage account and container combination.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @param serviceFuture the ServiceFuture object tracking the Retrofit calls
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    ServiceFuture<List<SasTokenInformation>> listSasTokensNextAsync(final String nextPageLink, final ServiceFuture<List<SasTokenInformation>> serviceFuture, final ListOperationCallback<SasTokenInformation> serviceCallback);

    /**
     * Gets the SAS token associated with the specified Data Lake Analytics and Azure Storage account and container combination.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;SasTokenInformation&gt; object
     */
    Observable<Page<SasTokenInformation>> listSasTokensNextAsync(final String nextPageLink);

    /**
     * Gets the SAS token associated with the specified Data Lake Analytics and Azure Storage account and container combination.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;SasTokenInformation&gt; object
     */
    Observable<ServiceResponse<Page<SasTokenInformation>>> listSasTokensNextWithServiceResponseAsync(final String nextPageLink);

}
