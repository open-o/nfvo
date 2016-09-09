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
package org.openo.nfvo.monitor.umc.fm.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.openo.nfvo.monitor.umc.fm.db.entity.AlarmType;
import org.openo.nfvo.monitor.umc.fm.db.entity.ProbableCause;
import org.openo.nfvo.monitor.umc.fm.db.entity.SystemType;
import org.openo.nfvo.monitor.umc.fm.db.entity.SystemTypeMocRelation;
import org.openo.nfvo.monitor.umc.pm.common.DebugPrn;

/**
 * 
 * @date 2016/3/31 9:59:17
 *
 */
public class FmCacheUtil {
    private static DebugPrn dMsg = new DebugPrn(FmCacheUtil.class.getName());

    // systemtype-->name
    private static Map<String, SystemType> systemTypeMap;
    // typeid-->description
    private static Map<String, AlarmType> alarmTypeMap;
    // mocId-->systemType
    private static Map<String, SystemTypeMocRelation> systemTypeMocRelationMap;
    // code-->ProbableCause
    private static Map<String, ProbableCause> probableCauseMap;

    public static void init() {

        FmCacheBuilder builder = new FmCacheBuilder();
        List<Map<String, ?>> cacheList = builder.buildFmCache();

        systemTypeMap = Collections.unmodifiableMap(
                (Map<String, SystemType>) cacheList.get(FmCacheBuilder.FM_SYSTEMTYPEMAP_INDEX));
        alarmTypeMap = Collections.unmodifiableMap(
                (Map<String, AlarmType>) cacheList.get(FmCacheBuilder.FM_ALARMTYPE_INDEX));
        systemTypeMocRelationMap =
                Collections.unmodifiableMap((Map<String, SystemTypeMocRelation>) cacheList
                        .get(FmCacheBuilder.FM_SYSTEMTYPEMOCRELATION_INDEX));
        probableCauseMap = Collections.unmodifiableMap(
                (Map<String, ProbableCause>) cacheList.get(FmCacheBuilder.FM_PROBABLECAUSE_INDEX));
    }

    public static SystemType querySystemTypeBySystemType(String systemType) {
        if (systemTypeMap != null) {
            return systemTypeMap.get(systemType);
        }

        return null;
    }

    public static SystemTypeMocRelation querySystemTypeByMoc(String mocId) {
        if (systemTypeMocRelationMap != null) {
            return systemTypeMocRelationMap.get(mocId);
        }

        return null;
    }

    public static AlarmType queryAlarmByTypeId(String typeId) {
        if (alarmTypeMap != null) {
            return alarmTypeMap.get(typeId);
        }

        return null;
    }

    public static Collection<ProbableCause> queryAllProbableCause() {
        return probableCauseMap.values();
    }

    public static ProbableCause queryProbableCauseByCode(String code) {
        return probableCauseMap.get(code);
    }
}
