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

package com.zte.ums.sfc.console.service;

import com.zte.ums.sfc.console.entity.FlowClassfierReq4S;
import com.zte.ums.sfc.console.entity.PortChainReq4S;
import com.zte.ums.sfc.console.entity.PortPairGroupReq4S;
import com.zte.ums.sfc.console.entity.Result;
import com.zte.ums.sfc.console.entity.portpair.PortPairReq4S;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/restconf/config/neutron:neutron/")
public interface ISdnControllerService {

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Result querySdnController() throws Exception;

    @Path("/port-pairs")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String createPortPair(PortPairReq4S portPairReq4S) throws Exception;

    @Path("/port-pair-groups")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String createPortPairGroup(PortPairGroupReq4S ppg4S) throws Exception;

    @Path("sfc-flow-classifiers")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String createFlowCla(FlowClassfierReq4S flowClassfierReq4S) throws Exception;


    @Path("/port-chains")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String createPortChain(PortChainReq4S portChainReq4S) throws Exception;

    @Path("/port-pairs/port-pair/${id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePortPair(String uuid) throws Exception;


    @Path("/port-pair-groups/port-pair-group/${id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePortPairGroup(String uuid) throws Exception;

    @Path("sfc-flow-classifiers/sfc-flow-classifier/${id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteFlowClassifiers(String uuid) throws Exception;

    @Path("/port-chains/port-chain/${id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePortChain(String uuid) throws Exception;

}
