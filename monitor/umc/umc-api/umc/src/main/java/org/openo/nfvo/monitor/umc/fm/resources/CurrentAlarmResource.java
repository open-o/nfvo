/**
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
package org.openo.nfvo.monitor.umc.fm.resources;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.nfvo.monitor.umc.db.entity.CurrentAlarm;
import org.openo.nfvo.monitor.umc.fm.common.FMConsts;
import org.openo.nfvo.monitor.umc.fm.db.process.FmDBProcess;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.AlarmIds;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.CurAlarmQueryRequest;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.UpdateAckStateParam;
import org.openo.nfvo.monitor.umc.fm.resource.bean.response.CurAlarmQueryResult;
import org.openo.nfvo.monitor.umc.fm.resource.bean.response.DeleteCurAlarmResult;
import org.openo.nfvo.monitor.umc.fm.resource.bean.response.NgictAlarmData;
import org.openo.nfvo.monitor.umc.fm.util.JacksonJsonUtil;
import org.openo.nfvo.monitor.umc.fm.wrapper.CurrentAlarmServiceWrapper;
import org.openo.nfvo.monitor.umc.pm.adpt.fm.bean.FmAlarmData;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * API for FM current alarm
 *
 */
//@Path("/umcfm/v1")
@Path("/fm")
@Api(tags = {"FMCurrentAlarmInterface"})
@Produces(MediaType.APPLICATION_JSON)
public class CurrentAlarmResource {
    public static final int MAX_NUMBER_OF_DELETE_AND_ACK_ALARM = 1000;
    FmDBProcess fmDBProcess = new FmDBProcess();

    @GET
    @Path("/curalarms")
    @ApiOperation(value = "get currentAlarm by condition", response = NgictAlarmData.class, responseContainer = "List")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public CurAlarmQueryResult getCurAlarms(@Context HttpServletRequest request, @HeaderParam("language-option") String language) throws Exception {
        CurAlarmQueryRequest req =
                (CurAlarmQueryRequest) JacksonJsonUtil.jsonToBean(
                        request.getParameter(FMConsts.REQUEST_PARAM_NAME),
                        CurAlarmQueryRequest.class, true);
        if (!req.isValid()) {
            throw new NotAcceptableException("json string is not valid.");
        }

        return new CurrentAlarmServiceWrapper().queryCurrentAlarm(req, language);
    }

    @DELETE
    @ApiOperation(value = "delete currentAlarm by Ids ", response = CurrentAlarm.class)
    @Path("/curalarms")
    @Timed
    public DeleteCurAlarmResult deleteCurAlarmByIds(@Context HttpServletRequest request)
            throws Exception {
        AlarmIds req =
                (AlarmIds) JacksonJsonUtil.jsonToBean(
                        request.getParameter(FMConsts.REQUEST_PARAM_NAME), AlarmIds.class, true);
        return new CurrentAlarmServiceWrapper().deleteCurAlarmByIds(req);
    }

    @PUT
    @ApiOperation(value = "update currentAlarm data ", response = CurrentAlarm.class)
    @Path("/curalarms")
    @Timed
    public NgictAlarmData[] updateCurrentAlarm(@Context HttpServletRequest request, @HeaderParam("language-option") String language)
            throws Exception {
        String ackUserId = request.getSession(false) == null ? "admin" : (String) request.getSession().getAttribute("username");
        UpdateAckStateParam req =
                (UpdateAckStateParam) JacksonJsonUtil.jsonToBean(
                        request.getParameter(FMConsts.REQUEST_PARAM_NAME),
                        UpdateAckStateParam.class, true);
        return new CurrentAlarmServiceWrapper().updateCurrentAlarm(req, ackUserId, language);
    }

    @GET
    @Path("/curalarms/findAll")
    @ApiOperation(value = "get all currentAlarm ", response = NgictAlarmData.class, responseContainer = "List")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public List<CurrentAlarm> findAllCurAlarms(@HeaderParam("language-option") String language) {
        return new CurrentAlarmServiceWrapper().queryAllCurAlarm(language);
    }

    @GET
    @Path("/curalarms/count")
    @ApiOperation(value = "get currentAlarm count by oid")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public int getCurAlarmsCount(@ApiParam(value = "oid param", required = true) @QueryParam("oid") String oid) throws Exception {
        return new CurrentAlarmServiceWrapper().queryCurAlarmsCount(oid);
    }

    @POST
    @Path("/curalarms/insert")
    @ApiOperation(value = "insert currentAlarm ", response = NgictAlarmData.class, responseContainer = "List")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public void insertCurAlarms(
            @ApiParam(value = "insert currentAlarm", required = true) CurrentAlarm curAlarm) {
        FmDBProcess.insertCurAlarm(curAlarm);
    }

    @POST
    @Path("/curalarms/insert/test")
    @ApiOperation(value = "insert currentAlarm ")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public void insertCurAlarmsTest(
            @ApiParam(value = "insert currentAlarm test", required = true) FmAlarmData fmAlarmData) {
        FmDBProcess.insertCurrentAlarmData(fmAlarmData);
    }

}
