/*
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
package org.openo.nfvo.monitor.umc.pm.resources;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.openo.nfvo.monitor.umc.pm.bean.HistoryPmDataResponse;
import org.openo.nfvo.monitor.umc.pm.bean.PmQueryConditionBean;
import org.openo.nfvo.monitor.umc.pm.wrapper.HistoryPmDataServiceWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Api of history performance data querying. Query history performance data according query
 * condition.
 */
//@Path("/umcpm/v1")
@Path("/pm")
@Api(tags = {"historydataqueries"})
@Produces(MediaType.APPLICATION_JSON)
public class HistoryPmDataResource {
    private static final Logger logger = LoggerFactory.getLogger(HistoryPmDataResource.class);

    @POST
    @Path("/historydataqueries")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "query history performance data.", response = HistoryPmDataResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "Internal Server Error.")})
    @Timed
    public HistoryPmDataResponse queryPmData(
            @ApiParam(value = "query condition.", required = true) PmQueryConditionBean queryCond, @HeaderParam("language-option") String language) {
        logger.info("query history performance data");
        return HistoryPmDataServiceWrapper.queryPmData(queryCond, language);
    }
}
