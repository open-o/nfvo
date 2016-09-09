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
package org.openo.nfvo.monitor.umc.fm.db.process;

import java.util.List;
import java.util.Random;

import org.openo.nfvo.monitor.umc.db.UmcDbUtil;
import org.openo.nfvo.monitor.umc.db.dao.CurrentAlarmDao;
import org.openo.nfvo.monitor.umc.db.entity.CurrentAlarm;
import org.openo.nfvo.monitor.umc.fm.cache.FmCacheProcess;
import org.openo.nfvo.monitor.umc.fm.common.FMConsts;
import org.openo.nfvo.monitor.umc.fm.db.entity.ProbableCause;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.CurAlarmQueryCond;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.ProbableCauseCond;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.UpdateAckStateParam;
import org.openo.nfvo.monitor.umc.pm.adpt.fm.bean.FmAlarmData;

/**
 * This class include insert,update,query,delete method for FM DB
 */
public class FmDBProcess {

    public static void insertCurrentAlarmData(FmAlarmData fmAlarmData) {
        CurrentAlarmDao dao = (CurrentAlarmDao) UmcDbUtil.getDao(FMConsts.CURRENTALARM);
        CurrentAlarm existAlarm = dao.findByAlarmKey(fmAlarmData.getAlarmKey());

        if(existAlarm != null){
            dao.delete(existAlarm);
        }

        CurrentAlarm currentAlarm = buildCurrentAlarm(fmAlarmData);
        dao.save(currentAlarm);
    }

    private static CurrentAlarm buildCurrentAlarm(FmAlarmData fmAlarmData) {
        CurrentAlarm currentAlarm = new CurrentAlarm();
        ProbableCause probableCause = new ProbableCause();
        ProbableCauseCond queryCond = new ProbableCauseCond();
        queryCond.setCode(fmAlarmData.getCode());
        List<ProbableCause> probableCauses = FmCacheProcess.queryProbableCause(queryCond);
        if (probableCauses.size() != 0) {
            probableCause = probableCauses.get(0);
            if (probableCause != null)
            {
            	currentAlarm.setAlarmType(probableCause.getAlarmType());
            }
        }
        currentAlarm.setAlarmKey(fmAlarmData.getAlarmKey());
        currentAlarm.setPosition1(fmAlarmData.getOid());
        currentAlarm.setCode(fmAlarmData.getCode());
        currentAlarm.setAlarmRaisedTime(fmAlarmData.getAlarmRaiseTime());
        currentAlarm.setPerceivedSeverity(fmAlarmData.getServerity());
        currentAlarm.setAdditionalText(fmAlarmData.getDetailInfo());
        currentAlarm.setNeIp(fmAlarmData.getDevIp());
        currentAlarm.setSystemType(fmAlarmData.getSystemType());
        currentAlarm.setMoc(fmAlarmData.getMoc());

        currentAlarm.setId(generateId());
        currentAlarm.setAlarmSource("umc");
        currentAlarm.setOriginalPerceivedSeverity(fmAlarmData.getServerity());
        currentAlarm.setDispproduct("N/A");
        currentAlarm.setAlarmRaisedTime_gmt(fmAlarmData.getAlarmRaiseTime());
        currentAlarm.setTimeZoneOffset(0);
        currentAlarm.setDataType(0);
        currentAlarm.setAckState(2);
        currentAlarm.setVisible(1);
        return currentAlarm;
    }

    public static List<CurrentAlarm> queryCurAlarm(CurAlarmQueryCond condition) {
        CurrentAlarmDao dao = (CurrentAlarmDao) UmcDbUtil.getDao(FMConsts.CURRENTALARM);
        return dao.findByCondition(condition);
    }

    public static List<CurrentAlarm> queryAllCurAlarm() {
        CurrentAlarmDao dao = (CurrentAlarmDao) UmcDbUtil.getDao(FMConsts.CURRENTALARM);
        return dao.findAll();
    }

    public static void insertCurAlarm(CurrentAlarm curAlarm) {
        CurrentAlarmDao dao = (CurrentAlarmDao) UmcDbUtil.getDao(FMConsts.CURRENTALARM);
        dao.save(curAlarm);
    }

    public static CurrentAlarm queryCurAlarmByAlarmKey(String alarmKey) {
        CurrentAlarmDao dao = (CurrentAlarmDao) UmcDbUtil.getDao(FMConsts.CURRENTALARM);
        return dao.findByAlarmKey(alarmKey);
    }

    public static void restoreCurAlarm(String alarmKey) {
        CurrentAlarmDao dao = (CurrentAlarmDao) UmcDbUtil.getDao(FMConsts.CURRENTALARM);
        dao.deleteByAlarmKey(alarmKey);
    }

    public static void deleteCurAlarmById(long id) throws Exception {
        CurrentAlarmDao dao = (CurrentAlarmDao) UmcDbUtil.getDao(FMConsts.CURRENTALARM);
        dao.deleteById(id);
    }

    public static void updateCurAlarm(UpdateAckStateParam updateParam, String ackUser) throws Exception {
        CurrentAlarmDao dao = (CurrentAlarmDao) UmcDbUtil.getDao(FMConsts.CURRENTALARM);
        dao.update(updateParam, ackUser);
    }

    public static void updateCurAlarmByAlarmKey(CurrentAlarm curAlarm) throws Exception {
        CurrentAlarmDao dao = (CurrentAlarmDao) UmcDbUtil.getDao(FMConsts.CURRENTALARM);
        dao.updateCurAlarm(curAlarm);
    }

    private static long generateId() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            str.append(random.nextInt(10));
        }
        long num = Long.parseLong(str.toString());
        return num;
    }

    public static int getCurAlarmsCountByOid(String oid) {
        int count = 0;
        CurrentAlarmDao dao = (CurrentAlarmDao) UmcDbUtil.getDao(FMConsts.CURRENTALARM);
        count = dao.getAlarmsCountByOid(oid);
        return count;
    }
}
