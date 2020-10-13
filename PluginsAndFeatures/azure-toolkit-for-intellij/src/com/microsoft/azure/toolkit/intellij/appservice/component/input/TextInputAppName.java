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

package com.microsoft.azure.toolkit.intellij.appservice.component.input;

import com.microsoft.azure.management.resources.Subscription;
import com.microsoft.azure.toolkit.lib.AzureValidationInfo;
import com.microsoft.intellij.util.ValidationUtils;

import java.util.Objects;

public class TextInputAppName extends AzureTextField {

    private Subscription subscription;

    @Override
    public AzureValidationInfo validateValue() {
        final AzureValidationInfo info = super.validateValue();
        if (Objects.isNull(info)) {
            try {
                ValidationUtils.validateAppServiceName(this.subscription.subscriptionId(), this.getValue());
            } catch (final IllegalArgumentException e) {
                return AzureValidationInfo.builder().input(this).message(e.getMessage()).type(AzureValidationInfo.Type.ERROR).build();
            }
        }
        return info;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
