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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.openo.nfvo.monitor.umc.pm.adpt.roc.RocConfiguration;
import org.openo.nfvo.monitor.umc.pm.adpt.roc.entity.ResourceEntity;
import org.openo.nfvo.monitor.umc.pm.adpt.roc.entity.ResourceResponse;
import org.openo.nfvo.monitor.umc.pm.common.DebugPrn;
import org.openo.nfvo.monitor.umc.pm.common.RestRequestException;
import org.openo.nfvo.monitor.umc.util.APIHttpClient;

public class ResourceServiceConsumer{

    private static final DebugPrn logger = new DebugPrn(ResourceServiceConsumer.class.getName());
    
    private String neType;

    public ResourceServiceConsumer(String neType) {
    	this.neType = neType;
    }


    /**
     * get ne informap by roc rest api
     * 
     * @param oid
     * @return
     * @throws RestRequestException
     */
    @SuppressWarnings("rawtypes")
	public Map getNeInfoMap(String oid) throws RestRequestException {
		String url = "";
		Map paraMap = null;
		if (neType.equalsIgnoreCase("nfv.host.linux")) {
			url = RocConfiguration.getRocServerAddr()+ "/api/roc/v1/resource/hosts/" + oid;
		} else if (neType.equalsIgnoreCase("nfv.vdu.linux")) {
			url = RocConfiguration.getRocServerAddr()+ "/api/roc/v1/resource/vdus/" + oid;
		}
		try {
			String jsonStr = APIHttpClient.doGet(url, "", "utf-8", "");
			logger.info(oid + " jsonstr:" + jsonStr);
			ObjectMapper objectMapper = new ObjectMapper();
			paraMap = objectMapper.readValue(jsonStr, Map.class);
		} catch (Exception e) {
			return paraMap;
		}
		if (paraMap.containsKey("data")&& ((List) paraMap.get("data")).size() > 0) {
			paraMap = (Map) ((List) paraMap.get("data")).get(0);
		} else {
			paraMap = null;
		}
		return paraMap;

	}


    public ResourceEntity[] queryAllResource() throws RestRequestException {
		String url = "";
		Map paraMap = null;
		if (neType.equalsIgnoreCase("nfv.host.linux")) {
			url = RocConfiguration.getRocServerAddr()+ "/api/roc/v1/resource/hosts";
		} else if (neType.equalsIgnoreCase("nfv.vdu.linux")) {
			url = RocConfiguration.getRocServerAddr()+ "/api/roc/v1/resource/vdus";
		}
		String response = APIHttpClient.doGet(url, "", "utf-8", "");
		JSONObject responseObject = JSONObject.fromObject(response);
		Map<String, Class> map = new HashMap<String, Class>();  
		map.put("data", ResourceEntity.class);
		ResourceResponse resourceResponse = (ResourceResponse)JSONObject.toBean(responseObject,ResourceResponse.class,map);
		if( resourceResponse != null){
			return resourceResponse.getData();
		}else{
			return null;
		}	
    }
}
