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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openo.nfvo.monitor.dac.dataaq.common.DataCollector;
import org.openo.nfvo.monitor.dac.dataaq.common.ICollectorPara;
import org.openo.nfvo.monitor.dac.dataaq.common.MonitorException;
import org.openo.nfvo.monitor.dac.dataaq.datacollector.para.SnmpCollectorPara;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.VariableBinding;

public class SnmpDataCollector extends DataCollector {

    static final Logger LOGGER = LoggerFactory.getLogger(SnmpDataCollector.class);
	
	@SuppressWarnings("rawtypes")
	@Override
	public Map collectData(ICollectorPara para, Map commands)
			throws MonitorException {
		Snmp snmp = null;
        HashMap<String, HashMap<String, List<String>>> result = new HashMap<String, HashMap<String, List<String>>>();
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
				List<String> oid_result = new ArrayList<String>();
				List<String> oid_appended = new ArrayList<String>();
				HashMap<String, List<String>> tmpresult = new HashMap<String, List<String>>();
				List<VariableBinding> bindValues;
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
}
