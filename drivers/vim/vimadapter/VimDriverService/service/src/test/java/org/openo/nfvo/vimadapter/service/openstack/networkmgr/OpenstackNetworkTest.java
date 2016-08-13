
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

public class OpenstackNetworkTest {

    private static String basePath = System.getProperty("user.dir");

    private JSONObject jsonObj;

    private JSONObject vimJsonObj;

    private OpenstackNetwork openstackNetwork;

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
        openstackNetwork = new OpenstackNetwork(conMap);
    }

    @After
    public void tearDown() {
        jsonObj = null;
        vimJsonObj = null;
        openstackNetwork = null;
    }

    @Test
    public void testCreateNetworkRspIsNull() {
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
        JSONObject result = openstackNetwork.createNetwork(network);
        JSONObject expectedResult = new JSONObject();
        expectedResult.put("reason", "RestfulResponse is null.");
        expectedResult.put("retCode", Constant.REST_FAIL);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testCreateNetwork() {
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
                String responseString =
                        "{\"network\":{\"id\":\"123\"},\"access\":{\"serviceCatalog\":[{\"name\":\"\",\"endpoints\":[{\"publicURL\":\"\",\"region\":\"\"}]}]}}";
                restfulResponse.setResponseJson(responseString);
                return restfulResponse;
            }
        };
        JSONObject result = openstackNetwork.createNetwork(network);
        JSONObject expectedResult = new JSONObject();
        expectedResult.put("retCode", Constant.REST_SUCCESS);
        expectedResult.put("backendId", "123");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testCreateNetwork1() {
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
                restfulResponse.setStatus(Constant.HTTP_CONFLICT_STATUS_CODE);
                String responseString = jsonObj.getJSONObject("NETWORK").toString();
                restfulResponse.setResponseJson(responseString);
                return restfulResponse;
            }
        };
        JSONObject result = openstackNetwork.createNetwork(network);
        JSONObject expectedResult = new JSONObject();
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testCreateNetwork2() {
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
        JSONObject result = openstackNetwork.createNetwork(network);
        JSONObject expectedResult = new JSONObject();
        expectedResult.put("reason", "Create network Openstack return fail.");
        expectedResult.put("retCode", Constant.REST_FAIL);
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testCreateNetworkException() {
        JSONObject network = jsonObj.getJSONObject("NETWORK");
        new MockUp<VimRestfulUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
                    boolean isHttps) throws LoginException {
                throw new LoginException();
            }
        };
        JSONObject result = openstackNetwork.createNetwork(network);
        JSONObject expectedResult = new JSONObject();
        expectedResult.put("reason", "OpenStackLoginException");
        expectedResult.put("retCode", Constant.REST_FAIL);
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testGetNetworkFromOpenStack() {
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
                restfulResponse.setStatus(Constant.HTTP_CONFLICT_STATUS_CODE);
                String responseString = jsonObj.getJSONObject("NETWORK").toString();
                restfulResponse.setResponseJson(responseString);
                if(count < 3) {
                    count += 1;
                    return restfulResponse;
                }
                return null;
            }
        };
        JSONObject result = openstackNetwork.createNetwork(network);
        JSONObject expectedResult = new JSONObject();
        expectedResult.put("reason", "RestfulResponse is null.");
        expectedResult.put("retCode", Constant.REST_FAIL);
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testGetNetworkFromOpenStack1() {
        JSONObject network = jsonObj.getJSONObject("NETWORKFLAT");
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
                restfulResponse.setStatus(Constant.HTTP_CONFLICT_STATUS_CODE);
                String responseString = jsonObj.getJSONObject("NETWORKFLAT").toString();
                restfulResponse.setResponseJson(responseString);
                return restfulResponse;
            }
        };
        JSONObject result = openstackNetwork.createNetwork(network);
        JSONObject expectedResult = new JSONObject();
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testGetNetworkFromOpenStack2() {
        JSONObject network = jsonObj.getJSONObject("NETWORKFLAT");
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
                restfulResponse.setStatus(Constant.HTTP_CONFLICT_STATUS_CODE);
                String responseString = jsonObj.getJSONObject("NETWORKFLAT").toString();
                restfulResponse.setResponseJson(responseString);
                if(count < 3) {
                    count += 1;
                    return restfulResponse;
                }
                return null;
            }
        };
        JSONObject result = openstackNetwork.createNetwork(network);
        JSONObject expectedResult = new JSONObject();
        expectedResult.put("reason", "RestfulResponse is null.");
        expectedResult.put("retCode", Constant.REST_FAIL);
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testGetNetworkFromOpenStack3() {
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
                restfulResponse.setStatus(Constant.HTTP_CONFLICT_STATUS_CODE);
                String responseString = jsonObj.getJSONObject("NETWORK").toString();
                restfulResponse.setResponseJson(responseString);
                if(count < 3) {
                    count += 1;
                    return restfulResponse;
                } else {
                    restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
                    return restfulResponse;
                }
            }
        };
        JSONObject result = openstackNetwork.createNetwork(network);
        JSONObject expectedResult = new JSONObject();
        expectedResult.put("retCode", Constant.REST_SUCCESS);
        expectedResult.put("backendId", "123");
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testGetNetworkFromOpenStack4() {
        JSONObject network = jsonObj.getJSONObject("NETWORKFLAT");
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
                restfulResponse.setStatus(Constant.HTTP_CONFLICT_STATUS_CODE);
                String responseString = jsonObj.getJSONObject("NETWORKFLAT").toString();
                restfulResponse.setResponseJson(responseString);
                if(count < 3) {
                    count += 1;
                    return restfulResponse;
                } else {
                    restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
                    return restfulResponse;
                }
            }
        };
        JSONObject result = openstackNetwork.createNetwork(network);
        JSONObject expectedResult = new JSONObject();
        expectedResult.put("retCode", Constant.REST_SUCCESS);
        expectedResult.put("backendId", "1");
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testRemoveNetwork() {
        JSONObject network = jsonObj.getJSONObject("NETWORKFLAT");
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
        int result = openstackNetwork.removeNetwork(network);
        int expectedResult = Constant.HTTP_BAD_REQUEST_STATUS_CODE;
        assertEquals(expectedResult, result);
    }

    @Test
    public void testRemoveNetwork1() {
        JSONObject network = jsonObj.getJSONObject("NETWORKFLAT");
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
                restfulResponse.setStatus(Constant.HTTP_NOTFOUND_STATUS_CODE);
                String responseString = jsonObj.getJSONObject("NETWORKFLAT").toString();
                restfulResponse.setResponseJson(responseString);
                return restfulResponse;

            }
        };
        int result = openstackNetwork.removeNetwork(network);
        int expectedResult = Constant.HTTP_NOTFOUND_STATUS_CODE;
        assertEquals(expectedResult, result);
    }

    @Test
    public void testRemoveNetwork2() {
        JSONObject network = jsonObj.getJSONObject("NETWORKFLAT");
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
                String responseString = jsonObj.getJSONObject("NETWORKFLAT").toString();
                restfulResponse.setResponseJson(responseString);
                return restfulResponse;

            }
        };
        int result = openstackNetwork.removeNetwork(network);
        int expectedResult = 0;
        assertEquals(expectedResult, result);
    }

    @Test
    public void testRemoveNetworkException() {
        JSONObject network = jsonObj.getJSONObject("NETWORKFLAT");
        new MockUp<VimRestfulUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
                    boolean isHttps) throws LoginException {
                throw new LoginException();

            }
        };
        int result = openstackNetwork.removeNetwork(network);
        int expectedResult = Constant.INTERNAL_EXCEPTION_STATUS_CODE;
        assertEquals(expectedResult, result);
    }
}
