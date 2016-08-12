package org.openo.orchestrator.nfv.umc.fm.db;

import org.openo.orchestrator.nfv.umc.fm.common.FMConsts;
import org.openo.orchestrator.nfv.umc.fm.common.IFmDataConsumer;
import org.openo.orchestrator.nfv.umc.fm.db.process.FmDBProcess;
import org.openo.orchestrator.nfv.umc.pm.adpt.fm.bean.FmAlarmData;
import org.openo.orchestrator.nfv.umc.pm.common.PmConst;

public class FmDataToDB implements IFmDataConsumer {

	@Override
	public void consume(FmAlarmData alarmData) {
    	int systemType = alarmData.getSystemType();
    	if (systemType == FMConsts.ALARMTYPE_TRAP)
    	{
            if (alarmData.getEventType() == PmConst.EVENTTYPE_RESTORE) {
            	FmDBProcess.restoreCurAlarm(alarmData.getAlarmKey());  // alarm restore.
            } else {
            	FmDBProcess.insertCurrentAlarmData(alarmData);
            }
    	}

	}

}
