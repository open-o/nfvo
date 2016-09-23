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
import java.util.Set;

import org.openo.nfvo.monitor.dac.common.DacConst;
import org.openo.nfvo.monitor.dac.common.util.DaConfReader;
import org.openo.nfvo.monitor.dac.common.util.ExtensionImpl;
import org.openo.nfvo.monitor.dac.dataaq.common.DataAcquireException;
import org.openo.nfvo.monitor.dac.dataaq.common.ICollectorPara;
import org.openo.nfvo.monitor.dac.dataaq.common.IDataCollector;
import org.openo.nfvo.monitor.dac.dataaq.common.IDataParser;
import org.openo.nfvo.monitor.dac.dataaq.common.IMonitor;
import org.openo.nfvo.monitor.dac.dataaq.common.Monitor;
import org.openo.nfvo.monitor.dac.dataaq.common.MonitorException;
import org.openo.nfvo.monitor.dac.dataaq.datacollector.UnixPerformDataCollector;
import org.openo.nfvo.monitor.dac.dataaq.datacollector.para.SshCollectorPara;
import org.openo.nfvo.monitor.dac.dataaq.datacollector.para.TelnetCollectorPara;
import org.openo.nfvo.monitor.dac.dataaq.dataparser.TelnetDataParser;
import org.openo.nfvo.monitor.dac.dataaq.monitor.bean.common.DaMonitorPerfInfo;
import org.openo.nfvo.monitor.dac.dataaq.monitor.bean.common.DaPerfCounterInfo;


@ExtensionImpl(keys = {DacConst.MOC_NFV_VDU_LINUX, DacConst.MOC_NFV_HOST_LINUX}, entensionId = IMonitor.EXTENSIONID)
public class MonitorTelnet extends Monitor {

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public Map perform(Map paras, IDataCollector dataCollector, IDataParser dataParser)
            throws DataAcquireException {
        if (dataCollector instanceof UnixPerformDataCollector) {
            taskInfo = ((UnixPerformDataCollector) dataCollector).getTaskInfo();
        }
        String MonitorName = (String) paras.get(DacConst.REALMONITORNAME);
        DaMonitorPerfInfo monitorParserMapInfo = getMonitorParserMapInfo(MonitorName);
        String commandStr = monitorParserMapInfo.getCommand();

        String ip = (String) paras.get(DacConst.IPADDRESS);
        String portString = (String) paras.get(DacConst.PORT);
        String userName = (String) paras.get(DacConst.USERNAME);
        String passWord = (String) paras.get(DacConst.PASSWORD);

        ICollectorPara para;
        String protocol = (String) paras.get(DacConst.PROTOCOL);
        if (protocol == null || protocol.equalsIgnoreCase(DacConst.TELNET)) {
            int port = 23;
            if (portString != null) {
                port = Integer.parseInt(portString);
            }
            para = new TelnetCollectorPara(ip, port, userName, passWord);
        } else {
            int port = 22;
            if (portString != null) {
                port = Integer.parseInt(portString);
            }
            para = new SshCollectorPara(ip, port, userName, passWord);
        }

        Map commandsMap = new HashMap();
        commandsMap.put(commandStr, monitorParserMapInfo.acceptTokens);

        Map retDataCollected;
        try {
            retDataCollected = dataCollector.collectData(para, commandsMap);
        } catch (MonitorException e) {
            if (e.getMessage().startsWith("Password")) {
                throw new DataAcquireException(e, DacConst.ERRORCODE_PASSWORDERROR);
            } else {
                throw new DataAcquireException(e, DacConst.ERRORCODE_COLLECTERROR);
            }
        }

        Map resultMap = new HashMap();

        try {
            Set retDataEntySet = retDataCollected.entrySet();
            for (Iterator iterator = retDataEntySet.iterator(); iterator.hasNext();) {
                Map.Entry valueForEveryCmdEntry = (Map.Entry) iterator.next();
                String retCommandName = (String) valueForEveryCmdEntry.getKey();
                ArrayList valueCollected = (ArrayList) valueForEveryCmdEntry.getValue();

                List perfCounters = monitorParserMapInfo.getPerfCounters();
                int size = perfCounters.size();
                for (int i = 0; i < size; i++) {
                    DaPerfCounterInfo perfCounterInfo = (DaPerfCounterInfo) perfCounters.get(i);
                    Object value = dataParser.parse(valueCollected, perfCounterInfo);
                    resultMap.put(perfCounterInfo.getName(), value);
                }
            }
        } catch (Exception e) {
            throw new DataAcquireException(e, DacConst.ERRORCODE_INCORRECTPARSERCONTENT);
        }

        if (resultMap.get("TELNETINPACKET") != null) {
            if (resultMap.get("TELNETSPEED") != null) {
            	TelnetDataParser telnetDataParser = (TelnetDataParser) dataParser;
                resultMap = telnetDataParser
                        .cacheAndCalculateLinuxTelnetNetworkInterface(this.taskInfo, resultMap);
            }
        }

        return resultMap;
    }

    protected DaMonitorPerfInfo getMonitorParserMapInfo(String monitorName) {
        return DaConfReader.getInstance().getMonitorParserMapInfo(monitorName);
    }
}
