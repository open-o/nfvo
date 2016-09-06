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
package org.openo.orchestrator.nfv.umc.pm.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openo.orchestrator.nfv.umc.db.entity.PmTask;
import org.openo.orchestrator.nfv.umc.pm.common.DebugPrn;
import org.openo.orchestrator.nfv.umc.pm.common.PmConst;
import org.openo.orchestrator.nfv.umc.pm.task.bean.DataaqTaskInfo;
import org.openo.orchestrator.nfv.umc.pm.task.bean.PmTaskInfo;


public class PmTaskUtil {
	public static DebugPrn dMsg = new DebugPrn(PmTaskUtil.class.getName());
	
    public static DataaqTaskInfo createTaskInfo(PmTask itPmTask) throws PmTaskException
    {
    	String pmType = itPmTask.getPOid();
		DataaqTaskInfo dataaqTaskInfo = new DataaqTaskInfo();
		dataaqTaskInfo.setJobId((Integer)itPmTask.getJobId());
		dataaqTaskInfo.setNeId(itPmTask.getOid());
		dataaqTaskInfo.setPmType(pmType);
		dataaqTaskInfo.setNeType(itPmTask.getNetTypeId());
		dataaqTaskInfo.setBeginTime(new Date());
		dataaqTaskInfo.setEndTime(null);
		dataaqTaskInfo.setMeasureGranularity(Integer.valueOf(itPmTask.getGranularity()));
		return dataaqTaskInfo;
    }
    
    public static boolean isTaskActive(String version, PmTaskInfo pmTaskInfo)
    {
		if (PmConst.TASK_ACTIVE.equals(pmTaskInfo.getTaskStatus()))
		{
			if (version == null || pmTaskInfo.getVersion().equals(PmConst.TASK_ALLVERSION)
					|| pmTaskInfo.getVersion().equals(version))
			{
				return true;
			}
		}
		return false;
    }
    
	public static String getFormatTime(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(PmConst.DATE_FORMATE);
		String time = sdf.format(date);
		return time;
	}
}
