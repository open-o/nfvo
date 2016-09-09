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
package org.openo.nfvo.monitor.umc.pm.adpt.roc.service;

import org.openo.nfvo.monitor.umc.pm.adpt.roc.RocConfiguration;
import org.openo.nfvo.monitor.umc.pm.adpt.roc.entity.ResourceTypeResponse;
import org.openo.nfvo.monitor.umc.pm.bean.ResourceType;
import org.openo.nfvo.monitor.umc.pm.common.DebugPrn;
import org.openo.nfvo.monitor.umc.pm.common.RestRequestException;

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;


/**
 * 
 */
public class ModelServiceConsumer {
    private static final DebugPrn logger = new DebugPrn(ModelServiceConsumer.class.getName());

    private static IModelRestService rocModelService;

    private static IModelRestService getRocModelServiceProxy() {
        if (rocModelService == null) {
            rocModelService =
                    ConsumerFactory.createConsumer(RocConfiguration.getRocServerAddr(),
                            IModelRestService.class);
        }

        return rocModelService;
    }

    /**
     * @param id
     * @return
     * @throws RestRequestException
     */
    public static ResourceType queryResourceType(String id) throws RestRequestException {
        logger.info("queryResourceType. id = " + id);

        try {
            ResourceTypeResponse[] response = getRocModelServiceProxy().queryResourceType(id);
            logger.info("response : " + response[0]);
            return new ResourceType(response[0].getId(), response[0].getName());
        } catch (Exception e) {
            throw new RestRequestException("roc model rest request error.", e);
        }
    }

}
