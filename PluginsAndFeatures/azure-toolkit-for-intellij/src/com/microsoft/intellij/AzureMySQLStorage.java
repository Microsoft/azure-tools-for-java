/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.intellij;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.microsoft.azure.toolkit.intellij.link.base.ServiceType;
import com.microsoft.azure.toolkit.intellij.link.mysql.PasswordSaveType;
import com.microsoft.azure.toolkit.intellij.link.po.MySQLServicePO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Content;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

public class AzureMySQLStorage extends AzureSecurityServiceStorage<MySQLServicePO> {

    private AzureMySQLStorage() {
    }

    public static AzureMySQLStorage getStorage() {
        return ServiceManager.getService(AzureMySQLStorage.App.class);
    }

    public static AzureMySQLStorage getProjectStorage(Project project) {
        return ServiceManager.getService(project, AzureMySQLStorage.Prj.class);
    }

    public static class AzureMySQLStorageStateComponent extends AzureMySQLStorage {

        public Element getState() {
            Element rootElement = new Element(ELEMENT_NAME_SERVICES);
            this.writeState(rootElement);
            return rootElement;
        }

        public void loadState(@NotNull Element state) {
            this.readState(state);
        }

        private void writeState(Element servicesElement) {
            for (MySQLServicePO service : super.getServices()) {
                Element serviceElement = new Element(ELEMENT_NAME_SERVICE);
                serviceElement.setAttribute("type", service.getType().getName());
                serviceElement.setAttribute("id", service.getId());
                serviceElement.addContent(new Element("url").setText(service.getUrl()));
                serviceElement.addContent(new Element("username").setText(service.getUsername()));
                serviceElement.addContent(new Element("passwordSave").setText(service.getPasswordSave().name()));
                servicesElement.addContent(serviceElement);
            }
        }

        private void readState(Element servicesElement) {
            if (CollectionUtils.isEmpty(servicesElement.getContent())) {
                return;
            }
            for (Content content : servicesElement.getContent()) {
                if (!(content instanceof Element)) {
                    continue;
                }
                Element serviceElement = (Element) content;
                final String id = serviceElement.getAttributeValue("id");
                String serviceTypeName = serviceElement.getAttributeValue("type");
                ServiceType serviceType = ServiceType.parseTypeByName(serviceTypeName);
                if (CollectionUtils.size(serviceElement.getContent()) != 3) {
                    continue;
                }
                String url = null;
                String username = null;
                String passwordSave = null;
                for (Content innerContent : serviceElement.getContent()) {
                    if (!(content instanceof Element)) {
                        continue;
                    }
                    Element innerElement = (Element) innerContent;
                    if ("url".equals(innerElement.getName())) {
                        url = innerElement.getText();
                    } else if ("username".equals(innerElement.getName())) {
                        username = innerElement.getText();
                    } else if ("passwordSave".equals(innerElement.getName())) {
                        passwordSave = innerElement.getText();
                    }
                }
                if (super.getServices().stream().filter(e -> StringUtils.equals(e.getId(), id)).count() <= 0L) {
                    MySQLServicePO service = new MySQLServicePO.Builder()
                            .id(id)
                            .url(url)
                            .username(username)
                            .passwordSave(PasswordSaveType.valueOf(passwordSave))
                            .build();
                    super.getServices().add(service);
                }
            }
        }
    }

    @State(
            name = ELEMENT_NAME_SERVICES,
            storages = {@Storage("azure/azureServices.xml")}
    )
    public static class App extends AzureMySQLStorageStateComponent implements PersistentStateComponent<Element> {
    }

    @State(
            name = ELEMENT_NAME_SERVICES,
            storages = {@Storage("azure/azureServices.xml")}
    )
    public static class Prj extends AzureMySQLStorageStateComponent implements PersistentStateComponent<Element> {
    }

}
