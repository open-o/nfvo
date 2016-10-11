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

package org.openo.nfvo.vimadapter.service.activator;

import org.openo.nfvo.vimadapter.service.api.internalsvc.inf.IVimAdapterMgrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

/**
 * <br/>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Aug 31, 2016
 */
public class RoaVimServicePostProcessor implements DestructionAwareBeanPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(RoaVimServicePostProcessor.class);

    @Override
    public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
        if(bean instanceof IVimAdapterMgrService) {
            LOG.warn("Register to Microservice BUS!");
            IVimAdapterMgrService vimAdapterSvc = (IVimAdapterMgrService)bean;
            vimAdapterSvc.register();
        }

        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
        return bean;
    }

    @Override
    public void postProcessBeforeDestruction(Object bean, String name) throws BeansException {
        //post processing
    }

}
