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
package org.openo.orchestrator.nfv.dac.snmptrap.processor;

import java.util.Map;
import java.util.Properties;

import org.openo.orchestrator.nfv.dac.common.util.ExtensionAccess;
import org.openo.orchestrator.nfv.dac.snmptrap.entity.TrapData;
import org.openo.orchestrator.nfv.dac.snmptrap.processor.entity.TrapDescInfo;
import org.openo.orchestrator.nfv.dac.snmptrap.processor.util.TrapProcessorConfig;
import org.openo.orchestrator.nfv.dac.snmptrap.processor.util.TrapProcessorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;

public class TrapPrivateProcessor implements TrapProcessor
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TrapPrivateProcessor.class);
	private static final String EXTENTISONID = "trap.private";
    private TrapProcessor next;

    public TrapPrivateProcessor()
    {
    }

    public TrapPrivateProcessor(TrapProcessor next)
    {
        this.next = next;
    }

    @Override
    public boolean process(TrapData trapData, PDU trapPDU)
    {

        TrapDescInfo trapDescInfo = TrapProcessorUtil.getTrapDescInfo(trapData.getTrapOid());


        if (trapDescInfo != null)
        {
            return handleTrap(trapData, trapPDU);
        }
        else if (next != null)
        {
            return next.process(trapData, trapPDU);
        }
        return false;
    }

    @Override
    public TrapProcessor getNext()
    {
        return this.next;
    }

    @Override
    public void setNext(TrapProcessor next)
    {
        this.next = next;
    }

    private boolean handleTrap(TrapData trapData, PDU trapPDU)
    {
        String entityTemp = "";
        String trapOID = trapData.getTrapOid();
        LOGGER.info("ItTrapBindVarProcessor trapoid:" + trapOID);
        TrapDescInfo trapDescInfo = TrapProcessorUtil.getTrapDescInfo(trapOID);
        trapData.setEventType(trapDescInfo.getEventType());
        trapData.setSeverityLevel(trapDescInfo.getAlarmSeverity());
        trapData.setAlarmCode(trapDescInfo.getAlarmCode());
        trapData.setReasonCode(trapDescInfo.getReasonCode());

        StringBuilder strEntity = new StringBuilder();
        strEntity.append(trapDescInfo.getEventEntity());
//        trapData.setEntity(trapDescInfo.getEventEntity());

        Map judgeBindOids = TrapProcessorUtil.getJudgeBindOidByRelateOid(trapOID);
        Map bindVar = TrapProcessorConfig.getBindInfoByOid(trapOID);
        Properties parsedBindValues = null;
        // parse by config file
        if (bindVar != null && bindVar.size() > 0)
        {
        	parsedBindValues = TrapProcessorUtil.parseBindingVar(trapPDU, bindVar);
            if (judgeBindOids != null && judgeBindOids.size() != 0)
            {
            	strEntity.append("@");
            	strEntity.append(TrapProcessorUtil.parseJudgeValueByBindVar(trapPDU, bindVar, judgeBindOids));
            }
        }

        // parse by mib file
        else
        {
        	parsedBindValues = TrapProcessorUtil.parseBindingVarByMibFiles(trapPDU);
            if (judgeBindOids != null && judgeBindOids.size() != 0)
            {
            	strEntity.append("@");
            	strEntity.append(TrapProcessorUtil.parseJudgeValueByMibFiles(trapPDU, judgeBindOids));
            }
        }
        trapData.setBindValues(parsedBindValues);
        trapData.setEntity(strEntity.toString());
		Object[] parsers = ExtensionAccess.getExtensions(ITrapParser.class.getName(), EXTENTISONID);
		for (Object parser : parsers) {
			((ITrapParser)parser).parser(trapData, trapPDU);
		}
		TrapProcessorUtil.sendTrapData(trapData);
        return true;
    }

}
