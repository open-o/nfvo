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
package org.openo.nfvo.monitor.dac.snmptrap.processor.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.openo.nfvo.monitor.dac.common.util.filescan.FastFileSystem;
import org.openo.nfvo.monitor.dac.snmptrap.processor.entity.AlarmMappingData;
import org.openo.nfvo.monitor.dac.snmptrap.processor.entity.BindOidData;
import org.openo.nfvo.monitor.dac.snmptrap.processor.entity.TrapBindInfo;
import org.openo.nfvo.monitor.dac.snmptrap.processor.entity.TrapDescInfo;
import org.openo.nfvo.monitor.dac.snmptrap.processor.entity.TrapMappingData;
import org.openo.nfvo.monitor.dac.snmptrap.util.TrapConfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * parse trap processor config file

 */
public class TrapProcessorConfig
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TrapProcessorConfig.class);

    private static HashMap<String, TrapDescInfo> trapOidDescInfoCacheMap = null;
    private static HashMap<String, HashMap> bindOidInfoCacheMap = null;

    private static final String TRAP_BIND_INFO = "*-trap-bind-info.xml";
    private static final String TRAP_ALARM_INFO = "*-private-trapdef.xml";
    private static final String specialTrapdefFileName = "*-special-trapdef.xml";
    // key - trapOid value - TrapMappingData
    private static HashMap spTrapCfgMap = null;

    static
    {
        getAllTrapInfo();
        getQueryBind();
        getSpTrapCfgMap();
    }

    private static void getAllTrapInfo()
    {
        try
        {
            trapOidDescInfoCacheMap = new HashMap<String, TrapDescInfo>();
            File[] files = FastFileSystem.getFiles(TRAP_ALARM_INFO);
            for (File file : files)
            {
	            Element rootElemt = TrapConfUtil.getElementFromXmlFile(file);
	            List<Element> alarmInfos = rootElemt.getChildren();
	            for (Element info : alarmInfos)
	            {
	                byte eventType = 1;
	                long alarmCode = info.getAttribute("ALARMCODE").getLongValue();
	                long reasonCode = info.getAttribute("REASONCODE").getLongValue();
	                String trapOid = info.getAttribute("TRAPOID").getValue();
	                String eventEntity = info.getAttribute("EVENTENTITY").getValue();
	                byte alarmSeverity = Byte.parseByte(info.getAttribute("ALARMSEVERITY").getValue());

	                if (info.getAttribute("EVENTTYPE") != null)
	                {
	                    eventType = Byte.parseByte(info.getAttribute("EVENTTYPE").getValue());
	                }
	                TrapDescInfo trapDescInfo =
	                                            new TrapDescInfo(eventType, alarmCode, alarmSeverity, reasonCode, trapOid,
	                                                    eventEntity);
	                trapOidDescInfoCacheMap.put(trapOid, trapDescInfo);
	            }
            }
        }
        catch (JDOMException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static TrapDescInfo getTrapDescInfo(String strOid)
    {
        TrapDescInfo trapDescInfo = trapOidDescInfoCacheMap.get(strOid);
        return trapDescInfo;
    }


    private static void getQueryBind()
    {
        try
        {
            bindOidInfoCacheMap = new HashMap<String, HashMap>();
            File[] files = FastFileSystem.getFiles(TRAP_BIND_INFO);
            for (File file : files)
            {
	            Element rootElemt = TrapConfUtil.getElementFromXmlFile(file);
	            List<Element> bindInfos = rootElemt.getChildren();
	            for (Element info : bindInfos)
	            {
	                String trapOid = info.getAttribute("TRAPOID").getValue().trim();
	                String bindOid = info.getAttribute("BINDOID").getValue().trim();
	                String bindDesc = info.getAttribute("BINDDESC").getValue().trim();
	                String formulae = info.getAttribute("FORMULA").getValue().trim();
	                String valueDesc = info.getAttribute("VALUEDESC").getValue().trim();
	                TrapBindInfo bindInfo = new TrapBindInfo(trapOid, bindOid, bindDesc, formulae, valueDesc);

	                HashMap<String, HashMap<String, TrapBindInfo>> bindDescMap = bindOidInfoCacheMap.get(trapOid);
	                if (bindDescMap == null)
	                {
	                    bindDescMap = new HashMap<String, HashMap<String, TrapBindInfo>>();
	                    bindOidInfoCacheMap.put(trapOid, bindDescMap);
	                }

	                HashMap<String, TrapBindInfo> temp = (HashMap<String, TrapBindInfo>) bindDescMap.get(bindOid);
	                if (temp == null)
	                {
	                    temp = new HashMap<String, TrapBindInfo>();
	                    bindDescMap.put(bindOid, temp);
	                }

	                temp.put(bindInfo.getBindFormula(), bindInfo);
	            }
            }
        }
        catch (JDOMException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static HashMap getBindInfoByOid(String strOid)
    {
        HashMap returnInfos = new HashMap<String, List<?>>();

        HashMap bindInfos = bindOidInfoCacheMap.get(strOid);
        if (bindInfos != null && bindInfos.size() > 0)
        {
            Iterator bindKeyIte = bindInfos.keySet().iterator();
            while (bindKeyIte.hasNext())
            {
                String bindOid = (String) bindKeyIte.next();
                HashMap bindValue = (HashMap) bindInfos.get(bindOid);

                Collection collection = bindValue.values();
                List infoList = new ArrayList();
                infoList.addAll(collection);
                returnInfos.put(bindOid, infoList);
            }
        }
        return returnInfos;
    }


    private static void getSpTrapCfgMap()
    {
        if (spTrapCfgMap == null)
        {
            try
            {
                spTrapCfgMap = new HashMap();
                parseSpecialTrapFile();
            }
            catch (JDOMException e)
            {
                LOGGER.error("Deal file error -> " + specialTrapdefFileName);
            }
            catch (IOException e)
            {
                LOGGER.error("Deal file error -> " + specialTrapdefFileName);
            }
        }
    }

    public static TrapMappingData getTrapMappingData(String strOid)
    {
        TrapMappingData trapMappingData = (TrapMappingData)spTrapCfgMap.get(strOid);
        return trapMappingData;
    }

    private static void parseSpecialTrapFile() throws JDOMException, IOException
    {
        File[] files = FastFileSystem.getFiles(specialTrapdefFileName);
        for (File file : files)
        {
	        Element elementRoot = TrapConfUtil.getElementFromXmlFile(file);
	        if (null == elementRoot)
	        {
	            LOGGER.warn(specialTrapdefFileName + " :The elementRoot is null");
	            return;
	        }
	        List trapMappingList = elementRoot.getChildren();
	        if ((null == trapMappingList) || (trapMappingList.size() == 0))
	        {
	            return;
	        }

	        for (int i = 0; i < trapMappingList.size(); i++)
	        {
	            TrapMappingData trapMappingData;
	            Element trapMapping = (Element) trapMappingList.get(i);

	            Element eleAlarmMapping = trapMapping.getChild("alarm-mapping");
	            List<Element> alarmMappingList = eleAlarmMapping.getChildren("alarmcfg");
	            Hashtable alarmMappingDatas = new Hashtable();
	            for (Element eleAlarmMappingData : alarmMappingList)
	            {
	                long alarmCode = Long.parseLong(TrapConfUtil.getElementValue(eleAlarmMappingData, "alarmcode"));
	                long alarmReasonCode =
	                                       Long.parseLong(TrapConfUtil.getElementValue(eleAlarmMappingData,
	                                           "alarmreasoncode"));
	                byte alarmSeverity = Byte.parseByte(TrapConfUtil.getElementValue(eleAlarmMappingData, "alarmseverity"));
	                String entity = TrapConfUtil.getElementValue(eleAlarmMappingData, "entity");
	                byte eventType = Byte.parseByte(TrapConfUtil.getElementValue(eleAlarmMappingData, "eventtype"));
	                String bindsValue = TrapConfUtil.getElementValue(eleAlarmMappingData, "bindsvalue");// ��ֵ,��ݴ�ֵȷ���澯
	                AlarmMappingData amData =
	                                          new AlarmMappingData(alarmCode, alarmReasonCode, alarmSeverity, eventType,
	                                                  entity, bindsValue);
	                alarmMappingDatas.put(amData.getBindsValue(), amData);
	            }


	            List entityOidList = trapMapping.getChildren("entityOid");
	            List<String> entityOids = new ArrayList<String>();
	            for (int m = 0; m < entityOidList.size(); m++)
	            {
	                Element entityOid = (Element) entityOidList.get(m);
	                entityOids.add(entityOid.getAttributeValue("value"));
	            }

	            List trapBindOidList = trapMapping.getChildren("trapOid");
	            for (int k = 0; k < trapBindOidList.size(); k++)
	            {
	                Element trapBindOid = (Element) trapBindOidList.get(k);
	                String trapOid = trapBindOid.getAttributeValue("value");
	                List bindOids = trapBindOid.getChildren("bindOid");
	                Hashtable bindDatas = new Hashtable();
	                for (int j = 0; j < bindOids.size(); j++)
	                {
	                    Element eleBindOid = (Element) bindOids.get(j);
	                    String relOid = TrapConfUtil.getElementValue(eleBindOid, "value");// .1.3.6.1.4.1.1981.1.4.5
	                    String syntax = TrapConfUtil.getElementValue(eleBindOid, "syntax");// string
	                    bindDatas.put(relOid, new BindOidData(relOid, syntax));
	                }
	                trapMappingData = new TrapMappingData(trapOid, bindDatas, alarmMappingDatas, entityOids);
	                spTrapCfgMap.put(trapOid, trapMappingData);
	            }
	        }
        }
    }
}
