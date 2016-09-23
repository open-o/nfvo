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
package org.openo.nfvo.monitor.umc.pm.adpt.roc.service;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.openo.nfvo.monitor.umc.pm.adpt.roc.RocConfiguration;
import org.openo.nfvo.monitor.umc.pm.adpt.roc.entity.ResourceTypeResponse;
import org.openo.nfvo.monitor.umc.pm.bean.ResourceType;
import org.openo.nfvo.monitor.umc.pm.common.DebugPrn;
import org.openo.nfvo.monitor.umc.pm.common.RestRequestException;
import org.openo.nfvo.monitor.umc.util.APIHttpClient;


/**
 * 
 */
public class ModelServiceConsumer {
    private static final DebugPrn logger = new DebugPrn(ModelServiceConsumer.class.getName());
    private static List<ResourceType> resourceTypeList= new ArrayList<ResourceType>();
    static {
    	ResourceType vdu = new ResourceType("nfv.vdu.linux","VDU(LINUX)");
    	ResourceType host = new ResourceType("nfv.host.linux","HOST(LINUX)");
    	resourceTypeList.add(vdu);
    	resourceTypeList.add(host);
    }
    
    
    
    /**
     * @param id
     * @return
     * @throws RestRequestException
     */
    public static ResourceType queryResourceType(String id) {
        logger.info("queryResourceType. id = " + id);
        if(id!=null && !id.equalsIgnoreCase("")){
        	if(id.equalsIgnoreCase("nfv.vdu.linux")){
        		return resourceTypeList.get(0);
        	}if(id.equalsIgnoreCase("nfv.host.linux")){
        		return resourceTypeList.get(1);
        	}
        }
        return null;
    }
    
    /**
     * @param id
     * @return
     * @throws RestRequestException
     */
    public static ResourceType queryResourceType_bak(String id) throws RestRequestException {
        logger.info("queryResourceType. id = " + id);
        String url = RocConfiguration.getRocServerAddr()+"/openoapi/roc/v1/resource/definitions";
        String requestQueryString = "id="+id;
        String response = APIHttpClient.doGet(url, requestQueryString, "utf-8", "");
        JSONArray jsonArray = JSONArray.fromObject(response);
	    List<ResourceTypeResponse> templateList = JSONArray.toList(jsonArray, ResourceTypeResponse.class);
        try {
            logger.info("response : " + templateList.get(0));
            return new ResourceType(templateList.get(0).getId(), templateList.get(0).getName());
        } catch (Exception e) {
            throw new RestRequestException("roc model rest request error.", e);
        }
    }
    
    
    

}
