package org.openo.orchestrator.nfv.umc.pm.task.threshold;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openo.orchestrator.nfv.umc.cache.CacheUtil;
import org.openo.orchestrator.nfv.umc.pm.common.DebugPrn;
import org.openo.orchestrator.nfv.umc.pm.common.PmConst;
import org.openo.orchestrator.nfv.umc.pm.task.bean.PoCounterThreshold;


/**
 * Performance threshold calculating thread.
 */
public class ThresholdHandleThread implements Runnable
{
	private static DebugPrn dMsg = new DebugPrn(ThresholdHandleThread.class.getName());
	private Map attrList = null;

	public ThresholdHandleThread(Map attrList)
	{
		this.attrList = attrList;

	}

	public void run()
	{
		String pmType = (String) attrList.get("pmType");
		List pmDataList = (List) attrList.get("pmDataList");
		for (int index = 0; index < pmDataList.size(); index++) {
			List data = (List) pmDataList.get(index);
			String neOid = (String) data.get(PmConst.NEID_INDEX);
			Map<String, PoCounterThreshold> poTmap = CacheUtil.getPOThreshold(neOid, pmType);
			if (poTmap != null && poTmap.size() > 0) {
				Map<String, Object> datas = (Map<String, Object>) data.get(PmConst.PMDATA_INDEX);
				Set clumns = datas.keySet();
				for (Object clumn : clumns) {
					PoCounterThreshold pot = poTmap.get(pmType + ":" + clumn);
					if (pot != null) {
						ThresholdAlarmProcess.getInstanse()
								.handleThresholdAlarm(index, (Double) datas.get(clumn), data, pot);
						// dMsg.debug("count threshold " + pmType + ":" + i
						// + "value:" + data.get(i + PmConst.MOID_INDEX));
					}
				}
			}
		}

	}

}
