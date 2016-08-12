package org.openo.orchestrator.nfv.umc.fm.common;

import org.openo.orchestrator.nfv.umc.pm.adpt.fm.bean.FmAlarmData;

public interface IFmDataConsumer {
	String ROLE = IFmDataConsumer.class.getName();
	
	void consume(FmAlarmData alarmData);
}
