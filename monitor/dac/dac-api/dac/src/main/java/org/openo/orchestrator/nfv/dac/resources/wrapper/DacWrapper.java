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
package org.openo.orchestrator.nfv.dac.resources.wrapper;

import org.openo.orchestrator.nfv.dac.resources.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

/**
 * DAC rest interface processing class
 */
public class DacWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DacWrapper.class);

    /**
     * Register DAC
     * @return status code 201
     */
    public static Response registerDAC() {
        LOGGER.info("register DAC success.");
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * unregister DAC
     * @return status code 204
     */
    public static Response unregisterDAC() {
        TaskService.deleteAllMonitorTask();
        LOGGER.info("unregister DAC success.");
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
