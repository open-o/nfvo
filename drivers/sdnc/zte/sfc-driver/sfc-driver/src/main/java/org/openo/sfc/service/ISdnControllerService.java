/**
 * Copyright 2016 [ZTE] and others.
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

package org.openo.sfc.service;

import org.openo.sfc.entity.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/restconf/config/neutron:neutron")
public interface ISdnControllerService {

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Result querySdnController() throws Exception;

    @Path("/port-pairs")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPortPair(@HeaderParam("Authorization") String authorization, String portPairReq4S) throws Exception;

    @Path("/port-pair-groups")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPortPairGroup(@HeaderParam("Authorization") String authorization, String ppg4S) throws Exception;

    @Path("/sfc-flow-classifiers")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createFlowCla(@HeaderParam("Authorization") String authorization, String flowClassfierReq) throws Exception;


    @Path("/port-chains")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPortChain(@HeaderParam("Authorization") String authorization, String portChainReq4S) throws Exception;

    @Path("/port-pairs/port-pair/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePortPair(@HeaderParam("Authorization") String authorization, @PathParam("id") String id) throws Exception;


    @Path("/port-pair-groups/port-pair-group/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePortPairGroup(@HeaderParam("Authorization") String authorization, @PathParam("id") String uuid) throws Exception;

    @Path("/sfc-flow-classifiers/sfc-flow-classifier/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFlowClassifiers(@HeaderParam("Authorization") String authorization, @PathParam("id") String id) throws Exception;

    @Path("/port-chains/port-chain/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePortChain(@HeaderParam("Authorization") String authorization, @PathParam("id") String id) throws Exception;

}
