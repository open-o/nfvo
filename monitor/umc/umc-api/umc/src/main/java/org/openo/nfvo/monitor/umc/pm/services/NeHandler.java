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
package org.openo.nfvo.monitor.umc.pm.services;

import java.util.List;
import java.util.Map;

import org.openo.nfvo.monitor.umc.pm.common.PmConst;
import org.openo.nfvo.monitor.umc.pm.task.PmTaskService;

/**
 * 
 * @date 2016/4/1 11:05:18
 *
 */
public class NeHandler {

    public static void createHandle(Map<String, String> infoMap, String oid, String moc) {
    	String proxyIp = infoMap.get(PmConst.PROXYIP);
    	String version = infoMap.get(PmConst.VERSION);
        PmTaskService.pmTaskCreate(version, proxyIp, oid, moc);
        PmTaskService.pmThresholdCreate(oid, moc);
    }

    public static void createHandle(String proxyIp, String oid, String moc) {
        PmTaskService.pmTaskCreate(null, proxyIp, oid, moc);
        PmTaskService.pmThresholdCreate(oid, moc);
    }

    public static void deleteHandle(List<String> oids) {
        for (String oid : oids) {
            PmTaskService.pmTaskDelete(oid);
            PmTaskService.pmThresholdDelete(oid);
        }
    }

    public static void deleteHandle(String oid) {
        PmTaskService.pmTaskDelete(oid);
        PmTaskService.pmThresholdDelete(oid);
    }
}
