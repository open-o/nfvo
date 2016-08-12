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
package org.openo.orchestrator.nfv.umc.pm.adpt.dac.service.resources;

import lombok.Data;

import org.openo.orchestrator.nfv.umc.pm.adpt.dac.DacConfiguration;
import org.openo.orchestrator.nfv.umc.pm.adpt.dac.bean.TaskCreateAndModifyInfo;
import org.openo.orchestrator.nfv.umc.pm.common.DebugPrn;
import org.openo.orchestrator.nfv.umc.pm.common.RestRequestException;

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;

/**
 * @author 10090474
 */
@Data
public class ProxyServiceConsumer {
    private static final DebugPrn logger = new DebugPrn(ProxyServiceConsumer.class.getName());
    private IProxyRestService proxyService;
    private String proxyIp;

    public ProxyServiceConsumer(String proxyIp) {
        this.proxyIp = proxyIp;
        this.proxyService = ConsumerFactory.createConsumer(
                "http://" + this.proxyIp + ":" + DacConfiguration.getInstance().getDacServerPort(),
                IProxyRestService.class);
    }

    /**
     * @param proxyTaskInfo
     * @throws RestRequestException
     */
    public void addProxyTask(TaskCreateAndModifyInfo proxyTaskInfo) throws RestRequestException {
        logger.info("addProxyTask.");

        try {
            this.proxyService.addProxyTask(proxyTaskInfo);
            logger.info("addProxyTask end.");
            return;
        } catch (Exception e) {
            throw new RestRequestException("proxy rest request error.", e);
        }
    }

    /**
     * @param proxyTaskInfo
     * @throws RestRequestException
     */
    public void modifyProxyTask(TaskCreateAndModifyInfo proxyTaskInfo) throws RestRequestException {
        logger.info("modifyProxyTask.");

        try {
            this.proxyService.modifyProxyTask(proxyTaskInfo.getTaskId() + "", proxyTaskInfo);
            logger.info("modifyProxyTask end.");
            return;
        } catch (Exception e) {
            throw new RestRequestException("proxy rest request error.", e);
        }
    }

    /**
     * @param taskId
     * @throws RestRequestException
     */
    public void deleteProxyTask(String taskId) throws RestRequestException {
        logger.info("deleteProxyTask.");

        try {
            this.proxyService.deleteProxyTask(taskId);
            logger.info("deleteProxyTask end.");
            return;
        } catch (Exception e) {
            throw new RestRequestException("proxy rest request error.", e);
        }
    }


}
