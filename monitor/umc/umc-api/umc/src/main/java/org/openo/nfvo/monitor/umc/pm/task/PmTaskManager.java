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

import java.util.Date;
import java.util.Properties;

import org.openo.nfvo.monitor.umc.cache.CacheUtil;
import org.openo.nfvo.monitor.umc.monitor.wrapper.MonitorServiceWrapper;
import org.openo.nfvo.monitor.umc.pm.adpt.dac.DacAdptImpl;
import org.openo.nfvo.monitor.umc.pm.adpt.dac.bean.TaskCreateAndModifyInfo;
import org.openo.nfvo.monitor.umc.pm.adpt.dac.bean.TaskCreateAndModifyInfoAck;
import org.openo.nfvo.monitor.umc.pm.adpt.dac.bean.TaskDeleteInfo;
import org.openo.nfvo.monitor.umc.pm.adpt.dac.bean.TaskDeleteInfoAck;
import org.openo.nfvo.monitor.umc.pm.common.DebugPrn;
import org.openo.nfvo.monitor.umc.pm.common.PmConst;
import org.openo.nfvo.monitor.umc.pm.task.bean.DataaqTaskInfo;
import org.openo.nfvo.monitor.umc.pm.task.cache.DataaqTaskCacheUtil;


/**
 * Performance measure task manager.
 */
public class PmTaskManager {
	private static DebugPrn dMsg = new DebugPrn(PmTaskManager.class.getName());
	
	private static PmTaskManager pmTaskManager = new PmTaskManager();
	
	public static PmTaskManager getInstance()
	{
		return pmTaskManager;
	}
	
	/**
	 * Send modify performance data collection task to DAC. 
	 * @param dataaqTaskInfo performance data collection task information.
	 * @return task id.
	 * @throws PmTaskException
	 */
	public int modifyTask(DataaqTaskInfo dataaqTaskInfo) throws PmTaskException {
		String proxyIp = dataaqTaskInfo.getProxyIp();
		dataaqTaskInfo.setTaskCreateTime(new Date());
		dMsg.info("modifyTask=" + dataaqTaskInfo.toString());
		TaskCreateAndModifyInfo modifyInfo = getTaskCreateAndModifyInfo(dataaqTaskInfo);
		TaskCreateAndModifyInfoAck taskModifyInfoAck = DacAdptImpl.taskModify(proxyIp, modifyInfo);
		
		if (taskModifyInfoAck.getStatus() == PmConst.STATUS_FAIL) {
			String errorMsg = taskModifyInfoAck.getErrorMsg();
			String debugMsg = taskModifyInfoAck.getDebugMsg();
			
			dMsg.info("modify task in proxy fail. proxy debugMsg : " + debugMsg);
			
			throw new PmTaskException(errorMsg);
		}

		int jobId = taskModifyInfoAck.getTaskId();
		dataaqTaskInfo.setJobId(jobId);
		DataaqTaskCacheUtil.getInstance().saveTaskInfoInTable(proxyIp, jobId, dataaqTaskInfo);
		return jobId;
	}
	
	/**
	 * Send create performance data collection task to DAC. 
	 * @param dataaqTaskInfo performance data collection task information.
	 * @return task id.
	 * @throws PmTaskException
	 */
	public int createTask(DataaqTaskInfo dataaqTaskInfo) throws PmTaskException {
		String proxyIp = dataaqTaskInfo.getProxyIp();
		dataaqTaskInfo.setTaskCreateTime(new Date());
		dMsg.info("createTask=" + dataaqTaskInfo.toString());		
		TaskCreateAndModifyInfo createInfo = getTaskCreateAndModifyInfo(dataaqTaskInfo);	
		TaskCreateAndModifyInfoAck taskCreateInfoAck = DacAdptImpl.taskCreate(proxyIp, createInfo);

		if (taskCreateInfoAck.getStatus() == PmConst.STATUS_FAIL) {
			String errorMsg = taskCreateInfoAck.getErrorMsg();
			String debugMsg = taskCreateInfoAck.getDebugMsg();
			
			dMsg.info("create task in proxy fail. proxy debugMsg : " + debugMsg);
			
			throw new PmTaskException(errorMsg);
		}

		int jobId = taskCreateInfoAck.getTaskId();
		dataaqTaskInfo.setJobId(jobId);
		DataaqTaskCacheUtil.getInstance().saveTaskInfoInTable(proxyIp, jobId, dataaqTaskInfo);
		return jobId;
	}
	
	/**
	 * Send delete performance data collection task to DAC. 
	 * @param jobId task id.
	 * @param proxyIp DAC IP address
	 * @param pmType measure type id
	 * @throws PmTaskException
	 */
	public void deleteTask(int jobId, String proxyIp, String pmType) throws PmTaskException {
		if (jobId == PmConst.JOBID_ERROR) {
			return;
		}
		
		dMsg.info("deleteTask jobId=" + jobId + ", proxyIp=" + proxyIp);
		
		TaskDeleteInfo taskDeleteInfo = new TaskDeleteInfo(jobId);
		TaskDeleteInfoAck taskDeleteInfoAck = DacAdptImpl.taskDelete(proxyIp, taskDeleteInfo);
		
		int status = taskDeleteInfoAck.getStatus();
		if (status == PmConst.STATUS_FAIL) {
			String errorMsg = taskDeleteInfoAck.getErrorMsg();
			String debugMsg = taskDeleteInfoAck.getDebugMsg();
			
			dMsg.info("delete task in proxy fail. proxy debugMsg : " + debugMsg);
			
			throw new PmTaskException(errorMsg);
		}
	}
	

	private TaskCreateAndModifyInfo getTaskCreateAndModifyInfo(DataaqTaskInfo dataaqTaskInfo) throws PmTaskException {
		int measureGranularity = dataaqTaskInfo.getMeasureGranularity();
	
		String poid = dataaqTaskInfo.getPmType();
		String monitorName = CacheUtil.getMonitorNameByPOID(poid);

//		List<PoCounterInfo> counters = CacheUtil.getCountersByPMType(poid);
//		int indexNum = counters.size();
//		String[] clmnName = new String[indexNum];
//		for (int k = 0; k < indexNum; k++) {
//			PoCounterInfo counterInfo = counters.get(k);
//			
//			clmnName[k] = counterInfo.getAttrClmn();
//			
//		}
		String neId = dataaqTaskInfo.getNeId();
		Properties prop = null;
		prop = MonitorServiceWrapper.getInstance().getMonitorInfoByOid(neId);
		prop.put(PmConst.MONITORNAME, monitorName);
		
		TaskCreateAndModifyInfo taskCreateInfo = new TaskCreateAndModifyInfo();
		taskCreateInfo.setTaskId(dataaqTaskInfo.getJobId());
		taskCreateInfo.setGranularity(measureGranularity);
		taskCreateInfo.setMonitorName(monitorName);
//		taskCreateInfo.setColumnName(clmnName);
		taskCreateInfo.setCommParam(prop);
					
		return taskCreateInfo;
	}

	
}
