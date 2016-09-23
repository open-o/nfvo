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
package org.openo.nfvo.monitor.dac.dataaq.dataparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.openo.nfvo.monitor.dac.common.DacConst;
import org.openo.nfvo.monitor.dac.common.util.Calculator;
import org.openo.nfvo.monitor.dac.common.util.DacUtil;
import org.openo.nfvo.monitor.dac.common.util.ExtensionImpl;
import org.openo.nfvo.monitor.dac.dataaq.common.DataAcquireException;
import org.openo.nfvo.monitor.dac.dataaq.common.IDataParser;
import org.openo.nfvo.monitor.dac.dataaq.common.MonitorException;
import org.openo.nfvo.monitor.dac.dataaq.monitor.bean.common.DaParserInfo;
import org.openo.nfvo.monitor.dac.dataaq.monitor.bean.common.DaPerfCounterInfo;
import org.openo.nfvo.monitor.dac.dataaq.monitor.bean.common.MonitorTaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionImpl(keys = {DacConst.MOC_NFV_VDU_LINUX, DacConst.MOC_NFV_HOST_LINUX}, entensionId = IDataParser.EXTENSIONID)
public class TelnetDataParser implements IDataParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelnetDataParser.class);

    @Override
    public Object parse(ArrayList dataCollected, DaPerfCounterInfo perfCounterInfo)
            throws MonitorException {
        List<String> result = new ArrayList<String>();

        if (dataCollected.size() == 0) {
            return result;
        }

        String[] valueStr = (String[]) dataCollected.toArray(new String[dataCollected.size()]);
        List parserContent = perfCounterInfo.getCounterParsers();
        String formular = perfCounterInfo.getValue();

        Map<String, List<String>> valueParsed = new HashMap<>();
        for (Object aParserContent : parserContent) {
            DaParserInfo parseInfo = (DaParserInfo) aParserContent;
            int line = parseInfo.getLine();
            String name = parseInfo.getName();
            int token = parseInfo.getToken();
            boolean iftokenall = parseInfo.iftokenall();
            List<String> value =
                    DacUtil.getInfo(line, token, valueStr, perfCounterInfo.iflist(), iftokenall);

            String unit = parseInfo.getUnit();
            if (unit != null) {
                value = DacUtil.delUnit(value, unit);
            }
            valueParsed.put(name, value);//<"cpuidleratio",{92.12}>
        }

        if (perfCounterInfo.ifstring()) {
            List<String> list = valueParsed.get(formular);
            if (list == null) {
                LOGGER.error("There's no such list as " + formular);
                return new ArrayList();
            }
            return list;
        }

        try {
            result = new Calculator().calculate(valueParsed, formular);
        } catch (MonitorException e) {
            Set<String> keySet = valueParsed.keySet();
            for (String key : keySet) {
                Object obj = valueParsed.get(key);
                LOGGER.info(" newKEY: " + key);
                if (obj != null) {
                    List list = (ArrayList) obj;
                    for (int j = 0, size = list.size(); (j < size) && (j <= 15); j++) {
                        String neirong = list.get(j).toString();
                        LOGGER.info("[" + j + "]: " + neirong);
                    }
                    if (list.size() > 15) {
                        LOGGER.info("Total size: " + list.size());
                    }
                } else {
                    LOGGER.info(obj != null ? obj.toString() : null);
                }
            }
            LOGGER.error(formular, e);
        }
        return result;
    }


    private static final ConcurrentHashMap telnetNetworkInterfaceCache = new ConcurrentHashMap();

    public Map cacheAndCalculateLinuxTelnetNetworkInterface(MonitorTaskInfo mtaskInfo, Map result)
            throws DataAcquireException {
        String ip = mtaskInfo.getMonitorProperty(DacConst.IPADDRESS).toString();
        StringBuilder sb = new StringBuilder("TaskId: ");
        sb.append(mtaskInfo.getJobId()).append(" IP: ");
        sb.append(ip);
        sb.append(" TELNET SERVER: ");
        StringBuilder cacheMsg = new StringBuilder();
        cacheMsg.append(sb).append("\n");
        StringBuilder sb1 = new StringBuilder();
        sb1.append(mtaskInfo.cachedMessage);

        long collectInterval = mtaskInfo.getGranularity();
        String sTaskId = String.valueOf(mtaskInfo.getJobId());
        Map map1 = (Map) telnetNetworkInterfaceCache.get(sTaskId);

        List vecCollectTime = new ArrayList();
        vecCollectTime.add(String.valueOf(System.currentTimeMillis() / 1000));
        result.put(DacConst.m_COLLECTEDTIME, vecCollectTime);
        telnetNetworkInterfaceCache.put(sTaskId, result);

        if (sTaskId.equals("0")) {
            return result;
        }

        if (map1 == null) {
            throw new DataAcquireException(DacConst.ERRORCODE_NOTUPONDATA, "Taskid: " + sTaskId
                    + " The first data acquisition from " + ip + ", not reporting data.");
        }

        List telnetInPacket = (ArrayList) result.get("TELNETINPACKET");
        List telnetOutPacket = (ArrayList) result.get("TELNETOUTPACKET");
        List telnetInError = (ArrayList) result.get("TELNETINERROR");
        List telnetOutError = (ArrayList) result.get("TELNETOUTERROR");
        List telnetInByte = (ArrayList) result.get("TELNETINBYTE");
        List telnetOutByte = (ArrayList) result.get("TELNETOUTBYTE");
        List telnetSpeed = (ArrayList) result.get("TELNETSPEED");

        if (telnetInPacket == null || telnetOutPacket == null || telnetInError == null
                || telnetOutError == null || telnetInByte == null || telnetOutByte == null) {
            return null;
        }

        int size = telnetInPacket.size();
        Set keySet = map1.keySet();
        Iterator it = keySet.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            Object obj = map1.get(key);
            cacheMsg.append("cached dataaq key: ").append(key).append("\n");
            if (obj instanceof List) {
                List vec = (ArrayList) obj;
                for (int j = 0, vsize = vec.size(); j < vsize; j++) {
                    String neirong = (String) vec.get(j);
                    cacheMsg.append(j).append(": ").append(neirong).append("\n");
                }
            } else {
                cacheMsg.append("value:").append(obj.toString()).append("\n");
            }
        }
        sb1.append(cacheMsg);
        mtaskInfo.cachedMessage = sb1.toString();

        long cdTimeLast = Long
                .parseLong(((ArrayList) map1.get(DacConst.m_COLLECTEDTIME)).get(0).toString());
        long cdTimeThis = System.currentTimeMillis() / 1000;

        List lastTelnetInPacket = (ArrayList) map1.get("TELNETINPACKET");
        if (lastTelnetInPacket.size() != size) {
            LOGGER.info(
                    "TelnetNetworkInterface: lastTelnetInPacket's size is not equal to this one.");
            throw new DataAcquireException(DacConst.ERRORCODE_NOTUPONDATA,
                    "Taskid: " + sTaskId + " Collect the networkInterface data acquisition from "
                            + ip + ", lastTelnetInPacket's size is not equal to this one."
                            + "; cdTimeLast = " + cdTimeLast + "; cdTimeThis = " + cdTimeThis
                            + "; collectInterval = " + collectInterval);
        }
        List lastTelnetOutPacket = (ArrayList) map1.get("TELNETOUTPACKET");
        if (lastTelnetOutPacket.size() != size) {
            LOGGER.info(
                    "TelnetNetworkInterface: lastTelnetOutPacket's size is not equal to this one.");
            return result;
        }
        List lastTelnetInError = (ArrayList) map1.get("TELNETINERROR");
        if (lastTelnetInError.size() != size) {
            LOGGER.info(
                    "TelnetNetworkInterface: lastTelnetInError's size is not equal to this one.");
            return result;
        }
        List lastTelnetOutError = (ArrayList) map1.get("TELNETOUTERROR");
        if (lastTelnetOutError.size() != size) {
            LOGGER.info(
                    "TelnetNetworkInterface: lastTelnetOutError's size is not equal to this one.");
            return result;
        }
        List lastTelnetInByte = (ArrayList) map1.get("TELNETINBYTE");
        if (lastTelnetInByte.size() != size) {
            LOGGER.info(
                    "TelnetNetworkInterface: lastTelnetInByte's size is not equal to this one.");
            return result;
        }
        List lastTelnetOutByte = (ArrayList) map1.get("TELNETOUTBYTE");
        if (lastTelnetOutByte.size() != size) {
            LOGGER.info(
                    "TelnetNetworkInterface: lastTelnetOutByte's size is not equal to this one.");
            return result;
        }

        List packetRecVec = new ArrayList();
        List packetSentVec = new ArrayList();
        List packetRecErrorVec = new ArrayList();
        List packetSentErrorVec = new ArrayList();
        List packetRecErrorRatioVec = new ArrayList();
        List packetSentErrorRatioVec = new ArrayList();
        List byteRecRatioVec = new ArrayList();
        List byteSentRatioVec = new ArrayList();
        for (int i = 0; i < size; i++) {
            long periodOutErrors;
            long periodInErrors;
            long periodOutPkts;
            long periodInPkts;
            double periodRecErrorRatio;
            double periodSentErrorRatio;
            double periodInBytes;
            double periodOutBytes;
            double periodRecRatio;
            double periodSentRatio;
            long periodTime;
            int speed;

            if ((cdTimeThis - cdTimeLast) > (collectInterval + 180)) {
                throw new DataAcquireException(DacConst.ERRORCODE_NOTUPONDATA,
                        "Taskid: " + sTaskId + " Recovery of the data acquisition from " + ip
                                + ", not reporting first data." + "; cdTimeLast = " + cdTimeLast
                                + "; cdTimeThis = " + cdTimeThis + "; collectInterval = "
                                + collectInterval);
            } else {
                periodTime = cdTimeThis - cdTimeLast;
                speed = Integer.parseInt((String) telnetSpeed.get(i));
                long lastValue = Long.parseLong((String) lastTelnetOutError.get(i));
                long thisValue = Long.parseLong((String) telnetOutError.get(i));
                periodOutErrors = thisValue < lastValue ? thisValue : (thisValue - lastValue);

                lastValue = Long.parseLong((String) lastTelnetInError.get(i));
                thisValue = Long.parseLong((String) telnetInError.get(i));
                periodInErrors = thisValue < lastValue ? thisValue : (thisValue - lastValue);

                lastValue = Long.parseLong((String) lastTelnetOutPacket.get(i));
                thisValue = Long.parseLong((String) telnetOutPacket.get(i));
                periodOutPkts = thisValue < lastValue ? thisValue : (thisValue - lastValue);

                lastValue = Long.parseLong((String) lastTelnetInPacket.get(i));
                thisValue = Long.parseLong((String) telnetInPacket.get(i));
                periodInPkts = thisValue < lastValue ? thisValue : (thisValue - lastValue);

                periodRecErrorRatio = periodInPkts == 0 ? 0 : periodInErrors * 100 / periodInPkts;
                periodSentErrorRatio =
                        periodOutPkts == 0 ? 0 : periodOutErrors * 100 / periodOutPkts;

                lastValue = Long.parseLong((String) lastTelnetInByte.get(i));
                thisValue = Long.parseLong((String) telnetInByte.get(i));
                periodInBytes = thisValue < lastValue ? thisValue : (thisValue - lastValue);

                lastValue = Long.parseLong((String) lastTelnetOutByte.get(i));
                thisValue = Long.parseLong((String) telnetOutByte.get(i));
                periodOutBytes = thisValue < lastValue ? thisValue : (thisValue - lastValue);

                periodRecRatio = speed == 0
                        ? 0
                        : periodInBytes * 8 * 100 / (speed * 1024 * 1024 * periodTime);
                periodSentRatio = speed == 0
                        ? 0
                        : periodOutBytes * 8 * 100 / (speed * 1024 * 1024 * periodTime);
            }
            packetSentErrorVec.add(String.valueOf(periodOutErrors));
            packetRecErrorVec.add(String.valueOf(periodInErrors));
            packetSentVec.add(String.valueOf(periodOutPkts));
            packetRecVec.add(String.valueOf(periodInPkts));
            packetRecErrorRatioVec.add(String.valueOf(periodRecErrorRatio));
            packetSentErrorRatioVec.add(String.valueOf(periodSentErrorRatio));
            byteRecRatioVec.add(String.valueOf(periodRecRatio));
            byteSentRatioVec.add(String.valueOf(periodSentRatio));
        }

        packetRecErrorRatioVec = DacUtil.listSetScale(packetRecErrorRatioVec, 3);
        packetSentErrorRatioVec = DacUtil.listSetScale(packetSentErrorRatioVec, 3);
        byteRecRatioVec = DacUtil.listSetScale(byteRecRatioVec, 3);
        byteSentRatioVec = DacUtil.listSetScale(byteSentRatioVec, 3);

        Map resultMap = new HashMap();
        resultMap.putAll(result);
        resultMap.put("SENTERRORS", packetSentErrorVec);
        resultMap.put("RECEIVEDERRORS", packetRecErrorVec);
        resultMap.put("PACKETSENT", packetSentVec);
        resultMap.put("PACKETRECEIVED", packetRecVec);
        resultMap.put("RECEIVEDERRORRATIO", packetRecErrorRatioVec);
        resultMap.put("SENTERRORRATIO", packetSentErrorRatioVec);
        resultMap.put("BYTESINRATIO", byteRecRatioVec);
        resultMap.put("BYTESOUTRATIO", byteSentRatioVec);

        return resultMap;
    }
}
