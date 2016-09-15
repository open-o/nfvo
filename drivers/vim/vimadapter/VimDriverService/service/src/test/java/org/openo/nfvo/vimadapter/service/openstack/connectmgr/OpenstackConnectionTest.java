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
package org.openo.nfvo.vimadapter.service.openstack.connectmgr;

import java.util.Map;

import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vimadapter.common.servicetoken.VimRestfulUtil;

import mockit.Mock;
import mockit.MockUp;

public class OpenstackConnectionTest {

    OpenstackConnection connection = new OpenstackConnection();
    @Test
    public void connectTest(){
        /*new MockUp<VimRestfulUtil>(){
            @Mock
            public Map<String, String> generateParametesMap(String url, String methodType, String path,
                    String authMode) {
                Map<String, String> retMap = new HashMap<>();
                retMap.put("url", "http://localhost:8080");
                retMap.put("methodType", "get");
                retMap.put("path", "/test/abc");
                retMap.put("authMode", "test");
                return retMap;
            }

        };*/
        new MockUp<VimRestfulUtil>(){
            @Mock
            public RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params, String domainTokens) {
                RestfulResponse resp = new RestfulResponse();
                return resp;
            }

        };
        //int res = connection.connect();
        //assertTrue(res > 0);
    }

}
