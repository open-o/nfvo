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

package org.openo.nfvo.vnfmdriver.common.swagger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.openo.nfvo.vnfmdriver.rest.NSLCMServiceRestAPI;
import org.openo.nfvo.vnfmdriver.rest.VNFServiceRestAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Feb 15, 2017
 */
@Api(tags = {"v1"})
@Produces(MediaType.APPLICATION_JSON)
public class SwaggerUtil {

    private static final Logger logger = LoggerFactory.getLogger(SwaggerUtil.class);

    /**
     * <br>
     *
     * @param req
     * @param resp
     * @param vnfmId
     * @param language
     * @return
     * @since NFVO 0.5
     */
    @POST
    @Path("/openoapi/ericsson-vnfm/v1/{vnfmId}/vnfs")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Instantiate VNF", response = VNFServiceRestAPI.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully."),
    @ApiResponse(code = 404, message = "VnfmDriver Resource Not Found."),
    @ApiResponse(code = 500, message = "VnfmDriver resource failed to process the request.")})
    public String instantiateVNFJson(
            @ApiParam(value = "request", required=true) @QueryParam("req") HttpServletRequest req,
            @ApiParam(value = "response", required=true) @QueryParam("resp") HttpServletResponse resp,
            @ApiParam(value = "URLParam", required=true) @PathParam("vnfmId") String vnfmId,
            @HeaderParam("language-option") String language) {
        logger.info("Instantiate VNF.");
        return "description";
    }

    /**
     * <br>
     *
     * @param req
     * @param resp
     * @param vnfmId
     * @param vnfInstanceId
     * @param language
     * @return
     * @since NFVO 0.5
     */
    @POST
    @Path("/openoapi/ericsson-vnfm/v1/{vnfmId}/vnfs/{vnfInstanceId}/terminate")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Terminate VNF", response = VNFServiceRestAPI.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully."),
	@ApiResponse(code = 404, message = "VnfmDriver Resource Not Found."),
	@ApiResponse(code = 500, message = "VnfmDriver resource failed to process the request.")})
    public String terminateVNFJson(
    		@ApiParam(value = "request", required=true) @QueryParam("req") HttpServletRequest req,
            @ApiParam(value = "response", required=true) @QueryParam("resp") HttpServletResponse resp,
            @ApiParam(value = "URLParam", required=true) @PathParam("vnfmId") String vnfmId,
            @ApiParam(value = "URLParam", required=true) @PathParam("vnfInstanceId") String vnfInstanceId,
            @HeaderParam("language-option") String language) {
        logger.info("Terminate VNF.");
        return "description";
    }

    /**
     * <br>
     *
     * @param resp
     * @param vnfmId
     * @param vnfInstanceId
     * @param language
     * @return
     * @since NFVO 0.5
     */
    @GET
    @Path("/openoapi/ericsson-vnfm/v1/{vnfmId}/vnfs/{vnfInstanceId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Query VNF", response = VNFServiceRestAPI.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully."),
	@ApiResponse(code = 404, message = "VnfmDriver Resource Not Found."),
	@ApiResponse(code = 500, message = "VnfmDriver resource failed to process the request.")})
    public String queryVNFJson(
            @ApiParam(value = "response", required=true) @QueryParam("resp") HttpServletResponse resp,
            @ApiParam(value = "URLParam", required=true) @PathParam("vnfmId") String vnfmId,
            @ApiParam(value = "URLParam", required=true) @PathParam("vnfInstanceId") String vnfInstanceId,
            @HeaderParam("language-option") String language) {
        logger.info("Query VNF.");
        return "description";
    }

    /**
     * <br>
     *
     * @param resp
     * @param vnfmId
     * @param jobid
     * @param responseId
     * @param language
     * @return
     * @since NFVO 0.5
     */
    @GET
    @Path("/openoapi/ericsson-vnfm/v1/{vnfmId}/jobs/{jobid}&responseId={responseId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get operation status", response = VNFServiceRestAPI.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully."),
	@ApiResponse(code = 404, message = "VnfmDriver Resource Not Found."),
	@ApiResponse(code = 500, message = "VnfmDriver resource failed to process the request.")})
    public String getOperationStatusJson(
            @ApiParam(value = "response", required=true) @QueryParam("resp") HttpServletResponse resp,
            @ApiParam(value = "URLParam", required=true) @PathParam("vnfmId") String vnfmId,
            @ApiParam(value = "URLParam", required=true) @PathParam("jobid") String jobid,
            @ApiParam(value = "URLParam", required=true) @PathParam("responseId") String responseId,
            @HeaderParam("language-option") String language) {
        logger.info("Get operation status.");
        return "description";
    }

    /**
     * <br>
     *
     * @param req
     * @param resp
     * @param language
     * @return
     * @since NFVO 0.5
     */
    @POST
    @Path("/openoapi/nslcm/v1/ns/grantvnf")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Grant VNF lifecycle operation", response = NSLCMServiceRestAPI.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully."),
	@ApiResponse(code = 404, message = "VnfmDriver Resource Not Found."),
	@ApiResponse(code = 500, message = "VnfmDriver resource failed to process the request.")})
    public String grantVNFJson(
            @ApiParam(value = "request", required=true) @QueryParam("req") HttpServletRequest req,
            @ApiParam(value = "response", required=true) @QueryParam("resp") HttpServletResponse resp,
            @HeaderParam("language-option") String language) {
        logger.info("Grant VNF lifecycle operation.");
        return "description";
    }
}
