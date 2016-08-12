/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.umc.drill.wrapper.resources.config;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.util.ServiceAccessUtil;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * @author 10188044
 * @date 2015-8-11
 *       <p/>
 *       The factory that produce the configuration info used when calling the roc service
 */
public class ClientConfigFactory {

    private static String rocServerAddr;

    /**
     * @param rocServerAddr the rocServerAddr to set
     */
    public static void setRocServerAddr(String rocServerAddr) {
        ClientConfigFactory.rocServerAddr = rocServerAddr;
    }

    /**
     * create the ClientConfig for JerseyClient
     *
     * @return
     */
    public static ClientConfig createClientConfig() {
        // create the apache PoolingHttpClient
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(10);

        ClientConfig clientConfig =
                new ClientConfig()
                        .connectorProvider(new ApacheConnectorProvider())
                        // specify the PoolingHttpClientConnectionManager manager
                        .property(ApacheClientProperties.CONNECTION_MANAGER, connectionManager)
                        .property(ClientProperties.CONNECT_TIMEOUT, "2000")
                        .property(ClientProperties.READ_TIMEOUT, "3000")
                        // config the serialization / deserialization provider for json
                        .register(JacksonJsonProvider.class);
        return clientConfig;
    }

    /**
     * build the full resource servie URI
     */
    public static String buildResourceServerURI() {
        return "http://" + rocServerAddr + ServiceAccessUtil.getRocApiRootDomain();
    }
}
