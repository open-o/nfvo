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

package org.openo.nfvo.vimadapter.test;

import java.io.File;

import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.nfvo.vimadapter.util.MyTestManager;

/**
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version NFVO 0.5 Oct 8, 2016
 */
public class ITNetworkRoaFail extends MyTestManager {

    private static final String ADD_NETWORK_PATH =
            "src/integration-test/resources/vimadapter/testcase/networkroa/addnetworkfail1.json";

    private static final String DELETE_NETWORK_PATH =
            "src/integration-test/resources/vimadapter/testcase/networkroa/deletenetworkfail1.json";

    @Test
    public void testOperateFail() throws ServiceException {
        execTestCase(new File(ADD_NETWORK_PATH));
        execTestCase(new File(DELETE_NETWORK_PATH));

    }
}
