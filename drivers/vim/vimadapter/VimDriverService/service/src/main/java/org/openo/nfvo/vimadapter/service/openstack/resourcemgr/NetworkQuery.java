/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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

package org.openo.nfvo.vimadapter.service.openstack.resourcemgr;

import java.util.Map;

import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vimadapter.common.servicetoken.VimRestfulUtil;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.constant.UrlConstant;
import org.openo.nfvo.vimadapter.service.openstack.api.ConnectInfo;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.ConnectionMgr;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.OpenstackConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Query network from openstack.<br/>
 * 
 * @author
 * @version NFVO 0.5 Aug 24, 2016
 */
public class NetworkQuery {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkQuery.class);

    private ConnectInfo connect;

    private String queryId;

    private String url;

    private String vimId;

    private String vimName;

    private String authenticateMode;

    JSONObject restJson = new JSONObject();

    /**
     * Constructor<br/>
     * 
     * @param conMap the openstack info map
     * @since NFVO 0.5
     */
    public NetworkQuery(Map<String, String> conMap) {
        connect = new ConnectInfo(conMap);
        this.queryId = conMap.get("queryId");
        vimId = conMap.get("vimId");
        vimName = conMap.get("vimName");
        authenticateMode = connect.getAuthenticateMode();

        init();
    }

    /**
     * Initialise the url<br/>
     * 
     * @since NFVO 0.5
     */
    public void init() {
        if(null != queryId && !"".equals(queryId.trim())) {
            url = UrlConstant.GET_NETWORK + '/' + queryId;
            return;
        }

        url = UrlConstant.GET_NETWORK;
    }

    /**
     * Get Networks from openstack.<br/>
     * 
     * @return the result of network information
     * @since NFVO 0.5
     */
    public JSONObject getNetworks() {
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);
        OpenstackConnection con = ConnectionMgr.getConnectionMgr().getConnection(connect);

        String path = con.getServiceUrl(Constant.NEUTRON);

        Map<String, String> paramsMap = VimRestfulUtil.generateParametesMap(url, Constant.GET, path, authenticateMode);
        RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens());
        if(null == rsp) {
            LOG.error("function=getNetworks,  RestfulResponse is null");
            return restJson;
        }
        String result = rsp.getResponseContent();
        LOG.warn("function=getNetworks, result:" + result);
        JSONObject jsonObject = JSONObject.fromObject(result);

        if(jsonObject.containsKey(Constant.WRAP_NETWORKS) || jsonObject.containsKey(Constant.WRAP_NETWORK)) {
            return getNetworksMap(jsonObject);
        }
        return restJson;
    }

    private JSONObject getNetworksMap(JSONObject jsonObject) {
        JSONArray networkList = new JSONArray();
        if(!url.equals(UrlConstant.GET_NETWORK)) {
            networkList.add(jsonObject.getJSONObject(Constant.WRAP_NETWORK));
        } else {
            networkList = jsonObject.getJSONArray(Constant.WRAP_NETWORKS);
        }

        for(int i = 0; i < networkList.size(); i++) {
            JSONObject jsonObj = networkList.getJSONObject(i);
            jsonObj.put("vimId", vimId);
            jsonObj.put("vimName", vimName);
        }
        restJson.put("networks", networkList);
        restJson.put(Constant.RETCODE, Constant.REST_SUCCESS);
        return restJson;
    }
}
