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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.nfvo.monitor.umc.db.UmcDbUtil;
import org.openo.nfvo.monitor.umc.pm.common.PmException;
import org.openo.nfvo.monitor.umc.pm.osf.db.util.H2DbServer;
import org.openo.nfvo.monitor.umc.pm.osf.db.util.HibernateSession;
import org.openo.nfvo.monitor.umc.pm.services.PmService;
import org.openo.nfvo.monitor.umc.pm.task.PmTaskService;
import org.openo.nfvo.monitor.umc.util.DebugPrn;

public class PmServiceImplTest {

	private static DebugPrn dMsg = new DebugPrn(
			PmServiceImplTest.class.getName());

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
	}

	@AfterClass
	public static void tearDown() throws Exception {
		try {
		    HibernateSession.destory();
			H2DbServer.shutDown();
		} catch (Exception e) {
			Assert.fail("Exception" + e.getMessage());
		}
	}

	@Test
	public void testTask() {
		PmTaskService.pmTaskCreate("127.0.0.1", "nfv.vdu.linux=010074149067",
				"nfv.vdu.linux");
		PmTaskService.pmThresholdCreate("nfv.vdu.linux=010074149067",
				"nfv.vdu.linux");
		PmTaskService.pmTaskModify("nfv.vdu.linux=010074149067");
		PmTaskService.pmTaskReCreate("10.0.0.1", "nfv.vdu.linux=010074149067",
				"nfv.vdu.linux");
		PmTaskService.pmTaskDelete("nfv.vdu.linux=010074149067");
		PmTaskService.pmThresholdDelete("nfv.vdu.linux=010074149067");
	}

}
