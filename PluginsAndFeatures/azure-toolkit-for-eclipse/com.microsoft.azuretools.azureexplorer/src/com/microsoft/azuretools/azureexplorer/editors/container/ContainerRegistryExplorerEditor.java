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
 */

package com.microsoft.azuretools.azureexplorer.editors.container;

import com.microsoft.azuretools.azurecommons.helpers.NotNull;
import com.microsoft.azuretools.azurecommons.util.Utils;
import com.microsoft.azuretools.core.components.AzureListenerWrapper;
import com.microsoft.azuretools.core.mvp.ui.containerregistry.ContainerRegistryProperty;
import com.microsoft.tooling.msservices.serviceexplorer.azure.container.ContainerRegistryPropertyMvpView;
import com.microsoft.tooling.msservices.serviceexplorer.azure.container.ContainerRegistryPropertyViewPresenter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import java.util.Collections;
import java.util.List;

public class ContainerRegistryExplorerEditor extends EditorPart implements ContainerRegistryPropertyMvpView {

    private static final String INSIGHT_NAME = "AzurePlugin.Eclipse.Editor.ContainerRegistryExplorerEditor";

    public static final String ID = "com.microsoft.azuretools.azureexplorer.editors.container.ContainerRegistryExplorerEditor";

    private final ContainerRegistryPropertyViewPresenter<ContainerRegistryExplorerEditor> containerExplorerPresenter;

    private static final String LABEL_NAME = "Registry Name";
    private static final String LABEL_TYPE = "Type";
    private static final String LABEL_RES_GRP = "Resource Group";
    private static final String LABEL_SUBSCRIPTION = "Subscription Id";
    private static final String LABEL_REGION = "Region";
    private static final String LABEL_LOGIN_SERVER_URL = "Login Server URL";
    private static final String LABEL_ADMIN_USER_ENABLED = "Admin User Enabled";
    private static final String LABEL_USER_NAME = "User Name";
    private static final String LABEL_PASSWORD = "Password";
    private static final String LABEL_PASSWORD2 = "Password2";
    private static final String LOADING = "<Loading...>";
    private static final String COPY_TO_CLIPBOARD = "<a>Copy to Clipboard</a>";

    private static final int PROGRESS_BAR_HEIGHT = 3;

    private String password = "";
    private String password2 = "";
    private String registryId;
    private String subscriptionId;
    private String currentRepo;

    private ScrolledComposite scrolledComposite;
    private Composite panelHolder;
    private Text txtRegistryName;
    private Text txtType;
    private Text txtResGrp;
    private Text txtSubscriptionId;
    private Text txtRegion;
    private Text txtLoginServerUrl;
    private Text txtUserName;
    private Label lblUserName;
    private Label lblPrimaryPassword;
    private Label lblSecondaryPassword;
    private Link lnkPrimaryPassword;
    private Link lnkSecondaryPassword;
    private Composite compAdminUserBtn;
    private Button btnEnable;
    private Button btnDisable;

    private boolean isAdminEnabled;

    private SashForm sashForm;
    private Composite cmpoRepo;
    private Composite cmpoTag;
    private ToolBar repoToolBar;
    private ToolItem tltmRefreshRepo;
    private ToolItem tltmRepoPreviousPage;
    private ToolItem tltmRepoNextPage;
    private org.eclipse.swt.widgets.List lstRepo;
    private Label label;
    private Label lblTag;
    private org.eclipse.swt.widgets.List lstTag;
    private ToolBar tagToolBar;
    private ToolItem tltmRefreshTag;
    private ToolItem tltmTagPreviousPage;
    private ToolItem tltmTagNextPage;
    private Composite container;
    private ProgressBar progressBar;

    public ContainerRegistryExplorerEditor() {
        this.containerExplorerPresenter = new ContainerRegistryPropertyViewPresenter<ContainerRegistryExplorerEditor>();
        this.containerExplorerPresenter.onAttachView(this);
    }

    @Override
    public void createPartControl(Composite parent) {

        scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        panelHolder = new Composite(scrolledComposite, SWT.NONE);
        GridLayout glPanelHolder = new GridLayout(1, false);
        glPanelHolder.marginWidth = 0;
        glPanelHolder.marginHeight = 0;
        glPanelHolder.verticalSpacing = 0;
        glPanelHolder.horizontalSpacing = 0;
        panelHolder.setLayout(glPanelHolder);

        setScrolledCompositeContent();
        setChildrenTransparent(panelHolder);

        progressBar = new ProgressBar(panelHolder, SWT.HORIZONTAL | SWT.INDETERMINATE);
        GridData gdProgressBar = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
        gdProgressBar.heightHint = PROGRESS_BAR_HEIGHT;
        progressBar.setLayoutData(gdProgressBar);

        container = new Composite(panelHolder, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        GridLayout glContainer = new GridLayout(4, false);
        glContainer.marginHeight = 2;
        glContainer.marginWidth = 0;
        glContainer.horizontalSpacing = 30;
        glContainer.verticalSpacing = 10;
        container.setLayout(glContainer);

        Label lblRegistryName = new Label(container, SWT.NONE);
        lblRegistryName.setText(LABEL_NAME);

        txtRegistryName = new Text(container, SWT.READ_ONLY);
        txtRegistryName.setText(LOADING);

        Label lblAdminUserEnabled = new Label(container, SWT.NONE);
        lblAdminUserEnabled.setText(LABEL_ADMIN_USER_ENABLED);

        compAdminUserBtn = new Composite(container, SWT.NONE);
        compAdminUserBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        GridLayout compositeLayout = new GridLayout(2, true);
        compositeLayout.marginWidth = 0;
        compositeLayout.marginHeight = 0;
        compositeLayout.horizontalSpacing = 0;
        compositeLayout.verticalSpacing = 0;
        compAdminUserBtn.setLayout(compositeLayout);

        btnEnable = new Button(compAdminUserBtn, SWT.NONE);
        btnEnable.setEnabled(false);
        btnEnable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btnEnable.setText("Enable");

        btnDisable = new Button(compAdminUserBtn, SWT.NONE);
        btnDisable.setEnabled(false);
        btnDisable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        btnDisable.setText("Disable");

        Label lblType = new Label(container, SWT.NONE);
        lblType.setText(LABEL_TYPE);

        txtType = new Text(container, SWT.READ_ONLY);
        txtType.setText(LOADING);

        lblUserName = new Label(container, SWT.NONE);
        lblUserName.setText(LABEL_USER_NAME);
        lblUserName.setVisible(false);

        txtUserName = new Text(container, SWT.READ_ONLY);
        txtUserName.setText(LOADING);
        txtUserName.setVisible(false);

        Label lblResourceGroup = new Label(container, SWT.NONE);
        lblResourceGroup.setText(LABEL_RES_GRP);

        txtResGrp = new Text(container, SWT.READ_ONLY);
        txtResGrp.setText(LOADING);

        lblPrimaryPassword = new Label(container, SWT.NONE);
        lblPrimaryPassword.setText(LABEL_PASSWORD);
        lblPrimaryPassword.setVisible(false);

        lnkPrimaryPassword = new Link(container, SWT.NONE);
        lnkPrimaryPassword.setText(COPY_TO_CLIPBOARD);
        lnkPrimaryPassword.setVisible(false);

        lnkPrimaryPassword.addListener(SWT.Selection,
                new AzureListenerWrapper(INSIGHT_NAME, "lnkPrimaryPassword", null) {
                    @Override
                    protected void handleEventFunc(Event event) {
                        try {
                            Utils.copyToSystemClipboard(password);
                        } catch (Exception e) {
                            onError(e.getMessage());
                        }
                    }
                });

        Label lblSubscriptionId = new Label(container, SWT.NONE);
        lblSubscriptionId.setText(LABEL_SUBSCRIPTION);

        txtSubscriptionId = new Text(container, SWT.READ_ONLY);
        txtSubscriptionId.setText(LOADING);

        lblSecondaryPassword = new Label(container, SWT.NONE);
        lblSecondaryPassword.setText(LABEL_PASSWORD2);
        lblSecondaryPassword.setVisible(false);

        lnkSecondaryPassword = new Link(container, SWT.NONE);
        lnkSecondaryPassword.setText(COPY_TO_CLIPBOARD);
        lnkSecondaryPassword.setVisible(false);

        lnkSecondaryPassword.addListener(SWT.Selection,
                new AzureListenerWrapper(INSIGHT_NAME, "lnkSecondaryPassword", null) {
                    @Override
                    protected void handleEventFunc(Event event) {
                        try {
                            Utils.copyToSystemClipboard(password2);
                        } catch (Exception e) {
                            onError(e.getMessage());
                        }
                    }
                });

        Label lblRegion = new Label(container, SWT.NONE);
        lblRegion.setText(LABEL_REGION);

        txtRegion = new Text(container, SWT.READ_ONLY);
        txtRegion.setText(LOADING);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);

        Label lblLoginServerUrl = new Label(container, SWT.NONE);
        lblLoginServerUrl.setText(LABEL_LOGIN_SERVER_URL);

        txtLoginServerUrl = new Text(container, SWT.READ_ONLY);
        txtLoginServerUrl.setText(LOADING);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);

        label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

        sashForm = new SashForm(container, SWT.NONE);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));

        cmpoRepo = new Composite(sashForm, SWT.NONE);
        cmpoRepo.setLayout(new GridLayout(1, false));

        Label lblRepo = new Label(cmpoRepo, SWT.NONE);
        lblRepo.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
        lblRepo.setText("Repository");

        lstRepo = new org.eclipse.swt.widgets.List(cmpoRepo, SWT.BORDER);
        lstRepo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        lstRepo.addListener(SWT.Selection, new AzureListenerWrapper(INSIGHT_NAME, "lstRepo", null) {
            @Override
            protected void handleEventFunc(Event event) {
                int index = lstRepo.getSelectionIndex();
                if (index < 0 || index >= lstRepo.getItemCount()) {
                    return;
                }
                String selectedRepo = lstRepo.getItem(index);
                if (Utils.isEmptyString(selectedRepo) || selectedRepo.equals(currentRepo)) {
                    return;
                }
                lstRepo.setEnabled(false);
                resetTagList();
                currentRepo = selectedRepo;
                progressBar.setVisible(true);
                containerExplorerPresenter.onListTags(subscriptionId, registryId, currentRepo, true /* isNextPage */);
            }
        });

        repoToolBar = new ToolBar(cmpoRepo, SWT.FLAT | SWT.RIGHT);
        repoToolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        tltmRefreshRepo = new ToolItem(repoToolBar, SWT.NONE);
        tltmRefreshRepo.setToolTipText("Refresh");
        tltmRefreshRepo.setText("Refresh");
        tltmRefreshRepo.addListener(SWT.Selection, new AzureListenerWrapper(INSIGHT_NAME, "tltmRefreshRepo", null) {
            @Override
            protected void handleEventFunc(Event event) {
                resetRepoList();
                progressBar.setVisible(true);
                containerExplorerPresenter.onRefreshRepositories(subscriptionId, registryId, true /* isNextPage */);
            }
        });

        tltmRepoPreviousPage = new ToolItem(repoToolBar, SWT.NONE);
        tltmRepoPreviousPage.setToolTipText("Previous page");
        tltmRepoPreviousPage
                .setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_BACK));
        tltmRepoPreviousPage.addListener(SWT.Selection,
                new AzureListenerWrapper(INSIGHT_NAME, "tltmRepoPreviousPage", null) {
                    @Override
                    protected void handleEventFunc(Event event) {
                        resetRepoList();
                        progressBar.setVisible(true);
                        containerExplorerPresenter.onListRepositories(subscriptionId, registryId,
                                false /* isNextPage */);
                    }
                });

        tltmRepoNextPage = new ToolItem(repoToolBar, SWT.NONE);
        tltmRepoNextPage.setToolTipText("Next page");
        tltmRepoNextPage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_FORWARD));
        tltmRepoNextPage.addListener(SWT.Selection, new AzureListenerWrapper(INSIGHT_NAME, "tltmRepoNextPage", null) {
            @Override
            protected void handleEventFunc(Event event) {
                resetRepoList();
                progressBar.setVisible(true);
                containerExplorerPresenter.onListRepositories(subscriptionId, registryId, false /* isNextPage */);
            }
        });
        cmpoTag = new Composite(sashForm, SWT.NONE);
        cmpoTag.setLayout(new GridLayout(1, false));

        lblTag = new Label(cmpoTag, SWT.NONE);
        lblTag.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
        lblTag.setText("Tag");

        lstTag = new org.eclipse.swt.widgets.List(cmpoTag, SWT.BORDER);
        lstTag.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        tagToolBar = new ToolBar(cmpoTag, SWT.FLAT | SWT.RIGHT);
        tagToolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        tltmRefreshTag = new ToolItem(tagToolBar, SWT.NONE);
        tltmRefreshTag.setToolTipText("Refresh");
        tltmRefreshTag.setText("Refresh");
        tltmRefreshTag.addListener(SWT.Selection, new AzureListenerWrapper(INSIGHT_NAME, "tltmRefreshTag", null) {
            @Override
            protected void handleEventFunc(Event event) {
                if (Utils.isEmptyString(currentRepo)) {
                    return;
                }
                resetTagList();
                progressBar.setVisible(true);
                containerExplorerPresenter.onListTags(subscriptionId, registryId, currentRepo, true /* isNextPage */);
            }
        });

        tltmTagPreviousPage = new ToolItem(tagToolBar, SWT.NONE);
        tltmTagPreviousPage.setToolTipText("Previous page");
        tltmTagPreviousPage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_BACK));
        tltmTagPreviousPage.addListener(SWT.Selection,
                new AzureListenerWrapper(INSIGHT_NAME, "tltmTagPreviousPage", null) {
                    @Override
                    protected void handleEventFunc(Event event) {
                        if (Utils.isEmptyString(currentRepo)) {
                            return;
                        }
                        resetTagList();
                        progressBar.setVisible(true);
                        containerExplorerPresenter.onListTags(subscriptionId, registryId, currentRepo,
                                false /* isNextPage */);
                    }
                });

        tltmTagNextPage = new ToolItem(tagToolBar, SWT.NONE);
        tltmTagNextPage.setToolTipText("Next page");
        tltmTagNextPage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_FORWARD));
        tltmTagNextPage.addListener(SWT.Selection, new AzureListenerWrapper(INSIGHT_NAME, "tltmTagNextPage", null) {
            @Override
            protected void handleEventFunc(Event event) {
                if (Utils.isEmptyString(currentRepo)) {
                    return;
                }
                resetTagList();
                progressBar.setVisible(true);
                containerExplorerPresenter.onListTags(subscriptionId, registryId, currentRepo, true /* isNextPage */);
            }
        });
        sashForm.setWeights(new int[] { 1, 1 });

        btnEnable.addListener(SWT.Selection, new AzureListenerWrapper(INSIGHT_NAME, "btnEnable", null) {
            @Override
            protected void handleEventFunc(Event event) {
                onAdminUserBtnClick();
            }
        });

        btnDisable.addListener(SWT.Selection, new AzureListenerWrapper(INSIGHT_NAME, "btnDisable", null) {
            @Override
            protected void handleEventFunc(Event event) {
                onAdminUserBtnClick();
            }
        });
        disableToolBarItems();
    }

    @Override
    public void onReadProperty(String sid, String id) {
        progressBar.setVisible(true);
        containerExplorerPresenter.onGetRegistryProperty(sid, id);
    }

    @Override
    public void showProperty(ContainerRegistryProperty property) {
        isAdminEnabled = property.isAdminEnabled();

        txtRegistryName.setText(property.getName());
        txtType.setText(property.getType());
        txtResGrp.setText(property.getGroupName());
        txtSubscriptionId.setText(subscriptionId);
        txtRegion.setText(property.getRegionName());
        txtLoginServerUrl.setText(property.getLoginServerUrl());

        compAdminUserBtn.setEnabled(true);
        updateAdminUserBtn(isAdminEnabled);
        lblUserName.setVisible(isAdminEnabled);
        txtUserName.setVisible(isAdminEnabled);
        lblPrimaryPassword.setVisible(isAdminEnabled);
        lnkPrimaryPassword.setVisible(isAdminEnabled);
        lblSecondaryPassword.setVisible(isAdminEnabled);
        lnkSecondaryPassword.setVisible(isAdminEnabled);
        resetRepoList();
        if (isAdminEnabled) {
            txtUserName.setText(property.getUserName());
            password = property.getPassword();
            password2 = property.getPassword2();
            progressBar.setVisible(true);
            containerExplorerPresenter.onRefreshRepositories(subscriptionId, registryId, true /* isNextPage */);
            sashForm.setVisible(true);
        } else {
            sashForm.setVisible(false);
            progressBar.setVisible(false);
        }

        panelHolder.layout();
        setScrolledCompositeContent();
        this.setPartName(property.getName());
    }

    @Override
    public void listRepo(List<String> repos) {
        lstRepo.removeAll();
        fillList(repos, lstRepo);
        if (containerExplorerPresenter.hasNextRepoPage()) {
            tltmRepoNextPage.setEnabled(true);
        } else {
            tltmRepoNextPage.setEnabled(false);
        }
        if (containerExplorerPresenter.hasPreviousRepoPage()) {
            tltmRepoPreviousPage.setEnabled(true);
        } else {
            tltmRepoPreviousPage.setEnabled(false);
        }
        tltmRefreshRepo.setEnabled(true);
        progressBar.setVisible(false);
    }

    @Override
    public void listTag(List<String> tags) {
        lstTag.removeAll();
        fillList(tags, lstTag);
        if (containerExplorerPresenter.hasNextRepoPage()) {
            tltmTagNextPage.setEnabled(true);
        } else {
            tltmTagNextPage.setEnabled(false);
        }
        if (containerExplorerPresenter.hasPreviousRepoPage()) {
            tltmTagPreviousPage.setEnabled(true);
        } else {
            tltmTagPreviousPage.setEnabled(false);
        }
        tltmRefreshTag.setEnabled(true);
        lstRepo.setEnabled(true);
        progressBar.setVisible(false);
    }

    @Override
    public void dispose() {
        this.containerExplorerPresenter.onDetachView();
        super.dispose();
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        if (input instanceof ContainerRegistryExplorerEditorInput) {
            ContainerRegistryExplorerEditorInput containerInput = (ContainerRegistryExplorerEditorInput) input;
            this.setPartName(containerInput.getName());
            this.subscriptionId = containerInput.getSubscriptionId();
            this.registryId = containerInput.getId();
            containerExplorerPresenter.onGetRegistryProperty(containerInput.getSubscriptionId(),
                    containerInput.getId());
        }

        IWorkbench workbench = PlatformUI.getWorkbench();
        final IWorkbenchPage activePage = workbench.getActiveWorkbenchWindow().getActivePage();
        workbench.addWorkbenchListener(new IWorkbenchListener() {
            @Override
            public boolean preShutdown(IWorkbench workbench, boolean forced) {
                activePage.closeEditor(ContainerRegistryExplorerEditor.this, true);
                return true;
            }

            @Override
            public void postShutdown(IWorkbench workbench) {
            }
        });
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void setFocus() {
    }

    @Override
    public void doSave(IProgressMonitor arg0) {
    }

    @Override
    public void doSaveAs() {
    }

    private void onAdminUserBtnClick() {
        compAdminUserBtn.setEnabled(false);
        btnEnable.setEnabled(false);
        btnDisable.setEnabled(false);
        progressBar.setVisible(true);
        this.containerExplorerPresenter.onEnableAdminUser(subscriptionId, registryId, !isAdminEnabled);
    }

    private void updateAdminUserBtn(boolean isAdminEnabled) {
        btnEnable.setEnabled(!isAdminEnabled);
        btnDisable.setEnabled(isAdminEnabled);
    }

    private void setScrolledCompositeContent() {
        scrolledComposite.setContent(panelHolder);
        scrolledComposite.setMinSize(panelHolder.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    private void setChildrenTransparent(Composite container) {
        if (container == null) {
            return;
        }
        Color transparentColor = container.getBackground();
        for (Control control : container.getChildren()) {
            if (control instanceof Text) {
                control.setBackground(transparentColor);
            }
        }
    }

    private void resetRepoList() {
        currentRepo = null;
        disableToolBarItems();
        lstRepo.removeAll();
        lstTag.removeAll();
    }

    private void resetTagList() {
        disableTagToolBarItems();
        lstTag.removeAll();
    }

    private void disableToolBarItems() {
        disableRepoToolBarItems();
        disableTagToolBarItems();
    }

    private void disableRepoToolBarItems() {
        tltmRefreshRepo.setEnabled(false);
        tltmRepoPreviousPage.setEnabled(false);
        tltmRepoNextPage.setEnabled(false);
    }

    private void disableTagToolBarItems() {
        tltmRefreshTag.setEnabled(false);
        tltmTagPreviousPage.setEnabled(false);
        tltmTagNextPage.setEnabled(false);
    }

    private void fillList(@NotNull List<String> list, @NotNull org.eclipse.swt.widgets.List widget) {
        if (list.size() > 0) {
            Collections.sort(list);
            for (String item : list) {
                widget.add(item);
            }
        }
    }
}
