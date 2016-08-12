package org.openo.orchestrator.nfv.umc.pm.datacollect;

import java.util.List;

import org.openo.orchestrator.nfv.umc.pm.datacollect.bean.PmData;

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
