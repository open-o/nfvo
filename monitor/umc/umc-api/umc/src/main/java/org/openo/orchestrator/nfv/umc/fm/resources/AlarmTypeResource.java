/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.umc.fm.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.orchestrator.nfv.umc.fm.common.FMConsts;
import org.openo.orchestrator.nfv.umc.fm.db.entity.AlarmType;
import org.openo.orchestrator.nfv.umc.fm.resource.bean.request.AlarmTypeQueryRequest;
import org.openo.orchestrator.nfv.umc.fm.util.JacksonJsonUtil;
import org.openo.orchestrator.nfv.umc.fm.wrapper.AlarmTypeServiceWrapper;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@Path("/umcfm/v1")
@Api(tags = {"FM AlarmType Interface"})
@Produces(MediaType.APPLICATION_JSON)
public class AlarmTypeResource {

    @GET
    @ApiOperation(value = "get AlarmType Info by typeid ", response = AlarmType.class)
    @Path("/alarmtype")
    @Timed
    public AlarmType listAlarmType(
            @ApiParam(value = "typeid", required = true) @QueryParam("typeid") int typeid, @HeaderParam("language-option") String language)
            throws Exception {
        return new AlarmTypeServiceWrapper().getAlarmType(typeid, language);
    }

    @GET
    @ApiOperation(value = "get AlarmType Info by typeid ", response = AlarmType.class, responseContainer = "List")
    @Path("/alarmtypes")
    @Timed
    public AlarmType[] listAlarmTypes(@Context HttpServletRequest request, @HeaderParam("language-option") String language) throws Exception {
        AlarmTypeQueryRequest req =
                (AlarmTypeQueryRequest) JacksonJsonUtil.jsonToBean(
                        request.getParameter(FMConsts.REQUEST_PARAM_NAME), AlarmTypeQueryRequest.class, true);
        return new AlarmTypeServiceWrapper().getAlarmTypes(req, language);
    }


}
