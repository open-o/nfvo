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
package org.openo.orchestrator.nfv.umc.snmptrap.processor;

import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import org.openo.orchestrator.nfv.umc.cache.CacheUtil;
import org.openo.orchestrator.nfv.umc.fm.adpt.roc.common.RocConst;
import org.openo.orchestrator.nfv.umc.fm.common.FMConsts;
import org.openo.orchestrator.nfv.umc.fm.service.FmService;
import org.openo.orchestrator.nfv.umc.pm.adpt.fm.bean.FmAlarmData;
import org.openo.orchestrator.nfv.umc.pm.adpt.roc.RocAdptImpl;
import org.openo.orchestrator.nfv.umc.pm.common.PmException;
import org.openo.orchestrator.nfv.umc.pm.task.PmTaskException;
import org.openo.orchestrator.nfv.umc.snmptrap.common.NeTrapInfo;
import org.openo.orchestrator.nfv.umc.snmptrap.common.TrapMsgData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class TrapMsgHandleThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrapMsgHandleThread.class);
    private TrapMsgData trapMsgData = null;

    public TrapMsgHandleThread(TrapMsgData trapMsgData) {
        this.trapMsgData = trapMsgData;

    }

    @Override
    public void run() {
        if (trapMsgData != null) {
        	int systemType = trapMsgData.getSystemType();
        	switch (systemType)
        	{
        	case FMConsts.ALARMTYPE_TRAP:
        		processTrapAlarm();
        		break;
        	case FMConsts.ALARMTYPE_STATUS:
        		processStatusAlarm();
        		break;
        	default:
        		break;
        	}
        }
    }

    private Properties analysisDetailInfo(String detailInfo) {
        Properties props = new Properties();
        String[] keyvalue = detailInfo.split(",");
        for (int i = 0; i < keyvalue.length; i++) {
            String[] map = keyvalue[i].split("=");
            if(map.length>1){
                props.put(map[0], map[1]);
            }
        }
        return props;
    }

    private void processTrapAlarm()
    {
        FmAlarmData alarmData = new FmAlarmData();

        alarmData.setCode(trapMsgData.getAlarmCode());
        alarmData.setEventType(trapMsgData.getEventType());
        alarmData.setAlarmRaiseTime(trapMsgData.getAlarmRaiseTime());
        alarmData.setServerity(trapMsgData.getSeverityLevel());
        alarmData.setSystemType(trapMsgData.getSystemType());
        alarmData.setAlarmKey( trapMsgData.getEntity());
        alarmData.setDevIp(trapMsgData.getIpAddress());
        alarmData.setDetailInfo(buildDetailInfo(trapMsgData.getBindValues()));
        sendTrapAlarm(alarmData);
        processVimAlarm(alarmData);
    }

    private void processStatusAlarm()
    {
    	FmAlarmData alarmData = new FmAlarmData();
    	 alarmData.setCode(trapMsgData.getAlarmCode());
    	 alarmData.setAlarmKey(trapMsgData.getEntity());
    	 alarmData.setAlarmRaiseTime(trapMsgData.getAlarmRaiseTime());
    	 alarmData.setOid(trapMsgData.getNeId());
    	 alarmData.setEventType(trapMsgData.getEventType());
    	 alarmData.setServerity(trapMsgData.getSeverityLevel());
    	 alarmData.setSystemType(trapMsgData.getSystemType());
    	 alarmData.setDevIp(trapMsgData.getIpAddress());
    	 alarmData.setMoc(trapMsgData.getNeType());
    	 alarmData.setDetailInfo("");
         try {
             FmService.send(alarmData);
         } catch (PmException e) {
             LOGGER.warn("send status alarm failed!", e);
         }
    }

    private String buildDetailInfo(Properties props)
    {
    	if (props != null && props.size() != 0)
    	{
	    	StringBuilder tmp = new StringBuilder();
	    	Set keys = props.keySet();
	    	for (Object key : keys)
	    	{
	    		String value = props.getProperty(key.toString());
	    		tmp.append(key).append("=").append(value).append(",");
	    	}
	    	tmp.deleteCharAt(tmp.length() - 1);
	    	return tmp.toString();
    	}
    	else
    	{
    		return "";
    	}
    }

    private void sendTrapAlarm(FmAlarmData alarmData)
    {
    	String keyId = trapMsgData.getEventKey();
    	Vector<NeTrapInfo> neTrapInfos = CacheUtil.getNeTrapInfo(keyId);
    	if (neTrapInfos != null)
    	{
	    	for (NeTrapInfo neTrapInfo : neTrapInfos)
	    	{
	    		if (neTrapInfo.isTrapSupport(trapMsgData.getTrapOid()))
	    		{
	    			alarmData.setMoc(neTrapInfo.getNeTypeId());
	    			alarmData.setOid(neTrapInfo.getNeId());
	    			setAlarmKey(alarmData);
	                try {
	                    FmService.send(alarmData);
	                } catch (PmException e) {
	                    LOGGER.warn("send trap alarm failed!", e);
	                }
	    		}
	    	}
    	}
    }

    private void setAlarmKey(FmAlarmData alarmData)
    {
    	StringBuilder tmp = new StringBuilder();
    	tmp.append(alarmData.getOid()).append("@");
    	tmp.append(alarmData.getAlarmKey());
    	alarmData.setAlarmKey(tmp.toString());
    }

	private void processVimAlarm(FmAlarmData alarmData) {
		if (trapMsgData.getTrapOid().indexOf("1.3.6.1.4.1.3902.4101.1.4.1") != -1) {

            Properties props = trapMsgData.getBindValues();
            String detailInfo = props.getProperty("alarmAdditionalText");
            alarmData.setDetailInfo(detailInfo);
            Properties detailProps = analysisDetailInfo(detailInfo);

            String objectInstance = (String) props.get("alarmManagedObjectInstance");
            if (objectInstance.substring(1,objectInstance.length()-1).equals("host")) {
                String oid = detailProps.getProperty("hid");
                String neType = RocConst.NETYPE_HOST;
                alarmData.setOid(oid);
                alarmData.setMoc(neType);

                try{
                    Properties rocProps = RocAdptImpl.getCommPara(oid, neType);
                    alarmData.setDevIp(rocProps.getProperty("ipaddress"));
                }catch(PmTaskException e){
                    alarmData.setDevIp(trapMsgData.getIpAddress());
                }
            } else if (objectInstance.substring(1,objectInstance.length()-1).equals("vm")) {
                String oid = detailProps.getProperty("hid");
                String neType = RocConst.NETYPE_VDU;
                alarmData.setOid(oid);
                alarmData.setMoc(neType);

                try{
                    Properties rocProps = RocAdptImpl.getCommPara(oid, neType);
                    alarmData.setDevIp(rocProps.getProperty("ipaddress"));
                }catch(PmTaskException e){
                    alarmData.setDevIp(trapMsgData.getIpAddress());
                }

            } else {
                // equals cloud,server,storage,switch,chassis
            	String ipaddress = trapMsgData.getIpAddress();
                String oid = detailProps.getProperty("vimid");
                alarmData.setOid(oid);
                alarmData.setMoc(RocConst.NETYPE_OPENSTACK);
                alarmData.setDevIp(ipaddress);

            }
            try {
                FmService.send(alarmData);
            } catch (PmException e) {
                LOGGER.warn("send trap alarm failed!", e);
            }
        }
	}
}
