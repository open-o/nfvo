package org.openo.orchestrator.nfv.umc.pm.adpt.roc.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.openo.orchestrator.nfv.umc.pm.adpt.roc.entity.ResourceTypeResponse;


@Path("/api/roc/v1/resource/definitions")
public interface IModelRestService {

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResourceTypeResponse[] queryResourceType(@QueryParam("id") String id) throws Exception;

}
