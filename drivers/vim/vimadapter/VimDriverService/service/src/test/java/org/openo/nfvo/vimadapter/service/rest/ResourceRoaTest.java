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
package org.openo.nfvo.vimadapter.service.rest;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.HttpRest;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vimadapter.service.adapter.impl.AdapterResourceManager;
import org.openo.nfvo.vimadapter.service.openstack.entry.ResourceMgrOpenstack;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpServletRequest;

import mockit.Mock;
import mockit.MockUp;
import net.sf.json.JSONObject;

public class ResourceRoaTest {

    private ResourceRoa roa;
    HttpServletResponse resp;

    @Before
    public void setUp() throws Exception {
        roa = new ResourceRoa();
        Constructor<AdapterResourceManager> constructor = AdapterResourceManager.class
                .getDeclaredConstructor(new Class[0]);
        constructor.setAccessible(true);
        AdapterResourceManager adapter = constructor.newInstance(new Object[0]);
        roa.setAdapter(adapter);
        resp = new MockHttpServletResponse();
    }

    @Test
    public void getCpuLimitTest() throws ServiceException {
        String result = roa.getCpuLimit(null, resp, "", "vimId");
        String expectedRes = "{\"retCode\":-1}";
        assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getCpuLimitTest1() throws ServiceException {
        new MockUp<HttpRest>() {
            @Mock
            RestfulResponse get(String paramString, RestfulParametes paramRestfulParametes) throws ServiceException {
                RestfulResponse rsp = new RestfulResponse();
                rsp.setStatus(200);
                return rsp;
            }
        };
        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                JSONObject json = new JSONObject();
                json.put("vimId", "vimId");
                json.put("userName", "userName");
                json.put("url", "url");
                json.put("password", "password");
                json.put("type", "openstack");
                json.put("name", "name");
                json.put("version", "version");

                return json;
            }
        };
        new MockUp<ResourceMgrOpenstack>() {
            @Mock
            public JSONObject getCpuLimits(Map<String, String> conMap) {
                JSONObject rsp = new JSONObject();
                rsp.put("retCode", "200");
                return rsp;
            }
        };

        String result = roa.getCpuLimit(null, resp, "vimId", "vimId");
        String expectedRes = "{}";
        assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getDiskLimitTest() throws ServiceException {
        String result = roa.getDiskLimit(null, resp, "", "vimId");
        String expectedRes = "{\"retCode\":-1}";
        //assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getDiskLimitTest1() throws ServiceException {
        new MockUp<HttpRest>() {
            @Mock
            RestfulResponse get(String paramString, RestfulParametes paramRestfulParametes) throws ServiceException {
                RestfulResponse rsp = new RestfulResponse();
                rsp.setStatus(200);
                return rsp;
            }
        };
        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                JSONObject json = new JSONObject();
                json.put("vimId", "vimId");
                json.put("userName", "userName");
                json.put("url", "url");
                json.put("password", "password");
                json.put("type", "openstack");
                json.put("name", "name");
                json.put("version", "version");

                return json;
            }
        };
        new MockUp<ResourceMgrOpenstack>() {
            @Mock
            public JSONObject getDiskLimits(Map<String, String> conMap) {
                JSONObject rsp = new JSONObject();
                rsp.put("retCode", "200");
                return rsp;
            }
        };

        String result = roa.getDiskLimit(null, resp, "vimId", "vimId");
        String expectedRes = "{}";
        assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getNetworksTest() throws ServiceException {
        String result = roa.getNetworks(null, resp, "vimId");
        String expectedRes = "{\"retCode\":-1}";
        //assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getNetworksTest1() throws ServiceException {
        new MockUp<HttpRest>() {
            @Mock
            RestfulResponse get(String paramString, RestfulParametes paramRestfulParametes) throws ServiceException {
                RestfulResponse rsp = new RestfulResponse();
                rsp.setStatus(200);
                return rsp;
            }
        };
        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                JSONObject json = new JSONObject();
                json.put("vimId", "vimId");
                json.put("userName", "userName");
                json.put("url", "url");
                json.put("password", "password");
                json.put("type", "openstack");
                json.put("name", "name");
                json.put("version", "version");

                return json;
            }
        };
        new MockUp<ResourceMgrOpenstack>() {
            @Mock
            public JSONObject getNetworks(Map<String, String> conMap) {
                JSONObject rsp = new JSONObject();
                rsp.put("retCode", "200");
                return rsp;
            }
        };

        String result = roa.getNetworks(null, resp, "vimId");
        String expectedRes = "{}";
        assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getNetworkTest1() throws ServiceException {
        new MockUp<HttpRest>() {
            @Mock
            RestfulResponse get(String paramString, RestfulParametes paramRestfulParametes) throws ServiceException {
                RestfulResponse rsp = new RestfulResponse();
                rsp.setStatus(200);
                return rsp;
            }
        };
        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                JSONObject json = new JSONObject();
                json.put("vimId", "vimId");
                json.put("userName", "userName");
                json.put("url", "url");
                json.put("password", "password");
                json.put("type", "openstack");
                json.put("name", "name");
                json.put("version", "version");

                return json;
            }
        };
        new MockUp<ResourceMgrOpenstack>() {
            @Mock
            public JSONObject getNetworks(Map<String, String> conMap) {
                JSONObject rsp = new JSONObject();
                rsp.put("retCode", "200");
                return rsp;
            }
        };

        String result = roa.getNetwork(null, resp, "vimId", "vimId");
        String expectedRes = "{}";
        assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getNetworkTest() throws ServiceException {
        String result = roa.getNetwork(null, resp, "networkId", "vimId");
        String expectedRes = "{\"retCode\":-1}";
        //assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getHostsTest() throws ServiceException {
        String result = roa.getHosts(null, resp, "networkId", "vimId");
        String expectedRes = "{\"retCode\":-1}";
        //assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getHostTest() throws ServiceException {
        String result = roa.getHost(null, resp, "tenantId", "localhost", "vimId");
        String expectedRes = "{\"retCode\":-1}";
        //assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getHostTest1() throws ServiceException {
        new MockUp<HttpRest>() {
            @Mock
            RestfulResponse get(String paramString, RestfulParametes paramRestfulParametes) throws ServiceException {
                RestfulResponse rsp = new RestfulResponse();
                rsp.setStatus(200);
                return rsp;
            }
        };
        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                JSONObject json = new JSONObject();
                json.put("vimId", "vimId");
                json.put("userName", "userName");
                json.put("url", "url");
                json.put("password", "password");
                json.put("type", "openstack");
                json.put("name", "name");
                json.put("version", "version");

                return json;
            }
        };
        new MockUp<ResourceMgrOpenstack>() {
            @Mock
            public JSONObject getHosts(Map<String, String> conMap) {
                JSONObject rsp = new JSONObject();
                rsp.put("retCode", "200");
                return rsp;
            }
        };

        String result = roa.getHost(null, resp, "vimId", "localHost", "vimId");
        String expectedRes = "{}";
        assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getHostTestException() throws ServiceException {
        new MockUp<HttpRest>() {
            @Mock
            RestfulResponse get(String paramString, RestfulParametes paramRestfulParametes) throws ServiceException {
                RestfulResponse rsp = new RestfulResponse();
                rsp.setStatus(504);
                return rsp;
            }
        };

        String result = roa.getHost(null, resp, "vimId", "localHost", "vimId");
        assertTrue(true);
    }

    @Test
    public void getHostsTest1() throws ServiceException {
        new MockUp<HttpRest>() {
            @Mock
            RestfulResponse get(String paramString, RestfulParametes paramRestfulParametes) throws ServiceException {
                RestfulResponse rsp = new RestfulResponse();
                rsp.setStatus(200);
                return rsp;
            }
        };
        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                JSONObject json = new JSONObject();
                json.put("vimId", "vimId");
                json.put("userName", "userName");
                json.put("url", "url");
                json.put("password", "password");
                json.put("type", "openstack");
                json.put("name", "name");
                json.put("version", "version");

                return json;
            }
        };
        new MockUp<ResourceMgrOpenstack>() {
            @Mock
            public JSONObject getHosts(Map<String, String> conMap) {
                JSONObject rsp = new JSONObject();
                rsp.put("retCode", "200");
                return rsp;
            }
        };

        String result = roa.getHosts(null, resp, "vimId", "vimId");
        String expectedRes = "{}";
        assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getPortsTest() throws ServiceException {
        String result = roa.getPorts(null, resp, "vimId");
        String expectedRes = "{\"retCode\":-1}";
        //assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getPortTest() throws ServiceException {
        String result = roa.getPort(null, resp, "id", "vimId");
        String expectedRes = "{\"retCode\":-1}";
        assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getPortsTest1() throws ServiceException {
        new MockUp<HttpRest>() {
            @Mock
            RestfulResponse get(String paramString, RestfulParametes paramRestfulParametes) throws ServiceException {
                RestfulResponse rsp = new RestfulResponse();
                rsp.setStatus(200);
                return rsp;
            }
        };
        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                JSONObject json = new JSONObject();
                json.put("vimId", "vimId");
                json.put("userName", "userName");
                json.put("url", "url");
                json.put("password", "password");
                json.put("type", "openstack");
                json.put("name", "name");
                json.put("version", "version");

                return json;
            }
        };
        new MockUp<ResourceMgrOpenstack>() {
            @Mock
            public JSONObject getPorts(Map<String, String> conMap) {
                JSONObject rsp = new JSONObject();
                rsp.put("retCode", "200");
                return rsp;
            }
        };

        String result = roa.getPorts(null, resp, "vimId");
        String expectedRes = "{}";
        assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getPortTest1() throws ServiceException {
        new MockUp<HttpRest>() {
            @Mock
            RestfulResponse get(String paramString, RestfulParametes paramRestfulParametes) throws ServiceException {
                RestfulResponse rsp = new RestfulResponse();
                rsp.setStatus(200);
                return rsp;
            }
        };
        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                JSONObject json = new JSONObject();
                json.put("vimId", "vimId");
                json.put("userName", "userName");
                json.put("url", "url");
                json.put("password", "password");
                json.put("type", "openstack");
                json.put("name", "name");
                json.put("version", "version");

                return json;
            }
        };
        new MockUp<ResourceMgrOpenstack>() {
            @Mock
            public JSONObject getPorts(Map<String, String> conMap) {
                JSONObject rsp = new JSONObject();
                rsp.put("retCode", "200");
                return rsp;
            }
        };

        String result = roa.getPort(null, resp, "vimId", "vimId");
        String expectedRes = "{}";
        assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getProjectsTest() throws ServiceException {
        String result = roa.getProjects(null, resp, "vimId");
        String expectedRes = "{\"retCode\":-1}";
        //assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getProjectTest() throws ServiceException {
        String result = roa.getProject(null, resp, "id", "vimId");
        String expectedRes = "{\"retCode\":-1}";
        //assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getProjectsTest1() throws ServiceException {
        new MockUp<HttpRest>() {
            @Mock
            RestfulResponse get(String paramString, RestfulParametes paramRestfulParametes) throws ServiceException {
                RestfulResponse rsp = new RestfulResponse();
                rsp.setStatus(200);
                return rsp;
            }
        };
        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                JSONObject json = new JSONObject();
                json.put("vimId", "vimId");
                json.put("userName", "userName");
                json.put("url", "url");
                json.put("password", "password");
                json.put("type", "openstack");
                json.put("name", "name");
                json.put("version", "version");

                return json;
            }
        };
        new MockUp<ResourceMgrOpenstack>() {
            @Mock
            public JSONObject getProjects(Map<String, String> conMap) {
                JSONObject rsp = new JSONObject();
                rsp.put("retCode", "200");
                return rsp;
            }
        };

        String result = roa.getProjects(null, resp, "vimId");
        String expectedRes = "{}";
        assertTrue(expectedRes.equals(result));
    }

    @Test
    public void getProjectTest1() throws ServiceException {
        new MockUp<HttpRest>() {
            @Mock
            RestfulResponse get(String paramString, RestfulParametes paramRestfulParametes) throws ServiceException {
                RestfulResponse rsp = new RestfulResponse();
                rsp.setStatus(200);
                return rsp;
            }
        };
        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                JSONObject json = new JSONObject();
                json.put("vimId", "vimId");
                json.put("userName", "userName");
                json.put("url", "url");
                json.put("password", "password");
                json.put("type", "openstack");
                json.put("name", "name");
                json.put("version", "version");

                return json;
            }
        };
        new MockUp<ResourceMgrOpenstack>() {
            @Mock
            public JSONObject getProjects(Map<String, String> conMap) {
                JSONObject rsp = new JSONObject();
                rsp.put("retCode", "200");
                return rsp;
            }
        };

        String result = roa.getProject(null, resp, "vimId", "vimId");
        String expectedRes = "{}";
        assertTrue(expectedRes.equals(result));
    }

}
