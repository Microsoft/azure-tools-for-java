package com.microsoft.intellij.runner.functions.deploy.ui.creation;


import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.microsoft.azure.management.appservice.FunctionApp;
import com.microsoft.azure.management.appservice.OperatingSystem;
import com.microsoft.azuretools.authmanage.AuthMethodManager;
import com.microsoft.azuretools.telemetry.AppInsightsClient;
import com.microsoft.azuretools.telemetrywrapper.EventType;
import com.microsoft.azuretools.telemetrywrapper.EventUtil;
import com.microsoft.azuretools.utils.AzureUIRefreshCore;
import com.microsoft.azuretools.utils.AzureUIRefreshEvent;
import com.microsoft.intellij.runner.functions.IntelliJFunctionContext;
import com.microsoft.intellij.runner.functions.IntelliJFunctionRuntimeConfiguration;
import com.microsoft.intellij.runner.functions.component.AppServicePlanPanel;
import com.microsoft.intellij.runner.functions.component.ResourceGroupPanel;
import com.microsoft.intellij.runner.functions.component.SubscriptionPanel;
import com.microsoft.intellij.runner.functions.component.table.AppSettingsTable;
import com.microsoft.intellij.runner.functions.component.table.AppSettingsTableUtils;
import com.microsoft.intellij.runner.functions.library.function.CreateFunctionHandler;
import org.jetbrains.annotations.Nullable;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.microsoft.azuretools.telemetry.TelemetryConstants.CREATE_WEBAPP;
import static com.microsoft.azuretools.telemetry.TelemetryConstants.WEBAPP;

public class FunctionCreationDialog extends JDialog {

    private static final String DIALOG_TITLE = "Create Function App";
    private static final String WARNING_MESSAGE = "<html><font size=\"3\" color=\"red\">%s</font></html>";

    private JPanel contentPanel;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel pnlCreate;
    private JTextField txtWebAppName;
    private JRadioButton rdoLinuxOS;
    private JRadioButton rdoWindowsOS;
    private JLabel lblOS;
    private JLabel lblMessage;
    private JPanel lblPanelRoot;
    private JPanel pnlAppSettings;
    private ResourceGroupPanel resourceGroupPanel;
    private SubscriptionPanel subscriptionPanel;
    private AppServicePlanPanel appServicePlanPanel;
    private AppSettingsTable appSettingsTable;

    private IntelliJFunctionContext functionConfiguration;
    private FunctionApp result = null;
    private Project project;

    public FunctionCreationDialog(Project project) {
        this.project = project;

        setModal(true);
        setTitle(DIALOG_TITLE);
        setContentPane(contentPanel);
        getRootPane().setDefaultButton(buttonOK);

        this.functionConfiguration = new IntelliJFunctionContext(project);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        rdoLinuxOS.addActionListener(e -> selectOS(false));
        rdoWindowsOS.addActionListener(e -> selectOS(false));

        final ButtonGroup osButtonGroup = new ButtonGroup();
        osButtonGroup.add(rdoLinuxOS);
        osButtonGroup.add(rdoWindowsOS);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        addValidationListener(contentPanel, e -> validateConfiguration());
        init();

        subscriptionPanel.addItemListener(e -> {
            final String subscriptionId = subscriptionPanel.getSubscriptionId();
            if (subscriptionId != null) {
                resourceGroupPanel.loadResourceGroup(subscriptionId);
                appServicePlanPanel.loadAppServicePlan(subscriptionId, getSelectedOperationSystemEnum());
            }
        });
    }

    public FunctionApp getCreatedWebApp() {
        return this.result;
    }

    public OperatingSystem getSelectedOperationSystemEnum() {
        return rdoWindowsOS.isSelected() ? OperatingSystem.WINDOWS : OperatingSystem.LINUX;
    }

    private void addValidationListener(Container parent, ActionListener actionListener) {
        for (final Component component : parent.getComponents()) {
            if (component instanceof AbstractButton) {
                ((AbstractButton) component).addActionListener(actionListener);
            } else if (component instanceof JComboBox) {
                ((JComboBox) component).addActionListener(actionListener);
            } else if (component instanceof JTextField) {
                ((JTextField) component).getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        validateConfiguration();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        validateConfiguration();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        validateConfiguration();
                    }
                });
            } else if (component instanceof Container) {
                addValidationListener((Container) component, actionListener);
            }
        }
    }

    private void selectOS(boolean isWindows) {
        appServicePlanPanel.setOSType(getSelectedOperationSystemEnum());
        pack();
    }

    private void init() {
        final String projectName = project.getName();
        final DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
        final String date = df.format(new Date());
        final String defaultWebAppName = String.format("%s-%s", projectName, date);

        txtWebAppName.setText(defaultWebAppName);

        rdoWindowsOS.setSelected(true);
        subscriptionPanel.loadSubscription();
    }

    private void onOK() {
        createFunctionApp();
    }

    private void updateConfiguration() {
        functionConfiguration.setAppName(txtWebAppName.getText());
        functionConfiguration.setSubscription(subscriptionPanel.getSubscriptionId());
        // resource group
        functionConfiguration.setResourceGroup(resourceGroupPanel.getResourceGroupName());
        // app service plan
        if (appServicePlanPanel.isNewAppServicePlan()) {
            functionConfiguration.setAppServicePlanName(appServicePlanPanel.getAppServicePlanName());
            functionConfiguration.setRegion(appServicePlanPanel.getAppServicePlanRegion());
            functionConfiguration.setPricingTier(appServicePlanPanel.getAppServicePlanPricingTier());
        } else {
            functionConfiguration.setAppServicePlanName(appServicePlanPanel.getAppServicePlanName());
            functionConfiguration.setAppServicePlanResourceGroup(appServicePlanPanel.getAppServicePlanResourceGroup());
        }
        // runtime
        final IntelliJFunctionRuntimeConfiguration runtimeConfiguration = new IntelliJFunctionRuntimeConfiguration();
        runtimeConfiguration.setOs(getSelectedOperationSystemEnum() == OperatingSystem.WINDOWS ? "windows" : "linux");
        functionConfiguration.setRuntime(runtimeConfiguration);
        functionConfiguration.setAppSettings(appSettingsTable.getAppSettings());
    }

    private void validateConfiguration() {
        updateConfiguration();
        try {
            functionConfiguration.validate();
            lblMessage.setText("");
            buttonOK.setEnabled(true);
        } catch (ConfigurationException e) {
            lblMessage.setText(String.format(WARNING_MESSAGE, e.getMessage()));
            buttonOK.setEnabled(false);
        }
    }

    private void onCancel() {
        dispose();
    }

    private void createFunctionApp() {
        updateConfiguration();
        ProgressManager.getInstance().run(new Task.Modal(null, "Creating New Function App...", true) {
            @Override
            public void run(ProgressIndicator progressIndicator) {
                final Map<String, String> properties = functionConfiguration.getTelemetryProperties(null);
                EventUtil.executeWithLog(WEBAPP, CREATE_WEBAPP, properties, null, (operation) -> {
                    progressIndicator.setIndeterminate(true);
                    EventUtil.logEvent(EventType.info, operation, properties);
                    final CreateFunctionHandler createFunctionHandler = new CreateFunctionHandler(functionConfiguration);
                    createFunctionHandler.execute();
                    result = AuthMethodManager.getInstance().getAzureClient(functionConfiguration.getSubscription()).appServices().functionApps()
                            .getByResourceGroup(functionConfiguration.getResourceGroup(), functionConfiguration.getAppName());
                    ApplicationManager.getApplication().invokeLater(() -> {
                        sendTelemetry(true, null);
                        if (AzureUIRefreshCore.listeners != null) {
                            AzureUIRefreshCore.execute(new AzureUIRefreshEvent(AzureUIRefreshEvent.EventType.REFRESH,
                                    null));
                        }
                    });
                    dispose();
                }, (ex) -> {
                    JOptionPane.showMessageDialog(null, "Create Function Failed : " + ex.getMessage(),
                            "Create Function Failed", JOptionPane.ERROR_MESSAGE);
                    sendTelemetry(false, ex.getMessage());
                });
            }
        });
    }

    private void sendTelemetry(boolean success, @Nullable String errorMsg) {
        final Map<String, String> telemetryMap = new HashMap<>();
        telemetryMap.put("SubscriptionId", functionConfiguration.getSubscription());
        telemetryMap.put("CreateNewApp", String.valueOf(true));
        telemetryMap.put("CreateNewSP", String.valueOf(appServicePlanPanel.isNewAppServicePlan()));
        telemetryMap.put("CreateNewRGP", String.valueOf(resourceGroupPanel.isNewResourceGroup()));
        telemetryMap.put("Success", String.valueOf(success));
        if (!success) {
            telemetryMap.put("ErrorMsg", errorMsg);
        }
        AppInsightsClient.createByType(AppInsightsClient.EventType.Action, "Function", "Deploy", telemetryMap);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        final String localSettingPath = Paths.get(project.getBasePath(), "local.settings.json").toString();
        appSettingsTable = new AppSettingsTable(localSettingPath);
        pnlAppSettings = AppSettingsTableUtils.createAppSettingPanel(appSettingsTable);
        appSettingsTable.loadLocalSetting();

        appServicePlanPanel = new AppServicePlanPanel(this);
        resourceGroupPanel = new ResourceGroupPanel(this);
        subscriptionPanel = new SubscriptionPanel(this);
    }
}
