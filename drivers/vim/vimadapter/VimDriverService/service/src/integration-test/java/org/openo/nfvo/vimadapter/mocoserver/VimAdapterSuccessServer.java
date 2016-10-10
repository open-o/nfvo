/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.nfvo.vimadapter.mocoserver;

import org.openo.sdno.testframework.moco.MocoHttpServer;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version NFVO 0.5 Aug 2, 2016
 */
public class VimAdapterSuccessServer extends MocoHttpServer {

    private static final String QUERY_VIMINFO_FILE =
            "src/integration-test/resources/vimadapter/mocoserver/getviminfo.json";

    private static final String CONNETC_VIM_FILE =
            "src/integration-test/resources/vimadapter/mocoserver/connetvim.json";

    private static final String QUERY_PROJECTS_FILE =
            "src/integration-test/resources/vimadapter/mocoserver/getprojects.json";

    private static final String QUERY_PROJECT_FILE =
            "src/integration-test/resources/vimadapter/mocoserver/getproject.json";

    private static final String QUERY_PORTS_FILE = "src/integration-test/resources/vimadapter/mocoserver/getports.json";

    private static final String QUERY_PORT_FILE = "src/integration-test/resources/vimadapter/mocoserver/getport.json";

    private static final String QUERY_NETWORKS_FILE =
            "src/integration-test/resources/vimadapter/mocoserver/getnetworks.json";

    private static final String QUERY_NETWORK_FILE =
            "src/integration-test/resources/vimadapter/mocoserver/getnetwork.json";

    private static final String QUERY_HOSTS_FILE = "src/integration-test/resources/vimadapter/mocoserver/gethosts.json";

    private static final String QUERY_HOST_FILE = "src/integration-test/resources/vimadapter/mocoserver/gethost.json";

    private static final String QUERY_LIMITSCPU_FILE =
            "src/integration-test/resources/vimadapter/mocoserver/getlimitscpu.json";

    private static final String QUERY_LIMITSDISK_FILE =
            "src/integration-test/resources/vimadapter/mocoserver/getlimitsdisk.json";

    private static final String CREATE_NETWORK_FILE =
            "src/integration-test/resources/vimadapter/mocoserver/createnetwork.json";

    private static final String DELETE_NETWORK_FILE =
            "src/integration-test/resources/vimadapter/mocoserver/deletenetwork.json";

    public VimAdapterSuccessServer() {
        super();
    }

    public VimAdapterSuccessServer(int port) {
        super(port);
    }

    @Override
    public void addRequestResponsePairs() {
        this.addRequestResponsePair(QUERY_VIMINFO_FILE);
        this.addRequestResponsePair(CONNETC_VIM_FILE);
        this.addRequestResponsePair(QUERY_PROJECTS_FILE);
        this.addRequestResponsePair(QUERY_PROJECT_FILE);
        this.addRequestResponsePair(QUERY_PORTS_FILE);
        this.addRequestResponsePair(QUERY_PORT_FILE);
        this.addRequestResponsePair(QUERY_NETWORKS_FILE);
        this.addRequestResponsePair(QUERY_NETWORK_FILE);
        this.addRequestResponsePair(QUERY_HOSTS_FILE);
        this.addRequestResponsePair(QUERY_HOST_FILE);
        this.addRequestResponsePair(QUERY_LIMITSCPU_FILE);
        this.addRequestResponsePair(QUERY_LIMITSDISK_FILE);
        this.addRequestResponsePair(CREATE_NETWORK_FILE);
        this.addRequestResponsePair(DELETE_NETWORK_FILE);
    }

}
