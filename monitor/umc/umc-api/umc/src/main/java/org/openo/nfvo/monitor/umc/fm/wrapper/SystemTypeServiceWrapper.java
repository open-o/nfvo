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

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.openo.nfvo.monitor.umc.fm.cache.FmCacheProcess;
import org.openo.nfvo.monitor.umc.fm.db.entity.SystemType;
import org.openo.nfvo.monitor.umc.fm.db.entity.SystemTypeMocRelation;
import org.openo.nfvo.monitor.umc.fm.db.process.FmDBProcess;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.SystemTypeQueryRequest;

/**
 * Wrap query,insert,update,delete operation for system type
 */
public class SystemTypeServiceWrapper {

    public SystemType[] getSystemType(SystemTypeQueryRequest condition, String language) {
        String[] mocIds = condition.getMocIds();
        List<SystemTypeMocRelation> relation = FmCacheProcess.querySystemTypeByMoc(mocIds);
        int[] systemTypes = new int[relation.size()];
        for (int i = 0; i < relation.size(); i++) {
            systemTypes[i] = relation.get(i).getSystemType();
        }

        // delete the duplicate systemType
        Set<Integer> set = new TreeSet<Integer>();
        for (int i : systemTypes) {
            set.add(i);
        }
        Integer[] systemTypes2 = set.toArray(new Integer[0]);
        int[] newSystemTypes = new int[systemTypes2.length];
        for (int i = 0; i < newSystemTypes.length; i++) {
            newSystemTypes[i] = systemTypes2[i];
        }

        List<SystemType> result = FmCacheProcess.querySystemTypes(newSystemTypes);
        for(int i=0;i<result.size();i++){
            result.get(i).translate(language);
        }
        return result.toArray(new SystemType[result.size()]);
    }

}
