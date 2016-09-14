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
public class ProjectQuery {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectQuery.class);

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
    public ProjectQuery(Map<String, String> conMap) {
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
     * @since NFVO 0.5
     */
    public void init() {
        if(null != queryId && !"".equals(queryId.trim())) {
            url = UrlConstant.GET_PROJECT + '/' + queryId;
            return;
        }

        url = UrlConstant.GET_PROJECT;
    }

    /**
     * Get ports from openstack.<br/>
     * 
     * @return the result of ports information
     * @since NFVO 0.5
     */
    public JSONObject getProjects() {

        restJson.put(Constant.RETCODE, Constant.REST_FAIL);
        OpenstackConnection con = ConnectionMgr.getConnectionMgr().getConnection(connectInfo);
        LOG.warn("function=getProjects: url->" + con.getServiceUrl(Constant.KEYSTONE));
        String path = con.getServiceUrl(Constant.KEYSTONE);
        Map<String, String> paramsMap = VimRestfulUtil.generateParametesMap(url, Constant.GET, path, authenticateMode);
        RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens());
        if(null == rsp) {
            LOG.error("function=getProjects,  RestfulResponse is null");
            return restJson;
        }
        String result = rsp.getResponseContent();

        LOG.warn("function=getProjects, result: " + result);
        JSONObject projectsObj = JSONObject.fromObject(result);
        if(projectsObj.containsKey(Constant.WRAP_PROJECT) || projectsObj.containsKey(Constant.WRAP_PROJECTS)) {
            return getProjectMap(projectsObj);
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
    private JSONObject getProjectMap(JSONObject projectsObj) {
        JSONArray projectList = new JSONArray();
        if(!url.equals(UrlConstant.GET_PROJECT)) {
            projectList.add(projectsObj.getJSONObject(Constant.WRAP_PROJECT));
        } else {
            projectList = projectsObj.getJSONArray(Constant.WRAP_PROJECTS);
        }

        for(int i = 0; i < projectList.size(); i++) {
            JSONObject jsonObj = projectList.getJSONObject(i);
            jsonObj.put("vimId", vimId);
            jsonObj.put("vimName", vimName);
        }
        restJson.put("projects", projectList);
        restJson.put(Constant.RETCODE, Constant.REST_SUCCESS);
        return restJson;
    }

}
