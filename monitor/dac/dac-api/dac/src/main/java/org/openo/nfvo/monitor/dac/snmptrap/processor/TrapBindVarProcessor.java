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
package org.openo.nfvo.monitor.dac.snmptrap.processor;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openo.nfvo.monitor.dac.common.util.ExtensionUtil;
import org.openo.nfvo.monitor.dac.snmptrap.entity.TrapData;
import org.openo.nfvo.monitor.dac.snmptrap.processor.entity.AlarmMappingData;
import org.openo.nfvo.monitor.dac.snmptrap.processor.entity.BindOidData;
import org.openo.nfvo.monitor.dac.snmptrap.processor.entity.TrapMappingData;
import org.openo.nfvo.monitor.dac.snmptrap.processor.util.TrapProcessorConfig;
import org.openo.nfvo.monitor.dac.snmptrap.processor.util.TrapProcessorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.smi.VariableBinding;

/**
 * use bind value to seperate trap
 *
 */
public class TrapBindVarProcessor implements TrapProcessor {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TrapBindVarProcessor.class);
	private static final String KEY = "trap.bindvar";
	private TrapProcessor next;

	public TrapBindVarProcessor() {
	}

	public TrapBindVarProcessor(TrapProcessor next) {
		this.next = next;
	}

	@Override
	public boolean process(TrapData trapData, PDU trapPDU) {

		TrapMappingData trapMappingData = TrapProcessorUtil
				.getTrapMappingData(trapData.getTrapOid());
		if (null != trapMappingData) {
			return handleTrap(trapData, trapPDU);
		} else if (next != null) {
			return next.process(trapData, trapPDU);
		}
		return false;
	}

	@Override
	public TrapProcessor getNext() {
		return this.next;
	}

	@Override
	public void setNext(TrapProcessor next) {
		this.next = next;
	}

	private boolean handleTrap(TrapData trapData, PDU trapPDU) {
		String trapOID = trapData.getTrapOid();
		LOGGER.info("ItTrapBindVarProcessor trapoid:" + trapOID);
		TrapMappingData trapMappingData = TrapProcessorUtil
				.getTrapMappingData(trapOID);

		Hashtable bindDatas = trapMappingData.getBindDatas();
		Hashtable alarmMappingDatas = trapMappingData.getAlarmMappingDatas();
		List<BindOidData> bindOidDatas = new ArrayList<BindOidData>();
		List<VariableBinding> allVars = trapPDU.getVariableBindings();
		for (VariableBinding vb : allVars) {
			String oid = vb.getOid().toString();
			Iterator iter = bindDatas.keySet().iterator();
			while (iter.hasNext()) {
				String bindOid = iter.next().toString();
				String bindSyntax = trapMappingData.getByBindOid(bindOid)
						.getBindSyntax();
				if ((oid + ".").indexOf(bindOid + ".") != -1) {
					String value = vb.getVariable().toString().trim();
					bindOidDatas
							.add(new BindOidData(bindOid, value, bindSyntax));
					break;
				}
			}
		}

		boolean isFound = true;
		String bindValues = "";

		int sizeBindOid = bindOidDatas.size();
		if (sizeBindOid == 0) {
			return false;
		}
		BindOidData bodFirst = bindOidDatas.get(0);
		String bosFirst = bodFirst.getBindSyntax();
		String bovFirst = bodFirst.getBindValue();
		if (sizeBindOid == 1 && bosFirst.equalsIgnoreCase("String")
				&& trapMappingData.containsBindsValue(bovFirst)) {
			bindValues = bovFirst;
		} else {
			Iterator iter2 = alarmMappingDatas.keySet().iterator();
			while (iter2.hasNext()) {
				bindValues = iter2.next().toString();
				String[] bindValue = bindValues.split("/");
				int size = bindValue.length;
				if (size != bindOidDatas.size()) {
					return false;
				}
				for (int i = 0; i < bindOidDatas.size(); i++) {
					BindOidData bindOidData = bindOidDatas.get(i);
					String bindSyntax = bindOidData.getBindSyntax();
					String strValue = bindOidData.getBindValue();

					if (bindSyntax.equalsIgnoreCase("string")) {
						isFound = bindValue[i].equalsIgnoreCase(strValue);
					} else if (bindSyntax.equalsIgnoreCase("number")) {
						isFound = isMatched(strValue, bindValue[i]);
					}

					if (!isFound) {
						break;
					}
				}
				if (isFound) {
					break;
				}
			}
		}

		if (isFound) {
			AlarmMappingData alarmMappingData = trapMappingData
					.getByBindsValue(bindValues);

			trapData.setEventType(alarmMappingData.getEventType());
			trapData.setSeverityLevel(alarmMappingData.getAlarmSeverity());
			trapData.setAlarmCode(alarmMappingData.getAlarmCode());
			trapData.setReasonCode(alarmMappingData.getAlarmReasonCode());

			StringBuilder strEntity = new StringBuilder();
			strEntity.append(alarmMappingData.getEntity());
			List<String> entityOids = trapMappingData.getEntityOids();
			TrapProcessorUtil.processEntityOids(strEntity, entityOids, trapPDU);
			trapData.setEntity(strEntity.toString());

			Map bindVar = TrapProcessorConfig.getBindInfoByOid(trapOID);
			Properties parsedBindValues = null;
			if (bindVar != null && bindVar.size() > 0) {
				// parse by config file
				parsedBindValues = TrapProcessorUtil.parseBindingVar(trapPDU, bindVar);
			} else {
				// parse by mib file
				parsedBindValues = TrapProcessorUtil.parseBindingVarByMibFiles(trapPDU);
			}
			trapData.setBindValues(parsedBindValues);
			
			Object[] parsers = ExtensionUtil.getInstances(ITrapParser.EXTENSIONID, KEY);	
			for (Object parser : parsers) {			
				((ITrapParser)parser).parser(trapData, trapPDU);
			}
			TrapProcessorUtil.sendTrapData(trapData);
			return true;
		}

		return false;
	}

	private boolean isMatched(String bindValue, String bindExpression) {
		long longValue = Long.parseLong(bindValue);
		String[] strExps = bindExpression.split(",");
		for (int i = 0; i < strExps.length; i++) {
			String strExp = strExps[i];

			// 111
			if (strExp.indexOf("-") == -1) {
				if (Long.decode(strExp).intValue() == longValue) {
					return true;
				} else {
					continue;
				}
			}
			// 111-222
			else {
				String[] strInArr = strExps[i].split("-");
				if (longValue >= Long.decode(strInArr[0]).intValue()
						&& longValue <= Long.decode(strInArr[1]).intValue()) {
					return true;
				}
			}
		}
		return false;
	}
}
