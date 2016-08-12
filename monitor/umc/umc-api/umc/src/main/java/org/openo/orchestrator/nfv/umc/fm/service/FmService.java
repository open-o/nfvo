package org.openo.orchestrator.nfv.umc.fm.service;

import org.openo.orchestrator.nfv.umc.fm.common.FMConsts;
import org.openo.orchestrator.nfv.umc.fm.common.IFmDataConsumer;
import org.openo.orchestrator.nfv.umc.fm.db.process.FmDBProcess;
import org.openo.orchestrator.nfv.umc.pm.adpt.fm.bean.FmAlarmData;
import org.openo.orchestrator.nfv.umc.pm.common.PmConst;
import org.openo.orchestrator.nfv.umc.pm.common.PmException;
import org.openo.orchestrator.nfv.umc.util.ExtensionAccess;


public class FmService {

    public static void send(FmAlarmData alarmData) throws PmException {

		Object[] fmDataConsumers = ExtensionAccess.getExtensions(IFmDataConsumer.class.getName(), FMConsts.CONSUMERID);		

		for (Object fmDataConsumer : fmDataConsumers) {			
			((IFmDataConsumer)fmDataConsumer).consume(alarmData);
		}
		
		saveAlarmDataToDB(alarmData);
    }
    
    private static void saveAlarmDataToDB(FmAlarmData alarmData)
    {
    	int systemType = alarmData.getSystemType();
    	if (systemType != FMConsts.ALARMTYPE_TRAP)
    	{
            if (alarmData.getEventType() == PmConst.EVENTTYPE_RESTORE) {
            	FmDBProcess.restoreCurAlarm(alarmData.getAlarmKey());  // alarm restore.
            } else {
            	FmDBProcess.insertCurrentAlarmData(alarmData);
            }
    	}
    }
}
