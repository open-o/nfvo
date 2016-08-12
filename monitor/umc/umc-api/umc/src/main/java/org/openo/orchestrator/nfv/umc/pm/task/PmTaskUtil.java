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
