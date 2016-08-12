/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.orchestrator.nfv.umc.fm.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openo.orchestrator.nfv.umc.fm.db.entity.AlarmType;
import org.openo.orchestrator.nfv.umc.fm.db.entity.ProbableCause;
import org.openo.orchestrator.nfv.umc.fm.db.entity.SystemType;
import org.openo.orchestrator.nfv.umc.fm.db.entity.SystemTypeMocRelation;
import org.openo.orchestrator.nfv.umc.fm.resource.bean.request.ProbableCauseCond;

/**
 * @author wangjiangping
 * @date 2016/3/31 13:53:22
 *
 */
public class FmCacheProcess {

    public static SystemType querySystemType(int systemTypes) {
        return FmCacheUtil.querySystemTypeBySystemType(String.valueOf(systemTypes));
    }

    public static List<SystemType> querySystemTypes(int[] systemTypes) {
        List<SystemType> list = new ArrayList<SystemType>();

        for(int i=0;i<systemTypes.length;i++){
            list.add(FmCacheUtil.querySystemTypeBySystemType(String.valueOf(systemTypes[i])));
        }

        return list;
    }

    public static List<SystemTypeMocRelation> querySystemTypeByMoc(String[] mocIds) {
        List<SystemTypeMocRelation> list = new ArrayList<SystemTypeMocRelation>();

        for(int i=0;i<mocIds.length;i++){
            list.add(FmCacheUtil.querySystemTypeByMoc(mocIds[i]));
        }

        return list;
    }

    public static AlarmType queryAlarmType(int typeid) {
        return FmCacheUtil.queryAlarmByTypeId(String.valueOf(typeid));
    }

    public static List<AlarmType> queryAlarmTypes(int[] typeid) {
        List<AlarmType> list = new ArrayList<AlarmType>();

        for(int i=0;i<typeid.length;i++){
            list.add(FmCacheUtil.queryAlarmByTypeId(String.valueOf(typeid[i])));
        }

        return list;
    }

    public static List<ProbableCause> queryProbableCause(int systemType) {
        List<ProbableCause> list = new ArrayList<ProbableCause>();

        Collection<ProbableCause> probableCauses = FmCacheUtil.queryAllProbableCause();

        for(ProbableCause probableCause : probableCauses){
            if(systemType == probableCause.getSystemType()){
                list.add(probableCause);
            }
        }

        return list;
    }

    private static List<ProbableCause> queryProbableCauses(int[] systemTypes) {
        List<ProbableCause> list = new ArrayList<ProbableCause>();

        for(int i=0;i<systemTypes.length;i++){
            list.addAll(queryProbableCause(systemTypes[i]));
        }

        return list;
    }

    public static ProbableCause queryProbableCauseByCode(String code) {

        return FmCacheUtil.queryProbableCauseByCode(code);
    }

    public static List<ProbableCause> queryProbableCause(ProbableCauseCond condtion) {
        List<ProbableCause> list = new ArrayList<ProbableCause>();

        int[] systemTypes = condtion.getSystemTypes();
        long code = condtion.getCode();

        if(code != 0) {
            list.add(queryProbableCauseByCode(String.valueOf(code)));
        } else {
            list.addAll(queryProbableCauses(systemTypes));
        }

        return list;
    }
}
