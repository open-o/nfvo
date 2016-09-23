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
package org.openo.nfvo.monitor.umc.pm.naf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openo.nfvo.monitor.umc.cometdserver.CometdException;
import org.openo.nfvo.monitor.umc.cometdserver.CometdService;
import org.openo.nfvo.monitor.umc.pm.common.PmConst;
import org.openo.nfvo.monitor.umc.pm.common.PmOsfUtil;
import org.openo.nfvo.monitor.umc.pm.datacollect.IPmDataConsumer;
import org.openo.nfvo.monitor.umc.util.ExtensionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PmDataToNaf implements IPmDataConsumer {
	private static final Logger LOGGER = LoggerFactory.getLogger(PmDataToNaf.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public void consume(Map<String, Object> attrList) {
		String pmType = (String)attrList.get(PmConst.pmType);
		List<List<Object>> dataList = (List<List<Object>>)attrList.get(PmConst.pmDataList);
		List<PmNafData> pmNafDataList = new ArrayList<PmNafData>();
		for (List<Object> data : dataList) 
		{
			PmNafData nafData = new PmNafData();
			nafData.setPoId(pmType);
	        nafData.setCollectTime(PmOsfUtil.parseDate((String) data.get(PmConst.PM_DATA_INDEX_ENDTIME),  PmConst.DATE_PATTERN).getTime());
	        nafData.setGranularity(Integer.parseInt(data.get(PmConst.PM_DATA_INDEX_GRANULARITY).toString()));
	        nafData.setNeId(data.get(PmConst.PM_DATA_INDEX_NEID).toString());
	        Properties p = new Properties();
	        p.putAll((Map<String, Object>)data.get(PmConst.PM_DATA_INDEX_DATA));
	        nafData.setData(p);
	        pmNafDataList.add(nafData);
		}
		
		// pre process cometd data
		Object[] pmNafDataProcesses = ExtensionUtil.getInstances(IPmNafDataProcess.EXTENSIONID, IPmNafDataProcess.KEY);		
		if (pmNafDataProcesses != null && pmNafDataProcesses.length > 0)
		{
			for (Object pmNafDataProcess : pmNafDataProcesses) {			
				((IPmNafDataProcess)pmNafDataProcess).process(pmNafDataList);
			}
		}
		try {
			CometdService.getInstance().publish(CometdService.PM_UPLOAD_CHANNEL, pmNafDataList);
		} catch (CometdException e) {
			LOGGER.warn(e.getMessage(), e);
		}
	}
}
