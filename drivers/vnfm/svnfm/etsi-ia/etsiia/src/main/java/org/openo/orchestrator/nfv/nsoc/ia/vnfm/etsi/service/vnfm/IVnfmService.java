/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 * */
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.vnfm;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.OperateVnfRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.ScaleVnfRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.TerminalVnfRequest;

@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IVnfmService {
    @POST
    @Path("/vnfinstances")
    public String instantiateVnf(String request);

    @GET
    @Path("/vnfinstances/{vnfinstanceid}/progress")
    public String queryVnfInstanceProcess(
            @PathParam("vnfinstanceid") String vnfInstanceId);

    @GET
    @Path("/vnfinstances/{vnfinstanceid}/notifications")
    public String queryVnfInstanceProcessDetail(
            @PathParam("vnfinstanceid") String vnfInstanceId);

    @POST
    @Path("/vnfinstances/{vnfinstanceid}/operation")
    public void operateVnf(@PathParam("vnfinstanceid") String vnfInstanceId,
            String request);

    @POST
    @Path("/vnfinstances/{vnfinstanceid}/action_scale")
    public void scaleVnf(@PathParam("vnfinstanceid") String vnfInstanceId,
            String request);

    @POST
    @Path("/vnfinstances/{vnfinstanceid}/action_terminate")
    public void terminalVnf(@PathParam("vnfinstanceid") String vnfInstanceId,
            String request);

    @GET
    @Path("/vnfinstances/{vnfinstanceid}")
    public String QueryVnf(@PathParam("vnfinstanceid") String vnfInstanceId);


    @GET
    @Path("/vnfinstances")
    public String QueryVnfs();

}
