/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.nfvo.vnfmadapter.common;

import java.util.Map;

import net.sf.json.JSONObject;

import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vnfmadapter.common.servicetoken.VNFRestfulUtil;
import org.openo.nfvo.vnfmadapter.service.constant.Constant;
import org.openo.nfvo.vnfmadapter.service.vnfm.api.ConnectInfo;
import org.openo.nfvo.vnfmadapter.service.vnfm.connect.ConnectMgrVnfm;
import org.openo.nfvo.vnfmadapter.service.vnfm.connect.VnfmConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ResultRequestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ResultRequestUtil.class);

    private ResultRequestUtil() throws VnfmException {
        throw new VnfmException("can't be instanced.");
    }

    /**
     * @param info
     *            Connection info
     * @param path
     *            url defined
     * @param methodName
     *            [get, put, delete, post]
     * @param paramsJson
     *            raw data with json format, if <code>methodName</code> is get
     *            or delete, fill it with null
     * @return
     */
    public static JSONObject call(ConnectInfo info, String path, String methodName, String paramsJson) {
        JSONObject resultJson = new JSONObject();
        if(info == null) {
            LOG.error("function=call, msg= connection info is null.");
            resultJson.put("retCode", Constant.REST_FAIL);
            resultJson.put("data", "connection info is null.");
            return resultJson;
        }
        VnfmConnection connect;
        String result = null;
        ConnectMgrVnfm mgrVcmm = new ConnectMgrVnfm();
        connect = mgrVcmm.getConnection(info);
        if(connect == null) {
            connect = new VnfmConnection(info);
        }
        String vnfPath = path.contains("%s") ? String.format(path, connect.gotRoaRand()) : path;
        LOG.info("function=call, msg=url is {}.", info.getUrl() + vnfPath);
        Map<String, String> paramsMap =
                VNFRestfulUtil.generateParamsMap(vnfPath, methodName, info.getUrl(), info.getAuthenticateMode());
        RestfulResponse rsp =
                VNFRestfulUtil.getRemoteResponse(paramsMap, paramsJson, connect.gotAccessSession(), false);
        if(rsp == null) {
            resultJson.put("retCode", Constant.HTTP_INNERERROR);
            resultJson.put("data", "get restful response error.");
            return resultJson;
        }
        result = rsp.getResponseContent();
        LOG.warn("function=call, msg=response status is {}.", rsp.getStatus());
        resultJson.put("retCode", rsp.getStatus());
        resultJson.put("data", result);
        return resultJson;
    }
}
