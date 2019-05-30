package com.microsoft.intellij.serviceexplorer.azure.arm;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.microsoft.azuretools.authmanage.AuthMethodManager;
import com.microsoft.azuretools.ijidea.actions.AzureSignInAction;
import com.microsoft.intellij.AzurePlugin;
import com.microsoft.intellij.forms.arm.UpdateDeploymentForm;
import com.microsoft.intellij.ui.util.UIUtils;
import com.microsoft.tooling.msservices.components.DefaultLoader;
import com.microsoft.tooling.msservices.helpers.Name;
import com.microsoft.tooling.msservices.serviceexplorer.NodeActionEvent;
import com.microsoft.tooling.msservices.serviceexplorer.NodeActionListener;
import com.microsoft.tooling.msservices.serviceexplorer.azure.arm.deployments.DeploymentNode;

@Name("Update Deployment")
public class UpdateDeploymentAction extends NodeActionListener {

    private final DeploymentNode deploymentNode;
    public static final String NOTIFY_UPDATE_DEPLOYMENT_SUCCESS = "Update deployment successfully";
    public static final String NOTIFY_UPDATE_DEPLOYMENT_FAIL = "Update deployment failed";

    public UpdateDeploymentAction(DeploymentNode deploymentNode) {
        this.deploymentNode = deploymentNode;
    }

    @Override
    protected void actionPerformed(NodeActionEvent nodeActionEvent) {
        Project project = (Project) deploymentNode.getProject();
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        try {
            if (AzureSignInAction.doSignIn(AuthMethodManager.getInstance(), project)) {
                UpdateDeploymentForm updateDeploymentForm = new UpdateDeploymentForm(project, deploymentNode);
                updateDeploymentForm.show();
            }
        } catch (Exception ex) {
            AzurePlugin.log("Error Update Deployment", ex);
            UIUtils.showNotification(statusBar, NOTIFY_UPDATE_DEPLOYMENT_FAIL + ", " + ex.getMessage(),
                MessageType.ERROR);
        }
    }
}
