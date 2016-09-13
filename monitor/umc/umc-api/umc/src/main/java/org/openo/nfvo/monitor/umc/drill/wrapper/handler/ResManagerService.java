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
package org.openo.nfvo.monitor.umc.drill.wrapper.handler;

import org.openo.nfvo.monitor.umc.drill.wrapper.resources.ResourceServicesStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *       Temporarily used to query the name of VNFM and VIM by id
 */
public class ResManagerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResManagerService.class);

    /**
     * query VNFM name by id.If error occurs,return the request id instead
     *
     * @param vnfmId
     * @return
     */
    public static String getVnfmName(String vnfmId) {
    	return ResourceServicesStub.queryVnfm(vnfmId);
    }

    /**
     * query VIM name by id.If error occurs,return the request id instead
     *
     * @param vimId
     * @return
     */
    public static String getVimName(String vimId) {
    	return ResourceServicesStub.queryVim(vimId);
    }
}
