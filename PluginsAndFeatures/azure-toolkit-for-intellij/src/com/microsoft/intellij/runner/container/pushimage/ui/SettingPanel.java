/*
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

package com.microsoft.intellij.runner.container.pushimage.ui;

import icons.MavenIcons;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.packaging.artifacts.Artifact;
import com.intellij.packaging.impl.run.BuildArtifactsBeforeRunTaskProvider;
import com.intellij.ui.ListCellRendererWrapper;
import com.microsoft.azuretools.azurecommons.util.Utils;
import com.microsoft.azuretools.core.mvp.model.webapp.PrivateRegistryImageSetting;
import com.microsoft.azuretools.telemetry.AppInsightsClient;
import com.microsoft.intellij.runner.container.common.ContainerSettingPanel;
import com.microsoft.intellij.runner.container.pushimage.PushImageRunConfiguration;
import com.microsoft.intellij.runner.container.utils.DockerUtil;
import com.microsoft.intellij.util.MavenRunTaskUtil;

import org.jetbrains.idea.maven.model.MavenConstants;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.schedulers.Schedulers;

public class SettingPanel {
    private final Project project;
    private JPanel rootPanel;
    private JComboBox<Artifact> cbArtifact;
    private JLabel lblArtifact;
    private JPanel pnlArtifact;
    private ContainerSettingPanel containerSettingPanel;
    private JPanel pnlMavenProject;
    private JLabel lblMavenProject;
    private JComboBox cbMavenProject;
    private Artifact lastSelectedArtifact;
    private boolean isCbArtifactInited;

    private boolean telemetrySent;

    /**
     * Constructor.
     */

    public SettingPanel(Project project) {
        this.project = project;
        $$$setupUI$$$(); // tell IntelliJ to call createUIComponents() here.
        // Artifact to build
        isCbArtifactInited = false;
        cbArtifact.addActionListener(e -> {
            final Artifact selectedArtifact = (Artifact) cbArtifact.getSelectedItem();
            if (!Comparing.equal(lastSelectedArtifact, selectedArtifact)) {
                if (isCbArtifactInited) {
                    if (lastSelectedArtifact != null) {
                        BuildArtifactsBeforeRunTaskProvider
                                .setBuildArtifactBeforeRunOption(rootPanel, project, lastSelectedArtifact, false);
                    }
                    if (selectedArtifact != null) {
                        BuildArtifactsBeforeRunTaskProvider
                                .setBuildArtifactBeforeRunOption(rootPanel, project, selectedArtifact, true);
                    }
                }
                lastSelectedArtifact = selectedArtifact;
            }
        });

        cbArtifact.setRenderer(new ListCellRendererWrapper<Artifact>() {
            @Override
            public void customize(JList jlist, Artifact artifact, int i, boolean b, boolean b1) {
                if (artifact != null) {
                    setIcon(artifact.getArtifactType().getIcon());
                    setText(artifact.getName());
                }
            }
        });

        cbMavenProject.addActionListener(e -> {
            MavenProject selectedMavenProject = (MavenProject) cbMavenProject.getSelectedItem();
            if (selectedMavenProject != null) {
                containerSettingPanel.setDockerPath(
                        DockerUtil.getDefaultDockerFilePathIfExist(selectedMavenProject.getDirectory())
                );
            }
        });

        cbMavenProject.setRenderer(new ListCellRendererWrapper<MavenProject>() {
            @Override
            public void customize(JList jList, MavenProject mavenProject, int i, boolean b, boolean b1) {
                if (mavenProject != null) {
                    setIcon(MavenIcons.MavenProject);
                    setText(mavenProject.toString());
                }
            }
        });

        telemetrySent = false;
    }

    // TODO: refactor later
    private void sendTelemetry(String targetName) {
        if (telemetrySent) {
            return;
        }
        Observable.fromCallable(() -> {
            Map<String, String> map = new HashMap<>();
            String fileType = "";
            if (targetName != null) {
                fileType = MavenRunTaskUtil.getFileType(targetName);
            }
            map.put("FileType", fileType);
            AppInsightsClient.createByType(AppInsightsClient.EventType.Dialog, "Push Image",
                    "Open", map);
            return true;
        }).subscribeOn(Schedulers.io()).subscribe(
                (res) -> telemetrySent = true,
                (err) -> telemetrySent = true
        );
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    /**
     * Function triggered by any content change events.
     */
    @SuppressWarnings("Duplicates")
    public void apply(PushImageRunConfiguration pushImageRunConfiguration) {
        pushImageRunConfiguration.setDockerFilePath(containerSettingPanel.getDockerPath());
        // set ACR info
        pushImageRunConfiguration.setPrivateRegistryImageSetting(new PrivateRegistryImageSetting(
                containerSettingPanel.getServerUrl().replaceFirst("^https?://", "").replaceFirst("/$", ""),
                containerSettingPanel.getUserName(),
                containerSettingPanel.getPassword(),
                containerSettingPanel.getImageTag(),
                ""
        ));

        // set target
        if (lastSelectedArtifact != null) {
            String targetPath = lastSelectedArtifact.getOutputFilePath();
            pushImageRunConfiguration.setTargetPath(targetPath);
            pushImageRunConfiguration.setTargetName(Paths.get(targetPath).getFileName().toString());
        } else {
            MavenProject mavenProject = (MavenProject) cbMavenProject.getSelectedItem();
            if (mavenProject != null) {
                pushImageRunConfiguration.setTargetPath(MavenRunTaskUtil.getTargetPath(mavenProject));
                pushImageRunConfiguration.setTargetName(MavenRunTaskUtil.getTargetName(mavenProject));
            }
        }
    }

    @SuppressWarnings("Duplicates")
    private void setupArtifactCombo(List<Artifact> artifacts, String targetPath) {
        isCbArtifactInited = false;
        cbArtifact.removeAllItems();
        if (null != artifacts) {
            for (Artifact artifact : artifacts) {
                cbArtifact.addItem(artifact);
                if (null != targetPath && Comparing.equal(artifact.getOutputFilePath(), targetPath)) {
                    cbArtifact.setSelectedItem(artifact);
                }
            }
        }
        cbArtifact.setVisible(true);
        lblArtifact.setVisible(true);
        isCbArtifactInited = true;
    }

    @SuppressWarnings("Duplicates")
    private void setupMavenProjectCombo(List<MavenProject> mvnprjs, String targetPath) {
        cbMavenProject.removeAllItems();
        if (null != mvnprjs) {
            for (MavenProject prj : mvnprjs) {
                cbMavenProject.addItem(prj);
                if (MavenRunTaskUtil.getTargetPath(prj).equals(targetPath)) {
                    cbMavenProject.setSelectedItem(prj);
                }
            }
        }
        cbMavenProject.setVisible(true);
        lblMavenProject.setVisible(true);
    }

    /**
     * Function triggered in constructing the panel.
     *
     * @param conf configuration instance
     */
    public void reset(PushImageRunConfiguration conf) {
        if (!MavenRunTaskUtil.isMavenProject(project)) {
            List<Artifact> artifacts = MavenRunTaskUtil.collectProjectArtifact(project);
            setupArtifactCombo(artifacts, conf.getTargetPath());
        } else {
            List<MavenProject> mavenProjects = MavenProjectsManager.getInstance(project).getProjects();
            setupMavenProjectCombo(mavenProjects, conf.getTargetPath());
        }

        PrivateRegistryImageSetting acrInfo = conf.getPrivateRegistryImageSetting();
        containerSettingPanel.setTxtFields(acrInfo);

        // load dockerFile path from existing configuration.
        if (!Utils.isEmptyString(conf.getDockerFilePath())) {
            containerSettingPanel.setDockerPath(conf.getDockerFilePath());
        }
        containerSettingPanel.onListRegistries();
        sendTelemetry(conf.getTargetName());
    }

    public void disposeEditor() {
        containerSettingPanel.disposeEditor();
    }

    private void createUIComponents() {
        containerSettingPanel = new ContainerSettingPanel(this.project);
    }

    private void $$$setupUI$$$(){}
}
