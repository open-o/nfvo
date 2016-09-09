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

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.openo.nfvo.monitor.umc.pm.bean.PmMeaTaskBean;
import org.openo.nfvo.monitor.umc.pm.wrapper.PmMeaTaskServiceWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Apis of performance measure task. Provide performance measurement tasks CRUD operations.
 */
@Path("/umcpm/v1")
@Api(tags = { "meatasks" })
@Produces(MediaType.APPLICATION_JSON)
public class MeasureTaskResource {
	private static final Logger logger = LoggerFactory.getLogger(MeasureTaskResource.class);
	
	/**
	 * Api of query performance measure task by id.
	 * @param meataskid measure task id
	 * @return performance measure task detail information.
	 */
	@GET
	@Path("/meatasks/{meataskid}")
	@ApiOperation(value = "get a meaTask by meataskid", response = PmMeaTaskBean.class)
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal Server Error.") })
	@Produces(MediaType.APPLICATION_JSON)
	@Timed
	public PmMeaTaskBean getMeaTaskById(
			@ApiParam(value = "measure task id", required = true) @PathParam("meataskid") String meataskid, @HeaderParam("language-option") String language) {
		logger.info("get a meaTask by meataskid");
		return PmMeaTaskServiceWrapper.getMeaTaskById(meataskid, language);
	}
	
	/**
	 * Api of query performance measure task by resource type id and measure type id.
	 * @param resourceTypeId resource type id
	 * @param moTypeId measure type id
	 * @return performance measure tasks
	 */
	@GET
	@Path("/meatasks")
	@ApiOperation(value = "query measure task by resource type id or motype id", response = PmMeaTaskBean.class, responseContainer = "List")
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal Server Error.") })
	@Produces(MediaType.APPLICATION_JSON)
	@Timed
	public PmMeaTaskBean[] queryMeaTasks(
			@ApiParam(value = "resource type id") @QueryParam("resourceTypeId") String resourceTypeId,
			@ApiParam(value = "mo Type Id") @QueryParam("moTypeId") String moTypeId, @HeaderParam("language-option") String language) {
		logger.info("query measure tasks. resourceTypeId = " + resourceTypeId + " ,moTypeId = " + moTypeId);
		return PmMeaTaskServiceWrapper.getAllPmMeaTasks(resourceTypeId, moTypeId, language);
	}
}
