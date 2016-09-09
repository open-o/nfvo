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
package org.openo.nfvo.monitor.umc.pm.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.openo.nfvo.monitor.umc.pm.adpt.roc.RocAdptImpl;
import org.openo.nfvo.monitor.umc.pm.bean.Resource;
import org.openo.nfvo.monitor.umc.pm.common.RestRequestException;

public class ResourceServiceWrapper {
    /**
     * @param resTypeId
     * @return
     * @throws RestRequestException 
     */
    public static List<Resource> queryResources(String resTypeId) throws RestRequestException {
        if (resTypeId == null || resTypeId.isEmpty()) {
            List<String> resTypeIdList = RocAdptImpl.getNeTypeIds();
            List<Resource> resList = new ArrayList<Resource>();
            for (int i = 0; i < resTypeIdList.size(); i++) {
                resList.addAll(RocAdptImpl.getResourcesByType(resTypeIdList.get(i)));
            }

            return resList;
        }

        return RocAdptImpl.getResourcesByType(resTypeId);
    }

}
