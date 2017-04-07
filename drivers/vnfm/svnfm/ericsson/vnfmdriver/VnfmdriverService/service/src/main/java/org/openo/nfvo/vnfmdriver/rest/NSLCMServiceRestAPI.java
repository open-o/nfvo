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

package org.openo.nfvo.vnfmdriver.rest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import net.sf.json.JSONObject;

import org.openo.nfvo.vnfmdriver.common.constant.Constant;
import org.openo.nfvo.vnfmdriver.common.restfulutil.HttpContextUitl;
import org.openo.nfvo.vnfmdriver.process.NSLCMServiceProcessor;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Feb 28, 2017
 */
@Component
@Path("/openoapi/nslcm/v1/ns")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NSLCMServiceRestAPI {

    private static final Logger LOG = LogManager.getLogger(NSLCMServiceRestAPI.class);

    @Resource
    private NSLCMServiceProcessor nslcmServiceProcessor;

    /**
     * <br>
     *
     * @param req
     * @param resp
     * @return
     * @since NFVO 0.5
     */
    @POST
    @Path("/grantvnf")
    public String grantVnf(@Context HttpServletRequest req, @Context HttpServletResponse resp) throws IOException {
        LOG.info("fuc=[grantVnf] start!");

        JSONObject jsonInstantiateOfReq = HttpContextUitl.extractJsonObject(req);
        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        if(null == jsonInstantiateOfReq) {
            LOG.error("fuc=[grantVnf] Invalid Request!");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson = nslcmServiceProcessor.grantVnf(jsonInstantiateOfReq);


        resp.setStatus(restJson.getInt(Constant.RESP_STATUS));
        resp.flushBuffer();

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("fuc=[grantVnf] fail!");
            return "";
        }

        LOG.info("fuc=[grantVnf] end!");
        return restJson.getString(Constant.DATA);
    }
}
