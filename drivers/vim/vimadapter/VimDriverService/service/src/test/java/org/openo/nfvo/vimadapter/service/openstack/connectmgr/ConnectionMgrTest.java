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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.openo.nfvo.vimadapter.common.FileUtil;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.entity.Vim;
import org.openo.nfvo.vimadapter.service.openstack.api.ConnectInfo;

import mockit.Mock;
import mockit.MockUp;
import net.sf.json.JSONObject;

public class ConnectionMgrTest {

    private ConnectionMgr connectionMgr;

    private String basePath;

    private JSONObject vimJsonObj;

    @Before
    public void setUp() {
        connectionMgr = ConnectionMgr.getConnectionMgr();
        basePath = System.getProperty("user.dir");
        vimJsonObj = JSONObject.fromObject(FileUtil.ReadFile(basePath + "/src/test/resources" + "/testjson/vim.json"));
    }

    @Test
    public void testAddConnection() throws Exception {
        JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim();
        new MockUp<OpenstackConnection>() {

            @Mock
            public int connect() {
                return Constant.HTTP_OK_STATUS_CODE;
            }
        };
        int result = connectionMgr.addConnection(vim);
        int expectedResult = Constant.HTTP_OK_STATUS_CODE;
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetConnection() throws Exception {
        JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim();
        ConnectInfo info = new ConnectInfo(vim);
        new MockUp<OpenstackConnection>() {

            @Mock
            public int connect() {
                return Constant.HTTP_OK_STATUS_CODE;
            }

            @Mock
            public int isConnected() {
                return Constant.HTTP_UNAUTHORIZED_STATUS_CODE;
            }
        };
        connectionMgr.addConnection(vim);
        OpenstackConnection result = connectionMgr.getConnection(info);
        OpenstackConnection expectedResult = new OpenstackConnection();
        assertEquals(expectedResult.getDomainTokens(), result.getDomainTokens());
    }

    @Test
    public void testGetConnection1() throws Exception {
        JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim();
        ConnectInfo info = new ConnectInfo(vim);
        new MockUp<OpenstackConnection>() {

            @Mock
            public int connect() {
                return Constant.HTTP_OK_STATUS_CODE;
            }
        };
        connectionMgr.disConnect(vim);
        OpenstackConnection result = connectionMgr.getConnection(info);
        OpenstackConnection expectedResult = new OpenstackConnection();
        assertEquals(expectedResult.getDomainTokens(), result.getDomainTokens());
    }

    @Test
    public void testDisConnect() throws Exception {
        JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim();
        new MockUp<OpenstackConnection>() {

            @Mock
            public int connect() {
                return Constant.HTTP_OK_STATUS_CODE;
            }
        };
        connectionMgr.addConnection(vim);
        connectionMgr.disConnect(vim);
    }

    @Test
    public void testIsConnected() throws Exception {
        JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim();
        new MockUp<OpenstackConnection>() {

            @Mock
            public int connect() {
                return Constant.HTTP_OK_STATUS_CODE;
            }

            @Mock
            public int isConnected() {
                return Constant.HTTP_OK_STATUS_CODE;
            }
        };
        connectionMgr.addConnection(vim);
        int result = connectionMgr.isConnected(vim);
        int expectedResult = Constant.HTTP_OK_STATUS_CODE;
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsConnected1() throws Exception {
        JSONObject vimJson = vimJsonObj.getJSONObject("VIM");
        Vim vim = new Vim();
        int result = connectionMgr.isConnected(vim);
        int expectedResult = Constant.CONNECT_FAIL_STATUS_CODE;
        assertEquals(expectedResult, result);
    }

}
