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

package org.openo.nfvo.resmanagement.service.business.impl;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.nfvo.resmanagement.common.constant.ParamConstant;
import org.openo.nfvo.resmanagement.common.constant.UrlConstant;
import org.openo.nfvo.resmanagement.common.util.RestfulUtil;
import org.openo.nfvo.resmanagement.service.business.inf.LimitsBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version NFVO 0.5 Sep 10, 2016
 */
public class LimitsBusinessImpl implements LimitsBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(LimitsBusinessImpl.class);

    /**
     * <br>
     * 
     * @param paramJson
     * @return
     * @throws ServiceException
     * @since NFVO 0.5
     */
    @Override
    public JSONObject getCpuLimits(JSONObject paramJson) throws ServiceException {
        return getResponse(paramJson, UrlConstant.GET_LIMITSCPU_URL);
    }

    /**
     * <br>
     * 
     * @param paramJson
     * @return
     * @throws ServiceException
     * @since NFVO 0.5
     */
    @Override
    public JSONObject getDiskLimits(JSONObject paramJson) throws ServiceException {
        return getResponse(paramJson, UrlConstant.GET_LIMITSDISK_URL);
    }

    /**
     * <br>
     * 
     * @param paramJson
     * @param getLimitscpuUrl
     * @return
     * @since NFVO 0.5
     */
    private JSONObject getResponse(JSONObject paramJson, String getLimitscpuUrl) {
        String vimId = paramJson.getString(ParamConstant.PARAM_VIMID);
        String tenantId = paramJson.getString(ParamConstant.PARAM_TENANTID);
        String url = String.format(getLimitscpuUrl, tenantId);
        RestfulParametes restParametes = new RestfulParametes();
        restParametes.put("vimId", vimId);
        String result = RestfulUtil.getResponseContent(url, restParametes, ParamConstant.PARAM_GET);
        LOGGER.warn("function=getLimits; result={}", result);
        if(null == result) {
            JSONObject obj = new JSONObject();
            obj.put("msg", "getLimits fail!");
            return obj;
        }
        return JSONObject.fromObject(result);
    }

}
