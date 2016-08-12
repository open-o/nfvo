/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.umc.sm.resources;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.orchestrator.nfv.umc.sm.bean.CheckRightRequest;
import org.openo.orchestrator.nfv.umc.sm.bean.CheckRightResponse;
import org.openo.orchestrator.nfv.umc.sm.bean.FrameCommInfo;
import org.openo.orchestrator.nfv.umc.sm.bean.LoginInfo;
import org.openo.orchestrator.nfv.umc.sm.bean.LoginResult;
import org.openo.orchestrator.nfv.umc.sm.bean.VersionInfo;
import org.openo.orchestrator.nfv.umc.sm.wrapper.SmcServiceWrapper;
import org.openo.orchestrator.nfv.umc.sm.wrapper.UiFrameServiceWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/umcsm/v1")
@Api(tags = {"umc sm"})
@Produces(MediaType.APPLICATION_JSON)
public class UiframeResource {

    private static final String SSOLOGOUT = "SSOLogout";
    private static final String DEFAULT_URL = "/iui/framework/login.html";
    private static final Logger LOGGER = LoggerFactory.getLogger(UiframeResource.class);

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "login ", response = LoginResult.class)
    @Timed
    public LoginResult doLogin(@ApiParam(value = "login info", required = true) LoginInfo logininfo,
            @Context HttpServletRequest request, @Context HttpServletResponse servletResponse) {
        String requestIpAddr = request.getRemoteAddr();
        LoginResult result = SmcServiceWrapper.getInstance().doLogin(logininfo, requestIpAddr);

        if (result.getResult() == LoginResult.SUCCESS) {
            servletResponse.addHeader("OpenoAuth", "true");
        } else {
            servletResponse.addHeader("OpenoAuth", "false");
        }

        return result;

    }

    @GET
    @Path("/loginOut")
    @ApiOperation(value = "logout ")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public void doLogout(
            @ApiParam(value = "loginOut", required = true, allowableValues = SSOLOGOUT) @QueryParam("SSOAction") String SSOAction,
            @Context HttpServletRequest request) {
        if (SSOAction != null & SSOAction.equals(SSOLOGOUT)) {

            SmcServiceWrapper.getInstance().doLogout(request.getRemoteAddr());
            try {
                URI defaultURI = new URI(DEFAULT_URL);
                throw new RedirectionException(302, defaultURI);
            } catch (URISyntaxException e) {
                LOGGER.info("", e);
            }

        }
        throw new NotFoundException("can not found SSOAction :" + SSOAction);

    }

    @GET
    @Path("/heartbeat")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "heartbeat check ", response = String.class)
    @Timed
    public String heatbeat(
            @ApiParam(value = "username", required = true) @QueryParam("username") String username,
            @Context HttpServletRequest request) {
        return String.valueOf(SmcServiceWrapper.getInstance()
                .checkLoginUserName(request.getRemoteAddr(), username));

    }

    @GET
    @Path("/userName")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "get login username ", response = String.class)
    @Timed
    public String getLoginUserName(@Context HttpServletRequest request) {

        return SmcServiceWrapper.getInstance().getLoginUserName(request.getRemoteAddr());

    }

    @GET
    @Path("/frameCommInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get ui frame config info ", response = FrameCommInfo.class)
    @Timed
    public FrameCommInfo getframecomminfo() {
        return UiFrameServiceWrapper.getInstance().getFrameconfig();

    }

    @GET
    @Path("/versionInfo")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get portal version info ", response = VersionInfo.class)
    @Timed
    public VersionInfo getVersionInfo() {
        return UiFrameServiceWrapper.getInstance().getVersioninfo();

    }

    @GET
    @Path("/checkRight")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "checkRight ", response = CheckRightResponse.class)
    @Timed
    public CheckRightResponse checkRight(
            @ApiParam(value = "check operation right", required = true) @QueryParam("data") String checkpara) {

        LOGGER.info("receive checkRight parameter: " + checkpara);

        try {
            ObjectMapper mapper = new ObjectMapper();
            CheckRightRequest checkreq = mapper.readValue(checkpara, CheckRightRequest.class);
            // CheckRightRequest checkreq = new CheckRightRequest(new
            // String[]{"a","b"});
            // String json = mapper.writeValueAsString(checkreq);
            // LOGGER.info("test convert checkRight parameter: " + json);

            boolean[] checkresult =
                    SmcServiceWrapper.getInstance().checkRight(checkreq.getOperations());
            return new CheckRightResponse(checkresult);
        } catch (IOException e1) {
            LOGGER.warn("", e1);
        }

        return new CheckRightResponse(new boolean[0]);

    }
}
