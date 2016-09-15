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
package org.openo.nfvo.vimadapter.service.openstack.networkmgr;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.HttpRest;
import org.openo.baseservice.roa.util.restclient.RestfulOptions;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.OpenstackConnection;

import mockit.Mock;
import mockit.MockUp;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class OpenStackNetworkTest {

    OpenstackNetwork openstackNetwork;

    @Before
    public void setUp() {
        Map<String, String> conMap = new HashMap<>();
        conMap.put("url", "http://1.1.1.1:2345");
        conMap.put("userName", "userName");
        conMap.put("userPwd", "userPwd");
        conMap.put("vimId", "vimId");
        conMap.put("vimName", "vimName");
        conMap.put("neutron", "neutron");
        openstackNetwork = new OpenstackNetwork(conMap);

    }

    @Test
    public void testCreateNetwork() {
        /*
         * new MockUp<OpenstackConnection>() {
         *
         * @Mock public int connect() { return Constant.HTTP_OK_STATUS_CODE; }
         * };
         */
        new MockUp<HttpRest>() {
            @Mock
            public RestfulResponse post(String servicePath, RestfulParametes restParametes, RestfulOptions option)
                    throws ServiceException {
                RestfulResponse rsp = new RestfulResponse();
                rsp.setStatus(200);
                return rsp;
            }
        };
        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                JSONObject json = new JSONObject();
                JSONObject networkJson = new JSONObject();
                JSONObject finalJson = new JSONObject();
                finalJson.put("id", "id");
                JSONObject tenant = new JSONObject();
                tenant.put("id", "id");
                finalJson.put("tenant", tenant);
                networkJson.put("token", finalJson);
                JSONArray serviceCatalog = new JSONArray();
                JSONObject json1 = new JSONObject();
                json1.put("endpoints", new JSONArray());
                json1.put("name", "name");
                serviceCatalog.add(json1);
                networkJson.put("serviceCatalog", serviceCatalog);
                json.put("access", networkJson);

                return json;
            }
        };
        new MockUp<OpenstackConnection>() {
            @Mock
            public String getServiceUrl(String serviceName) {
                return "http://1.1.1.1:2345";

            }
        };
        JSONObject network = new JSONObject();
        openstackNetwork.createNetwork(network);
    }

    @Test
    public void testRemoveNetwork() {
        /*
         * new MockUp<OpenstackConnection>() {
         *
         * @Mock public int connect() { return Constant.HTTP_OK_STATUS_CODE; }
         * };
         */
        new MockUp<HttpRest>() {
            @Mock
            public RestfulResponse post(String servicePath, RestfulParametes restParametes, RestfulOptions option)
                    throws ServiceException {
                RestfulResponse rsp = new RestfulResponse();
                rsp.setStatus(200);
                return rsp;
            }
        };
        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                JSONObject json = new JSONObject();
                JSONObject networkJson = new JSONObject();
                JSONObject finalJson = new JSONObject();
                finalJson.put("id", "id");
                JSONObject tenant = new JSONObject();
                tenant.put("id", "id");
                finalJson.put("tenant", tenant);
                networkJson.put("token", finalJson);
                JSONArray serviceCatalog = new JSONArray();
                JSONObject json1 = new JSONObject();
                json1.put("endpoints", new JSONArray());
                json1.put("name", "name");
                serviceCatalog.add(json1);
                networkJson.put("serviceCatalog", serviceCatalog);
                json.put("access", networkJson);

                return json;
            }
        };
        new MockUp<OpenstackConnection>() {
            @Mock
            public String getServiceUrl(String serviceName) {
                return "http://1.1.1.1:2345";

            }
        };
        openstackNetwork.removeNetwork("id");
    }

    @Test
    public void testRemoveNetwork1() {

        new MockUp<HttpRest>() {
            @Mock
            public RestfulResponse post(String servicePath, RestfulParametes restParametes, RestfulOptions option)
                    throws ServiceException {
                RestfulResponse rsp = new RestfulResponse();
                rsp.setStatus(200);
                return rsp;
            }
        };
        new MockUp<HttpRest>() {
            @Mock
            public RestfulResponse delete(String servicePath, RestfulParametes restParametes, RestfulOptions option)
                    throws ServiceException {
                RestfulResponse rsp = new RestfulResponse();
                rsp.setStatus(200);
                return rsp;
            }
        };
        new MockUp<JSONObject>() {
            @Mock
            public JSONObject fromObject(Object object) {
                JSONObject json = new JSONObject();
                JSONObject networkJson = new JSONObject();
                JSONObject finalJson = new JSONObject();
                finalJson.put("id", "id");
                JSONObject tenant = new JSONObject();
                tenant.put("id", "id");
                finalJson.put("tenant", tenant);
                networkJson.put("token", finalJson);
                JSONArray serviceCatalog = new JSONArray();
                JSONObject json1 = new JSONObject();
                json1.put("endpoints", new JSONArray());
                json1.put("name", "name");
                serviceCatalog.add(json1);
                networkJson.put("serviceCatalog", serviceCatalog);
                json.put("access", networkJson);

                return json;
            }
        };
        new MockUp<OpenstackConnection>() {
            @Mock
            public String getServiceUrl(String serviceName) {
                return "http://1.1.1.1:2345";

            }
        };
        openstackNetwork.removeNetwork("id");
    }
}
