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
package org.openo.nfvo.monitor.dac.dataaq.datacollector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.openo.nfvo.monitor.dac.dataaq.common.ICollectorPara;
import org.openo.nfvo.monitor.dac.dataaq.common.IDataCollector;
import org.openo.nfvo.monitor.dac.dataaq.common.MonitorException;
import org.openo.nfvo.monitor.dac.dataaq.datacollector.para.SnmpCollectorPara;
import org.openo.nfvo.monitor.dac.dataaq.monitor.bean.common.MonitorTaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.smi.VariableBinding;

public class SnmpDataCollector implements IDataCollector {
    static final Logger LOGGER = LoggerFactory.getLogger(SnmpDataCollector.class);
    private MonitorTaskInfo taskInfo = null;
	@SuppressWarnings("rawtypes")
	@Override
	public Map collectData(ICollectorPara para, Map commands)
			throws MonitorException {
		Snmp snmp = null;
        HashMap<String, HashMap<String, Vector<String>>> result = new HashMap<String, HashMap<String, Vector<String>>>();
		SnmpCollectorPara snmpPara = (SnmpCollectorPara) para;
		int version = snmpPara.getVersion();
		try {
			snmp = SnmpDataCollectorHelper.getSnmp(version, snmpPara);
			Target target = SnmpDataCollectorHelper.getTarget(version, snmpPara);
	        String requestOid = null;
	        for (Iterator iter = commands.keySet().iterator(); iter.hasNext();)
			{
				requestOid = (String) iter.next();
				String flag = (String) commands.get(requestOid);
				boolean getNext = false;
				if (flag.trim().equals("true"))
				{
					getNext = true;
				}
				Vector<String> oid_result = new Vector<String>();
				Vector<String> oid_appended = new Vector<String>();
				HashMap<String, Vector<String>> tmpresult = new HashMap<String, Vector<String>>();
				Vector<VariableBinding> bindValues;
				if (getNext)
				{
					PDU pdu = null;
					if (version != SnmpConstants.version1)
					{
						pdu = SnmpDataCollectorHelper.createGetBulkPdu(requestOid, version);
					}
					else
					{
						pdu = SnmpDataCollectorHelper.createGetNextPdu(requestOid, version);
					}
					bindValues = SnmpDataCollectorHelper.sendRequest(requestOid + ".", pdu, snmp, target);
				}
				else
				{
					bindValues = SnmpDataCollectorHelper.sendRequest(SnmpDataCollectorHelper.createGetPdu(requestOid, version), snmp, target);
				}
				if (bindValues != null && bindValues.size() != 0)
				{
					for (VariableBinding bindValue : bindValues)
					{
						String value = bindValue.getVariable().toString();
						if (value.equals("noSuchInstance") || value.equals("noSuchObject"))
						{
							value = "-1";
						}
						oid_result.add(value);
						String part = bindValue.getOid().toString().replaceFirst(requestOid, "");
						part = part.replaceFirst(".", "");
						oid_appended.add(part);
					}
					tmpresult.put("VALUE", oid_result);
					tmpresult.put("OIDAPPEND", oid_appended);
					result.put(requestOid, tmpresult);
				}
			}
	        
	        
		} catch (IOException e) {
			LOGGER.warn("TaskId=" + taskInfo.getJobId() + ":SnmpDataCollector failed!" + e.getMessage());
		}  
		finally
		{
			try {
				snmp.close();
			} catch (IOException e) {
				LOGGER.warn("DefaultUdpTransportMapping close failed", e);
			}
		}
        return result;
	}
	
    
//    public static void main(String[] args)
//    {
//    	SnmpCollectorPara para = new SnmpCollectorPara();
//    	para.setVersion(SnmpConstants.version2c);
//    	para.setHostNameOrIp("10.74.164.23");
//    	para.setReadCommunity("public");
//    	para.setPort(161);
//    	para.setAuthPassword("1234567890abcdef");
//    	para.setAuthProtocol(1);
//    	para.setSecurityLevel(SecurityLevel.AUTH_PRIV);
//    	para.setPrivPassword("1234567890abcdef");
//    	para.setUserName("user4");
//    	para.setSecurityName("user4");
//    	Map commands = new HashMap();
//    	commands.put("1.3.6.1.2.1.2.2.1.13", "true");
////    	commands.put("1.3.6.1.2.1.2.2.1.2", "true");
//    	SnmpDataCollector collector = new SnmpDataCollector();
//    	try {
//			collector.collectData(para, commands);
//		} catch (MonitorException e) {
//			 System.out.println("Error:" + e.getMessage()); 
//		}
//    }


	@Override
	public void setMonitorTaskInfo(MonitorTaskInfo monitorTaskInfo) {
		taskInfo = monitorTaskInfo;
	}
}
