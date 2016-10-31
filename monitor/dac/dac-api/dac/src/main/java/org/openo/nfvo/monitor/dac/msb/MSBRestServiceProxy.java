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
package org.openo.nfvo.monitor.dac.msb;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.openo.nfvo.monitor.dac.msb.bean.MsbRegisterBean;
import org.openo.nfvo.monitor.dac.msb.bean.ServiceNodeBean;
import org.openo.nfvo.monitor.dac.util.APIHttpClient;
import org.openo.nfvo.monitor.dac.util.Global;

public class MSBRestServiceProxy {

    public static String registerService(MsbRegisterBean registerInfo){
    	String url = MsbConfiguration.getMsbAddress()+Global.getMsbApiRootDomain();
    	JSONObject registerObj = JSONObject.fromObject(registerInfo);
    	String registerResponse = APIHttpClient.doPost2Str(url, registerObj, "");
        return registerResponse;
    }

    public static void unRegiserService(String serviceName,String version,String ip,String port){
        String url = MsbConfiguration.getMsbAddress()+Global.getMsbApiRootDomain()+"/"+serviceName+"/version/"+version+"/nodes/"+ip+"/"+port;
        APIHttpClient.doDelete(url, "");
    }
    public static List<String> queryService(String serviceName,String version){
    	List<String> ipList = new ArrayList<String>();
    	String url = MsbConfiguration.getMsbAddress()+Global.getMsbApiRootDomain()+"/"+serviceName+"/version/"+version;
    	String response = APIHttpClient.doGet(url, null, "utf-8", "");
    	if(!response.equals("")){
        	JSONObject jsonObject = JSONObject.fromObject(response);
    		Map<String, Class> map = new HashMap<String, Class>();  
    		map.put("nodes", ServiceNodeBean.class);
    		MsbRegisterBean serviceBean = (MsbRegisterBean) JSONObject.toBean(jsonObject, MsbRegisterBean.class ,map);
    		List<ServiceNodeBean> nodeList = serviceBean.getNodes();
    		
    		for(ServiceNodeBean node :nodeList){
    			String ip = node.getIp();
    			ipList.add(ip);
    		}
    	}
		return ipList;
		
    }

}
