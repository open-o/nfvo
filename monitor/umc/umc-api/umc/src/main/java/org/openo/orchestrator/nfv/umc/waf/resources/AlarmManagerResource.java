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
/**
 *
 */
package org.openo.orchestrator.nfv.umc.waf.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 10188044
 *
 */
@Path("/alarm")
public class AlarmManagerResource {

	public AlarmSendService alarmService = AlarmSendService.getInstance();
	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmManagerResource.class);

	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String sendAlarm(String oid) {
		/**TODO:
		int mockAlarmSize = 5;
		Date reqTime = new Date();
		for (int i = 0; i < mockAlarmSize; i++) {

			MockAlarmData alarmData = new MockAlarmData(oid, MockAlarmConst.EVENTTYPE_ALARM,
					MockAlarmConst.SYSTEM_TYPE,
					MockAlarmConst.alarmCodes[(int)(Math.random()*MockAlarmConst.alarmCodes.length)],
					new Date(), MockAlarmConst.SEVERITY_MINOR, null, null);
			try {

				alarmService.send(alarmData);
			} catch (AlarmSendException e) {
				return "Server Throw Exception, "+i+" alarm(s) may be sent successfully! Please check manually!"+e.getMessage();
			}
		}
		return "Raise alarm Success! Size:"+mockAlarmSize+". Request submit time:"+DateFormat.getDateTimeInstance().format(reqTime);
		**/
		LOGGER.info("service is not available now!");
		return "service is not available now!";
	}
}
