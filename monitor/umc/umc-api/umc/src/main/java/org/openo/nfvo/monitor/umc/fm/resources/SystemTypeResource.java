/**
 * Copyright 2016, CMCC Technologies Co., Ltd.
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
package org.openo.nfvo.monitor.umc.fm.resources;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.nfvo.monitor.umc.fm.common.FMConsts;
import org.openo.nfvo.monitor.umc.fm.db.entity.SystemType;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.SystemTypeQueryRequest;
import org.openo.nfvo.monitor.umc.fm.util.JacksonJsonUtil;
import org.openo.nfvo.monitor.umc.fm.wrapper.SystemTypeServiceWrapper;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * API for FM SystemType
 *
 */
@Path("/umcfm/v1")
@Api(tags = {"FM SystemType Interface"})
@Produces(MediaType.APPLICATION_JSON)
public class SystemTypeResource {


    @GET
    @ApiOperation(value = "get SystemType Info by systemType ", response = SystemType.class, responseContainer = "List")
    @Path("/systemtype")
    @Timed
    public SystemType[] listSystemType(@Context HttpServletRequest request, @HeaderParam("language-option") String language) throws Exception {
        SystemTypeQueryRequest req =
                (SystemTypeQueryRequest) JacksonJsonUtil.jsonToBean(
                        request.getParameter(FMConsts.REQUEST_PARAM_NAME),
                        SystemTypeQueryRequest.class, true);
        return new SystemTypeServiceWrapper().getSystemType(req, language);
    }

}
