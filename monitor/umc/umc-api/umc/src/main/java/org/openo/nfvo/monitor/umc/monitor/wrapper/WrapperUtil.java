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
package org.openo.nfvo.monitor.umc.monitor.wrapper;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.openo.nfvo.monitor.umc.cache.CacheUtil;
import org.openo.nfvo.monitor.umc.cometdclient.DacCometdClient;
import org.openo.nfvo.monitor.umc.db.entity.MonitorInfo;
import org.openo.nfvo.monitor.umc.pm.common.DebugPrn;
import org.openo.nfvo.monitor.umc.pm.common.PmConst;
import org.openo.nfvo.monitor.umc.snmptrap.common.NeTrapInfo;
import org.openo.nfvo.monitor.umc.snmptrap.common.TrapInfo;

public class WrapperUtil {
	private static DebugPrn dMsg = new DebugPrn(WrapperUtil.class.getName());
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, String> convertParamInfo2Map(MonitorInfo paramInfo) {
    	Map<String, String> map = new HashMap<String, String>();
    	map.put(PmConst.IPADDRESS, paramInfo.getIpAddress());
    	map.put(PmConst.OID, paramInfo.getOid());
    	String proxyIp = PmConst.LOCAL_IPADDRESS;
    	Map<String, DacCometdClient> dacMap = DACServiceWrapper.getInstance().ip2CometdClientMap;
    	if(!dacMap.isEmpty()){
    		for(String dacIp:dacMap.keySet()){
    			proxyIp = dacIp;
    			break;
    		}
    	}
    	//map.put(PmConst.PROXYIP, PmConst.LOCAL_IPADDRESS);//TODO
    	map.put(PmConst.PROXYIP, proxyIp);
    	String customPara = paramInfo.getCustomPara();
        if (customPara != null && !customPara.equals("")) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map paraMap = objectMapper.readValue(customPara, Map.class);
                map.putAll(paraMap);
            } catch (Exception e) {
            	dMsg.warn(e.getMessage());
            }
        }
        return map;
    }
    
    public static void cacheTrapInfo(MonitorInfo paramInfo, Map<String, String> infoMap)
    {
    	TrapInfo trapInfo = CacheUtil.getTrapInfoByNeTypeId(paramInfo.getNeTypeId());
    	if (trapInfo != null)
    	{
    		NeTrapInfo neTrapInfo = new NeTrapInfo();
    		neTrapInfo.setTrapInfo(trapInfo);
    		neTrapInfo.setNeId(paramInfo.getOid());
    		neTrapInfo.setNeTypeId(paramInfo.getNeTypeId());
    		String[] keyIds = trapInfo.getKeyId();
    		for (String keyId : keyIds)
    		{
    			String value = infoMap.get(keyId);
    			if (value != null)
    			{
    				CacheUtil.cacheNeTrapInfo(value, neTrapInfo);
    			}
    		}
    	}
    }
    
    public static void removeTrapInfo(String neId)
    {
		CacheUtil.removeNeTrapInfo(neId);
    }
}
