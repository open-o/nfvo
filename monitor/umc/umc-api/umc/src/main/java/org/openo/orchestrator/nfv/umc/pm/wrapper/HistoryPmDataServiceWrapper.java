package org.openo.orchestrator.nfv.umc.pm.wrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.orchestrator.nfv.umc.cache.CacheUtil;
import org.openo.orchestrator.nfv.umc.db.UmcDbUtil;
import org.openo.orchestrator.nfv.umc.pm.adpt.roc.service.ModelServiceConsumer;
import org.openo.orchestrator.nfv.umc.pm.bean.HistoryPmDataBean;
import org.openo.orchestrator.nfv.umc.pm.bean.HistoryPmDataResponse;
import org.openo.orchestrator.nfv.umc.pm.bean.PmData;
import org.openo.orchestrator.nfv.umc.pm.bean.PmMoType;
import org.openo.orchestrator.nfv.umc.pm.bean.PmQueryConditionBean;
import org.openo.orchestrator.nfv.umc.pm.bean.Resource;
import org.openo.orchestrator.nfv.umc.pm.bean.ResourceType;
import org.openo.orchestrator.nfv.umc.pm.common.RestRequestException;
import org.openo.orchestrator.nfv.umc.pm.db.dao.IHistoryPmDataPo;
import org.openo.orchestrator.nfv.umc.pm.db.process.IPmDbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HistoryPmDataServiceWrapper {

    private static final Logger logger = LoggerFactory.getLogger(HistoryPmDataServiceWrapper.class);

    public static HistoryPmDataResponse queryPmData(PmQueryConditionBean queryCond, String language) {
        logger.info("queryPmData.");

        Date beginTime = new Date(queryCond.getBeginTime());
        Date endTime = new Date(queryCond.getEndTime());
        int granularity = queryCond.getGranularity();
        List<String> neDnList = buildNeDns(queryCond);
        String[] counterIds = queryCond.getCounterOrIndexId();

        String pmType = CacheUtil.getPmTypeIdByMoTypeId(queryCond.getMoTypeId());
        String dataTableName = CacheUtil.getDataTableNameByPMType(pmType);
        IPmDbQuery pmDbQueryDao = (IPmDbQuery) UmcDbUtil.getDao(dataTableName);
        Map<String, String> resourceId2NameMap = buildResourceId2NameMap(queryCond);

        @SuppressWarnings("unchecked")
        List<IHistoryPmDataPo> allDataList =
                (List<IHistoryPmDataPo>) pmDbQueryDao.queryDataFromDB(beginTime, endTime,
                        granularity, neDnList);
        int pageNo = queryCond.getPageNo() > 0 ? queryCond.getPageNo() : 1;
        int pageSize = queryCond.getPageSize() > 0 ? queryCond.getPageSize() : 1;
        HistoryPmDataBean[] pmDataArray =
                buildPmPageData(allDataList, queryCond.getResourceTypeId(),
                        queryCond.getMoTypeId(), counterIds, resourceId2NameMap, pageNo, pageSize, language);

        return new HistoryPmDataResponse(allDataList.size(), pmDataArray);
    }

    private static HistoryPmDataBean[] buildPmPageData(List<IHistoryPmDataPo> allDataList,
            String resourceTypeId, String moTypeId, String[] counterIds,
            Map<String, String> resourceId2NameMap, int pageNo, int pageSize, String language) {
        int startIndex = (pageNo - 1) * pageSize;
        if (startIndex > allDataList.size()) {
            startIndex = allDataList.size();
        }
        int endIndex = pageNo * pageSize;
        if (endIndex > allDataList.size()) {
            endIndex = allDataList.size();
        }
        List<IHistoryPmDataPo> pageDataList = allDataList.subList(startIndex, endIndex);

        HistoryPmDataBean[] pmDataArray = new HistoryPmDataBean[pageDataList.size()];
        for (int i = 0, size = pageDataList.size(); i < size; i++) {
            pmDataArray[i] =
                    buildPmData(pageDataList.get(i), getResourceTypeById(resourceTypeId), moTypeId,
                            counterIds, resourceId2NameMap, language);
        }
        return pmDataArray;
    }

    private static ResourceType getResourceTypeById(String id) {
        try {
            return ModelServiceConsumer.queryResourceType(id);
        } catch (RestRequestException e) {
            logger.warn(e.getMessage(), e);
        }

        return null;
    }

    private static Map<String, String> buildResourceId2NameMap(PmQueryConditionBean queryCond) {
        Map<String, String> resourceId2NameMap = new HashMap<>();
        Resource[] resources = queryCond.getResources();
        for (int i = 0; i < resources.length; i++) {
            resourceId2NameMap.put(resources[i].getId(), resources[i].getName());
        }
        return resourceId2NameMap;
    }

    /**
     * @param pmDataPo
     * @param resourceType
     * @param moTypeId
     * @param counterIds
     * @param resourceId2NameMap
     * @return
     */
    private static HistoryPmDataBean buildPmData(IHistoryPmDataPo pmDataPo,
            ResourceType resourceType, String moTypeId, String[] counterIds,
            Map<String, String> resourceId2NameMap, String language) {
        HistoryPmDataBean pmData = new HistoryPmDataBean();
        pmData.setBeginTime(pmDataPo.getBeginTime().getTime());
        pmData.setEndTime(pmDataPo.getEndTime().getTime());
        pmData.setGranularity(pmDataPo.getGranularity());
        Resource resource =
                new Resource(pmDataPo.getNedn(), getResourceNameById(pmDataPo.getNedn(),
                        resourceId2NameMap));
        pmData.setResource(resource);
        pmData.setResourceType(resourceType);
        pmData.setMoType(new PmMoType(moTypeId, null));

        PmData[] datas = new PmData[counterIds.length];
        for (int i = 0; i < counterIds.length; i++) {
            String counterColumnName = getCounterColumnNameById(counterIds[i]);
            datas[i] =
                    new PmData(counterIds[i], getCounterNameById(counterIds[i]),
                            pmDataPo.getValueByColumnName(counterColumnName));
            datas[i].translate(language);
        }
        pmData.setDatas(datas);

        return pmData;
    }

    private static String getCounterColumnNameById(String counterId) {
        return CacheUtil.getCounterInfoById(counterId).getAttrClmn();
    }

    private static String getCounterNameById(String counterId) {
        return CacheUtil.getCounterInfoById(counterId).getAttrName();
    }

    private static String getResourceNameById(String resourceId,
            Map<String, String> resourceId2NameMap) {
        if (resourceId2NameMap.get(resourceId) != null) {
            return resourceId2NameMap.get(resourceId);
        }
        return resourceId;
    }

    private static List<String> buildNeDns(PmQueryConditionBean queryCond) {
        List<String> neDnList = new ArrayList<String>();
        for (int i = 0; i < queryCond.getResources().length; i++) {
            neDnList.add(queryCond.getResources()[i].getId());
        }
        return neDnList;
    }
}
