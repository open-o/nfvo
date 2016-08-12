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

import org.glassfish.jersey.client.ClientConfig;

/**
 * @author 10188044
 * @date 2015-8-12
 *       <p/>
 *       the context that saves the ClientConfig instance and the serverURI
 */
public class ServiceContext {
    // the ClientConfig used by JerseyClient
    private ClientConfig clientconfig = null;
    // the URI of the resource service
    private String resourceServerURI = null;

    public ServiceContext() {
        // initialize the context of calling the resource services
        clientconfig = ClientConfigFactory.createClientConfig();
        resourceServerURI = ClientConfigFactory.buildResourceServerURI();
    }

    /**
     * return the clientconfig
     */
    public ClientConfig getClientConfig() {
        return clientconfig;
    }

    /**
     * return the full URI of resource service
     */
    public String getServerURI() {
        return resourceServerURI;
    }
}
