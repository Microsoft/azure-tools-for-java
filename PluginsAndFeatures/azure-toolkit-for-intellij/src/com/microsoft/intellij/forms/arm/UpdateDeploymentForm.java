package com.microsoft.intellij.forms.arm;

import com.intellij.icons.AllIcons;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.HyperlinkLabel;
import com.microsoft.azure.management.resources.DeploymentMode;
import com.microsoft.azure.management.resources.Subscription;
import com.microsoft.azuretools.utils.AzureModel;
import com.microsoft.intellij.ui.util.UIUtils;
import com.microsoft.tooling.msservices.components.DefaultLoader;
import com.microsoft.tooling.msservices.serviceexplorer.azure.arm.deployments.DeploymentNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UpdateDeploymentForm extends DeploymentBaseForm {

    private JPanel contentPane;
    private JLabel subsNameLabel;
    private JLabel rgNameLabel;
    private JLabel deploymentNameLabel;
    private JLabel lblTemplateHover;
    private Project project;
    private final DeploymentNode deploymentNode;
    private JRadioButton templateFileRadioButton;
    private JRadioButton templateURLRadioButton;
    private JTextField templateURLTextField;
    private TextFieldWithBrowseButton templateTextField;
    private HyperlinkLabel templateURLLabel;

    public UpdateDeploymentForm(Project project, DeploymentNode deploymentNode) {
        super(project, false);
        setModal(true);
        setTitle("Update Resource Template");
        this.project = project;
        this.deploymentNode = deploymentNode;
        initTemplateComponent();
        fill();
        init();
    }

    @Override
    protected void doOKAction() {
        String deploymentName = deploymentNode.getDeployment().name();
        ProgressManager.getInstance().run(new Task.Backgroundable(project,
            "Update your azure resource " + deploymentName + "...", false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                try {
                    if (templateFileRadioButton.isSelected()) {
                        String fileText = templateTextField.getText();
                        String content = IOUtils.toString(new FileReader(fileText));
                        deploymentNode.getDeployment().update().
                            withTemplate(content)
                            .withParameters("{}")
                            .withMode(DeploymentMode.INCREMENTAL).apply();
                    } else {
                        deploymentNode.getDeployment().update().
                            withTemplateLink(templateURLTextField.getText(), "1.0.0.0")
                            .withParameters("{}")
                            .withMode(DeploymentMode.INCREMENTAL).apply();
                    }
                } catch (Exception e) {
                    DefaultLoader.getIdeHelper().invokeAndWait(() -> DefaultLoader.getUIHelper().
                        showException("Deploy Azure resource Failed", e, "Deploy Azure resource Failed", false, true));
                }
            }
        });
        close(DialogWrapper.OK_EXIT_CODE, true);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }

    private void fill() {
        Map<String, Subscription> sidMap = AzureModel.getInstance().getSidToSubscriptionMap();
        if (sidMap.containsKey(deploymentNode.getSubscriptionId())) {
            subsNameLabel.setText(sidMap.get(deploymentNode.getSubscriptionId()).displayName());
        }
        rgNameLabel.setText(deploymentNode.getDeployment().resourceGroupName());
        deploymentNameLabel.setText(deploymentNode.getDeployment().name());
    }

    protected void initTemplateComponent() {
        final ButtonGroup templateGroup = new ButtonGroup();
        templateGroup.add(templateFileRadioButton);
        templateGroup.add(templateURLRadioButton);
        templateFileRadioButton.setSelected(true);
        templateFileRadioButton.addItemListener((e) -> radioTemplateLogic());
        templateURLRadioButton.addItemListener((e) -> radioTemplateLogic());

        templateTextField.addActionListener(
            UIUtils.createFileChooserListener(templateTextField, project,
                FileChooserDescriptorFactory.createSingleLocalFileDescriptor()));
        templateURLLabel.setHyperlinkText("Browse for samples");
        templateURLLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BrowserUtil.browse(TEMPLATE_URL);
            }
        });

        radioTemplateLogic();
    }

    private void radioTemplateLogic() {
        boolean isFile = templateFileRadioButton.isSelected();
        templateTextField.setVisible(isFile);
        templateURLTextField.setVisible(!isFile);
        templateURLLabel.setVisible(!isFile);
        pack();
    }

    private void createUIComponents() {
        lblTemplateHover = new JLabel(AllIcons.General.Information);
        lblTemplateHover.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BrowserUtil.browse(ARM_DOC);
            }
        });
    }

}
