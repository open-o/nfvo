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
package org.openo.nfvo.vimadapter.service.openstack.entry;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openo.nfvo.vimadapter.service.openstack.resourcemgr.NetworkQuery;

import mockit.Mock;
import mockit.MockUp;
import net.sf.json.JSONObject;

public class ResourceMgrOpenstackTest {

    @Test
    public void testGetNetworks() throws Exception {
        ResourceMgrOpenstack resourceMgrOpenstack = new ResourceMgrOpenstack();
        Map<String, String> conMap = new HashMap<String, String>();
        conMap.put("extraInfo", "{}");
        conMap.put("domainName", "domainName");
        conMap.put("authenticMode", "authenticMode");
        conMap.put("url", "url");
        conMap.put("userName", "userName");
        conMap.put("userPwd", "userPwd");
        conMap.put("vimId", "vimId");
        conMap.put("authenticateMode", "authenticateMode");
        conMap.put("vimName", "vimName");
        new MockUp<NetworkQuery>() {

            @Mock
            public JSONObject getNetworks() {
                return null;
            }
        };
        JSONObject result = resourceMgrOpenstack.getNetworks(conMap);
        JSONObject expectedResult = null;
        assertEquals(expectedResult, result);
    }

}
