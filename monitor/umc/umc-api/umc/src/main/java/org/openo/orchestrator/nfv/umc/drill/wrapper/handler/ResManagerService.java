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
package org.openo.orchestrator.nfv.umc.drill.wrapper.handler;

import org.openo.orchestrator.nfv.umc.drill.wrapper.common.TopologyConsts;
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.util.ArrayUtils;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.ResourceServicesStub;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VimData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VnfmData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.response.RestQueryListReturnMsg;
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
        String vnfmName = vnfmId;
        RestQueryListReturnMsg<VnfmData> restResult = null;
        try {
            restResult = ResourceServicesStub.getServiceProxy().queryVnfm(vnfmId);
        } catch (Exception e) {
            LOGGER.error("query vnfm nameError!throw exception!", e);
            return vnfmName;
        }
        if (restResult == null
                || restResult.getOperationResult().equals(TopologyConsts.OPERATIONS_RESULT_FAIL)) {
            return vnfmName;
        }
        VnfmData[] vnfms = restResult.getData();
        if (ArrayUtils.isArrayNotEmpty(vnfms)) {
            vnfmName = vnfms[0].getName();
        }
        return vnfmName;
    }

    /**
     * query VIM name by id.If error occurs,return the request id instead
     *
     * @param vimId
     * @return
     */
    public static String getVimName(String vimId) {
        String vimName = vimId;
        RestQueryListReturnMsg<VimData> restResult = null;
        try {
            restResult = ResourceServicesStub.getServiceProxy().queryVim(vimId);
        } catch (Exception e) {
            LOGGER.error("query vnfm nameError!throw exception!", e);
            return vimName;
        }
        if (restResult == null
                || restResult.getOperationResult().equals(TopologyConsts.OPERATIONS_RESULT_FAIL)) {
            return vimName;
        }
        VimData[] vims = restResult.getData();
        if (ArrayUtils.isArrayNotEmpty(vims)) {
            vimName = vims[0].getName();
        }
        return vimName;
    }
}
