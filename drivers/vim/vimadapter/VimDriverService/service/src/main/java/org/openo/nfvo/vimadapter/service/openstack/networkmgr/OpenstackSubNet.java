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

package org.openo.nfvo.vimadapter.service.openstack.networkmgr;

import java.util.Map;

import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openo.nfvo.vimadapter.common.LoginException;
import org.openo.nfvo.vimadapter.common.StringUtil;
import org.openo.nfvo.vimadapter.common.servicetoken.VimRestfulUtil;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.openstack.api.ConnectInfo;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.ConnectionMgr;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.OpenstackConnection;

public class OpenstackSubNet {

    private static final Logger LOG = LoggerFactory.getLogger(OpenstackSubNet.class);

    private ConnectInfo conInfo;

    private OpenstackConnection con;

    public OpenstackSubNet(Map<String, String> conInfoMap) {
        conInfo = new ConnectInfo(conInfoMap);
    }

    public JSONObject createSubNet(JSONObject network) {
        JSONObject resultObj = new JSONObject();

        try {
            con = ConnectionMgr.getConnectionMgr().getConnection(conInfo);
            LOG.debug("function=createSubNet: url->" + con.getServiceUrl(Constant.ServiceName.NEUTRON));

            String reqBody = setSubNetBody(network);
            String path = con.getServiceUrl(Constant.ServiceName.NEUTRON);
            Map<String, String> paramsMap =
                    VimRestfulUtil.generateParametesMap("/v2.0/subnets", Constant.POST, path,
                            conInfo.getAuthenticateMode());
            RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, reqBody, con.getDomainTokens(), false);
            if(null == rsp) {
                LOG.error("function=createSubNet,  RestfulResponse is null");
                resultObj.put("reason", "RestfulResponse is null.");
                resultObj.put("retCode", Constant.REST_FAIL);
                return resultObj;
            }
            String result = rsp.getResponseContent();
            LOG.warn("function=createSubNet, msg -> status={}, result={}.", rsp.getStatus(), result);

            if(rsp.getStatus() == Constant.HTTP_OK_STATUS_CODE || rsp.getStatus() == Constant.HTTP_CREATED_STATUS_CODE) {
                JSONObject ceateResultObj = JSONObject.fromObject(result);
                resultObj.put("retCode", Constant.REST_SUCCESS);
                resultObj.put("subnet", getSubNetBody(ceateResultObj, network));
                return resultObj;
            } else {
                LOG.error("function=createSubNet, msg=createSubNet fail, retcode={}", rsp.getStatus());
                return getSubnetNum(network);
            }
        } catch(LoginException e) {
            LOG.error("function=createSubNet, msg=OpenStackLoginException occurs. e={}", e);
            resultObj.put("reason", "OpenStackLoginException");
        }
        resultObj.put("retCode", Constant.REST_FAIL);
        return resultObj;
    }

    private JSONObject getSubnetNum(JSONObject network) {
        JSONObject resultObj = new JSONObject();

        String path = con.getServiceUrl(Constant.ServiceName.NEUTRON);
        String url = String.format("/v2.0/networks/%s", network.getString("backendId"));
        Map<String, String> paramsMap =
                VimRestfulUtil.generateParametesMap(url, Constant.GET, path, conInfo.getAuthenticateMode());
        RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens(), false);
        if(null == rsp) {
            LOG.error("function=getSubnetNum,  RestfulResponse is null");
            resultObj.put("reason", "RestfulResponse is null.");
            resultObj.put("retCode", Constant.REST_FAIL);
            return resultObj;
        }

        if(rsp.getStatus() == Constant.HTTP_OK_STATUS_CODE) {
            String result = rsp.getResponseContent();

            JSONObject resObj = JSONObject.fromObject(result);
            JSONObject networkJson = resObj.getJSONObject("network");

            JSONArray subnetJson = networkJson.getJSONArray("subnets");
            if(subnetJson.isEmpty()) {
                resultObj.put("networkStatue", Constant.DELETE_FAIL_NETWORK);
                resultObj.put("retCode", Constant.REST_FAIL);
                return resultObj;
            }
        }
        resultObj.put("networkStatue", Constant.MATAIN_CONFLICT_NETWORK);
        resultObj.put("reason", "Create subnet OpenStack return fail.");

        resultObj.put("retCode", Constant.REST_FAIL);
        return resultObj;

    }

    public int deleteSubNet(String id) {
        try {
            con = ConnectionMgr.getConnectionMgr().getConnection(conInfo);
            String path = con.getServiceUrl(Constant.ServiceName.NEUTRON);
            String url = String.format("/v2.0/subnets/%s", id);
            Map<String, String> paramsMap =
                    VimRestfulUtil.generateParametesMap(url, Constant.DELETE, path, conInfo.getAuthenticateMode());
            RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens(), false);
            if(null == rsp) {
                LOG.error("function=deleteSubNet,  RestfulResponse is null");
                return Constant.HTTP_BAD_REQUEST_STATUS_CODE;
            }
            String result = rsp.getResponseContent();
            LOG.error("function=removeNetwork, msg -> status={}, result={}.", rsp.getStatus(), result);
            return rsp.getStatus();
        } catch(LoginException e) {
            LOG.error("function=deleteSubNet, msg=OpenStackLoginException occurs. e={}", e);
        }
        return Constant.INTERNAL_EXCEPTION_STATUS_CODE;
    }

    private String getSubNetBody(JSONObject ceateResultObj, JSONObject network) {
        JSONObject subObj = ceateResultObj.getJSONObject("subnet");
        JSONObject reqBody = new JSONObject();
        reqBody.put("name", subObj.get("name"));
        reqBody.put("allocPools", subObj.get("allocation_pools"));
        reqBody.put("hostRoutes", subObj.get("host_routes"));
        reqBody.put("cidr", subObj.get("cidr"));

        reqBody.put("ipVersion", subObj.get("ip_version"));
        reqBody.put("gatewayIp", subObj.get("gateway_ip"));

        reqBody.put("backendId", subObj.get("id"));
        reqBody.put("dnsServer", subObj.get("dns_nameservers"));
        reqBody.put("enableDhcp", network.getString("enableDhcp"));
        reqBody.put("id", network.getString("id"));
        reqBody.put("vimVendorId", network.getString("vimVendorId"));
        reqBody.put("networkId", network.getString("backendId"));
        reqBody.put("rpId", network.getString("rpId"));
        reqBody.put("rpName", network.getString("rpName"));
        reqBody.put("vimId", network.getString("vimId"));

        return reqBody.toString();
    }

    private String setSubNetBody(JSONObject network) {
        JSONObject reqBody = new JSONObject();

        reqBody.put("name", network.getString("name") + "_subnet");
        reqBody.put("network_id", network.getString("backendId"));
        reqBody.put("tenant_id", network.getString("vimVendorId"));
        reqBody.put("ip_version", "4");
        reqBody.put("enable_dhcp", network.getString("enableDhcp"));
        reqBody.put("gateway_ip", network.getString("subGate"));

        String[] ipList = network.getString("subnets").split("-");
        JSONObject pool = new JSONObject();
        pool.put("start", ipList[0]);
        pool.put("end", ipList[1]);

        JSONArray pools = new JSONArray();
        pools.add(pool);
        reqBody.put("allocation_pools", pools);

        reqBody.put("cidr", StringUtil.getCidr(ipList[0], network.getString("subMask")));
        JSONObject mesBody = new JSONObject();
        mesBody.put("subnet", reqBody);
        return mesBody.toString();
    }
}
