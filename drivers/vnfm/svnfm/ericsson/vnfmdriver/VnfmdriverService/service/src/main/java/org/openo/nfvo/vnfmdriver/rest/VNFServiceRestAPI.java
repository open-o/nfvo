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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

import org.apache.commons.lang3.StringUtils;
import org.openo.nfvo.vnfmdriver.common.constant.Constant;
import org.openo.nfvo.vnfmdriver.common.restfulutil.HttpContextUitl;
import org.openo.nfvo.vnfmdriver.process.VNFServiceProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Feb 28, 2017
 */
@Component
@Path("/openoapi/ericsson-vnfm/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VNFServiceRestAPI {

    private static final Logger LOG = LoggerFactory.getLogger(VNFServiceRestAPI.class);

    @Resource
    private VNFServiceProcessor vnfServiceProcessor;

    /**
     * <br>
     *
     * @param vnfmId
     * @param req
     * @param resp
     * @return
     * @since NFVO 0.5
     */
    @POST
    @Path("/{vnfmId}/vnfs")
    public String instantiateVNF(@PathParam("vnfmId") String vnfmId, @Context HttpServletRequest req,
                                 @Context HttpServletResponse resp) throws IOException {
        LOG.info("class=[VNFServiceRestAPI], fuc=[instantiateVNF], start!");

        JSONObject jsonInstantiateOfReq = HttpContextUitl.extractJsonObject(req);
        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        if((null == jsonInstantiateOfReq) || StringUtils.isEmpty(vnfmId)) {
            LOG.error("fuc=[instantiateVNF], Invalid Request!");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson = vnfServiceProcessor.addVnf(vnfmId, jsonInstantiateOfReq);

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("fuc=[instantiateVNF], instantiateVNF fail!");
            resp.setStatus(Constant.HTTP_INNERERROR);
            resp.flushBuffer();
            return restJson.toString();
        }

        resp.setStatus(restJson.getInt(Constant.REMOTE_RESP_STATUS));
        resp.flushBuffer();

        LOG.info("class=[VNFServiceRestAPI], fuc=[instantiateVNF], end!");
        return JSONObject.fromObject(restJson.getJSONObject("data")).toString();
    }

    /**
     * <br>
     *
     * @param req
     * @param resp
     * @param vnfmId
     * @param vnfInstanceId
     * @return
     * @since NFVO 0.5
     */
    @POST
    @Path("/{vnfmId}/vnfs/{vnfInstanceId}/terminate")
    public String terminateVNF(@Context HttpServletRequest req, @Context HttpServletResponse resp,
                               @PathParam("vnfmId") String vnfmId,
                               @PathParam("vnfInstanceId") String vnfInstanceId) throws IOException {
        LOG.info("class=[VNFServiceRestAPI], fuc=[terminateVNF], start!");

        JSONObject jsonTerminateOfReq = HttpContextUitl.extractJsonObject(req);
        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        if(StringUtils.isEmpty(vnfmId) || StringUtils.isEmpty(vnfInstanceId) ||
                                          null == jsonTerminateOfReq) {
            LOG.error("fuc=[terminateVNF], Invalid Request!");

            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson = vnfServiceProcessor.deleteVnf(vnfmId, vnfInstanceId, jsonTerminateOfReq);

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("fuc=[terminateVNF], terminateVNF fail!");

            resp.setStatus(Constant.HTTP_INNERERROR);
            resp.flushBuffer();
            return restJson.toString();
        }

        resp.setStatus(restJson.getInt(Constant.REMOTE_RESP_STATUS));
        resp.flushBuffer();

        LOG.info("class=[VNFServiceRestAPI], fuc=[terminateVNF], end!");

        return JSONObject.fromObject(restJson.getJSONObject("data")).toString();
    }

    /**
     * <br>
     *
     * @param vnfmId
     * @param vnfInstanceId
     * @param resp
     * @return
     * @since NFVO 0.5
     */
    @GET
    @Path("/{vnfmId}/vnfs/{vnfInstanceId}")
    public String queryVNF(@PathParam("vnfmId") String vnfmId, @PathParam("vnfInstanceId") String vnfInstanceId,
                           @Context HttpServletResponse resp) throws IOException {
        LOG.info("class=[VNFServiceRestAPI], fuc=[terminateVNF], start!");

        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        if(StringUtils.isEmpty(vnfmId) || StringUtils.isEmpty(vnfInstanceId)) {
            LOG.error("fuc=[queryVNF], Invalid Request!");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson = vnfServiceProcessor.getVnf(vnfmId, vnfInstanceId);

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("fuc=[queryVNF], queryVNF fail!");
            resp.setStatus(Constant.HTTP_INNERERROR);
            resp.flushBuffer();
            return restJson.toString();
        }

        resp.setStatus(restJson.getInt(Constant.REMOTE_RESP_STATUS));
        resp.flushBuffer();

        LOG.info("class=[VNFServiceRestAPI], fuc=[terminateVNF], end!");
        return JSONObject.fromObject(restJson.getJSONObject("data")).toString();
    }

    /**
     * <br>
     *
     * @param jobid
     * @param responseId
     * @param resp
     * @return
     * @since NFVO 0.5
     */
    @GET
    @Path("/{vnfmId}/jobs/{jobid}")
    public String getOperationStatus(@PathParam("vnfmId") String vnfmId, @PathParam("jobid") String jobid,
                                     @QueryParam("responseId") String responseId,
                                     @Context HttpServletResponse resp) throws IOException {
        LOG.info("class=[VNFServiceRestAPI], fuc=[getOperationStatus], start!");

        JSONObject restJson = new JSONObject();
        restJson.put(Constant.RETCODE, Constant.REST_FAIL);

        if(StringUtils.isEmpty(vnfmId) || StringUtils.isEmpty(jobid) || StringUtils.isEmpty(responseId)) {
            LOG.error("fuc=[getOperationStatus], Invalid Request!");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson = vnfServiceProcessor.getStatus(vnfmId, jobid, responseId);

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("fuc=[getOperationStatus], getOperationStatus fail!");
            resp.setStatus(Constant.HTTP_INNERERROR);
            resp.flushBuffer();
            return restJson.toString();
        }

        resp.setStatus(restJson.getInt(Constant.REMOTE_RESP_STATUS));
        resp.flushBuffer();

        LOG.info("class=[VNFServiceRestAPI], fuc=[getOperationStatus], end!");
        return JSONObject.fromObject(restJson.getJSONObject("data")).toString();
    }

    /**
     * <br>
     *
     * @return
     * @throws IOException
     * @since NFVO 0.5
     */
    @GET
    @Path("/swagger.json")
    public String getApiDoc(@Context HttpServletResponse resp) throws IOException {
        LOG.info("class=[VNFServiceRestAPI], fuc=[getApiDoc], start!");

        String ret = "";
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            ret = IOUtils.toString(classLoader.getResourceAsStream("swagger/generated/swagger-ui/swagger.json"));
        } catch (IOException e) {
            resp.setStatus(Constant.HTTP_INNERERROR);
            resp.flushBuffer();
            LOG.error("fuc=[getApiDoc], IOException!");
        }

        LOG.info("class=[VNFServiceRestAPI], fuc=[getApiDoc], end!");
        return ret;
    }
}
