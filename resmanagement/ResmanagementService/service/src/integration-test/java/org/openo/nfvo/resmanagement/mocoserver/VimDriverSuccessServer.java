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

package org.openo.nfvo.resmanagement.mocoserver;

import org.openo.sdno.testframework.moco.MocoHttpServer;

/**
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version NFVO 0.5 Sep 28, 2016
 */
public class VimDriverSuccessServer extends MocoHttpServer {

    private static final String GET_CPU_LIMITS = "src/integration-test/resources/mocoserver/getcpulimits.json";

    private static final String GET_DISK_LIMITS = "src/integration-test/resources/mocoserver/getdisklimits.json";

    public VimDriverSuccessServer() {
        super();
    }

    public VimDriverSuccessServer(int port) {
        super(port);
    }

    @Override
    public void addRequestResponsePairs() {
        this.addRequestResponsePair(GET_CPU_LIMITS);
        this.addRequestResponsePair(GET_DISK_LIMITS);
    }
}
