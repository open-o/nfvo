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

package org.openo.nfvo.vnfmadapter.service.process;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openo.nfvo.vnfmadapter.common.ResultRequestUtil;
import org.openo.nfvo.vnfmadapter.common.VnfmUtil;
import org.openo.nfvo.vnfmadapter.service.adapter.impl.AdapterResourceManager;
import org.openo.nfvo.vnfmadapter.service.constant.Constant;
import org.openo.nfvo.vnfmadapter.service.csm.vnf.VnfMgrVnfm;
import org.openo.nfvo.vnfmadapter.service.dao.impl.VnfmDaoImpl;
import org.openo.nfvo.vnfmadapter.service.dao.inf.VnfmDao;
import org.openo.nfvo.vnfmadapter.service.entity.Vnfm;

import mockit.Mock;
import mockit.MockUp;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class VnfMgrTest {

    private VnfmDao vnfmDao;

    private VnfMgr vnfMgr;

    @Before
    public void setUp() {
        vnfMgr = new VnfMgr();
        vnfmDao = new VnfmDaoImpl();
        vnfMgr.setVnfmDao(vnfmDao);
    }

    @Test
    public void testAddVnfByInvalidateDataVnfInfoNull() {
        String data = "{}";
        JSONObject subJsonObject = JSONObject.fromObject(data);
        VnfMgr vnfMgr = new VnfMgr();
        JSONObject result = vnfMgr.addVnf(subJsonObject, "vnmfId");

        JSONObject restJson = new JSONObject();
        restJson.put("retCode", Constant.REST_FAIL);
        assertEquals(restJson, result);
    }

    @Test
    public void testAddVnfByInvalidateDataVnfInfoEmpty() {
        String data = "{}";
        JSONObject subJsonObject = JSONObject.fromObject(data);
        VnfMgr vnfMgr = new VnfMgr();
        JSONObject result = vnfMgr.addVnf(subJsonObject, "vnmfId");

        JSONObject restJson = new JSONObject();
        restJson.put("retCode", Constant.REST_FAIL);
        assertEquals(restJson, result);
    }

    @Test
    public void testAddVnfByVnfmObjcetIsNullObject() {
        new MockUp<VnfmUtil>() {

            @Mock
            public JSONObject getVnfmById(String vnfmId) {
                return new JSONObject(true);
            }
        };
        String data =
                "{\"soId\": \"soId\",\"vapp_info\":{\"vnfm_id\":\"vnfm_id\",\"soId\": \"soId\",\"do_id\": \"do_id\"}}";
        JSONObject subJsonObject = JSONObject.fromObject(data);
        VnfMgr vnfMgr = new VnfMgr();
        JSONObject result = vnfMgr.addVnf(subJsonObject, "vnmfId");

        JSONObject restJson = new JSONObject();
        restJson.put("retCode", Constant.REST_FAIL);
        assertEquals(restJson, result);
    }

    @Test
    public void testAddVnfByVnfmObjcetTypeEmpty() {
        new MockUp<VnfmUtil>() {

            @Mock
            public JSONObject getVnfmById(String vnfmId) {
                JSONObject obj = new JSONObject();
                obj.put("type", "");
                return obj;
            }
        };
        String data =
                "{\"soId\": \"soId\",\"vapp_info\":{\"vnfm_id\":\"vnfm_id\",\"soId\": \"soId\",\"do_id\": \"do_id\"}}";
        JSONObject subJsonObject = JSONObject.fromObject(data);
        VnfMgr vnfMgr = new VnfMgr();
        JSONObject result = vnfMgr.addVnf(subJsonObject, "vnmfId");

        JSONObject restJson = new JSONObject();
        restJson.put("retCode", Constant.REST_FAIL);
        assertEquals(restJson, result);
    }

    @Test
    public void testAddVnf() {
        new MockUp<VnfmUtil>() {

            @Mock
            public JSONObject getVnfmById(String vnfmId) {
                JSONObject obj = new JSONObject();
                obj.put("type", "hw");
                obj.put("vnfmId", "123");
                obj.put("userName", "admin");
                obj.put("password", "admin");
                obj.put("url", "https://10.2.31.2:30001");
                return obj;
            }
        };

        new MockUp<VnfMgrVnfm>() {

            @Mock
            public JSONObject createVnf(JSONObject subJsonObject, JSONObject vnfmObjcet) {
                JSONObject restJson = new JSONObject();
                restJson.put("retCode", Constant.REST_SUCCESS);
                return restJson;
            }
        };

        new MockUp<AdapterResourceManager>() {

            @Mock
            public JSONObject uploadVNFPackage(JSONObject subJsonObject, Map<String, String> conMap) {
                JSONObject restJson = new JSONObject();
                restJson.put("retCode", Constant.REST_SUCCESS);
                restJson.put("vnfdId", "123");
                return restJson;
            }
        };

        String data =
                "{\"vnfPackageId\": \"vnfPackageId\",\"vnfId\": \"vnfId\",\"additionalParam\":{\"parameters\":{\"input\":\"input\"}}}";
        JSONObject subJsonObject = JSONObject.fromObject(data);
        VnfMgr vnfMgr = new VnfMgr();
        JSONObject result = vnfMgr.addVnf(subJsonObject, "vnfmId");

        JSONObject restJson = new JSONObject();
        restJson.put("retCode", Constant.REST_SUCCESS);
        assertEquals(restJson, result);
    }

    @Test
    public void testDeleteVnf() {
        new MockUp<VnfmUtil>() {

            @Mock
            public JSONObject getVnfmById(String vnfmId) {
                JSONObject obj = new JSONObject();
                obj.put("type", "hw");
                obj.put("vnfmId", "123");
                obj.put("userName", "admin");
                obj.put("password", "admin");
                obj.put("url", "https://10.2.31.2:30001");
                return obj;
            }
        };
        new MockUp<VnfMgrVnfm>() {

            @Mock
            public JSONObject removeVnf(JSONObject vnfmObject, String vnfId, JSONObject vnfObject) {
                JSONObject obj = new JSONObject();
                obj.put("retCode", Constant.REST_SUCCESS);
                return obj;
            }
        };
        VnfMgr vnfMgr = new VnfMgr();
        JSONObject vnfObject = new JSONObject();
        JSONObject result = vnfMgr.deleteVnf("vnfId", "vnfmId", vnfObject);

        JSONObject restJson = new JSONObject();
        restJson.put("retCode", Constant.REST_SUCCESS);
        assertEquals(restJson, result);
    }

    @Test
    public void testDeleteVnfByVnfmObjcetIsNullObject() {
        new MockUp<VnfmUtil>() {

            @Mock
            public JSONObject getVnfmById(String vnfmId) {
                JSONObject obj = new JSONObject(true);
                return obj;
            }
        };
        VnfMgr vnfMgr = new VnfMgr();
        JSONObject vnfObject = new JSONObject();
        JSONObject result = vnfMgr.deleteVnf("vnfId", "vnfmId", vnfObject);

        JSONObject restJson = new JSONObject();
        restJson.put("retCode", Constant.REST_FAIL);
        assertEquals(restJson, result);

    }

    @Test
    public void testGetVnf() {
        new MockUp<VnfmUtil>() {

            @Mock
            public JSONObject getVnfmById(String vnfmId) {
                JSONObject obj = new JSONObject();
                obj.put("type", "hw");
                obj.put("vnfmId", "123");
                obj.put("userName", "admin");
                obj.put("password", "admin");
                obj.put("url", "https://10.2.31.2:30001");
                return obj;
            }
        };

        new MockUp<VnfmDaoImpl>() {

            @Mock
            public Vnfm getVnfmById(String vnfmId) {
                Vnfm obj = new Vnfm();
                obj.setId("123");
                obj.setVersion("v2.0");
                obj.setVnfdId("234");
                obj.setVnfPackageId("123");
                return obj;
            }
        };

        new MockUp<ResultRequestUtil>() {

            @Mock
            public JSONObject call(JSONObject vnfmObjcet, String path, String methodName, String paramsJson) {
                JSONObject resultJson = new JSONObject();
                resultJson.put("retCode", Constant.HTTP_OK);
                JSONObject data = new JSONObject();

                JSONArray result = new JSONArray();
                JSONObject basicInfo = new JSONObject();
                basicInfo.put("id", "NE=345");
                basicInfo.put("vapp_name", "sc");
                basicInfo.put("status", "active");
                result.add(basicInfo);
                data.put("basic", result);
                resultJson.put("data", data.toString());
                return resultJson;
            }
        };

        JSONObject result = vnfMgr.getVnf("vnfId", "vnfmId");

        JSONObject restJson = new JSONObject();
        restJson.put("retCode", Constant.REST_SUCCESS);
        result.remove("vnfInfo");
        assertEquals(restJson, result);
    }

}
