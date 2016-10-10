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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.nfvo.vimadapter.mocoserver.VimAdapterSuccessServer;
import org.openo.nfvo.vimadapter.util.MyTestManager;

/**
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version NFVO 0.5 Oct 8, 2016
 */
public class ITResourceRoaSuccess extends MyTestManager {

    private VimAdapterSuccessServer vimAdapterServer = new VimAdapterSuccessServer();

    private static final String GET_PROJECTS_PATH =
            "src/integration-test/resources/vimadapter/testcase/resourceroa/getprojectssuccess1.json";

    private static final String GET_PROJECT_PATH =
            "src/integration-test/resources/vimadapter/testcase/resourceroa/getprojectsuccess1.json";

    private static final String GET_PORTS_PATH =
            "src/integration-test/resources/vimadapter/testcase/resourceroa/getportssuccess1.json";

    private static final String GET_PORT_PATH =
            "src/integration-test/resources/vimadapter/testcase/resourceroa/getportsuccess1.json";

    private static final String GET_NETWORKS_PATH =
            "src/integration-test/resources/vimadapter/testcase/resourceroa/getnetworkssuccess1.json";

    private static final String GET_NETWORK_PATH =
            "src/integration-test/resources/vimadapter/testcase/resourceroa/getnetworksuccess1.json";

    private static final String GET_HOSTS_PATH =
            "src/integration-test/resources/vimadapter/testcase/resourceroa/gethostssuccess1.json";

    private static final String GET_HOST_PATH =
            "src/integration-test/resources/vimadapter/testcase/resourceroa/gethostsuccess1.json";

    private static final String GET_LIMITSCPU_PATH =
            "src/integration-test/resources/vimadapter/testcase/resourceroa/getlimitscpusuccess1.json";

    private static final String GET_LIMITSDISK_PATH =
            "src/integration-test/resources/vimadapter/testcase/resourceroa/getlimitsdisksuccess1.json";

    @Before
    public void setup() throws ServiceException, InterruptedException {
        vimAdapterServer.start();
        // Thread.sleep(60 * 1000);
    }

    @After
    public void tearDown() throws ServiceException {
        vimAdapterServer.stop();
    }

    @Test
    public void testOperateSuccess() throws ServiceException {
        execTestCase(new File(GET_PROJECTS_PATH));
        execTestCase(new File(GET_PROJECT_PATH));
        execTestCase(new File(GET_PORTS_PATH));
        execTestCase(new File(GET_PORT_PATH));
        execTestCase(new File(GET_NETWORKS_PATH));
        execTestCase(new File(GET_NETWORK_PATH));
        execTestCase(new File(GET_HOSTS_PATH));
        execTestCase(new File(GET_HOST_PATH));
        execTestCase(new File(GET_LIMITSCPU_PATH));
        execTestCase(new File(GET_LIMITSDISK_PATH));

    }
}
