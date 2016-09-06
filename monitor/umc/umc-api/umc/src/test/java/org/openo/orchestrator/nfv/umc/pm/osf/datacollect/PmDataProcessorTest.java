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
package org.openo.orchestrator.nfv.umc.pm.osf.datacollect;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.orchestrator.nfv.umc.db.UmcDbUtil;
import org.openo.orchestrator.nfv.umc.pm.common.PmException;
import org.openo.orchestrator.nfv.umc.pm.datacollect.PmDataProcessor;
import org.openo.orchestrator.nfv.umc.pm.datacollect.bean.PmData;
import org.openo.orchestrator.nfv.umc.pm.osf.api.PmServiceImplTest;
import org.openo.orchestrator.nfv.umc.pm.osf.db.util.H2DbServer;
import org.openo.orchestrator.nfv.umc.pm.osf.db.util.HibernateSession;
import org.openo.orchestrator.nfv.umc.pm.services.NeHandler;
import org.openo.orchestrator.nfv.umc.pm.services.PmService;
import org.openo.orchestrator.nfv.umc.util.DebugPrn;
 
public class PmDataProcessorTest {

	private static DebugPrn dMsg = new DebugPrn(
			PmServiceImplTest.class.getName());
	String proxyIp = "127.0.0.1";

	@BeforeClass
	public static void setUp() {
		H2DbServer.startUp();
		UmcDbUtil.setSessionFactory(HibernateSession.init());

		dMsg.info("build cache");
		PmService.getInstance().buildCache();
		dMsg.info("restart all pm task");
		try {
			PmService.getInstance().reStartAllPmTask("127.0.0.1");
		} catch (PmException e) {
			Assert.fail("Exception" + e.getMessage());
		}
		NeHandler.createHandle("127.0.0.1", "nfv.vdu.linux=010074149067", "nfv.vdu.linux");
//		handler = new CreateNeHandler();
//		handler.handle(new String[] { "nfv.vdu.linux=010074149067",
//				"nfv.host.linux=010074149067" }, new String[] {
//				"nfv.vdu.linux", "nfv.host.linux" }, new String[] { "test1",
//				"test2" });

	}

	@AfterClass
	public static void setDown() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("nfv.vdu.linux=010074149067");
		list.add("nfv.host.linux=010074149067");
		NeHandler.deleteHandle(list);
		
		try {
			HibernateSession.destory();
			H2DbServer.shutDown();
		} catch (Exception e) {
			Assert.fail("Exception" + e.getMessage());
		}
	}

	@Test
	public void testPmDataSave() {
		List<PmData> pmDataList = new ArrayList<PmData>();
		PmData pmData = new PmData();
		pmData.setJobId(1);
		pmData.setGranularity(300);
		pmData.setCollectTime(1444038900000L);
		Properties p = new Properties();
		p.setProperty("CPUBUSYRATIO", "88.1");
		pmData.setValues(new Properties[] { p });
		pmDataList.add(pmData);
		PmDataProcessor.getInstance().pmTaskResultProcess(proxyIp, pmDataList);

		List<PmData> pmDataList1 = new ArrayList<PmData>();
		PmData pmData1 = new PmData();
		pmData1.setJobId(2);
		pmData1.setGranularity(300);
		pmData1.setCollectTime(1444038900000L);
		Properties p1 = new Properties();
		p1.setProperty("USEDMEMRATIO", "88.1");
		p1.setProperty("SWAPUSEDRATIO", "88.1");
		pmData1.setValues(new Properties[] { p1 });
		pmDataList1.add(pmData1);
		PmDataProcessor.getInstance().pmTaskResultProcess(proxyIp, pmDataList1);

		List<PmData> pmDataList2 = new ArrayList<PmData>();
		PmData pmData2 = new PmData();
		pmData2.setJobId(7);
		pmData2.setGranularity(300);
		pmData2.setCollectTime(1444038900000L);
		Properties p2 = new Properties();
		p2.setProperty("LOGICVOLUMNPATH", "/root");
		p2.setProperty("FILESYSTEMNAME", "/test");
		p2.setProperty("LOGICVOLUMNSIZE", "11");
		p2.setProperty("LOGICVOLUMNAVAILABLE", "5");
		p2.setProperty("FILESYSTEMCAPACITY", "85");
		Properties p3 = new Properties();
		p3.setProperty("LOGICVOLUMNPATH", "/root1");
		p3.setProperty("FILESYSTEMNAME", "/test1");
		p3.setProperty("LOGICVOLUMNSIZE", "111");
		p3.setProperty("LOGICVOLUMNAVAILABLE", "51");
		p3.setProperty("FILESYSTEMCAPACITY", "91");
		pmData2.setValues(new Properties[] { p2, p3 });
		pmDataList2.add(pmData2);
		PmDataProcessor.getInstance().pmTaskResultProcess(proxyIp, pmDataList2);
	}
}
