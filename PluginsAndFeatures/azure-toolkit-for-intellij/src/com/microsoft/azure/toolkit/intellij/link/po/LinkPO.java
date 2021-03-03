/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.azure.toolkit.intellij.link.po;

import com.microsoft.azure.toolkit.intellij.link.base.LinkType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(exclude = {"serviceId"})
@AllArgsConstructor
public class LinkPO {

    private String serviceId;
    private String moduleId;
    private LinkType type;
    private String envPrefix;

}
