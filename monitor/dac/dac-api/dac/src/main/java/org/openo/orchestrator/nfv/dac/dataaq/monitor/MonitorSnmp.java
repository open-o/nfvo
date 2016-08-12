package org.openo.orchestrator.nfv.dac.dataaq.monitor;

/**
 * <p>
 * �ļ����: CPUMonitorSnmp.java
 * </p>
 * <p>
 * �ļ�����:
 * </p>
 * <p>
 * ��Ȩ����: ��Ȩ����(C)2001-2005
 * </p>
 * <p>
 * �� ˾: ����������ͨѶ�ɷ����޹�˾
 * </p>
 * <p>
 * ����ժҪ: ��
 * </p>
 * <p>
 * ����˵��: ��
 * </p>
 * <p>
 * �������ڣ�2005-11-9
 * </p>
 * <p>
 * ������ڣ�2005-11-9
 * </p>
 * <p>
 * �޸ļ�¼1: // �޸���ʷ��¼�������޸����ڡ��޸��߼��޸�����
 * </p>
 * 
 * <pre>
 *    �޸����ڣ�
 *    �� �� �ţ�
 *    �� �� �ˣ�
 *    �޸����ݣ�
 * </pre>
 * <p>
 * �޸ļ�¼2����
 * </p>
 * 
 * @version 1.0
 * @author it
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.openo.orchestrator.nfv.dac.common.DacConst;
import org.openo.orchestrator.nfv.dac.common.util.Calculator;
import org.openo.orchestrator.nfv.dac.common.util.Crypto;
import org.openo.orchestrator.nfv.dac.common.util.DaConfReader;
import org.openo.orchestrator.nfv.dac.dataaq.common.DataAcquireException;
import org.openo.orchestrator.nfv.dac.dataaq.common.ICollectorPara;
import org.openo.orchestrator.nfv.dac.dataaq.common.IDataCollector;
import org.openo.orchestrator.nfv.dac.dataaq.common.IDataParser;
import org.openo.orchestrator.nfv.dac.dataaq.common.IMonitor;
import org.openo.orchestrator.nfv.dac.dataaq.common.MonitorException;
import org.openo.orchestrator.nfv.dac.dataaq.datacollector.para.SnmpCollectorPara;
import org.openo.orchestrator.nfv.dac.dataaq.dataparser.SnmpDataParser;
import org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common.DaMonitorPerfInfo;
import org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common.DaPerfCounterInfo;
import org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common.MonitorTaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author it
 * @version 1.0
 */
public class MonitorSnmp implements IMonitor
{
	private static final Logger dMsg = LoggerFactory.getLogger(MonitorSnmp.class);
    private MonitorTaskInfo taskInfo = null;

	public MonitorSnmp(MonitorTaskInfo taskInf)
	{
		taskInfo = taskInf;
	}
	
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
			Vector tmpVecValue = (Vector) (tmpM.get("VALUE"));

			Vector tmpVecAppendOid = (Vector) (tmpM.get("OIDAPPEND"));
			if (name.equals("IFNAME")) {
				Vector vec = (Vector) tmpVecValue;
				Vector newVec = new Vector();
				for (int i = 0; i < vec.size(); i++) {
					String portName = (String) vec.get(i);
					String newPortName = portName.replace('\'', '-');
					newPortName = newPortName.replaceAll(" ", "");
					newVec.add(newPortName);
				}
				tmpVecValue = newVec;
			}
			if (name.equals("IFDESCR")) {// netscreen50,v5.0.0.r8 ��֧��IF-MIB��
				Vector vec = (Vector) tmpVecValue;
				Vector newVec = new Vector();
				for (int i = 0; i < vec.size(); i++) {
					String portName = (String) vec.get(i);
					String newPortName = portName.replace('\'', '-');
					newPortName = newPortName.replaceAll(" ", "");
					newVec.add(newPortName);
				}
				tmpVecValue = newVec;
			}
			resultMap.put(name, tmpVecValue);

			resultMap.put(nameAppendOid.toString(), tmpVecAppendOid);

		}

		Vector nameis = monitorInfo.nameis;
		Vector valueis = monitorInfo.valueis;
		for (int i = 0, size = nameis.size(); i < size; i++) {
			String namei = (String) nameis.get(i);
			String valuei = (String) valueis.get(i);
			try {
				Vector result = new Calculator().calculate(resultMap, valuei);
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
