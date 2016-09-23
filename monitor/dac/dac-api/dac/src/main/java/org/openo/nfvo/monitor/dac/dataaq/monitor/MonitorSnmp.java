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
package org.openo.nfvo.monitor.dac.dataaq.monitor;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openo.nfvo.monitor.dac.common.DacConst;
import org.openo.nfvo.monitor.dac.common.util.Calculator;
import org.openo.nfvo.monitor.dac.common.util.Crypto;
import org.openo.nfvo.monitor.dac.common.util.DaConfReader;
import org.openo.nfvo.monitor.dac.dataaq.common.DataAcquireException;
import org.openo.nfvo.monitor.dac.dataaq.common.ICollectorPara;
import org.openo.nfvo.monitor.dac.dataaq.common.IDataCollector;
import org.openo.nfvo.monitor.dac.dataaq.common.IDataParser;
import org.openo.nfvo.monitor.dac.dataaq.common.Monitor;
import org.openo.nfvo.monitor.dac.dataaq.common.MonitorException;
import org.openo.nfvo.monitor.dac.dataaq.datacollector.para.SnmpCollectorPara;
import org.openo.nfvo.monitor.dac.dataaq.dataparser.SnmpDataParser;
import org.openo.nfvo.monitor.dac.dataaq.monitor.bean.common.DaMonitorPerfInfo;
import org.openo.nfvo.monitor.dac.dataaq.monitor.bean.common.DaPerfCounterInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @version 1.0
 */

public class MonitorSnmp extends Monitor
{
	private static final Logger dMsg = LoggerFactory.getLogger(MonitorSnmp.class);
	
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Map perform(Map paras, IDataCollector dataCollector, IDataParser dataParser) throws DataAcquireException
 {
		String ip = (String) paras.get(DacConst.IPADDRESS);
		int port = Integer.parseInt((String) paras.get(DacConst.SNMPPORT));
		int version = Integer.parseInt((String) paras
				.get(DacConst.SNMPVERSION));
		String readcommunity = null;
		if (paras.get(DacConst.SNMPCOMMUNITY) != null) {
			readcommunity = (String) paras.get(DacConst.SNMPCOMMUNITY);
			if (!"".equals(readcommunity)) {
				readcommunity = Crypto.unencrypt(readcommunity);
			}
		}
		String writecommunity = null;
		if (paras.get(DacConst.SNMPWRITECOMMUNITY) != null) {
			writecommunity = (String) paras
					.get(DacConst.SNMPWRITECOMMUNITY);
			if (!"".equals(writecommunity)) {
				writecommunity = Crypto.unencrypt(writecommunity);
			}
		}
		String userName = null;
		if (paras.get(DacConst.SNMPUSERNAME) != null) {
			userName = (String) paras.get(DacConst.SNMPUSERNAME);
		}
		String securityName = null;
		if (paras.get(DacConst.SNMPSECURITYNAME) != null) {
			securityName = (String) paras.get(DacConst.SNMPSECURITYNAME);
		}
		int securityLevel = 1;
		if (paras.get(DacConst.SNMPSECURITYLEVEL) != null) {
			securityLevel = (Integer) paras.get(DacConst.SNMPSECURITYLEVEL);
		}
		int authProtocol = 1;
		if (paras.get(DacConst.SNMPAUTHPROTOCOL) != null) {
			authProtocol = (Integer) paras.get(DacConst.SNMPAUTHPROTOCOL);
		}
		String authPassword = null;
		if (paras.get(DacConst.SNMPAUTHPASSWORD) != null) {
			authPassword = (String) paras.get(DacConst.SNMPAUTHPASSWORD);
			if (!"".equals(authPassword)) {
				authPassword = Crypto.unencrypt(authPassword);
			}
		}
		String privPassword = null;
		if (paras.get(DacConst.SNMPPRIVPASSWORD) != null) {
			privPassword = (String) paras.get(DacConst.SNMPPRIVPASSWORD);
			if (!"".equals(privPassword)) {
				privPassword = Crypto.unencrypt(privPassword);
			}
		}
		String monitorName = (String) paras.get(DacConst.REALMONITORNAME);
		ICollectorPara para = new SnmpCollectorPara(ip, port, version,
				readcommunity, writecommunity, userName, securityName,
				securityLevel, authProtocol, authPassword, 
				privPassword);

		DaMonitorPerfInfo monitorInfo = DaConfReader.getInstance()
				.getMonitorParserMapInfo(monitorName);

		Map commandsMap = new HashMap();
		List perfCounterList = monitorInfo.getPerfCounters();
		for (Iterator iter = perfCounterList.iterator(); iter.hasNext();) {
			DaPerfCounterInfo perfCounterInfo = (DaPerfCounterInfo) iter.next();
			String requestOid = perfCounterInfo.getValue();
			String flag = String.valueOf(String.valueOf(perfCounterInfo.getnext()));
			commandsMap.put(requestOid, flag);
		}

		Map collectedData = null;
		try {
			collectedData = dataCollector.collectData(para, commandsMap);
		} catch (MonitorException e) {
			throw new DataAcquireException(e,
					DacConst.ERRORCODE_COLLECTERROR);
		}

		Map resultMap = new HashMap();
		perfCounterList = monitorInfo.getPerfCounters();
		SnmpDataParser snmpDataParser = (SnmpDataParser) dataParser;
		for (Iterator iter = perfCounterList.iterator(); iter.hasNext();) {
			DaPerfCounterInfo perfCounterInfo = (DaPerfCounterInfo) iter.next();
			String name = perfCounterInfo.getName();
			String requestOid = perfCounterInfo.getValue();

			Object percounterValue = snmpDataParser.parse(requestOid,
					collectedData);

			if (percounterValue == null) {
				dMsg.info("name:" + name + "perfCounterInfo.getValue():"
						+ perfCounterInfo.getValue());
				continue;
			}
			StringBuffer nameAppendOid = new StringBuffer();
			nameAppendOid.append(name).append("OIDAPPEND");
			Map tmpM = (Map) percounterValue;
			List tmpListValue = (List) (tmpM.get("VALUE"));

			List tmpVecAppendOid = (List) (tmpM.get("OIDAPPEND"));
			if (name.equals("IFNAME")) {
			    List list = tmpListValue;
			    List newList = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					String portName = (String) list.get(i);
					String newPortName = portName.replace('\'', '-');
					newPortName = newPortName.replaceAll(" ", "");
					newList.add(newPortName);
				}
				tmpListValue = newList;
			}
			if (name.equals("IFDESCR")) {// netscreen50,v5.0.0.r8 锟斤拷支锟斤拷IF-MIB锟斤拷
				List vec = tmpListValue;
				List newVec = new ArrayList();
				for (int i = 0; i < vec.size(); i++) {
					String portName = (String) vec.get(i);
					String newPortName = portName.replace('\'', '-');
					newPortName = newPortName.replaceAll(" ", "");
					newVec.add(newPortName);
				}
				tmpListValue = newVec;
			}
			resultMap.put(name, tmpListValue);

			resultMap.put(nameAppendOid.toString(), tmpVecAppendOid);

		}

		List nameis = monitorInfo.nameis;
		List valueis = monitorInfo.valueis;
		for (int i = 0, size = nameis.size(); i < size; i++) {
			String namei = (String) nameis.get(i);
			String valuei = (String) valueis.get(i);
			try {
				List result = new Calculator().calculate(resultMap, valuei);
				resultMap.put(namei, result);
			} catch (MonitorException e) {
				throw new DataAcquireException(e,
						DacConst.ERRORCODE_COLLECTERROR);
			}
		}
		resultMap = snmpDataParser.parse(taskInfo, paras, resultMap);
		return resultMap;
	}
}
