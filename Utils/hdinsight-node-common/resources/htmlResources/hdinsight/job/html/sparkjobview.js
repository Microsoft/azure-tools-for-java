"use strict";

// to cache all job related object
var spark;

$(function () {
    initiate();
    commandBinding();
    getBasicInfoFromUrl();
    getJobHistory();
});

function initiate() {
    $('#jobGraphDiv').hide();
    // hide the error messagae tab first
    // $('#myTab li:eq(0)').hide();
    // show the job output tab
    var myTable = $('#myTable');
    myTable.colResizable({liveDrag:true});
    myTable.dragtable();

    var leftDiv = $('#leftDiv');
    var rightDiv = $('#rightDiv');
    leftDiv.resizable();

    myTable.find('li:eq(4) a').tab('show');

    var tableDiv =  $('#tableDIv');
    tableDiv.resizable();
    tableDiv.resize(function(){
        rightDiv.width($('#parent').width()-$('#tableDIv').width());
    });

    if (!String.prototype.format) {
        String.prototype.format = function() {
            var args = arguments;
            return this.replace(/{(\d+)}/g, function(match, number) {
                return typeof args[number] !== 'undefined'
                    ? args[number]
                    : match
                    ;
            });
        };
    }
}

function commandBinding() {
    $('#JobHistoryTbody').on('click', 'tr', function () {
        // clean all generated values
        spark.isJobGraphGenerated = false;
        spark.currentSelectedJobs = null;
        spark.currentSelectedStages = null;
        spark.appId = null;
        spark.attemptId = null;
        spark.applicationName = null;

        $('#summaryTitle').html("Application details");
        $('#basicInformationTitle').html("Basic Application Information");
        d3.selectAll("#stageSummaryTbody tr").remove();
        d3.selectAll("#taskSummaryTbody tr").remove();
        $('#errorMessage').text("");
        $('#jobOutputTextarea').text("");
        $('#livyJobLog').text("");
        $('#sparkDriverLog').text("");
        var rows = $('#JobHistoryTbody').find('tr');
        rows.removeClass('selected-hight');
        $(this).addClass('selected-hight');


        //get Application Id
        spark.appId = $(this).find('td:eq(1)').text();

        // get last attempt
        spark.attemptId = $(this).find('td:eq(4)').text();
        spark.applicationName = $(this).find('td:eq(2)').text();
        $('#jobName').text("Application: " + spark.applicationName);

        if (spark.appId === 'undefined') {
            return;
        }
        // save current Application ID to LocalStorage
        localStorage.setItem('selectedAppID', spark.appId);
        setBasicInformation();
        setAMcontainer();
        setDiagnosticsLog();
        setLivyLog();
        setDebugInfo("end livy log");
        setJobDetail();
        setStoredRDD();
        setStageDetailsWithTaskDetails();
        setExecutorsDetails();
    });

    $('#sparkEventButton').click(function () {
        JobUtils.openSparkEventLog(projectId, typeof appId === 'undefined' ? "" : appId.toString());
    });

    $('#livyLogButton').click(function() {
        JobUtils.openLivyLog(typeof appId === 'undefined' ? "" : appId.toString());
    });

    $("#openSparkUIButton").click(function () {
        var id = typeof appId === 'undefined' ? "" : appId.toString();
        if (id != "") {
            var application = $.grep(applicationList, function (e) {
                return e.id === id;
            });
            if (application != null && application.length == 1) {
                var currentAttemptId = application[0].attempts[0].attemptId;
                if (currentAttemptId != null) {
                    id = id + "/" + currentAttemptId;
                }
            }
        }
        if(sourceType == "intellij") {
            JobUtils.openSparkUIHistory(id);
        } else {
            JobUtils.openSparkUIHistory(clusterName, id);
        }

    });

    $("#openYarnUIButton").click(function () {
        if(sourceType == "intellij"){
            JobUtils.openYarnUIHistory(typeof appId == 'undefined' ? "" : appId.toString());
        } else {
            JobUtils.openYarnUIHistory(clusterName, typeof appId == 'undefined' ? "" : appId.toString());
        }
    });

    $("#refreshButton").click(function () {
        location.reload();
        refreshGetSelectedApplication();
    });
}

function getBasicInfoFromUrl() {

    spark.queriresMap = {};
    var urlinfo = window.location.href;
    var len = urlinfo.length;
    var offset = urlinfo.indexOf("?");

    var additionalInfo = urlinfo.substr(offset + 1, len);
    var infos = additionalInfo.split("&");
    for (var i = 0; i < infos.length; ++i) {
        var strs = infos[i].split("=");
        spark.queriresMap[strs[0]] = strs[1];
    }

    spark.sourceType = spark.queriresMap['sourcetype'] === 'undefined' ? "intellij" : "eclipse";
    spark.clusterName = spark.queriresMap['clusterName'];
    spark.engineType = spark.queriresMap['engineType'];
    spark.queryPort = spark.queriresMap['port'];
    spark.localhost = 'http://localhost:{0}/'.format(spark.queryPort);
}

function getJobHistory() {
    getMessageAsync("/applications/", 'spark', function (s) {
        writeToTable(s);
        refreshGetSelectedApplication();

        // try to click the first application
        $('#JobHistoryTbody').find('tr:eq(0)').click();
    });
}

function refreshGetSelectedApplication() {
    var selectedAppid = localStorage.getItem("selectedAppID");
    if (selectedAppid === 'undefined') {
        return;
    }

    var tableRow = $('#myTable tbody tr').filter(function () {
        return $(this).children('td:eq(1)').text() === selectedAppid;
    }).closest("tr");
    tableRow.click();
}


function getFirstAttempt(attempts) {
    return findElement(attempts, function (a) {
        return typeof a.attemptId === 'undefined' || a.attemptId === 1;
    });
}

function getLastAttempt(attempts) {
    return findElement(attempts, function (a) {
        return typeof a.attemptId === 'undefined' || a.attemptId === attemptId;
    });
}

function setBasicInformation() {
    getMessageAsync("/applications?appId=" + spark.appId, 'spark', function (s) {
        var application = JSON.parse(s);
        $('#startTime').text(formatServerTime(getFirstAttempt(application.attempts).startTime));
        $('#endTime').text(formatServerTime(getLastAttempt(application.attempts).endTime));
    });
}

function setMessageForLable(str) {
    var ss = document.getElementById("demo");
    ss.innerHTML = str;
}

function writeToTable(message) {
    spark.applicationList = JSON.parse(message);
    $('#myTable tbody').html("");
    d3.select("#myTable tbody")
        .selectAll('tr')
        .data(applicationList)
        .enter()
        .append('tr')
        .attr("align","center")
        .attr("class","ui-widget-content")
        .selectAll('td')
        .data(function(d) {
            return appInformationList(d);
        })
        .enter()
        .append('td')
        .attr('class',"ui-widget-content")
        .attr('id',function(d,i) {
            return i;
        })
        .html(function(d, i) {
            return d;
        });
}

function appInformationList(app) {
    var lists = [];
    var status = app.attempts[app.attempts.length - 1].completed;
    lists.push(getTheJobStatusImgLabel(status));
    lists.push(app.id);
    lists.push(app.name);
    lists.push(formatServerTime(app.attempts[0].startTime));
    if(app.attempts.length == 1 && typeof app.attempts[0].attemptId == 'undefined') {
        lists.push(0);
    } else {
        lists.push(app.attempts.length);
    }
    lists.push(app.attempts[0].sparkUser);
    return lists;
}

function getTheJobStatusImgLabel(str) {
    if (str == true) {
        return "<img src=\"resources/icons/Success.png\">";
    } else {
        return "<img src=\"resources/icons/Error.png\">";
    }
}

function setAMcontainer() {
    // filter out local application
    if (appId.substr(0, 5) === "local") {
        $("#containerNumber").text("Local Task");
    } else {
        getMessageAsync('/applications?appId=' + spark.appId, 'spark', function (str) {
            var myAttempts = JSON.parse(str);
            spark.containerId = myAttempts.appAttempts.appAttempt[0].containerId;
            spark.nodeId = myAttempts.appAttempts.appAttempt[0].nodeId;
            $("#containerNumber").text(spark.containerId);
            if (spark.appId.substr(0, 5) !== "local" && spark.attemptId !== 0) {
                getJobResult();
                getSparkDriverLog();
            }
        });
    }
}

function setDiagnosticsLog() {
    if (appId.substr(0, 5) === "local") {
        $("#errorMessage").text("No Yarn Error Message");
    } else {
        getMessageAsync("/apps?appId=" + spark.appId + 'yarn', function (s) {
            var responseObject = JSON.parse(s);
            var message = responseObject.app.diagnostics;
            if (message === 'undefined' || message === "") {
                message = "No Error Message";
            }
            $('#errorMessage').text(message);
        });
    }
}

function getSparkDriverLog() {
    if (spark.attemptId === 0 || typeof spark.containerId === 'undefined') {
        return;
    }
    getMessageAsync("/applications/driverLog?appId" + spark.appId, 'yarn', function (s) {
        var executorsObject = JSON.parse(s);
        // var hostPort = getDriverPortFromExecutor(executorsObject);
        // var ipAddress = hostPort.split(":")[0];
        // var url = localhost + projectId + "/jobhistory/logs/" + ipAddress + "/port/30050/" + containerId + "/" + containerId + "/livy/stderr?restType=yarnhistory";
        // getResultFromSparkHistory(url, function (result) {
        //     $("#sparkDriverLog").text(result);
        // });
    });
}

function getJobResult() {
    // there's no attemptId for non-spark job
    if (spark.attemptId === 0 || typeof spark.containerId === 'undefined') {
        return;
    }

    getMessageAsync("/yarnui/jobresult?appId" + spark.appId, 'yarnhistory', function (s) {
        // var executorsObject = JSON.parse(s);
        // var hostPort = getDriverPortFromExecutor(executorsObject);
        // var ipAddress = hostPort.split(":")[0];
        // var url = localhost + projectId + "/jobhistory/logs/" + ipAddress + "/port/30050/" + containerId + "/" + containerId + "/livy/stdout?restType=yarnhistory";
        // getResultFromSparkHistory(url, function (result) {
        //     if (result == "") {
        //         result = "No out put";
        //     }
        //     $("#jobOutputTextarea").text(result);
        // });
    });
}


// function setLivyLog() {
//     getMessageAsync(localhost + projectId + "/?restType=livy&applicationId=" + appId, function (s) {
//         $("#livyJobLog").text(s);
//     });
// }

function setJobDetail() {
    var selectedApp = findElement(spark.applicationList, function (d) {
       return d.id === appId;
    });
    if(typeof selectedApp === 'undefined') {
        return;
    }
    setDebugInfo("selectApp " + appId);
    if(selectedApp.attempts[0].sparkUser === 'hive') {
        return;
    }
    getMessageAsync("/applications/jobs?appId=" + spark.appId, 'spark', function (s) {
        spark.currentSelectedJobs = JSON.parse(s);
        renderJobDetails(spark.currentSelectedJobs);
        renderJobGraphOnApplicationLevel(spark.currentSelectedJobs);
    });
}

function setJobGraph(jobs) {
    spark.isJobGraphGenerated = true;
    d3.select("#job-graph-menu")
        .selectAll('li')
        .data(jobs)
        .enter()
        .append('li')
        .attr("role","presentation")
        .append("a")
        .attr("role","menuitem")
        .attr("tabindex", -1)
        .text(function(job) {
            return "Job " + job['jobId'];
        }).on('click', function(job, i) {
        setJobGraphForOneJob(job);
    });
}

function setJobGraphForOneJob(job) {
    var stageIds = job['stageIds'];
    var selectedStages = [];
    stageIds.forEach(function(stageId) {
       selectedStages.push(spark.currentSelectedStages.find(function(d) {
           return d['stageId'] === stageId;
       }));
    });
    renderJobGraph(selectedStages);
}

function stagesInfo(jobs, url) {
        getMessageAsync("/applications/stages?appId=" + spark.appId, 'spark', function (s) {
            var data = new Object();
            var stages = JSON.parse(s);
            data.jobs = jobs;
            data.stages = stages;
            data.stageDetails = [];
            data.jobs.stageIds.forEach(function(stageNumber) {
                getMessageAsync("/applications/stages?appId=", function(s) {
                    var detail = JSON.parse(s);
                });
            });
        });
}

// function setJobTimeLine() {
//     var url = localhost + projectId + "/cluster/apps/" + appId + "?restType=yarn";
//     getMessageAsync(url, function(s) {
//         var t = s;
//     });
// }

///applications/[app-id]/storage/rdd
function setStoredRDD() {
    if(spark.attemptId === 0) {
        renderStoredRDD('');
        return;
    }
    getMessageAsync("/applications/storage?appId=" + spark.appId, 'spark', function(s) {
        var rdds = JSON.parse(s);
        renderStoredRDD(rdds);
    });
}

function setStageDetailsWithTaskDetails() {
    if(attemptId == 0) {
        $("#stage_detail_info_message").text("No Stage Info");
        return;
    }
    $("#stage_detail_info_message").text('');
    getMessageAsync(localhost + projectId + "/applications/" + appId + "/" + attemptId + "/stages", function (s) {
        spark.currentSelectedStages = JSON.parse(s);
        renderStageSummary(currentSelectedStages);
        setTaskDetails();
        if(!isJobGraphGenerated && currentSelectedJobs != null) {
            setJobGraph(currentSelectedJobs);
        }
    });
}

function setTaskDetails() {
    var httpQuery = localhost + projectId + "/applications" + "?applicationId="+appId + "&attemptId=" + attemptId + "&multi-stages=" + currentSelectedStages.length;
    getMessageAsync(httpQuery, function(s){
        var tasks = JSON.parse(s);
        renderTaskSummary(tasks);
    });
}

function setExecutorsDetails() {
    var httpQuery = localhost + projectId + "/applications/" + appId + "/" + attemptId + "/executors";
    getMessageAsync(httpQuery, function (s) {
        try {
            var executors = JSON.parse(s);
            renderExecutors(executors);
        } catch (e) {

        }
    })
}
function setDebugInfo(s) {
    $("#debuginfo").text(s);
}

function filterTaskSummaryTable() {
    var input, filter, table, tr, td, i;
    filter = $("#filterTableInput").val().toLowerCase();
    tr = $("#taskSummaryTable tbody tr");
    tr.each(function () {
        var text = $(this).html().toLowerCase();
        if(text.indexOf(filter) > -1) {
            $(this).css("display","");
        } else {
            $(this).css("display","none");
        }
    });
}

function filterStageTaskTableWithStageIds(stageIds) {
    var tr = $("#stageSummaryTable tbody tr");
    tr.each(function (i) {
        var id = $("#stageSummaryTbody>tr>td:nth-child(2)")[i].innerHTML;
        if( $.inArray( parseInt(id), stageIds) > -1 ) {
            $(this).css("display","");
            filterTaskTableWithStageId(id);
        } else {
            $(this).css("display","none");
        }
    });
}

function filterTaskTableWithStageId(stageId) {
    var httpQuery = localhost + projectId + "/applications/" + appId + "/" + attemptId + "/stages/" + stageId;
    getMessageAsync(httpQuery, function (s) {
        var stageDetails = JSON.parse(s);
        if(getJsonLength(stageDetails)) {
            var filteredTaskIds = Object.keys(stageDetails[0].tasks);
            filterTaskTableWithTaskIds(filteredTaskIds);
        }
    })
}
function filterTaskTableWithTaskIds(taskIds) {
    var tr = $("#taskSummaryTable tbody tr");
    tr.each(function (i) {
        var id = $("#taskSummaryTbody>tr>td:nth-child(1)")[i].innerHTML;
        if( $.inArray( id, taskIds) > -1 ) {
            $(this).css("display","");
        } else {
            $(this).css("display","none");
        }
    });
}
function getJsonLength(jsonObject) {
    var length = 0;
    for(var item in jsonObject) {
        length++;
    }
    return length;
}
function openLivyLog() {

}