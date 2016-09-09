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
package org.openo.nfvo.monitor.umc.fm.db;

import org.openo.nfvo.monitor.umc.fm.common.FMConsts;
import org.openo.nfvo.monitor.umc.fm.common.IFmDataConsumer;
import org.openo.nfvo.monitor.umc.fm.db.process.FmDBProcess;
import org.openo.nfvo.monitor.umc.pm.adpt.fm.bean.FmAlarmData;
import org.openo.nfvo.monitor.umc.pm.common.PmConst;

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
