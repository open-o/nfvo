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
package org.openo.nfvo.monitor.umc.drill.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.openo.nfvo.monitor.umc.drill.resources.bean.response.TopologyResp;
import org.openo.nfvo.monitor.umc.drill.wrapper.common.TopologyConsts;
import org.openo.nfvo.monitor.umc.drill.wrapper.handler.HandlerFactory;

import com.codahale.metrics.annotation.Timed;

/**
 *       The resource represent the layer overview of the ns, vdu and host
 */
@Path("/umcdrill/v1/layer")
@Api(tags = {"Resource Layer Overview"})
public class LayerMonitorResource {
    // get the factory instance creating all the handlers
    HandlerFactory handlerFactory = HandlerFactory.getInstance();

    /**
     * show all the ns layer nodes info
     */
    @GET
    @Path("/ns")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @ApiOperation(value = "view NS Layer nodes", response = TopologyResp.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "view NS Layer nodes error ")})
    public TopologyResp monitorNS() {
        return handlerFactory.getHandler(TopologyConsts.HANDlER_LAYERMONITOR_NS).handleTopologyReq(
                "");
    }

    /**
     * show all the vdu layer nodes info
     */
    @GET
    @Path("/vdu")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @ApiOperation(value = "view VDU Layer nodes", response = TopologyResp.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "view VDU Layer nodes error ")})
    public TopologyResp monitorVDU() {
        return handlerFactory.getHandler(TopologyConsts.HANDlER_LAYERMONITOR_VDU)
                .handleTopologyReq("");
    }

    /**
     * show all the host layer nodes info
     */
    @GET
    @Path("/host")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @ApiOperation(value = "view Host Layer nodes", response = TopologyResp.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "view Host Layer nodes error ")})
    public TopologyResp monitorHost() {
        return handlerFactory.getHandler(TopologyConsts.HANDlER_LAYERMONITOR_HOST)
                .handleTopologyReq("");
    }
}
