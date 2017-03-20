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

package org.openo.nfvo.vnfmdriver.process;

import static org.junit.Assert.assertEquals;

import mockit.Mock;
import mockit.MockUp;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vnfmdriver.common.constant.Constant;
import org.openo.nfvo.vnfmdriver.common.restfulutil.HttpRestfulAPIUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Feb 28, 2017
 */
public class NsclmServiceProcessorTest {

    private static final Logger LOG = LoggerFactory.getLogger(NsclmServiceProcessorTest.class);

    /**
     * <br>
     *
     * @since NFVO 0.5
     */
    @Test
    public void testGrantVnf() {
        new MockUp<RestfulResponse>() {

            @Mock
            public String getResponseContent() {
                JSONObject ret = new JSONObject();
                ret.put(Constant.RETCODE, Constant.HTTP_OK);
                return ret.toString();
            }
        };

        new MockUp<HttpRestfulAPIUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(String url, String methodType, String params) {
                RestfulResponse rest = new RestfulResponse();
                return rest;
            }
        };

        JSONObject jsonInstantiateOfReq = new JSONObject();
        jsonInstantiateOfReq.put("data", "dummyData");
        NSLCMServiceProcessor nslcmServiceProcessor = new NSLCMServiceProcessor();
        JSONObject result = nslcmServiceProcessor.grantVnf(jsonInstantiateOfReq);

        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.HTTP_OK);

        assertEquals(restJson.get(Constant.RETCODE), result.get(Constant.RETCODE));
    }

    /**
     * <br>
     *
     * @since NFVO 0.5
     */
    @Test
    public void testGrantVnfNull() {

        new MockUp<HttpRestfulAPIUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(String url, String methodType, String params) {

                return null;
            }
        };

        JSONObject jsonInstantiateOfReq = new JSONObject();
        jsonInstantiateOfReq.put("data", "dummyData");
        NSLCMServiceProcessor nsclmServiceProcessor = new NSLCMServiceProcessor();
        JSONObject result = nsclmServiceProcessor.grantVnf(jsonInstantiateOfReq);

        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        assertEquals(restJson.get(Constant.RETCODE), result.get(Constant.RETCODE));
    }

    /**
     * <br>
     *
     * @since NFVO 0.5
     */
    @Test
    public void testGrantVnfException() {

        new MockUp<HttpRestfulAPIUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(String url, String methodType, String params) {

                throw new JSONException();
            }
        };

        JSONObject jsonInstantiateOfReq = new JSONObject();
        jsonInstantiateOfReq.put("data", "dummyData");
        NSLCMServiceProcessor nsclmServiceProcessor = new NSLCMServiceProcessor();
        JSONObject result = nsclmServiceProcessor.grantVnf(jsonInstantiateOfReq);

        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        assertEquals(restJson.get(Constant.RETCODE), result.get(Constant.RETCODE));
    }
}
