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

package org.openo.nfvo.monitor.umc.fm.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;
import org.openo.nfvo.monitor.umc.db.UmcDbUtil;
import org.openo.nfvo.monitor.umc.db.entity.CurrentAlarm;
import org.openo.nfvo.monitor.umc.fm.adpt.resources.RocResourceConfig;
import org.openo.nfvo.monitor.umc.fm.cache.FmCacheUtil;
import org.openo.nfvo.monitor.umc.fm.db.process.FmDBProcess;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.AlarmIds;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.CurAlarmQueryCond;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.CurAlarmQueryRequest;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.PositionCond;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.ProbableCauseCond;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.UpdateAckStateParam;
import org.openo.nfvo.monitor.umc.fm.resource.bean.response.CurAlarmQueryResult;
import org.openo.nfvo.monitor.umc.fm.resource.bean.response.DeleteCurAlarmResult;
import org.openo.nfvo.monitor.umc.fm.resource.bean.response.NgictAlarmData;
import org.openo.nfvo.monitor.umc.fm.resources.CurrentAlarmResource;
import org.openo.nfvo.monitor.umc.fm.wrapper.ProbableCauseServiceWrapper;
import org.openo.nfvo.monitor.umc.pm.adpt.fm.bean.FmAlarmData;
import org.openo.nfvo.monitor.umc.pm.osf.db.util.H2DbServer;
import org.openo.nfvo.monitor.umc.pm.osf.db.util.HibernateSession;
import org.openo.nfvo.monitor.umc.util.filescaner.FastFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

import io.dropwizard.testing.junit.ResourceTestRule;


@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CurrentAlarmResourceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProbableCauseServiceWrapper.class);
    private static final HttpServletRequest queryrequest = mock(HttpServletRequest.class);
    private static final FmDBProcess dbProcess = mock(FmDBProcess.class);
    static {
        RocResourceConfig.setRocResourceAddr("127.0.0.1:8204");
    }
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new CurrentAlarmResource())
            .build();
    @Captor
    private ArgumentCaptor<FmAlarmData> itAlarmDataCaptor;
    private FmAlarmData itAlarmData;
    private UpdateAckStateParam updatePara = new UpdateAckStateParam();
    private AlarmIds delPara = new AlarmIds();

    @Before
    public void setUp() {
        //H2DbServer.startUp();
/*    	FastFileSystem.init();
    	FmCacheUtil.init();*/
        UmcDbUtil.setSessionFactory(HibernateSession.init());

        itAlarmData = new FmAlarmData();
        itAlarmData.setAlarmKey("alarmKeyTest");
        itAlarmData.setAlarmRaiseTime(new Date());
        itAlarmData.setCode(11111111111L);
        itAlarmData.setDetailInfo("test insert alarm data");
        itAlarmData.setDevIp("127.0.0.1");
        itAlarmData.setEventType(0);
        itAlarmData.setMoc("test");
        itAlarmData.setOid("123456");
        itAlarmData.setServerity(4);
        itAlarmData.setSystemType(11);
        
    }

    @After
    public void tearDown() {
        try {
            HibernateSession.destory();
            //H2DbServer.shutDown();
        } catch (Exception e) {
            Assert.fail("Exception" + e.getMessage());
        }
    }
    
    @Test
    public void insertCurrentAlarmData() throws JsonProcessingException {
        final Response response = resources.client().target("/umcfm/v1/curalarms/insert/test")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(itAlarmData, MediaType.APPLICATION_JSON));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
    }

    @Test
    public void queryCurrentAlarm() throws Exception {
        CurrentAlarmResource alarmResource = new CurrentAlarmResource();
        
        CurAlarmQueryRequest req = new CurAlarmQueryRequest();
        CurAlarmQueryCond condition = new CurAlarmQueryCond();
        PositionCond[] positions = new PositionCond[1];
        positions[0] = new PositionCond();
        positions[0].setOid("123456");
        condition.setPositions(positions);
        req.setCondition(condition);
        req.setPageNumber(1);
        req.setPageSize(10);
        
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter("request")).andReturn(objectToString(req));
        EasyMock.replay(request);
        
        CurAlarmQueryResult result = new CurAlarmQueryResult();
        result = alarmResource.getCurAlarms(request, "en_US");
        String resultStr = objectToString(result);
        assertThat(resultStr).isNotEmpty();
        String str = "{\"totalCount\":1," +
        		"\"alarms\":[{\"id\":"+result.getAlarms()[0].getId()+"," +
        		"\"alarmKey\":\"alarmKeyTest\"," +
        		"\"moc\":\"test\"," +
        		"\"mocName\":\"\"," +
        		"\"position1\":\"123456\"," +
        		"\"alarmRaisedTime\":"+String.valueOf(result.getAlarms()[0].getAlarmRaisedTime())+"," +
        		"\"alarmChangedTime\":"+String.valueOf(result.getAlarms()[0].getAlarmChangedTime())+"," +
        		"\"systemType\":11," +
        		"\"systemTypeName\":\"SystemTypeName: 11\"," +
        		"\"probableCauseCode\":11111111111," +
        		"\"probableCauseCodeName\":\"11111111111\"," +
        		"\"alarmType\":3," +
        		"\"alarmTypeName\":\"alarmTypeName: 3\"," +
        		"\"perceivedSeverity\":4," +
        		"\"reasonCode\":0," +
        		"\"additionalText\":\"test insert alarm data\"," +
        		"\"customAttrs\":[null,null,null,null,null,null,null,null,null,null,null,null]," +
        		"\"ackState\":"+result.getAlarms()[0].getAckState()+"," +
        		"\"ackTime\":0," +
        		"\"commentTime\":"+String.valueOf(result.getAlarms()[0].getCommentTime())+"," +
        		"\"alarmClearedTime\":0," +
        		"\"clearType\":0," +
        		"\"visible\":false," +
        		"\"timeZoneOffset\":0," +
        		"\"DSTSaving\":0," +
        		"\"neIp\":\"127.0.0.1\"," +
        		"\"pathIds\":[null]," +
        		"\"originalPerceivedSeverity\":4," +
        		"\"isADMC\":false}]}";
        assertThat(resultStr).isEqualTo(str);
        EasyMock.verify(request);
    }
    
    @Test
    public void updateCurrentAlarm() throws Exception {
        CurrentAlarmResource alarmResource = new CurrentAlarmResource();
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        
        NgictAlarmData[] result = new NgictAlarmData[1];
        result[0] = new NgictAlarmData();
        CurrentAlarm curAlarm = FmDBProcess.queryCurAlarmByAlarmKey("alarmKeyTest");
        if(curAlarm != null){
            long[] ids = {curAlarm.getId()};
            updatePara.setIds(ids);
            updatePara.setAckState(2);
        }
        EasyMock.expect(request.getParameter("request")).andReturn(objectToString(updatePara));
        EasyMock.expect(request.getSession(false)).andReturn(null);
        EasyMock.replay(request);
        result = alarmResource.updateCurrentAlarm(request, "en_US");
        String resultStr = objectToString(result[0]);
        assertThat(resultStr).isNotEmpty();
        String assertStr = "{\"id\":" + String.valueOf(result[0].getId())+"," +
        		"\"alarmKey\":\"alarmKeyTest\"," +
        		"\"moc\":\"test\"," +
        		"\"mocName\":\"\"," +
        		"\"position1\":\"123456\"," +
        		"\"alarmRaisedTime\":" + String.valueOf(result[0].getAlarmRaisedTime())+"," +
        		"\"alarmChangedTime\":" + String.valueOf(result[0].getAlarmChangedTime())+"," +
        		"\"systemType\":11," +
        		"\"systemTypeName\":\"SystemTypeName: 11\"," +
        		"\"probableCauseCode\":11111111111," +
        		"\"probableCauseCodeName\":\"11111111111\"," +
        		"\"alarmType\":3," +
        		"\"alarmTypeName\":\"alarmTypeName: 3\"," +
        		"\"perceivedSeverity\":4," +
        		"\"reasonCode\":0," +
        		"\"additionalText\":\"test insert alarm data\"," +
        		"\"customAttrs\":[null,null,null,null,null,null,null,null,null,null,null,null]," +
        		"\"ackState\":" + String.valueOf(result[0].getAckState())+"," +
        		"\"ackTime\":" + String.valueOf(result[0].getAckTime())+"," +
        		"\"ackUserId\":\""+ String.valueOf(result[0].getAckUserId()) +"\"," +
        		"\"commentTime\":" + String.valueOf(result[0].getCommentTime())+"," +
        		"\"alarmClearedTime\":" + String.valueOf(result[0].getAlarmClearedTime())+"," +
        		"\"clearType\":0," +
        		"\"visible\":false," +
        		"\"timeZoneOffset\":0," +
        		"\"DSTSaving\":0," +
        		"\"neIp\":\"127.0.0.1\"," +
        		"\"pathIds\":[null]," +
        		"\"originalPerceivedSeverity\":4," +
        		"\"isADMC\":false}";
        assertThat(resultStr).isEqualTo(assertStr);
        
        EasyMock.verify(request);
    }
    
    @Test
    public void zdeleteCurrentAlarm() throws Exception {
        CurrentAlarmResource alarmResource = new CurrentAlarmResource();
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        
        DeleteCurAlarmResult result = new DeleteCurAlarmResult();
        CurrentAlarm curAlarm = FmDBProcess.queryCurAlarmByAlarmKey("alarmKeyTest");
        if(curAlarm != null){
            long[] ids = {curAlarm.getId()};
            delPara.setIds(ids);
        }
        EasyMock.expect(request.getParameter("request")).andReturn(objectToString(delPara));
        EasyMock.replay(request);
        result = alarmResource.deleteCurAlarmByIds(request);
        String resultStr = objectToString(result);
        String assertStr = "{\"success\":true,\"deleteFailedIds\":[]}";
        assertThat(resultStr).isEqualTo(assertStr);
        EasyMock.verify(request);
    }
    
    private CurrentAlarm buildCurrentAlarm(FmAlarmData itAlarmData) {
        CurrentAlarm currentAlarm = new CurrentAlarm();
//        ProbableCause probableCause = new ProbableCause();
        ProbableCauseCond systemTypesObj = new ProbableCauseCond();
        int[] systemTypes = new int[1];
        systemTypes[0] = 11;
        systemTypesObj.setSystemTypes(systemTypes);
//        List<ProbableCause> probableCauses = FmDBProcess.queryProbableCause(systemTypesObj);
//        if (probableCauses.size() != 0) {
//            probableCause = probableCauses.get(0);
//            currentAlarm.setAlarmType(probableCause.getAlarmType());
//        }
        currentAlarm.setAlarmType(1);
        currentAlarm.setAlarmKey("alarmKeyTest");
        currentAlarm.setPosition1("123456");
        currentAlarm.setCode(11111111111L);
        currentAlarm.setAlarmRaisedTime(itAlarmData.getAlarmRaiseTime());
        currentAlarm.setPerceivedSeverity(1);
        currentAlarm.setAdditionalText("test insert alarm data");
        currentAlarm.setNeIp("127.0.0.1");
        currentAlarm.setSystemType(11);
        currentAlarm.setMoc("test");

        currentAlarm.setId(12345678L);
        currentAlarm.setAlarmSource("umc");
        currentAlarm.setOriginalPerceivedSeverity(11);
        currentAlarm.setDispproduct("N/A");
        currentAlarm.setAlarmRaisedTime_gmt(itAlarmData.getAlarmRaiseTime());
        currentAlarm.setTimeZoneOffset(0);
        currentAlarm.setDataType(0);
        currentAlarm.setAckState(2);
        currentAlarm.setVisible(1);
        return currentAlarm;
    }
    
    public static String objectToString(Object o) {
        if (o == null) return "";
        Gson gson = new Gson();
        String str = gson.toJson(o);
        return str;
    } 
    
}
