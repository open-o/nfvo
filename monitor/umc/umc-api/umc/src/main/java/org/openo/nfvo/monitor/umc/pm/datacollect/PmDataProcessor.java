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
package org.openo.nfvo.monitor.umc.pm.datacollect;

import java.util.List;

import org.openo.nfvo.monitor.umc.pm.datacollect.bean.PmData;

/**
 * History performance data processor. Process history performance data reported by DAC.
 */
public class PmDataProcessor{


	public PmDataProcessor() {
	}

	private static PmDataProcessor pmDataProcessor = new PmDataProcessor();
	
	public static PmDataProcessor getInstance()
	{
		return pmDataProcessor;
	}

	/**
	 * Process history performance data reported by DAC.
	 * @param proxyIp DAC IP address
	 * @param pmDataList history performance data
	 */
	public void pmTaskResultProcess(String proxyIp, List<PmData> pmDataList) {
		PmDataManager.getInstance().filterByDataaqTask(proxyIp, pmDataList);
		if (pmDataList.isEmpty()) {
			return;
		}
		PmDataProcessorPool.getInstance().addPmDataProcessorTask(proxyIp, pmDataList);
	}
}
