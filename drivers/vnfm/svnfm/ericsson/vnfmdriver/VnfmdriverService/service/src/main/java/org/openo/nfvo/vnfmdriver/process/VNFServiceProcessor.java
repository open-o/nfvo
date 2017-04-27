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
import org.openo.nfvo.vnfmdriver.common.VnfmUtil;
import org.openo.nfvo.vnfmdriver.common.constant.Constant;
import org.openo.nfvo.vnfmdriver.common.restfulutil.HttpRestfulAPIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Feb 28, 2017
 */
@Service("vnfServiceProcessor")
public class VNFServiceProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(VNFServiceProcessor.class);

    /**
     * <br>
     *
     * @param vnfmId
     * @param jsonInstantiateOfReq
     * @return
     * @since NFVO 0.5
     */
    public JSONObject addVnf(String vnfmId, JSONObject jsonInstantiateOfReq) {

        RestfulResponse rsp = null;
        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        try {
            JSONObject vnfmObject = VnfmUtil.getVnfmById(vnfmId);
            if (null == vnfmObject) {
                restJson.put(Constant.RESP_STATUS, Constant.HTTP_NOTFOUND);
                return restJson;
            }

            String path = vnfmObject.getString("url");
            String uri = Constant.VNF_URL_BASE + String.format(Constant.ADD_VNF_URL, vnfmId);

            LOG.info("Request E/// vnfm Instantiate Vnf API. VNFM id:{}, URL:{}, Type:{}, Request body:{}",
                    vnfmId, path + uri, "POST", jsonInstantiateOfReq.toString());

            rsp = HttpRestfulAPIUtil.getRemoteResponse(path, uri, Constant.POST, jsonInstantiateOfReq.toString());

            if(null == rsp) {
                LOG.error("Receive null response");
                restJson.put(Constant.RESP_STATUS, Constant.HTTP_NOTFOUND);
                return restJson;
            } else {
                LOG.info("Receive response of Instantiate VNF. Status:{}, body:{}",
                        rsp.getStatus(), rsp.getResponseContent());
                restJson.put(Constant.RETCODE, Constant.HTTP_OK);
                restJson.put(Constant.RESP_STATUS, rsp.getStatus());
                restJson.put(Constant.DATA, rsp.getResponseContent());
            }
        } catch(JSONException e) {
            restJson.put(Constant.RESP_STATUS, Constant.HTTP_INNERERROR);
            LOG.error("JSONException!" + e.getMessage());
        }
        return restJson;
    }

    /**
     * Provide function for terminate VNF
     *
     * @param vnfmId
     * @param vnfInstanceId
     * @param jsonTerminateOfReq
     * @return
     * @since NFVO 0.5
     */
    public JSONObject deleteVnf(String vnfmId, String vnfInstanceId, JSONObject jsonTerminateOfReq) {

        RestfulResponse rsp = null;
        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        try {
            JSONObject vnfmObject = VnfmUtil.getVnfmById(vnfmId);
            if (null == vnfmObject) {
                restJson.put(Constant.RESP_STATUS, Constant.HTTP_NOTFOUND);
                return restJson;
            }

            String path = vnfmObject.getString("url");
            String uri = Constant.VNF_URL_BASE + String.format(Constant.DEL_VNF_URL, vnfmId, vnfInstanceId);

            LOG.info("Request E/// vnfm Terminate VNF API. VNFM id:{}, VNF instance id:{}, URL:{}, Type:{}, Request body:{}",
                    vnfmId, vnfInstanceId, path + uri, "POST", jsonTerminateOfReq.toString());

            rsp = HttpRestfulAPIUtil.getRemoteResponse(path, uri, Constant.POST, jsonTerminateOfReq.toString());

            if(null == rsp) {
                LOG.error("Receive null response");
                restJson.put(Constant.RESP_STATUS, Constant.HTTP_NOTFOUND);
                return restJson;
            } else {
                LOG.info("Receive response of Terminate VNF. Status:{}, body:{}",
                        rsp.getStatus(), rsp.getResponseContent());
                restJson.put(Constant.RETCODE, Constant.HTTP_OK);
                restJson.put(Constant.RESP_STATUS, rsp.getStatus());
                restJson.put(Constant.DATA, rsp.getResponseContent());
            }
        } catch(JSONException e) {
            restJson.put(Constant.RESP_STATUS, Constant.HTTP_INNERERROR);
            LOG.error("JSONException!" + e.getMessage());
        }

        return restJson;
    }

    /**
     * <br>
     *
     * @param vnfmId
     * @param vnfInstanceId
     * @return
     * @since NFVO 0.5
     */
    public JSONObject getVnf(String vnfmId, String vnfInstanceId) {
        RestfulResponse rsp = null;
        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        try {
            JSONObject vnfmObject = VnfmUtil.getVnfmById(vnfmId);
            if (null == vnfmObject) {
                restJson.put(Constant.RESP_STATUS, Constant.HTTP_NOTFOUND);
                return restJson;
            }

            String path = vnfmObject.getString("url");
            String uri = Constant.VNF_URL_BASE +
                         String.format(Constant.QEURY_VNF_URL, vnfmId, vnfInstanceId);

            LOG.info("Request E/// vnfm Query VNF API. VNFM id:{}, VNF instance id:{}, URL:{}, Type:{}, Request body:{}",
                    vnfmId, vnfInstanceId, path + uri, "GET", "");
            rsp = HttpRestfulAPIUtil.getRemoteResponse(path, uri, Constant.GET, null);

            if(null == rsp) {
                LOG.error("Receive null response");
                restJson.put(Constant.RESP_STATUS, Constant.HTTP_NOTFOUND);
                return restJson;
            } else {
                LOG.info("Receive response of Query VNF. Status:{}, body:{}",
                        rsp.getStatus(), rsp.getResponseContent());
                restJson.put(Constant.RETCODE, Constant.HTTP_OK);
                restJson.put(Constant.RESP_STATUS, rsp.getStatus());
                restJson.put(Constant.DATA, rsp.getResponseContent());
            }
        } catch(JSONException e) {
            restJson.put(Constant.RESP_STATUS, Constant.HTTP_INNERERROR);
            LOG.error("JSONException! {}", e.getMessage());
        }

        return restJson;
    }

    /**
     * <br>
     *
     * @param jobid
     * @param responseId
     * @return
     * @since NFVO 0.5
     */
    public JSONObject getStatus(String vnfmId, String jobid, String responseId) {
        RestfulResponse rsp = null;
        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        try {
            JSONObject vnfmObject = VnfmUtil.getVnfmById(vnfmId);
            if (null == vnfmObject) {
                restJson.put(Constant.RESP_STATUS, Constant.HTTP_NOTFOUND);
                return restJson;
            }

            String path = vnfmObject.getString("url");
            String uri = Constant.VNF_URL_BASE + String.format(Constant.GET_VNF_STATUS_URL,
                                                                vnfmId, jobid, responseId);

            LOG.info("Request E/// vnfm Get Operation Status API. " +
                     "VNFM id:{}, job id:{}, response id:{}, URL:{}, Type:{}, Request body:{}",
                     vnfmId, jobid, responseId, path + uri, "GET", "");

            rsp = HttpRestfulAPIUtil.getRemoteResponse(path, uri, Constant.GET, null);

            if(null == rsp) {
                LOG.error("Receive null response");
                restJson.put(Constant.RESP_STATUS, Constant.HTTP_NOTFOUND);
                return restJson;
            } else {
                LOG.info("Receive response of Get Operation Status. Status:{}, body:{}",
                        rsp.getStatus(), rsp.getResponseContent());
                restJson.put(Constant.RETCODE, Constant.HTTP_OK);
                restJson.put(Constant.RESP_STATUS, rsp.getStatus());
                restJson.put(Constant.DATA, rsp.getResponseContent());
            }
        } catch(JSONException e) {
            restJson.put(Constant.RESP_STATUS, Constant.HTTP_INNERERROR);
            LOG.error("JSONException!, {}", e.getMessage());
        }

        return restJson;
    }
}
