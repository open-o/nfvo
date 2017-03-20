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
        LOG.info("class=[VNFServiceProcessor], fuc=[addVnf], start!");

        RestfulResponse rsp = null;
        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        try {
            JSONObject vnfmObjcet = VnfmUtil.getVnfmById(vnfmId);
            if (null == vnfmObjcet) {
                LOG.error("func=[addVnf], get Vnfmd info fail!");
                return restJson;
            }

            String path = Constant.HTTP_PROTOCOL + vnfmObjcet.getString("url");
            String api = Constant.VNF_URL_BASE +
                         String.format(Constant.ADD_VNF_URL, vnfmId);
            rsp = HttpRestfulAPIUtil.getRemoteResponse(path, api, Constant.POST,
                                                        jsonInstantiateOfReq.toString());
            if(null == rsp) {
                LOG.error("class=[VNFServiceProcessor], fuc=[addVnf], invalid Response!");
                return restJson;
            } else {
                restJson.put(Constant.RETCODE, Constant.HTTP_OK);
                restJson.put(Constant.REMOTE_RESP_STATUS, rsp.getStatus());
                restJson.put("data", JSONObject.fromObject(rsp.getResponseContent()));
            }
        } catch(JSONException e) {
            LOG.error("class=[VNFServiceProcessor], fuc=[addVnf], JSONException!");
        }

        LOG.info("class=[VNFServiceProcessor], fuc=[addVnf], end!");
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
        LOG.info("class=[VNFServiceProcessor], fuc=[deleteVnf], start!");

        RestfulResponse rsp = null;
        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        try {
            JSONObject vnfmObjcet = VnfmUtil.getVnfmById(vnfmId);
            if (null == vnfmObjcet) {
                LOG.error("func=[deleteVnf], get Vnfmd info fail!");
                return restJson;
            }

            String path = Constant.HTTP_PROTOCOL + vnfmObjcet.getString("url");
            String api = Constant.VNF_URL_BASE +
                         String.format(Constant.DEL_VNF_URL, vnfmId, vnfInstanceId);
            rsp = HttpRestfulAPIUtil.getRemoteResponse(path, api, Constant.POST,
                                                        jsonTerminateOfReq.toString());
            if(null == rsp) {
                LOG.error("class=[VNFServiceProcessor], fuc=[deleteVnf], invalid Response!");
                return restJson;
            } else {
                restJson.put(Constant.RETCODE, Constant.HTTP_OK);
                restJson.put(Constant.REMOTE_RESP_STATUS, rsp.getStatus());
                restJson.put("data", JSONObject.fromObject(rsp.getResponseContent()));
            }
        } catch(JSONException e) {
            LOG.error("class=[VNFServiceProcessor], fuc=[deleteVnf], JSONException!");
        }

        LOG.info("class=[VNFServiceProcessor], fuc=[deleteVnf], end!");
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
        LOG.info("class=[VNFServiceProcessor], fuc=[getVnf], start!");

        RestfulResponse rsp = null;
        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        try {
            JSONObject vnfmObjcet = VnfmUtil.getVnfmById(vnfmId);
            if (null == vnfmObjcet) {
                LOG.error("func=[getVnf], get Vnfmd info fail!");
                return restJson;
            }

            String path = Constant.HTTP_PROTOCOL + vnfmObjcet.getString("url");
            String api = Constant.VNF_URL_BASE +
                         String.format(Constant.QEURY_VNF_URL, vnfmId, vnfInstanceId);
            rsp = HttpRestfulAPIUtil.getRemoteResponse(path, api, Constant.GET, null);

            if(null == rsp) {
                LOG.error("class=[VNFServiceProcessor], fuc=[getVnf], invalid Response!");
                return restJson;
            } else {
                restJson.put(Constant.RETCODE, Constant.HTTP_OK);
                restJson.put(Constant.REMOTE_RESP_STATUS, rsp.getStatus());
                restJson.put("data", JSONObject.fromObject(rsp.getResponseContent()));
            }
        } catch(JSONException e) {
            LOG.error("class=[VNFServiceProcessor], fuc=[getVnf], JSONException!");
        }

        LOG.info("class=[VNFServiceProcessor], fuc=[getVnf], end!");
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
        LOG.info("class=[VNFServiceProcessor], fuc=[getStatus], start!");

        RestfulResponse rsp = null;
        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        try {
            JSONObject vnfmObjcet = VnfmUtil.getVnfmById(vnfmId);
            if (null == vnfmObjcet) {
                LOG.error("func=[getStatus], get Vnfmd info fail!");
                return restJson;
            }

            String path = Constant.HTTP_PROTOCOL + vnfmObjcet.getString("url");
            String api = Constant.VNF_URL_BASE + String.format(Constant.GET_VNF_STATUS_URL,
                                                                vnfmId, jobid, responseId);
            rsp = HttpRestfulAPIUtil.getRemoteResponse(path, api, Constant.GET, null);

            if(null == rsp) {
                LOG.error("class=[VNFServiceProcessor], fuc=[getStatus], invalid Response!");
                return restJson;
            } else {
                restJson.put(Constant.RETCODE, Constant.HTTP_OK);
                restJson.put(Constant.REMOTE_RESP_STATUS, rsp.getStatus());
                restJson.put("data", JSONObject.fromObject(rsp.getResponseContent()));
            }
        } catch(JSONException e) {
            LOG.error("class=[VNFServiceProcessor], fuc=[getVnf], JSONException!");
        }

        LOG.info("class=[VNFServiceProcessor], fuc=[getStatus], end!");
        return restJson;
    }
}
