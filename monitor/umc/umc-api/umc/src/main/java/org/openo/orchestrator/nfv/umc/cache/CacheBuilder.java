package org.openo.orchestrator.nfv.umc.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.orchestrator.nfv.umc.pm.common.DebugPrn;
import org.openo.orchestrator.nfv.umc.pm.task.bean.PmTaskInfo;
import org.openo.orchestrator.nfv.umc.pm.task.bean.PoCounterInfo;
import org.openo.orchestrator.nfv.umc.pm.task.bean.PoCounterThreshold;
import org.openo.orchestrator.nfv.umc.pm.task.bean.PoTypeInfo;
import org.openo.orchestrator.nfv.umc.snmptrap.common.TrapInfo;
import org.openo.orchestrator.nfv.umc.util.filescaner.FastFileSystem;
import org.yaml.snakeyaml.Yaml;

public class CacheBuilder {
    private static DebugPrn dMsg = new DebugPrn(CacheBuilder.class.getName());

    public final static int PMTYPEMOTYPEIDMAP_INDEX = 0;
    public final static int PMTYPECOUNTERCLUMNMAP_INDEX = 1;
    public final static int PMTYPEDATATABLENAMEMAP_INDEX = 2;
    public final static int PMCOUNTERTHRESHOLDMAP_INDEX = 3;
    public final static int PMTASKINFOMAP_INDEX = 4;
    public final static int PMTASKINFOMAP_MOCID_INDEX = 5;
    public final static int PMTASKINFOMAP_NETYPEID_INDEX = 6;
    public final static int POID2MONITORNAME_INDEX = 7;
    public final static int NEID2TRAPINFOMAP_INDEX = 8;
    public final static String MODEL_FILE_PATTERN = "*-model.yml";
    Map<String, String> pmTypeMOTypeIdMap = new HashMap<String, String>();
    Map<String, List<PoCounterInfo>> pmTypeCountersMap = new HashMap<String, List<PoCounterInfo>>();
    Map<String, String> pmTypeDataTableNameMap = new HashMap<String, String>();
    Map<String, List<PoCounterThreshold>> pmCountThresholdMap =
            new HashMap<String, List<PoCounterThreshold>>();
    Map<String, PmTaskInfo> pmTaskInfoMap = new HashMap<String, PmTaskInfo>();
    Map<String, PmTaskInfo> pmTaskInfoMocIdMap = new HashMap<String, PmTaskInfo>();
    Map<String, List<PmTaskInfo>> pmTaskInfoNeTypeIdMap = new HashMap<String, List<PmTaskInfo>>();
    Map<String, String> PoId2MonitorNameMap = new HashMap<String, String>();
    Map<String, TrapInfo> neId2TrapInfoMap = new HashMap<String, TrapInfo>();
    
    public List<Map<String, ? extends Object>> buildCache() {
        Yaml yaml = new Yaml();
        File[] files = FastFileSystem.getFiles( MODEL_FILE_PATTERN);
        dMsg.info("load model yml files, file count:" + files.length);
        for (File file : files) {
            dMsg.info("load model yml file:" + file.getName());
            try {
                NeModel modelConfig = yaml.loadAs(new FileInputStream(file), NeModel.class);
                
                PoTypeInfo[] poTypeInfos = modelConfig.getPoTypeInfo();
                cachePoTypeInfos(poTypeInfos);
                               
                PmTaskInfo[] pmTaskInfos = modelConfig.getPmTaskInfo();
                cachePmTaskInfos(modelConfig, pmTaskInfos);
                
                PoCounterThreshold[] poCounterThresholds = modelConfig.getPoCounterThreshold();
                if(poCounterThresholds!=null){
                    cachePoCounterThresholds(modelConfig.getNeTypeId(), poCounterThresholds);                    
                }
                
                TrapInfo neTrapInfo = modelConfig.getTrapInfo();
                if (neTrapInfo != null)
                {
                	neId2TrapInfoMap.put(modelConfig.getNeTypeId(), neTrapInfo);
                }
                
            } catch (FileNotFoundException e) {
                dMsg.warn("load model files failed!", e);
            }
        }

        List<Map<String, ? extends Object>> cacheList =
                new ArrayList<Map<String, ? extends Object>>();
        cacheList.add(PMTYPEMOTYPEIDMAP_INDEX, pmTypeMOTypeIdMap);
        cacheList.add(PMTYPECOUNTERCLUMNMAP_INDEX, pmTypeCountersMap);
        cacheList.add(PMTYPEDATATABLENAMEMAP_INDEX, pmTypeDataTableNameMap);
        cacheList.add(PMCOUNTERTHRESHOLDMAP_INDEX, pmCountThresholdMap);
        cacheList.add(PMTASKINFOMAP_INDEX, pmTaskInfoMap);
        cacheList.add(PMTASKINFOMAP_MOCID_INDEX, pmTaskInfoMocIdMap);
        cacheList.add(PMTASKINFOMAP_NETYPEID_INDEX, pmTaskInfoNeTypeIdMap);
        cacheList.add(POID2MONITORNAME_INDEX, PoId2MonitorNameMap);
        cacheList.add(NEID2TRAPINFOMAP_INDEX, neId2TrapInfoMap);
        return cacheList;
    }

    private void cachePmTaskInfos(NeModel modelConfig, PmTaskInfo[] pmTaskInfos) {
        for (PmTaskInfo pmTaskInfo : pmTaskInfos) {
        	pmTaskInfo.setTaskId(Integer.parseInt(pmTaskInfo.getPoId()));
        	pmTaskInfo.setNeTypeId(modelConfig.getNeTypeId());
        	pmTaskInfo.setNeTypeName(modelConfig.getNeTypeName());
            pmTaskInfoMap.put(String.valueOf(pmTaskInfo.getTaskId()), pmTaskInfo);
            pmTaskInfoMocIdMap.put(pmTaskInfo.getMocId(), pmTaskInfo);
            
            if(pmTaskInfoNeTypeIdMap.containsKey(modelConfig.getNeTypeId())){
                pmTaskInfoNeTypeIdMap.get(pmTaskInfo.getNeTypeId()).add(pmTaskInfo);
            }else{
                List<PmTaskInfo> list = new ArrayList<PmTaskInfo>();
                list.add(pmTaskInfo);
                pmTaskInfoNeTypeIdMap.put(pmTaskInfo.getNeTypeId(), list);
            }
        }

    }

    private void cachePoTypeInfos(PoTypeInfo[] poTypeInfos) {
        for (PoTypeInfo poTypeInfo : poTypeInfos) {
            pmTypeMOTypeIdMap.put(poTypeInfo.getMoTypeId(), poTypeInfo.getPmType());
            pmTypeDataTableNameMap.put(poTypeInfo.getPmType(), poTypeInfo.getDataTableName());
            List<PoCounterInfo> list = Arrays.asList(poTypeInfo.getPoCounterInfo());
            pmTypeCountersMap.put(poTypeInfo.getPmType(), list);
            
            PoId2MonitorNameMap.put(poTypeInfo.getPmType(), poTypeInfo.getMonitorName());
        }
    }

    private void cachePoCounterThresholds(String neTypeId, PoCounterThreshold[] poCounterThresholds) {
        for (PoCounterThreshold poCounterThreshold : poCounterThresholds) {
            poCounterThreshold.setNeTypeId(neTypeId);
            List<PoCounterThreshold> list = pmCountThresholdMap.get(neTypeId);
            if (list == null) {
                list = new ArrayList<PoCounterThreshold>();
                pmCountThresholdMap.put(neTypeId, list);
            }
            list.add(poCounterThreshold);
        }
    }

}
