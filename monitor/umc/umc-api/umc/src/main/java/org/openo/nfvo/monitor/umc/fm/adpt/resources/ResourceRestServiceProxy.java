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
package org.openo.nfvo.monitor.umc.fm.adpt.resources;


import org.glassfish.jersey.client.ClientConfig;
import org.openo.nfvo.monitor.umc.util.APIHttpClient;

public class ResourceRestServiceProxy {

    static {
        ClientConfig config = new ClientConfig();
    }

    public static String getProbableCauseTree(String type) throws Exception {
    	String url = RocResourceConfig.getRocResourceAddr()+"/api/roc/v1/resource"+"/definitions?type="+type;
        String response = APIHttpClient.doGet(url, "", "utf-8", "");
        return response;
        //return dbRestServiceproxy.getProbableCauseTree(type);
    }

    public static String getProbableCause() throws Exception {
        //return dbRestServiceproxy.getProbableCause();
    	String url = RocResourceConfig.getRocResourceAddr()+"/api/roc/v1/resource"+"/definitions";
        String response = APIHttpClient.doGet(url, "", "utf-8", "");
        return response;
    }

    public static String getAllResource() {
    	String url = RocResourceConfig.getRocResourceAddr()+"/api/roc/v1/resource"+"/instances?types=all";
        String response = APIHttpClient.doGet(url, "", "utf-8", "");
        return response;
        //return dbRestServiceproxy.getAllResource();
    }

    public static String getNeNode(String id) {
    	String url = RocResourceConfig.getRocResourceAddr()+"/api/roc/v1/resource"+"/instances/"+id;
        String response = APIHttpClient.doGet(url, "", "utf-8", "");
        return response;
        //return dbRestServiceproxy.getNeNode(id);
    }

}
