/*
 * Copyright (c) 2017 Ericsson (China) Communication Co. Ltd.
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

package org.openo.nfvo.vnfmdriver.rest;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mockit.Mock;
import mockit.MockUp;
import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openo.nfvo.vnfmdriver.common.constant.Constant;
import org.openo.nfvo.vnfmdriver.common.restfulutil.HttpContextUitl;
import org.openo.nfvo.vnfmdriver.process.NSLCMServiceProcessor;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Feb 28, 2017
 */
@SuppressWarnings("unchecked")
public class NslcmServiceRestApiTest {

    private NSLCMServiceRestAPI nslcmServiceRestApi;

    /**
     * <br>
     *
     * @throws Exception when the some condition happens
     * @since NFVO 0.5
     */
    @Before
    public void setUp() throws Exception {
        nslcmServiceRestApi = new NSLCMServiceRestAPI();
        NSLCMServiceProcessor nslcmService = new NSLCMServiceProcessor();

        Class<NSLCMServiceRestAPI> cs = NSLCMServiceRestAPI.class;

        Field field = cs.getDeclaredField("nslcmServiceProcessor");
        field.setAccessible(true);
        field.set(nslcmServiceRestApi, nslcmService);

    }

    /**
     * <br>
     *
     * @since NFVO 0.5
     */
    @After
    public void tearDown() {
        Object nslcmServiceRestApi = null;
    }

    /**
     * <br>
     *
     * @throws Exception when the some condition happens
     * @since NFVO 0.5
     */
    @Test
    public void testGrantVnfNotNull() throws Exception {
        MockUp<HttpServletRequest> proxyStub = new MockUp<HttpServletRequest>() {};
        HttpServletRequest mockInstance = proxyStub.getMockInstance();

        MockUp<HttpServletResponse> proxy = new MockUp<HttpServletResponse>() {};
        HttpServletResponse rep = proxy.getMockInstance();

        final JSONObject jsonInstantiateOfReq = new JSONObject();

        new MockUp<HttpContextUitl>() {

            @Mock
            public <T> T extractJsonObject(HttpServletRequest vnfReq) {
                return (T)jsonInstantiateOfReq;
            }
        };

        MockUp<NSLCMServiceProcessor> nslcmServiceProcessor = new MockUp<NSLCMServiceProcessor>() {

            @SuppressWarnings("unchecked")
            @Mock
            public JSONObject grantVnf(JSONObject jsonInstantiateOfReq) {
                JSONObject obj = new JSONObject();
                obj.put(Constant.RETCODE, Constant.REST_FAIL);
                obj.put(Constant.RESP_STATUS, Constant.HTTP_NOTFOUND);
                obj.put(Constant.DATA, "");
                return obj;
            }
        };

        String result = nslcmServiceRestApi.grantVnf(mockInstance, rep);
        nslcmServiceProcessor.tearDown();

        assertEquals("", result);
    }

    /**
     * <br>
     *
     * @throws Exception when the some condition happens
     * @since NFVO 0.5
     */
    @Test
    public void testGrantVnfNull() throws Exception {
        MockUp<HttpServletRequest> proxyStub = new MockUp<HttpServletRequest>() {};
        HttpServletRequest mockInstance = proxyStub.getMockInstance();

        MockUp<HttpServletResponse> proxy = new MockUp<HttpServletResponse>() {};
        HttpServletResponse rep = proxy.getMockInstance();

        new MockUp<HttpContextUitl>() {

            @Mock
            public <T> T extractJsonObject(HttpServletRequest vnfReq) {
                return null;
            }
        };

        String result = nslcmServiceRestApi.grantVnf(mockInstance, rep);

        JSONObject restJson = new JSONObject();

        restJson.put(Constant.RETCODE, Constant.REST_FAIL);
        assertEquals(restJson.toString(), result);
    }

    /**
     * <br>
     *
     * @throws Exception when the some condition happens
     * @since NFVO 0.5
     */
    @Test
    public void testGrantVnf() throws Exception {
        MockUp<HttpServletRequest> proxyStub = new MockUp<HttpServletRequest>() {};
        HttpServletRequest mockInstance = proxyStub.getMockInstance();

        MockUp<HttpServletResponse> proxy = new MockUp<HttpServletResponse>() {};
        HttpServletResponse rep = proxy.getMockInstance();

        final JSONObject jsonInstantiateOfReq = new JSONObject();

        new MockUp<HttpContextUitl>() {

            @Mock
            public <T> T extractJsonObject(HttpServletRequest vnfReq) {
                return (T)jsonInstantiateOfReq;
            }
        };

        MockUp<NSLCMServiceProcessor> nslcmServiceProcessor = new MockUp<NSLCMServiceProcessor>() {

            @SuppressWarnings("unchecked")
            @Mock
            public JSONObject grantVnf(JSONObject jsonInstantiateOfReq) {
                JSONObject ret = new JSONObject();
                JSONObject data = new JSONObject();
                ret.put(Constant.RETCODE, Constant.HTTP_OK);
                data.put("test_key", "test_value");
                ret.put(Constant.DATA, data);
                ret.put(Constant.RESP_STATUS, "200");
                return ret;
            }
        };

        String result = nslcmServiceRestApi.grantVnf(mockInstance, rep);
        nslcmServiceProcessor.tearDown();
        JSONObject restJson = new JSONObject();
        restJson.put("test_key", "test_value");
        assertEquals(restJson.toString(), result);
    }
}
