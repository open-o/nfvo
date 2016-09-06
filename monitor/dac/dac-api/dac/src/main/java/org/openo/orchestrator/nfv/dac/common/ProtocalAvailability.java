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
package org.openo.orchestrator.nfv.dac.common;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.openo.orchestrator.nfv.dac.common.util.GeneralFileLocaterImpl;
import org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common.MonitorTaskInfo;
import org.openo.orchestrator.nfv.dac.dataaq.scheduler.MonitorTask;
import org.openo.orchestrator.nfv.dac.snmptrap.entity.TrapConst;
import org.openo.orchestrator.nfv.dac.snmptrap.entity.TrapData;
import org.openo.orchestrator.nfv.dac.snmptrap.processor.util.TrapProcessorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProtocalAvailability
{
	 private static final Logger dMsg = LoggerFactory.getLogger(MonitorTask.class);

    private static final byte severity = 1;

    private static final int systemType = 20104;

    private static ConcurrentHashMap<String, PAInfo> paInfoMap = new ConcurrentHashMap<String, PAInfo>();
	private HashMap<String, Integer> protocalCodeMap = new HashMap<String, Integer>();
	private HashSet<String> availablityMonitors = new HashSet<String>();
    private static ProtocalAvailability pa = new ProtocalAvailability();
    
    private ProtocalAvailability(){
    	try {
			loadXML();
		} catch (Exception e) {
			dMsg.warn("Load protocalCode map file failed:" + e.getMessage());
		} 
    }
    public static ProtocalAvailability getInstance(){
        return pa;
    }
    public synchronized void add(MonitorTaskInfo monitorTaskInfo)
    {
    	String neOid  = (String) monitorTaskInfo.getMonitorProperty(DacConst.RESID);
    	if (!paInfoMap.containsKey(neOid))
    	{
            paInfoMap.put(neOid, new PAInfo(true));
    	}
    }

    public synchronized void remove(MonitorTaskInfo monitorTaskInfo)
    {
    	String neOid  = (String) monitorTaskInfo.getMonitorProperty(DacConst.RESID);
    	if (paInfoMap.containsKey(neOid))
    	{
    		paInfoMap.remove(neOid);
    	}
    }

    public void connectSucc(MonitorTaskInfo monitorTaskInfo)
    {
    	if (isAvailabilityMonitor(monitorTaskInfo))
    	{
	        String neOid  = (String) monitorTaskInfo.getMonitorProperty(DacConst.RESID);
	        if (paInfoMap.containsKey(neOid))
	        {
	            PAInfo paInfo = paInfoMap.get(neOid);
	            synchronized (paInfo)
	            {
	                paInfo.setAvailability(true);
	            }
	            sendAlarm(TrapConst.EVENTTYPE_RESTORE, monitorTaskInfo);
	        }
    	}
    }

    public void connectFail(MonitorTaskInfo monitorTaskInfo)
    {
    	if (isAvailabilityMonitor(monitorTaskInfo))
    	{
	    	String neOid  = (String) monitorTaskInfo.getMonitorProperty(DacConst.RESID);
	        if (paInfoMap.containsKey(neOid))
	        {
	            PAInfo paInfo = paInfoMap.get(neOid);
	            synchronized (paInfo)
	            {
	                if (paInfo.isAvailability())
	                {
	                    paInfo.setAvailability(false);
	                }
	            }
	
	            sendAlarm(TrapConst.EVENTTYPE_ALARM, monitorTaskInfo);
	        }
    	}
    }
    

    private void sendAlarm(byte eventType, MonitorTaskInfo monitorTaskInfo)
    {
        TrapData alarmData = new TrapData();
        String neOid = (String) monitorTaskInfo.getMonitorProperty(DacConst.RESID);
        String ip = (String) monitorTaskInfo.getMonitorProperty(DacConst.IPADDRESS);
        String neType = (String) monitorTaskInfo.getMonitorProperty(DacConst.NETYPEID);
        int alarmCode = providerToCode(monitorTaskInfo.getProvider());
        alarmData.setIpAddress(ip);
        alarmData.setNeType(neType);
        alarmData.setAlarmCode(alarmCode);
        alarmData.setSeverityLevel(severity);
        alarmData.setEventType(eventType);
        alarmData.setNeId(neOid);
        alarmData.setSystemType(systemType);
        StringBuilder alarmKey = new StringBuilder(neOid);
        alarmKey.append("@");
        alarmKey.append(systemType);
        alarmKey.append("@");
        alarmKey.append(alarmCode);
        alarmData.setEntity(alarmKey.toString());
        TrapProcessorUtil.sendTrapData(alarmData);
    }
    
    /**
     * @param provider (TELNET,SSH,SNMP,JDBC,WMI,HTTP)
     * @return
     */
    private int providerToCode(String provider)
    {
    	return protocalCodeMap.get(provider);

    }

    
	private void loadXML() throws JDOMException, IOException
	{
		Element elementRoot = null;
		List codeMaps = null;
		Element codeMap = null;
		String protocal = null;
		String alarmCode = null;
		elementRoot = getElementFromXmlFile("provider-availability-code.xml");

		if (null == elementRoot)
		{
			dMsg.warn("provider-availability-code.xml");
			return;
		}

		codeMaps = elementRoot.getChildren();
		if ((null == codeMaps) || (codeMaps.size() == 0))
		{
			return;
		}

		for (int num = 0; num < codeMaps.size(); num++)
		{
			codeMap = (Element) codeMaps.get(num);
			protocal = codeMap.getAttributeValue("protocal");
			alarmCode = codeMap.getAttributeValue("alarmCode");
			protocalCodeMap.put(protocal, Integer.parseInt(alarmCode));
		}
	}
	
	private Element getElementFromXmlFile(String fileName) throws JDOMException, IOException
	{

		File file = GeneralFileLocaterImpl.getGeneralFileLocater().getFile(DacConst.SYSTEMDIR, fileName);
		if (file == null)
		{
			dMsg.warn(fileName + "XmlFile:file is null");
			return null;
		}

		SAXBuilder saxBuilder = new SAXBuilder(false);
		Document doc = null;

		doc = saxBuilder.build(file);

		return doc.getRootElement();
	}
	
    public void addAvailabilityMonitor(String monitor)
    {
    	availablityMonitors.add(monitor);
    }
    
    private boolean isAvailabilityMonitor(MonitorTaskInfo monitorTaskInfo)
    {
    	String monitorName  = (String) monitorTaskInfo.getMonitorProperty(DacConst.REALMONITORNAME);
    	return availablityMonitors.contains(monitorName);
    }
}


class PAInfo
{
    boolean availability;
    PAInfo(boolean availability)
    {
        this.availability = availability;
    }
    /**
     * @return the oid
     */
    public boolean isAvailability()
    {
        return this.availability;
    }
    /**
     * @param oid the oid to set
     */
    public void setAvailability(boolean availability)
    {
        this.availability = availability;
    }

}
