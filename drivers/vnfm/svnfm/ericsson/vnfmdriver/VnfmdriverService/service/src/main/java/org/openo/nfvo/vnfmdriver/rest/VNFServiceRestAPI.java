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
    public String instantiateVNF(@PathParam("vnfmId") String vnfmId,
                                 @Context HttpServletRequest req,
                                 @Context HttpServletResponse resp)throws IOException {
        JSONObject jsonInstantiateOfReq = HttpContextUitl.extractJsonObject(req);
        if (null == jsonInstantiateOfReq) {
            LOG.info("Receive Instantiate VNF request. VNFM id:{},  url:{}, type:{}, request body:{}",
                    vnfmId, req.getRequestURL(), req.getMethod(), "");
            LOG.error("Invalid request parameter");
            resp.setStatus(Constant.HTTP_NOTFOUND);
            resp.flushBuffer();
            LOG.info("Request Instantiate VNF finish. Returned response status:{}, body:{}", Constant.HTTP_NOTFOUND, "");
            return "";
        }
        LOG.info("Receive Instantiate VNF request. VNFM id:{},  url:{}, type:{}, request body:{}",
                vnfmId, req.getRequestURL(), req.getMethod(), jsonInstantiateOfReq.toString());


        JSONObject restJson = vnfServiceProcessor.addVnf(vnfmId, jsonInstantiateOfReq);

        resp.setStatus(restJson.getInt(Constant.RESP_STATUS));
        resp.flushBuffer();

        if (restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("Instantiate vnf fail.");
            LOG.info("Request Instantiate VNF finish. Returned response status:{}, body:{}", Constant.HTTP_NOTFOUND, "");
            return "";
        }

        LOG.info("Request Instantiate VNF finish. Returned response status:{}, body:{}",
                restJson.getInt(Constant.RESP_STATUS), restJson.getString(Constant.DATA));
        return restJson.getString(Constant.DATA);
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
    public String terminateVNF(@Context HttpServletRequest req,
                               @Context HttpServletResponse resp,
                               @PathParam("vnfmId") String vnfmId,
                               @PathParam("vnfInstanceId") String vnfInstanceId) throws IOException {

        JSONObject jsonTerminateOfReq = HttpContextUitl.extractJsonObject(req);
        if (null == jsonTerminateOfReq) {
            LOG.info("Receive Terminate VNF request. VNFM id:{}, VNF instance id:{}, url:{}, type:{}, request body:{}",
                    vnfmId, vnfInstanceId, req.getRequestURL(), req.getMethod(), "");
            LOG.error("Invalid request parameter");

            resp.setStatus(Constant.HTTP_NOTFOUND);
            resp.flushBuffer();
            LOG.info("Request Terminate VNF finish. Returned response status:{}, body:{}", Constant.HTTP_NOTFOUND, "");
            return "";
        }
        LOG.info("Receive Terminate VNF request. VNFM id:{}, VNF instance id:{}, url:{}, type:{}, request body:{}",
                vnfmId, vnfInstanceId, req.getRequestURL(), req.getMethod(), jsonTerminateOfReq);

        JSONObject restJson = vnfServiceProcessor.deleteVnf(vnfmId, vnfInstanceId, jsonTerminateOfReq);

        resp.setStatus(restJson.getInt(Constant.RESP_STATUS));
        resp.flushBuffer();

        if (restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("Terminate vnf fail.");
            LOG.info("Request Terminate VNF finish. Returned response status:{}, body:{}", Constant.HTTP_NOTFOUND, "");
            return "";
        }

        LOG.info("Request Terminate VNF finish. Returned response status:{}, body:{}",
                restJson.getInt(Constant.RESP_STATUS), restJson.getString(Constant.DATA));
        return restJson.getString(Constant.DATA);
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
    public String queryVNF(@PathParam("vnfmId") String vnfmId,
                           @PathParam("vnfInstanceId") String vnfInstanceId,
                           @Context HttpServletResponse resp,
                           @Context HttpServletRequest req) throws IOException {
        LOG.info("Receive Query VNF request. VNFM id:{}, VNF instance id:{}, url:{}, type:{}, request body:{}",
                vnfmId, vnfInstanceId, req.getRequestURL(), req.getMethod(), "");

        JSONObject restJson = vnfServiceProcessor.getVnf(vnfmId, vnfInstanceId);

        resp.setStatus(restJson.getInt(Constant.RESP_STATUS));
        resp.flushBuffer();

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("Query vnf fail.");
            LOG.info("Request Query VNF finish. Returned response status:{}, body:{}", Constant.HTTP_NOTFOUND, "");
            return "";
        }

        LOG.info("Request Query VNF finish. Returned response status:{}, body:{}",
                restJson.getInt(Constant.RESP_STATUS), restJson.getString(Constant.DATA));
        return restJson.getString(Constant.DATA);
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
    public String getOperationStatus(@PathParam("vnfmId") String vnfmId,
                                     @PathParam("jobid") String jobid,
                                     @QueryParam("responseId") String responseId,
                                     @Context HttpServletRequest req,
                                     @Context HttpServletResponse resp) throws IOException {
        LOG.info("Receive Get Operation Status request. VNFM id:{}, job id:{}, responseId:{}, url:{}, type:{}, request body:{}",
                vnfmId, jobid, responseId, req.getRequestURL(), req.getMethod(), "");

        if(StringUtils.isEmpty(responseId)) {
            LOG.error("Invalid request parameter");
            resp.setStatus(Constant.HTTP_NOTFOUND);
            resp.flushBuffer();
            LOG.info("Request Get Operation Status finish. Returned response status:{}, body:{}", Constant.HTTP_NOTFOUND, "");
            return "";
        }

        JSONObject restJson = vnfServiceProcessor.getStatus(vnfmId, jobid, responseId);

        resp.setStatus(restJson.getInt(Constant.RESP_STATUS));
        resp.flushBuffer();

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("Get operation status fail.");
            LOG.info("Request Get Operation Status finish. Returned response status:{}, body:{}", Constant.HTTP_NOTFOUND, "");
            return "";
        }

        LOG.info("Request Get Operation Status finish. Returned response status:{}, body:{}",
                restJson.getInt(Constant.RESP_STATUS), restJson.getString(Constant.DATA));
        return restJson.getString(Constant.DATA);
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
    public String getApiDoc(@Context HttpServletResponse resp,
                            @Context HttpServletRequest req) throws IOException {
        LOG.info("Receive Get API Doc request. url:{}, type:{}, request body:{}",
                req.getRequestURL(), req.getMethod(), "");

        String ret = "";
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            ret = IOUtils.toString(classLoader.getResourceAsStream("swagger.json"));
        } catch(IOException e) {
            resp.setStatus(Constant.HTTP_INNERERROR);
            resp.flushBuffer();
            LOG.error("Get API Doc Fail, IOException! {}", e.getMessage());
        }

        LOG.info("Request Get API Doc finish. Returned response status:200, body:{}", ret);
        return ret;
    }
}
