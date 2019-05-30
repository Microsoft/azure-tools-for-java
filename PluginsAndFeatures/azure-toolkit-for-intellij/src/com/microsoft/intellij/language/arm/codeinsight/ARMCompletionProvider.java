/**
 * Copyright (c) Microsoft Corporation
 * <p/>
 * All rights reserved.
 * <p/>
 * MIT License
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.microsoft.intellij.language.arm.codeinsight;

import com.google.common.collect.Sets;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.json.psi.impl.JsonPropertyImpl;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.microsoft.intellij.language.arm.ARMLanguage;
import java.util.Arrays;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class ARMCompletionProvider extends CompletionProvider<CompletionParameters> {
    public static final ARMCompletionProvider INSTANCE = new ARMCompletionProvider();
    private static final Set<String> KEYWORDS = Sets.newConcurrentHashSet(Arrays.asList("$schema", "contentVersion",
        "parameters", "variables", "resources", "outputs"));
    private static final Set<String> RESOURCES = Sets.newConcurrentHashSet(Arrays.asList("type", "apiVersion", "name",
        "location", "sku", "properties"));
    private static final Set<String> PARAMETERS = Sets.newConcurrentHashSet();
    private static final Set<String> VARIABLES = Sets.newConcurrentHashSet();
    private static final Set<String> OUTPUTS = Sets.newConcurrentHashSet();
    private static final String[] SCOPES = new String[]{"parameters", "variables", "resources", "outputs"};

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context,
        @NotNull CompletionResultSet result) {
        long timeS = System.currentTimeMillis();
        try {
            if (parameters.getOriginalFile().getLanguage().getID().equals(ARMLanguage.ID)) {
                PsiElement position = parameters.getPosition();
                if (isProperty(position)) {
                    Scope scope = findScope(position);
                    if (scope == Scope.root) {
                        for (String keyword : KEYWORDS) {
                            result.addElement(LookupElementBuilder.create(keyword).bold());
                        }
                    } else if (scope == Scope.resources) {
                        for (String resource : RESOURCES) {
                            result.addElement(LookupElementBuilder.create(resource).bold());
                        }
                    } else if (scope == Scope.variables) {

                    }
                } else {

                }
            }
        } catch (Exception ignore) {
            System.out.println(ignore);
        }
        System.out.println("time cost: " + (System.currentTimeMillis() - timeS));
    }

    private boolean isProperty(PsiElement position) {
        PsiElement parentElement = position.getParent().getParent().getOriginalElement();
        if (parentElement instanceof JsonPropertyImpl) {
            if (((JsonPropertyImpl) parentElement).getValue() == null) {
                return true;
            }
            return !position.getText().equals(((JsonPropertyImpl) parentElement).getValue().getText());
        }
        return false;
    }

    private Scope findScope(PsiElement position) {
        try {
            while (position.getParent().getParent().getParent() != null) {
                position = position.getParent();
            }
            String positionName = ((JsonPropertyImpl) position).getName();
            return Scope.valueOf(positionName);
        } catch (Exception ignore) {
            return Scope.root;
        }
    }

    enum Scope {
        parameters, variables, resources, outputs, root
    }
}
