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

package com.microsoft.intellij.serviceexplorer.azure.appservice;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.microsoft.azure.management.appservice.OperatingSystem;
import com.microsoft.azure.management.appservice.WebApp;
import com.microsoft.azure.management.appservice.WebAppBase;
import com.microsoft.azure.toolkit.intellij.appservice.jfr.RunFlightRecorderDialog;
import com.microsoft.azure.toolkit.lib.appservice.jfr.FlightRecorderConfiguration;
import com.microsoft.azure.toolkit.lib.appservice.jfr.FlightRecorderManager;
import com.microsoft.azure.toolkit.lib.appservice.jfr.FlightRecorderStarterBase;
import com.microsoft.azure.toolkit.lib.common.task.AzureTask;
import com.microsoft.azure.toolkit.lib.common.task.AzureTaskManager;
import com.microsoft.azuretools.telemetry.TelemetryConstants;
import com.microsoft.azuretools.telemetrywrapper.EventUtil;
import com.microsoft.intellij.util.PluginUtil;
import com.microsoft.tooling.msservices.helpers.Name;
import com.microsoft.tooling.msservices.serviceexplorer.NodeActionEvent;
import com.microsoft.tooling.msservices.serviceexplorer.NodeActionListener;
import com.microsoft.tooling.msservices.serviceexplorer.azure.webapp.WebAppNode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import static com.microsoft.intellij.ui.messages.AzureBundle.message;

@Name("Profile Flight Recorder")
public class ProfileFlightRecordAction extends NodeActionListener {
    private static final Logger logger = Logger.getLogger(ProfileFlightRecordAction.class.getName());
    private static final String PROFILE_FLIGHT_RECORDER = "Profile Flight Recorder";
    private static final int ONE_SECOND = 1000;
    private static final int TWO_SECONDS = 2000;
    private final Project project;
    private final WebAppBase appService;

    public ProfileFlightRecordAction(WebAppNode webAppNode) {
        super();
        this.project = (Project) webAppNode.getProject();
        appService = webAppNode.getWebapp();
    }

    @Override
    protected void actionPerformed(NodeActionEvent nodeActionEvent) {
        // prerequisite check
        if (appService.operatingSystem() == OperatingSystem.LINUX &&
                StringUtils.containsIgnoreCase(appService.linuxFxVersion(), "DOCKER|")) {
            notifyUserWithErrorMessage(message("webapp.flightRecord.error.notSupport.title"),
                                       String.format(message("webapp.flightRecord.error.notSupport.message"), appService.name()));
            return;
        }
        if (!StringUtils.equalsIgnoreCase(appService.state(), "running")) {
            notifyUserWithErrorMessage(message("webapp.flightRecord.error.notRunning.title"),
                                       String.format(message("webapp.flightRecord.error.notRunning.message"), appService.name()));
            return;
        }
        EventUtil.executeWithLog(appService instanceof WebApp ? TelemetryConstants.WEBAPP : TelemetryConstants.FUNCTION,
                                 "start-flight-recorder", op -> {
                final AzureTask task = new AzureTask(project, PROFILE_FLIGHT_RECORDER, true, this::doProfileFlightRecorderAll, AzureTask.Modality.ANY);
                AzureTaskManager.getInstance().runInBackground(task);
            });
    }

    private void doProfileFlightRecorderAll() {
        try {
            ProgressIndicator progressIndicator = ProgressManager.getInstance().getProgressIndicator();
            progressIndicator.setText(String.format(message("webapp.flightRecord.task.startProfileWebApp.title"), appService.name()));
            CountDownLatch finishLatch = new CountDownLatch(1);
            AzureTaskManager.getInstance().runAndWait(() -> {
                FlightRecorderConfiguration config = collectFlightRecorderConfiguration();
                if (Objects.isNull(config)) {
                    PluginUtil.showWarningNotificationProject(project,
                                                              message("webapp.flightRecord.error.cancelled.title"),
                                                              message("webapp.flightRecord.error.cancelled.message"));
                    finishLatch.countDown();
                    return;
                }
                if (config.getDuration() <= 0) {
                    notifyUserWithErrorMessage(message("webapp.flightRecord.error.invalidDuration.title"),
                                               message("webapp.flightRecord.error.invalidDuration.message"));
                    finishLatch.countDown();
                } else {
                    new Thread(() -> {
                        doProfileFlightRecorder(progressIndicator,
                                                config, finishLatch);
                    }).start();
                }
            }, AzureTask.Modality.NONE);
            finishLatch.await();
        } catch (Exception ex) {
            notifyUserWithErrorMessage(
                    message("webapp.flightRecord.error.profileFailed.title"), message("webapp.flightRecord.error.profileFailed.message") + ex.getMessage());
        }
    }

    private FlightRecorderConfiguration collectFlightRecorderConfiguration() {
        RunFlightRecorderDialog ui = new RunFlightRecorderDialog(project, appService);
        ui.setTitle(String.format(message("webapp.flightRecord.task.startRecorder.title"), appService.defaultHostName()));
        ui.setOkActionListener((config) -> {
            ui.close(DialogWrapper.OK_EXIT_CODE);
        });

        if (ui.showAndGet()) {
            return ui.getData();
        }
        return null;
    }

    private void doProfileFlightRecorder(ProgressIndicator progressIndicator,
                                         FlightRecorderConfiguration config, CountDownLatch latch) {
        try {
            progressIndicator.setText(String.format(message("webapp.flightRecord.task.startProcessRecorder.title"), config.getPid(), config.getProcessName()));
            File file = File.createTempFile("jfr-snapshot-" + appService.name() + "-", ".jfr");
            FileUtils.forceDeleteOnExit(file);
            FlightRecorderStarterBase starter = FlightRecorderManager.getFlightRecorderStarter(appService);
            starter.startFlightRecorder(config.getPid(), config.getDuration(), file.getName());
            progressIndicator.setText(String.format(message("webapp.flightRecord.hint.recording"), config.getDuration()));

            progressIndicator.checkCanceled();
            try {
                Thread.sleep(ONE_SECOND * config.getDuration() + TWO_SECONDS);
            } catch (InterruptedException e) {
                // ignore
            }
            progressIndicator.checkCanceled();
            progressIndicator.setText(message("webapp.flightRecord.hint.profileCompletedOnAzure"));
            progressIndicator.setText(message("webapp.flightRecord.hint.downloadingJfr"));
            byte[] content = starter.downloadJFRFile(file.getName());
            if (content != null) {
                FileUtils.writeByteArrayToFile(file, content);
                progressIndicator.setText(message("webapp.flightRecord.hint.downloadingJfrDone"));
                PluginUtil.showInfoNotificationProject(project,
                                                       message("webapp.flightRecord.hint.profileRecorderComplete"),
                                                       getActionOnJfrFile(file.getAbsolutePath()));
            } else {
                notifyUserWithErrorMessage(message("webapp.flightRecord.error.jfrDownload.title"), message("webapp.flightRecord.error.jfrDownload.message"));
            }

        } catch (IOException e) {
            notifyUserWithErrorMessage(message("webapp.flightRecord.error.profileFlightRecorderFailed.title"),
                                       message("webapp.flightRecord.error.profileFlightRecorderFailed.message") + e.getMessage());
        } finally {
            latch.countDown();
        }
    }

    private void notifyUserWithErrorMessage(String title, String errorMessage) {
        PluginUtil.showErrorNotificationProject(
                project, title,
                errorMessage);
    }

    private String getActionOnJfrFile(String filePath) {
        if (PluginUtil.isIdeaUltimate()) {
            return String.format(message("webapp.flightRecord.hint.openJfrIntelliJ"), filePath);
        } else {
            return String.format(message("webapp.flightRecord.hint.openJfrZuluMissionControl"), filePath);
        }
    }
}
