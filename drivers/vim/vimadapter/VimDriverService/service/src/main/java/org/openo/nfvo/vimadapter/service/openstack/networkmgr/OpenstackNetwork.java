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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vimadapter.common.LoginException;
import org.openo.nfvo.vimadapter.common.servicetoken.VimRestfulUtil;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.openstack.api.ConnectInfo;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.ConnectionMgr;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.OpenstackConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenstackNetwork {

    private static final Logger LOG = LoggerFactory.getLogger(OpenstackNetwork.class);

    private ConnectInfo conInfo;

    private OpenstackConnection con = null;

    public OpenstackNetwork(Map<String, String> conInfoMap) {
        conInfo = new ConnectInfo(conInfoMap);
    }

    public JSONObject createNetwork(JSONObject network) {
        JSONObject resultObj = new JSONObject();
        try {
            con = ConnectionMgr.getConnectionMgr().getConnection(conInfo);
            LOG.warn("function=createNetwork: url->" + con.getServiceUrl(Constant.ServiceName.NEUTRON));

            String reqBody = getNetworkBody(network);
            String path = con.getServiceUrl(Constant.ServiceName.NEUTRON);
            Map<String, String> paramsMap =
                    VimRestfulUtil.generateParametesMap("/v2.0/networks", Constant.POST, path,
                            conInfo.getAuthenticateMode());
            RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, reqBody, con.getDomainTokens(), false);
            if(null == rsp) {
                LOG.error("function=createNetwork,  RestfulResponse is null");
                resultObj.put("reason", "RestfulResponse is null.");
                resultObj.put("retCode", Constant.REST_FAIL);
                return resultObj;
            }
            String resultCreate = rsp.getResponseContent();

            if(rsp.getStatus() == Constant.HTTP_OK_STATUS_CODE || rsp.getStatus() == Constant.HTTP_CREATED_STATUS_CODE) {
                LOG.warn("function=createNetwork, msg= status={}, result={}.", rsp.getStatus(), resultCreate);
                JSONObject ceateResultObj = JSONObject.fromObject(resultCreate);
                JSONObject subresultObj = ceateResultObj.getJSONObject("network");
                resultObj.put("retCode", Constant.REST_SUCCESS);
                resultObj.put("backendId", subresultObj.getString("id"));
                return resultObj;
            } else if(rsp.getStatus() == Constant.HTTP_CONFLICT_STATUS_CODE) {
                return getNetworkFromOpenStack(network);
            } else {
                LOG.error("function=createNetwork, msg=Openstack return fail,status={}, result={}.", rsp.getStatus(),
                        resultCreate);
                resultObj.put("reason", "Create network Openstack return fail.");
            }
        } catch(LoginException e) {
            LOG.error("function=createNetwork, msg=OpenStackLoginException occurs. e={}", e);
            resultObj.put("reason", "OpenStackLoginException");
        }
        resultObj.put("retCode", Constant.REST_FAIL);
        return resultObj;
    }

    private JSONObject getNetworkFromOpenStack(JSONObject network) {
        JSONObject resultObj = new JSONObject();

        String netType = network.getString("type");

        if(("vlan").equals(netType)) {
            String url =
                    String.format("/v2.0/networks?provider:segmentation_id=%s&provider:physical_network=%s",
                            network.getString("segmentation"), network.getString("physicalNet"));
            String path = con.getServiceUrl(Constant.ServiceName.NEUTRON);
            Map<String, String> paramsMap =
                    VimRestfulUtil.generateParametesMap(url, Constant.GET, path, conInfo.getAuthenticateMode());
            RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens(), false);
            if(null == rsp) {
                LOG.error("function=getNetworkFromOpenStack,  RestfulResponse is null");
                resultObj.put("reason", "RestfulResponse is null.");
                resultObj.put("retCode", Constant.REST_FAIL);
                return resultObj;
            }
            String resultVlanGet = rsp.getResponseContent();

            LOG.warn("function=createNetwork conflict, msg -> status={}, resultGet={}.", rsp.getStatus(), resultVlanGet);

            if(rsp.getStatus() == Constant.HTTP_OK_STATUS_CODE || rsp.getStatus() == Constant.HTTP_CREATED_STATUS_CODE) {
                JSONObject getVlanResultObj = JSONObject.fromObject(resultVlanGet);
                JSONArray networkVlanArray = getVlanResultObj.getJSONArray("networks");
                if(!networkVlanArray.isEmpty()) {
                    resultObj.put("retCode", Constant.REST_SUCCESS);
                    resultObj.put("backendId", networkVlanArray.getJSONObject(0).getString("id"));
                }
            }
        } else if(("flat").equals(netType)) {
            String url =
                    String.format("/v2.0/networks?provider:network_type=flat&provider:physical_network=%s",
                            network.getString("physicalNet"));
            String path = con.getServiceUrl(Constant.ServiceName.NEUTRON);
            Map<String, String> paramsMap =
                    VimRestfulUtil.generateParametesMap(url, Constant.GET, path, conInfo.getAuthenticateMode());
            RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens(), false);
            if(null == rsp) {
                LOG.error("function=getNetworkFromOpenStack,  RestfulResponse is null");
                resultObj.put("reason", "RestfulResponse is null.");
                resultObj.put("retCode", Constant.REST_FAIL);
                return resultObj;
            }

            String resultFlatGet = rsp.getResponseContent();
            LOG.warn("function=createNetwork conflict, msg -> status={}, resultGet={}.", rsp.getStatus(), resultFlatGet);

            if(rsp.getStatus() == Constant.HTTP_OK_STATUS_CODE || rsp.getStatus() == Constant.HTTP_CREATED_STATUS_CODE) {
                JSONObject getFlatResultObj = JSONObject.fromObject(resultFlatGet);
                JSONArray networkFlatArray = getFlatResultObj.getJSONArray("networks");
                if(!networkFlatArray.isEmpty()) {
                    resultObj.put("retCode", Constant.REST_SUCCESS);
                    resultObj.put("backendId", networkFlatArray.getJSONObject(0).getString("id"));
                }
            }

        }
        return resultObj;

    }

    public int removeNetwork(JSONObject network) {
        try {
            con = ConnectionMgr.getConnectionMgr().getConnection(conInfo);
            String url = String.format("/v2.0/networks/%s", network.getString("backendId"));
            String path = con.getServiceUrl(Constant.ServiceName.NEUTRON);
            Map<String, String> paramsMap =
                    VimRestfulUtil.generateParametesMap(url, Constant.DELETE, path, conInfo.getAuthenticateMode());
            RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens(), false);
            if(null == rsp) {
                LOG.error("function=removeNetwork,  RestfulResponse is null");
                return Constant.HTTP_BAD_REQUEST_STATUS_CODE;
            }
            String result = rsp.getResponseContent();

            if(rsp.getStatus() == Constant.HTTP_OK_STATUS_CODE
                    || rsp.getStatus() == Constant.HTTP_NOCONTENT_STATUS_CODE
                    || rsp.getStatus() == Constant.HTTP_CONFLICT_STATUS_CODE
                    || rsp.getStatus() == Constant.HTTP_NOTFOUND_STATUS_CODE) {
                LOG.warn("function=removeNetwork, msg -> status={}, result={}.", rsp.getStatus(), result);
                return rsp.getStatus();
            } else {
                LOG.error("function=removeNetwork fail,msg -> status={}, result={}.", rsp.getStatus(), result);
                return rsp.getStatus();
            }
        } catch(LoginException e) {
            LOG.error("function=removeNetwork, msg=OpenStackLoginException occurs. e={}", e);
        }
        return Constant.INTERNAL_EXCEPTION_STATUS_CODE;
    }

    private String getNetworkBody(JSONObject network) {
        JSONObject reqBody = new JSONObject();

        String networkType = network.getString("type");

        reqBody.put("provider:physical_network", network.getString("physicalNet"));
        reqBody.put("tenant_id", network.getString("vimVendorId"));
        reqBody.put("name", network.getString("name"));

        reqBody.put("provider:network_type", networkType);
        reqBody.put("shared", network.getString("isPublic"));
        if(("vlan").equals(networkType)) {
            reqBody.put("provider:segmentation_id", network.getString("segmentation"));
        }

        JSONObject mesbody = new JSONObject();
        mesbody.put("network", reqBody);

        return mesbody.toString();
    }
}
