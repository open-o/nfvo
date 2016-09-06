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
package org.openo.orchestrator.nfv.umc.pm.task.threshold;

import java.util.Date;
import java.util.List;

import org.openo.orchestrator.nfv.umc.fm.common.FMConsts;
import org.openo.orchestrator.nfv.umc.fm.service.FmService;
import org.openo.orchestrator.nfv.umc.pm.adpt.fm.bean.FmAlarmData;
import org.openo.orchestrator.nfv.umc.pm.adpt.roc.RocAdptImpl;
import org.openo.orchestrator.nfv.umc.pm.common.DebugPrn;
import org.openo.orchestrator.nfv.umc.pm.common.PmConst;
import org.openo.orchestrator.nfv.umc.pm.common.PmException;
import org.openo.orchestrator.nfv.umc.pm.task.PmTaskException;
import org.openo.orchestrator.nfv.umc.pm.task.bean.PoCounterThreshold;

/**
 * Performance Threshold Alarm Processor.
 */
public class ThresholdAlarmProcess {

    private static DebugPrn dMsg = new DebugPrn(ThresholdAlarmProcess.class.getName());
    private static ThresholdAlarmProcess instanse = null;

    public static ThresholdAlarmProcess getInstanse() {
        if (instanse == null) {
            instanse = new ThresholdAlarmProcess();
        }
        return instanse;
    }

    private ThresholdAlarmProcess() {}

    /**
     * Performance Threshold Alarm Calculating.
     * 
     * @param index
     * @param indexValue
     * @param data
     * @param thresholdInfo
     */
    public void handleThresholdAlarm(int index, double indexValue, List data,
            PoCounterThreshold thresholdInfo) {
        byte serverity = 0;
        byte eventType = PmConst.EVENTTYPE_ALARM;
        int alarmLevel = getAlarmLevel(indexValue, thresholdInfo);
        if (alarmLevel == PmConst.ALARM_RESTORE) {
            eventType = PmConst.EVENTTYPE_RESTORE;
        } else {
            serverity = (byte) alarmLevel;
        }
        String oid = (String) data.get(PmConst.NEID_INDEX);
        String alarmID = oid + "@" + index;

        FmAlarmData alarmData = new FmAlarmData();
        alarmData.setOid(oid);
        alarmData.setCode(thresholdInfo.getAlarmCode());
        alarmData.setAlarmKey(getAlarmKey(alarmID, thresholdInfo));
        alarmData.setEventType(eventType);
        alarmData.setAlarmRaiseTime(new Date());
        alarmData.setServerity(serverity);
        alarmData.setDetailInfo(buildDetailInfo(indexValue, thresholdInfo));
        alarmData.setSystemType(FMConsts.ALARMTYPE_THRESHOLD);
        alarmData.setMoc(thresholdInfo.getNeTypeId());

        try {
            alarmData.setDevIp(String.valueOf(
                    RocAdptImpl.getCommPara(oid, thresholdInfo.getNeTypeId()).get(PmConst.IPADDRESS)));
        } catch (PmTaskException e) {}

        try {
            FmService.send(alarmData);
        } catch (PmException e) {
            dMsg.warn("send threshold alarm failed!", e);
        }
    }

    private String getAlarmKey(String alarmID, PoCounterThreshold thresholdInfo) {
        StringBuilder tmp = new StringBuilder("ID=");
        tmp.append(alarmID);
        tmp.append("@Index=");
        tmp.append(thresholdInfo.getPmType());
        tmp.append(":");
        tmp.append(thresholdInfo.getAttrId());
        return tmp.toString();
    }

    private String buildDetailInfo(double value, PoCounterThreshold thresholdInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("i18n.fm.currentalarm.additionaltext.key").append("|").append(thresholdInfo.getAttrName())
                .append("|").append(String.valueOf(value));

        int direct = thresholdInfo.getDirection();
        if (direct == PmConst.ALARM_DIRECT_DOWN) {
            sb.append("|").append("i18n.fm.common.word.down");
        } else {
            sb.append("|").append("i18n.fm.common.word.up");
        }
        sb.append("|").append(String.valueOf(thresholdInfo.getWarning())).append("|")
                .append(String.valueOf(thresholdInfo.getMinor())).append("|")
                .append(String.valueOf(thresholdInfo.getMajor())).append("|")
                .append(String.valueOf(thresholdInfo.getCritical()));

        return sb.toString();
    }

    private int getAlarmLevel(double indexValue, PoCounterThreshold thresholdInfo) {
        int direct = thresholdInfo.getDirection();
        double warningTH = thresholdInfo.getWarning();
        double minorTH = thresholdInfo.getMinor();
        double majorTH = thresholdInfo.getMajor();
        double criticalTH = thresholdInfo.getCritical();
        if (direct == PmConst.ALARM_DIRECT_UP) {
            if (indexValue < warningTH) {
                return 0;
            }
            if (indexValue <= minorTH) {
                return 4;
            }
            if (indexValue <= majorTH) {
                return 3;
            }
            if (indexValue <= criticalTH) {
                return 2;
            }
            return 1;
        } else {
            if (indexValue > warningTH) {
                return 0;
            }
            if (indexValue >= minorTH) {
                return 4;
            }
            if (indexValue >= majorTH) {
                return 3;
            }
            if (indexValue >= criticalTH) {
                return 2;
            }
            return 1;
        }
    }
}
