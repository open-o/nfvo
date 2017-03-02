/*
 * Copyright Ericsson AB. 2017
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

package org.openo.nfvo.vnfmdriver.activator;

import org.openo.nfvo.vnfmdriver.activator.inf.InfServiceController;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Feb 28, 2017
 */
@Component
public class ServiceRunningEntry implements DestructionAwareBeanPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceRunningEntry.class);

    /**
     * <br>
     *
     * @param bean
     * @param name
     * @return
     * @throws BeansException
     * @since NFVO 0.5
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
        LOG.info(String.format("bean:[%s] Initialization finished", name));
        if(bean instanceof InfServiceController) {
            LOG.info("Ericsson VNFMDriver module regist start!");
            InfServiceController serviceCtrl = (InfServiceController)bean;
            serviceCtrl.start();
            LOG.info("Ericsson VNFMDriver module regist end!");
        }

        return bean;
    }

    /**
     * <br>
     *
     * @param bean
     * @param name
     * @return
     * @throws BeansException
     * @since NFVO 0.5
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
        return bean;
    }

    /**
     * <br>
     *
     * @param bean
     * @param name
     * @throws BeansException
     * @since NFVO 0.5
     */
    @Override
    public void postProcessBeforeDestruction(Object bean, String name) throws BeansException {
        if(bean instanceof InfServiceController) {
            LOG.info("Ericsson VNFM Driver Module Stop!");
            InfServiceController serviceCtrl = (InfServiceController)bean;
            serviceCtrl.stop();
        }

        return;
    }

}
