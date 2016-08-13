package org.openo.nfvo.vimadapter.service.openstack.connectmgr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vimadapter.common.LoginException;
import org.openo.nfvo.vimadapter.common.TestUtil;
import org.openo.nfvo.vimadapter.common.servicetoken.VimRestfulUtil;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.entity.Vim;
import org.openo.nfvo.vimadapter.service.openstack.api.ConnectInfo;

import mockit.Mock;
import mockit.MockUp;
import net.sf.json.JSONObject;

public class OpenstackConnectionTest {

	private String basePath;

	private OpenstackConnection openstackConnection;	
	
	private JSONObject vimJsonObj;



	@Before
	public void setUp() {
		basePath = System.getProperty("user.dir");
		vimJsonObj = JSONObject.fromObject(TestUtil.ReadFile(basePath + "/src/test/resources" + "/testjson/vim.json"));
		JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim("111", vimJson, "ACTIVE");
        ConnectInfo info = new ConnectInfo(vim);
		openstackConnection = new OpenstackConnection(info);
	}

	@Test
	public void testConnectRspIsNull() {		
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				return null;
			}
		}; 
		int result = openstackConnection.connect();
		int expectedResult = Constant.INTERNAL_EXCEPTION_STATUS_CODE;
		assertEquals(expectedResult,result);
	}
	
	@Test
	public void testConnect1() {		
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
				String responseString = "{\"access\":{\"serviceCatalog\":[{\"name\":\"\",\"endpoints\":[{\"publicURL\":\"\",\"region\":\"\"}]}]}}";
				restfulResponse.setResponseJson(responseString);
				return restfulResponse;
			}
		}; 
		int result = openstackConnection.connect();
		int expectedResult = Constant.ACCESS_OBJ_NULL_STATUS_CODE;
		assertEquals(expectedResult,result);
	}
	
	@Test
	public void testConnect2() {		
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
				String responseString = "{\"access\":{\"token\":{\"id\":\"id\",\"tenant\":{\"id\":\"id\"}}}}";
				restfulResponse.setResponseJson(responseString);
				return restfulResponse;
			}
		}; 
		int result = openstackConnection.connect();
		int expectedResult = Constant.SERVICE_URL_ERROR_STATUS_CODE;
		assertEquals(expectedResult,result);
	}
	
	@Test
	public void testConnect3() {		
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
				String responseString = "{\"access\":{\"token\":{\"id\":\"id\",\"tenant\":{\"id\":\"id\"}},"
						+ "\"serviceCatalog\":[{\"name\":\"\",\"endpoints\":[{\"publicURL\":\"\",\"region\":\"\"}]}]}}";
				restfulResponse.setResponseJson(responseString);
				return restfulResponse;
			}
		}; 
		int result = openstackConnection.connect();
		int expectedResult = Constant.HTTP_CREATED_STATUS_CODE;
		assertEquals(expectedResult,result);
	}
	
	@Test
	public void testHandleEndpointUrl() {		
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
				String responseString = "{\"access\":{\"token\":{\"id\":\"id\",\"tenant\":{\"id\":\"id\"}},"
						+ "\"serviceCatalog\":[{\"name\":\"\",\"endpoints\":[{\"publicURL\":\"12.23.34.45/\",\"region\":\"\"}]}]}}";
				restfulResponse.setResponseJson(responseString);
				return restfulResponse;
			}
		}; 
		int result = openstackConnection.connect();
		int expectedResult = Constant.HTTP_CREATED_STATUS_CODE;
		assertEquals(expectedResult,result);
	}
	
	@Test
	public void testHandleEndpointUrl1() {		
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
				String responseString = "{\"access\":{\"token\":{\"id\":\"id\",\"tenant\":{\"id\":\"id\"}},"
						+ "\"serviceCatalog\":[{\"name\":\"\",\"endpoints\":[{\"publicURL\":\"12.23.34.45/v2.0\",\"region\":\"\"}]}]}}";
				restfulResponse.setResponseJson(responseString);
				return restfulResponse;
			}
		}; 
		int result = openstackConnection.connect();
		int expectedResult = Constant.HTTP_CREATED_STATUS_CODE;
		assertEquals(expectedResult,result);
	}
	
	@Test
	public void testIsConnected() {		
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				return null;
			}
		}; 
		int result = openstackConnection.isConnected();
		int expectedResult = Constant.HTTP_BAD_REQUEST_STATUS_CODE;
		assertEquals(expectedResult,result);
	}
	
	@Test
	public void testIsConnected1() {		
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_OK_STATUS_CODE);
				return restfulResponse;
			}
		}; 
		int result = openstackConnection.isConnected();
		int expectedResult = Constant.HTTP_OK_STATUS_CODE;
		assertEquals(expectedResult,result);
	}
	
	@Test
	public void testIsConnected2() {		
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_UNAUTHORIZED_STATUS_CODE);
				return restfulResponse;
			}
		}; 
		int result = openstackConnection.isConnected();
		int expectedResult = Constant.HTTP_OK_STATUS_CODE;
		assertEquals(expectedResult,result);
	}
	
	@Test
	public void testIsConnected3() {		
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_BAD_REQUEST_STATUS_CODE);
				return restfulResponse;
			}
		}; 
		int result = openstackConnection.isConnected();
		int expectedResult = Constant.HTTP_BAD_REQUEST_STATUS_CODE;
		assertEquals(expectedResult,result);
	}
	
	@Test
	public void testDisconnect() {		
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_BAD_REQUEST_STATUS_CODE);
				return restfulResponse;
			}
		}; 
		openstackConnection.disconnect();
	}
	
	@Test
	public void testDisconnect1() {
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
				String responseString = "{\"access\":{\"token\":{\"id\":\"id\",\"tenant\":{\"id\":\"id\"}}}}";
				restfulResponse.setResponseJson(responseString);
				return restfulResponse;
			}
		};
		openstackConnection.connect();
		openstackConnection.disconnect();
		assertNull(openstackConnection.getDomainTokens());
	}	

	@Test
	public void testDisconnectExceptions() {
		new MockUp<VimRestfulUtil>(){
			int count = 0;
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) throws LoginException {
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
				String responseString = "{\"access\":{\"token\":{\"id\":\"id\",\"tenant\":{\"id\":\"id\"}}}}";
				restfulResponse.setResponseJson(responseString);
				if (count<1){
					count+=1;
					return restfulResponse;
				}
				else throw new LoginException();
			}
		};
		openstackConnection.connect();
		openstackConnection.disconnect();
		assertNull(openstackConnection.getDomainTokens());
	}
	
	@Test
	public void testDeleteTokens() {
		new MockUp<VimRestfulUtil>(){
			int count = 0;
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) {
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
				String responseString = "{\"access\":{\"token\":{\"id\":\"id\",\"tenant\":{\"id\":\"id\"}}}}";
				restfulResponse.setResponseJson(responseString);
				if (count<1){
					count+=1;
					return restfulResponse;
				}
				else return null;
			}
		};
		openstackConnection.connect();
		openstackConnection.disconnect();
		assertNull(openstackConnection.getDomainTokens());
	}
	
	@Test
	public void testIsNeedRenewInfo() {
		JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim("111", vimJson, "ACTIVE");
        ConnectInfo info = new ConnectInfo(vim);
		boolean result = openstackConnection.isNeedRenewInfo(info);
		assertFalse(result);
	}
	
	@Test
	public void testSetConnInfo() {
		JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim("111", vimJson, "ACTIVE");
        ConnectInfo info = new ConnectInfo(vim);
		openstackConnection.setConnInfo(info);
	}
	
	@Test
	public void testGetConnInfo() {
		openstackConnection.getConnInfo();
	}
	
	@Test
	public void testGetDomainTokens() {
		openstackConnection.getDomainTokens();
	}
	
	@Test
	public void testGetProjectId() {
		openstackConnection.getProjectId();
	}
	
	@Test
	public void testGetUrlMap() {
		OpenstackConnection openstackConnection1 = new OpenstackConnection();
		openstackConnection1.getUrlMap();
	}
}
