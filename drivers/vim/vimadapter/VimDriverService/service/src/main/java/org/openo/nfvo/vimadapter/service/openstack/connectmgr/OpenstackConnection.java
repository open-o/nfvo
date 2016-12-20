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

package org.openo.nfvo.vimadapter.service.openstack.connectmgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vimadapter.common.servicetoken.VimRestfulUtil;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.constant.ParamConstants;
import org.openo.nfvo.vimadapter.service.openstack.api.ConnectInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * It provides openstack connection function.<br/>
 *
 * @author
 * @version NFVO 0.5 Aug 24, 2016
 */
public class OpenstackConnection {

    private static final Logger LOG = LoggerFactory.getLogger(OpenstackConnection.class);

    private ConnectInfo connInfo;

    private String domainTokens;

    private String projectId;

    private Map<String, List<JSONObject>> urlMap = new HashMap<>(Constant.DEFAULT_COLLECTION_SIZE);

    public OpenstackConnection() {
        // constructor
    }

    /**
     * Constructor<br/>
     *
     * @param info ConnectInfo of openstack
     * @since NFVO 0.5
     */
    public OpenstackConnection(ConnectInfo info) {
        this.connInfo = info;
    }

    public Map<String, List<JSONObject>> getUrlMap() {
        return urlMap;
    }

    /**
     * Connect to openstack<br/>
     *
     * @return the status of connection
     * @since NFVO 0.5
     */
    public int connect() {
        int retCode = Constant.INTERNAL_EXCEPTION_STATUS_CODE;
        String reqParam = String.format(ParamConstants.DOMAIN_TOKENS_V3, connInfo.getDomainName(),
                connInfo.getUserName(), connInfo.getUserPwd());
        Map<String, String> paramsMap = VimRestfulUtil.generateParametesMap("/v3/auth/tokens", Constant.POST,
                connInfo.getUrl(), connInfo.getAuthenticateMode());
        LOG.warn("function=connect, paramsMap={}, reqParam={}", paramsMap, reqParam);
        RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, reqParam, null);
        if(null == rsp) {
            LOG.error("function=connect,  RestfulResponse is null");
            return retCode;
        }
        LOG.warn("function=connect, status={}, content={}", rsp.getStatus(), rsp.getResponseContent());
        String result = rsp.getResponseContent();
        retCode = rsp.getStatus();
        if(retCode == Constant.HTTP_OK_STATUS_CODE || retCode == Constant.HTTP_CREATED_STATUS_CODE) {
            JSONObject accessObj = JSONObject.fromObject(result);
            LOG.warn("function=connect, accessObj={}", accessObj);
            if(!accessObj.containsKey(Constant.WRAP_TOKEN)) {
                return Constant.ACCESS_OBJ_NULL_STATUS_CODE;
            }

            JSONObject token = accessObj.getJSONObject(Constant.WRAP_TOKEN);
            this.domainTokens = rsp.getRespHeaderStr("X-Subject-Token");
            this.projectId = token.getJSONObject(Constant.WRAP_PROJECT).getString(Constant.ID);

            if(!setServiceUrl(token)) {
                return Constant.SERVICE_URL_ERROR_STATUS_CODE;
            }
        }

        return retCode;
    }

    private boolean setServiceUrl(JSONObject accessObj) {
        if(accessObj.containsKey(Constant.CATALOG)) {
            JSONArray serviceCatalog = accessObj.getJSONArray(Constant.CATALOG);
            LOG.debug("function=setServiceUrl, msg=serviceCatalog:" + serviceCatalog);
            int scSize = serviceCatalog.size();
            JSONObject singleLog = null;
            List<JSONObject> urlst = null;

            for(int i = 0; i < scSize; i++) {
                singleLog = serviceCatalog.getJSONObject(i);
                urlst = getRegionUrlMap(singleLog.getJSONArray(Constant.WRAP_ENDPOINTS));
                urlMap.put(singleLog.getString(Constant.SERVICENAME).toLowerCase(Locale.getDefault()), urlst);
            }

            LOG.warn("function=setServiceUrl, msg=urlMap:{}", urlMap);
            return true;
        }

        LOG.error("function=setServiceUrl, msg=get service catalog failed");
        return false;
    }

    private List<JSONObject> getRegionUrlMap(JSONArray endPoints) {
        List<JSONObject> urlst = new ArrayList<>(Constant.DEFAULT_COLLECTION_SIZE);
        JSONObject regionUrlMap = new JSONObject();
        int epSize = endPoints.size();
        String url = null;
        String region = null;

        for(int i = 0; i < epSize; i++) {
            if(!endPoints.getJSONObject(i).containsKey("interface")) {
                continue;
            }
            String inter = endPoints.getJSONObject(i).getString("interface");
            if(inter.equalsIgnoreCase("public")) {
                url = handleEndpointUrl(endPoints.getJSONObject(i).getString("url"));
                region = endPoints.getJSONObject(i).getString(Constant.REGION);
                regionUrlMap.put(region, url);
                urlst.add(regionUrlMap);
            }

        }
        LOG.warn("function=getRegionUrlMap, msg=urlMap:{}", urlst);
        return urlst;
    }

    private static String handleEndpointUrl(String url) {
        if(null == url || url.isEmpty()) {
            return null;
        }

        String localStr;

        localStr = url.trim();

        int index = localStr.lastIndexOf("/v");

        if(index == -1) {
            if(localStr.charAt(localStr.length() - 1) == '/') {

                localStr = localStr.substring(0, localStr.length() - 1);
            }
        } else {
            localStr = localStr.substring(0, index);
        }
        return localStr;
    }

    public int isConnected() {
        int retCode = Constant.HTTP_BAD_REQUEST_STATUS_CODE;
        Map<String, String> paramsMap = VimRestfulUtil.generateParametesMap("/v3/services", Constant.GET,
                connInfo.getUrl(), connInfo.getAuthenticateMode());
        RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, domainTokens);
        if(null == rsp) {
            LOG.error("function=isConnected,  RestfulResponse is null");
            return retCode;
        }
        if(rsp.getStatus() == Constant.HTTP_OK_STATUS_CODE) {
            LOG.warn("function=isConnected, msg=handshanke code is " + rsp.getStatus());
            return Constant.HTTP_OK_STATUS_CODE;
        } else if(rsp.getStatus() == Constant.HTTP_UNAUTHORIZED_STATUS_CODE) {
            LOG.warn("function=isConnected, msg=domian tokens invalid, create one.");
            connect();
            return Constant.HTTP_OK_STATUS_CODE;
        }
        return retCode;
    }

    /**
     * Disconnect to openstack<br/>
     *
     * @since NFVO 0.5
     */
    public void disconnect() {
        if(null == domainTokens) {
            return;
        }
        deleteTokens(domainTokens);
        domainTokens = null;
    }

    private void deleteTokens(String tokens) {
        LOG.info("function=deleteTokens, msg=delete tokens ...");
        Map<String, String> paramsMap = VimRestfulUtil.generateParametesMap("/v3/auth/tokens?nocatalog",
                Constant.DELETE, connInfo.getUrl(), connInfo.getAuthenticateMode());
        RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, domainTokens);
        if(null == rsp) {
            LOG.error("function=deleteTokens,  RestfulResponse is null");
        }
    }

    /**
     * Judge to need new ConnectInfo to openstack.<br/>
     *
     * @param info ConnectInfo to openstack
     * @return the result of need new connect
     * @since NFVO 0.5
     */
    public boolean isNeedRenewInfo(ConnectInfo info) {
        return connInfo.isNeedRenewInfo(info);
    }

    public void setConnInfo(ConnectInfo connInfo) {
        this.connInfo = connInfo;
    }

    public ConnectInfo getConnInfo() {
        return connInfo;
    }

    public String getDomainTokens() {
        return domainTokens;
    }

    public String getProjectId() {
        return projectId;
    }

    /**
     * Get service url by serviceName<br/>
     *
     * @param serviceName the name of service
     * @return the url of service
     * @since NFVO 0.5
     */
    public String getServiceUrl(String serviceName) {
        return urlMap.get(serviceName.toLowerCase(Locale.getDefault())).get(0).values().iterator().next().toString();
    }
}
