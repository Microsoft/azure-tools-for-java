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

package com.microsoft.intellij.runner.container.pushimage;

import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.project.Project;
import com.microsoft.azuretools.core.mvp.model.container.pojo.PushImageRunModel;
import com.microsoft.azuretools.core.mvp.model.webapp.PrivateRegistryImageSetting;
import com.microsoft.azuretools.telemetry.TelemetryConstants;
import com.microsoft.azuretools.telemetrywrapper.Operation;
import com.microsoft.azuretools.telemetrywrapper.TelemetryManager;
import com.microsoft.intellij.runner.AzureRunProfileState;
import com.microsoft.intellij.runner.RunProcessHandler;
import com.microsoft.intellij.runner.container.utils.Constant;
import com.microsoft.intellij.runner.container.utils.DockerProgressHandler;
import com.microsoft.intellij.runner.container.utils.DockerUtil;
import com.microsoft.intellij.util.MavenRunTaskUtil;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class PushImageRunState extends AzureRunProfileState<String> {
    private final PushImageRunModel dataModel;


    public PushImageRunState(Project project, PushImageRunModel pushImageRunModel) {
        super(project);
        this.dataModel = pushImageRunModel;
    }

    @Override
    protected String getDeployTarget() {
        return "Container Registry";
    }

    @Override
    public String executeSteps(@NotNull RunProcessHandler processHandler,
                               @NotNull Map<String, String> telemetryMap) throws Exception {
        processHandler.setText("Starting job ...  ");
        String basePath = project.getBasePath();
        if (basePath == null) {
            processHandler.println("Project base path is null.", ProcessOutputTypes.STDERR);
            throw new FileNotFoundException("Project base path is null.");
        }
        // locate artifact to specified location
        String targetFilePath = dataModel.getTargetPath();
        processHandler.setText(String.format("Locating artifact ... [%s]", targetFilePath));

        // validate dockerfile
        Path targetDockerfile = Paths.get(dataModel.getDockerFilePath());
        processHandler.setText(String.format("Validating dockerfile ... [%s]", targetDockerfile));
        if (!targetDockerfile.toFile().exists()) {
            throw new FileNotFoundException("Dockerfile not found.");
        }
        // replace placeholder if exists
        String content = new String(Files.readAllBytes(targetDockerfile));
        content = content.replaceAll(Constant.DOCKERFILE_ARTIFACT_PLACEHOLDER,
                Paths.get(basePath).toUri().relativize(Paths.get(targetFilePath).toUri()).getPath()
        );
        Files.write(targetDockerfile, content.getBytes());

        // build image
        PrivateRegistryImageSetting acrInfo = dataModel.getPrivateRegistryImageSetting();
        processHandler.setText(String.format("Building image ...  [%s]", acrInfo.getImageTagWithServerUrl()));
        DockerClient docker = DefaultDockerClient.fromEnv().build();
        String image = DockerUtil.buildImage(docker,
                acrInfo.getImageTagWithServerUrl(),
                targetDockerfile.getParent(),
                targetDockerfile.getFileName().toString(),
                new DockerProgressHandler(processHandler)
        );

        // push to ACR
        processHandler.setText(String.format("Pushing to ACR ... [%s] ", acrInfo.getServerUrl()));
        DockerUtil.pushImage(docker, acrInfo.getServerUrl(), acrInfo.getUsername(), acrInfo.getPassword(),
                acrInfo.getImageTagWithServerUrl(),
                new DockerProgressHandler(processHandler)
        );

        return image;
    }

    @Override
    protected Operation createOperation() {
        return TelemetryManager.createOperation(TelemetryConstants.ACR, TelemetryConstants.ACR_PUSHIMAGE);
    }


    @Override
    protected void onSuccess(String image, @NotNull RunProcessHandler processHandler) {
        processHandler.setText("pushed.");
        processHandler.notifyComplete();
    }

    @Override
    protected void onFail(@NotNull String errMsg, @NotNull RunProcessHandler processHandler) {
        processHandler.println(errMsg, ProcessOutputTypes.STDERR);
        processHandler.notifyComplete();
    }

    @Override
    protected void updateTelemetryMap(@NotNull Map<String, String> telemetryMap) {
        String fileName = dataModel.getTargetName();
        if (null != fileName) {
            telemetryMap.put("FileType", MavenRunTaskUtil.getFileType(fileName));
        } else {
            telemetryMap.put("FileType", "");
        }
    }
}
