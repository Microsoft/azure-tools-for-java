/**
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

package com.microsoft.azure.hdinsight.spark.common;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.microsoft.azure.hdinsight.sdk.common.HttpResponse;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class SparkBatchRemoteDebugJobScenario {
    private SparkBatchSubmission submissionMock;
    private Exception caught;
    private HttpResponse responseMock;
    private ArgumentCaptor<SparkSubmissionParameter> submissionParameterArgumentCaptor;
    private WireMockServer httpServerMock;
    private SparkBatchRemoteDebugJob debugJobMock;
    private Logger loggerMock;

    @Before
    public void setUp() throws Throwable {
        submissionMock = mock(SparkBatchSubmission.class);
        when(submissionMock.getBatchSparkJobStatus(anyString(), anyInt())).thenCallRealMethod();
        when(submissionMock.getHttpResponseViaGet(anyString())).thenCallRealMethod();
        when(submissionMock.createBatchSparkJob(anyString(), any())).thenCallRealMethod();

        debugJobMock = mock(SparkBatchRemoteDebugJob.class);
        when(debugJobMock.getSubmission()).thenReturn(submissionMock);

        loggerMock = mock(Logger.class);
        when(debugJobMock.log()).thenReturn(loggerMock);

        caught = null;
        responseMock = mock(HttpResponse.class);
        submissionParameterArgumentCaptor = ArgumentCaptor.forClass(SparkSubmissionParameter.class);

        if (httpServerMock != null) {
            WireMock.reset();
        }
    }

    @After
    public void cleanUp()
    {
        if (httpServerMock != null) {
            httpServerMock.stop();
        }
    }

    @Given("^create batch Spark Job with driver debugging for '(.+)' with following parameters$")
    public void createBatchSparkJobWithDriverDebuggingConfig(
            String connectUrl,
            Map<String, Object> sparkConfig) throws Throwable {
        SparkSubmissionParameter parameter = new SparkSubmissionParameterScenario()
                .mockSparkSubmissionParameterWithJobConf(new HashMap<String, Object>() {{
                    Map<String, Object> cleanedConf = sparkConfig.entrySet().stream()
                            .filter(entry -> !entry.getKey().isEmpty() || !entry.getValue().toString().isEmpty())
                            .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

                    if (!cleanedConf.isEmpty()) { put("conf", new SparkConfigures(cleanedConf)); }
                }});

        ArgumentCaptor<String> submittedUrlCaptured = ArgumentCaptor.forClass(String.class);
        when(submissionMock.createBatchSparkJob(
                        submittedUrlCaptured.capture(),
                        submissionParameterArgumentCaptor.capture()))
                .thenReturn(responseMock);

        caught = null;

        try {
            debugJobMock = SparkBatchRemoteDebugJob.factory(connectUrl, parameter, submissionMock);

            debugJobMock.createBatchSparkJobWithDriverDebugging();
            assertEquals(submittedUrlCaptured.getValue(), connectUrl);
        } catch (Exception e) {
            caught = e;
        }
    }

    @Then("^throw exception '(.+)' with checking type only$")
    public void checkException(String exceptedName) throws Throwable {
        assertNotNull(caught);
        assertEquals(exceptedName, caught.getClass().getName());
    }

    @Then("^throw exception '(.+)' with message '(.*)'$")
    public void checkExceptionWithMessage(String exceptedName, String expectedMessage) throws Throwable {
        assertNotNull(caught);
        assertEquals(exceptedName, caught.getClass().getName());
        assertEquals(expectedMessage, caught.getMessage());
    }

    @Then("^the Spark driver JVM option should be '(.+)'$")
    public void checkSparkDriverJVMOption(String expectedDriverJvmOption) throws Throwable {
        assertNull(caught);

        String submittedDriverJavaOption =
                ((SparkConfigures) submissionParameterArgumentCaptor.getValue().getJobConfig()
                        .getOrDefault("conf", new SparkConfigures()))
                    .get("spark.driver.extraJavaOptions").toString();

        assertEquals(expectedDriverJvmOption, submittedDriverJavaOption);
    }

    @Given("^setup a mock livy service for (.+) request '(.+)' to return '(.+)' with status code (\\d+)$")
    public void mockLivyService(String action, String serviceUrl, String response, int statusCode) throws Throwable {
        URI mockUri = new URI(serviceUrl);
        int mockedPort = mockUri.getPort() == -1 ? 80 : mockUri.getPort();

        if (httpServerMock == null || httpServerMock.port() != mockedPort) {
            httpServerMock = new WireMockServer(
                    wireMockConfig()
                            .bindAddress(mockUri.getHost())
                            .port(mockedPort));
            httpServerMock.start();
        }

        configureFor(mockUri.getHost(), mockedPort);

        stubFor(request(action, urlEqualTo(serviceUrl.substring(mockUri.resolve("/").toString().length() - 1)))
                .willReturn(
                        aResponse()
                        .withStatus(statusCode)
                        .withBody(response)));
    }

    @Then("^getting spark job url '(.+)', batch ID (\\d+)'s application id should be '(.+)'$")
    public void checkGetSparkJobApplicationId(
            String connectUrl,
            int batchId,
            String expectedApplicationId) throws Throwable {
        when(debugJobMock.getSparkJobApplicationId(any(), anyInt())).thenCallRealMethod();

        caught = null;
        try {
            assertEquals(expectedApplicationId, debugJobMock.getSparkJobApplicationId(
                    new URI(connectUrl), batchId));
        } catch (Exception e) {
            caught = e;
            assertEquals(expectedApplicationId, "__exception_got__");
        }
    }

    @Then("^getting spark job url '(.+)', batch ID (\\d+)'s application id, '(.+)' should be got with (\\d+) times retried$")
    public void checkGetSparkJobApplicationIdRetryCount(
            String connectUrl,
            int batchId,
            String getUrl,
            int expectedRetriedCount) throws Throwable {
        when(debugJobMock.getDelaySeconds()).thenReturn(1);
        when(debugJobMock.getRetriesMax()).thenReturn(3);
        when(debugJobMock.getSparkJobApplicationId(any(), anyInt())).thenCallRealMethod();

        try {
            debugJobMock.getSparkJobApplicationId(new URI(connectUrl), batchId);
        } catch (Exception ignore) { }

        verify(expectedRetriedCount, getRequestedFor(urlEqualTo(getUrl)));
    }

    @Then("^getting spark job url '(.+)', batch ID (\\d+)'s driver log URL should be '(.+)'$")
    public void checkGetSparkJobDriverLogUrl(
            String connectUrl,
            int batchId,
            String expectedDriverLogURL) throws Throwable {
        when(debugJobMock.getSparkJobDriverLogUrl(any(), anyInt())).thenCallRealMethod();

        assertEquals(expectedDriverLogURL, debugJobMock.getSparkJobDriverLogUrl(
                new URI(connectUrl), batchId));
    }

    @Then("^Parsing driver HTTP address '(.+)' should get host '(.+)'$")
    public void checkParsingDriverHTTPAddressHost(
            String httpAddress,
            String expectedHost) throws Throwable {
        when(debugJobMock.parseAmHostHttpAddressHost(anyString())).thenCallRealMethod();

        assertEquals(expectedHost, debugJobMock.parseAmHostHttpAddressHost(httpAddress));
    }

    @Then("^Parsing driver HTTP address '(.+)' should be null$")
    public void checkParsingDriverHTTPAddressHostFailure(String httpAddress) throws Throwable {
        assertNull(debugJobMock.parseAmHostHttpAddressHost(httpAddress));
    }

    @Then("^parsing JVM debugging port should be ([-]?\\d+) for the following html:$")
    public void checkParsingJVMDebuggingPort(String expectedPort, List<String> htmls) throws Throwable {
        when(debugJobMock.parseJvmDebuggingPort(anyString())).thenCallRealMethod();

        htmls.forEach((html) -> assertEquals(
                Integer.parseInt(expectedPort), debugJobMock.parseJvmDebuggingPort(html)));
    }

    @Then("^getting Spark driver debugging port from URL '(.+)', batch ID (\\d+) should be (\\d+)$")
    public void checkGetSparkDriverDebuggingPort(
            String connectUrl,
            int batchId,
            int expectedPort) throws Throwable {
        when(debugJobMock.getConnectUri()).thenReturn(new URI(connectUrl));
        when(debugJobMock.getBatchId()).thenReturn(batchId);
        when(debugJobMock.getSparkDriverDebuggingPort()).thenCallRealMethod();
        when(debugJobMock.parseJvmDebuggingPort(anyString())).thenCallRealMethod();
        when(debugJobMock.getSparkJobDriverLogUrl(any(), anyInt())).thenCallRealMethod();

        try {
            assertEquals(expectedPort, debugJobMock.getSparkDriverDebuggingPort());
        } catch (Exception e) {
            caught = e;
            assertEquals(expectedPort, 0);
        }

    }

    @Then("^getting Spark driver host from URL '(.+)', batch ID (\\d+) should be '(.+)'$")
    public void checkGetSparkDriverHost(
            String connectUrl,
            int batchId,
            String expectedHost) throws Throwable {
        when(debugJobMock.getConnectUri()).thenReturn(new URI(connectUrl));
        when(debugJobMock.getBatchId()).thenReturn(batchId);
        when(debugJobMock.getSparkDriverHost()).thenCallRealMethod();
        when(debugJobMock.getSparkJobYarnApplication(any(), anyString())).thenCallRealMethod();
        when(debugJobMock.getSparkJobApplicationId(any(), anyInt())).thenCallRealMethod();
        when(debugJobMock.parseAmHostHttpAddressHost(anyString())).thenCallRealMethod();

        try {
            assertEquals(expectedHost, debugJobMock.getSparkDriverHost());
        } catch (Exception e) {
            caught = e;
            assertEquals(expectedHost, "__exception_got__");
        }
    }
}