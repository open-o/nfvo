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
import org.openo.nfvo.vimadapter.service.openstack.api.ConnectInfo;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.ConnectionMgr;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.OpenstackConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version NFVO 0.5 Sep 6, 2016
 */
public class CpuLimitQuery {

    private static final Logger LOG = LoggerFactory.getLogger(CpuLimitQuery.class);

    private ConnectInfo connect;

    private String queryId;

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
    public CpuLimitQuery(Map<String, String> conMap) {
        connect = new ConnectInfo(conMap);
        queryId = conMap.get("queryId");
        vimId = conMap.get("vimId");
        vimName = conMap.get("vimName");
        authenticateMode = connect.getAuthenticateMode();
    }

    /**
     * Get Limits from openstack.<br/>
     * 
     * @return the result of Limits information
     * @since NFVO 0.5
     */
    public JSONObject getLimits() {
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);
        OpenstackConnection con = ConnectionMgr.getConnectionMgr().getConnection(connect);
        LOG.error("function=getCpuLimits: url->" + con.getServiceUrl(Constant.NOVA));

        String url = String.format("/v2/%s/limits", queryId);
        String path = con.getServiceUrl(Constant.NOVA);
        Map<String, String> paramsMap = VimRestfulUtil.generateParametesMap(url, Constant.GET, path, authenticateMode);
        RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens());
        if(null == rsp) {
            LOG.error("function=getCpuLimits,  RestfulResponse is null");
            return restJson;
        }
        String result = rsp.getResponseContent();

        LOG.warn("function=getLimits, result: " + result);
        JSONObject limitObj = JSONObject.fromObject(result);
        if(limitObj.containsKey(Constant.WRAP_LIMITS)) {
            return getLimitMap(limitObj);
        }

        return restJson;
    }

    private JSONObject getLimitMap(JSONObject limitObj) {
        restJson.put("vimId", vimId);
        restJson.put("vimName", vimName);
        restJson.put("limits", limitObj.getJSONObject(Constant.WRAP_LIMITS));
        restJson.put(Constant.RETCODE, Constant.REST_SUCCESS);
        return restJson;
    }

}
