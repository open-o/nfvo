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
 * Class for Port Query.<br/>
 * <p>
 * </p>
 * 
 * @author
 * @version NFVO 0.5 Aug 26, 2016
 */
public class PortQuery {

    private static final Logger LOG = LoggerFactory.getLogger(PortQuery.class);

    private ConnectInfo connectInfo;

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
    public PortQuery(Map<String, String> conMap) {
        connectInfo = new ConnectInfo(conMap);
        queryId = conMap.get("queryId");
        vimId = conMap.get("vimId");
        vimName = conMap.get("vimName");
        authenticateMode = connectInfo.getAuthenticateMode();
        init();
    }

    /**
     * Init Method.<br/>
     *
     * @since  NFVO 0.5
     */
    public void init() {
        if(null != queryId && !"".equals(queryId.trim())) {
            url = UrlConstant.GET_PORT + '/' + queryId;
            return;
        }

        url = UrlConstant.GET_PORT;
    }

    /**
     * Get ports from openstack.<br/>
     * 
     * @return the result of ports information
     * @since NFVO 0.5
     */
    public JSONObject getPorts() {
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);
        OpenstackConnection con = ConnectionMgr.getConnectionMgr().getConnection(connectInfo);
        LOG.warn("function=getPorts: url->" + con.getServiceUrl(Constant.NEUTRON));
        String path = con.getServiceUrl(Constant.NEUTRON);
        Map<String, String> paramsMap = VimRestfulUtil.generateParametesMap(url, Constant.GET, path, authenticateMode);
        RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens());
        if(null == rsp) {
            LOG.error("function=getPorts,  RestfulResponse is null");
            return restJson;
        }
        String result = rsp.getResponseContent();

        LOG.warn("function=getPorts, result: " + result);
        JSONObject portsObj = JSONObject.fromObject(result);
        if(portsObj.containsKey(Constant.WRAP_PORT) || portsObj.containsKey(Constant.WRAP_PORTS)) {
            return getPortMap(portsObj);
        }
        return restJson;
    }

    /**
     * <br/>
     * 
     * @param hostsObj
     * @return
     * @since NFVO 0.5
     */
    private JSONObject getPortMap(JSONObject portsObj) {
        JSONArray portList = new JSONArray();
        if(!url.equals(UrlConstant.GET_PORT)) {
            portList.add(portsObj.getJSONObject(Constant.WRAP_PORT));
        } else {
            portList = portsObj.getJSONArray(Constant.WRAP_PORTS);
        }

        for(int i = 0; i < portList.size(); i++) {
            JSONObject jsonObj = portList.getJSONObject(i);
            jsonObj.put("vimId", vimId);
            jsonObj.put("vimName", vimName);
        }
        restJson.put("ports", portList);
        restJson.put(Constant.RETCODE, Constant.REST_SUCCESS);
        return restJson;
    }

}