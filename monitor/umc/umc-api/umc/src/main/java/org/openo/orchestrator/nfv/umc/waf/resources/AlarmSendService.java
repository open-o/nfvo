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
package org.openo.orchestrator.nfv.umc.waf.resources;

import org.openo.orchestrator.nfv.umc.waf.bean.AlarmSendException;
import org.openo.orchestrator.nfv.umc.waf.bean.MockAlarmData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmSendService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmSendService.class);
	private static AlarmSendService instance = new AlarmSendService();
	private AlarmSendService(){

	}
	public static AlarmSendService getInstance(){
		return instance;
	}

	public void send(MockAlarmData alarmData) throws AlarmSendException {
		LOGGER.info("send alarm!Now do nothing!");
	}
}
