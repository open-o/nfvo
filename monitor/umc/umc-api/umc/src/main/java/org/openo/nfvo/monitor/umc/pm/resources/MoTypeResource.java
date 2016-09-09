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

import org.openo.nfvo.monitor.umc.pm.bean.GranularityBean;
import org.openo.nfvo.monitor.umc.pm.bean.PmCounter;
import org.openo.nfvo.monitor.umc.pm.wrapper.CounterServiceWrapper;
import org.openo.nfvo.monitor.umc.pm.wrapper.GranularityServiceWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Apis of performance models. Provide performance models querying operations.
 */
@Path("/umcpm/v1/motypes")
@Api(tags = { "motypes" })
@Produces(MediaType.APPLICATION_JSON)
public class MoTypeResource {
	private static final Logger logger = LoggerFactory.getLogger(MoTypeResource.class);
	
	/**
	 * Api of query counters by resource type id and measure type id.
	 * @param resourceTypeId resource type id
	 * @param moTypeId measure type id
	 * @param motypeid measure type id
	 * @return performance counters
	 */
	@GET
	@Path("/{motypeid}/counters")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "query counters by resourceTypeId and moTypeId", response = PmCounter.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal Server Error.") })
	@Timed
	public PmCounter[] queryCounters(
			@ApiParam(value = "resource type id") @QueryParam("resourceTypeId") String resourceTypeId,
			@ApiParam(value = "mo type id") @QueryParam("moTypeId") String moTypeId,
			@ApiParam(value = "mo type id") @PathParam("motypeid") String motypeid,
			@HeaderParam("language-option") String language) {
		logger.info("query counters by resourceTypeId and moTypeId.");
		return CounterServiceWrapper.queryCounters(resourceTypeId, moTypeId, language);
	}
	
	/**
	 * Api of query performance granularity by resource type id and measure type id.
	 * @param resourceTypeId resource type id
	 * @param moTypeId measure type id
	 * @param motypeid measure type id
	 * @return performance granularity
	 */
	@GET
	@Path("/{motypeid}/granularities")
	@ApiOperation(value = "query granularitys by resource type id and motype id", response = GranularityBean.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 500, message = "Internal Server Error.") })
	@Produces(MediaType.APPLICATION_JSON)
	@Timed
	public GranularityBean[] queryGranularitys(
			@ApiParam(value = "resource Type Id") @QueryParam("resourceTypeId") String resourceTypeId,
			@ApiParam(value = "mo Type Id") @QueryParam("moTypeId") String moTypeId,
			@ApiParam(value = "mo type id") @PathParam("motypeid") String motypeid,
			@HeaderParam("language-option") String language) {
		logger.info("query granularitys by resourceTypeId and moTypeId.");
		return GranularityServiceWrapper.getGranularitys(resourceTypeId, moTypeId, language);
	}
}
