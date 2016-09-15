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

package org.openo.nfvo.vimadapter.service.openstack.networkmgr;

import java.util.Map;

import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vimadapter.common.Constants;
import org.openo.nfvo.vimadapter.common.servicetoken.VimRestfulUtil;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.openstack.api.ConnectInfo;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.ConnectionMgr;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.OpenstackConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Create or delete network to openstack.<br/>
 *
 * @author
 * @version NFVO 0.5 Aug 24, 2016
 */
public class OpenstackNetwork {

    private static final Logger LOG = LoggerFactory.getLogger(OpenstackNetwork.class);

    private ConnectInfo conInfo;

    private String vimId;

    private String vimName;

    private OpenstackConnection con = null;

    /**
     * Constructor<br/>
     *
     * @param conMap the openstack info map
     * @since NFVO 0.5
     */
    public OpenstackNetwork(Map<String, String> conMap) {
        conInfo = new ConnectInfo(conMap);
        vimId = conMap.get(Constants.VIM_ID);
        vimName = conMap.get(Constants.VIM_NAME);
    }

    /**
     * Create network to openstack.<br/>
     *
     * @param network the information of network to create
     * @return the result of creating network to openstack
     * @since NFVO 0.5
     */
    public JSONObject createNetwork(JSONObject network) {
        JSONObject resultObj = new JSONObject();
        con = ConnectionMgr.getConnectionMgr().getConnection(conInfo);
        LOG.warn("function=createNetwork: url->" + con.getServiceUrl(Constant.NEUTRON));

        String path = con.getServiceUrl(Constant.NEUTRON);
        Map<String, String> paramsMap = VimRestfulUtil.generateParametesMap("/v2.0/networks", Constant.POST, path,
                conInfo.getAuthenticateMode());
        RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, network.toString(), con.getDomainTokens());
        if(null == rsp) {
            LOG.error("function=createNetwork,  RestfulResponse is null");
            resultObj.put(Constant.RETCODE, Constant.REST_FAIL);
            return resultObj;
        }
        String resultCreate = rsp.getResponseContent();

        if(rsp.getStatus() == Constant.HTTP_OK_STATUS_CODE || rsp.getStatus() == Constant.HTTP_CREATED_STATUS_CODE) {
            LOG.warn("function=createNetwork, msg= status={}, result={}.", rsp.getStatus(), resultCreate);
            JSONObject ceateResultObj = JSONObject.fromObject(resultCreate);
            JSONObject subresultObj = ceateResultObj.getJSONObject(Constant.WRAP_NETWORK);
            resultObj.put(Constant.RETCODE, Constant.REST_SUCCESS);
            resultObj.put(Constant.WRAP_NETWORK, subresultObj);
            resultObj.put(Constants.VIM_ID, vimId);
            resultObj.put(Constants.VIM_NAME, vimName);
            return resultObj;
        } else if(rsp.getStatus() == Constant.HTTP_CONFLICT_STATUS_CODE) {
            return getNetworkFromOpenStack(network);
        } else {
            LOG.error("function=createNetwork, msg=Openstack return fail,status={}, result={}.", rsp.getStatus(),
                    resultCreate);
        }
        resultObj.put(Constant.RETCODE, Constant.REST_FAIL);
        return resultObj;
    }

    private JSONObject getNetworkFromOpenStack(JSONObject network) {
        JSONObject resultObj = new JSONObject();

        String netType = network.getString("provider:network_type");

        String url = String.format("/v2.0/networks?provider:physical_network=%s",
                network.getString("provider:physical_network"));

        if(("vlan").equals(netType)) {
            url = String.format(url + "&provider:segmentation_id=%s",
                    network.getString("provider:segmentation_id"));
        } else if(("flat").equals(netType)) {
            url += "&provider:network_type=flat";
        }

        String path = con.getServiceUrl(Constant.NEUTRON);
        Map<String, String> paramsMap =
                VimRestfulUtil.generateParametesMap(url, Constant.GET, path, conInfo.getAuthenticateMode());
        RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens());
        if(null == rsp) {
            LOG.error("function=getNetworkFromOpenStack,  RestfulResponse is null");
            resultObj.put(Constant.RETCODE, Constant.REST_FAIL);
            return resultObj;
        }
        String resultGet = rsp.getResponseContent();

        LOG.warn("function=createNetwork conflict, msg -> status={}, resultGet={}.", rsp.getStatus(),
                resultGet);

        if(rsp.getStatus() == Constant.HTTP_OK_STATUS_CODE
                || rsp.getStatus() == Constant.HTTP_CREATED_STATUS_CODE) {
            JSONObject getVlanResultObj = JSONObject.fromObject(resultGet);
            JSONArray networkVlanArray = getVlanResultObj.getJSONArray("networks");
            if(!networkVlanArray.isEmpty()) {
                resultObj.put(Constant.RETCODE, Constant.REST_SUCCESS);
                resultObj.put(Constant.WRAP_NETWORK, networkVlanArray.getJSONObject(0));
                resultObj.put(Constants.VIM_ID, vimId);
                resultObj.put(Constants.VIM_NAME, vimName);
            }
        }

        return resultObj;

    }

    /**
     * Delete network to openstack.<br/>
     *
     * @param networkId the information of network to delete
     * @return the result of deleting network to openstack
     * @since NFVO 0.5
     */
    public int removeNetwork(String networkId) {
        con = ConnectionMgr.getConnectionMgr().getConnection(conInfo);
        String url = String.format("/v2.0/networks/%s", networkId);
        String path = con.getServiceUrl(Constant.NEUTRON);
        Map<String, String> paramsMap =
                VimRestfulUtil.generateParametesMap(url, Constant.DELETE, path, conInfo.getAuthenticateMode());
        RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens());
        if(null == rsp) {
            LOG.error("function=removeNetwork,  RestfulResponse is null");
            return Constant.HTTP_BAD_REQUEST_STATUS_CODE;
        }
        String result = rsp.getResponseContent();

        if(rsp.getStatus() == Constant.HTTP_OK_STATUS_CODE || rsp.getStatus() == Constant.HTTP_NOCONTENT_STATUS_CODE
                || rsp.getStatus() == Constant.HTTP_CONFLICT_STATUS_CODE
                || rsp.getStatus() == Constant.HTTP_NOTFOUND_STATUS_CODE) {
            LOG.warn("function=removeNetwork, msg -> status={}, result={}.", rsp.getStatus(), result);
            return rsp.getStatus();
        } else {
            LOG.error("function=removeNetwork fail,msg -> status={}, result={}.", rsp.getStatus(), result);
            return rsp.getStatus();
        }
    }

}
