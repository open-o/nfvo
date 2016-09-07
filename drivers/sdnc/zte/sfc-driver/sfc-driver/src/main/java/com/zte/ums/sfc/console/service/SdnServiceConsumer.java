/**
 * Copyright 2016 [ZTE] and others.
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

package com.zte.ums.sfc.console.service;

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class SdnServiceConsumer {
    private static final Logger logger = LoggerFactory.getLogger(SdnServiceConsumer.class.getName());

    public static ISdnControllerService getSdnConProxy(String url) throws Exception {
        return ConsumerFactory.createConsumer(url, new ClientConfig().register(JacksonJsonProvider.class),
                ISdnControllerService.class);
    }
}
