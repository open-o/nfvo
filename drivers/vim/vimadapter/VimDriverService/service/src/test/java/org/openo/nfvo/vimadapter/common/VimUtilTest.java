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
package org.openo.nfvo.vimadapter.common;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vimadapter.common.servicetoken.VimRestfulUtil;
import org.openo.nfvo.vimadapter.service.entity.Vim;
import org.openo.nfvo.vimadapter.testutils.JsonUtil;

import mockit.Mock;
import mockit.MockUp;

public class VimUtilTest {

    @Test
    public void getVimsTestNullResp() {
        List<Vim> vimList = VimUtil.getVims();
        assertNull(vimList);
    }

    @Test
    public void getVimsTestInvalidStatus() {
        new MockUp<VimRestfulUtil>() {
            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params) {
                RestfulResponse response = new RestfulResponse();
                response.setStatus(500);
                return response;
            }
        };
        List<Vim> vimList = VimUtil.getVims();
        assertNull(vimList);
    }

    @Test
    public void getVimsTest() {
        new MockUp<VimRestfulUtil>() {
            private String toJson(List list) {
                try {
                    return JsonUtil.marshal(list);
                } catch (IOException e) {
                    return "";
                }
            }
            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params) {
                RestfulResponse response = new RestfulResponse();
                response.setStatus(200);
                List<Map<String, String>> vims = new ArrayList<>();
                Map<String, String> vim = new HashMap<String, String>();
                vim.put("vimId", "123");
                vim.put("name", "123");
                vim.put("url", "123");
                vim.put("userName", "123");
                vim.put("password", "123");
                vim.put("type", "123");
                vim.put("version", "123");
                vims.add(vim);
                String vimStr = this.toJson(vims);
                response.setResponseJson(vimStr);
                return response;
            }
        };
        List<Vim> vimList = VimUtil.getVims();
        assertTrue(!vimList.isEmpty());
    }

}
