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

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.nfvo.vimadapter.service.adapter.impl.AdapterNetworkManager;
import org.springframework.mock.web.MockHttpServletRequest;

import mockit.Mock;
import mockit.MockUp;
import net.sf.json.JSONObject;

public class NetworkRoaTest {

    private NetworkRoa roa;
    private HttpServletResponse resp;

    @Before
    public void setUp() {
        roa = new NetworkRoa();
        resp = new MockHttpServletResponse();
        roa.setAdapter(new AdapterNetworkManager());
    }

    @Test
    public void addNetworkTest() throws ServiceException {
        new MockUp<IOUtils>() {
            @Mock
            public String toString(InputStream input) throws IOException {
                return "data";
            }
        };

        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                JSONObject json = new JSONObject();
                return json;
            }
        };
        HttpServletRequest mock = new MockHttpServletRequest();
        String result = roa.addNetwork(mock, resp, "vimID");
        String expectedRes = "{}";
        assertTrue(expectedRes.equals(result));
    }

    @Test
    public void addNetworkTestBranch() throws ServiceException {
        new MockUp<IOUtils>() {
            @Mock
            public String toString(InputStream input) throws IOException {
                return "data";
            }
        };

        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                JSONObject json = new JSONObject();
                json.put("key", "value");
                return json;
            }
        };
        HttpServletRequest mock = new MockHttpServletRequest();
        //String result = roa.addNetwork(mock, resp, "vimID");

    }

    @Test
    public void addNetworkTestBranch1() throws ServiceException {
        new MockUp<IOUtils>() {
            @Mock
            public String toString(InputStream input) throws IOException {
                return "data";
            }
        };
        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                JSONObject json = new JSONObject();
                json.put("key", "value");
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
        HttpServletRequest mock = new MockHttpServletRequest();
        String result = roa.addNetwork(mock, resp, "");
    }

    @Test
    public void addNetworkTestBranch2() throws ServiceException {
        new MockUp<IOUtils>() {
            @Mock
            public String toString(InputStream input) throws IOException {
                return "data";
            }
        };

        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                return null;
            }
        };
        HttpServletRequest mock = new MockHttpServletRequest();
        String result = roa.addNetwork(mock, resp, "vimId");
    }

    @Test
    public void addNetworkTestBranch3() throws ServiceException {
        new MockUp<IOUtils>() {
            @Mock
            public String toString(InputStream input) throws IOException {
                return "data";
            }
        };

        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                return null;
            }
        };
        HttpServletRequest mock = new MockHttpServletRequest();
        String result = roa.addNetwork(mock, resp, null);
    }

    @Test
    public void delNetworkTest() throws ServiceException {
//        String result = roa.delNetwork(null, resp, "networkId", "vimId");
//        String expectedRes = "{}";
//        assertTrue(expectedRes.equals(result));
    }

    @Test
    public void delNetworkTestBranch() throws ServiceException {
        String result = roa.delNetwork(null, resp, "networkId", "");
        String expectedRes = "{}";
        assertTrue(expectedRes.equals(result));
    }

    @Test
    public void delNetworkTestBranch1() throws ServiceException {
        String result = roa.delNetwork(null, resp, "", "vimId");
        String expectedRes = "{}";
        assertTrue(expectedRes.equals(result));
    }

    @Test
    public void delNetworkTestBranch2() throws ServiceException {
        String result = roa.delNetwork(null, resp, "networkId", null);
        String expectedRes = "{}";
        assertTrue(expectedRes.equals(result));
    }

    @Test
    public void delNetworkTestBranch3() throws ServiceException {
        String result = roa.delNetwork(null, resp, null, "vimId");
        String expectedRes = "{}";
        assertTrue(expectedRes.equals(result));
    }

}
