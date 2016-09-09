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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.nfvo.monitor.umc.fm.db.entity.AlarmType;
import org.openo.nfvo.monitor.umc.fm.db.entity.FmAlarmType;
import org.openo.nfvo.monitor.umc.fm.db.entity.FmProbableCause;
import org.openo.nfvo.monitor.umc.fm.db.entity.FmSystemType;
import org.openo.nfvo.monitor.umc.fm.db.entity.FmSystemTypeMocRelation;
import org.openo.nfvo.monitor.umc.fm.db.entity.ProbableCause;
import org.openo.nfvo.monitor.umc.fm.db.entity.SystemType;
import org.openo.nfvo.monitor.umc.fm.db.entity.SystemTypeMocRelation;
import org.openo.nfvo.monitor.umc.pm.common.DebugPrn;
import org.openo.nfvo.monitor.umc.util.filescaner.FastFileSystem;
import org.yaml.snakeyaml.Yaml;

/**
 * 
 * @date 2016/4/19 17:24:02
 * @description Build fm cache
 */
public class FmCacheBuilder {
    private static DebugPrn dMsg = new DebugPrn(FmCacheBuilder.class.getName());

    public final static int FM_SYSTEMTYPEMAP_INDEX = 0;
    public final static int FM_ALARMTYPE_INDEX = 1;
    public final static int FM_SYSTEMTYPEMOCRELATION_INDEX = 2;
    public final static int FM_PROBABLECAUSE_INDEX = 3;

    Map<String, SystemType> systemTypeMap = new HashMap<String, SystemType>();
    Map<String, AlarmType> alarmTypeMap = new HashMap<String, AlarmType>();
    Map<String, SystemTypeMocRelation> systemTypeMocRelationMap = new HashMap<String, SystemTypeMocRelation>();
    Map<String, ProbableCause> probableCauseMap = new HashMap<String, ProbableCause>();

    public final static String SYS_DIR = "system";
    public final static String FILENAME_SYSTEMTYPE = "systemtype.yml";
    public final static String FILENAME_ALARMTYPE = "alarmtype.yml";
    public final static String FILENAME_SYSTEMTYPEMOCRELATION = "systemtype_moc_relation.yml";
    public final static String FILENAME_PROBABLECAUSE = "*-probablecause.yml";

    public List<Map<String, ?>> buildFmCache() {
        List<Map<String, ? extends Object>> cacheList =
                new ArrayList<Map<String, ? extends Object>>();

        buildSystemTypeCache();
        buildAlarmTypeCache();
        buildSystemTypeMocRelationCache();
        buildProbableCauseCache();

        cacheList.add(FM_SYSTEMTYPEMAP_INDEX, systemTypeMap);
        cacheList.add(FM_ALARMTYPE_INDEX, alarmTypeMap);
        cacheList.add(FM_SYSTEMTYPEMOCRELATION_INDEX, systemTypeMocRelationMap);
        cacheList.add(FM_PROBABLECAUSE_INDEX, probableCauseMap);

        return cacheList;
    }

    private void buildSystemTypeCache() {
        Yaml yaml = new Yaml();
        File file = FastFileSystem.getFile(SYS_DIR, FILENAME_SYSTEMTYPE);
        if(file != null){
            dMsg.info("load fm yml file:" + file.getName());
            try {
                FmSystemType fmSystemType = yaml.loadAs(new FileInputStream(file), FmSystemType.class);
                SystemType[] systemTypes = fmSystemType.getSystemType();

                for(SystemType systemType : systemTypes){
                    systemTypeMap.put(String.valueOf(systemType.getSystemType()), systemType);
                }

            } catch (FileNotFoundException e) {
                dMsg.warn("load model files failed!", e);
            }
        }
    }

    private void buildAlarmTypeCache() {
        Yaml yaml = new Yaml();
        File file = FastFileSystem.getFile(SYS_DIR, FILENAME_ALARMTYPE);
        if(file != null){
            dMsg.info("load fm yml file:" + file.getName());
            try {
                FmAlarmType fmAlarmType = yaml.loadAs(new FileInputStream(file), FmAlarmType.class);
                AlarmType[] alarmTypes = fmAlarmType.getAlarmType();

                for(AlarmType alarmType : alarmTypes){
                    alarmTypeMap.put(String.valueOf(alarmType.getTypeId()), alarmType);
                }

            } catch (FileNotFoundException e) {
                dMsg.warn("load model files failed!", e);
            }
        }
    }

    private void buildSystemTypeMocRelationCache() {
        Yaml yaml = new Yaml();
        File file = FastFileSystem.getFile(SYS_DIR, FILENAME_SYSTEMTYPEMOCRELATION);
        if(file != null){
            dMsg.info("load fm yml file:" + file.getName());
            try {
                FmSystemTypeMocRelation fmSystemTypeMocRelation = yaml.loadAs(new FileInputStream(file), FmSystemTypeMocRelation.class);
                SystemTypeMocRelation[] systemTypeMocRelations = fmSystemTypeMocRelation.getSystemTypeMocRelation();

                for(SystemTypeMocRelation systemTypeMocRelation : systemTypeMocRelations){
                    systemTypeMocRelationMap.put(systemTypeMocRelation.getMocId(), systemTypeMocRelation);
                }

            } catch (FileNotFoundException e) {
                dMsg.warn("load model files failed!", e);
            }
        }
    }

    private void buildProbableCauseCache() {
        Yaml yaml = new Yaml();
        File[] files = FastFileSystem.getFiles(FILENAME_PROBABLECAUSE);
        for (File file : files)
        {
            dMsg.info("load fm yml file:" + file.getName());
            try {
                FmProbableCause fmProbableCause = yaml.loadAs(new FileInputStream(file), FmProbableCause.class);
                ProbableCause[] probableCauses = fmProbableCause.getProbableCause();

                for(ProbableCause probableCause : probableCauses){
                    probableCauseMap.put(String.valueOf(probableCause.getCode()), probableCause);
                }

            } catch (FileNotFoundException e) {
                dMsg.warn("load model files failed!", e);
            }
        }
    }

}
