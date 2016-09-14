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

package org.openo.nfvo.jujuvnfmadapter.service.rest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.collections.map.UnmodifiableMap;
import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.nfvo.jujuvnfmadapter.common.EntityUtils;
import org.openo.nfvo.jujuvnfmadapter.common.StringUtil;
import org.openo.nfvo.jujuvnfmadapter.service.constant.Constant;
import org.openo.nfvo.jujuvnfmadapter.service.process.VnfMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * Provide interfaces for instantiate or terminate VNF.
 * <br/>
 * 
 * @author
 * @version NFVO 0.5 Aug 24, 2016
 */
@SuppressWarnings("unchecked")
@Path("/openoapi/vnfm/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VnfRoa {

    private static final Logger LOG = LoggerFactory.getLogger(VnfRoa.class);

    private VnfMgr vnfMgr;

    private static Map<String, String> progressItem;

    private static Map<String, String> jobStatusitem;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("Building", "50");
        map.put("Active", "100");
        map.put("Stopped", "50");
        map.put("Error", "100");
        
        progressItem = UnmodifiableMap.decorate(map);

        map = new HashMap<>();
        map.put("Building", "processing");
        map.put("Active", "finished");
        map.put("Stopped", "processing");
        map.put("Error", "error");
        jobStatusitem = UnmodifiableMap.decorate(map);
    }

    public void setVnfMgr(VnfMgr vnfMgr) {
        this.vnfMgr = vnfMgr;
    }

    /**
     * Provide function for instantiate VNF
     * <br/>
     * 
     * @param context
     * @param resp
     * @param vnfmId
     * @return
     * @throws ServiceException
     * @since NFVO 0.5
     */
    @POST
    @Path("/{vnfmId}/vnfs")
    public String addVnf(@Context HttpServletRequest context, @Context HttpServletResponse resp,
            @PathParam("vnfmId") String vnfmId) throws ServiceException {
        LOG.warn("function=addVnf, msg=enter to add a vnf");
        JSONObject subJsonObject = StringUtil.getJsonFromContexts(context);
        JSONObject restJson = new JSONObject();

        if(null == subJsonObject) {
            LOG.error("function=addVnf, msg=params are insufficient");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson = vnfMgr.addVnf(subJsonObject, vnfmId);

        if(restJson.getInt(EntityUtils.RESULT_CODE_KEY) == Constant.REST_FAIL) {
            LOG.error("function=addVnf, msg=addvnf fail");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        return JSONObject.fromObject(restJson.getJSONObject("data")).toString();
    }

    /**
     * Provide function for terminate VNF
     * <br/>
     * 
     * @param vnfmId
     * @param resp
     * @param vnfInstanceId
     * @param context
     * @return
     * @throws ServiceException
     * @since NFVO 0.5
     */
    @POST
    @Path("/{vnfmId}/vnfs/{vnfInstanceId}/terminate")
    public String delVnf(@PathParam("vnfmId") String vnfmId, @Context HttpServletResponse resp,
            @PathParam("vnfInstanceId") String vnfInstanceId, @Context HttpServletRequest context)
            throws ServiceException {
        LOG.warn("function=delVnf, msg=enter to delete a vnf: vnfInstanceId: {}, vnfmId: {}", vnfInstanceId, vnfmId);
        JSONObject vnfObject = StringUtil.getJsonFromContexts(context);
        JSONObject restJson = new JSONObject();

        if(StringUtils.isEmpty(vnfInstanceId) || StringUtils.isEmpty(vnfmId)) {
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson = vnfMgr.deleteVnf(vnfInstanceId, vnfmId, vnfObject);
        if(restJson.getInt(EntityUtils.RESULT_CODE_KEY) == Constant.REST_FAIL) {
            LOG.error("function=delVnf, msg=delVnf fail");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        return JSONObject.fromObject(restJson.getJSONObject("data")).toString();
    }

    /**
     * Provide function for get VNF
     * <br/>
     * 
     * @param vnfmId
     * @param resp
     * @param vnfInstanceId
     * @param context
     * @return
     * @throws ServiceException
     * @since NFVO 0.5
     */
    @GET
    @Path("/{vnfmId}/vnfs/{vnfInstanceId}")
    public String getVnf(@PathParam("vnfmId") String vnfmId, @Context HttpServletResponse resp,
            @PathParam("vnfInstanceId") String vnfInstanceId, @Context HttpServletRequest context)
            throws ServiceException {
        LOG.warn("function=getVnf, msg=enter to get a vnf: vnfInstanceId: {}, vnfmId: {}", vnfInstanceId, vnfmId);
        JSONObject restJson = new JSONObject();

        if(StringUtils.isEmpty(vnfInstanceId) || StringUtils.isEmpty(vnfmId)) {
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson = vnfMgr.getVnf(vnfInstanceId, vnfmId);
        if(restJson.getInt(EntityUtils.RESULT_CODE_KEY) == Constant.REST_FAIL) {
            LOG.error("function=getVnf, msg=getVnf fail");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson.remove(EntityUtils.RESULT_CODE_KEY);
        return restJson.toString();
    }

    /**
     * Provide function for get job
     * <br/>
     * 
     * @param jobId
     * @param vnfmId
     * @param resp
     * @param responseId
     * @return
     * @throws ServiceException
     * @since NFVO 0.5
     */
    @GET
    @Path("/{vnfmId}/jobs/{jobId}")
    public String getJob(@PathParam("jobId") String jobId, @PathParam("vnfmId") String vnfmId,
            @Context HttpServletResponse resp, @QueryParam("@responseId") String responseId) throws ServiceException {
        LOG.warn("function=getJob, msg=enter to get a job: jobId: {}", jobId);
        JSONObject restJson = new JSONObject();

        if(StringUtils.isEmpty(jobId) || StringUtils.isEmpty(vnfmId)) {
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson = vnfMgr.getJob(jobId, vnfmId);
        if(restJson.getInt(EntityUtils.RESULT_CODE_KEY) == Constant.REST_FAIL) {
            LOG.error("function=getJob, msg=getJob fail");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        return getJobBody(restJson);
    }

    private String getJobBody(JSONObject restJson) {
        JSONObject responseJson = new JSONObject();
        JSONObject jobInfoJson = new JSONObject();
        JSONObject retJson = (JSONObject)restJson.get("data");
        jobInfoJson.put("jobId", retJson.get("id"));
        responseJson.put("progress", progressItem.get(retJson.get(EntityUtils.STATUS)));
        responseJson.put("status", jobStatusitem.get(retJson.get(EntityUtils.STATUS)));
        responseJson.put("errorCode", "null");
        responseJson.put("responseId", "1");
        jobInfoJson.put("responseDescription", responseJson);
        return jobInfoJson.toString();
    }
}
