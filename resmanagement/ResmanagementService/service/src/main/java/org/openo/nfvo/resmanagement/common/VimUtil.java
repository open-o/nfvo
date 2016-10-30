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

package org.openo.nfvo.resmanagement.common;

import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.nfvo.resmanagement.common.constant.UrlConstant;
import org.openo.nfvo.resmanagement.common.util.RestfulUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version NFVO 0.5 Oct 30, 2016
 */
public class VimUtil {

    private static final Logger LOG = LoggerFactory.getLogger(VimUtil.class);

    private VimUtil() {

    }

    /**
     * Get VIMs.
     * 
     * @return
     */
    public static JSONArray getVims() {
        String esrResponse =
                RestfulUtil.getResponseContent(UrlConstant.ESR_GET_VIMS_URL, new RestfulParametes(), "get");
        LOG.info("Get vims from ESR! EsrResponse:{}", esrResponse);
        if(null == esrResponse) {
            LOG.error("ESR return fail.");
            return null;
        } else {
            return JSONArray.fromObject(esrResponse);
        }

    }

    /**
     * Get VIM.
     * 
     * @param vimId
     * @return
     */
    public static JSONObject getVimById(String vimId) {
        JSONObject esrResponse = RestfulUtil.getResponseObj(String.format(UrlConstant.ESR_GET_VIM_URL, vimId), "get");
        LOG.info("Get vims from ESR! EsrResponse:{}", esrResponse);
        if(null == esrResponse) {
            LOG.error("ESR return fail.");
            return null;
        } else {
            return esrResponse;
        }
    }
}
