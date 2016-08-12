package org.openo.orchestrator.nfv.umc.pm.task.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.openo.orchestrator.nfv.umc.pm.task.bean.DataaqTaskInfo;


/**
 * 
 */
public class DataaqTaskCacheUtil {

	/**
	 * key->proxyIp_jobId || value->taskInfo
	 */
	private static ConcurrentHashMap<String, DataaqTaskInfo> taskHashTable = new ConcurrentHashMap<String, DataaqTaskInfo>(50);
	
	private static final DataaqTaskCacheUtil instance = new DataaqTaskCacheUtil();
	
	private DataaqTaskCacheUtil() {}
	
	public static DataaqTaskCacheUtil getInstance() {
		return instance;
	}
	
	/**
	 * taskHashTable(Key->proxyIp_jobId, Value->dataaqTaskInfo)
	 */
	public void saveTaskInfoInTable(String proxyIp, int jobId, DataaqTaskInfo dataaqTaskInfo) {
		String key = proxyIp + "_" + jobId;
		taskHashTable.put(key, dataaqTaskInfo);
	}
	
	public DataaqTaskInfo getTaskInfoByNeJobId(String proxyIp, int jobId) {
		String key = proxyIp + "_" + jobId;
		return taskHashTable.get(key);
	}
	
	public void deleteMonitorTaskByNeTaskId(String proxyIp, int jobId) {
		String ip_jobId = proxyIp + "_" + jobId;
		taskHashTable.remove(ip_jobId);
	}
	
	public List<DataaqTaskInfo> getAllTaskInfo() {
		List<DataaqTaskInfo> viewOfCachedTasks = new ArrayList<DataaqTaskInfo>();
		Collection<DataaqTaskInfo> cachedTasks = taskHashTable.values();
		
		viewOfCachedTasks.addAll(cachedTasks);
		
		return viewOfCachedTasks;
	}
	
	public int getMaxPmJobId(String proxyIp) {
		int maxPmJobId = 0;

		Iterator<String> it = taskHashTable.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String[] keyArray = key.split("_");
			String cachedProxyIp = keyArray[0];
			String jobId = keyArray[1];
			
			if (cachedProxyIp.equals(proxyIp)) {
				int buf = Integer.parseInt(jobId);
				if (buf > maxPmJobId) {
					maxPmJobId = buf;
				}
			}
		}
		
		return maxPmJobId;
	}
}
