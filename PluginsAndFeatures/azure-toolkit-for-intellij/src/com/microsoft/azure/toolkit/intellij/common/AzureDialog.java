/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */
package com.microsoft.azure.toolkit.intellij.common;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.microsoft.azure.toolkit.lib.common.form.AzureForm;
import com.microsoft.azure.toolkit.lib.common.form.AzureFormInput;
import com.microsoft.azure.toolkit.lib.common.form.AzureValidationInfo;
import com.microsoft.intellij.ui.components.AzureDialogWrapper;
import lombok.extern.java.Log;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log
public abstract class AzureDialog<T> extends AzureDialogWrapper {
    protected OkActionListener<T> okActionListener;

    public AzureDialog(Project project) {
        super(project, true);
        setTitle(this.getDialogTitle());
        setModal(true);
    }

    public AzureDialog() {
        super(true);
        setTitle(this.getDialogTitle());
        setModal(true);
    }

    @Override
    protected void doOKAction() {
        if (Objects.nonNull(this.okActionListener)) {
            final T data = this.getForm().getData();
            this.okActionListener.onOk(data);
        } else {
            super.doOKAction();
        }
    }

    public void close() {
        this.doCancelAction();
    }

    @Override
    protected List<ValidationInfo> doValidateAll() {
        final List<AzureValidationInfo> infos = this.getForm().validateData();
        this.setOKActionEnabled(infos.stream().noneMatch(
                i -> i == AzureValidationInfo.PENDING || i.getType() == AzureValidationInfo.Type.ERROR || AzureValidationInfo.UNINITIALIZED.equals(i)));
        List<ValidationInfo> resultList = infos.stream()
                    .filter(i -> i != AzureValidationInfo.PENDING && i != AzureValidationInfo.OK && !AzureValidationInfo.UNINITIALIZED.equals(i))
                    .map(AzureDialog::toIntellijValidationInfo)
                    .collect(Collectors.toList());
        // this is in order to let ok action disable if only there is any uninitialized filed.
        if (infos.stream().filter(e -> AzureValidationInfo.UNINITIALIZED.equals(e)).count() > 0L) {
            setErrorInfoAll(resultList);
        }
        return resultList;
    }

    //TODO: @wangmi move to some util class
    private static ValidationInfo toIntellijValidationInfo(final AzureValidationInfo info) {
        final AzureFormInput<?> input = info.getInput();
        if (input instanceof AzureFormInputComponent) {
            final JComponent component = ((AzureFormInputComponent<?>) input).getInputComponent();
            return new ValidationInfo(info.getMessage(), component);
        }
        return new ValidationInfo(info.getMessage(), null);
    }

    public abstract AzureForm<T> getForm();

    protected abstract String getDialogTitle();

    public void setOkActionListener(OkActionListener<T> listener) {
        this.okActionListener = listener;
    }

    @FunctionalInterface
    public interface OkActionListener<T> {
        void onOk(T data);
    }
}
