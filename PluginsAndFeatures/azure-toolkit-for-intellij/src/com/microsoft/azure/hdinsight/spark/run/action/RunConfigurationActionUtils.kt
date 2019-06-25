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

package com.microsoft.azure.hdinsight.spark.run.action

import com.intellij.execution.ExecutionException
import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.configuration.AbstractRunConfiguration
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunnerSettings
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.execution.configurations.RuntimeConfigurationException
import com.intellij.execution.impl.RunDialog
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.openapi.ui.Messages
import com.microsoft.azure.hdinsight.common.logger.ILogger
import com.microsoft.azure.hdinsight.spark.run.SparkBatchJobRunner
import com.microsoft.azuretools.telemetrywrapper.ErrorType
import com.microsoft.azuretools.telemetrywrapper.EventUtil
import com.microsoft.azuretools.telemetrywrapper.Operation

object RunConfigurationActionUtils: ILogger {
    fun runEnvironmentProfileWithCheckSettings(environment: ExecutionEnvironment, operation: Operation) {
        val runner = ProgramRunner.getRunner(environment.executor.id, environment.runProfile) ?: return
        val setting = environment.runnerAndConfigurationSettings ?: return

        try {
            if (setting.isEditBeforeRun && !RunDialog.editConfiguration(environment, "Edit configuration")) {
                EventUtil.logError(operation, ErrorType.userError, ExecutionException("run config dialog closed"), null, null)
                operation.complete()
                return
            }

            var configError = getRunConfigurationError(environment.runProfile, runner)
            while (configError != null) {
                EventUtil.logError(operation, ErrorType.userError, ExecutionException(configError), null, null)

                if (Messages.YES == Messages.showYesNoDialog(
                                environment.project,
                                "Configuration is incorrect: $configError. Do you want to edit it?",
                                "Change Configuration Settings",
                                "Edit",
                                "Continue Anyway",
                                Messages.getErrorIcon())) {
                    if (!RunDialog.editConfiguration(environment, "Edit configuration")) {
                        EventUtil.logError(operation, ErrorType.userError, ExecutionException("run config dialog closed"), null, null)
                        operation.complete()
                        return
                    }
                } else {
                    break
                }

                configError = getRunConfigurationError(environment.runProfile, runner)
            }

            environment.assignNewExecutionId()

            if (runner is SparkBatchJobRunner) {
                runner.operation = operation
            }
            runner.execute(environment)
        } catch (e: ExecutionException) {
            EventUtil.logError(operation, ErrorType.userError, e, null, null)
            operation.complete()

            ProgramRunnerUtil.handleExecutionError(environment.project, environment, e, setting.configuration)
        }
    }

    fun checkRunnerSettings(runProfile: RunProfile?, runner: ProgramRunner<RunnerSettings>) {
        (runProfile as? AbstractRunConfiguration)?.checkRunnerSettings(runner, null, null)
    }

    fun getRunConfigurationError(runProfile: RunProfile?, runner: ProgramRunner<RunnerSettings>): String? {
        try {
            checkRunnerSettings(runProfile, runner)
        } catch (err: RuntimeConfigurationError) {
            return err.message
        } catch (ignored: RuntimeConfigurationException) {
        }

        return null
    }
}