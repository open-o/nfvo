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
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.resources;

import java.net.URI;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.jetty.http.HttpStatus;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.common.ConversionTool;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.InstantiateRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.OperateRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.ScaleRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.TerminalRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.changedresource.ChangedResource;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.changedresource.ResourceResponse;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.process.CurrentStepInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.process.OperationExecutionProgress;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.process.ScheduleResponse;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.InstantiateVnfResponse;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.ProcessContent;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.ProcessEntity;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.VnfInfoFromVnfm;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.VnfInstanceProcessDetailResponse;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.VnfInstanceProcessResponse;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.wrapper.VnfmAdapterServiceWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(tags = {"etsi ia Resource"})
public class VnfmAdapterResource {
    private static final Logger logger = LoggerFactory.getLogger(VnfmAdapterResource.class);

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/vnfinstances")
    @ApiOperation(value = "query all vnfs on the vnfm which is specified by vnfmid", response = VnfInfoFromVnfm.class, responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "the vnfm id is wrong"),
                           @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = "the vnfm url is invalid")})
    public List<VnfInfoFromVnfm> queryVnfs(
            @ApiParam(value = "vnfm id") @QueryParam(value = "vnfmid") String vnfmId) {

        return VnfmAdapterServiceWrapper.getInstance().queryVnfs(vnfmId);
    }

    @GET
    @Path("/vnfinstances/{vnfinstanceid}")
    @ApiOperation(value = "query the specified vnf info", response = VnfInfoFromVnfm.class)
    @ApiResponses(value = {@ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "the vnf instance id is wrong"),
                           @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = "the vnfm url is invalid")})
    public VnfInfoFromVnfm queryVnf(
            @ApiParam(value = "vnf instance id") @PathParam("vnfinstanceid") String vnfInstanceId) {

        return VnfmAdapterServiceWrapper.getInstance().queryVnf(vnfInstanceId);
    }


    @GET
    @Path("/vnfinstances/{vnfinstanceid}/changedresource")
    @ApiOperation(value = "special,query vnf resource,changed resource", response = ResourceResponse.class)
    @ApiResponses(value = {@ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "the vnf instance id is wrong"),
                           @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = "the vnfm url is invalid")})
    public ResourceResponse queryResource(
            @ApiParam(value = "vnf instance id") @PathParam("vnfinstanceid") String vnfInstanceId,
            @ApiParam(value = "1,add/2,delete/3,update") @QueryParam("operation") int operation) {

        logger.info("query vnf resource,vnf instance id:"+vnfInstanceId);

        //get vnf info in vnfm
        VnfInfoFromVnfm sourceVnfInfo = VnfmAdapterServiceWrapper.getInstance().queryVnf(vnfInstanceId);

        //convert to ChangedResource
        ChangedResource cr = ConversionTool.convertFromVnfInfoFromVnfmToChangedResource(vnfInstanceId,sourceVnfInfo,operation);

        //send result
        ResourceResponse result = new ResourceResponse();
        result.setInstanceId(vnfInstanceId);
        result.setResult(new Gson().toJson(cr));

        return result;
    }


    @POST
    @Path("/vnfinstances")
    @ApiOperation(value = "instantiate vnf", code=HttpStatus.CREATED_201 ,response = InstantiateVnfResponse.class)
    @ApiResponses(value = {@ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "the vnfm id is wrong"),
                           @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = "the vnfm url is invalid")})
    public Response instantiateVnf(
            @ApiParam(value = "instantiate request param") InstantiateRequest request) {

        InstantiateVnfResponse vnfmResponse = VnfmAdapterServiceWrapper.getInstance().instantiateVnf(request);

        //return uri
        URI returnURI = uriInfo.getBaseUriBuilder().path("/vnfinstances/"+vnfmResponse.getVnfInstanceId()).build();

        return Response.created(returnURI).entity(vnfmResponse).build();
    }



    @POST
    @Path("/vnfinstances/changedresource")
    @ApiOperation(value = "special,instantiate vnf,return changed resource",code=HttpStatus.CREATED_201, response = ResourceResponse.class)
    @ApiResponses(value = {@ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "the vnfm id is wrong"),
                           @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = "the vnfm url is invalid")})
    public Response instantiate(
            @ApiParam(value = "instantiate request param") InstantiateRequest request) {

        logger.info("instantiate json:"+new Gson().toJson(request));

        //instantiate vnf
        InstantiateVnfResponse vnfmResponse = VnfmAdapterServiceWrapper.getInstance().instantiateVnf(request);

        //retrieve data
        VnfInfoFromVnfm sourceVnfInfo = VnfmAdapterServiceWrapper.getInstance().queryVnf(vnfmResponse.getVnfInstanceId(),request.getVnfmId());

        //convert to ChangedResource
        ChangedResource cr = ConversionTool.convertFromVnfInfoFromVnfmToChangedResource(vnfmResponse.getVnfInstanceId(),sourceVnfInfo,1);
        ResourceResponse result = new ResourceResponse();
        result.setInstanceId(vnfmResponse.getVnfInstanceId());
        result.setResult(new Gson().toJson(cr));

        //return uri
        URI returnURI = uriInfo.getBaseUriBuilder().path("/vnfinstances/"+vnfmResponse.getVnfInstanceId()).build();

        return Response.created(returnURI).entity(result).build();
    }


    @POST
    @Path("/vnfinstances/{vnfinstanceid}/operation")
    @ApiOperation(value = "operate vnf" , code = HttpStatus.ACCEPTED_202 , response = String.class)
    @ApiResponses(value = {@ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "the vnf instance id is wrong"),
                           @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = "the vnfm url is invalid")})
    public Response operateVnf(
            @ApiParam(value = "vnf instance id") @PathParam("vnfinstanceid") String vnfInstanceId,
            @ApiParam(value = "operationtype:start/stop") OperateRequest request) {
        //
        VnfmAdapterServiceWrapper.getInstance().operateVnf(vnfInstanceId, request);

        //return uri
        URI returnURI = uriInfo.getBaseUriBuilder().path("/vnfinstances/"+vnfInstanceId).build();

        return Response.created(returnURI).status(HttpStatus.ACCEPTED_202).build();
    }

    @POST
    @Path("/vnfinstances/{vnfinstanceid}/scale")
    @ApiOperation(value = "scale vnf",code = HttpStatus.ACCEPTED_202 , response = String.class)
    @ApiResponses(value = {@ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "the vnf instance id is wrong"),
                           @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = "the vnfm url is invalid")})
    public Response scaleVnf(
            @ApiParam(value = "vnf instance id") @PathParam("vnfinstanceid") String vnfInstanceId,
            @ApiParam(value = "type:scaleIn,scaleOut,scaleUp,scaleDown;aspect:oncrete resources;additionalParam:keypair value;") ScaleRequest request) {

        VnfmAdapterServiceWrapper.getInstance().scaleVnf(vnfInstanceId, request);

        //return uri
        URI returnURI = uriInfo.getBaseUriBuilder().path("/vnfinstances/"+vnfInstanceId).build();

        return Response.created(returnURI).status(HttpStatus.ACCEPTED_202).build();
    }

    @POST
    @Path("/vnfinstances/{vnfinstanceid}")
    @ApiOperation(value = "terminal vnf",code = HttpStatus.NO_CONTENT_204 , response = String.class)
    @ApiResponses(value = {@ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "the vnf instance id is wrong"),
                           @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = "the vnfm url is invalid")})
    public Response terminalVnf(
            @ApiParam(value = "vnf instance id") @PathParam("vnfinstanceid") String vnfInstanceId,
            @ApiParam(value = "terminationType:forceful/graceful;gracefulTerminationTimeout:seconds") TerminalRequest request) {
        VnfmAdapterServiceWrapper.getInstance().terminalVnf(vnfInstanceId, request);

        return Response.status(HttpStatus.NO_CONTENT_204).build();
    }

    @GET
    @Path("/vnfinstances/{vnfinstanceid}/progress")
    @ApiOperation(value = "query execution processes of the specified vnf", response = VnfInstanceProcessResponse.class)
    @ApiResponses(value = {@ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "the vnf instance id is wrong"),
                           @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = "the vnfm url is invalid")})
    public VnfInstanceProcessResponse queryVnfInstanceProcess(
            @ApiParam(value = "vnf instance id") @PathParam("vnfinstanceid") String vnfInstanceId) {

        return VnfmAdapterServiceWrapper.getInstance().queryVnfInstanceProcess(vnfInstanceId);
    }

    @GET
    @Path("/vnfinstances/{vnfinstanceid}/notifications")
    @ApiOperation(value = "query process detail of the specified vnf", response = VnfInstanceProcessDetailResponse.class)
    @ApiResponses(value = {@ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "the vnf instance id is wrong"),
                           @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = "the vnfm url is invalid")})
    public VnfInstanceProcessDetailResponse queryVnfInstanceProcessDetail(
            @ApiParam(value = "vnf instance id") @PathParam("vnfinstanceid") String vnfInstanceId) {

        return VnfmAdapterServiceWrapper.getInstance().queryVnfInstanceProcessDetail(vnfInstanceId);
    }

    @GET
    @Path("/vnfinstances/{vnfinstanceid}/executionprogress")
    @ApiOperation(value = "special,query vnf execution schedule", response = ScheduleResponse.class)
    @ApiResponses(value = {@ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "the vnf instance id is wrong"),
                           @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = "the vnfm url is invalid")})
    public ScheduleResponse queryExecutionSchedule(@ApiParam(value = "vnf instance id") @PathParam("vnfinstanceid") String vnfInstanceId
        ,@ApiParam(value = "progress step,1、2、3、4、。。。。") @QueryParam("step") int step) {

       //query detail process
        VnfInstanceProcessDetailResponse detail = VnfmAdapterServiceWrapper.getInstance().queryVnfInstanceProcessDetail(vnfInstanceId);

        if(detail!=null && detail.getEntries()!=null && detail.getEntries().size() > 0)
        {
            int current = step > detail.getEntries().size() || step == 0 ? detail.getEntries().size()-1 : step-1;

            ProcessEntity entity = detail.getEntries().get(current);

            if(entity!=null && entity.getContent()!=null)
            {

                ProcessContent content = entity.getContent();
                OperationExecutionProgress progress = new OperationExecutionProgress();

                progress.setStatus("processing");
                CurrentStepInfo currentStepInfo = new CurrentStepInfo();
                currentStepInfo.setCurrentTime(entity.getUpdated());
                currentStepInfo.setDescription(content.getDetail());
                currentStepInfo.setName(content.getTask()+"_"+content.getSubTask());
                currentStepInfo.setPercent(Integer.toString(content.getProgress()));
                currentStepInfo.setStatus(ConversionTool.convertProgressStatus(content.getStatus()));
                progress.setCurrentStepInfo(currentStepInfo);

                //
                ScheduleResponse schedule = new ScheduleResponse();
                schedule.setNextStepNo(current+1+1);
                schedule.setProgressJson(new Gson().toJson(progress));
                schedule.setStatus(ConversionTool.convertProgressStatus(content.getStatus()));

                return schedule;
            }
        }

        //return empty data
        ScheduleResponse schedule = new ScheduleResponse();
        schedule.setNextStepNo(1);
        schedule.setProgressJson(new Gson().toJson(new OperationExecutionProgress()));
        schedule.setStatus("processing");

        return schedule;
    }

}
