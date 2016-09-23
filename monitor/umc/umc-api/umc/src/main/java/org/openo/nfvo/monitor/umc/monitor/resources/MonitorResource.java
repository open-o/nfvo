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
package org.openo.nfvo.monitor.umc.monitor.resources;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.openo.nfvo.monitor.umc.db.entity.DACInfo;
import org.openo.nfvo.monitor.umc.db.entity.MonitorInfo;
import org.openo.nfvo.monitor.umc.monitor.bean.MonitorParamInfo;
import org.openo.nfvo.monitor.umc.monitor.bean.MonitorResult;
import org.openo.nfvo.monitor.umc.monitor.bean.MonitorTaskInfo;
import org.openo.nfvo.monitor.umc.monitor.wrapper.DACServiceWrapper;
import org.openo.nfvo.monitor.umc.monitor.wrapper.MonitorServiceWrapper;

import com.codahale.metrics.annotation.Timed;

@Path("/umcmonitor/v1")
@Api(tags = {"umc Monitor"})
@Produces(MediaType.APPLICATION_JSON)
public class MonitorResource {

    @GET
    @Path("/dacs")
    @ApiOperation(value = "get all DACInfo ", response = DACInfo.class, responseContainer = "List")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public List<DACInfo> getDACInfos() {
        return DACServiceWrapper.getInstance().getDACInfoInstances();
    }

    @POST
    @Path("/dacs")
    @ApiOperation(value = "add one DACInfo ", response = DACInfo.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "add DACInfo error ")})
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public MonitorResult addDACInfo(
            @ApiParam(value = "DACInfo Instance Info", required = true) DACInfo dacInfo) {
        return DACServiceWrapper.getInstance().saveDACInfoInstance(dacInfo);

    }

    @GET
    @Path("/dacs/{dacId}")
    @ApiOperation(value = "get one DACInfo by DACId", response = DACInfo.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "get DACInfo error ")})
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public DACInfo getDACInfo(
            @ApiParam(value = "DACInfo dacId", required = true) @PathParam("dacId") String dacId) {

        return DACServiceWrapper.getInstance().getDACInfoInstance(dacId);

    }

    @PUT
    @Path("/dacs")
    @ApiOperation(value = "update one DACInfo", response = MonitorResult.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "update DACInfo error ")})
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public MonitorResult updateDACInfo(
            @ApiParam(value = "DACInfo Instance Info", required = true) DACInfo dacInfo) {

        return DACServiceWrapper.getInstance().updateDACInfoInstance(dacInfo);

    }

    @DELETE
    @Path("/dacs/{dacId}")
    @ApiOperation(value = "delete one DACInfo by dacId", response = MonitorResult.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "delete dacInfo error ")})
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public MonitorResult deleteDACInfo(
            @ApiParam(value = "DACInfo dacId", required = true) @PathParam("dacId") String dacId) {

        return DACServiceWrapper.getInstance().deleteDACInfoInstance(dacId);

    }

    @PUT
    @Path("/monitortask/{ifproxyipchange}")
    @ApiOperation(value = "update Monitor Task", response = MonitorResult.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "update Monitor Task error ")})
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public MonitorResult updateMonitorTask(
            @ApiParam(value = "Monitor Task Info", required = true) MonitorTaskInfo monitorTaskInfo,
            @ApiParam(value = "If proxyIP is Change", required = true) @PathParam("ifproxyipchange") int ifProxyIPChange) {

        return MonitorServiceWrapper.getInstance().updateMonitorTask(monitorTaskInfo, ifProxyIPChange);

    }

    @POST
    @Path("/monitorinfo")
    @ApiOperation(value = "add Device Monitor Param Information", response = MonitorResult.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "add Device Monitor Param Information error")})
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public MonitorResult addMonitorInfo(
            @ApiParam(value = "Monitor Param Info", required = true) MonitorParamInfo monitorParamInfo) {

     return MonitorServiceWrapper.getInstance().addMonitorInfo(monitorParamInfo);
    }

    @POST
    @Path("/monitorinfo/{oid}")
    @ApiOperation(value = "update Device Monitor Param Information", response = MonitorResult.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "update Device Monitor Param Information error ")})
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public MonitorResult updateMonitorInfo(
            @ApiParam(value = "Monitor device oid", required = true) @PathParam("oid") String oid,
            @ApiParam(value = "Monitor Param Info", required = true) MonitorParamInfo monitorParamInfo) {

        return MonitorServiceWrapper.getInstance().updateMonitorInfo(oid, monitorParamInfo);

    }

    @DELETE
    @Path("/monitorinfo/{oid}")
    @ApiOperation(value = "delete Device Monitor Param Information", response = MonitorResult.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "delete Device Monitor Param Information error ")})
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public MonitorResult deleteMonitorInfo(
            @ApiParam(value = "Monitor device oid", required = true) @PathParam("oid") String oid) {

        return MonitorServiceWrapper.getInstance().deleteMonitorInfo(oid);

    }
    
    @GET
    @Path("/monitorinfos")
    @ApiOperation(value = "get all MonitorInfos ", response = MonitorInfo.class, responseContainer = "List")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public List<MonitorInfo> getMonitorInfos() {
        return DACServiceWrapper.getInstance().getMonitorInfos();
    }

    @GET
    @Path("/monitorinfo/{oid}")
    @ApiOperation(value = "get MonitorInfo by Oid", response = MonitorInfo.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "get MonitorInfo error ")})
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public MonitorInfo getMonitorInfosByOid(
            @ApiParam(value = "MonitorInfo Oid", required = true) @PathParam("oid") String oid) {

        return DACServiceWrapper.getInstance().getMonitorInfoByOid(oid);
    }
    
    @GET
    @Path("/monitorinfos/{neTypeId}")
    @ApiOperation(value = "get MonitorInfos by NeTypeId", response = MonitorInfo.class)
    @ApiResponses(value = {@ApiResponse(code = 500, message = "get MonitorInfos error ")})
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public List<MonitorInfo> getMonitorInfosByNeTypeId(
            @ApiParam(value = "MonitorInfo NeTypeId", required = true) @PathParam("neTypeId") String neTypeId) {

        return DACServiceWrapper.getInstance().getMonitorInfoByNeTypeId(neTypeId);
    }
    
}
