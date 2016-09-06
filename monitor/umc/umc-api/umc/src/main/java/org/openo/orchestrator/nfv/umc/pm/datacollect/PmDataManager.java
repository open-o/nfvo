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
package org.openo.orchestrator.nfv.umc.pm.datacollect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.openo.orchestrator.nfv.umc.cache.CacheUtil;
import org.openo.orchestrator.nfv.umc.pm.common.PmConst;
import org.openo.orchestrator.nfv.umc.pm.common.PmException;
import org.openo.orchestrator.nfv.umc.pm.common.PmOsfUtil;
import org.openo.orchestrator.nfv.umc.pm.datacollect.bean.PmData;
import org.openo.orchestrator.nfv.umc.pm.task.bean.DataaqTaskInfo;
import org.openo.orchestrator.nfv.umc.pm.task.bean.PoCounterInfo;
import org.openo.orchestrator.nfv.umc.pm.task.cache.DataaqTaskCacheUtil;
import org.openo.orchestrator.nfv.umc.util.ExtensionAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Performance data manager. Provide interfaces for performance data storage, computing availability,
 * northbound querying, data aggregation, threshold calculation etc.
 */
public class PmDataManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(PmDataManager.class);




	private static PmDataManager pmDataManager = new PmDataManager();
	
	public PmDataManager() {

	}

	public static PmDataManager getInstance()
	{
		return pmDataManager;
	}
	
	/**
	 * Save history performance data to database.
	 * @param proxyIp DAC ip address
	 * @param pmType measure type id
	 * @param pmDataList history performance data
	 */
	public void saveToDB(String proxyIp, String pmType,
			List<PmData> pmDataList) {		
		List<List<Object>> dataList = new ArrayList<List<Object>>();

		for (PmData pmData : pmDataList) {			
			int jobId = pmData.getJobId();

			DataaqTaskInfo dataaqTaskInfo = DataaqTaskCacheUtil.getInstance().getTaskInfoByNeJobId(
					proxyIp, jobId);
			if (dataaqTaskInfo == null) {
				continue;
			}
			
			String neOid = dataaqTaskInfo.getNeId();
			int granularity = pmData.getGranularity();
			long endTime = pmData.getCollectTime();
			long beginTime = endTime - granularity  * 1000;
			String beginTimeStr = formatRemoteDate(new Date(beginTime),
					 PmConst.DATE_PATTERN);
			String endTimeStr = formatRemoteDate(new Date(endTime),
					 PmConst.DATE_PATTERN);
			
			// performance data
			List<Map<String, Object>> collectDataList = getCollectDataList(pmType, pmData);
			for (Map<String, Object> datas : collectDataList) {
				// cache one record data
				List<Object> data = new ArrayList<Object>();
				data.add(beginTimeStr);
				data.add(endTimeStr);
				data.add(granularity);
				data.add(neOid);
				data.add(datas);
				dataList.add(data);
			}			
			
		}
		
		Map<String, Object> attrList = new HashMap<String, Object>();		
		attrList.put(PmConst.pmType, pmType);
		attrList.put(PmConst.pmDataList, dataList);

		Object[] pmDataConsumers = ExtensionAccess.getExtensions(IPmDataConsumer.class.getName(), PmConst.CONSUMERID);		

		for (Object pmDataConsumer : pmDataConsumers) {			
			((IPmDataConsumer)pmDataConsumer).consume(attrList);
		}

	}
	
	private List<Map<String, Object>> getCollectDataList(String pmType, PmData pmData) 
	{
		
		Map<String, String> clmnTypes = getClmnTypes(pmType);
		List<Map<String, Object>> collectData = new ArrayList<Map<String, Object>>();
		Properties[] props = pmData.getValues();
		for (Properties p : props)
		{
			Map<String, Object> data = new HashMap<String, Object>();
			@SuppressWarnings("rawtypes")
            Set keys = p.keySet();
			for (Object key : keys)
			{
				String clmnType = clmnTypes.get(key);
                if ( PmConst.JAVA_LANG_CLASS_STRING.equals(clmnType))
                {
                    data.put((String)key,p.getProperty((String)key));
                }
                else
                {
                    Double numValue = new Double(p.getProperty((String)key));
                    data.put((String)key, numValue);
                }
			}
			collectData.add(data);
		}
		return collectData;
	}

	private Map<String, String> getClmnTypes(String pmType)
	{
		List<PoCounterInfo> counters = CacheUtil.getCountersByPMType(pmType);
		int indexNum = counters.size();					
		// column name -> data type
		Map<String, String> clmnType = new HashMap<String, String>();

		for (int k = 0; k < indexNum; k++) {
			PoCounterInfo counterInfo = counters.get(k);
			
			clmnType.put(counterInfo.getAttrClmn(), 
					PmOsfUtil.getJavaDataTyeOfDbFieldType(counterInfo.getAttrDataType()));
		}
		return clmnType;
	}
	
	/**
	 * Performance data filtering. Filtering data that does not belong to an active effective performance measure task.
	 * @param proxyIp DAC ip address
	 * @param pmDataList history performance data
	 */
	public void filterByDataaqTask(String proxyIp, List<PmData> pmDataList) {
		PmData[] pmDatas = pmDataList.toArray(new PmData[pmDataList
				.size()]);
		for (PmData pmData : pmDatas) {
			int jobId = pmData.getJobId();
			DataaqTaskInfo dataaqTaskInfo = DataaqTaskCacheUtil.getInstance()
					.getTaskInfoByNeJobId(proxyIp, jobId);
			if (dataaqTaskInfo == null) {
				pmDataList.remove(pmData);
				continue;
			}
		}
	}

	/**
	 * @param proxyIp
	 * @param pmDataList
	 * @return
	 * @throws PmException
	 */
	public String getPoidFromDataaqTask(String proxyIp, List<PmData> pmDataList)
			throws PmException {
		if (pmDataList == null || pmDataList.isEmpty()) {
			throw new PmException("pmDataList == null || pmDataList.isEmpty()",
					null);
		}
		int jobId = pmDataList.get(0).getJobId();
		DataaqTaskInfo dataaqTaskInfo = DataaqTaskCacheUtil.getInstance()
				.getTaskInfoByNeJobId(proxyIp, jobId);
		if (dataaqTaskInfo == null) {
			return null;
		}
		return dataaqTaskInfo.getPmType();
	}


	private String formatRemoteDate(Date translatedDate, String datePattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
//		TimeZone gmt0 = TimeZone.getTimeZone("GMT0");
//		sdf.setTimeZone(gmt0);
		return sdf.format(translatedDate);
	}
}
