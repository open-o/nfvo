package org.openo.orchestrator.nfv.umc.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.openo.orchestrator.nfv.umc.db.entity.PmTaskThreshold;
import org.openo.orchestrator.nfv.umc.pm.common.DebugPrn;
import org.openo.orchestrator.nfv.umc.pm.common.PmException;
import org.openo.orchestrator.nfv.umc.pm.db.process.PmDBProcess;
import org.openo.orchestrator.nfv.umc.pm.task.bean.PmTaskInfo;
import org.openo.orchestrator.nfv.umc.pm.task.bean.PoCounterInfo;
import org.openo.orchestrator.nfv.umc.pm.task.bean.PoCounterThreshold;
import org.openo.orchestrator.nfv.umc.snmptrap.common.NeTrapInfo;
import org.openo.orchestrator.nfv.umc.snmptrap.common.TrapInfo;


public class CacheUtil {
    private static DebugPrn dMsg = new DebugPrn(CacheUtil.class.getName());

    // moTypeId-->pmType
    private static Map<String, String> pmTypeMOTypeIdMap;
    // pmType-->[counter1,counter2...]
    private static Map<String, List<PoCounterInfo>> pmTypeCountersMap;
    // counterId-->counter(POCounterInfo)
    private static Map<String, PoCounterInfo> conterId2InfoMap =
            new HashMap<String, PoCounterInfo>();
    // pmType-->dataTableName
    private static Map<String, String> pmTypeDataTableNameMap;
    // neTypeId-->[counterThreshold1,counterThreshold2...]
    private static Map<String, List<PoCounterThreshold>> pmCountThresholdMap;
    // neId-->[poThreshold1,poThreshold2...]
    private static Map<String, Map<String, Map<String, PoCounterThreshold>>> neIdPOThresholdMap;

    // taskId-->PmTaskInfo
    private static Map<String, PmTaskInfo> pmTaskInfoMap;
    // mocId-->PmTaskInfo
    private static Map<String, PmTaskInfo> pmTaskInfoMocIdMap;
    // neTypeId-->List<PmTaskInfo>
    private static Map<String, List<PmTaskInfo>> pmTaskInfosNeTypeIdMap;
    // poId-->montiorName
    private static Map<String, String> PoId2MonitorNameMap;
    
    // neTypeId-->trapInfo
    private static Map<String, TrapInfo> neTypeId2TrapInfoMap;
    
    // keyId-->NeTrapInfo
    private static Map<String, Vector<NeTrapInfo>> keyId2NeTrapInfoMap = new HashMap<String, Vector<NeTrapInfo>>();
    
    private static Map<String, Vector<String>> neId2KeyIds = new HashMap<String, Vector<String>>();

    @SuppressWarnings("unchecked")
    public static void buildCache() {

        CacheBuilder builder = new CacheBuilder();
        List<Map<String, ?>> cacheList = builder.buildCache();
        pmTypeMOTypeIdMap = Collections.unmodifiableMap(
                (Map<String, String>) cacheList.get(CacheBuilder.PMTYPEMOTYPEIDMAP_INDEX));
        
        pmTypeCountersMap = Collections.unmodifiableMap((Map<String, List<PoCounterInfo>>) cacheList
                .get(CacheBuilder.PMTYPECOUNTERCLUMNMAP_INDEX));
        for (String pmType : pmTypeCountersMap.keySet()) {
            List<PoCounterInfo> counterList = pmTypeCountersMap.get(pmType);
            for (PoCounterInfo counterInfo : counterList) {
                conterId2InfoMap.put(getCounterId(pmType, counterInfo.getAttrId()), counterInfo);
            }
        }
        
        pmTypeDataTableNameMap = Collections.unmodifiableMap(
                (Map<String, String>) cacheList.get(CacheBuilder.PMTYPEDATATABLENAMEMAP_INDEX));
        pmCountThresholdMap =
                Collections.unmodifiableMap((Map<String, List<PoCounterThreshold>>) cacheList
                        .get(CacheBuilder.PMCOUNTERTHRESHOLDMAP_INDEX));
        neIdPOThresholdMap = cachePOCounterThreshold();

        pmTaskInfoMap = Collections.unmodifiableMap(
                (Map<String, PmTaskInfo>) cacheList.get(CacheBuilder.PMTASKINFOMAP_INDEX));
        pmTaskInfoMocIdMap = Collections.unmodifiableMap(
            (Map<String, PmTaskInfo>) cacheList.get(CacheBuilder.PMTASKINFOMAP_MOCID_INDEX));
        pmTaskInfosNeTypeIdMap = Collections.unmodifiableMap(
            (Map<String, List<PmTaskInfo>>) cacheList.get(CacheBuilder.PMTASKINFOMAP_NETYPEID_INDEX));
        PoId2MonitorNameMap = Collections.unmodifiableMap(
            (Map<String, String>) cacheList.get(CacheBuilder.POID2MONITORNAME_INDEX));
        neTypeId2TrapInfoMap = Collections.unmodifiableMap(
                (Map<String, TrapInfo>) cacheList.get(CacheBuilder.NEID2TRAPINFOMAP_INDEX));
    }

    public static String getCounterId(String pmType, int attrId) {
        return "C" + pmType + String.format("%04d", attrId);
    }

    private static Map<String, Map<String, Map<String, PoCounterThreshold>>> cachePOCounterThreshold() {
        Map<String, Map<String, Map<String, PoCounterThreshold>>> neIdKeyMap =
                new HashMap<String, Map<String, Map<String, PoCounterThreshold>>>();

        try {
            List<PmTaskThreshold> itThresholdInfos = PmDBProcess.queryThreshold();
            for (PmTaskThreshold itThresholdInfo : itThresholdInfos) {
                String oid = itThresholdInfo.getOid();
                Map<String, Map<String, PoCounterThreshold>> poIdKeyMap = neIdKeyMap.get(oid);
                if (poIdKeyMap == null) {
                    poIdKeyMap = new HashMap<String, Map<String, PoCounterThreshold>>();
                    neIdKeyMap.put(oid, poIdKeyMap);
                }
                String poId = itThresholdInfo.getPoid();
                Map<String, PoCounterThreshold> thresholdMap = poIdKeyMap.get(poId);
                if (thresholdMap == null) {
                    thresholdMap = new HashMap<String, PoCounterThreshold>();
                    poIdKeyMap.put(poId, thresholdMap);
                }

                PoCounterThreshold threshold = new PoCounterThreshold();
                threshold.setNeId(oid);
                threshold.setPmType(poId);
                threshold.setNeTypeId(itThresholdInfo.getNeTypeId());
                threshold.setAttrId(itThresholdInfo.getPoattributeId());
                threshold.setAttrName(itThresholdInfo.getPoattributeName());
                threshold.setAlarmCode(itThresholdInfo.getAlarmCode());
                threshold.setIsAlarm(itThresholdInfo.getIsAlarm());
                threshold.setDirection(itThresholdInfo.getDirect());
                threshold.setCritical(itThresholdInfo.getCritial());
                threshold.setMajor(itThresholdInfo.getMajor());
                threshold.setMinor(itThresholdInfo.getMinor());
                threshold.setWarning(itThresholdInfo.getWarning());
                thresholdMap.put(
                        CacheUtil.getThresholdID(threshold.getPmType(), threshold.getAttrId()),
                        threshold);

            }
        } catch (PmException e) {
            dMsg.warn("query counter threshold failed!", e);
        }
        return neIdKeyMap;
    }

    /**
     * Query counter information by performance type id.
     * 
     * @param pmType performance type id
     * @return counter information
     */
    public static List<PoCounterInfo> getCountersByPMType(String pmType) {
        if (pmTypeCountersMap != null) {
            return pmTypeCountersMap.get(pmType);
        }
        return null;
    }

    /**
     * Query performance type id by performance measure id.
     * 
     * @param moType performance measure id
     * @return performance type id
     */
    public static String getPmTypeIdByMoTypeId(String moType) {
        if (pmTypeMOTypeIdMap != null) {
            return pmTypeMOTypeIdMap.get(moType);
        }
        return null;
    }


    /**
     * @param pmType
     * @return
     */
    public static String getDataTableNameByPMType(String pmType) {
        if (pmTypeDataTableNameMap != null) {
            return pmTypeDataTableNameMap.get(pmType);
        }
        return null;
    }

    public static String getAttrClmnsByID(String pmType, int attrId) {
        if (pmTypeCountersMap != null) {
            List<PoCounterInfo> poInfos = pmTypeCountersMap.get(pmType);
            for (PoCounterInfo poInfo : poInfos) {
                if (attrId == poInfo.getAttrId()) {
                    return poInfo.getAttrClmn();
                }
            }
        }
        return null;
    }

    public static PoCounterInfo getCounterInfoById(String counterId) {
        return conterId2InfoMap.get(counterId);
    }

    public static String getPMTypeByDataTableName(String tableName) {
        if (pmTypeDataTableNameMap != null) {
            Set<Entry<String, String>> entrySet = pmTypeDataTableNameMap.entrySet();
            for (Entry<String, String> entry : entrySet) {
                if (entry.getValue().equalsIgnoreCase(tableName)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public static String extractPmTypeFromIndexId(String indexId) {
        return indexId.substring(1, indexId.length() - 4);
    }

    public static int extractCounterId(String indexId) {
        return Integer.parseInt(indexId.substring(indexId.length() - 4));
    }

    public static Map<String, String> getPmTypeMOTypeIdMap() {
        return pmTypeMOTypeIdMap;
    }

    public static Map<String, List<PoCounterInfo>> getPmTypeCountersMap() {
        return pmTypeCountersMap;
    }

    static void setPmTypeMOTypeIdMap(Map<String, String> pmTypeMOTypeIdMap) {
        CacheUtil.pmTypeMOTypeIdMap = pmTypeMOTypeIdMap;
    }

    static void setPmTypeCountersMap(Map<String, List<PoCounterInfo>> pmTypeCountersMap) {
        CacheUtil.pmTypeCountersMap = pmTypeCountersMap;
    }

    public static Map<String, String> getPmTypeDataTableNameMap() {
        return pmTypeDataTableNameMap;
    }

    static void setPmTypeDataTableNameMap(Map<String, String> pmTypeDataTableNameMap) {
        CacheUtil.pmTypeDataTableNameMap = pmTypeDataTableNameMap;
    }

    public static void cachePOThreshold(String neId, String poId, PoCounterThreshold threshold) {
        Map<String, Map<String, PoCounterThreshold>> map = neIdPOThresholdMap.get(neId);
        if (map == null) {
            map = new HashMap<String, Map<String, PoCounterThreshold>>();
            neIdPOThresholdMap.put(neId, map);
        }
        Map<String, PoCounterThreshold> cMap = map.get(poId);
        if (cMap == null) {
            cMap = new HashMap<String, PoCounterThreshold>();
            map.put(poId, cMap);
        }
        String key = CacheUtil.getThresholdID(threshold.getPmType(), threshold.getAttrId());
        cMap.put(key, threshold);

    }

    public static String getThresholdID(String poId, int attributeId) {
        StringBuilder buf = new StringBuilder(poId);
        buf.append(":");
        buf.append(getAttrClmnsByID(poId, attributeId));
        return buf.toString();

    }

    public static void removePOThreshold(String neId) {
        neIdPOThresholdMap.remove(neId);

    }

    public static Map<String, PoCounterThreshold> getPOThreshold(String neId, String poId) {
        Map<String, Map<String, PoCounterThreshold>> map = neIdPOThresholdMap.get(neId);
        if (map != null) {
            return map.get(poId);
        } else {
            return new HashMap<String, PoCounterThreshold>();
        }
    }

    public static List<PoCounterThreshold> getCounterThresholdByNeTypeId(String neTypeId) {
        return pmCountThresholdMap.get(neTypeId);
    }
    
    public static PmTaskInfo getPmTaskInfoByTaskId(String taskId){
        return pmTaskInfoMap.get(taskId);
    }
    
    public static PmTaskInfo getPmTaskInfoByMocId(String mocId){
        return pmTaskInfoMocIdMap.get(mocId);
    }
    
    public static List<PmTaskInfo> getPmTaskInfosByNeTypeId(String neTypeId){
        return pmTaskInfosNeTypeIdMap.get(neTypeId);
    }

    public static Collection<PmTaskInfo> getAllPmTaskInfo() {
        return pmTaskInfoMap.values();
    }
    
    public static String getMonitorNameByPOID(String poId) {
        return PoId2MonitorNameMap.get(poId);
    }
    
    public static TrapInfo getTrapInfoByNeTypeId(String neTypeId)
    {
    	return neTypeId2TrapInfoMap.get(neTypeId);
    }
    
    public static void cacheNeTrapInfo(String keyId, NeTrapInfo neTrapInfo)
    {
    	Vector<NeTrapInfo> neTrapInfos = keyId2NeTrapInfoMap.get(keyId);
    	if (neTrapInfos == null)
    	{
    		neTrapInfos = new Vector<NeTrapInfo>();
    		keyId2NeTrapInfoMap.put(keyId, neTrapInfos);
    	}
    	neTrapInfos.add(neTrapInfo);
    	
    	String neId = neTrapInfo.getNeId();
    	Vector<String> keyIds = neId2KeyIds.get(neId);
    	if (keyIds == null)
    	{
    		keyIds = new Vector<String>();
    		neId2KeyIds.put(neId, keyIds);
    	}
    	keyIds.add(keyId);
    }
    
    public static Vector<NeTrapInfo> getNeTrapInfo(String keyId)
    {
    	return keyId2NeTrapInfoMap.get(keyId);
    }
    
    public static void removeNeTrapInfo(String neId)
    {
    	Vector<String> keyIds = neId2KeyIds.get(neId);
    	if (keyIds != null)
    	{
    		for (String keyId : keyIds)
    		{
    			Vector<NeTrapInfo> neTrapInfos = getNeTrapInfo(keyId);
    			if (neTrapInfos != null)
    			{
	    			for (NeTrapInfo neTrapInfo : neTrapInfos)
	    			{
	    				if (neTrapInfo.getNeId().equals(neId))
	    				{
	    					neTrapInfos.remove(neTrapInfo);
	    					break;
	    				}
	    			}
    			}
    		}
    	}
    }
}
