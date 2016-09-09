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
package org.openo.nfvo.monitor.umc.pm.task;

import java.util.List;

import org.openo.nfvo.monitor.umc.cache.CacheUtil;
import org.openo.nfvo.monitor.umc.db.entity.PmTask;
import org.openo.nfvo.monitor.umc.db.entity.PmTaskThreshold;
import org.openo.nfvo.monitor.umc.pm.common.DebugPrn;
import org.openo.nfvo.monitor.umc.pm.common.PmConst;
import org.openo.nfvo.monitor.umc.pm.common.PmException;
import org.openo.nfvo.monitor.umc.pm.db.process.PmDBProcess;
import org.openo.nfvo.monitor.umc.pm.task.bean.DataaqTaskInfo;
import org.openo.nfvo.monitor.umc.pm.task.bean.PmTaskInfo;
import org.openo.nfvo.monitor.umc.pm.task.bean.PoCounterThreshold;
import org.openo.nfvo.monitor.umc.pm.task.cache.DataaqTaskCacheUtil;

/**
 * Performance task service. Provide CRUD operations for Performance task.
 */
public class PmTaskService {

    private static final DebugPrn dMsg = new DebugPrn(PmTaskService.class.getName());

    /**
     * Re-create Performance task.
     * @param proxyIp DAC IP address
     * @param oid resource id
     * @param moc measure type id
     */
    public static void pmTaskReCreate(String version, String proxyIp, String oid, String moc) {
        pmTaskDelete(oid);
        pmTaskCreate(version, proxyIp, oid, moc);
    }

    /**
     * Create Performance task.
     * @param proxy DAC IP address
     * @param oid resource id
     * @param moc measure type id
     */
    public static void pmTaskCreate(String version, String proxy, String oid, String moc) {
        try {
            List<PmTaskInfo> pmTaskInfos = CacheUtil.getPmTaskInfosByNeTypeId(moc);
            for (PmTaskInfo pmTaskInfo : pmTaskInfos) {
                PmTask itPmTask = pmTaskDBCreate(oid, pmTaskInfo);
                if (PmTaskUtil.isTaskActive(version, pmTaskInfo)) {
                    try {
                        pmTaskStart(proxy, itPmTask);
                    } catch (PmTaskException e) {
                        dMsg.warn("Task start failed:" + oid + ":" + pmTaskInfo.getPoId(), e);
                    }
                }
                else
                {
                    itPmTask.setTaskStatus(PmConst.TASK_INACTIVE);
                    PmDBProcess.update(PmConst.PM_TASK, itPmTask);
                }
            }
        } catch (PmException e) {
            dMsg.warn(e.getMessage());
        }
    }

    private static void pmTaskStart(String proxyIp, PmTask itPmTask)
            throws PmTaskException, PmException {
        DataaqTaskInfo dataaqTaskInfo = PmTaskUtil.createTaskInfo(itPmTask);
        dataaqTaskInfo.setProxyIp(proxyIp);
        PmTaskManager.getInstance().createTask(dataaqTaskInfo);
        itPmTask.setBeginTime(dataaqTaskInfo.getBeginTime().getTime());
        itPmTask.setProxy(proxyIp);
        PmDBProcess.update(PmConst.PM_TASK, itPmTask);
    }

    /**
     * Create performance threshold task
     * @param oid resource id
     * @param moc measure type id
     */
    public static void pmThresholdCreate(String oid, String moc) {
        try {
        	List<PoCounterThreshold> pmIndexDefs = CacheUtil.getCounterThresholdByNeTypeId(moc);
        	if (pmIndexDefs != null)
        	{
	            for (PoCounterThreshold pmIndexDef : pmIndexDefs) {
	                PmTaskThreshold itThresholdInfo = pmThresholdDBCreate(oid, pmIndexDef);
	                PoCounterThreshold threshold = createThreshold(itThresholdInfo);
	                CacheUtil.cachePOThreshold(oid, threshold.getPmType(), threshold);
	            }
        	}
        } catch (PmException e) {
            dMsg.warn(e.getMessage());
        }
    }

    /**
     * Delete performance threshold task
     * @param oid resource id
     */
    public static void pmThresholdDelete(String oid) {
        pmThresholdDBDelete(oid);
        CacheUtil.removePOThreshold(oid);

    }

    private static PmTask pmTaskDBCreate(String oid, PmTaskInfo pmTaskInfo)
            throws PmException {
        PmTask itPmTask = new PmTask();
        int maJobId = PmDBProcess.queryMaxPmJobId();
        int jobId = maJobId == -1 ? 1 : maJobId + 1;
        itPmTask.setOid(oid);
        itPmTask.setJobId(jobId);
        itPmTask.setTaskId(pmTaskInfo.getTaskId());
        itPmTask.setNetTypeId(pmTaskInfo.getNeTypeId());
        itPmTask.setPOid(pmTaskInfo.getPoId());
        itPmTask.setTaskStatus(pmTaskInfo.getTaskStatus());
        itPmTask.setGranularity(pmTaskInfo.getGranularity());
        itPmTask.setEndTime(pmTaskInfo.getEndTime());
        PmDBProcess.save(PmConst.PM_TASK, itPmTask);
        return itPmTask;
    }

    private static PmTaskThreshold pmThresholdDBCreate(String oid, PoCounterThreshold pmIndexDef)
            throws PmException {
        PmTaskThreshold itThresholdInfo = new PmTaskThreshold();
        itThresholdInfo.setOid(oid);
        itThresholdInfo.setNeTypeId(pmIndexDef.getNeTypeId());
        itThresholdInfo.setPoid(pmIndexDef.getPmType());
        itThresholdInfo.setPoattributeId(pmIndexDef.getAttrId());
        itThresholdInfo.setPoattributeName(pmIndexDef.getAttrName());
        itThresholdInfo.setAlarmCode(pmIndexDef.getAlarmCode());
        itThresholdInfo.setIsAlarm(pmIndexDef.getIsAlarm());
        itThresholdInfo.setDirect(pmIndexDef.getDirection());
        itThresholdInfo.setCritial(pmIndexDef.getCritical());
        itThresholdInfo.setMajor(pmIndexDef.getMajor());
        itThresholdInfo.setMinor(pmIndexDef.getMinor());
        itThresholdInfo.setWarning(pmIndexDef.getWarning());
        PmDBProcess.save(PmConst.PM_TASK_THRESHOLD, itThresholdInfo);
        return itThresholdInfo;
    }

    private static void pmThresholdDBDelete(String oid) {
        try {
            PmDBProcess.deleteDbByOid(PmConst.PM_TASK_THRESHOLD, oid);
        } catch (PmException e) {
            dMsg.warn(e.getMessage());
        }
    }

    private static PoCounterThreshold createThreshold(PmTaskThreshold itThresholdInfo) {
        PoCounterThreshold threshold = new PoCounterThreshold();
        threshold.setNeId(itThresholdInfo.getOid());
        threshold.setPmType(itThresholdInfo.getPoid());
        threshold.setNeTypeId(itThresholdInfo.getNeTypeId());
        threshold.setAttrId(itThresholdInfo.getPoattributeId());
        threshold.setAttrName(itThresholdInfo.getPoattributeName());
        threshold.setAlarmCode(itThresholdInfo.getAlarmCode());
        threshold.setIsAlarm(itThresholdInfo.getIsAlarm());
        threshold.setDirection(itThresholdInfo.getDirect());
        threshold.setCritical(itThresholdInfo.getCritial());
        threshold.setMajor(itThresholdInfo.getMajor());
        threshold.setMinor(itThresholdInfo.getMinor());
        threshold.setWarning(itThresholdInfo.getWarning());
        return threshold;
    }

    /**
     * Delete performance task.
     * @param oid resource id.
     */
    public static void pmTaskDelete(String oid) {
        List<DataaqTaskInfo> allCachedTasks = DataaqTaskCacheUtil.getInstance().getAllTaskInfo();
        for (DataaqTaskInfo dataaqTaskInfo : allCachedTasks) {
            if (dataaqTaskInfo.getNeId().equals(oid)) {
                int jobId = dataaqTaskInfo.getJobId();
                String poid = dataaqTaskInfo.getPmType();
                String proxyIp = dataaqTaskInfo.getProxyIp();
                try {
                    PmTaskManager.getInstance().deleteTask(jobId, proxyIp, poid);
                } catch (PmTaskException e) {
                    dMsg.info("fail to delete pm job on proxy. error message : " + e.getMessage());
                }

                DataaqTaskCacheUtil.getInstance().deleteMonitorTaskByNeTaskId(proxyIp, jobId);
            }
        }
        try {
            PmDBProcess.deleteDbByOid(PmConst.PM_TASK, oid);
            PmDBProcess.deleteDbByOid(PmConst.MONITOR_INFO, oid);
        } catch (PmException e) {
            dMsg.warn(e.getMessage());
        }
    }

    /**
     * Modify performance task.
     * @param oid resource id.
     */
    public static void pmTaskModify(String oid) {
        List<DataaqTaskInfo> allCachedTasks = DataaqTaskCacheUtil.getInstance().getAllTaskInfo();
        for (DataaqTaskInfo dataaqTaskInfo : allCachedTasks) {
            if (dataaqTaskInfo.getNeId().equals(oid)) {
                try {
                    PmTaskManager.getInstance().modifyTask(dataaqTaskInfo);
                } catch (PmTaskException e) {
                    dMsg.info("fail to modify pm job on proxy. error message : " + e.getMessage());
                }
            }
        }
    }
}
