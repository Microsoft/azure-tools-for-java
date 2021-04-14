/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

package com.microsoft.intellij.ui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.AnimatedIcon;
import com.microsoft.azure.toolkit.intellij.common.AzureDialog;
import com.microsoft.azure.toolkit.lib.Azure;
import com.microsoft.azure.toolkit.lib.auth.Account;
import com.microsoft.azure.toolkit.lib.auth.AzureAccount;
import com.microsoft.azure.toolkit.lib.auth.model.AuthType;
import com.microsoft.azure.toolkit.lib.common.exception.AzureToolkitRuntimeException;
import com.microsoft.azure.toolkit.lib.common.operation.AzureOperationBundle;
import com.microsoft.azure.toolkit.lib.common.operation.IAzureOperationTitle;
import com.microsoft.azure.toolkit.lib.common.task.AzureTask;
import com.microsoft.azure.toolkit.lib.common.task.AzureTaskManager;
import com.microsoft.azuretools.adauth.StringUtils;
import com.microsoft.azuretools.authmanage.AuthMethod;
import com.microsoft.azuretools.authmanage.AuthMethodManager;
import com.microsoft.azuretools.authmanage.CommonSettings;
import com.microsoft.azuretools.authmanage.SubscriptionManager;
import com.microsoft.azuretools.authmanage.models.AuthMethodDetails;
import com.microsoft.azuretools.authmanage.models.SubscriptionDetail;
import com.microsoft.azuretools.sdkmanage.AzureCliAzureManager;
import com.microsoft.azuretools.sdkmanage.IdentityAzureManager;
import com.microsoft.azuretools.telemetrywrapper.*;
import com.microsoft.intellij.ui.components.AzureDialogWrapper;
import com.microsoft.intellij.util.PluginUtil;
import com.microsoft.tooling.msservices.components.DefaultLoader;
import org.jdesktop.swingx.JXHyperlink;
import org.jetbrains.annotations.Nullable;
import reactor.core.scheduler.Schedulers;
import rx.Single;

import javax.swing.*;
import java.net.URI;
import java.util.*;
import java.util.concurrent.Callable;

import static com.microsoft.azuretools.telemetry.TelemetryConstants.*;

public class SignInWindow extends AzureDialogWrapper {
    private static final Logger LOGGER = Logger.getInstance(SignInWindow.class);
    private static final String SIGN_IN_ERROR = "Sign In Error";

    private JPanel contentPane;

    private JRadioButton deviceLoginRadioButton;

    private JRadioButton automatedRadioButton;
    private JLabel authFileLabel;
    private JTextField authFileTextField;
    private JButton browseButton;
    private JButton createNewAuthenticationFileButton;
    private JLabel automatedCommentLabel;
    private JLabel deviceLoginCommentLabel;
    private JRadioButton azureCliRadioButton;
    private JPanel azureCliPanel;
    private JLabel azureCliCommentLabel;

    private AuthMethodDetails authMethodDetails;
    private AuthMethodDetails authMethodDetailsResult;

    private String accountEmail;

    private Project project;
    private AzureDialog.OkActionListener<? super AuthMethodDetails> okActionListener;

    public SignInWindow(AuthMethodDetails authMethodDetails, Project project) {
        super(project, true, IdeModalityType.PROJECT);
        this.project = project;
        setModal(true);
        setTitle("Azure Sign In");
        setOKButtonText("Sign in");

        this.authMethodDetails = authMethodDetails;
        authFileTextField.setText(authMethodDetails == null ? null : authMethodDetails.getCredFilePath());

        automatedRadioButton.addActionListener(e -> refreshAuthControlElements());

        deviceLoginRadioButton.addActionListener(e -> refreshAuthControlElements());

        azureCliRadioButton.addActionListener(e -> refreshAuthControlElements());

        browseButton.addActionListener(e -> doSelectCredFilepath());

        createNewAuthenticationFileButton.addActionListener(e -> doCreateServicePrincipal());

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(deviceLoginRadioButton);
        buttonGroup.add(automatedRadioButton);
        buttonGroup.add(azureCliRadioButton);
        deviceLoginRadioButton.setSelected(true);

        init();


    }

    public AuthMethodDetails getAuthMethodDetails() {
        return authMethodDetailsResult;
    }

    @Nullable
    public static SignInWindow go(AuthMethodDetails authMethodDetails, Project project) {
        SignInWindow signInWindow = new SignInWindow(authMethodDetails, project);
        signInWindow.show();
        if (signInWindow.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
            return signInWindow;
        }

        return null;
    }

    private void checkAccountAvailability() {
        Account cliAccount = Azure.az(AzureAccount.class).accounts().stream().filter(d -> d.getAuthType() == AuthType.AZURE_CLI).findFirst().orElse(null);
        Optional.ofNullable(cliAccount).ifPresent(account -> account.checkAvailable().subscribeOn(Schedulers.boundedElastic()).doOnSuccess(avail -> {
            azureCliCommentLabel.setIcon(null);
            azureCliPanel.setEnabled(true);
            azureCliRadioButton.setText("Azure CLI");
            azureCliRadioButton.setEnabled(true);
            azureCliRadioButton.setSelected(true);
        }).doOnError(e -> {
            azureCliCommentLabel.setIcon(null);
            azureCliPanel.setEnabled(false);
            azureCliRadioButton.setText("Azure CLI (Not logged in)");
            azureCliRadioButton.setEnabled(false);
        }).subscribe());
    }

    @Override
    public void doCancelAction() {
        authMethodDetailsResult = authMethodDetails;
        super.doCancelAction();
    }

    @Override
    public void doHelpAction() {
        final JXHyperlink helpLink = new JXHyperlink();
        helpLink.setURI(URI.create("https://docs.microsoft.com/en-us/azure/azure-toolkit-for-intellij-sign-in-instructions"));
        helpLink.doClick();
    }

    @Nullable
    @Override
    protected String getDimensionServiceKey() {
        return "SignInWindow";
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }

    public Single<AuthMethodDetails> login() {
        final IAzureOperationTitle title = AzureOperationBundle.title("account.sign_in");
        final AzureTask<AuthMethodDetails> task = new AzureTask<>(null, title, false, () -> {
            final ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
            indicator.setIndeterminate(true);
            return this.doLogin();
        });
        return AzureTaskManager.getInstance().runInModalAsObservable(task).toSingle();
    }

    private @Nullable AuthMethodDetails doLogin() {
        authMethodDetailsResult = new AuthMethodDetails();
        if (automatedRadioButton.isSelected()) { // automated
            final Map<String, String> properties = new HashMap<>();
            properties.put(AZURE_ENVIRONMENT, CommonSettings.getEnvironment().getName());
            properties.putAll(signInSPProp);
            EventUtil.logEvent(EventType.info, ACCOUNT, SIGNIN, properties, null);
            final String authPath = authFileTextField.getText();
            if (StringUtils.isNullOrWhiteSpace(authPath)) {
                final String title = "Sign in dialog info";
                final String message = "Select authentication file";
                DefaultLoader.getUIHelper().showMessageDialog(contentPane, message, title, Messages.getInformationIcon());
                return null;
            }

            authMethodDetailsResult.setAuthMethod(AuthMethod.SP);
            // TODO: check field is empty, check file is valid
            authMethodDetailsResult.setCredFilePath(authPath);
        } else if (deviceLoginRadioButton.isSelected()) {
            doDeviceLogin();
            if (StringUtils.isNullOrEmpty(accountEmail)) {
                System.out.println("Canceled by the user.");
                return null;
            }
            authMethodDetailsResult.setAuthMethod(AuthMethod.DC);
            authMethodDetailsResult.setAccountEmail(accountEmail);
            authMethodDetailsResult.setAzureEnv(CommonSettings.getEnvironment().getName());
        } else if (azureCliRadioButton.isSelected()) {
            call(() -> AzureCliAzureManager.getInstance().signIn(), signInAZProp);
            if (AzureCliAzureManager.getInstance().isSignedIn()) {
                authMethodDetailsResult.setAuthMethod(AuthMethod.AZ);
            } else {
                return null;
            }
        }
        return authMethodDetailsResult;
    }

    @Override
    protected void init() {
        super.init();
        azureCliPanel.setEnabled(false);
        azureCliRadioButton.setText("Azure CLI (checking...)");
        azureCliRadioButton.setEnabled(false);
        azureCliCommentLabel.setIcon(new AnimatedIcon.Default());
        refreshAuthControlElements();
    }

    private AuthMethodManager getAuthMethodManager() {
        return AuthMethodManager.getInstance();
    }

    private void refreshAuthControlElements() {
        refreshAutomateLoginElements();
        refreshDeviceLoginElements();
        refreshAzureCliElements();
    }

    private void refreshAutomateLoginElements() {
        automatedCommentLabel.setEnabled(automatedRadioButton.isSelected());
        authFileLabel.setEnabled(automatedRadioButton.isSelected());
        authFileTextField.setEnabled(automatedRadioButton.isSelected());
        browseButton.setEnabled(automatedRadioButton.isSelected());
        createNewAuthenticationFileButton.setEnabled(automatedRadioButton.isSelected());
    }

    private void refreshDeviceLoginElements() {
        deviceLoginCommentLabel.setEnabled(deviceLoginRadioButton.isSelected());
    }

    private void refreshAzureCliElements() {
        azureCliCommentLabel.setEnabled(azureCliRadioButton.isSelected());
    }

    private void doSelectCredFilepath() {
        FileChooserDescriptor fileDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor("azureauth");
        fileDescriptor.setTitle("Select Authentication File");
        final VirtualFile file = FileChooser.chooseFile(
            fileDescriptor,
            this.project,
            LocalFileSystem.getInstance().findFileByPath(System.getProperty("user.home"))
        );
        if (file != null) {
            authFileTextField.setText(file.getPath());
        }
    }

    @Nullable
    private synchronized IdentityAzureManager doDeviceLogin() {
        try {
            IdentityAzureManager dcAuthManager = (IdentityAzureManager) AuthMethodManager.getInstance().getAzureManager();
            if (AuthMethodManager.getInstance().isSignedIn()) {
                doSignOut();
            }
            call(() -> dcAuthManager.signIn(null), signInDCProp);
            accountEmail = dcAuthManager.getCurrentUserId();

            return dcAuthManager;
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorWindow.show(project, ex.getMessage(), SIGN_IN_ERROR);
        }

        return null;
    }

    private void call(Callable loginCallable, Map<String, String> properties) {
        Operation operation = TelemetryManager.createOperation(ACCOUNT, SIGNIN);
        Optional.ofNullable(ProgressManager.getInstance().getProgressIndicator()).ifPresent(indicator -> indicator.setText2("Signing in..."));

        try {
            operation.start();
            operation.trackProperties(properties);
            operation.trackProperty(AZURE_ENVIRONMENT, CommonSettings.getEnvironment().getName());
            loginCallable.call();
        } catch (Exception e) {
            EventUtil.logError(operation, ErrorType.userError, e, properties, null);
            throw new AzureToolkitRuntimeException(e.getMessage(), e);
        } finally {
            operation.complete();
        }
    }

    private void doSignOut() {
        try {
            accountEmail = null;
            // AuthMethod.AD is deprecated.
            AuthMethodManager.getInstance().signOut();
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorWindow.show(project, ex.getMessage(), "Sign Out Error");
        }
    }

    private void doCreateServicePrincipal() {
        IdentityAzureManager dcAuthManager = null;
        try {
            if (getAuthMethodManager().isSignedIn()) {
                getAuthMethodManager().signOut();
            }

            dcAuthManager = doDeviceLogin();
            if (dcAuthManager == null || !dcAuthManager.isSignedIn()) {
                // canceled by the user
                System.out.println(">> Canceled by the user");
                return;
            }

            SubscriptionManager subscriptionManager = dcAuthManager.getSubscriptionManager();

            Optional.ofNullable(ProgressManager.getInstance().getProgressIndicator()).ifPresent(indicator -> indicator.setText2("Loading subscriptions..."));
            subscriptionManager.getSubscriptionDetails();

            SrvPriSettingsDialog d = SrvPriSettingsDialog.go(subscriptionManager.getSubscriptionDetails(), project);
            List<SubscriptionDetail> subscriptionDetailsUpdated;
            String destinationFolder;
            if (d != null) {
                subscriptionDetailsUpdated = d.getSubscriptionDetails();
                destinationFolder = d.getDestinationFolder();
            } else {
                System.out.println(">> Canceled by the user");
                return;
            }

            Map<String, List<String>> tidSidsMap = new HashMap<>();
            for (SubscriptionDetail sd : subscriptionDetailsUpdated) {
                if (sd.isSelected()) {
                    System.out.format(">> %s\n", sd.getSubscriptionName());
                    String tid = sd.getTenantId();
                    List<String> sidList;
                    if (!tidSidsMap.containsKey(tid)) {
                        sidList = new LinkedList<>();
                    } else {
                        sidList = tidSidsMap.get(tid);
                    }
                    sidList.add(sd.getSubscriptionId());
                    tidSidsMap.put(tid, sidList);
                }
            }

            SrvPriCreationStatusDialog d1 = SrvPriCreationStatusDialog
                .go(dcAuthManager, tidSidsMap, destinationFolder, project);
            if (d1 == null) {
                System.out.println(">> Canceled by the user");
                return;
            }

            String path = d1.getSelectedAuthFilePath();
            if (path == null) {
                System.out.println(">> No file was created");
                return;
            }

            authFileTextField.setText(path);
            PluginUtil.displayInfoDialog("Authentication File Created", String.format(
                "Your credentials have been exported to %s, please keep the authentication file safe", path));
        } catch (Exception ex) {
            ex.printStackTrace();
            //LOGGER.error("doCreateServicePrincipal", ex);
            ErrorWindow.show(project, ex.getMessage(), "Get Subscription Error");

        } finally {
            if (dcAuthManager != null) {
                try {
                    System.out.println(">> Signing out...");
                    AuthMethodManager.getInstance().signOut();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
