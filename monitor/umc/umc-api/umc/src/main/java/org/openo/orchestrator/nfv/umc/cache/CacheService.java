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
package org.openo.orchestrator.nfv.umc.cache;

import java.util.List;
import java.util.Map;

import org.openo.orchestrator.nfv.umc.db.UmcDbUtil;
import org.openo.orchestrator.nfv.umc.db.dao.MonitorInfoDao;
import org.openo.orchestrator.nfv.umc.db.entity.MonitorInfo;
import org.openo.orchestrator.nfv.umc.db.entity.PmTask;
import org.openo.orchestrator.nfv.umc.monitor.wrapper.WrapperUtil;
import org.openo.orchestrator.nfv.umc.pm.common.PmConst;
import org.openo.orchestrator.nfv.umc.pm.common.PmException;
import org.openo.orchestrator.nfv.umc.pm.db.process.PmDBProcess;
import org.openo.orchestrator.nfv.umc.pm.task.PmTaskException;
import org.openo.orchestrator.nfv.umc.pm.task.PmTaskUtil;
import org.openo.orchestrator.nfv.umc.pm.task.bean.DataaqTaskInfo;
import org.openo.orchestrator.nfv.umc.pm.task.cache.DataaqTaskCacheUtil;
import org.openo.orchestrator.nfv.umc.snmptrap.common.TrapInfo;
import org.openo.orchestrator.nfv.umc.util.DebugPrn;

public class CacheService {
	private static final DebugPrn dMsg = new DebugPrn(CacheService.class.getName());
    private static boolean cacheFlag = false;
    
    public static void init() {
        if (!cacheFlag) {
            CacheUtil.buildCache();
            cacheAllActivePmTask();
            cacheAllNeTrapInfo();
            cacheFlag = true;
        }

    }
    

    /**
     * All active measurement tasks specified cache information network
     * 
     */
    private static void cacheAllActivePmTask() 
    {
        try {
            List<PmTask> itPmTasks = PmDBProcess.queryTaskInfos(null);

            for (PmTask itPmTask : itPmTasks) {

                if (itPmTask.getTaskStatus().equals(PmConst.TASK_ACTIVE)) {
                    String proxyIp = itPmTask.getProxy();
                    DataaqTaskInfo dataaqTaskInfo = PmTaskUtil.createTaskInfo(itPmTask);
                    dataaqTaskInfo.setProxyIp(proxyIp);
                    DataaqTaskCacheUtil.getInstance().saveTaskInfoInTable(proxyIp,
                            dataaqTaskInfo.getJobId(), dataaqTaskInfo);
                }

            }
        } catch (PmException e) {
            dMsg.error("Query itPmtask info error, " + e.getMessage());
        } catch (PmTaskException e) {
            dMsg.warn(e.getMessage());
        }

    }
    
    private static void cacheAllNeTrapInfo()
    {
    	Thread t = new Thread(new Runnable(){
    		public void run()
    		{
    	        MonitorInfoDao dao = (MonitorInfoDao) UmcDbUtil.getDao(PmConst.MONITOR_INFO);
    	        List<MonitorInfo> list = dao.findAll();
    	        if (list != null)
    	        {
    	        	for (MonitorInfo itPmMonitorInfo : list)
    	        	{
    	            	TrapInfo trapInfo = CacheUtil.getTrapInfoByNeTypeId(itPmMonitorInfo.getNeTypeId());
    	            	if (trapInfo != null)
    	            	{
	    	                Map<String, String> infoMap = WrapperUtil.convertParamInfo2Map(itPmMonitorInfo);
	    	                WrapperUtil.cacheTrapInfo(itPmMonitorInfo, infoMap);
    	            	}
    	        	}
    	        }
    		}
    	});
    	
    	t.start();
    }
}
