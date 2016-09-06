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
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.roc;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/resource")
public interface IRocServiceRest {
    @GET
    @Path("/instances")
    @Produces("text/plain")
    public String retrieveAllInstances(@QueryParam("types") String types,@QueryParam("instanceId") String instanceId);

    @POST
    @Path("/vnfs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String createVnf(String vnfJsonInfo);

    @GET
    @Path("/vnfs/{vnf_id}")
    @Produces("text/plain")
    public String retrieveVnfs(@PathParam("vnf_id") String vnfId);

    @GET
    @Path("/vnfs")
    @Produces("text/plain")
    public String retrieveVnfs(@QueryParam("vnfmId") String vnfmId,
            @QueryParam("nsId") String nsId,
            @QueryParam("conductorId") String conductorId);

    @PUT
    @Path("/vnfs/{vnf_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String updateVnf(@PathParam("vnf_id") String vnfId,
            String vnfJsonInfo);

    @DELETE
    @Path("/vnfs/{vnf_id}")
    @Produces("text/plain")
    public String deleteVnf(@PathParam("vnf_id") String vnfId);

    @POST
    @Path("/vnfcs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String createVnfc(String vnfcJsonInfo);

    @GET
    @Path("/vnfcs/{vnfc_id}")
    @Produces({ "text/plain", "application/json" })
    public String retrieveVnfcs(@PathParam("vnfc_id") String vnfcId);

    @GET
    @Path("/vnfcs")
    @Produces("text/plain")
    public String retrieveVnfcs(@QueryParam("vnfId") String vnfId,
            @QueryParam("vduId") String vduId);

    @PUT
    @Path("/vnfcs/{vnfc_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String updateVnfc(@PathParam("vnfc_id") String vduId,String vduJsonInfo);

    @DELETE
    @Path("/vnfcs")
    @Produces("text/plain")
    public String deleteVnfcs(@QueryParam("vnfId") String vnfId,
            @QueryParam("vduId") String vduId);

    @DELETE
    @Path("/vnfcs/{vnfc_id}")
    @Produces("text/plain")
    public String deleteVnfc(@PathParam("vnfc_id") String vnfcId);

    @POST
    @Path("/vdus")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String createVdu(String vduJsonInfo);

    @GET
    @Path("/vdus/{vdu_id}")
    @Produces("text/plain")
    public String retrieveVdus(@PathParam("vdu_id") String vduId);

    @GET
    @Path("/vdus")
    @Produces("text/plain")
    public String retrieveVdus(@QueryParam("vimId") String vimId,
            @QueryParam("vnfId") String vnfId,
            @QueryParam("hostId") String hostId);

    @PUT
    @Path("/vdus/{vdu_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String updateVdu(@PathParam("vdu_id") String vduId,
            String vduJsonInfo);

    @DELETE
    @Path("/vdus")
    @Produces("text/plain")
    public String deleteVdus(@QueryParam("vimId") String vimId,
            @QueryParam("vnfId") String vnfId,
            @QueryParam("hostId") String hostId);

    @DELETE
    @Path("/vdus/{vdu_id}")
    @Produces("text/plain")
    public String deleteVdu(@PathParam("vdu_id") String vduId);

    @GET
    @Path("/vims/{vim_id}")
    @Produces("text/plain")
    public String queryVims(@PathParam("vim_id") String vimId);

    @GET
    @Path("/vnfms/{vnfm_id}")
    @Produces("text/plain")
    public String retrieveVnfms(@PathParam("vnfm_id") String vnfmId);

    @POST
    @Path("/nsrs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String createNs(String nsJson);

    @GET
    @Path("/nsrs/{ns_id}")
    @Produces("text/plain")
    public String retrieveNss(@PathParam("ns_id") String nsId);

    @PUT
    @Path("/nsrs/{ns_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String updateNs(@PathParam("ns_id") String nsId, String nsJson);

    @DELETE
    @Path("/nsrs/{ns_id}")
    @Produces("text/plain")
    public String deleteNs(@PathParam("ns_id") String nsId);

    @POST
    @Path("/networks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String createNetwork(String networkJson);

    @GET
    @Path("/networks/{network_oid}")
    @Produces("text/plain")
    public String retrieveNetworks(@PathParam("network_oid") String networkOid);

    @GET
    @Path("/networks")
    @Produces("text/plain")
    public String retrieveNetworks(@QueryParam("vimId") String vimId,
            @QueryParam("networkId") String networkId,
            @QueryParam("instanceId") String instanceId);

    @PUT
    @Path("/networks/{network_oid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String updateNetwork(@PathParam("network_oid") String networkOid,
            String networkJson);

    @DELETE
    @Path("/networks/{network_oid}")
    @Produces("text/plain")
    public String deleteNetwork(@PathParam("network_oid") String networkOid);

    @DELETE
    @Path("/networks")
    @Produces("text/plain")
    public String deleteNetworks(@QueryParam("vimId") String vimId);

    @POST
    @Path("/subnets")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String createSubnet(String subnetJson);

    @GET
    @Path("/subnets/{subnet_oid}")
    @Produces("text/plain")
    public String retrieveSubnets(@PathParam("subnet_oid") String subnetOid);

    @GET
    @Path("/subnets")
    @Produces("text/plain")
    public String retrieveSubnets(@QueryParam("vimId") String vimId,
            @QueryParam("networkId") String networkId,
            @QueryParam("subnetId") String subnetId);

    @PUT
    @Path("/subnets/{subnet_oid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String updateSubnet(@PathParam("subnet_oid") String subnetOid,
            String subnetJson);

    @DELETE
    @Path("/subnets/{subnet_oid}")
    @Produces("text/plain")
    public String deleteSubnet(@PathParam("subnet_oid") String subnetOid);

    @DELETE
    @Path("/subnets")
    @Produces({ "text/plain", "application/json" })
    public String deleteSubnets(@QueryParam("vimId") String vimId,
            @QueryParam("networkId") String networkId);

    @GET
    @Path("/hosts")
    @Produces({ "text/plain", "application/json" })
    public String retrieveHosts(@QueryParam("vimId") String vimId,
            @QueryParam("name") String name);

    @POST
    @Path("/cps")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String createConnectionPoint(String cpJson);

    @GET
    @Path("/cps/{cp_id}")
    @Produces("text/plain")
    public String retrieveConnectionPoints(@PathParam("cp_id") String cpOid);

    @GET
    @Path("/cps")
    @Produces("text/plain")
    public String retrieveConnectionPointsByInstanceId(@QueryParam("instanceId") String instanceId);

    @PUT
    @Path("/cps/{cp_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String updateConnectionPoint(@PathParam("cp_id") String cpOid,String cpJson);

    @DELETE
    @Path("/cps/{cp_id}")
    @Produces({ "text/plain", "application/json" })
    public String deleteConnectionPoint(@PathParam("cp_id") String cpOid);

    @DELETE
    @Path("/cps")
    @Produces({ "text/plain", "application/json" })
    public String deleteConnectionPointsByInstanceId(@QueryParam("instanceId") String instanceId);
}
