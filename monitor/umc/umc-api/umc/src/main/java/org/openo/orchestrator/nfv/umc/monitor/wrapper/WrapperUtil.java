package org.openo.orchestrator.nfv.umc.monitor.wrapper;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.openo.orchestrator.nfv.umc.cache.CacheUtil;
import org.openo.orchestrator.nfv.umc.db.entity.MonitorInfo;
import org.openo.orchestrator.nfv.umc.pm.common.DebugPrn;
import org.openo.orchestrator.nfv.umc.pm.common.PmConst;
import org.openo.orchestrator.nfv.umc.snmptrap.common.NeTrapInfo;
import org.openo.orchestrator.nfv.umc.snmptrap.common.TrapInfo;

public class WrapperUtil {
	private static DebugPrn dMsg = new DebugPrn(WrapperUtil.class.getName());
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, String> convertParamInfo2Map(MonitorInfo paramInfo) {
    	Map<String, String> map = new HashMap<String, String>();
    	map.put(PmConst.IPADDRESS, paramInfo.getIpAddress());
    	map.put(PmConst.OID, paramInfo.getOid());
    	map.put(PmConst.PROXYIP, PmConst.LOCAL_IPADDRESS);
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
