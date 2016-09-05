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

public class NetworkQueryTest {
	private static String basePath = System.getProperty("user.dir");

	private JSONObject jsonObj;

	private JSONObject vimJsonObj;
	
	private NetworkQuery networkQuery;
	
	@Before
	public void setUp(){
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
		conMap.put("queryId", "queryId");
		networkQuery = new NetworkQuery(conMap);
	}
	
	@After
	public void tearDown(){
		networkQuery = null;
	}
	
	@Test
	public void testInit(){
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
		networkQuery = new NetworkQuery(conMap);
	}
	
	@Test
	public void testGetNetworksRspIsNull(){
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
		assertNull(networkQuery.getNetworks());
	}

	@Test
	public void testGetNetworks(){
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
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
				String responseString = jsonObj.getJSONObject("NETWORK").toString();
				restfulResponse.setResponseJson(responseString);
				return restfulResponse;
			}
		};
		List<JSONObject> result = networkQuery.getNetworks();
		String expectedResult = "[{\"vimId\":\"vimId\",\"vimName\":\"vimName\",\"id\":\"123\",\"shared\":\"true\","
				+ "\"subnets\":[],\"name\":\"123\",\"tenantId\":\"123\",\"status\":\"\",\"provider\":{\"networkType\":"
				+ "\"openstack\",\"physicalNetwork\":\"\",\"segmentationId\":\"4005\"},\"router\":{\"external\":\"123\"}}]";
		assertEquals(expectedResult,result.toString());
	}
	
	@Test
	public void testGetNetworksException(){
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) throws LoginException {
				throw new LoginException();
			}
		};
		assertNull(networkQuery.getNetworks());
	}
	
	@Test
	public void testGetNetworksMap(){
		Map<String, String> conMap = new HashMap<String, String>();
		conMap.put("extraInfo", "{}");
		conMap.put("domainName", "domainName");
		conMap.put("authenticMode", "authenticMode");
		conMap.put("url", "/v2.0/newworks");
		conMap.put("userName", "userName");
		conMap.put("userPwd", "userPwd");
		conMap.put("vimId", "vimId");
		conMap.put("authenticateMode", "authenticateMode");
		conMap.put("vimName", "vimName");
		NetworkQuery networkQuery1 = new NetworkQuery(conMap);
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
				RestfulResponse restfulResponse = new RestfulResponse();
				restfulResponse.setStatus(Constant.HTTP_CREATED_STATUS_CODE);
				String responseString = jsonObj.getJSONObject("NETWORK").toString();
				restfulResponse.setResponseJson(responseString);
				return restfulResponse;
			}
		};
		List<JSONObject> result = networkQuery1.getNetworks();
		String expectedResult = "[{\"vimId\":\"vimId\",\"vimName\":\"vimName\",\"id\":\"123\",\"shared\":\"true\","
				+ "\"subnets\":[],\"name\":\"123\",\"tenantId\":\"123\",\"status\":\"\",\"provider\":{\"networkType\":"
				+ "\"openstack\",\"physicalNetwork\":\"\",\"segmentationId\":\"4005\"},\"router\":{\"external\":\"123\"}}]";
		assertEquals(expectedResult,result.toString());
	}
}
