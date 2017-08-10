/*
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

package com.microsoft.intellij.ui.webapp.deploysetting;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionToolbarPosition;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.packaging.artifacts.Artifact;
import com.intellij.packaging.impl.run.BuildArtifactsBeforeRunTaskProvider;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.microsoft.azure.management.appservice.AppServicePlan;
import com.microsoft.azure.management.appservice.JavaVersion;
import com.microsoft.azure.management.appservice.OperatingSystem;
import com.microsoft.azure.management.appservice.PricingTier;
import com.microsoft.azure.management.appservice.WebApp;
import com.microsoft.azure.management.resources.Location;
import com.microsoft.azure.management.resources.ResourceGroup;
import com.microsoft.azure.management.resources.Subscription;
import com.microsoft.azuretools.core.mvp.model.ResourceEx;
import com.microsoft.azuretools.utils.AzulZuluModel;
import com.microsoft.azuretools.utils.WebAppUtils;
import com.microsoft.intellij.runner.webapp.webappconfig.WebAppConfiguration;
import com.microsoft.intellij.runner.webapp.webappconfig.WebAppSettingModel;
import com.microsoft.intellij.util.MavenRunTaskUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.model.MavenConstants;
import org.jetbrains.idea.maven.project.MavenProject;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class WebAppSettingPanel implements WebAppDeployMvpView {

    // presenter
    private final WebAppDeployViewPresenter<WebAppSettingPanel> webAppDeployViewPresenter;

    // cache variable
    private ResourceEx<WebApp> selectedWebApp = null;
    private final Project project;
    private final WebAppConfiguration webAppConfiguration;
    private List<ResourceEx<WebApp>> cachedWebAppList = null;

    private String lastSelectedSid;
    private String lastSelectedResGrp;
    private String lastSelectedLocation;
    private String lastSelectedPriceTier;
    private Artifact lastSelectedArtifact;
    private boolean isArtifact;
    private WebAppSettingModel.JdkChoice lastJdkChoice = WebAppSettingModel.JdkChoice.DEFAULT;

    // const
    private static final String URL_PREFIX = "https://";
    private static final String NOT_APPLICABLE = "N/A";
    private static final String TABLE_LOADING_MESSAGE = "Loading ... ";
    private static final String TABLE_EMPTY_MESSAGE = "No available Web App.";
    private static final String DEFAULT_APP_NAME = "webapp-" ;
    private static final String DEFAULT_PLAN_NAME = "appsp-";
    private static final String DEFAULT_RGP_NAME = "rg-webapp-";


    //widgets
    private JPanel pnlRoot;
    private JPanel pnlExist;
    private JPanel pnlCreate;
    private JPanel pnlWebAppTable;
    private JCheckBox chkToRoot;
    private JRadioButton rdoUseExist;
    private JRadioButton rdoCreateNew;
    private JRadioButton rdoCreateAppServicePlan;
    private JRadioButton rdoUseExistAppServicePlan;
    private JRadioButton rdoCreateResGrp;
    private JRadioButton rdoUseExistResGrp;
    private JRadioButton rdoDefaultJdk;
    private JRadioButton rdoThirdPartyJdk;
    private JTextField txtWebAppName;
    private JTextField txtCreateAppServicePlan;
    private JTextField txtNewResGrp;
    private JTextField txtSelectedWebApp;
    private JComboBox<Subscription> cbSubscription;
    private JComboBox<WebAppUtils.WebContainerMod> cbWebContainer;
    private JComboBox<Location> cbLocation;
    private JComboBox<PricingTier> cbPricing;
    private JComboBox<AppServicePlan> cbExistAppServicePlan;
    private JComboBox<ResourceGroup> cbExistResGrp;
    private JComboBox<AzulZuluModel> cbThirdPartyJdk;
    private JComboBox<Artifact> cbArtifact;
    private HyperlinkLabel lblJdkLicense;
    private JLabel lblLocation;
    private JLabel lblPricing;
    private JLabel lblDefaultJdk;
    private JLabel lblArtifact;
    private JBTable table;
    private AnActionButton btnRefresh;

    private boolean isCbArtifactInited;

    /**
     * The setting panel for web app deployment run configuration.
     */
    public WebAppSettingPanel(Project project, @NotNull WebAppConfiguration webAppConfiguration) {
        this.project = project;
        this.webAppConfiguration = webAppConfiguration;
        this.webAppDeployViewPresenter = new WebAppDeployViewPresenter<>();
        this.webAppDeployViewPresenter.onAttachView(this);

        final ButtonGroup btnGrpForDeploy = new ButtonGroup();
        btnGrpForDeploy.add(rdoUseExist);
        btnGrpForDeploy.add(rdoCreateNew);
        rdoUseExist.addActionListener(e -> toggleDeployPanel(true /*isUsingExisting*/));
        rdoCreateNew.addActionListener(e -> toggleDeployPanel(false /*isUsingExisting*/));
        toggleDeployPanel(true /*showUsingExisting*/);

        final ButtonGroup btnGrpForResGrp = new ButtonGroup();
        btnGrpForResGrp.add(rdoUseExistResGrp);
        btnGrpForResGrp.add(rdoCreateResGrp);
        rdoCreateResGrp.addActionListener(e -> toggleResGrpPanel(true /*isCreatingNew*/));
        rdoUseExistResGrp.addActionListener(e -> toggleResGrpPanel(false /*isCreatingNew*/));

        final ButtonGroup btnGrpForAppServicePlan = new ButtonGroup();
        btnGrpForAppServicePlan.add(rdoUseExistAppServicePlan);
        btnGrpForAppServicePlan.add(rdoCreateAppServicePlan);
        rdoUseExistAppServicePlan.addActionListener(e -> toggleAppServicePlanPanel(false /*isCreatingNew*/));
        rdoCreateAppServicePlan.addActionListener(e -> toggleAppServicePlanPanel(true /*isCreatingNew*/));

        final ButtonGroup btnGrpForJdk = new ButtonGroup();
        btnGrpForJdk.add(rdoDefaultJdk);
        btnGrpForJdk.add(rdoThirdPartyJdk);
        rdoDefaultJdk.addActionListener(e -> {
            toggleJdkPanel(WebAppSettingModel.JdkChoice.DEFAULT);
            lastJdkChoice = WebAppSettingModel.JdkChoice.DEFAULT;
        });
        rdoThirdPartyJdk.addActionListener(e -> {
            toggleJdkPanel(WebAppSettingModel.JdkChoice.THIRD_PARTY);
            lastJdkChoice = WebAppSettingModel.JdkChoice.THIRD_PARTY;
            cbThirdPartyJdk.requestFocus();
        });

        cbExistResGrp.setRenderer(new ListCellRendererWrapper<ResourceGroup>() {
            @Override
            public void customize(JList list, ResourceGroup resourceGroup, int
                    index, boolean isSelected, boolean cellHasFocus) {
                if (resourceGroup != null) {
                    setText(resourceGroup.name());
                }
            }
        });

        cbExistResGrp.addActionListener(e -> {
            ResourceGroup resGrp = (ResourceGroup) cbExistResGrp.getSelectedItem();
            if (resGrp == null) {
                return;
            }
            String selectedGrp = resGrp.name();
            if (!Comparing.equal(lastSelectedResGrp, selectedGrp)) {
                cbExistAppServicePlan.removeAllItems();
                lblLocation.setText(NOT_APPLICABLE);
                lblPricing.setText(NOT_APPLICABLE);
                webAppDeployViewPresenter.onLoadAppServicePlan(lastSelectedSid, selectedGrp);
                lastSelectedResGrp = selectedGrp;
            }
        });

        cbSubscription.setRenderer(new ListCellRendererWrapper<Subscription>() {
            @Override
            public void customize(JList list, Subscription subscription, int
                    index, boolean isSelected, boolean cellHasFocus) {
                if (subscription != null) {
                    setText(subscription.displayName());
                }
            }
        });

        cbSubscription.addActionListener(e -> {
            Subscription subscription = (Subscription) cbSubscription.getSelectedItem();
            if (subscription == null) {
                return;
            }
            String selectedSid = subscription.subscriptionId();
            if (!Comparing.equal(lastSelectedSid, selectedSid)) {
                cbExistResGrp.removeAllItems();
                cbLocation.removeAllItems();
                webAppDeployViewPresenter.onLoadResourceGroups(selectedSid);
                webAppDeployViewPresenter.onLoadLocation(selectedSid);
                lastSelectedSid = selectedSid;
            }
        });

        cbLocation.setRenderer(new ListCellRendererWrapper<Location>() {
            @Override
            public void customize(JList list, Location location, int
                    index, boolean isSelected, boolean cellHasFocus) {
                if (location != null) {
                    setText(location.displayName());
                }
            }
        });

        cbLocation.addActionListener(e -> {
            Location location = (Location) cbLocation.getSelectedItem();
            if (location != null) {
                lastSelectedLocation = location.name();
            }
        });

        cbExistAppServicePlan.setRenderer(new ListCellRendererWrapper<AppServicePlan>() {
            @Override
            public void customize(JList list, AppServicePlan appServicePlan, int
                    index, boolean isSelected, boolean cellHasFocus) {
                if (appServicePlan != null) {
                    setText(appServicePlan.name());
                }
            }
        });

        cbExistAppServicePlan.addActionListener(e -> {
            AppServicePlan plan = (AppServicePlan) cbExistAppServicePlan.getSelectedItem();
            if (plan != null) {
                lblLocation.setText(plan.regionName());
                lblPricing.setText(plan.pricingTier().toString());
            }
        });

        cbPricing.addActionListener(e -> {
            PricingTier pricingTier = (PricingTier) cbPricing.getSelectedItem();
            if (pricingTier != null) {
                lastSelectedPriceTier = pricingTier.toString();
            }
        });

        cbThirdPartyJdk.setRenderer(new ListCellRendererWrapper<AzulZuluModel>() {
            @Override
            public void customize(JList list, AzulZuluModel azulZuluModel, int
                    index, boolean isSelected, boolean cellHasFocus) {
                if (azulZuluModel != null) {
                    setText(azulZuluModel.getName());
                }
            }
        });

        cbArtifact.addActionListener(e -> {
            final Artifact selectArtifact = (Artifact) cbArtifact.getSelectedItem();
            if (!Comparing.equal(lastSelectedArtifact, selectArtifact)) {
                if (lastSelectedArtifact != null && isCbArtifactInited) {
                    BuildArtifactsBeforeRunTaskProvider
                            .setBuildArtifactBeforeRunOption(pnlRoot, project, lastSelectedArtifact, false);
                }
                if (selectArtifact != null && isCbArtifactInited) {
                    BuildArtifactsBeforeRunTaskProvider
                            .setBuildArtifactBeforeRunOption(pnlRoot, project, selectArtifact, true);
                }
                lastSelectedArtifact = selectArtifact;
            }
        });

        isCbArtifactInited = false;

        cbArtifact.setRenderer(new ListCellRendererWrapper<Artifact>() {
            @Override
            public void customize(JList list, Artifact artifact, int index, boolean isSelected, boolean cellHasFocus) {
                if (artifact != null) {
                    setIcon(artifact.getArtifactType().getIcon());
                    setText(artifact.getName());
                }
            }
        });

        lblJdkLicense.setHyperlinkText("License");
        lblJdkLicense.setHyperlinkTarget(AzulZuluModel.getLicenseUrl());
    }

    /**
     * Set the artifacts into the combo box.
     */
    private void setupArtifactCombo(List<Artifact> artifacts) {
        isCbArtifactInited = false;
        cbArtifact.removeAllItems();
        for (Artifact artifact: artifacts) {
            cbArtifact.addItem(artifact);
            if (Comparing.equal(artifact.getOutputFilePath(), webAppConfiguration.getTargetPath())) {
                cbArtifact.setSelectedItem(artifact);
            }
        }
        cbArtifact.setVisible(true);
        lblArtifact.setVisible(true);
        isArtifact = true;
        isCbArtifactInited = true;
    }

    public JPanel getMainPanel() {
        return pnlRoot;
    }

    /**
     * Shared implementation of
     * {@link com.microsoft.intellij.runner.webapp.webappconfig.WebAppSettingEditor#resetEditorFrom(Object)}.
     */
    public void resetEditorFrom(@NotNull WebAppConfiguration webAppConfiguration) {
        if (!MavenRunTaskUtil.isMavenProject(webAppConfiguration.getProject())) {
            List<Artifact> artifacts = MavenRunTaskUtil.collectProjectArtifact(project);
            setupArtifactCombo(artifacts);
        }
        // Default values
        DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
        String date = df.format(new Date());
        if (webAppConfiguration.getWebAppName().isEmpty()) {
            txtWebAppName.setText(DEFAULT_APP_NAME + date);
        } else {
            txtWebAppName.setText(webAppConfiguration.getWebAppName());
        }
        if (webAppConfiguration.getAppServicePlan().isEmpty()) {
            txtCreateAppServicePlan.setText(DEFAULT_PLAN_NAME + date);
        } else {
            txtCreateAppServicePlan.setText(webAppConfiguration.getAppServicePlan());
        }
        if (webAppConfiguration.getResourceGroup().isEmpty()) {
            txtNewResGrp.setText(DEFAULT_RGP_NAME + date);
        } else {
            txtNewResGrp.setText(webAppConfiguration.getResourceGroup());
        }

        if (webAppConfiguration.isCreatingNew()) {
            rdoCreateNew.doClick();
            if (webAppConfiguration.isCreatingResGrp()) {
                rdoCreateResGrp.doClick();
            } else {
                rdoUseExistResGrp.doClick();
            }
            if (webAppConfiguration.isCreatingAppServicePlan()) {
                rdoCreateAppServicePlan.doClick();
            } else {
                rdoUseExistAppServicePlan.doClick();
            }
            if (Comparing.equal(webAppConfiguration.getJdkChoice(), WebAppSettingModel.JdkChoice.DEFAULT.toString())) {
                rdoDefaultJdk.doClick();
            } else {
                rdoThirdPartyJdk.doClick();
            }
        } else {
            rdoUseExist.doClick();
            chkToRoot.setSelected(webAppConfiguration.isDeployToRoot());
        }
        btnRefresh.setEnabled(false);
        this.webAppDeployViewPresenter.onLoadWebApps();
        this.webAppDeployViewPresenter.onLoadWebContainer();
        this.webAppDeployViewPresenter.onLoadSubscription();
        this.webAppDeployViewPresenter.onLoadPricingTier();
        this.webAppDeployViewPresenter.onLoadThirdPartyJdk();
    }

    /**
     * Shared implementation of
     * {@link com.microsoft.intellij.runner.webapp.webappconfig.WebAppSettingEditor#applyEditorTo(Object)}.
     */
    public void applyEditorTo(@NotNull WebAppConfiguration webAppConfiguration) {
        // Get war output full path and file name
        if (isArtifact && lastSelectedArtifact != null) {
            webAppConfiguration.setTargetPath(lastSelectedArtifact.getOutputFilePath());
            Path p = Paths.get(webAppConfiguration.getTargetPath());
            if (p != null) {
                webAppConfiguration.setTargetName(p.getFileName().toString());
            } else {
                webAppConfiguration.setTargetName(lastSelectedArtifact.getName() + "." + MavenConstants.TYPE_WAR);
            }
        } else {
            MavenProject mavenProject = MavenRunTaskUtil.getMavenProject(project);
            if (mavenProject != null) {
                String targetPath = new File(mavenProject.getBuildDirectory()).getPath()
                        + File.separator + mavenProject.getFinalName() + "." + mavenProject.getPackaging();
                String targetName = mavenProject.getFinalName() + "." + mavenProject.getPackaging();
                webAppConfiguration.setTargetPath(targetPath);
                webAppConfiguration.setTargetName(targetName);
            }
        }

        if (rdoUseExist.isSelected()) {
            webAppConfiguration.setWebAppId(selectedWebApp == null ? "" : selectedWebApp.getResource().id());
            webAppConfiguration.setSubscriptionId(selectedWebApp == null ? "" : selectedWebApp.getSubscriptionId());
            webAppConfiguration.setDeployToRoot(chkToRoot.isSelected());
            webAppConfiguration.setCreatingNew(false);
        } else if (rdoCreateNew.isSelected()) {
            webAppConfiguration.setWebAppName(txtWebAppName.getText());
            webAppConfiguration.setSubscriptionId(lastSelectedSid);
            WebAppUtils.WebContainerMod container = (WebAppUtils.WebContainerMod) cbWebContainer.getSelectedItem();
            webAppConfiguration.setWebContainer(container == null ? "" : container.getValue());
            // resource group
            if (rdoCreateResGrp.isSelected()) {
                webAppConfiguration.setCreatingResGrp(true);
                webAppConfiguration.setResourceGroup(txtNewResGrp.getText());
            } else {
                webAppConfiguration.setCreatingResGrp(false);
                webAppConfiguration.setResourceGroup(lastSelectedResGrp);
            }
            // app service plan
            if (rdoCreateAppServicePlan.isSelected()) {
                webAppConfiguration.setCreatingAppServicePlan(true);
                webAppConfiguration.setAppServicePlan(txtCreateAppServicePlan.getText());

                webAppConfiguration.setRegion(lastSelectedLocation == null ? "" : lastSelectedLocation);
                webAppConfiguration.setPricing(lastSelectedPriceTier == null ? "" : lastSelectedPriceTier);
            } else {
                webAppConfiguration.setCreatingAppServicePlan(false);
                AppServicePlan appServicePlan = (AppServicePlan) cbExistAppServicePlan.getSelectedItem();
                if (appServicePlan != null) {
                    webAppConfiguration.setAppServicePlan(appServicePlan.id());
                }
            }
            // JDK
            switch (lastJdkChoice) {
                case DEFAULT:
                    webAppConfiguration.setJdkChoice(lastJdkChoice.toString());
                    break;
                case THIRD_PARTY:
                    webAppConfiguration.setJdkChoice(lastJdkChoice.toString());
                    AzulZuluModel azulZuluModel = (AzulZuluModel) cbThirdPartyJdk.getSelectedItem();
                    webAppConfiguration.setJdkUrl(azulZuluModel == null ? "" : azulZuluModel.getDownloadUrl());
                    break;
                default:
                    break;
            }
            webAppConfiguration.setCreatingNew(true);
        }
    }

    @Override
    public void renderWebAppsTable(@NotNull List<ResourceEx<WebApp>> webAppLists) {
        btnRefresh.setEnabled(true);
        table.getEmptyText().setText(TABLE_EMPTY_MESSAGE);
        List<ResourceEx<WebApp>> sortedList = webAppLists.stream()
                .filter(resource -> resource.getResource().javaVersion() != JavaVersion.OFF)
                .sorted((a, b) -> a.getSubscriptionId().compareToIgnoreCase(b.getSubscriptionId()))
                .collect(Collectors.toList());
        cachedWebAppList = sortedList;
        if (sortedList.size() > 0) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.getDataVector().clear();
            for (int i = 0; i < sortedList.size(); i++) {
                WebApp app = sortedList.get(i).getResource();
                model.addRow(new String[]{
                        app.name(),
                        app.javaVersion().toString(),
                        app.javaContainer() + " " + app.javaContainerVersion(),
                        app.resourceGroupName(),
                });
                if (Comparing.equal(app.id(), webAppConfiguration.getWebAppId())) {
                    table.setRowSelectionInterval(i, i);
                }
            }
        }
    }

    private void toggleDeployPanel(boolean isUsingExisting) {
        pnlExist.setVisible(isUsingExisting);
        pnlCreate.setVisible(!isUsingExisting);
    }

    private void toggleResGrpPanel(boolean isCreatingNew) {
        txtNewResGrp.setEnabled(isCreatingNew);
        cbExistResGrp.setEnabled(!isCreatingNew);
    }

    private void toggleAppServicePlanPanel(boolean isCreatingNew) {
        txtCreateAppServicePlan.setEnabled(isCreatingNew);
        cbLocation.setEnabled(isCreatingNew);
        cbPricing.setEnabled(isCreatingNew);
        cbExistAppServicePlan.setEnabled(!isCreatingNew);
        lblLocation.setEnabled(!isCreatingNew);
        lblPricing.setEnabled(!isCreatingNew);
    }

    private void toggleJdkPanel(WebAppSettingModel.JdkChoice choice) {
        switch (choice) {
            case DEFAULT:
                lblDefaultJdk.setEnabled(true);
                cbThirdPartyJdk.setEnabled(false);
                break;
            case THIRD_PARTY:
                lblDefaultJdk.setEnabled(false);
                cbThirdPartyJdk.setEnabled(true);
                break;
            default:
                break;
        }
    }

    private void resetWidget() {
        btnRefresh.setEnabled(false);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.getDataVector().clear();
        model.fireTableDataChanged();
        table.getEmptyText().setText(TABLE_LOADING_MESSAGE);
        txtSelectedWebApp.setText("");
    }

    private void createUIComponents() {
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.addColumn("Name");
        tableModel.addColumn("JDK");
        tableModel.addColumn("Web container");
        tableModel.addColumn("Resource group");

        table = new JBTable(tableModel);
        table.getEmptyText().setText(TABLE_LOADING_MESSAGE);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getSelectionModel().addListSelectionListener(event -> {
            if (event.getValueIsAdjusting()) {
                return;
            }
            if (table.getSelectedRow() < 0) {
                selectedWebApp = null;
                return;
            }
            if (cachedWebAppList != null) {
                selectedWebApp = cachedWebAppList.get(event.getFirstIndex());
                txtSelectedWebApp.setText(selectedWebApp.toString());
            }
        });

        btnRefresh = new AnActionButton("Refresh", AllIcons.Actions.Refresh) {
            @Override
            public void actionPerformed(AnActionEvent anActionEvent) {
                resetWidget();
                webAppDeployViewPresenter.onRefresh();
            }
        };

        ToolbarDecorator tableToolbarDecorator = ToolbarDecorator.createDecorator(table)
                .addExtraActions(btnRefresh).setToolbarPosition(ActionToolbarPosition.TOP);

        pnlWebAppTable = tableToolbarDecorator.createPanel();
    }

    @Override
    public void fillSubscription(List<Subscription> subscriptions) {
        cbSubscription.removeAllItems();
        for (Subscription subscription: subscriptions) {
            cbSubscription.addItem(subscription);
            if (Comparing.equal(subscription.subscriptionId(), webAppConfiguration.getSubscriptionId())) {
                cbSubscription.setSelectedItem(subscription);
            }
        }
    }

    @Override
    public void fillResourceGroup(List<ResourceGroup> resourceGroups) {
        cbExistResGrp.removeAllItems();
        for (ResourceGroup group: resourceGroups) {
            cbExistResGrp.addItem(group);
            if (Comparing.equal(group.name(), webAppConfiguration.getResourceGroup())) {
                cbExistResGrp.setSelectedItem(group);
            }
        }
    }

    @Override
    public void fillAppServicePlan(List<AppServicePlan> appServicePlans) {
        cbExistAppServicePlan.removeAllItems();
        for (AppServicePlan plan: appServicePlans) {
            if (Comparing.equal(plan.operatingSystem(), OperatingSystem.WINDOWS)) {
                cbExistAppServicePlan.addItem(plan);
                if (Comparing.equal(plan.id(), webAppConfiguration.getAppServicePlan())) {
                    cbExistAppServicePlan.setSelectedItem(plan);
                }
            }
        }
    }

    @Override
    public void fillLocation(List<Location> locations) {
        cbLocation.removeAllItems();
        for (Location location: locations) {
            cbLocation.addItem(location);
            if (Comparing.equal(location.name(), webAppConfiguration.getRegion())) {
                cbLocation.setSelectedItem(location);
            }
        }
    }

    @Override
    public void fillPricingTier(List<PricingTier> prices) {
        cbPricing.removeAllItems();
        for (PricingTier price: prices) {
            cbPricing.addItem(price);
            if (Comparing.equal(price.toString(), webAppConfiguration.getPricing())) {
                cbPricing.setSelectedItem(price);
            }
        }
    }

    @Override
    public void fillWebContainer(List<WebAppUtils.WebContainerMod> webContainers) {
        cbWebContainer.removeAllItems();
        for (WebAppUtils.WebContainerMod container: webContainers) {
            cbWebContainer.addItem(container);
            if (Comparing.equal(container.toString(), webAppConfiguration.getWebContainer())) {
                cbWebContainer.setSelectedItem(container);
            }
        }
    }

    @Override
    public void fillThirdPartyJdk(List<AzulZuluModel> jdks) {
        cbThirdPartyJdk.removeAllItems();
        for (AzulZuluModel jdk: jdks) {
            cbThirdPartyJdk.addItem(jdk);
            if (Comparing.equal(jdk.getDownloadUrl(), webAppConfiguration.getJdkUrl())) {
                cbThirdPartyJdk.setSelectedItem(jdk);
            }
        }
    }
}
