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
package org.openo.orchestrator.nfv.umc.pm.wrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.openo.orchestrator.nfv.umc.cache.CacheUtil;
import org.openo.orchestrator.nfv.umc.db.entity.PmTask;
import org.openo.orchestrator.nfv.umc.pm.adpt.roc.RocAdptImpl;
import org.openo.orchestrator.nfv.umc.pm.bean.PmMeaTaskBean;
import org.openo.orchestrator.nfv.umc.pm.bean.PmMoType;
import org.openo.orchestrator.nfv.umc.pm.bean.Resource;
import org.openo.orchestrator.nfv.umc.pm.bean.ResourceType;
import org.openo.orchestrator.nfv.umc.pm.common.PmException;
import org.openo.orchestrator.nfv.umc.pm.db.process.PmDBProcess;
import org.openo.orchestrator.nfv.umc.pm.task.bean.PmTaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PmMeaTaskServiceWrapper {
	private static final Logger logger = LoggerFactory.getLogger(PmMeaTaskServiceWrapper.class);

	public static PmMeaTaskBean getMeaTaskById(String taskId, String language) {
		PmMeaTaskBean pmTask = null;
		try {
		    PmTaskInfo pmTaskInfo = CacheUtil.getPmTaskInfoByTaskId(taskId);
		    pmTaskInfo.translate(language);
			pmTask = createPmMeaTaskBean(pmTaskInfo);
            List<PmTask> itPmTasks = PmDBProcess.queryTaskInfos(taskId);
			Resource[] rss = new Resource[itPmTasks.size()];
			for (int i = 0; i < itPmTasks.size(); i++)
			{
				PmTask itPmTask = itPmTasks.get(i);
				Resource rs = new Resource();
				rs.setId(itPmTask.getOid());
				rs.setName(RocAdptImpl.getResourceName(rs.getId(), itPmTask.getNetTypeId()));
				rss[i] = rs;
			}
			pmTask.setResources(rss);
		} catch (PmException e) {
			logger.warn("getMeaTaskById failed!" + e.getMessage());
		}
		return pmTask;
	}

	public static PmMeaTaskBean[] getAllPmMeaTasks(String resourceTypeId, String moTypeId, String language) {
		ArrayList<PmMeaTaskBean> pmTaskbeans = new ArrayList<PmMeaTaskBean>();
		Collection<PmTaskInfo> pmTaskInfos = new ArrayList<PmTaskInfo>();

	    if(resourceTypeId == null && moTypeId == null){
	        pmTaskInfos = CacheUtil.getAllPmTaskInfo();
	    }
	    else if(moTypeId != null){
	        pmTaskInfos.add(CacheUtil.getPmTaskInfoByMocId(moTypeId));
	    }
	    
		for (PmTaskInfo pmTaskInfo : pmTaskInfos)
		{
			pmTaskInfo.translate(language);
			PmMeaTaskBean pmTask = createPmMeaTaskBean(pmTaskInfo);
			pmTaskbeans.add(pmTask);
		}

		return pmTaskbeans.toArray(new PmMeaTaskBean[pmTaskbeans.size()]);
	}
	
	@SuppressWarnings("deprecation")
    private static final long LONG_TIME_AFTER = (new Date(200, 0, 1)).getTime();
	
	private static PmMeaTaskBean createPmMeaTaskBean(PmTaskInfo pmTaskInfo)
	{
		PmMeaTaskBean pmTask = new PmMeaTaskBean();
		pmTask.setId(String.valueOf(pmTaskInfo.getTaskId()));
		pmTask.setName(pmTaskInfo.getTaskName());
		pmTask.setActiveStatus(Integer.valueOf(pmTaskInfo.getTaskStatus()));
		pmTask.setGranularity(Integer.valueOf(pmTaskInfo.getGranularity()));
		pmTask.setBeginTime(pmTaskInfo.getBeginTime());
		if (pmTaskInfo.getEndTime() > 0) {
		    pmTask.setEndTime(pmTaskInfo.getEndTime());
		} else {
	          pmTask.setEndTime(LONG_TIME_AFTER);
		}
		ResourceType rt = new ResourceType();
		rt.setId(pmTaskInfo.getNeTypeId());
		rt.setName(pmTaskInfo.getNeTypeName());
		pmTask.setResourceType(rt);
		PmMoType moType = new PmMoType();
		moType.setId(pmTaskInfo.getMocId());
		moType.setName(pmTaskInfo.getPoName());
		pmTask.setMoType(moType);
		return pmTask;
	}
}
