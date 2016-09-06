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
package org.openo.orchestrator.nfv.umc.fm.naf;

import java.util.HashMap;
import java.util.Map;

import org.openo.orchestrator.nfv.umc.cometdserver.CometdException;
import org.openo.orchestrator.nfv.umc.cometdserver.CometdService;
import org.openo.orchestrator.nfv.umc.db.entity.CurrentAlarm;
import org.openo.orchestrator.nfv.umc.fm.common.FMConsts;
import org.openo.orchestrator.nfv.umc.fm.common.IFmDataConsumer;
import org.openo.orchestrator.nfv.umc.fm.db.process.FmDBProcess;
import org.openo.orchestrator.nfv.umc.pm.adpt.fm.bean.FmAlarmData;
import org.openo.orchestrator.nfv.umc.pm.common.PmConst;
import org.openo.orchestrator.nfv.umc.util.UMCUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FmDataToNaf implements IFmDataConsumer {
	private static final Logger LOGGER = LoggerFactory.getLogger(FmDataToNaf.class);
	
	@Override
	public void consume(FmAlarmData alarmData) {

		if (needSendNafData(alarmData))
		{
			try {
				FmNafData nafData = createNafData(alarmData);
				CometdService.getInstance().publish(CometdService.FM_UPLOAD_CHANNEL, nafData);
			} catch (CometdException e) {
				LOGGER.warn(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * trap event and 
	 * @param alarmData
	 * @return
	 */
	private boolean needSendNafData(FmAlarmData alarmData) {
		int systemType = alarmData.getSystemType();
		if (systemType != FMConsts.ALARMTYPE_TRAP && 
				alarmData.getEventType() == PmConst.EVENTTYPE_RESTORE)
		{
	        CurrentAlarm existAlarm = FmDBProcess.queryCurAlarmByAlarmKey(alarmData.getAlarmKey());
	        if(existAlarm == null)
	        {
	        	return false; //restore event, no alarm need to restore
	        }
		}
		return true;
	}


	private FmNafData createNafData(FmAlarmData alarmData)
	{
		FmNafData nafData = new FmNafData();
		nafData.setEventCode(alarmData.getCode());
		
		nafData.setEventId(alarmData.getAlarmKey());
		nafData.setEventRaiseTime(alarmData.getAlarmRaiseTime().getTime());
		nafData.setEventServerity(alarmData.getServerity());
		nafData.setEventType(alarmData.getEventType());
		nafData.setNeId(alarmData.getOid());
		nafData.setNeIp(alarmData.getDevIp());
		nafData.setNeType(alarmData.getMoc());
		nafData.setEventSubType(FMConsts.DEVICEALARM);
		nafData.setEventSystemType(alarmData.getSystemType());
//        ProbableCauseCond queryCond = new ProbableCauseCond();
//        queryCond.setCode(alarmData.getCode());
//        List<ProbableCause> probableCauses = FmCacheProcess.queryProbableCause(queryCond);
//        if (probableCauses.size() != 0) {
//        	ProbableCause probableCause = probableCauses.get(0);
//        	nafData.setEventSubType(probableCause.getAlarmType());
//        	nafData.setEventName(UMCUtil.getI18nValue(probableCause.getCodeName()));
//        	nafData.setEventReason(UMCUtil.getI18nValue(probableCause.getReason()));
//        	nafData.setEventRepairAction(UMCUtil.getI18nValue(probableCause.getProposedRepairActions()));
//        }
        nafData.setEventDetailInfo(getDetailInfo(alarmData));
        return nafData;
	}
	
	private String getDetailInfo(FmAlarmData alarmData)
	{
		int systemType = alarmData.getSystemType();
		String detailInfo = alarmData.getDetailInfo();
		if (systemType == FMConsts.ALARMTYPE_THRESHOLD)
		{
	        String[] key_vals = detailInfo.split("\\|");
	        String template_key = key_vals[0];

	        String[] varWord = new String[] {"", "POATTRIBUTENAME", "VALUE", "DIRECT", "WARN", "MINOR",
	                "MAJOR", "CRITICAL"};
	        
	        Map<String, String> vals = new HashMap<String, String>();
	        for (int i = 1; i < key_vals.length; i++) {
	            if(i==1 || i==3){
	                vals.put(varWord[i], UMCUtil.getI18nInstance().translate(key_vals[i]));          
	            }else{
	                vals.put(varWord[i], key_vals[i]);
	            }
	        }
	        
	        detailInfo = UMCUtil.getI18nInstance().translate(template_key, vals);
		}
		return detailInfo;
	}

}
