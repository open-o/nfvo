package org.openo.orchestrator.nfv.umc.pm.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.openo.orchestrator.nfv.umc.pm.bean.Resource;
import org.openo.orchestrator.nfv.umc.pm.common.RestRequestException;
import org.openo.orchestrator.nfv.umc.pm.wrapper.ResourceServiceWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Api of resource data querying. Provide resource data querying operations.
 */
@Path("/umcpm/v1")
@Api(tags = { "eco-pm" })
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ResourceResource {
	private static final Logger logger = LoggerFactory.getLogger(ResourceResource.class);

	/**
	 *  Query resource data by resource type id.
	 * @param resourceTypeId resource type id
	 * @return resource information
	 */
	@GET
	@Path("/resources")
	@ApiOperation(value = "query Resource", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 500, message = "Internal Server Error.") })
	@Timed
	public Response queryResources(
			@ApiParam(value = "Resource Type Id") @QueryParam("resourcetypeid") String resourceTypeId) {
		logger.info("query Resources");
        try {
            List<Resource> beans = ResourceServiceWrapper.queryResources(resourceTypeId);
            return Response.ok(beans).status(Response.Status.OK).build();
        } catch (RestRequestException e) {
            logger.error(e.getMessage(), e);
            throw new InternalServerErrorException(e.getMessage(), e);
        }

	}
}
