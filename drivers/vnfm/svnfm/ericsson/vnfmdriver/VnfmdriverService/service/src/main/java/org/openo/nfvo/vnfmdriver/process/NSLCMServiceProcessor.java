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

package org.openo.nfvo.vnfmdriver.process;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vnfmdriver.common.constant.Constant;
import org.openo.nfvo.vnfmdriver.common.restfulutil.HttpRestfulAPIUtil;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.stereotype.Service;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Feb 28, 2017
 */
@Service("nslcmServiceProcessor")
public class NSLCMServiceProcessor {

    private static final Logger LOG = LogManager.getLogger(NSLCMServiceProcessor.class);

    /**
     * <br>
     *
     * @param jsonInstantiateOfReq
     * @return
     * @since NFVO 0.5
     */
    public JSONObject grantVnf(JSONObject jsonInstantiateOfReq) {
        LOG.info("fuc=[grantVnfLifecycle], start!");

        RestfulResponse rsp = null;
        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        try {
            String url = Constant.NSLCM_URL_BASE+ Constant.GRANT_VNF_URL;
            rsp = HttpRestfulAPIUtil.getRemoteResponse(url, Constant.POST, jsonInstantiateOfReq.toString());

            if(null == rsp) {
                restJson.put(Constant.RESP_STATUS, Constant.HTTP_NOTFOUND);
                LOG.error("fuc=[grantVnfLifecycle], invalid Response!");
                return restJson;
            } else {
                restJson.put(Constant.RETCODE, Constant.HTTP_OK);
                restJson.put(Constant.RESP_STATUS, rsp.getStatus());
                restJson.put(Constant.DATA, rsp.getResponseContent());
            }
        } catch(JSONException e) {
            restJson.put(Constant.RESP_STATUS, Constant.HTTP_INNERERROR);
            LOG.error("fuc=[grantVnfLifecycle], JSONException!");
        }

        LOG.info("fuc=[grantVnfLifecycle], end!");
        return restJson;
    }
}
