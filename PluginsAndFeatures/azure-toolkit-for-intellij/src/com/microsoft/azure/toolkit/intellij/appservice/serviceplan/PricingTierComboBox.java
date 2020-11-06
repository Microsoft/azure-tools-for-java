/*
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
 */

package com.microsoft.azure.toolkit.intellij.appservice.serviceplan;

import com.microsoft.azure.management.appservice.PricingTier;
import com.microsoft.azure.toolkit.intellij.common.AzureComboBox;
import com.microsoft.azuretools.azurecommons.helpers.NotNull;
import com.microsoft.azuretools.core.mvp.model.function.AzureFunctionMvpModel;

import java.util.Collections;
import java.util.List;

public class PricingTierComboBox extends AzureComboBox<PricingTier> {

    private PricingTier defaultPricingTier = PricingTier.BASIC_B2;
    private List<PricingTier> pricingTierList = Collections.EMPTY_LIST;

    public PricingTierComboBox() {
        super();
    }

    public void setDefaultPricingTier(final PricingTier defaultPricingTier) {
        this.defaultPricingTier = defaultPricingTier;
        setValue(defaultPricingTier);
    }

    public void setPricingTierList(final List<PricingTier> pricingTierList) {
        this.pricingTierList = pricingTierList;
    }

    @Override
    protected String getItemText(final Object item) {
        return item == AzureFunctionMvpModel.CONSUMPTION_PRICING_TIER ? "Consumption" : super.getItemText(item);
    }

    @NotNull
    @Override
    protected List<? extends PricingTier> loadItems() throws Exception {
        return pricingTierList;
    }
}
