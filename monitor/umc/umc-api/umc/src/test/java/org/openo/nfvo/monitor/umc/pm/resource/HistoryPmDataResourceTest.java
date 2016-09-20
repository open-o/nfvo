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
package org.openo.nfvo.monitor.umc.pm.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import io.dropwizard.testing.junit.ResourceTestRule;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.openo.nfvo.monitor.umc.cache.CacheService;
import org.openo.nfvo.monitor.umc.cache.CacheUtil;
import org.openo.nfvo.monitor.umc.db.UmcDbUtil;
import org.openo.nfvo.monitor.umc.db.entity.MonitorInfo;
import org.openo.nfvo.monitor.umc.fm.cache.FmCacheUtil;
import org.openo.nfvo.monitor.umc.i18n.I18n;
import org.openo.nfvo.monitor.umc.pm.bean.HistoryPmDataResponse;
import org.openo.nfvo.monitor.umc.pm.bean.PmQueryConditionBean;
import org.openo.nfvo.monitor.umc.pm.bean.Resource;
import org.openo.nfvo.monitor.umc.pm.common.GeneralFileLocaterImpl;
import org.openo.nfvo.monitor.umc.pm.common.PmConst;
import org.openo.nfvo.monitor.umc.pm.common.PmException;
import org.openo.nfvo.monitor.umc.pm.datacollect.PmDataProcessor;
import org.openo.nfvo.monitor.umc.pm.datacollect.bean.PmData;
import org.openo.nfvo.monitor.umc.pm.db.process.PmCommonProcess;
import org.openo.nfvo.monitor.umc.pm.osf.db.util.HibernateSession;
import org.openo.nfvo.monitor.umc.pm.resources.HistoryPmDataResource;
import org.openo.nfvo.monitor.umc.pm.services.NeHandler;
import org.openo.nfvo.monitor.umc.pm.services.PmService;
import org.openo.nfvo.monitor.umc.util.ExtensionAccess;
import org.openo.nfvo.monitor.umc.util.filescaner.FastFileSystem;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * 
 * @date 2016/2/17 11:36:53
 *
 */
public class HistoryPmDataResourceTest {

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new HistoryPmDataResource())
            .build();

    Client client = resources.client().register(JacksonJsonProvider.class);// 注册json支持
    private static MonitorInfo monitorInfo = null;
    public static PmQueryConditionBean pmQueryConditionBean = new PmQueryConditionBean();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	    String filePath = "E:\\monitor-dev-code\\monitor\\umc\\umc-api\\microservice-standalone\\src\\main\\assembly\\conf";
     	GeneralFileLocaterImpl.getGeneralFileLocater().setConfigPath(filePath);
    	initFastFileSystem(filePath);
    	UmcDbUtil.setSessionFactory(HibernateSession.init());
    	FmCacheUtil.init();
    	CacheService.init();
    	I18n.init();
		monitorInfo = new MonitorInfo();
		String oid = "nfv.host.linux=010074149067";
		String customPara = "{\"PORT\":\"22\",\"USERNAME\":\"cmcc\",\"PASSWORD\":\"123456\",\"PROTOCOL\":\"SSH\",\"PROXYIP\":\"192.168.113.99\"}";
		monitorInfo.setOid(oid);
		monitorInfo.setIpAddress("192.168.113.107");
		monitorInfo.setNeTypeId("nfv.host.linux");
		monitorInfo.setCustomPara(customPara);
		monitorInfo.setLabel("computer-node02");
		monitorInfo.setExtendPara("");
/*		try {
			PmService.reStartAllPmTask("127.0.0.1");
		} catch (PmException e) {
			Assert.fail("Exception" + e.getMessage());
		}*/
		PmCommonProcess.save(PmConst.MONITOR_INFO, monitorInfo);
		NeHandler.createHandle("127.0.0.1", "nfv.host.linux=010074149067", "nfv.host.linux");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		list.add("nfv.host.linux=010074149067");
		NeHandler.deleteHandle(list);
        HibernateSession.destory();
    }

    @Before
    public void setUp() throws Exception {
    	
        Resource[] resources = new Resource[1];
        resources[0] = new Resource();
        resources[0].setId("nfv.host.linux=010074149067");
        resources[0].setName("");
        System.out.println(System.currentTimeMillis());
        pmQueryConditionBean.setBeginTime(System.currentTimeMillis()-100000000L);
        pmQueryConditionBean.setEndTime(System.currentTimeMillis()+8000000000L);
        pmQueryConditionBean.setPageNo(1);
        pmQueryConditionBean.setPageSize(20);
        pmQueryConditionBean.setGranularity(300);
        pmQueryConditionBean.setResources(resources);
        pmQueryConditionBean.setResourceTypeId("nfv.host.linux");
        String[] counterOrIndexId = new String[]{"C700130001","C700130002","C700130003","C700130004","C700130005"};
        pmQueryConditionBean.setCounterOrIndexId(counterOrIndexId);
        //pmQueryConditionBean.setMoTypeId("moTypeId");
        pmQueryConditionBean.setMoTypeId("nfv.host.linux.cpu");
        
		List<PmData> pmDataList = new ArrayList<PmData>();
		PmData pmData = new PmData();
		pmData.setJobId(1);
		pmData.setGranularity(300);
		pmData.setCollectTime(1474361688205L);
		Properties p = new Properties();
		p.setProperty("CPUBUSYRATIO", "88.1");
		pmData.setValues(new Properties[] { p });
		pmDataList.add(pmData);
		PmDataProcessor.getInstance().pmTaskResultProcess("127.0.0.1", pmDataList);
        Thread.sleep(60000);
    }

    @After
    public void tearDown() throws Exception {
    	PmCommonProcess.delete(PmConst.MONITOR_INFO, monitorInfo);
    	PmCommonProcess.clear("NFV_HOST_LINUX_CPU");
		PmCommonProcess.clear("NFV_HOST_LINUX_RAM");
		PmCommonProcess.clear("NFV_HOST_LINUX_FS");
		PmCommonProcess.clear("CurrentAlarm");
    }

    /**
     * Test method for {@link org.openo.nfvo.monitor.umc.pm.resources.HistoryPmDataResource#queryPmData(org.openo.nfvo.monitor.umc.pm.bean.PmQueryConditionBean)}.
     */
    @Test
    public void testQueryPmData() {
    	/*CacheUtil cache= EasyMock.createMock(CacheUtil.class);  

        when(cache.getPmTypeIdByMoTypeId("moTypeId")).thenReturn("pmTypeId");
        when(cache.getDataTableNameByPMType("pmTypeId")).thenReturn("NFV_VDU_LINUX_CPU");*/
    	HistoryPmDataResource historyPmDataResource = new HistoryPmDataResource();
    	 HistoryPmDataResponse historyPmDataResponse = historyPmDataResource.queryPmData(pmQueryConditionBean, "zh-cn");
       /* WebTarget target = client.target("/umcpm/v1/historydataqueries");
        Response response = target.request().buildPost(Entity.entity(pmQueryConditionBean, MediaType.APPLICATION_JSON)).invoke();
        HistoryPmDataResponse historyPmDataResponse = response.readEntity(HistoryPmDataResponse.class);*/

        assertThat(historyPmDataResponse.getTotalCout()).isGreaterThan(0);
        //response.close();
    }
	public static void initFastFileSystem(String configPath)
	{
		FastFileSystem.setInitDir(configPath);
		FastFileSystem.getInstance();
		File descFiles[] = FastFileSystem.getFiles("*-extendsdesc.xml");
		File implFiles[] = FastFileSystem.getFiles("*-extendsimpl.xml");
		ExtensionAccess.tryToInjectExtensionBindings(descFiles, implFiles);
	}
}
