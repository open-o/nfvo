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
 
package org.openo.nfvo.vimadapter.service.openstack.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openo.nfvo.vimadapter.common.FileUtil;
import org.openo.nfvo.vimadapter.service.entity.Vim;
import org.powermock.api.mockito.PowerMockito;

import net.sf.json.JSONObject;

public class ConnectInfoTest {

    private String basePath;

    private JSONObject vimJsonObj;

    @Before
    public void setUp() {
        basePath = System.getProperty("user.dir");
        vimJsonObj = JSONObject.fromObject(FileUtil.ReadFile(basePath + "/src/test/resources" + "/testjson/vim.json"));
    }

    @Test
    public void testGetUrl() {
        ConnectInfo connectInfo = new ConnectInfo();
        connectInfo.setUrl("/url");
        assertEquals("/url", connectInfo.getUrl());
    }

    @Test
    public void testGetDomainName() {
        ConnectInfo connectInfo = new ConnectInfo();
        connectInfo.setDomainName("domainName");
        assertEquals("domainName", connectInfo.getDomainName());
    }

    @Test
    public void testGetUserName() {
        ConnectInfo connectInfo = new ConnectInfo();
        connectInfo.setUserName("userName");
        assertEquals("userName", connectInfo.getUserName());
    }

    @Test
    public void testGetUserPwd() {
        ConnectInfo connectInfo = new ConnectInfo();
        connectInfo.setUserPwd("userPwd");
        assertEquals("userPwd", connectInfo.getUserPwd());
    }

    @Test
    public void testAuthenticateMode() {
        ConnectInfo connectInfo = new ConnectInfo();
        connectInfo.setAuthenticateMode("authenticateMode");
        assertEquals("authenticateMode", connectInfo.getAuthenticateMode());
    }

    @Test
    public void testGenerateConByMap() {
        JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim();
        String data = "{\"vim\":\"111\"}";
        JSONObject paramJson = JSONObject.fromObject(data);
        Map<String, String> conMap = vim.generateConMap(paramJson);

        ConnectInfo connectInfo = new ConnectInfo(conMap);
        assertEquals(new ConnectInfo(conMap), connectInfo.generateConByMap(conMap));
    }

    @Test
    public void testHashCode() {
        ConnectInfo connectInfo = new ConnectInfo();
        assertEquals(31, connectInfo.hashCode());
    }

    @Test
    public void testHashCode1() {
        ConnectInfo connectInfo = new ConnectInfo();
        connectInfo.setUrl("url");
        assertEquals(116110, connectInfo.hashCode());
    }

    @Test
    public void testToString() {
        ConnectInfo connectInfo = new ConnectInfo();
        assertEquals("ConnectInfo [AuthenticateMode: null,url=null, domainName=null, userName=null]",
                connectInfo.toString());
    }

    @Test
    public void testEquals() {
        ConnectInfo connectInfo = new ConnectInfo();
        boolean result = connectInfo.equals(connectInfo);
        assertTrue(result);
    }

    @Test
    public void testEquals1() {
        ConnectInfo connectInfo = new ConnectInfo();
        boolean result = connectInfo.equals(null);
        assertFalse(result);
    }

    @Test
    public void testEquals2() {
        ConnectInfo connectInfo = new ConnectInfo();
        boolean result = connectInfo.equals("");
        assertFalse(result);
    }

    @Test
    public void testEquals3() {
        ConnectInfo connectInfo = new ConnectInfo();
        ConnectInfo obj = PowerMockito.mock(ConnectInfo.class);
        boolean result = connectInfo.equals(obj);
        assertFalse(result);
    }

    @Test
    public void testEquals4() {
        ConnectInfo connectInfo = new ConnectInfo();
        ConnectInfo obj = new ConnectInfo();
        obj.setUrl("url");
        boolean result = connectInfo.equals(obj);
        assertFalse(result);
    }

    @Test
    public void testEquals5() {
        ConnectInfo connectInfo = new ConnectInfo();
        ConnectInfo obj = new ConnectInfo();
        boolean result = connectInfo.equals(obj);
        assertTrue(result);
    }

    @Test
    public void testEquals6() {
        ConnectInfo connectInfo = new ConnectInfo();
        connectInfo.setUrl("url1");
        ConnectInfo obj = new ConnectInfo();
        obj.setUrl("url2");
        boolean result = connectInfo.equals(obj);
        assertFalse(result);
    }

    @Test
    public void testIsNeedRenewInfo() {
        JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim();

        ConnectInfo connectInfo = new ConnectInfo(vim);
        ConnectInfo connectInfo1 = new ConnectInfo(vim);
        assertFalse(connectInfo.isNeedRenewInfo(connectInfo1));
    }

}
