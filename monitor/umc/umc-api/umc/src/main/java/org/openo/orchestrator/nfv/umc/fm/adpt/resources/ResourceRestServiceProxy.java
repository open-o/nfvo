/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.orchestrator.nfv.umc.fm.adpt.resources;


import org.glassfish.jersey.client.ClientConfig;

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;

public class ResourceRestServiceProxy {

//    private static final String DB_REST_URL = "http://127.0.0.1:8204";
    private static IFmResourceRestService dbRestServiceproxy;

    static {
        ClientConfig config = new ClientConfig();
        dbRestServiceproxy =
                ConsumerFactory.createConsumer(RocResourceConfig.getRocResourceAddr(), config, IFmResourceRestService.class);
    }

    public static String getProbableCauseTree(String type) throws Exception {
        return dbRestServiceproxy.getProbableCauseTree(type);
    }

    public static String getProbableCause() throws Exception {
        return dbRestServiceproxy.getProbableCause();
    }

    public static String getAllResource() {
        return dbRestServiceproxy.getAllResource();
    }

    public static String getNeNode(String id) {
        return dbRestServiceproxy.getNeNode(id);
    }

}
