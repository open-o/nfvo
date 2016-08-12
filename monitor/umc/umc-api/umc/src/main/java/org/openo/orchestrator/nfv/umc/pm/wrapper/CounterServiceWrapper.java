package org.openo.orchestrator.nfv.umc.pm.wrapper;

import java.util.List;

import org.openo.orchestrator.nfv.umc.cache.CacheUtil;
import org.openo.orchestrator.nfv.umc.pm.bean.PmCounter;
import org.openo.orchestrator.nfv.umc.pm.common.PmConst;
import org.openo.orchestrator.nfv.umc.pm.common.PmOsfUtil;
import org.openo.orchestrator.nfv.umc.pm.task.bean.PoCounterInfo;

public class CounterServiceWrapper {
	public static PmCounter[] queryCounters(String resourceTypeId, String moTypeId, String language) {
		String pmType = CacheUtil.getPmTypeIdByMoTypeId(moTypeId);
        List<PoCounterInfo> counterList = CacheUtil.getCountersByPMType(pmType);
        if (counterList == null || counterList.size() == 0) {
            return new PmCounter[0];
        }
        
        PmCounter[] counterArray = new PmCounter[counterList.size()];
        for (int i=0; i<counterList.size(); i++) {
            counterList.get(i).translate(language);
            counterArray[i] = convert2PmCounter(pmType, counterList.get(i));
            
        }
        return counterArray;
	}

    private static PmCounter convert2PmCounter(String pmType, PoCounterInfo poCounterInfo) {
        return new PmCounter(CacheUtil.getCounterId(pmType, poCounterInfo.getAttrId()),
                poCounterInfo.getAttrNameI18n(),
                buildDataType(poCounterInfo.getAttrDataType()));
    }

    private static String buildDataType(String attrDataType) {
    	String dataType = PmOsfUtil.getJavaDataTyeOfDbFieldType(attrDataType);
        if (dataType.equals(PmConst.JAVA_LANG_CLASS_STRING)) {
            return "STRING";
        }
        
        if (dataType.equals(PmConst.JAVA_LANG_CLASS_INTEGER)) {
            return "INT";
        }
        
        if (dataType.equals(PmConst.JAVA_LANG_CLASS_FLOAT)) {
            return "FLOAT";
        }

        if (dataType.equals(PmConst.JAVA_LANG_CLASS_DOUBLE)) {
            return "DOUBLE";
        }
        
        if (dataType.equals(PmConst.JAVA_LANG_CLASS_LONG)) {
            return "LONG";
        }
        
        if (dataType.equals(PmConst.JAVA_UTIL_CLASS_DATE)) {
            return "DATE";
        }
        
        if (dataType.equals("7") || dataType.equals("8")) {
            return "PERCENT";
        }
        
        return "STRING";
    }

}
