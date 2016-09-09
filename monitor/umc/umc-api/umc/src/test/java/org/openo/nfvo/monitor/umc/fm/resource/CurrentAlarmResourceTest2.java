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
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.openo.nfvo.monitor.umc.db.UmcDbUtil;
import org.openo.nfvo.monitor.umc.fm.resource.bean.response.CurAlarmQueryResult;
import org.openo.nfvo.monitor.umc.fm.resources.CurrentAlarmResource;
import org.openo.nfvo.monitor.umc.pm.osf.db.util.H2DbServer;
import org.openo.nfvo.monitor.umc.pm.osf.db.util.HibernateSession;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import io.dropwizard.testing.junit.ResourceTestRule;

/**
 * Unit tests for {@link org.openo.nfvo.monitor.umc.fm.resources.CurrentAlarmResource}.
 */
public class CurrentAlarmResourceTest2 {



    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new CurrentAlarmResource())
            .build();

    Client client = resources.client().register(JacksonJsonProvider.class);// 注册json支持

    @BeforeClass
    public static void setUpBeforeClass() {
        H2DbServer.startUp();
        UmcDbUtil.setSessionFactory(HibernateSession.init());
    }

    @AfterClass
    public static void tearDownAfterClass() {
        HibernateSession.destory();
        H2DbServer.shutDown();
    }

    @Before
    public void setUp() {

        //when(request.getParameter("request")).thenReturn("abcd");

    }

    /**
     * 
     * @date 2016/2/17 11:29:17
     * cannot work, to be fix.
     * reason: cannot mock @Context HttpServletRequest request correct.
     */
    /**
     * Unit tests for {@link org.openo.nfvo.monitor.umc.fm.resource.CurrentAlarmResource#getCurAlarms(HttpServletRequest, String)}.
     */
//    @Test
//    public void getCurAlarmsTest() {
//
//        String str = request.getParameter("request");
//        WebTarget target = client.target("/umcfm/v1/curalarms");
//        Response response =target.queryParam("request", str).request().header("language-option", "en_US").get();
//        CurAlarmQueryResult curAlarmQueryResult = response.readEntity(CurAlarmQueryResult.class);
//
//        assertThat(curAlarmQueryResult.getTotalCount()).isGreaterThan(new Long(0));
//        response.close();
//    }

    /**
     * Unit tests for {@link org.openo.nfvo.monitor.umc.fm.resources.CurrentAlarmResource#getCurAlarms(HttpServletRequest, String)}.
     */
    @Test
    public void getCurAlarmsTest2() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String get_param="{\"condition\":{\"severities\":[1,2],\"ackStates\":[1,2],\"positions\":[{\"oid\":\"134d07f9-40f2-4437-8918-b52beaf4f950\",\"locationId\":\"12\",\"group\":false}],\"isVisibles\":[\"1\",\"2\"],\"probableCauses\":[{\"systemType\":1234,\"codes\":[100001]}],\"ids\":[1440230101537,1440230101539],\"mocs\":[\"nfv.host.linux\"],\"alarmRaisedTime\":{\"beginTime\":1439433508000,\"endTime\":1439433598000,\"timeMode\":0},\"ackTime\":{\"relativeTime\":5000,\"timeMode\":1}},\"pageSize\":10,\"pageNumber\":1}";
        when(request.getParameter("request")).thenReturn(get_param);

        try {
            CurAlarmQueryResult curAlarmQueryResult = new CurrentAlarmResource().getCurAlarms(request, "en_US");
            assertThat(curAlarmQueryResult.getTotalCount()).isGreaterThan(new Long(0));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
