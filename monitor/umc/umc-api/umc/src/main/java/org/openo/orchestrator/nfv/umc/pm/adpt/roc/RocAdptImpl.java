package org.openo.orchestrator.nfv.umc.pm.adpt.roc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openo.orchestrator.nfv.umc.db.UmcDbUtil;
import org.openo.orchestrator.nfv.umc.db.dao.MonitorInfoDao;
import org.openo.orchestrator.nfv.umc.db.entity.MonitorInfo;
import org.openo.orchestrator.nfv.umc.pm.adpt.roc.entity.ResourceEntity;
import org.openo.orchestrator.nfv.umc.pm.adpt.roc.service.ResourceServiceConsumer;
import org.openo.orchestrator.nfv.umc.pm.bean.Resource;
import org.openo.orchestrator.nfv.umc.pm.common.DebugPrn;
import org.openo.orchestrator.nfv.umc.pm.common.PmConst;
import org.openo.orchestrator.nfv.umc.pm.common.RestRequestException;
import org.openo.orchestrator.nfv.umc.pm.task.PmTaskException;

public class RocAdptImpl {
	private static DebugPrn dMsg = new DebugPrn(RocAdptImpl.class.getName());
		
	@SuppressWarnings("rawtypes")
	public static String getResourceName(String oid, String neType)
	{
		ResourceServiceConsumer rocService = new ResourceServiceConsumer(neType);
		try {
			Map neInfoMap = rocService.getNeInfoMap(oid);
			return (String)neInfoMap.get("name");
		} catch (Exception e) {
			dMsg.warn("getResourceName fail! " + e.getMessage());
			
		}
		return oid;
	}
	
	/**
	 * get ne customPara from roc
	 * @param oid
	 * @param neType
	 * @return
	 * @throws PmTaskException
	 */
	@SuppressWarnings("rawtypes")
	public static Properties getCommPara(String oid, String neType) throws PmTaskException
	{
		Properties p = new Properties();
		p.put(PmConst.MOC, neType);
		if (RocConfiguration.getRocServerAddr() != null) {
			ResourceServiceConsumer rocService = new ResourceServiceConsumer(
					neType);
			try {
				Map neInfoMap = rocService.getNeInfoMap(oid);

				if (neInfoMap != null && neInfoMap.size() > 0) {
					p.put(PmConst.RESID, neInfoMap.get(PmConst.oid));
					p.put(PmConst.IPADDRESS, neInfoMap.get(PmConst.ipAddress));
					if (neInfoMap.get(PmConst.customPara) != null) {
						p.put(PmConst.CUSTOMPARA,
								neInfoMap.get(PmConst.customPara));
					}
				} else {
					// Get monitor param info from table.
					MonitorInfoDao dao = (MonitorInfoDao) UmcDbUtil
							.getDao(PmConst.MONITOR_INFO);
					MonitorInfo itPmMonitorInfo = dao.queryByOid(oid);

					p.put(PmConst.RESID, itPmMonitorInfo.getOid());
					p.put(PmConst.IPADDRESS, itPmMonitorInfo.getIpAddress());
					p.put(PmConst.CUSTOMPARA, itPmMonitorInfo.getCustomPara());
				}
			} catch (Exception e) {
				dMsg.warn("getCommPara fail! " + e.getMessage());
				throw new PmTaskException(e);
			}
		}else{
		    MonitorInfoDao dao = (MonitorInfoDao) UmcDbUtil.getDao(PmConst.MONITOR_INFO);
		    MonitorInfo itPmMonitorInfo = dao.queryByOid(oid);
		    
		    p.put(PmConst.RESID, itPmMonitorInfo.getOid());
		    p.put(PmConst.IPADDRESS, itPmMonitorInfo.getIpAddress());
		    p.put(PmConst.CUSTOMPARA, itPmMonitorInfo.getCustomPara());
		}
		
		return p;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String>getCustomParaMap(String customPara) throws JsonParseException, JsonMappingException, IOException{
		Map<String, String> paraMap = new HashMap<String, String>();
		
		if(customPara!=null && !"".equals(customPara)){
			ObjectMapper objMaper = new ObjectMapper();
			paraMap = objMaper.readValue(customPara, HashMap.class);
			if(paraMap.get(PmConst.PROXYIP)==null){
				paraMap.put(PmConst.PROXYIP, PmConst.LOCAL_IPADDRESS);
			}
		}
		return paraMap;
	}
	

	public static List<String> getNeTypeIds()
	{
		List<String> list = new ArrayList<String>();
		list.add(PmConst.VDU);
		list.add(PmConst.HOST);
		return list;
	}
	
	public static List<Resource> getResourcesByType(String resourceType) throws RestRequestException {
	    dMsg.info("getResourcesByType begin. resourceType = " + resourceType);
        ResourceServiceConsumer rocService = new ResourceServiceConsumer(resourceType);
        ResourceEntity[] resources = rocService.queryAllResource();   
        dMsg.info("getResourcesByType end. resourceType = " + resourceType);
        return convert2ResourceList(resources);

	}

    private static List<Resource> convert2ResourceList(ResourceEntity[] resources) {
        List<Resource> resourceList = new ArrayList<>();
        for (int i = 0; i < resources.length; i++) {
            resourceList.add(new Resource(resources[i].getOid(), resources[i].getName()));
        }
        return resourceList;
    }
}
