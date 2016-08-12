package org.openo.orchestrator.nfv.umc.pm.adpt.roc.service.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.openo.orchestrator.nfv.umc.pm.adpt.roc.entity.ResourceResponse;
import org.openo.orchestrator.nfv.umc.pm.adpt.roc.service.IPmResourceRestService;
import org.openo.orchestrator.nfv.umc.pm.common.RestRequestException;


@Path("/api/roc/v1/resource/hosts")
public interface IHostRestServiceResource extends IPmResourceRestService{
	
	
	@Path("/{host_id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public String getResourceList(@PathParam("host_id") String hostId) throws RestRequestException;
	
	@Path("/")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public ResourceResponse queryAllResource() throws RestRequestException;
	
}
