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

package org.openo.nfvo.vimadapter.service.openstack.networkmgr;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vimadapter.common.LoginException;
import org.openo.nfvo.vimadapter.common.TestUtil;
import org.openo.nfvo.vimadapter.common.servicetoken.VimRestfulUtil;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.OpenstackConnection;

import mockit.Mock;
import mockit.MockUp;
import net.sf.json.JSONObject;

public class OpenstackSubNetTest {

    private static String basePath = System.getProperty("user.dir");

    private JSONObject jsonObj;

    private JSONObject vimJsonObj;

    private OpenstackSubNet openstackSubNet;

    @Before
    public void setUp() {
        jsonObj = JSONObject.fromObject(TestUtil.ReadFile(basePath + "/src/test/resources" + "/testjson/network.json"));
        vimJsonObj = JSONObject.fromObject(TestUtil.ReadFile(basePath + "/src/test/resources" + "/testjson/vim.json"));
        Map<String, String> conMap = new HashMap<String, String>();
        conMap.put("extraInfo", "{}");
        conMap.put("domainName", "domainName");
        conMap.put("authenticMode", "authenticMode");
        conMap.put("url", "url");
        conMap.put("userName", "userName");
        conMap.put("userPwd", "userPwd");
        conMap.put("vimId", "vimId");
        conMap.put("authenticateMode", "authenticateMode");
        conMap.put("vimName", "vimName");
        openstackSubNet = new OpenstackSubNet(conMap);
    }

    @After
    public void tearDown() {
        jsonObj = null;
        vimJsonObj = null;
        openstackSubNet = null;
    }

    @Test
    public void testCreateSubNetRspIsNull() {
        JSONObject network = jsonObj.getJSONObject("NETWORK");
        new MockUp<OpenstackConnection>() {

            @Mock
            public String getServiceUrl(String serviceName) {
                return "12.34.56.78";
            }
        };
        new MockUp<VimRestfulUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
                    boolean isHttps) {
                return null;
            }
        };
        JSONObject result = openstackSubNet.createSubNet(network);
        JSONObject expectedResult = new JSONObject();
        expectedResult.put("reason", "RestfulResponse is null.");
        expectedResult.put("retCode", Constant.REST_FAIL);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testCreateSubNet() {
        JSONObject network = jsonObj.getJSONObject("NETWORK");
        new MockUp<OpenstackConnection>() {

            @Mock
            public String getServiceUrl(String serviceName) {
                return "12.34.56.78";
            }
        };
        new MockUp<VimRestfulUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
                    boolean isHttps) {
                RestfulResponse restfulResponse = new RestfulResponse();
                restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
                String responseString = jsonObj.getJSONObject("NETWORK").toString();
                restfulResponse.setResponseJson(responseString);
                return restfulResponse;
            }
        };
        JSONObject result = openstackSubNet.createSubNet(network);
        String expectedResult = "{\"retCode\":1,\"subnet\":{\"name\":\"project8581_EXT1\",\"allocPools\":"
                + "\"pool1\",\"hostRoutes\":\"route1\",\"cidr\":\"\",\"ipVersion\":\"3.0\",\"gatewayIp\":"
                + "\"12.34\",\"backendId\":\"123\",\"dnsServer\":\"\",\"enableDhcp\":\"False\","
                + "\"id\":\"123\",\"vimVendorId\":\"c174842154151451\",\"networkId\":\"1\",\"rpId\":"
                + "\"1234566\",\"rpName\":\"az1.dc1\",\"vimId\":\"aHR0cHM6Ly9pZGVdG10eS5hejEZGMxLmRvbWFpbm5hbWY29tL2lkZW50aXR5\"}}";
        assertEquals(expectedResult, result.toString());
    }

    @Test
    public void testCreateSubNet1() {
        JSONObject network = jsonObj.getJSONObject("NETWORK");
        new MockUp<OpenstackConnection>() {

            @Mock
            public String getServiceUrl(String serviceName) {
                return "12.34.56.78";
            }
        };
        new MockUp<VimRestfulUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
                    boolean isHttps) {
                RestfulResponse restfulResponse = new RestfulResponse();
                restfulResponse.setStatus(0);
                String responseString = jsonObj.getJSONObject("NETWORK").toString();
                restfulResponse.setResponseJson(responseString);
                return restfulResponse;
            }
        };
        JSONObject result = openstackSubNet.createSubNet(network);
        JSONObject expectedResult = new JSONObject();
        expectedResult.put("networkStatue", Constant.MATAIN_CONFLICT_NETWORK);
        expectedResult.put("reason", "Create subnet OpenStack return fail.");
        expectedResult.put("retCode", Constant.REST_FAIL);
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testCreateSubNetException() {
        JSONObject network = jsonObj.getJSONObject("NETWORK");
        new MockUp<VimRestfulUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
                    boolean isHttps) throws LoginException {
                throw new LoginException();
            }
        };
        JSONObject result = openstackSubNet.createSubNet(network);
        JSONObject expectedResult = new JSONObject();
        expectedResult.put("reason", "OpenStackLoginException");
        expectedResult.put("retCode", Constant.REST_FAIL);
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testGetSubnetNum() {
        JSONObject network = jsonObj.getJSONObject("NETWORK");
        new MockUp<OpenstackConnection>() {

            @Mock
            public String getServiceUrl(String serviceName) {
                return "12.34.56.78";
            }
        };
        new MockUp<VimRestfulUtil>() {

            int count = 0;

            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
                    boolean isHttps) {
                RestfulResponse restfulResponse = new RestfulResponse();
                restfulResponse.setStatus(0);
                String responseString = jsonObj.getJSONObject("NETWORK").toString();
                restfulResponse.setResponseJson(responseString);
                if(count < 3) {
                    count += 1;
                    return restfulResponse;
                }
                return null;
            }
        };
        JSONObject result = openstackSubNet.createSubNet(network);
        JSONObject expectedResult = new JSONObject();
        expectedResult.put("reason", "RestfulResponse is null.");
        expectedResult.put("retCode", Constant.REST_FAIL);
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testGetSubnetNum1() {
        JSONObject network = jsonObj.getJSONObject("NETWORK");
        new MockUp<OpenstackConnection>() {

            @Mock
            public String getServiceUrl(String serviceName) {
                return "12.34.56.78";
            }
        };
        new MockUp<VimRestfulUtil>() {

            int count = 0;

            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
                    boolean isHttps) {
                RestfulResponse restfulResponse = new RestfulResponse();
                restfulResponse.setStatus(0);
                String responseString = jsonObj.getJSONObject("NETWORK").toString();
                restfulResponse.setResponseJson(responseString);
                if(count < 3) {
                    count += 1;
                    return restfulResponse;
                }
                restfulResponse.setStatus(Constant.HTTP_OK_STATUS_CODE);
                return restfulResponse;
            }
        };
        JSONObject result = openstackSubNet.createSubNet(network);
        JSONObject expectedResult = new JSONObject();
        expectedResult.put("networkStatue", Constant.DELETE_FAIL_NETWORK);
        expectedResult.put("retCode", Constant.REST_FAIL);
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testDeleteSubNetRspIsNull() {
        new MockUp<OpenstackConnection>() {

            @Mock
            public String getServiceUrl(String serviceName) {
                return "12.34.56.78";
            }
        };
        new MockUp<VimRestfulUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
                    boolean isHttps) {
                return null;
            }
        };
        int result = openstackSubNet.deleteSubNet("");
        int expectedResult = Constant.HTTP_BAD_REQUEST_STATUS_CODE;
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteSubNet() {
        new MockUp<OpenstackConnection>() {

            @Mock
            public String getServiceUrl(String serviceName) {
                return "12.34.56.78";
            }
        };
        new MockUp<VimRestfulUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
                    boolean isHttps) {
                RestfulResponse restfulResponse = new RestfulResponse();
                restfulResponse.setStatus(Constant.HTTP_BAD_REQUEST_STATUS_CODE);
                String responseString = jsonObj.getJSONObject("NETWORK").toString();
                restfulResponse.setResponseJson(responseString);
                return restfulResponse;
            }
        };
        int result = openstackSubNet.deleteSubNet("");
        int expectedResult = Constant.HTTP_BAD_REQUEST_STATUS_CODE;
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteSubNetExecption() {
        new MockUp<VimRestfulUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
                    boolean isHttps) throws LoginException {
                throw new LoginException();
            }
        };
        int result = openstackSubNet.deleteSubNet("");
        int expectedResult = Constant.INTERNAL_EXCEPTION_STATUS_CODE;
        assertEquals(expectedResult, result);
    }
}
