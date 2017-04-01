/*
 * Copyright Ericsson AB. 2017
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
import org.openo.nfvo.vnfmdriver.common.VnfmUtil;
import org.openo.nfvo.vnfmdriver.common.constant.Constant;
import org.openo.nfvo.vnfmdriver.common.restfulutil.HttpRestfulAPIUtil;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Feb 28, 2017
 */
public class VnfServiceProcessorTest {

    /**
     * <br>
     *
     * @since NFVO 0.5
     */
    @Test
    public void testAddVnfmObjcetNull() {
        new MockUp<VnfmUtil>() {

            @Mock
            public  JSONObject getVnfmById(String vnfmId) {

                return null;
            }
        };

        JSONObject jsonInstantiateOfReq = new JSONObject();
        jsonInstantiateOfReq.put("data", "dummyData");
        VNFServiceProcessor vnfServiceProcessor = new VNFServiceProcessor();
        JSONObject result = vnfServiceProcessor.addVnf("vnfmId", jsonInstantiateOfReq);

        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);
        restJson.put(Constant.RESP_STATUS, Constant.HTTP_NOTFOUND);

        assertEquals(restJson.get(Constant.RETCODE), result.get(Constant.RETCODE));
    }

    /**
     * <br>
     *
     * @since NFVO 0.5
     */
    @Test
    public void testAddVnfObjectNull() {
        String data = "{}";
        JSONObject subJsonObject = JSONObject.fromObject(data);
        VNFServiceProcessor vnfServiceProcessor = new VNFServiceProcessor();
        JSONObject result = vnfServiceProcessor.addVnf("vnfmId", subJsonObject);

        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);
        restJson.put(Constant.RESP_STATUS, Constant.HTTP_NOTFOUND);
        assertEquals(restJson, result);
    }

    /**
     * <br>
     *
     * @since NFVO 0.5
     */
    @Test
    public void testAddVnfNull() {

        new MockUp<VnfmUtil>() {

            @Mock
            public  JSONObject getVnfmById(String vnfmId) {
                JSONObject ret = new JSONObject();
                ret.put("url",Constant.VNF_URL_BASE);
                return ret;
            }
        };

        new MockUp<HttpRestfulAPIUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(String path, String url, String methodType, String params) {

                return null;
            }
        };

        JSONObject jsonInstantiateOfReq = new JSONObject();
        jsonInstantiateOfReq.put("data", "dummyData");
        VNFServiceProcessor vnfServiceProcessor = new VNFServiceProcessor();
        JSONObject result = vnfServiceProcessor.addVnf("vnfmId", jsonInstantiateOfReq);

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
    public  void testDeleteVnfmObjcetNull() {
        new MockUp<VnfmUtil>() {

            @Mock
            public  JSONObject getVnfmById(String vnfmId) {

                return null;
            }
        };

        JSONObject jsonTerminateOfReq = new JSONObject();
        jsonTerminateOfReq.put("data", "dummyData");
        VNFServiceProcessor vnfServiceProcessor = new VNFServiceProcessor();
        JSONObject result = vnfServiceProcessor.deleteVnf("vnfmId", "vnfInstanceId", jsonTerminateOfReq);

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
    public void testDeleteVnfNull() {

        new MockUp<HttpRestfulAPIUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(String path, String url, String methodType, String params) {

                return null;
            }

        };
        new MockUp<VnfmUtil>() {

            @Mock
            public  JSONObject getVnfmById(String vnfmId) {
                JSONObject ret = new JSONObject();
                ret.put("url",Constant.VNF_URL_BASE);
                return ret;
            }
        };

        JSONObject jsonTerminateOfReq = new JSONObject();
        jsonTerminateOfReq.put("data", "dummyData");
        VNFServiceProcessor vnfServiceProcessor = new VNFServiceProcessor();
        JSONObject result = vnfServiceProcessor.deleteVnf("vnfmId", "vnfInstanceId", jsonTerminateOfReq);

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
    public void testDeleteVnfException() {

        new MockUp<HttpRestfulAPIUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(String path, String url, String methodType, String params) {

                throw new JSONException();
            }

        };

        JSONObject jsonTerminateOfReq = new JSONObject();
        jsonTerminateOfReq.put("data", "dummyData");
        VNFServiceProcessor vnfServiceProcessor = new VNFServiceProcessor();
        JSONObject result = vnfServiceProcessor.deleteVnf("vnfmId", "vnfInstanceId", jsonTerminateOfReq);

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
    public void testGetVnfmObjcetNull() {
        new MockUp<VnfmUtil>() {

            @Mock
            public  JSONObject getVnfmById(String vnfmId) {

                return null;
            }
        };

        VNFServiceProcessor vnfServiceProcessor = new VNFServiceProcessor();
        JSONObject result = vnfServiceProcessor.getVnf("vnfmId", "vnfInstanceId");

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
    public void testGetVnfNull() {
        new MockUp<VnfmUtil>() {

            @Mock
            public  JSONObject getVnfmById(String vnfmId) {
                JSONObject ret = new JSONObject();
                ret.put("url",Constant.VNF_URL_BASE);
                return ret;
            }
        };

        new MockUp<HttpRestfulAPIUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(String path, String url, String methodType, String params) {
                return null;
            }

        };

        VNFServiceProcessor vnfServiceProcessor = new VNFServiceProcessor();
        JSONObject result = vnfServiceProcessor.getVnf("vnfmId", "vnfInstanceId");

        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        assertEquals(restJson.get(Constant.RETCODE), result.get(Constant.RETCODE));
    }

    @Test
    public void testGetVnfException() {
        new MockUp<HttpRestfulAPIUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(String path, String url, String methodType, String params) {

                throw new JSONException();
            }

        };

        VNFServiceProcessor vnfServiceProcessor = new VNFServiceProcessor();
        JSONObject result = vnfServiceProcessor.getVnf("vnfmId", "vnfInstanceId");

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
    public  void testGetStatusVnfmObjcetNull() {
        new MockUp<VnfmUtil>() {

            @Mock
            public  JSONObject getVnfmById(String vnfmId) {

                return null;
            }
        };

        VNFServiceProcessor vnfServiceProcessor = new VNFServiceProcessor();
        JSONObject result = vnfServiceProcessor.getStatus("vnfmId", "jobId", "responseId");

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
    public void testGetStatusNull() {
        new MockUp<VnfmUtil>() {

            @Mock
            public  JSONObject getVnfmById(String vnfmId) {
                JSONObject ret = new JSONObject();
                ret.put("url",Constant.VNF_URL_BASE);
                return ret;
            }
        };

        new MockUp<HttpRestfulAPIUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(String path, String url, String methodType, String params) {

                return null;
            }

        };

        VNFServiceProcessor vnfServiceProcessor = new VNFServiceProcessor();
        JSONObject result = vnfServiceProcessor.getStatus("vnfmId", "jobId", "responseId");

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
    public void testGetStatusException() {

        new MockUp<HttpRestfulAPIUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(String path, String url, String methodType, String params) {

                throw new JSONException();
            }

        };

        VNFServiceProcessor vnfServiceProcessor = new VNFServiceProcessor();
        JSONObject result = vnfServiceProcessor.getStatus("jobid", "vnfmid", "responseId");

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
    public void testAddVnf() {
        new MockUp<RestfulResponse>() {

            @Mock
            public String getResponseContent() {
                JSONObject ret = new JSONObject();
                ret.put("test_key", "test_value");
                return ret.toString();
            }
        };

        new MockUp<HttpRestfulAPIUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(String path, String url, String methodType, String params) {
                RestfulResponse rest = new RestfulResponse();
                return rest;
            }
        };
        new MockUp<VnfmUtil>() {

            @Mock
            public JSONObject getVnfmById(String vnfmId) {
                JSONObject ret = new JSONObject();
                ret.put("url",Constant.VNF_URL_BASE);
                return ret;
            }
        };
        JSONObject jsonInstantiateOfReq = new JSONObject();
        jsonInstantiateOfReq.put("data", "dummyData");
        VNFServiceProcessor vnfServiceProcessor = new VNFServiceProcessor();
        JSONObject result = vnfServiceProcessor.addVnf("vnfmId", jsonInstantiateOfReq);

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
    public void testGetStatus() {
        new MockUp<RestfulResponse>() {

            @Mock
            public String getResponseContent() {
                JSONObject ret = new JSONObject();
                ret.put("test_key", "test_value");
                return ret.toString();
            }

        };

        new MockUp<HttpRestfulAPIUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(String path, String url, String methodType, String params) {
                RestfulResponse rest = new RestfulResponse();
                return rest;
            }

        };
        new MockUp<VnfmUtil>() {

            @Mock
            public  JSONObject getVnfmById(String vnfmId) {
                JSONObject ret = new JSONObject();
                ret.put("url",Constant.VNF_URL_BASE);
                return ret;
            }
        };

        VNFServiceProcessor vnfServiceProcessor = new VNFServiceProcessor();
        JSONObject result = vnfServiceProcessor.getStatus("vnfmId", "jobId", "responseId");

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
    public void testGetVnf() {


        new MockUp<HttpRestfulAPIUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(String path, String url, String methodType, String params) {
                RestfulResponse rest = new RestfulResponse();

                return rest;
            }

        };
        new MockUp<RestfulResponse>() {

            @Mock
            public String getResponseContent() {
                JSONObject ret = new JSONObject();
                ret.put("test_key", "test_value");
                return ret.toString();
            }

        };
        new MockUp<VnfmUtil>() {

            @Mock
            public  JSONObject getVnfmById(String vnfmId) {
                JSONObject ret = new JSONObject();
                ret.put("url",Constant.VNF_URL_BASE);
                return ret;
            }
        };

        VNFServiceProcessor vnfServiceProcessor = new VNFServiceProcessor();
        JSONObject result = vnfServiceProcessor.getVnf("vnfmId", "vnfInstanceId");

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
    public void testDeleteVnf() {

        new MockUp<RestfulResponse>() {

            @Mock
            public String getResponseContent() {
                JSONObject ret = new JSONObject();
                ret.put("test_key", "test_value");
                return ret.toString();
            }

        };

        new MockUp<HttpRestfulAPIUtil>() {

            @Mock
            public RestfulResponse getRemoteResponse(String path, String url, String methodType, String params) {
                RestfulResponse rest = new RestfulResponse();
                return rest;
            }

        };

        new MockUp<VnfmUtil>() {

            @Mock
            public  JSONObject getVnfmById(String vnfmId) {
                JSONObject ret = new JSONObject();
                ret.put("url",Constant.VNF_URL_BASE);
                return ret;
            }
        };
        JSONObject jsonTerminateOfReq = new JSONObject();
        jsonTerminateOfReq.put("data", "dummyData");
        VNFServiceProcessor vnfServiceProcessor = new VNFServiceProcessor();
        JSONObject result = vnfServiceProcessor.deleteVnf("vnfmId", "vnfInstanceId", jsonTerminateOfReq);

        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.HTTP_OK);

        assertEquals(restJson.get(Constant.RETCODE), result.get(Constant.RETCODE));
    }
}
