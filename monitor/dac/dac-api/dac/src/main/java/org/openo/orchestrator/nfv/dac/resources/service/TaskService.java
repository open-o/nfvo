/*
 * Copyright 2016, CMCC Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.orchestrator.nfv.dac.resources.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import org.openo.orchestrator.nfv.dac.common.DacConst;
import org.openo.orchestrator.nfv.dac.common.ProtocalAvailability;
import org.openo.orchestrator.nfv.dac.common.bean.TaskBean;
import org.openo.orchestrator.nfv.dac.common.util.ExtensionAccess;
import org.openo.orchestrator.nfv.dac.dataaq.common.DataAcquireException;
import org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common.IMonitorTaskInfo;
import org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common.MonitorTaskInfo;
import org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common.NullTaskInfo;
import org.openo.orchestrator.nfv.dac.dataaq.scheduler.MonitorTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
    /**
     * cache all task timer thread with task id. key->task id, value->the timer thread
     */
    private static ConcurrentHashMap<Integer, Timer> taskIdToTimerMap = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<Integer, MonitorTaskInfo> taskIdToTaskInfo = new ConcurrentHashMap<>();

    /**
     * Create monitor task
     *
     * @param taskInfo task info
     * @throws DataAcquireException
     */
    public static void taskCreate(TaskBean taskInfo) throws DataAcquireException {
        MonitorTaskInfo monitorTaskInfo = convertTaskInfo(taskInfo);
        createMonitorTask(monitorTaskInfo);
    }

    /**
     * Modify monitor task
     * @param taskId task id
     * @param taskInfo task info
     * @throws DataAcquireException
     */
    public static void taskModify(int taskId, TaskBean taskInfo) throws DataAcquireException {
        MonitorTaskInfo monitorTaskInfo = convertTaskInfo(taskInfo);
        modifyMonitorTask(taskId, monitorTaskInfo);
    }

    /**
     * Delete monitor task
     * @param taskId task id
     * @throws DataAcquireException
     */
    public static void taskDelete(int taskId) throws DataAcquireException {
        deleteMonitorTask(taskId);
    }

    /**
     * Create monitor task for acquire data
     *
     * @param taskInfo task info
     * @throws DataAcquireException
     */
    private static void createMonitorTask(MonitorTaskInfo taskInfo) throws DataAcquireException {
        if (taskIdToTimerMap.get(taskInfo.getJobId()) != null) {
            return;// ignore.the task is running
        }
        activateMonitorTask(taskInfo);
    }

    /**
     * modify monitor task
     *
     * @param taskId   the task id
     * @param taskInfo the task info
     * @throws DataAcquireException
     */
    private static void modifyMonitorTask(int taskId, MonitorTaskInfo taskInfo)
            throws DataAcquireException {
        deleteMonitorTask(taskId);// delete the old task
        activateMonitorTask(taskInfo);// active the new task
    }

    /**
     * delete monitor task by task id
     *
     * @param taskId the task id
     */
    private static void deleteMonitorTask(int taskId) {
        Timer timer = taskIdToTimerMap.get(taskId);
        if (timer != null) {
            timer.cancel();// destroy the timer thread.
            MonitorTaskInfo taskInfo = taskIdToTaskInfo.get(taskId);
            ProtocalAvailability.getInstance().remove(taskInfo);
            taskIdToTimerMap.remove(taskId);
            taskIdToTaskInfo.remove(taskId);
            LOGGER.info("taskId=" + taskId + " has deleted.");
        }
    }

    /**
     * Delete all running monitor tasks
     */
    public static void deleteAllMonitorTask() {
        for (Integer jobId : taskIdToTimerMap.keySet()) {
            deleteMonitorTask(jobId);
        }
    }

    /**
     * Convert TaskBean object to MonitorTaskInfo object
     *
     * @param taskBean TaskBean object
     * @return MonitorTaskInfo object
     */
    private static MonitorTaskInfo convertTaskInfo(TaskBean taskBean) {
        String neTypeId = taskBean.getCommParam().getProperty(DacConst.NETYPEID);

        if(neTypeId==null){
            return new NullTaskInfo();
        }

        MonitorTaskInfo monitorTaskInfo = getMonitorTaskInfoInstance(neTypeId, taskBean.getMonitorName());
        monitorTaskInfo.setJobId(taskBean.getTaskId());
        monitorTaskInfo.setGranularity(taskBean.getGranularity());
        monitorTaskInfo.setColumnName(taskBean.getColumnName());
        monitorTaskInfo.setMonitor(taskBean.getMonitorName());
        monitorTaskInfo.setCommPara(taskBean.getCommParam());
        return monitorTaskInfo;
    }

    /**
     * Return Specific MonitorTaskInfo Instance by neTypeId.
     * Specific MonitorTaskInfo is defined in file: conf/dataaq/extend/[device*]-extendsimpl.xml
     */
    private static MonitorTaskInfo getMonitorTaskInfoInstance(String neTypeId, String monitorName) {
    	// provider:monitorname
    	String[] tmp = monitorName.split(":");
    	if (tmp.length == 2)
    	{
    		monitorName = tmp[1];
    	}

        String extensionID = IMonitorTaskInfo.class.getName();
        Object taskInfo =  ExtensionAccess.getExtension(extensionID, monitorName);
        if(taskInfo == null)
        {
        	taskInfo = ExtensionAccess.getExtension(extensionID, getQueryId(neTypeId, monitorName));
            if (taskInfo == null)
            {
            	taskInfo = ExtensionAccess.getExtension(extensionID, neTypeId);
                if (taskInfo == null)
                {
                	return null;
                }
            }
        }
        return (MonitorTaskInfo)taskInfo;

    }

    private static String getQueryId(String neTypeId, String monitorName)
    {
    	StringBuilder tmp = new StringBuilder();
    	tmp.append(neTypeId).append(".").append(monitorName);
    	return tmp.toString();
    }

    private static void activateMonitorTask(MonitorTaskInfo taskInfo) throws DataAcquireException {
        MonitorTask monitorTask = new MonitorTask(taskInfo);
        long offset = DateOffset.nextOffSet();
        Date beginTime = getNextGranularityTimeFromBeginTime(taskInfo.getGranularity());
        monitorTask.initReportTime(beginTime);
        Date nextLatestExeTime = offsetDate(beginTime, offset);
        nextLatestExeTime = adjustBeginTime(nextLatestExeTime);

        Timer timer = new Timer();
        LOGGER.info("new timer for monitorTask: " + taskInfo.getJobId());
        // schedule the monitor task by a timer thread.
        timer.schedule(monitorTask, nextLatestExeTime, taskInfo.getGranularity() * 1000);
        taskIdToTimerMap.put(taskInfo.getJobId(), timer);// cache
        taskIdToTaskInfo.put(taskInfo.getJobId(), taskInfo);
        ProtocalAvailability.getInstance().add(taskInfo);
    }

    /**
     * Calculate the execution time of the next granularity according to the begin time.<br>
     * <br>
     * current time is 9:22 and granularity is 5 minutes, the next execute time is 9:25<br>
     * current time is 9:22 and granularity is 15 minutes, the next execute time is 9:30<br>
     * current time is 9:22 and granularity is 30 minutes, the next execute time is 9:30<br>
     * current time is 9:22 and granularity is 60 minutes, the next execute time is 10:00<br>
     */
    private static Date getNextGranularityTimeFromBeginTime(long granularity) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        if (granularity <= 3600) {
            long retLong = cal.get(Calendar.HOUR_OF_DAY) * 60 * 60 + cal.get(Calendar.MINUTE) * 60
                    + cal.get(Calendar.SECOND);
            retLong = retLong / granularity * granularity;
            cal.set(Calendar.HOUR_OF_DAY, (int) retLong / 60 / 60);
            cal.set(Calendar.MINUTE, (int) retLong / 60 % 60);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.add(Calendar.SECOND, (int) granularity);
        } else {
            int hour = (int) granularity / 3600;
            cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + hour);
        }

        return cal.getTime();
    }

    /**
     * Calculate the new time according to the offset
     *
     * @param date   initial time
     * @param offset offset
     * @return the new time
     */
    private static Date offsetDate(Date date, long offset) {
        long time = date.getTime();
        return new Date(time + offset);
    }

    private static Date adjustBeginTime(Date beginTime) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(beginTime);
        int second = ca.get(Calendar.SECOND) / 10;
        ca.set(Calendar.SECOND, second * 10);
        ca.set(Calendar.MILLISECOND, 0);
        beginTime = ca.getTime();
        return beginTime;
    }

    /**
     * Calculating a random offset
     */
    private static class DateOffset {
        private static int delta = 15000;

        private static synchronized long nextOffSet() {
            delta = (delta + 4370) % 230000; // (20s~230s)
            return delta;
        }
    }
}
