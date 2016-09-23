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
package org.openo.nfvo.monitor.umc.pm.osf.api;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.nfvo.monitor.umc.UMCApp;
import org.openo.nfvo.monitor.umc.cache.CacheService;
import org.openo.nfvo.monitor.umc.db.UmcDbUtil;
import org.openo.nfvo.monitor.umc.db.entity.MonitorInfo;
import org.openo.nfvo.monitor.umc.fm.adpt.resources.RocResourceConfig;
import org.openo.nfvo.monitor.umc.fm.cache.FmCacheUtil;
import org.openo.nfvo.monitor.umc.i18n.I18n;
import org.openo.nfvo.monitor.umc.pm.adpt.roc.RocConfiguration;
import org.openo.nfvo.monitor.umc.pm.common.PmConst;
import org.openo.nfvo.monitor.umc.pm.common.PmException;
import org.openo.nfvo.monitor.umc.pm.db.process.PmCommonProcess;
import org.openo.nfvo.monitor.umc.pm.osf.db.util.HibernateSession;
import org.openo.nfvo.monitor.umc.pm.services.PmService;
import org.openo.nfvo.monitor.umc.pm.task.PmTaskService;
import org.openo.nfvo.monitor.umc.util.DebugPrn;
import org.openo.nfvo.monitor.umc.util.ExtensionAccess;
import org.openo.nfvo.monitor.umc.util.ExtensionUtil;
import org.openo.nfvo.monitor.umc.util.filescaner.FastFileSystem;

public class PmServiceImplTest {

	private static DebugPrn dMsg = new DebugPrn(PmServiceImplTest.class.getName());
	private static MonitorInfo monitorInfo = null;
    static {
        RocResourceConfig.setRocResourceAddr("127.0.0.1:8204");
        RocConfiguration.setRocServerAddr("127.0.0.1:8204");
    }
	@BeforeClass
	public static void setUp() {
        String[] packageUrls =new String[] {UMCApp.class.getPackage().getName()};
        ExtensionUtil.init(packageUrls);
		String filePath = "E:\\monitor-dev-code\\monitor\\umc\\umc-api\\microservice-standalone\\src\\main\\assembly\\conf";
    	initFastFileSystem(filePath);
    	UmcDbUtil.setSessionFactory(HibernateSession.init());
		dMsg.info("build cache");
    	FmCacheUtil.init();
    	CacheService.init();
    	I18n.init();
		dMsg.info("restart all pm task");
		try {
			PmService.reStartAllPmTask("127.0.0.1");
		} catch (PmException e) {
			Assert.fail("Exception" + e.getMessage());
		}
		
		monitorInfo = new MonitorInfo();
		String oid = "nfv.host.linux=010074149067";
		String customPara = "{\"PORT\":\"22\",\"USERNAME\":\"cmcc\",\"PASSWORD\":\"123456\",\"PROTOCOL\":\"SSH\",\"PROXYIP\":\"192.168.113.99\"}";
		monitorInfo.setOid(oid);
		monitorInfo.setIpAddress("192.168.113.107");
		monitorInfo.setNeTypeId("nfv.host.linux");
		monitorInfo.setCustomPara(customPara);
		monitorInfo.setLabel("computer-node02");
		monitorInfo.setExtendPara("");
		monitorInfo.setOrigin("roc");
		initMonitorInfo();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		try {
		    HibernateSession.destory();
		} catch (Exception e) {
			Assert.fail("Exception" + e.getMessage());
		}
	}
	

	

	public static void initMonitorInfo() {
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
		PmCommonProcess.clear(PmConst.PM_TASK);
		PmCommonProcess.clear(PmConst.PM_TASK_THRESHOLD);
	} catch (PmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
   
	@Test
	public void testTask() {
		PmTaskService.pmTaskCreate("","127.0.0.1", "nfv.host.linux=010074149067",
				"nfv.host.linux");
		PmTaskService.pmThresholdCreate("nfv.host.linux=010074149067",
				"nfv.host.linux");
		PmTaskService.pmTaskModify("nfv.host.linux=010074149067");
	    PmTaskService.pmTaskReCreate("","10.2.41.169", "nfv.host.linux=010074149067",
				"nfv.host.linux");
	    PmTaskService.pmTaskDelete("nfv.host.linux=010074149067");
		PmTaskService.pmThresholdDelete("nfv.host.linux=010074149067");
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
