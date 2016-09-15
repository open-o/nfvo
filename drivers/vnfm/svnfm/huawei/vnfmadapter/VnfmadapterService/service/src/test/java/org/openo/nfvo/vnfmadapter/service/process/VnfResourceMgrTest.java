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

import org.junit.Test;
import org.openo.nfvo.vnfmadapter.common.servicetoken.VnfmRestfulUtil;
import org.openo.nfvo.vnfmadapter.service.constant.Constant;

import mockit.Mock;
import mockit.MockUp;
import net.sf.json.JSONObject;

/**
 * <br/>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Aug 9, 2016
 */
public class VnfResourceMgrTest {

    @Test
    public void testgrantVnfResource() {
        new MockUp<VnfmRestfulUtil>() {

            @Mock
            public JSONObject sendReqToApp(String path, String methodName, JSONObject paraJson) {
                JSONObject retJson = new JSONObject();
                retJson.put("retCode", Constant.REST_SUCCESS);
                return retJson;
            }
        };
        String data =
                "{\"type\": \"instantiation\",\"operation_right\": \"increase\",\"vnf_name\": \"vnf_name\",\"vm_list\": [{\"vm_flavor\": {\"storage\": [{\"vol_type\": \"local_volume\",\"vol_size\": \"2\",\"storage_type\": \"local_image\",\"disk_size\": \"100\"}],\"num_cpus\": \"6\",\"mem_size\": \"8\"},\"init_number\": \"1\"}],\"version\": \"version\",\"template_id\": \"template_id\",\"template_name\": \"template_name\",\"plan_id\": \"plan_id\",\"plan_name\": \"plan_name\",\"project_id\": \"project_id\",\"project_name\": \"project_name\",\"creator\": \"creator\",\"status\": \"status\",\"tenant\": \"tenant\",\"parent_tenant\": \"parent_tenant\",\"vnfd_id\": \"vnfd_id\",\"location\": \"location\",\"dr_location\": \"dr_location\",\"nfvo_id\": \"nfvo_id\"}";
        JSONObject vnfObj = JSONObject.fromObject(data);
        VnfResourceMgr vnfResourceMgr = new VnfResourceMgr();
        JSONObject result = vnfResourceMgr.grantVnfResource(vnfObj, "vnfId", "vnfmId");

        JSONObject retJson = new JSONObject();
        retJson.put("retCode", Constant.REST_SUCCESS);
        assertEquals(retJson, result);
    }

    @Test
    public void testgrantVnfResourceByResMapNull() {
        new MockUp<VnfmRestfulUtil>() {

            @Mock
            public JSONObject sendReqToApp(String path, String methodName, JSONObject paraJson) {
                JSONObject retJson = new JSONObject();
                retJson.put("retCode", Constant.REST_SUCCESS);
                return retJson;
            }
        };
        String data =
                "{\"type\": \"instantiation\",\"operation_right\": \"increase\",\"vnf_name\": \"vnf_name\",\"vm_list\": [{\"vm_flavors\": {\"storage\": [{\"vol_type\": \"local_volume\",\"vol_size\": \"2\",\"storage_type\": \"local_image\",\"disk_size\": \"100\"}],\"num_cpus\": \"6\",\"mem_size\": \"8\"},\"init_number\": \"1\"}],\"version\": \"version\",\"template_id\": \"template_id\",\"template_name\": \"template_name\",\"plan_id\": \"plan_id\",\"plan_name\": \"plan_name\",\"project_id\": \"project_id\",\"project_name\": \"project_name\",\"creator\": \"creator\",\"status\": \"status\",\"tenant\": \"tenant\",\"parent_tenant\": \"parent_tenant\",\"vnfd_id\": \"vnfd_id\",\"location\": \"location\",\"dr_location\": \"dr_location\",\"nfvo_id\": \"nfvo_id\"}";
        JSONObject vnfObj = JSONObject.fromObject(data);
        VnfResourceMgr vnfResourceMgr = new VnfResourceMgr();
        JSONObject result = vnfResourceMgr.grantVnfResource(vnfObj, "vnfId", "vnfmId");

        JSONObject retJson = new JSONObject();
        retJson.put("retCode", Constant.REST_FAIL);
        retJson.put("errorMsg", "resource params error");
        assertEquals(retJson, result);
    }

    @Test
    public void testgrantVnfResourceByTypeIsEmpty() {
        String data =
                "{\"type\": \"\",\"operation_right\": \"increase\",\"vnf_name\": \"vnf_name\",\"vm_list\": [{\"vm_flavor\": {\"storage\": [{\"vol_type\": \"local_volume\",\"vol_size\": \"2\",\"storage_type\": \"local_image\",\"disk_size\": \"100\"}],\"num_cpus\": \"6\",\"mem_size\": \"8\"},\"init_number\": \"1\"}],\"version\": \"version\",\"template_id\": \"template_id\",\"template_name\": \"template_name\",\"plan_id\": \"plan_id\",\"plan_name\": \"plan_name\",\"project_id\": \"project_id\",\"project_name\": \"project_name\",\"creator\": \"creator\",\"status\": \"status\",\"tenant\": \"tenant\",\"parent_tenant\": \"parent_tenant\",\"vnfd_id\": \"vnfd_id\",\"location\": \"location\",\"dr_location\": \"dr_location\",\"nfvo_id\": \"nfvo_id\"}";
        JSONObject vnfObj = JSONObject.fromObject(data);
        VnfResourceMgr vnfResourceMgr = new VnfResourceMgr();
        JSONObject result = vnfResourceMgr.grantVnfResource(vnfObj, "vnfId", "vnfmId");

        JSONObject retJson = new JSONObject();
        retJson.put("retCode", Constant.REST_FAIL);
        retJson.put("errorMsg", "basic params error");
        assertEquals(retJson, result);
    }

    @Test
    public void testgrantVnfResourceByVnfNameIsEmpty() {
        String data =
                "{\"type\": \"instantiation\",\"operation_right\": \"increase\",\"vnf_name\": \"\",\"vm_list\": [{\"vm_flavor\": {\"storage\": [{\"vol_type\": \"local_volume\",\"vol_size\": \"2\",\"storage_type\": \"local_image\",\"disk_size\": \"100\"}],\"num_cpus\": \"6\",\"mem_size\": \"8\"},\"init_number\": \"1\"}],\"version\": \"version\",\"template_id\": \"template_id\",\"template_name\": \"template_name\",\"plan_id\": \"plan_id\",\"plan_name\": \"plan_name\",\"project_id\": \"project_id\",\"project_name\": \"project_name\",\"creator\": \"creator\",\"status\": \"status\",\"tenant\": \"tenant\",\"parent_tenant\": \"parent_tenant\",\"vnfd_id\": \"vnfd_id\",\"location\": \"location\",\"dr_location\": \"dr_location\",\"nfvo_id\": \"nfvo_id\"}";
        JSONObject vnfObj = JSONObject.fromObject(data);
        VnfResourceMgr vnfResourceMgr = new VnfResourceMgr();
        JSONObject result = vnfResourceMgr.grantVnfResource(vnfObj, "vnfId", "vnfmId");

        JSONObject retJson = new JSONObject();
        retJson.put("retCode", Constant.REST_FAIL);
        retJson.put("errorMsg", "basic params error");
        assertEquals(retJson, result);
    }

    @Test
    public void testgrantVnfResourceByVnfIdIsEmpty() {
        String data =
                "{\"type\": \"instantiation\",\"operation_right\": \"increase\",\"vnf_name\": \"vnf_name\",\"vm_list\": [{\"vm_flavor\": {\"storage\": [{\"vol_type\": \"local_volume\",\"vol_size\": \"2\",\"storage_type\": \"local_image\",\"disk_size\": \"100\"}],\"num_cpus\": \"6\",\"mem_size\": \"8\"},\"init_number\": \"1\"}],\"version\": \"version\",\"template_id\": \"template_id\",\"template_name\": \"template_name\",\"plan_id\": \"plan_id\",\"plan_name\": \"plan_name\",\"project_id\": \"project_id\",\"project_name\": \"project_name\",\"creator\": \"creator\",\"status\": \"status\",\"tenant\": \"tenant\",\"parent_tenant\": \"parent_tenant\",\"vnfd_id\": \"vnfd_id\",\"location\": \"location\",\"dr_location\": \"dr_location\",\"nfvo_id\": \"nfvo_id\"}";
        JSONObject vnfObj = JSONObject.fromObject(data);
        VnfResourceMgr vnfResourceMgr = new VnfResourceMgr();
        JSONObject result = vnfResourceMgr.grantVnfResource(vnfObj, "", "vnfmId");

        JSONObject retJson = new JSONObject();
        retJson.put("retCode", Constant.REST_FAIL);
        retJson.put("errorMsg", "basic params error");
        assertEquals(retJson, result);
    }

    @Test
    public void testgrantVnfResourceByRequestTypeIsEmpty() {
        String data =
                "{\"type\": \"instantiation\",\"operation_right\": \"\",\"vnf_name\": \"vnf_name\",\"vm_list\": [{\"vm_flavor\": {\"storage\": [{\"vol_type\": \"local_volume\",\"vol_size\": \"2\",\"storage_type\": \"local_image\",\"disk_size\": \"100\"}],\"num_cpus\": \"6\",\"mem_size\": \"8\"},\"init_number\": \"1\"}],\"version\": \"version\",\"template_id\": \"template_id\",\"template_name\": \"template_name\",\"plan_id\": \"plan_id\",\"plan_name\": \"plan_name\",\"project_id\": \"project_id\",\"project_name\": \"project_name\",\"creator\": \"creator\",\"status\": \"status\",\"tenant\": \"tenant\",\"parent_tenant\": \"parent_tenant\",\"vnfd_id\": \"vnfd_id\",\"location\": \"location\",\"dr_location\": \"dr_location\",\"nfvo_id\": \"nfvo_id\"}";
        JSONObject vnfObj = JSONObject.fromObject(data);
        VnfResourceMgr vnfResourceMgr = new VnfResourceMgr();
        JSONObject result = vnfResourceMgr.grantVnfResource(vnfObj, "vnfId", "vnfmId");

        JSONObject retJson = new JSONObject();
        retJson.put("retCode", Constant.REST_FAIL);
        retJson.put("errorMsg", "basic params error");
        assertEquals(retJson, result);
    }

    @Test
    public void testgrantVnfResourceByJSONException() {
        String data =
                "{\"operation_right\": \"increase\",\"vnf_name\": \"vnf_name\",\"vm_list\": [{\"vm_flavor\": {\"storage\": [{\"vol_type\": \"local_volume\",\"vol_size\": \"2\",\"storage_type\": \"local_image\",\"disk_size\": \"100\"}],\"num_cpus\": \"6\",\"mem_size\": \"8\"},\"init_number\": \"1\"}],\"version\": \"version\",\"template_id\": \"template_id\",\"template_name\": \"template_name\",\"plan_id\": \"plan_id\",\"plan_name\": \"plan_name\",\"project_id\": \"project_id\",\"project_name\": \"project_name\",\"creator\": \"creator\",\"status\": \"status\",\"tenant\": \"tenant\",\"parent_tenant\": \"parent_tenant\",\"vnfd_id\": \"vnfd_id\",\"location\": \"location\",\"dr_location\": \"dr_location\",\"nfvo_id\": \"nfvo_id\"}";
        JSONObject vnfObj = JSONObject.fromObject(data);
        VnfResourceMgr vnfResourceMgr = new VnfResourceMgr();
        JSONObject result = vnfResourceMgr.grantVnfResource(vnfObj, "vnfId", "vnfmId");

        JSONObject retJson = new JSONObject();
        retJson.put("retCode", Constant.REST_FAIL);
        retJson.put("errorMsg", "params parse exception");
        assertEquals(retJson, result);
    }
}
