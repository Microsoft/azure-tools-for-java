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
 *
 */

package com.microsoft.azure.hdinsight.spark.run;

import com.intellij.debugger.impl.GenericDebuggerRunner;
import com.intellij.debugger.impl.GenericDebuggerRunnerSettings;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationInfoProvider;
import com.intellij.execution.configurations.RemoteConnection;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.jcraft.jsch.JSchException;
import com.microsoft.azure.hdinsight.common.HDInsightUtil;
import com.microsoft.azure.hdinsight.sdk.cluster.IClusterDetail;
import com.microsoft.azure.hdinsight.spark.common.*;
import com.microsoft.azure.hdinsight.spark.run.configuration.RemoteDebugRunConfiguration;
import com.microsoft.azuretools.telemetry.AppInsightsClient;
import com.microsoft.intellij.hdinsight.messages.HDInsightBundle;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rx.Single;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SparkBatchJobDebuggerRunner extends GenericDebuggerRunner {
    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        // Only support debug now, will enable run in future
        return DefaultDebugExecutor.EXECUTOR_ID.equals(executorId) && profile instanceof RemoteDebugRunConfiguration;
    }

    @NotNull
    @Override
    public String getRunnerId() {
        return "SparkBatchJobDebug";
    }

    @Override
    public GenericDebuggerRunnerSettings createConfigurationData(ConfigurationInfoProvider settingsProvider) {
        return null;
    }

    @Override
    protected void execute(@NotNull ExecutionEnvironment environment, @Nullable Callback callback, @NotNull RunProfileState state) throws ExecutionException {
        SparkBatchJobSubmissionState submissionState = (SparkBatchJobSubmissionState) state;
        SparkSubmitModel submitModel = submissionState.getSubmitModel();
        SparkSubmissionParameter submissionParameter = submitModel.getSubmissionParameter();
        IClusterDetail clusterDetail = submitModel.getSelectedClusterDetail();
        Map<String, String> postEventProperty = new HashMap<>();

        submitModel
                .buildArtifactObservable(submissionParameter.getArtifactName())
                .flatMap((artifact) -> submitModel.deployArtifactObservable(artifact, clusterDetail)
                                                  .subscribeOn(Schedulers.io()))
                .map((selectedClusterDetail) -> {
                    // Create Batch Spark Debug Job
                    try {
                        return submitModel.tryToCreateBatchSparkDebugJob(selectedClusterDetail);
                    } catch (Exception e) {
                        HDInsightUtil.setJobRunningStatus(submitModel.getProject(), false);
                        throw Exceptions.propagate(e);
                    }
                })
                .flatMap((remoteDebugJob) ->
                    startDebuggerObservable(environment, callback, submissionState, remoteDebugJob)
                            .subscribeOn(Schedulers.computation())
                            .zipWith( // Block with getting the job log from cluster
                                    submitModel.jobLogObservable(
                                            remoteDebugJob.getBatchId(), clusterDetail)
                                                    .subscribeOn(Schedulers.computation()),
                                    (session, ignore) -> session)
                            .doOnError(err -> {
                                try {
                                    HDInsightUtil.showErrorMessageOnSubmissionMessageWindow(
                                            submitModel.getProject(),
                                            "Error : Spark batch debugging job is killed, got exception " + err);

                                    remoteDebugJob.killBatchJob();
                                    HDInsightUtil.setJobRunningStatus(submitModel.getProject(), false);
                                } catch (IOException ignore) { }
                            }))
                .subscribe(
                        sparkBatchDebugSession -> {
                            // Spark Job is done
                            HDInsightUtil.showInfoOnSubmissionMessageWindow(
                                    submitModel.getProject(),
                                    "Info : Debugging Spark batch job in cluster is done.");

                            sparkBatchDebugSession.close();

                            HDInsightUtil.setJobRunningStatus(submitModel.getProject(), false);

                            postEventProperty.put("IsSubmitSucceed", "true");
                            AppInsightsClient.create(
                                    HDInsightBundle.message("SparkRunConfigDebugButtonClick"), null,
                                    postEventProperty);
                        },
                        (throwable) -> {
                            // set the running flag to false
                            HDInsightUtil.setJobRunningStatus(submitModel.getProject(), false);

                            String errorMessage;

                            if (throwable instanceof CompositeException) {
                                CompositeException exceptions = (CompositeException) throwable;

                                errorMessage = exceptions.getExceptions().stream()
                                        .map(Throwable::getMessage)
                                        .collect(Collectors.joining("; "));
                            } else {
                                errorMessage = throwable.getMessage();
                            }

                            HDInsightUtil.showErrorMessageOnSubmissionMessageWindow(
                                    submitModel.getProject(),
                                    "Error : Spark batch Job remote debug failed, got exception: " + errorMessage);

                            postEventProperty.put("IsSubmitSucceed", "false");
                            postEventProperty.put("SubmitFailedReason", errorMessage.substring(0, 50));
                            AppInsightsClient.create(
                                    HDInsightBundle.message("SparkRunConfigDebugButtonClick"),
                                    null,
                                    postEventProperty);
                        });
    }

    /**
     * Get SSH Host from the HDInsight connection URL
     *
     * @param connectionUrl the HDInsight connection URL, such as: https://spkdbg.azurehdinsight.net/batch
     * @return SSH host
     * @throws URISyntaxException connection URL is invalid
     */
    protected String getSshHost(String connectionUrl) throws URISyntaxException {
        URI connectUri = new URI(connectionUrl);
        String segs[] = connectUri.getHost().split("\\.");
        segs[0] = segs[0].concat("-ssh");
        return StringUtils.join(segs, ".");
    }

    /**
     * Create SSH port forwarding session for debugging
     *
     * @param connectionUrl the HDInsight connection URL, such as: https://spkdbg.azurehdinsight.net/batch
     * @param submitModel the Spark submit model with advanced setting
     * @param remoteDebugJob the remote Spark job which is listening a port for debugging
     * @return Spark batch debug session and local forwarded port pair
     * @throws URISyntaxException connection URL is invalid
     * @throws JSchException SSH connection exception
     * @throws IOException networking exception
     * @throws SparkSubmitAdvancedConfigModel.UnknownSSHAuthTypeException invalid SSH authentication type
     */
    protected SimpleEntry<SparkBatchDebugSession, Integer> createSshPortForwardDebugSession (
            String connectionUrl,
            SparkSubmitModel submitModel,
            SparkBatchRemoteDebugJob remoteDebugJob
    )
            throws URISyntaxException, JSchException, IOException, SparkJobException {
        SparkSubmitAdvancedConfigModel advModel = submitModel.getAdvancedConfigModel();

        if (advModel == null) {
            throw new SparkSubmitAdvancedConfigModel.NotAdvancedConfig("SSH authentication not set");
        }

        String sshServer = getSshHost(connectionUrl);
        SparkBatchDebugSession session = SparkBatchDebugSession.factory(sshServer, advModel.sshUserName);
        String driverHost = remoteDebugJob.getSparkDriverHost();
        int driverDebugPort = remoteDebugJob.getSparkDriverDebuggingPort();

        HDInsightUtil.showInfoOnSubmissionMessageWindow(
                submitModel.getProject(),
                String.format("Info : Remote Spark batch job is listening on %s:%d",
                              driverHost, driverDebugPort));

        switch (advModel.sshAuthType) {
            case UseKeyFile:
                session.setPrivateKeyFile(advModel.sshKyeFile);
                break;
            case UsePassword:
                session.setPassword(advModel.sshPassword);
                break;
            default:
                throw new SparkSubmitAdvancedConfigModel.UnknownSSHAuthTypeException(
                        "Unknown SSH authentication type: " + advModel.sshAuthType.name());
        }

        session.open().forwardToRemotePort(driverHost, driverDebugPort);

        int localPort = session.getForwardedLocalPort(driverHost, driverDebugPort);

        HDInsightUtil.showInfoOnSubmissionMessageWindow(
                submitModel.getProject(),
                String.format("Info : Local port %d is forwarded to %s:%d for Spark job driver debugging",
                        localPort, driverHost, driverDebugPort));

        return new SimpleEntry<>(session, localPort);
    }

    /**
     * Start Spark batch job remote debugging
     *
     * @param environment ID of the {@link Executor} with which the user is trying to run the configuration.
     * @param callback callback when debugger is prepared
     * @param submissionState the submission state from run configuration
     * @param remoteDebugJob the remote Spark job which is listening a port for debugging
     * @return a single Observable with SparkBatchDebugSession instance which is done
     */
    protected Single<SparkBatchDebugSession> startDebuggerObservable(
            @NotNull ExecutionEnvironment environment,
            @Nullable Callback callback,
            @NotNull SparkBatchJobSubmissionState submissionState,
            @NotNull SparkBatchRemoteDebugJob remoteDebugJob) {
        SparkSubmitModel submitModel = submissionState.getSubmitModel();
        IClusterDetail clusterDetail = submitModel.getSelectedClusterDetail();

        return Single.fromEmitter(em -> {
            try {
                // Create SSH port forwarding session for debugging
                SimpleEntry<SparkBatchDebugSession, Integer> sessionPortPair =
                        createSshPortForwardDebugSession(
                                clusterDetail.getConnectionUrl(), submitModel, remoteDebugJob);

                // Set the debug connection to localhost and local forwarded port to the state
                submissionState.setRemoteConnection(new RemoteConnection(
                        true,
                        "localhost",
                        Integer.toString(sessionPortPair.getValue()),
                        false));

                // Execute with attaching to JVM through local forwarded port
                super.execute(environment, callback, submissionState);

                em.onSuccess(sessionPortPair.getKey());
            } catch (Exception ex) {
                em.onError(ex);
            }
        });
    }
}
