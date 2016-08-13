package org.openo.nfvo.vimadapter.service.openstack.resourcemgr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.List;
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

public class RpQueryTest {

	private static String basePath = System.getProperty("user.dir");

	private JSONObject jsonObj;

	private JSONObject vimJsonObj;
	
	private RpQuery rpQuery;
	
	@Before
	public void setUp(){
		jsonObj = JSONObject.fromObject(TestUtil.ReadFile(basePath + "/src/test/resources" + "/testjson/resource.json"));
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
		rpQuery = new RpQuery(conMap);
	}
	
	@After
	public void tearDown(){
		rpQuery = null;
	}
	
	@Test
	public void testGetRpsRspIsNull(){
		new MockUp<OpenstackConnection>(){
			@Mock
		    public String getServiceUrl(String serviceName) {
				return "12.34.56.78";
			}
		};
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				return null;
			}
		};
		assertNull(rpQuery.getRps());
	}

	@Test
	public void testGetRps(){
		new MockUp<OpenstackConnection>(){
			@Mock
			public int connect()  {
				return Constant.HTTP_OK_STATUS_CODE ;
			}
			@Mock
		    public String getServiceUrl(String serviceName) {
				return "12.34.56.78";
			}
		};
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
				String responseString = jsonObj.getJSONObject("Rp").toString();
				restfulResponse.setResponseJson(responseString);
				return restfulResponse;
			}
		};
		List<JSONObject> result = rpQuery.getRps();
		String expectedResult = "[{\"total\":{\"vcpus\":\"100\",\"cpumhz\":\"260000\",\"memory\":\"100\","
				+ "\"disk\":\"100\"},\"used\":{\"vcpus\":\"100\",\"cpumhz\":\"260000\",\"memory\":\"50\","
				+ "\"disk\":\"50\"},\"name\":\"34.56\",\"id\":\"vimId-34.56\",\"tenantName\":\"admin\","
				+ "\"status\":\"active\",\"vimId\":\"vimId\",\"vimName\":\"vimName\"}]";
		assertEquals(expectedResult,result.toString());
	}
	
	@Test
	public void testGetRpsException(){
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) throws LoginException {
				throw new LoginException();
			}
		};
		assertNull(rpQuery.getRps());
	}
	
	@Test
	public void testGetRpsMap(){
		new MockUp<OpenstackConnection>(){
			@Mock
		    public String getServiceUrl(String serviceName) {
				return "12.34";
			}
		};
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
				String responseString = jsonObj.getJSONObject("Rp").toString();
				restfulResponse.setResponseJson(responseString);
				return restfulResponse;
			}
		};
		List<JSONObject> result = rpQuery.getRps();
		assertNull(result);
	}
	

	@Test
	public void testGetRpStatus(){
		new MockUp<OpenstackConnection>(){
			@Mock
			public int connect()  {
				return Constant.HTTP_OK_STATUS_CODE ;
			}
			@Mock
		    public String getServiceUrl(String serviceName) {
				return "12.34.56.78";
			}
		};
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
				String responseString = jsonObj.getJSONObject("Rp").toString();
				restfulResponse.setResponseJson(responseString);
				return restfulResponse;
			}
		};
		List<JSONObject> result = rpQuery.getRps();
		String expectedResult = "[{\"total\":{\"vcpus\":\"100\",\"cpumhz\":\"260000\",\"memory\":\"100\","
				+ "\"disk\":\"100\"},\"used\":{\"vcpus\":\"100\",\"cpumhz\":\"260000\",\"memory\":\"50\","
				+ "\"disk\":\"50\"},\"name\":\"34.56\",\"id\":\"vimId-34.56\",\"tenantName\":\"admin\","
				+ "\"status\":\"active\",\"vimId\":\"vimId\",\"vimName\":\"vimName\"}]";
		assertEquals(expectedResult,result.toString());
	}
	
	@Test
	public void testGetRpStatus1(){
		new MockUp<OpenstackConnection>(){
			@Mock
			public int connect()  {
				return Constant.HTTP_OK_STATUS_CODE ;
			}
			@Mock
		    public String getServiceUrl(String serviceName) {
				return "12.34.56.78";
			}
		};
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
				String responseString = jsonObj.getJSONObject("Rp1").toString();
				restfulResponse.setResponseJson(responseString);
				return restfulResponse;
			}
		};
		List<JSONObject> result = rpQuery.getRps();
		String expectedResult = "[{\"total\":{\"vcpus\":\"100\",\"cpumhz\":\"260000\",\"memory\":\"100\","
				+ "\"disk\":\"100\"},\"used\":{\"vcpus\":\"100\",\"cpumhz\":\"260000\",\"memory\":\"50\","
				+ "\"disk\":\"50\"},\"name\":\"34.56\",\"id\":\"vimId-34.56\",\"tenantName\":\"admin\","
				+ "\"status\":\"active\",\"vimId\":\"vimId\",\"vimName\":\"vimName\"}]";
		assertEquals(expectedResult,result.toString());
	}
}
