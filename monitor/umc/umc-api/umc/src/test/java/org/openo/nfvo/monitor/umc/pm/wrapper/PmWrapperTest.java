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
package org.openo.nfvo.monitor.umc.pm.wrapper;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openo.nfvo.monitor.umc.db.UmcDbUtil;
import org.openo.nfvo.monitor.umc.pm.bean.PmMeaTaskBean;
import org.openo.nfvo.monitor.umc.pm.common.PmException;
import org.openo.nfvo.monitor.umc.pm.osf.db.util.H2DbServer;
import org.openo.nfvo.monitor.umc.pm.osf.db.util.HibernateSession;
import org.openo.nfvo.monitor.umc.pm.services.NeHandler;
import org.openo.nfvo.monitor.umc.pm.services.PmService;
import org.openo.nfvo.monitor.umc.util.DebugPrn;

public class PmWrapperTest {
	private static DebugPrn dMsg = new DebugPrn(PmWrapperTest.class.getName());

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
	public void testQueryCounters() {
		CounterServiceWrapper.queryCounters("nfv.vdu.linux",
				"nfv.vdu.linux.cpu","zh_CN");
	}

	@Test
	public void testGetAllPmMeaTasks() {
		PmMeaTaskServiceWrapper.getAllPmMeaTasks(null, null, "zh_CN");
	}

	@Test
	public void testGetMeaTaskById() {
		String taskId = "3";
		PmMeaTaskBean bean = PmMeaTaskServiceWrapper.getMeaTaskById(taskId, "zh_CN");
		assertEquals(bean.getId(), taskId);
	}
}
