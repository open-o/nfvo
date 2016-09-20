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
package org.openo.nfvo.monitor.umc.pm.osf.datacollect;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.nfvo.monitor.umc.cache.CacheService;
import org.openo.nfvo.monitor.umc.cometdserver.CometdServlet;
import org.openo.nfvo.monitor.umc.db.UmcDbUtil;
import org.openo.nfvo.monitor.umc.db.entity.MonitorInfo;
import org.openo.nfvo.monitor.umc.fm.cache.FmCacheUtil;
import org.openo.nfvo.monitor.umc.i18n.I18n;
import org.openo.nfvo.monitor.umc.pm.common.GeneralFileLocaterImpl;
import org.openo.nfvo.monitor.umc.pm.common.PmConst;
import org.openo.nfvo.monitor.umc.pm.common.PmException;
import org.openo.nfvo.monitor.umc.pm.datacollect.PmDataProcessor;
import org.openo.nfvo.monitor.umc.pm.datacollect.bean.PmData;
import org.openo.nfvo.monitor.umc.pm.db.process.PmCommonProcess;
import org.openo.nfvo.monitor.umc.pm.osf.api.PmServiceImplTest;
import org.openo.nfvo.monitor.umc.pm.osf.db.util.HibernateSession;
import org.openo.nfvo.monitor.umc.pm.services.NeHandler;
import org.openo.nfvo.monitor.umc.pm.services.PmService;
import org.openo.nfvo.monitor.umc.util.DebugPrn;
import org.openo.nfvo.monitor.umc.util.ExtensionAccess;
import org.openo.nfvo.monitor.umc.util.filescaner.FastFileSystem;
 
public class PmDataProcessorTest {

	private static DebugPrn dMsg = new DebugPrn(
			PmServiceImplTest.class.getName());
	String proxyIp = "127.0.0.1";
	private static MonitorInfo monitorInfo = null;
	@BeforeClass
	public static void setUp() {
	    String filePath = "E:\\monitor-dev-code\\monitor\\umc\\umc-api\\microservice-standalone\\src\\main\\assembly\\conf";
    	initFastFileSystem(filePath);
    	GeneralFileLocaterImpl.getGeneralFileLocater().setConfigPath(filePath);
    	UmcDbUtil.setSessionFactory(HibernateSession.init());
		dMsg.info("build cache");
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
    	
		dMsg.info("restart all pm task");
		try {
			PmService.reStartAllPmTask("127.0.0.1");
		} catch (PmException e) {
			Assert.fail("Exception" + e.getMessage());
		}
		NeHandler.createHandle("127.0.0.1", "nfv.host.linux=010074149067", "nfv.host.linux");
//		handler = new CreateNeHandler();
//		handler.handle(new String[] { "nfv.vdu.linux=010074149067",
//				"nfv.host.linux=010074149067" }, new String[] {
//				"nfv.vdu.linux", "nfv.host.linux" }, new String[] { "test1",
//				"test2" });

	}

	@AfterClass
	public static void setDown() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("nfv.host.linux=010074149067");
		NeHandler.deleteHandle(list);
		try {
			HibernateSession.destory();
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
		pmData2.setJobId(3);
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
		
		try {
			Thread.sleep(120000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	    @Before
		public void initMonitorInfo() {
			try {
				PmCommonProcess.save(PmConst.MONITOR_INFO, monitorInfo);
			} catch (PmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	   @After
		public void deleteMonitorInfo(){
		   try {
			PmCommonProcess.delete(PmConst.MONITOR_INFO, monitorInfo);
			PmCommonProcess.clear("NFV_HOST_LINUX_CPU");
			PmCommonProcess.clear("NFV_HOST_LINUX_RAM");
			PmCommonProcess.clear("NFV_HOST_LINUX_FS");
			PmCommonProcess.clear("CurrentAlarm");
		} catch (PmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
