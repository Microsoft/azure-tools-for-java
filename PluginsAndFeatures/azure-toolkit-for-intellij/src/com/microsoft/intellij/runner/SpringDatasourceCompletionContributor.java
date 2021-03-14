/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.intellij.runner;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.lookup.AutoCompletionPolicy;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import com.microsoft.azure.toolkit.intellij.link.LinkMySQLToModuleDialog;
import com.microsoft.azure.toolkit.intellij.link.base.LinkType;
import com.microsoft.azure.toolkit.intellij.link.po.LinkPO;
import com.microsoft.intellij.AzureLinkStorage;
import com.microsoft.intellij.helpers.AzureIconLoader;
import com.microsoft.tooling.msservices.serviceexplorer.AzureIconSymbol;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SpringDatasourceCompletionContributor extends CompletionContributor {

    public SpringDatasourceCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        resultSet.addElement(LookupElementBuilder
                                .create("spring.datasource.url")
                                .withIcon(AzureIconLoader.loadIcon(AzureIconSymbol.MySQL.BIND_INTO))
                                .withInsertHandler(new MyInsertHandler())
                                .withBoldness(true)
                                .withTypeText("String")
                                .withTailText(" (Connect to Azure Datasource for MySQL)")
                                .withAutoCompletionPolicy(AutoCompletionPolicy.SETTINGS_DEPENDENT)
                        );
                    }
                }
        );

    }

    private class MyInsertHandler implements InsertHandler<LookupElement> {

        @Override
        public void handleInsert(@NotNull InsertionContext insertionContext, @NotNull LookupElement lookupElement) {
            Module module = ModuleUtil.findModuleForFile(insertionContext.getFile().getVirtualFile(), insertionContext.getProject());
            LinkPO moduleLink = AzureLinkStorage.getProjectStorage(insertionContext.getProject()).getLinkByModuleId(module.getName())
                    .stream()
                    .filter(e -> LinkType.SERVICE_WITH_MODULE == e.getType())
                    .findFirst().orElse(null);
            if (Objects.nonNull(moduleLink)) {
                String envPrefix = moduleLink.getEnvPrefix();
                this.insertSpringDatasourceProperties(envPrefix, insertionContext);
            } else {
                ApplicationManager.getApplication().invokeLater(() -> {
                    final LinkMySQLToModuleDialog dialog = new LinkMySQLToModuleDialog(insertionContext.getProject(), null, module);
                    String envPrefix = dialog.showAndGetEnvPrefix();
                    WriteCommandAction.runWriteCommandAction(insertionContext.getProject(), () -> {
                        if (StringUtils.isNotBlank(envPrefix)) {
                            this.insertSpringDatasourceProperties(envPrefix, insertionContext);
                        } else {
                            EditorModificationUtil.insertStringAtCaret(insertionContext.getEditor(), "=", true);
                        }
                    });
                });

            }
        }

        private void insertSpringDatasourceProperties(String envPrefix, @NotNull InsertionContext insertionContext) {
            StringBuilder builder = new StringBuilder();
            builder.append("=${").append(envPrefix).append("URL}").append(StringUtils.LF)
                    .append("spring.datasource.username=${").append(envPrefix).append("USERNAME}").append(StringUtils.LF)
                    .append("spring.datasource.password=${").append(envPrefix).append("PASSWORD}").append(StringUtils.LF);
            EditorModificationUtil.insertStringAtCaret(insertionContext.getEditor(), builder.toString(), true);
        }
    }

}
