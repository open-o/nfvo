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
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.openo.nfvo.vimadapter.common.LoginException;
import org.openo.nfvo.vimadapter.common.servicetoken.VimRestfulUtil;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.openstack.api.ConnectInfo;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.ConnectionMgr;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.OpenstackConnection;

public class VendorQuery {

    private static final Logger LOG = LoggerFactory.getLogger(VendorQuery.class);

    private ConnectInfo connect;

    private String vimId;

    private String vimName;

    private String authenticateMode;

    public VendorQuery(Map<String, String> conMap) {
        connect = new ConnectInfo(conMap);
        vimId = conMap.get("vimId");
        vimName = conMap.get("vimName");
        authenticateMode = conMap.get("authenticateMode");
    }

    public List<JSONObject> getVendors() {
        String result = null;
        try {
            OpenstackConnection con = ConnectionMgr.getConnectionMgr().getConnection(connect);

            String url = "/v2.0/tenants";
            String path = con.getServiceUrl(Constant.ServiceName.KEYSTONE);
            Map<String, String> paramsMap =
                    VimRestfulUtil.generateParametesMap(url, Constant.GET, path, authenticateMode);
            RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null, con.getDomainTokens(), false);
            if(null == rsp) {
                LOG.error("function=getVendors,  RestfulResponse is null");
                return null;
            }
            result = rsp.getResponseContent();

            LOG.warn("function = getVendors result: " + result);
            JSONObject vendorObj = JSONObject.fromObject(result);

            if(vendorObj.containsKey(Constant.WRAP_TENANTS)) {
                return getVendorsMap(vendorObj);
            }
        } catch(LoginException e) {
            LOG.error("function=getVendors, msg=get from OpenStack OpenStackLoginException,info:" + e);
        }
        return null;
    }

    private List<JSONObject> getVendorsMap(JSONObject vendorObj) {
        JSONArray tenantsArray = vendorObj.getJSONArray(Constant.WRAP_TENANTS);
        List<JSONObject> list = new ArrayList<JSONObject>(Constant.DEFAULT_COLLECTION_SIZE);
        int tenantSize = tenantsArray.size();
        JSONObject tenantsObject = null;

        for(int index = 0; index < tenantSize; index++) {
            tenantsObject = tenantsArray.getJSONObject(index);
            if(!Constant.TENANTS_NAME_LIST.contains(tenantsObject.getString("name"))) {
                JSONObject tenantsMap = new JSONObject();
                tenantsMap.put("description", tenantsObject.getString("description"));
                tenantsMap.put("id", tenantsObject.getString("id"));
                tenantsMap.put("name", tenantsObject.getString("name"));
                String status =
                        Boolean.parseBoolean(tenantsObject.getString("enabled")) ? Constant.ACTIVE : Constant.INACTIVE;
                tenantsMap.put("status", status);
                tenantsMap.put("vimId", vimId);
                tenantsMap.put("vimName", vimName);
                list.add(tenantsMap);
            }
        }
        return list;
    }
}
