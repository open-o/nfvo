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
package org.openo.orchestrator.nfv.dac.snmptrap.processor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.openo.orchestrator.nfv.dac.common.util.filescan.FastFileSystem;
import org.openo.orchestrator.nfv.dac.snmptrap.entity.TrapConst;
import org.openo.orchestrator.nfv.dac.snmptrap.entity.TrapData;
import org.openo.orchestrator.nfv.dac.snmptrap.processor.entity.TrapDescInfo;
import org.openo.orchestrator.nfv.dac.snmptrap.processor.util.TrapProcessorConfig;
import org.openo.orchestrator.nfv.dac.snmptrap.processor.util.TrapProcessorUtil;
import org.openo.orchestrator.nfv.dac.snmptrap.util.TrapConfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;

public class TrapRestoreProcessor implements TrapProcessor
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TrapRestoreProcessor.class);

    private static final String trapRestorecfgFileName = "*-restore-trapdef.xml";

    //key = trap oid
    //vaule <restore trap oid, judge>
    private static HashMap trapRestoreMap = null;

    private TrapProcessor next;

    public TrapRestoreProcessor()
    {
    }

    public TrapRestoreProcessor(TrapProcessor next)
    {
        this.next = next;
    }

    @Override
    public boolean process(TrapData trapData, PDU trapPDU)
    {
        getTrapRestoreMap();
        ArrayList relateTrapInfo = getRelateTrapInfo(trapData.getTrapOid());
        if (relateTrapInfo != null)
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
        return next;
    }

    @Override
    public void setNext(TrapProcessor next)
    {
        this.next = next;
    }

    private boolean handleTrap(TrapData trapData, PDU trapPDU)
    {
        String restoreOID = trapData.getTrapOid();
        LOGGER.info("ItTrapBindVarProcessor trapoid:" + restoreOID);
        trapData.setEventType(TrapConst.EVENTTYPE_RESTORE);
        trapData.setSeverityLevel((byte) 0);
        trapData.setAlarmCode(0);
        trapData.setReasonCode(0);
        ArrayList relateTrapInfo = getRelateTrapInfo(restoreOID);
        Vector relateOid = (Vector) relateTrapInfo.get(0);
        Map judgeBindOid = (HashMap) relateTrapInfo.get(1);

        for (int i = 0; i < relateOid.size(); i++)
        {
            String trapOID = relateOid.get(i).toString();
            TrapDescInfo relTrapDescInfo = TrapProcessorUtil.getTrapDescInfo(trapOID);
            StringBuilder strEntity = new StringBuilder();
            strEntity.append(relTrapDescInfo.getEventEntity());
            Map bindVar = TrapProcessorConfig.getBindInfoByOid(trapOID);

            // parse by config file
            if (bindVar != null && bindVar.size() > 0)
            {
                if (judgeBindOid != null && judgeBindOid.size() != 0)
                {
                	strEntity.append("@");
                    strEntity.append(TrapProcessorUtil.parseJudgeValueByBindVar(trapPDU, bindVar, judgeBindOid));
                }
            }

            // parse by mib file
            else
            {
                if (judgeBindOid != null && judgeBindOid.size() != 0)
                {
                	strEntity.append("@");
                    strEntity.append(TrapProcessorUtil.parseJudgeValueByMibFiles(trapPDU, judgeBindOid));
                }
            }
            if (i > 0)
            {
            	trapData = cloneTrapData(trapData);
            }
            trapData.setAlarmCode(relTrapDescInfo.getAlarmCode());
            trapData.setReasonCode(relTrapDescInfo.getReasonCode());
            trapData.setEntity(strEntity.toString());
            TrapProcessorUtil.sendTrapData(trapData);
        }

        return true;
    }

    private void getTrapRestoreMap()
    {
        if (trapRestoreMap == null)
        {
            try
            {
                trapRestoreMap = new HashMap();
                parseDevMapFile();
            }
            catch (JDOMException e)
            {
                LOGGER.error("Deal file error -> " + trapRestorecfgFileName);
            }
            catch (IOException e)
            {
                LOGGER.error("Deal file error -> " + trapRestorecfgFileName);
            }
        }
    }

    private void parseDevMapFile() throws JDOMException, IOException
    {
        File[] files = FastFileSystem.getFiles(trapRestorecfgFileName);
        for (File file : files)
        {
	        Element elementRoot = TrapConfUtil.getElementFromXmlFile(file);
	        if (null == elementRoot)
	        {
	            LOGGER.warn(trapRestorecfgFileName + " :The elementRoot is null");
	            return;
	        }
	        List restoreRelations = elementRoot.getChildren();
	        if ((null == restoreRelations) || (restoreRelations.size() == 0))
	        {
	            return;
	        }

	        //<restore trapoid="xxxxx">
	        for (int i = 0; i < restoreRelations.size(); i++)
	        {
	            Element eleOidRelations = (Element) restoreRelations.get(i);
	            String trapOid = TrapConfUtil.getElementValue(eleOidRelations, "trapoid");
	            Map judgeOidsMap = new HashMap();
	            List judgeOids = eleOidRelations.getChildren("judge");
	            for (int j = 0; j < judgeOids.size(); j++)
	            {
	                Element judgeOid = (Element) judgeOids.get(j);
	                String judOid = TrapConfUtil.getElementValue(judgeOid, "bind-oid");
	                String judToken = TrapConfUtil.getElementValue(judgeOid, "token-value");
	                judgeOidsMap.put(judOid, judToken);
	            }

	            Vector vecRelOids = new Vector();
	            List relateOids = eleOidRelations.getChildren("relate");
	            for (int j = 0; j < relateOids.size(); j++)
	            {
	                Element relateOid = (Element) relateOids.get(j);
	                String relOid = TrapConfUtil.getElementValue(relateOid, "trap-oid");
	                vecRelOids.add(relOid);
	                TrapProcessorUtil.addRestoreTrap(relOid);
	                TrapProcessorUtil.addJudgeBindOid(relOid, judgeOidsMap);
	            }

	            ArrayList restoreTrapInfo = new ArrayList(2);
	            restoreTrapInfo.add(0, vecRelOids);
	            restoreTrapInfo.add(1, judgeOidsMap);

	            trapRestoreMap.put(trapOid, restoreTrapInfo);
	        }
        }
    }

    private ArrayList getRelateTrapInfo(String oid)
    {
        ArrayList relateTrapInfo = (ArrayList)trapRestoreMap.get(oid);
        if (relateTrapInfo == null)
        {
        	oid = TrapProcessorUtil.processOid(oid);
            relateTrapInfo = (ArrayList)trapRestoreMap.get(oid);
        }
        return relateTrapInfo;
    }

    private TrapData cloneTrapData(TrapData data)
    {
    	TrapData newTrapData = new TrapData();
    	newTrapData.setEventKey(data.getEventKey());
    	newTrapData.setIpAddress(data.getIpAddress());
    	newTrapData.setAlarmCode(data.getAlarmCode());
    	newTrapData.setReasonCode(data.getReasonCode());
    	newTrapData.setSystemType(data.getSystemType());
    	newTrapData.setTrapOid(data.getTrapOid());
    	newTrapData.setEventType(data.getEventType());
    	newTrapData.setSeverityLevel(data.getSeverityLevel());
    	return newTrapData;
    }
}
