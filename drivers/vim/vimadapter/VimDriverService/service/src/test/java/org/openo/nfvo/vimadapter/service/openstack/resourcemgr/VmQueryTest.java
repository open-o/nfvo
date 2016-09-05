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

public class VmQueryTest {

	private static String basePath = System.getProperty("user.dir");

	private JSONObject jsonObj;

	
	private VmQuery vmQuery;
	
	@Before
	public void setUp(){
		jsonObj = JSONObject.fromObject(TestUtil.ReadFile(basePath + "/src/test/resources" + "/testjson/resource.json"));
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
		vmQuery = new VmQuery(conMap);
	}
	
	@After
	public void tearDown(){
		vmQuery = null;
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
		vmQuery = new VmQuery(conMap);
	}
	
	@Test
	public void testGetVmsRspIsNull(){
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
		assertNull(vmQuery.getVms());
	}
	
	@Test
	public void testGetVmsException(){
		new MockUp<VimRestfulUtil>(){
			@Mock
			public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens,
					boolean isHttps) throws LoginException {
				throw new LoginException();
			}
		};
		assertNull(vmQuery.getVms());
	}
	
	@Test
	public void testGetVmsMap(){
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
				String responseString = jsonObj.getJSONObject("Vm").toString();
				restfulResponse.setResponseJson(responseString);
				return restfulResponse;
			}
		};
		List<JSONObject> result = vmQuery.getVms();
		String expectedResult = "[{\"id\":\"1111\",\"status\":\"active\",\"hostId\":\"huawei\",\"name\":"
				+ "\"huawei\",\"tenantId\":\"123\",\"image\":\"\",\"metadata\":{\"metadata\":\"\"},\"vimId\":\"vimId\",\"vimName\":\"vimName\"}]";
		assertEquals(expectedResult,result.toString());
	}
	
	@Test
	public void testGetVmsMap1(){
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
				String responseString = jsonObj.getJSONObject("Vm1").toString();
				restfulResponse.setResponseJson(responseString);
				return restfulResponse;
			}
		};
		List<JSONObject> result = vmQuery.getVms();
		String expectedResult = "[{\"id\":\"1111\",\"status\":\"active\",\"hostId\":\"huawei\",\"name\":"
				+ "\"huawei\",\"tenantId\":\"123\",\"image\":\"\",\"metadata\":{\"metadata\":\"\"},\"vimId\":\"vimId\",\"vimName\":\"vimName\"}]";
		assertEquals(expectedResult,result.toString());
	}
}
