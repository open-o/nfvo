/**
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
package org.openo.nfvo.monitor.umc.fm.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openo.nfvo.monitor.umc.db.entity.CurrentAlarm;
import org.openo.nfvo.monitor.umc.fm.adpt.resources.ResourceRestServiceProxy;
import org.openo.nfvo.monitor.umc.fm.adpt.resources.bean.DBProbableCauseTree;
import org.openo.nfvo.monitor.umc.fm.adpt.resources.bean.RocInstances;
import org.openo.nfvo.monitor.umc.fm.cache.FmCacheProcess;
import org.openo.nfvo.monitor.umc.fm.common.FmException;
import org.openo.nfvo.monitor.umc.fm.db.entity.ProbableCause;
import org.openo.nfvo.monitor.umc.fm.db.process.FmDBProcess;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.AlarmIds;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.CurAlarmQueryCond;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.CurAlarmQueryRequest;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.UpdateAckStateParam;
import org.openo.nfvo.monitor.umc.fm.resource.bean.response.CurAlarmQueryResult;
import org.openo.nfvo.monitor.umc.fm.resource.bean.response.DeleteCurAlarmResult;
import org.openo.nfvo.monitor.umc.fm.resource.bean.response.ExceedLimitException;
import org.openo.nfvo.monitor.umc.fm.resource.bean.response.NeMap;
import org.openo.nfvo.monitor.umc.fm.resource.bean.response.NgictAlarmData;
import org.openo.nfvo.monitor.umc.fm.util.BasicDataTypeConvertTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * Wrap query,insert,update,delete operation for current alarm
 *
 */
public class CurrentAlarmServiceWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProbableCauseServiceWrapper.class);
    private static final int MAX_NUMBER_OF_DELETE_AND_ACK_ALARM = 1000;
    private static HashMap<String, String> mocMap;
    static {
        try {
            mocMap = getAllMocMap();
        } catch (Exception e) {
            LOGGER.warn("get all moc map failed !", e);
        }
    }

    public CurAlarmQueryResult queryCurrentAlarm(CurAlarmQueryRequest req, String language) throws Exception {
        if(mocMap==null){
            mocMap = getAllMocMap();
        }
        LOGGER.info("the mocMap is:" + BasicDataTypeConvertTool.objectToString(mocMap));
        int pageSize = req.getPageSize();
        int pageNumber = req.getPageNumber();
        int startIndex = pageSize * (pageNumber - 1);
        int endIndex = startIndex + pageSize - 1;

        List<NgictAlarmData> ngictAlarmDatas = queryByCond(req.getCondition());

        if (ngictAlarmDatas.size() - 1 < endIndex) {
            endIndex = ngictAlarmDatas.size() - 1;
        }
        ArrayList<NgictAlarmData> alarms = new ArrayList<NgictAlarmData>();
        for (int i = startIndex; i <= endIndex; i++) {
            ngictAlarmDatas.get(i).translate(language);
            alarms.add(ngictAlarmDatas.get(i));
        }
        CurAlarmQueryResult result = new CurAlarmQueryResult();
        result.setAlarms(alarms.toArray(new NgictAlarmData[alarms.size()]));
        result.setTotalCount(ngictAlarmDatas.size());
        return result;
    }

    public DeleteCurAlarmResult deleteCurAlarmByIds(AlarmIds request) throws Exception {
        ArrayList<Long> idsDeletedFailed = new ArrayList<Long>();
        long[] alarmIds = request.getIds();
        checkIdsExceedLimit(alarmIds);
        for (long id : alarmIds) {
            if (!deleteAlarm(id)) {
                idsDeletedFailed.add(id);
            }
        }
        DeleteCurAlarmResult deleteResult =
                getDeleteResult(BasicDataTypeConvertTool
                        .convertLongList2longArray(idsDeletedFailed));
        return deleteResult;
    }

    public NgictAlarmData[] updateCurrentAlarm(UpdateAckStateParam updateParam, String ackUser, String language) throws Exception {
        long[] ids = updateParam.getIds();
        checkIdsExceedLimit(ids);
        ackAlarms(updateParam, ackUser);
        return getAlarmDataByAlarmIds(updateParam.getIds(), language);
    }

    private List<NgictAlarmData> queryByCond(CurAlarmQueryCond condition) throws Exception {
        HashMap<String, String> neMap = getNeMap();
        List<CurrentAlarm> alarms  = FmDBProcess.queryCurAlarm(condition);
        if (alarms == null) {
            return new ArrayList<NgictAlarmData>();
        }

        ArrayList<NgictAlarmData> result = new ArrayList<NgictAlarmData>();
        for (CurrentAlarm alarm : alarms) {
            result.add(convert2NgictAlarmData(alarm, neMap));
        }
        return result;
    }

    private static HashMap<String, String> getAllMocMap() throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        String tree = ResourceRestServiceProxy.getProbableCause();
        LOGGER.info("the probableCause got from ROC is: " + tree);
        DBProbableCauseTree[] mocNodes = new Gson().fromJson(tree, DBProbableCauseTree[].class);
        for(int i=0; i<mocNodes.length; i++) {
            map.put(mocNodes[i].getId(), mocNodes[i].getName());
        }
        return map;
    }

    private HashMap<String, String> getNeMap() throws Exception {
        HashMap<String, String> neMap = new HashMap<String, String>();
        String instances = ResourceRestServiceProxy.getAllResource();
        RocInstances instanceObjs = new Gson().fromJson(instances, RocInstances.class);
        NeMap[] hostsInstance = instanceObjs.getHost();
        NeMap[] vnfcInstance = instanceObjs.getVnfc();
        NeMap[] vnfInstance = instanceObjs.getVnf();
        NeMap[] vimInstance = instanceObjs.getVim();
        NeMap[] vduInstance = instanceObjs.getVdu();

        for(int i=0; i<hostsInstance.length; i++) {
            neMap.put(hostsInstance[i].getOid(), hostsInstance[i].getName());
        }
        for(int j=0; j<vnfcInstance.length; j++) {
            neMap.put(vnfcInstance[j].getOid(), vnfcInstance[j].getName());
        }
        for(int k=0; k<vnfInstance.length; k++) {
            neMap.put(vnfInstance[k].getOid(), vnfInstance[k].getName());
        }
        for(int l=0; l<vimInstance.length; l++) {
            neMap.put(vimInstance[l].getOid(), vimInstance[l].getName());
        }
        for(int m=0; m<vduInstance.length; m++) {
            neMap.put(vduInstance[m].getOid(), vduInstance[m].getName());
        }
        return neMap;
    }

    private NgictAlarmData convert2NgictAlarmData(CurrentAlarm alarm, HashMap<String, String> neMap) {
        NgictAlarmData ngictAlarm = new NgictAlarmData();
        getAlarmCustomAttr(alarm);

        ngictAlarm.setId(alarm.getId());
        ngictAlarm.setAid(alarm.getAid());
        ngictAlarm.setAlarmKey(alarm.getAlarmKey());
        ngictAlarm.setMoc(alarm.getMoc());
        if(mocMap.get(alarm.getMoc()) != null) {
            ngictAlarm.setMocName(mocMap.get(alarm.getMoc()));
        }
        ngictAlarm.setPosition1(alarm.getPosition1());
        ngictAlarm.setPosition1DisplayName(neMap.get(alarm.getPosition1()));
        ngictAlarm.setSubPosition1(alarm.getSubPosition1());
        ngictAlarm.setSubName1(alarm.getSubName1());
        ngictAlarm.setPosition2(alarm.getPosition2());
        ngictAlarm.setSubPosition2(alarm.getSubPosition2());
        if(alarm.getAlarmRaisedTime() != null){
            ngictAlarm.setAlarmRaisedTime(alarm.getAlarmRaisedTime().getTime());
        }
        if(alarm.getAlarmChangedTime() != null){
            ngictAlarm.setAlarmChangedTime(alarm.getAlarmChangedTime().getTime());
        }
        ngictAlarm.setSystemType((short) alarm.getSystemType());
        ngictAlarm.setSystemTypeName("SystemTypeName: " + alarm.getSystemType());
        ngictAlarm.setProbableCauseCode(alarm.getCode());
        ngictAlarm.setProbableCauseCodeName(getCodeName(alarm.getCode()));
        ngictAlarm.setAlarmType((byte) alarm.getAlarmType());
        ngictAlarm.setAlarmTypeName("alarmTypeName: " + alarm.getAlarmType());
        ngictAlarm.setPerceivedSeverity((byte) alarm.getPerceivedSeverity());
        ngictAlarm.setReasonCode(alarm.getReason());
        ngictAlarm.setSpecificProblem(alarm.getSpecificProblem());
        ngictAlarm.setAdditionalText(alarm.getAdditionalText());
        ngictAlarm.setCustomAttrs(getAlarmCustomAttr(alarm));
        ngictAlarm.setAckState((byte) alarm.getAckState());
        if(alarm.getAckTime() != null){
            ngictAlarm.setAckTime(alarm.getAckTime().getTime());
        }
        ngictAlarm.setAckUserId(alarm.getAckUserId());
        ngictAlarm.setAckSystemId(alarm.getAckSystemId());
        ngictAlarm.setCommentText(alarm.getCommentText());
        if(alarm.getCommentTime() != null){
            ngictAlarm.setCommentTime(alarm.getCommentTime().getTime());
        }
        ngictAlarm.setCommentUserId(alarm.getCommentUserId());
        ngictAlarm.setCommentSystemId(alarm.getCommentSystemId());
        if(alarm.getAlarmClearedTime() != null){
            ngictAlarm.setAlarmClearedTime(alarm.getAlarmClearedTime().getTime());
        }
        ngictAlarm.setClearUserId(alarm.getClearUserId());
        ngictAlarm.setClearSystemId(alarm.getClearSystemId());
        ngictAlarm.setClearType((byte) alarm.getClearType());
        ngictAlarm.setVisible(isVisible(alarm));
        ngictAlarm.setTimeZoneID(alarm.getTimeZoneID());
        ngictAlarm.setTimeZoneOffset(alarm.getTimeZoneOffset());
        ngictAlarm.setDSTSaving(alarm.getDstsaving());
        ngictAlarm.setNeIp(alarm.getNeIp());
        ngictAlarm.setPathName(alarm.getPathName());
        ngictAlarm.setPathIds(new String[] {alarm.getPathid()});
        ngictAlarm.setLink(alarm.getLink());
        ngictAlarm.setOriginalPerceivedSeverity((byte) alarm.getOriginalPerceivedSeverity());
        ngictAlarm.setADMC(isADMC(alarm));
        ngictAlarm.setAckInfo(alarm.getAckInfo());
        return ngictAlarm;
    }

    private String getCodeName(long code) {
        ProbableCause probableCause = FmCacheProcess.queryProbableCauseByCode(String.valueOf(code));
        if(probableCause != null) {
            return probableCause.getCodeName();
        } else {
            return Long.toString(code);
        }
    }

    private String[] getAlarmCustomAttr(CurrentAlarm alarm) {
        String[] customAttrs = new String[12];
        customAttrs[0] = alarm.getCustomAttr1();
        customAttrs[1] = alarm.getCustomAttr2();
        customAttrs[2] = alarm.getCustomAttr3();
        customAttrs[3] = alarm.getCustomAttr4();
        customAttrs[4] = alarm.getCustomAttr5();
        customAttrs[5] = alarm.getCustomAttr6();
        customAttrs[6] = alarm.getCustomAttr7();
        customAttrs[7] = alarm.getCustomAttr8();
        customAttrs[8] = alarm.getCustomAttr9();
        customAttrs[9] = alarm.getCustomAttr10();
        customAttrs[10] = alarm.getCustomAttr11();
        customAttrs[11] = alarm.getCustomAttr12();
        return customAttrs;
    }

    private boolean isVisible(CurrentAlarm alarm) {
        if (alarm.getVisible() == 0) return true;
        return false;
    }

    private boolean isADMC(CurrentAlarm alarm) {
        if (alarm.getAdmc() == 0) {
            return false;
        }
        return true;
    }

    private boolean deleteAlarm(long id) throws Exception {
        try {
            FmDBProcess.deleteCurAlarmById(id);
            return true;
        } catch (FmException e) {
            LOGGER.warn("clear alarm failed, alarm id : " + id);
            return false;
        }
    }

    private DeleteCurAlarmResult getDeleteResult(long[] idsDeletedFailed) {
        DeleteCurAlarmResult deleteResult = new DeleteCurAlarmResult();
        deleteResult.setDeleteFailedIds(idsDeletedFailed);
        deleteResult.setSuccess(idsDeletedFailed.length == 0);
        return deleteResult;
    }

    private void checkIdsExceedLimit(long[] ids) throws ExceedLimitException {
        if (ids.length > MAX_NUMBER_OF_DELETE_AND_ACK_ALARM) {
            throw new ExceedLimitException(MAX_NUMBER_OF_DELETE_AND_ACK_ALARM, ids.length);
        }
    }

    private NgictAlarmData[] getAlarmDataByAlarmIds(long[] alarmIds, String language) throws Exception {
        CurAlarmQueryCond condition = new CurAlarmQueryCond();
        condition.setIds(alarmIds);
        List<NgictAlarmData> alarms = queryByCond(condition);
        for(int i=0;i<alarms.size();i++){
            alarms.get(i).translate(language);
        }
        return alarms.toArray(new NgictAlarmData[alarms.size()]);
    }

    private void ackAlarms(UpdateAckStateParam updateParam, String ackUser) throws Exception {
        try {
            FmDBProcess.updateCurAlarm(updateParam, ackUser);
        } catch (FmException e) {
            String idsStr = BasicDataTypeConvertTool.longArray2String(updateParam.getIds(), " ");
            LOGGER.warn("ack alarms failed. ackOperationType : " + updateParam.getAckState()
                    + "alarm ids: " + idsStr);
            throw e;
        }
    }

    public List<CurrentAlarm> queryAllCurAlarm(String language) {
        List<CurrentAlarm> list = FmDBProcess.queryAllCurAlarm();
        for(int i=0;i<list.size();i++){
            list.get(i).translate(language);
        }
        return list;
    }

    public int queryCurAlarmsCount(String oid) {
        return FmDBProcess.getCurAlarmsCountByOid(oid);
    }

}
