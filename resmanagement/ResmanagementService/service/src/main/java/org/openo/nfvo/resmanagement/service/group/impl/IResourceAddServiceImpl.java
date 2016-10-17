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

package org.openo.nfvo.resmanagement.service.group.impl;

import java.util.HashMap;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.nfvo.resmanagement.common.constant.ParamConstant;
import org.openo.nfvo.resmanagement.common.constant.UrlConstant;
import org.openo.nfvo.resmanagement.common.util.JsonUtil;
import org.openo.nfvo.resmanagement.common.util.RestfulUtil;
import org.openo.nfvo.resmanagement.service.base.openstack.inf.InterfaceResManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * iResource add service implementation.<br>
 * <p>
 * </p>
 *
 * @author
 * @version     NFVO 0.5  Sep 10, 2016
 */
public class IResourceAddServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(IResourceAddServiceImpl.class);

    /**
     *
     * Add iResource.<br>
     *
     * @param restParametes
     * @param iResMap
     * @throws ServiceException
     * @since  NFVO 0.5
     */
    @Transactional(rollbackFor = ServiceException.class)
    public void addIRes(RestfulParametes restParametes, Map<String, InterfaceResManagement> iResMap)
            throws ServiceException {
        addIResources(iResMap, createResUrlMap(), restParametes);
    }

    private void addIResources(Map<String, InterfaceResManagement> iResMap, HashMap<String, String> urlMap,
            RestfulParametes restParametes) throws ServiceException {
        for(String iResName : iResMap.keySet()) {
            if(ParamConstant.PARAM_HOST.equals(iResName)) {
                addHostResource(iResMap, restParametes,
                        String.format(urlMap.get(iResName), restParametes.get("tenantId")), iResName);
            } else {
                JSONArray iResArray = RestfulUtil.getResponseRes(restParametes, urlMap.get(iResName), iResName);
                LOGGER.warn("function=addIResources; iResArray={}", iResArray);
                for(Object object : iResArray) {
                    JSONObject iRes = JSONObject.fromObject(object);
                    int result = iResMap.get(iResName).add(iRes);
                    LOGGER.warn("function=addIResources; msg=iRes name is [{}],result is [{}]", iResName, result);
                }
            }
        }
    }

    /**
     * Add Host Resource.<br>
     *
     * @param iResMap
     * @param restParametes
     * @param url
     * @param iResName
     * @throws ServiceException
     * @since NFVO 0.5
     */
    public static void addHostResource(Map<String, InterfaceResManagement> iResMap, RestfulParametes restParametes,
            String url, String iResName) throws ServiceException {
        JSONArray hostResArray = RestfulUtil.getResponseRes(restParametes, url, iResName);
        LOGGER.warn("function=addHostResource; hostResArray={}", hostResArray);
        for(Object object : hostResArray) {
            JSONObject hostRes = JSONObject.fromObject(object);
            String hostName = hostRes.getString("host_name");
            String hostZone = hostRes.getString("zone");
            if("internal".equals(hostZone)) {
                continue;
            }
            String hostUrl = String.format(UrlConstant.GET_HOSTDETAIL_URL, restParametes.get("tenantId"), hostName);


            String result = RestfulUtil.getResponseContent(hostUrl, restParametes, ParamConstant.PARAM_GET);
            JSONObject hostObj = JSONObject.fromObject(result);
            JSONObject host = hostDataParse(hostObj, hostName);
            int res = iResMap.get(iResName).add(host);
            LOGGER.warn("function=addHostResource; result={}, res={}", result, res);
            if(res < 0) {
                LOGGER.error("function=addHostResource; add into DB fail!");
            }

        }
    }

    /**
     * <br>
     *
     * @param hostObj
     * @param hostName
     * @return
     * @since NFVO 0.5
     */
    public static JSONObject hostDataParse(JSONObject hostObj, String hostName) {
        LOGGER.warn("function=hostDataParse; hostObj={}, hostName={}", hostObj, hostName);
        JSONArray hostArray = hostObj.getJSONArray("hosts");
        for(Object object : hostArray) {
            JSONObject hostObject = JSONObject.fromObject(object);
            JSONObject resource = JSONObject.fromObject(hostObject.get("resource"));
            if(resource.getString("project").contains("total")) {
                JSONObject host = new JSONObject();
                host.put("name", hostName);
                host.put("cpu", JsonUtil.getJsonFieldStr(resource, "cpu"));
                host.put("memory", JsonUtil.getJsonFieldStr(resource, "memory_mb"));
                host.put("disk", JsonUtil.getJsonFieldStr(resource, "disk_gb"));
                host.put("vimId", JsonUtil.getJsonFieldStr(hostObject, "vimId"));
                host.put("vimName", JsonUtil.getJsonFieldStr(hostObject, "vimName"));
                return host;
            }
        }
        return new JSONObject();
    }

    private HashMap<String, String> createResUrlMap() {
        HashMap<String, String> urlMap = new HashMap<String, String>(10);
        urlMap.put(ParamConstant.PARAM_NETWORK, UrlConstant.GET_NETWORK_URL);
        urlMap.put(ParamConstant.PARAM_HOST, UrlConstant.GET_HOST_URL);
        urlMap.put(ParamConstant.PARAM_PORT, UrlConstant.GET_PORT_URL);
        return urlMap;
    }
}
