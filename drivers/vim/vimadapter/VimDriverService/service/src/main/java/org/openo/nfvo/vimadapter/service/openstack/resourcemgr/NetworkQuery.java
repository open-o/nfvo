/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.nfvo.vimadapter.service.openstack.resourcemgr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vimadapter.common.LoginException;
import org.openo.nfvo.vimadapter.common.servicetoken.VimRestfulUtil;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.constant.UrlConstant;
import org.openo.nfvo.vimadapter.service.openstack.api.ConnectInfo;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.ConnectionMgr;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.OpenstackConnection;

public class NetworkQuery {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkQuery.class);

    private ConnectInfo connect;

    private String queryId;

    private String url;

    private String vimId;

    private String vimName;

    private String authenticateMode;

    public NetworkQuery(Map<String, String> conMap) {
        connect = new ConnectInfo(conMap);
        this.queryId = conMap.get("queryId");
        vimId = conMap.get("vimId");
        vimName = conMap.get("vimName");
        authenticateMode = conMap.get("authenticateMode");

        init();
    }

    public void init() {
        if(null != queryId && !"".equals(queryId.trim())) {
            url = UrlConstant.GET_NETWORK + '/' + queryId;
            return;
        }

        url = UrlConstant.GET_NETWORK;
    }

    public List<JSONObject> getNetworks() {
        OpenstackConnection con = null;
        try {
            con = ConnectionMgr.getConnectionMgr().getConnection(connect);

            String path = con.getServiceUrl(Constant.ServiceName.NEUTRON);

            Map<String, String> paramsMap =
                    VimRestfulUtil.generateParametesMap(url, Constant.GET, path, authenticateMode);
            RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens(), false);
            if(null == rsp) {
                LOG.error("function=getNetworks,  RestfulResponse is null");
                return null;
            }
            String result = rsp.getResponseContent();
            LOG.warn("function=getNetworks, result:" + result);
            JSONObject jsonObject = JSONObject.fromObject(result);

            if(jsonObject.containsKey(Constant.WRAP_NETWORKS) || jsonObject.containsKey(Constant.WRAP_NETWORK)) {
                return getNetworksMap(jsonObject);
            }

        } catch(LoginException e) {
            LOG.error("function=getNetworks, msg=OpenstackCLoginException occurs. e={}", e);
        }
        return null;
    }

    private List<JSONObject> getNetworksMap(JSONObject jsonObject) {
        List<JSONObject> mapList = new ArrayList<JSONObject>(Constant.DEFAULT_COLLECTION_SIZE);
        if(!url.equals(UrlConstant.GET_NETWORK)) {
            mapList.add(addNetworksMap(jsonObject.getJSONObject(Constant.WRAP_NETWORK)));
            return mapList;
        }

        JSONArray networkJsonObj = jsonObject.getJSONArray(Constant.WRAP_NETWORKS);
        int netSize = networkJsonObj.size();

        for(int i = 0; i < netSize; i++) {
            mapList.add(addNetworksMap(networkJsonObj.getJSONObject(i)));
        }
        return mapList;
    }

    private JSONObject addNetworksMap(JSONObject obj) {
        JSONObject netWorkMap = new JSONObject();
        netWorkMap.put("vimId", vimId);
        netWorkMap.put("vimName", vimName);
        netWorkMap.put("id", obj.getString("id"));
        netWorkMap.put("shared", obj.getString("shared"));
        netWorkMap.put("subnets", obj.getJSONArray("subnets"));
        netWorkMap.put("name", obj.getString("name"));
        netWorkMap.put("tenantId", obj.getString("tenant_id"));
        netWorkMap.put("status", obj.getString("status"));

        JSONObject providerMap = new JSONObject();

        providerMap.put("networkType", obj.getString("provider:network_type"));
        providerMap.put("physicalNetwork", obj.getString("provider:physical_network"));
        providerMap.put("segmentationId", obj.getString("provider:segmentation_id"));
        netWorkMap.put("provider", providerMap);

        JSONObject routerMap = new JSONObject();
        routerMap.put("external", obj.getString("router:external"));
        netWorkMap.put("router", routerMap);

        return netWorkMap;
    }
}