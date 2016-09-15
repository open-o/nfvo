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

import org.junit.Test;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.entity.Vim;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.ConnectionMgr;

import mockit.Mock;
import mockit.MockUp;

public class ConnectMgrOpenstackTest {

    @Test
    public void testConnect1() throws Exception {
        ConnectMgrOpenstack connectMgrOpenstack = new ConnectMgrOpenstack();
        new MockUp<ConnectionMgr>() {

            @Mock
            public synchronized int addConnection(Vim vim) {
                return Constant.HTTP_OK_STATUS_CODE;
            }
        };
        int result = connectMgrOpenstack.connect(new Vim(), "POST");
        int expectedResult = Constant.HTTP_OK_STATUS_CODE;
        assertEquals(expectedResult, result);
    }

    @Test
    public void testConnect3() throws Exception {
        ConnectMgrOpenstack connectMgrOpenstack = new ConnectMgrOpenstack();
        new MockUp<ConnectionMgr>() {

            @Mock
            public synchronized void disConnect(Vim vim) {
            }
        };
        int result = connectMgrOpenstack.connect(new Vim(), "DEL");
        int expectedResult = Constant.HTTP_OK_STATUS_CODE;
        assertEquals(expectedResult, result);
    }

    @Test
    public void testConnect6() throws Exception {
        ConnectMgrOpenstack connectMgrOpenstack = new ConnectMgrOpenstack();
        int result = connectMgrOpenstack.connect(new Vim(), "");
        int expectedResult = Constant.TYPE_PARA_ERROR_STATUS_CODE;
        assertEquals(expectedResult, result);
    }

}
