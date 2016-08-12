/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.umc.drill.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.TopologyResp;
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.TopologyConsts;
import org.openo.orchestrator.nfv.umc.drill.wrapper.handler.HandlerFactory;

import com.codahale.metrics.annotation.Timed;

/**
 * @author 10188044
 * @date 2015-8-15
 *       <p>
 *       The resource represent the drill topology info
 */
@Path("/umcdrill/v1/topology")
@Api(tags = {"Resource Drill Topology"})
public class TopologyManagerResource {
    // get the factory instance creating all the handlers
    HandlerFactory handlerFactory = HandlerFactory.getInstance();

    /**
     * get the root-ns-vnf topology using the id of NS node
     *
     * @param nsId the id of NS node
     * @return the root-ns-vnf topology info
     */
    @GET
    @Path("/ns/{ns_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @ApiOperation(value = "drill NS node", response = TopologyResp.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "drill NS node error ")})
    public TopologyResp drillNS(
            @ApiParam(value = "the id of NS", required = true) @PathParam("ns_id") String nsId) {
        return handlerFactory.getHandler(TopologyConsts.HANDlER_DRILL_NS).handleTopologyReq(nsId);
    }


    /**
     * get the ns-vnf-vnfc topology using the id of VNF node
     *
     * @param vnfId the id of VNF node
     * @return the ns-vnf-vnfc topology info
     */
    @GET
    @Path("/vnf/{vnf_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @ApiOperation(value = "drill VNF node", response = TopologyResp.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "drill NS node error ")})
    public TopologyResp drillVNF(
            @ApiParam(value = "the id of VNF", required = true) @PathParam("vnf_id") String vnfId) {
        return handlerFactory.getHandler(TopologyConsts.HANDlER_DRILL_VNF).handleTopologyReq(vnfId);
    }

    /**
     * get the vnf-vnfc-vdu topology using the id of VNFC node
     *
     * @param vnfcId the id of VNFC node
     * @return the vnf-vnfc-vdu topology info
     */
    @GET
    @Path("vnfc/{vnfc_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @ApiOperation(value = "drill VNFC node", response = TopologyResp.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "drill VNFC node error ")})
    public TopologyResp drillVNFC(
            @ApiParam(value = "the id of VNFC", required = true) @PathParam("vnfc_id") String vnfcId) {
        return handlerFactory.getHandler(TopologyConsts.HANDlER_DRILL_VNFC).handleTopologyReq(
                vnfcId);
    }

    /**
     * get the vnfc-vdu-host topology using the id of VDU node
     *
     * @param vduId the id of VDU node
     * @return the vnfc-vdu-host topology info
     */
    @GET
    @Path("/vdu/{vdu_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @ApiOperation(value = "drill VDU node", response = TopologyResp.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "drill VDU node error ")})
    public TopologyResp drillVDU(
            @ApiParam(value = "the id of VDU", required = true) @PathParam("vdu_id") String vduId) {
        return handlerFactory.getHandler(TopologyConsts.HANDlER_DRILL_VDU).handleTopologyReq(vduId);
    }

    /**
     * get the vdu-host topology using the id of HOST node
     *
     * @param hostId the id of HOST node
     * @return the vdu-host topology info
     */
    @GET
    @Path("host/{host_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @ApiOperation(value = "drill Host node", response = TopologyResp.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "drill Host node error ")})
    public TopologyResp drillHost(
            @ApiParam(value = "the id of HOST", required = true) @PathParam("host_id") String hostId) {
        return handlerFactory.getHandler(TopologyConsts.HANDlER_DRILL_HOST).handleTopologyReq(
                hostId);
    }
}
