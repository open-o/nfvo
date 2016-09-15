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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vimadapter.common.servicetoken.VimRestfulUtil;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.openstack.api.ConnectInfo;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.ConnectionMgr;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.OpenstackConnection;

import mockit.Mock;
import mockit.MockUp;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HostQueryTest {

    private HostQuery hostQuery;

    @Before
    public void setUp() {
        Map<String, String> conMap = new HashMap<String, String>();
        conMap.put("url", "url");
        conMap.put("userName", "userName");
        conMap.put("userPwd", "userPwd");
        conMap.put("queryId", "queryId");
        conMap.put("vimId", "vimId");
        conMap.put("vimName", "vimName");
        hostQuery = new HostQuery(conMap);
    }

    @After
    public void tearDown() {
        hostQuery = null;
    }

    @Test
    public void testGetHosts() {
        new MockUp<ConnectionMgr>() {

            @Mock
            public OpenstackConnection getConnection(ConnectInfo info) {

                return new OpenstackConnection();
            }
        };
        new MockUp<OpenstackConnection>() {

            @Mock
            public String getServiceUrl(String serviceName) {
                return "url";
            }
        };
        new MockUp<VimRestfulUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                    String domainTokens) {
                RestfulResponse rsp = new RestfulResponse();
                String responseString = "{\"hosts\":[{\"resource\":\"\"}]}";
                rsp.setResponseJson(responseString);
                return rsp;
            }
        };
        JSONObject result = hostQuery.getHosts();

        JSONObject resObj = new JSONObject();
        resObj.put("vimId", "vimId");
        resObj.put("vimName", "vimName");
        resObj.put("resource", "");
        JSONArray arr = new JSONArray();
        arr.add(resObj);
        JSONObject resObject = new JSONObject();
        resObject.put("hosts", arr);
        resObject.put(Constant.RETCODE, Constant.REST_SUCCESS);
        //assertEquals(resObject, result);
    }

    @Test
    public void testGetHostsByResponseNull() {
        new MockUp<ConnectionMgr>() {

            @Mock
            public OpenstackConnection getConnection(ConnectInfo info) {

                return new OpenstackConnection();
            }
        };
        new MockUp<OpenstackConnection>() {

            @Mock
            public String getServiceUrl(String serviceName) {
                return "url";
            }
        };
        new MockUp<VimRestfulUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                    String domainTokens) {
                return null;
            }
        };
        JSONObject result = hostQuery.getHosts();

        JSONObject resObject = new JSONObject();
        resObject.put(Constant.RETCODE, Constant.REST_FAIL);
        assertEquals(resObject, result);
    }

    @Test
    public void testGetHostsByFail1() {
        new MockUp<ConnectionMgr>() {

            @Mock
            public OpenstackConnection getConnection(ConnectInfo info) {

                return new OpenstackConnection();
            }
        };
        new MockUp<OpenstackConnection>() {

            @Mock
            public String getServiceUrl(String serviceName) {
                return "url";
            }
        };
        new MockUp<VimRestfulUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                    String domainTokens) {
                RestfulResponse rsp = new RestfulResponse();
                String responseString = "{\"h\":[{\"resource\":\"\"}]}";
                rsp.setResponseJson(responseString);
                return rsp;
            }
        };
        JSONObject result = hostQuery.getHosts();

        JSONObject resObject = new JSONObject();
        resObject.put(Constant.RETCODE, Constant.REST_FAIL);
        assertEquals(resObject, result);
    }

    @Test
    public void testGetHostsByHost() {
        new MockUp<ConnectionMgr>() {

            @Mock
            public OpenstackConnection getConnection(ConnectInfo info) {

                return new OpenstackConnection();
            }
        };
        new MockUp<OpenstackConnection>() {

            @Mock
            public String getServiceUrl(String serviceName) {
                return "url";
            }
        };
        new MockUp<VimRestfulUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params,
                    String domainTokens) {
                RestfulResponse rsp = new RestfulResponse();
                String responseString = "{\"host\":[{\"resource\":\"\"}]}";
                rsp.setResponseJson(responseString);
                return rsp;
            }
        };
        JSONObject result = hostQuery.getHosts();

        JSONObject resObj = new JSONObject();
        resObj.put("vimId", "vimId");
        resObj.put("vimName", "vimName");
        resObj.put("resource", "");
        JSONArray arr = new JSONArray();
        arr.add(resObj);
        JSONObject resObject = new JSONObject();
        resObject.put("hosts", arr);
        resObject.put(Constant.RETCODE, Constant.REST_SUCCESS);
        //assertEquals(resObject, result);
    }

    @Test
    public void testInit() {
        Map<String, String> conMap = new HashMap<String, String>();
        conMap.put("url", "url");
        conMap.put("userName", "userName");
        conMap.put("userPwd", "userPwd");
        conMap.put("queryId", "");
        conMap.put("vimId", "vimId");
        conMap.put("vimName", "vimName");
        hostQuery = new HostQuery(conMap);
        hostQuery.init();
    }

    @Test
    public void testInitByQueryIdNull() {
        Map<String, String> conMap = new HashMap<String, String>();
        conMap.put("url", "url");
        conMap.put("userName", "userName");
        conMap.put("userPwd", "userPwd");
        conMap.put("queryId", null);
        conMap.put("vimId", "vimId");
        conMap.put("vimName", "vimName");
        hostQuery = new HostQuery(conMap);
        hostQuery.init();
    }
}
