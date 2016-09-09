/*
 * Copyright 2016, CMCC Technologies Co., Ltd.
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
package org.openo.nfvo.monitor.umc.drill.wrapper.resources;

import org.openo.nfvo.monitor.umc.drill.wrapper.resources.config.ServiceContext;

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;

/**
 *       the client stub of the resource service,holding the proxy instance used to send request
 */
public class ResourceServicesStub {

    // singleton instance
    private static ResourceServicesStub resourceServicesStub = new ResourceServicesStub();
    // the proxy instance
    private IResourceServices proxyInstance = null;

    private ResourceServicesStub() {
        // initialize the service context of resource service
        ServiceContext serviceContext = new ServiceContext();
        // create the proxy instance
        proxyInstance =
                ConsumerFactory.createConsumer(serviceContext.getServerURI(),
                        serviceContext.getClientConfig(), IResourceServices.class);
    }

    /**
     * get the proxy instance
     */
    public static IResourceServices getServiceProxy() {
        return resourceServicesStub.proxyInstance;
    }
}
