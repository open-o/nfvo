/*
 * Copyright (c) 2017 Ericsson (China) Communication Co. Ltd.
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
package org.openo.nfvo.vnfmdriver.common;

import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vnfmdriver.common.constant.Constant;
import org.openo.nfvo.vnfmdriver.common.restfulutil.HttpRestfulAPIUtil;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import net.sf.json.JSONObject;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version     NFVO 0.5  Feb 15, 2017
 */
public final class VnfmUtil {

    private static final Logger LOG = LogManager.getLogger(VnfmUtil.class);

    private VnfmUtil() {
    }

    /**
     *  get vnfm by id
     *
     * @param vnfmId
     * @return
     * @since NFVO 0.5
     */
    public static JSONObject getVnfmById(String vnfmId) {
        RestfulResponse rsp = HttpRestfulAPIUtil.getRemoteResponse(String.format(Constant.GET_VNFM_ID_URL, vnfmId),
                Constant.GET, null);

        if(rsp == null || rsp.getStatus() != Constant.HTTP_OK) {
            LOG.error("getVnfm fail!");

            return null;
        }

        LOG.info("func=getVnfmById, status={}", rsp.getStatus());
        return JSONObject.fromObject(rsp.getResponseContent());
    }
}

