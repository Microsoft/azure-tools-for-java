/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.intellij.connector;

import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.actionSystem.DataContext;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * the <b>{@code resource connection}</b>
 *
 * @param <R> type of the resource consumed by {@link C}
 * @param <C> type of the consumer consuming {@link R},
 *            it can only be {@link ModuleResource} for now({@code v3.52.0})
 * @since 3.52.0
 */
public interface Connection<R extends Resource, C extends Resource> {
    /**
     * @return the resource consumed by consumer
     */
    R getResource();

    /**
     * @return the consumer consuming resource
     */
    C getConsumer();

    /**
     * is this connection applicable for the specified {@code configuration}.<br>
     * - the {@code Connect Azure Resource} before run task will take effect if
     * applicable: the {@link #prepareBeforeRun} & {@link #updateJavaParametersAtRun}
     * will be called.
     *
     * @return true if this connection should intervene the specified {@code configuration}.
     */
    default boolean isApplicableFor(@NotNull RunConfiguration configuration) {
        return false;
    }

    /**
     * do some preparation in the {@code Connect Azure Resource} before run task
     * of the {@code configuration}<br>
     */
    boolean prepareBeforeRun(@NotNull RunConfiguration configuration, DataContext dataContext);

    /**
     * update java parameters exactly before start the {@code configuration}
     */
    default void updateJavaParametersAtRun(RunConfiguration configuration, @NotNull JavaParameters parameters) {
    }

    default String getType() {
        final String resourceType = Optional.ofNullable(getResource()).map(Resource::getType).filter(StringUtils::isNotBlank).orElse("default");
        final String consumerType = Optional.ofNullable(getConsumer()).map(Resource::getType).filter(StringUtils::isNotBlank).orElse("default");
        return resourceType + ":" + consumerType;
    }
}
