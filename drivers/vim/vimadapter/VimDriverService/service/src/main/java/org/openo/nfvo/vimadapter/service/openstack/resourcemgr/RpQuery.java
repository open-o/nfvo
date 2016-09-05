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
import org.openo.nfvo.vimadapter.service.openstack.api.ConnectInfo;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.ConnectionMgr;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.OpenstackConnection;

public class RpQuery {

    private static final Logger LOG = LoggerFactory.getLogger(RpQuery.class);

    private ConnectInfo connect;

    private String vimId;

    private String vimName;

    private String authenticateMode;

    public RpQuery(Map<String, String> conMap) {
        connect = new ConnectInfo(conMap);
        vimId = conMap.get("vimId");
        vimName = conMap.get("vimName");
        authenticateMode = conMap.get("authenticateMode");
    }

    public List<JSONObject> getRps() {
        try {
            OpenstackConnection con = ConnectionMgr.getConnectionMgr().getConnection(connect);
            LOG.error("function=getRps: url->" + con.getServiceUrl(Constant.ServiceName.NOVA));
            String rpStatus = getRpStatus(connect, con);
            String url = String.format("/v2/%s/os-hypervisors/statistics", con.getProjectId());
            String path = con.getServiceUrl(Constant.ServiceName.NOVA);
            Map<String, String> paramsMap =
                    VimRestfulUtil.generateParametesMap(url, Constant.GET, path, authenticateMode);
            RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens(), false);
            if(null == rsp) {
                LOG.error("function=getRps,  RestfulResponse is null");
                return null;
            }
            String result = rsp.getResponseContent();

            LOG.warn("function=getRps, result: " + result);
            JSONObject rpObj = JSONObject.fromObject(result);
            if(rpObj.containsKey(Constant.WRAP_HYPERVISOR_STATS)) {
                return getRpMap(rpObj, con, rpStatus);
            }
        } catch(LoginException e) {
            LOG.error("function=getRps, msg=LoginException, info:" + e);
        }

        return null;
    }

    private List<JSONObject> getRpMap(JSONObject rpObj, OpenstackConnection con, String rpStatus) {
        List<JSONObject> list = new ArrayList<JSONObject>(Constant.DEFAULT_COLLECTION_SIZE);
        JSONObject hypervisorObj = rpObj.getJSONObject(Constant.WRAP_HYPERVISOR_STATS);
        JSONObject resultJson = new JSONObject();
        JSONObject totalJson = new JSONObject();
        JSONObject usedJson = new JSONObject();
        totalJson.put("vcpus", hypervisorObj.getString("vcpus"));
        totalJson.put("cpumhz", String.valueOf(Integer.parseInt(hypervisorObj.getString("vcpus")) * Constant.CPUMHZ));
        totalJson.put("memory", hypervisorObj.getString("memory_mb"));
        totalJson.put("disk", hypervisorObj.getString("local_gb"));
        usedJson.put("vcpus", hypervisorObj.getString("vcpus_used"));
        usedJson.put("cpumhz",
                String.valueOf(Integer.parseInt(hypervisorObj.getString("vcpus_used")) * Constant.CPUMHZ));
        usedJson.put("memory", hypervisorObj.getString("memory_mb_used"));
        usedJson.put("disk", hypervisorObj.getString("local_gb_used"));
        resultJson.put("total", totalJson);
        resultJson.put("used", usedJson);

        String[] rpSplit = con.getServiceUrl(Constant.ServiceName.NOVA).split("[.]");
        String rpName = null;
        if(rpSplit.length >= Constant.OPENSTACK_NOVAURL_MIN_LENTH) {
            rpName = rpSplit[1] + '.' + rpSplit[2];
        }

        else {
            LOG.error("function=getRpMap, msg=get openstack rp name failed.");
            return null;
        }

        resultJson.put("name", rpName);
        resultJson.put("id", vimId + '-' + rpName);
        resultJson.put("tenantName", "admin");
        resultJson.put("tenantId", con.getProjectId());
        resultJson.put("status", rpStatus);
        resultJson.put("vimId", vimId);
        resultJson.put("vimName", vimName);
        list.add(resultJson);
        return list;
    }

    private String getRpStatus(ConnectInfo conInfo, OpenstackConnection con) {
        String rpStatus = Constant.INACTIVE;
        String url = String.format("/v2/%s/os-hypervisors", con.getProjectId());
        String path = con.getServiceUrl(Constant.ServiceName.NOVA);
        Map<String, String> paramsMap = VimRestfulUtil.generateParametesMap(url, Constant.GET, path, authenticateMode);
        RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens(), false);
        if(null == rsp) {
            LOG.error("function=getRpStatus,  RestfulResponse is null");
            return null;
        }
        String hostResult = rsp.getResponseContent();

        JSONObject hostObj = JSONObject.fromObject(hostResult);

        if(hostObj.containsKey(Constant.WRAP_HYPERVISOR)) {
            JSONArray array = hostObj.getJSONArray(Constant.WRAP_HYPERVISOR);
            JSONObject jo = null;
            int arraySize = array.size();
            for(int i = 0; i < arraySize; i++) {
                jo = array.getJSONObject(i);

                if(!jo.containsKey("status")) {
                    rpStatus = Constant.ACTIVE;
                    break;
                }

                if("normal".equals(jo.getString("status"))) {
                    rpStatus = Constant.ACTIVE;
                    break;
                }
            }
        }
        return rpStatus;
    }
}
