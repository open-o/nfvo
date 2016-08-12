/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.orchestrator.nfv.umc.pm.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.openo.orchestrator.nfv.umc.cache.CacheUtil;
import org.openo.orchestrator.nfv.umc.db.UmcDbUtil;
import org.openo.orchestrator.nfv.umc.pm.bean.HistoryPmDataResponse;
import org.openo.orchestrator.nfv.umc.pm.bean.PmQueryConditionBean;
import org.openo.orchestrator.nfv.umc.pm.bean.Resource;
import org.openo.orchestrator.nfv.umc.pm.osf.db.util.H2DbServer;
import org.openo.orchestrator.nfv.umc.pm.osf.db.util.HibernateSession;
import org.openo.orchestrator.nfv.umc.pm.resources.HistoryPmDataResource;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import io.dropwizard.testing.junit.ResourceTestRule;

/**
 * @author wangjiangping
 * @date 2016/2/17 11:36:53
 *
 */
public class HistoryPmDataResourceTest {

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new HistoryPmDataResource())
            .build();

    Client client = resources.client().register(JacksonJsonProvider.class);// 注册json支持

    public static PmQueryConditionBean pmQueryConditionBean = new PmQueryConditionBean();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        H2DbServer.startUp();
        UmcDbUtil.setSessionFactory(HibernateSession.init());
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        HibernateSession.destory();
        H2DbServer.shutDown();
    }

    @Before
    public void setUp() throws Exception {
        Resource[] resources = new Resource[1];
        resources[0] = new Resource();
        resources[0].setId("");
        resources[0].setName("");

        pmQueryConditionBean.setBeginTime(System.currentTimeMillis()-10000L);
        pmQueryConditionBean.setEndTime(System.currentTimeMillis());
        pmQueryConditionBean.setPageNo(1);
        pmQueryConditionBean.setPageSize(20);
        pmQueryConditionBean.setGranularity(300);
        pmQueryConditionBean.setResources(resources);
        pmQueryConditionBean.setMoTypeId("moTypeId");

    }

    @After
    public void tearDown() throws Exception {}

    /**
     * Test method for {@link org.openo.orchestrator.nfv.umc.pm.resources.HistoryPmDataResource#queryPmData(org.openo.orchestrator.nfv.umc.pm.bean.PmQueryConditionBean)}.
     */
    @Test
    public void testQueryPmData() {

        when(CacheUtil.getPmTypeIdByMoTypeId("moTypeId")).thenReturn("pmTypeId");
        when(CacheUtil.getDataTableNameByPMType("pmTypeId")).thenReturn("NFV_VDU_LINUX_CPU");


        WebTarget target = client.target("/umcpm/v1/historydataqueries");
        Response response = target.request().buildPost(Entity.entity(pmQueryConditionBean, MediaType.APPLICATION_JSON)).invoke();
        HistoryPmDataResponse historyPmDataResponse = response.readEntity(HistoryPmDataResponse.class);

        assertThat(historyPmDataResponse.getTotalCout()).isGreaterThan(0);
        response.close();
    }

}
