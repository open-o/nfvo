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

import org.openo.nfvo.monitor.umc.db.entity.MonitorInfo;
import org.openo.nfvo.monitor.umc.monitor.wrapper.DACServiceWrapper;
import org.openo.nfvo.monitor.umc.pm.bean.Resource;
import org.openo.nfvo.monitor.umc.pm.common.PmConst;
import org.openo.nfvo.monitor.umc.pm.common.RestRequestException;

public class ResourceServiceWrapper {
    /**
     * @param resTypeId
     * @return
     * @throws RestRequestException 
     */
    public static List<Resource> queryResources(String resTypeId) throws RestRequestException {
        
        List<Resource> resList = new ArrayList<Resource>();
        List<String> neTypeIds = new ArrayList<String>();
        
        if (resTypeId == null || resTypeId.isEmpty()) {
            neTypeIds.add(PmConst.VDU);
            neTypeIds.add(PmConst.HOST);
        }else{
            neTypeIds.add(resTypeId);
        }
        
        for(String neTypeId : neTypeIds){
            List<MonitorInfo> monitorInfos = DACServiceWrapper.getInstance().getMonitorInfoByNeTypeId(neTypeId);
            for(MonitorInfo monitorInfo : monitorInfos){
                resList.add(new Resource(monitorInfo.getOid(), monitorInfo.getLabel()));                    
            }
        }        
        return resList;
    }
}
