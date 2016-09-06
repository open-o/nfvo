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
package org.openo.orchestrator.nfv.dac.dataaq.scheduler;

import java.util.Date;
import java.util.Map;
import java.util.TimerTask;
import java.util.Vector;

import org.openo.orchestrator.nfv.dac.common.DacConst;
import org.openo.orchestrator.nfv.dac.common.ProtocalAvailability;
import org.openo.orchestrator.nfv.dac.common.util.DaConfReader;
import org.openo.orchestrator.nfv.dac.common.util.DacUtil;
import org.openo.orchestrator.nfv.dac.common.util.ExtensionAccess;
import org.openo.orchestrator.nfv.dac.dataaq.common.DataAcquireException;
import org.openo.orchestrator.nfv.dac.dataaq.common.IDataCollector;
import org.openo.orchestrator.nfv.dac.dataaq.common.IDataParser;
import org.openo.orchestrator.nfv.dac.dataaq.common.IMonitor;
import org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common.MonitorTaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class inherits the TimerTask used to perform specific monitoring tasks.
 */
public class MonitorTask extends TimerTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(MonitorTask.class);

	private MonitorTaskInfo monitorTaskInfo = null;
	private String osType = "";
	private String monitorName = null;
	private String version = "0";
	private Date reportTime;

	public MonitorTask(MonitorTaskInfo monitorTaskInfo) {
		this.monitorTaskInfo = monitorTaskInfo;
		if (monitorTaskInfo.getMonitorProperty(DacConst.VERSION) != null) {
			version = (String) monitorTaskInfo.getMonitorProperty(DacConst.VERSION);
		}
		monitorName = (String) monitorTaskInfo.getMonitorProperty(DacConst.MONITORNAME);
		osType = (String) monitorTaskInfo.getMonitorProperty(DacConst.HOSTTYPE);
	}

	@Override
	public void run() {
		long timerRunStart = System.currentTimeMillis();
		Map<String, Vector<String>> result;

		try {
			result = perform();
			ProtocalAvailability.getInstance().connectSucc(monitorTaskInfo);
		} catch (Exception e) {
			ProtocalAvailability.getInstance().connectFail(monitorTaskInfo);
			LOGGER.error("JobId: " + monitorTaskInfo.getJobId() + " Gathering Data Failed. " + e.getMessage()
					+ " Cached Message: " + monitorTaskInfo.cachedMessage, e);
			return;
		}
		Vector<Object> vPara = new Vector<>();
		vPara.add(this.monitorTaskInfo.getJobId());
		vPara.add(getReportTime());
		vPara.add(this.monitorTaskInfo.getGranularity());
		vPara.add(this.monitorTaskInfo.getColumnName());
		vPara.add(result);
		DacUtil.putDataMsg(vPara);
		long timerRunEnd = System.currentTimeMillis();
		LOGGER.debug("JobId: " + monitorTaskInfo.getJobId() + "; MonitorTask---timerRunPeriod = "
				+ (timerRunEnd - timerRunStart));
	}

	/**
	 * Perform data collection
	 *
	 * @return Data acquisition result
	 * @throws DataAcquireException
	 */

	public Map<String, Vector<String>> perform() throws DataAcquireException {
		String neTypeId = (String) monitorTaskInfo.getMonitorProperty(DacConst.NETYPEID);

		String ip = (String) monitorTaskInfo.getMonitorProperty(DacConst.IPADDRESS);
		String provider = monitorTaskInfo.getProvider();

		if (provider.equals(DacConst.SSH)) {
			provider = DacConst.TELNET;
		}

		String startTime = DacUtil.timeFormat(new Date());
		Map<String, Vector<String>> result;
		// SNMP:INOUTSTATISTICS::
		String nowMonitorName = assembleRealMonitorName(provider, monitorName, "", "");
		if (DaConfReader.getInstance().getMonitorParserMapInfo(nowMonitorName) == null) {
			// SNMP:CPU:ZTESWITCH:14
			nowMonitorName = assembleRealMonitorName(provider, monitorName, osType, version);
			if (DaConfReader.getInstance().getMonitorParserMapInfo(nowMonitorName) == null) {
				if (version != null && !version.equals("0")) {
					// SNMP:CPU:ZTESWITCH:0
					nowMonitorName = assembleRealMonitorName(provider, monitorName, osType, "0");
					if (DaConfReader.getInstance().getMonitorParserMapInfo(nowMonitorName) == null) {
						LOGGER.info("There's no such real monitor name as " + nowMonitorName + ", skip.");
						throw new DataAcquireException(DacConst.ERRORCODE_PROVIDERS,
								"There's no such real monitor name as " + nowMonitorName);
					}
				} else {
					LOGGER.info("There's no such real monitor name as " + nowMonitorName + ", skip.");
					throw new DataAcquireException(DacConst.ERRORCODE_PROVIDERS,
							"There's no such real monitor name as " + nowMonitorName);
				}

			}
		}

		monitorTaskInfo.addMonitorProperty(DacConst.REALMONITORNAME, nowMonitorName);
		IDataCollector dataCollector = createDataCollector(neTypeId);
		IDataParser dataParser = createDataParser(neTypeId);
		IMonitor monitor = createMonitor(neTypeId, monitorTaskInfo);
		Map<Object, Object> paras = monitorTaskInfo.getMonitorProperty();

		result = monitor.perform(paras, dataCollector, dataParser);// Perform
																	// data
																	// collection
		if (result == null) {
			LOGGER.info("JobId: " + monitorTaskInfo.getJobId() + " execution " + startTime + " aborted.");
			throw new DataAcquireException(DacConst.ERRORCODE_PROVIDERS,
					"data acquire [" + ip + "] fail. monitorName=" + monitorName);
		}

		LOGGER.info("JobId: " + monitorTaskInfo.getJobId() + " execution " + startTime + " completed.");
		return result;
	}

	/**
	 * DataCollector instance is defined in file:
	 * conf\extend\[device]-extendsimpl.xml if not defined in file, return null.
	 *
	 */
	private IDataCollector createDataCollector(String neTypeId) {
		String extensionID = IDataCollector.class.getName();
		Object dataCollector = ExtensionAccess.getExtension(extensionID, monitorName);
		if (dataCollector == null) {
			dataCollector = (IDataCollector) ExtensionAccess.getExtension(extensionID,
					getQueryId(neTypeId, monitorName));
			if (dataCollector == null) {
				dataCollector = (IDataCollector) ExtensionAccess.getExtension(extensionID, neTypeId);
				if (dataCollector == null) {
					return null;
				}
			}
		}
		((IDataCollector) dataCollector).setMonitorTaskInfo(monitorTaskInfo);
		return (IDataCollector) dataCollector;
	}

	/**
	 * DataParser instance is defined in file:
	 * conf\extend\[device]-extendsimpl.xml if not defined in file, return null.
	 *
	 * @param provider
	 *            Data acquisition protocol
	 * @return data parser
	 * @throws DataAcquireException
	 */
	private IDataParser createDataParser(String neTypeId) throws DataAcquireException {
		String extensionID = IDataParser.class.getName();
		Object dataParser = ExtensionAccess.getExtension(extensionID, monitorName);

		if (dataParser == null) {
			dataParser = (IDataParser) ExtensionAccess.getExtension(extensionID, getQueryId(neTypeId, monitorName));
			if (dataParser == null) {
				dataParser = (IDataParser) ExtensionAccess.getExtension(extensionID, neTypeId);
				if (dataParser == null) {
					return null;
				}
			}
		}
		return (IDataParser) dataParser;

	}

	/**
	 * Monitor instance is defined in file: conf\extend\[device]-extendsimpl.xml
	 * get monitor instance by monitorName, if null then try get it by
	 * neTypeId.monitorName, last by neTypeId. if still null, you should check your
	 * [device]-extendsimpl.xml file definition correct. This method should not
	 * return null.
	 *
	 * @param neTypeId
	 * @param monitorName
	 * @param taskInf
	 * @return monitor
	 * @throws DataAcquireException
	 */
	private IMonitor createMonitor(String neTypeId, MonitorTaskInfo taskInf) {
		IMonitor monitor = null;
		String extensionID = IMonitor.class.getName();

		Class<?> monitorClass = ExtensionAccess.getExtensionClass(extensionID, monitorName);
		if (monitorClass == null) {
			monitorClass = ExtensionAccess.getExtensionClass(extensionID, getQueryId(neTypeId, monitorName));
			if (monitorClass == null) {
				monitorClass = ExtensionAccess.getExtensionClass(extensionID, neTypeId);
				if (monitorClass == null) {
					return null;
				}
			}
		}

		try {
			monitor = (IMonitor) monitorClass.getConstructor(MonitorTaskInfo.class).newInstance(taskInf);
		} catch (Exception ex) {
			LOGGER.error("Get IMonitor instance Error, Please check it constructor method, neTypeId:" + neTypeId
					+ ", monitorName:" + monitorName);
		}

		return monitor;
	}

	private String getQueryId(String neTypeId, String monitorName) {
		StringBuilder tmp = new StringBuilder();
		tmp.append(neTypeId).append(".").append(monitorName);
		return tmp.toString();
	}

	private String assembleRealMonitorName(String provider, String monitorName, String osType, String version) {
		return provider + ":" + monitorName + ":" + osType + ":" + version;
	}

	/**
	 * get the data report time
	 *
	 * @return data report time
	 */
	private synchronized Date getReportTime() {
		long times = reportTime.getTime();
		Date time = new Date(times);
		reportTime = new Date(times + monitorTaskInfo.getGranularity() * 1000);
		return time;
	}

	/**
	 * init data report time
	 *
	 * @param initTime
	 */
	public void initReportTime(Date initTime) {
		if (reportTime == null) {
			reportTime = initTime;
		}
	}
}
