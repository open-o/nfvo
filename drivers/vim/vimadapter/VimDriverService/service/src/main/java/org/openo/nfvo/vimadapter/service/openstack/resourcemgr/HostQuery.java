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
 * Host Query Class.<br/>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Aug 26, 2016
 */
public class HostQuery {

    private static final Logger LOG = LoggerFactory.getLogger(HostQuery.class);

    private ConnectInfo connectInfo;

    private String queryId;

    private String tenantId;

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
    public HostQuery(Map<String, String> conMap) {
        connectInfo = new ConnectInfo(conMap);
        queryId = conMap.get("queryId");
        tenantId = conMap.get("tenantId");
        vimId = conMap.get("vimId");
        vimName = conMap.get("vimName");
        authenticateMode = connectInfo.getAuthenticateMode();
        init();
    }

    /**
     * Init Method.<br/>
     *
     * @since NFVO 0.5
     */
    public void init() {
        String urlStr = String.format(UrlConstant.GET_HOST, tenantId);
        if(null != queryId && !"".equals(queryId.trim())) {
            url = urlStr + '/' + queryId;
            return;
        }

        url = urlStr;
    }

    /**
     * Get Networks from openstack.<br/>
     *
     * @return the result of network information
     * @since NFVO 0.5
     */
    public JSONObject getHosts() {
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);
        OpenstackConnection con = ConnectionMgr.getConnectionMgr().getConnection(connectInfo);
        LOG.error("function=getHosts: url->" + con.getServiceUrl(Constant.NOVA));
        String path = con.getServiceUrl(Constant.NOVA);
        Map<String, String> paramsMap = VimRestfulUtil.generateParametesMap(url, Constant.GET, path, authenticateMode);
        RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens());
        if(null == rsp) {
            LOG.error("function=getHosts,  RestfulResponse is null");
            return restJson;
        }
        String result = rsp.getResponseContent();

        LOG.warn("function=getHosts, result: " + result);
        JSONObject hostsObj = JSONObject.fromObject(result);
        if(hostsObj.containsKey(Constant.WRAP_HOST) || hostsObj.containsKey(Constant.WRAP_HOSTS)) {
            return getHostMap(hostsObj);
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
    private JSONObject getHostMap(JSONObject hostsObj) {
        JSONArray hostList = new JSONArray();
        if(hostsObj.containsKey(Constant.WRAP_HOST)) {
            hostList = hostsObj.getJSONArray(Constant.WRAP_HOST);
        } else {
            hostList = hostsObj.getJSONArray(Constant.WRAP_HOSTS);
        }

        for(int i = 0; i < hostList.size(); i++) {
            JSONObject jsonObj = hostList.getJSONObject(i);
            jsonObj.put("vimId", vimId);
            jsonObj.put("vimName", vimName);
        }
        restJson.put("hosts", hostList);
        restJson.put(Constant.RETCODE, Constant.REST_SUCCESS);
        return restJson;
    }

}
