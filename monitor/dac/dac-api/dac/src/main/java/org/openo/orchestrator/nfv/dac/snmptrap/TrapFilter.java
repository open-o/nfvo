/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.dac.snmptrap;

import org.openo.orchestrator.nfv.dac.snmptrap.entity.TrapConst;
import org.openo.orchestrator.nfv.dac.snmptrap.entity.TrapData;
import org.openo.orchestrator.nfv.dac.snmptrap.processor.TrapProcessor;
import org.openo.orchestrator.nfv.dac.snmptrap.processor.util.TrapProcessorListParser;
import org.openo.orchestrator.nfv.dac.snmptrap.util.TrapConfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommandResponderEvent;


public class TrapFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(TrapFilter.class);
	public static void applyTrapFilter(CommandResponderEvent trapEvent)
	{
        TrapData trapData = new TrapData();
        trapData.setSystemType(TrapConst.TRAP_SYSTEM_TYPE);
        String ipAddress = getDevIp(trapEvent.getPeerAddress().toString());
        String trapOID = TrapConfUtil.getTrapOID(trapEvent.getPDU());
        trapData.setTrapOid(trapOID);
        trapData.setIpAddress(ipAddress);
        trapData.setEventKey(ipAddress);
        LOGGER.info("recive trap ip: " + ipAddress + " trapOid: " + trapOID);
        if (trapData.getTrapOid() != null && !trapData.getTrapOid().equals(""))
            {
                // first trap processor
                TrapProcessor processor = TrapProcessorListParser.getInstance().getTrapProcessor();
                processor.process(trapData, trapEvent.getPDU());
            }
	}

	private static String getDevIp(String ipStr)
	{
		return ipStr.split("/")[0];
	}
}
