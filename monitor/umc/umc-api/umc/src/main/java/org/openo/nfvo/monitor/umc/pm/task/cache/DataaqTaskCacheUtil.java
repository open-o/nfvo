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
package org.openo.nfvo.monitor.umc.pm.task.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.openo.nfvo.monitor.umc.pm.task.bean.DataaqTaskInfo;


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
