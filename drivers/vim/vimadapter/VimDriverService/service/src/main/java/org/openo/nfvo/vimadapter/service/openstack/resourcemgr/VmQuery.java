/*
 * Copyright 2016, Huawei Technologies Co., Ltd.
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

public class VmQuery {

    private static final Logger LOG = LoggerFactory.getLogger(VmQuery.class);

    private ConnectInfo connect;

    private String queryId;

    private String vimId;

    private String vimName;

    private String url;

    private String authenticateMode;

    public VmQuery(Map<String, String> conMap) {
        queryId = conMap.get("queryId");
        init();
        connect = new ConnectInfo(conMap);
        vimId = conMap.get("vimId");
        vimName = conMap.get("vimName");
        authenticateMode = conMap.get("authenticateMode");

    }

    public void init() {
        if(null != queryId && !"".equals(queryId.trim())) {
            url = UrlConstant.GET_VM.substring(0, UrlConstant.GET_VM.lastIndexOf("/") + 1) + queryId;
            return;
        }

        url = UrlConstant.GET_VM;
    }

    public List<JSONObject> getVms() {
        try {
            LOG.error("queryId=" + queryId + ",url=" + url);
            OpenstackConnection con = ConnectionMgr.getConnectionMgr().getConnection(connect);
            LOG.debug("function=getVms,msg=" + con.getServiceUrl(Constant.ServiceName.NOVA));

            String path = con.getServiceUrl(Constant.ServiceName.NOVA);

            Map<String, String> paramsMap =
                    VimRestfulUtil.generateParametesMap(String.format(url, con.getProjectId()), Constant.GET, path,
                            authenticateMode);
            RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens(), false);
            if(null == rsp) {
                LOG.error("function=getVms,  RestfulResponse is null");
                return null;
            }
            String result = rsp.getResponseContent();

            LOG.warn("function=getVms,msg=OpenStack result: " + result);
            JSONObject vmsJsonObj = JSONObject.fromObject(result);
            if(vmsJsonObj.containsKey(Constant.WRAP_SERVERS) || vmsJsonObj.containsKey(Constant.WRAP_SERVER)) {
                return getVmsMap(vmsJsonObj);
            }
        } catch(LoginException e) {
            LOG.error("function=getVms, msg=get from OpenStack OpenStackLoginException, exception=" + e);
        }

        return null;
    }

    private List<JSONObject> getVmsMap(JSONObject vmsJsonObj) {
        List<JSONObject> list = new ArrayList<JSONObject>(Constant.DEFAULT_COLLECTION_SIZE);
        JSONArray array = new JSONArray();
        if(vmsJsonObj.containsKey(Constant.WRAP_SERVERS)) {
            array = vmsJsonObj.getJSONArray(Constant.WRAP_SERVERS);
        }
        if(vmsJsonObj.containsKey(Constant.WRAP_SERVER)) {
            array.add(vmsJsonObj.getJSONObject(Constant.WRAP_SERVER));
        }
        int arraySize = array.size();
        JSONObject json = null;
        JSONObject vmJson = null;

        for(int i = 0; i < arraySize; i++) {
            json = array.getJSONObject(i);
            vmJson = new JSONObject();
            vmJson.put("id", json.getString("id"));
            vmJson.put("status", (json.getString("status")).toLowerCase());
            vmJson.put("hostId", json.getString("hostId"));
            vmJson.put("name", json.getString("name"));
            vmJson.put("tenantId", json.getString("tenant_id"));
            vmJson.put("image", json.getJSONObject("image").getString("id"));
            vmJson.put("metadata", json.getJSONObject("metadata").toString());
            vmJson.put("vimId", vimId);
            vmJson.put("vimName", vimName);
            list.add(vmJson);
        }
        return list;
    }
}
