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
package org.openo.orchestrator.nfv.dac.common.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.openo.orchestrator.nfv.dac.common.DacConst;
import org.openo.orchestrator.nfv.dac.common.ProtocalAvailability;
import org.openo.orchestrator.nfv.dac.common.util.filescan.FastFileSystem;
import org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common.DaMonitorPerfInfo;
import org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common.DaParserInfo;
import org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common.DaPerfCounterInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaConfReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(DaConfReader.class);
    private static DaConfReader instance = new DaConfReader();
    private Map<String, DaMonitorPerfInfo> monitorParserMaps = new HashMap<>();
    private HashMap<String, Hashtable<String, String>> hmQueueInfo = new HashMap<>();

    public static DaConfReader getInstance() {
        return instance;
    }

    private DaConfReader() {
        try {
            parseQueuesMapFile();
            parseMapFile();
        } catch (JDOMException | IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void parseQueuesMapFile() throws JDOMException, IOException {
        Element elementRoot;
        List listQueueInfo;
        Element eleDeviceInfo;
        String name;
        String maxQueueSize;
        String maxThreadNum;
        String runCollectPeriod;

        elementRoot = getElementFromXmlFile("dataaq-queues.xml");

        if (null == elementRoot) {
            return;
        }

        listQueueInfo = elementRoot.getChildren();
        if ((null == listQueueInfo) || (listQueueInfo.size() == 0)) {
            return;
        }

        for (Object aListQueueInfo : listQueueInfo) {
            Hashtable<String, String> htQueue = new Hashtable<>();
            eleDeviceInfo = (Element) aListQueueInfo;
            name = eleDeviceInfo.getAttributeValue("name");
            maxQueueSize = eleDeviceInfo.getAttributeValue("maxQueueSize");
            maxThreadNum = eleDeviceInfo.getAttributeValue("maxThreadNum");
            runCollectPeriod = eleDeviceInfo.getAttributeValue("runCollectPeriod");

            htQueue.put("maxQueueSize", maxQueueSize);
            htQueue.put("maxThreadNum", maxThreadNum);
            htQueue.put("runCollectPeriod", runCollectPeriod);

            hmQueueInfo.put(name, htQueue);
        }
    }

    private Element getElementFromXmlFile(String fileName) throws JDOMException, IOException {
        File file = GeneralFileLocaterImpl.getGeneralFileLocater().getFile(DacConst.SYSTEMDIR,
                fileName);
        if (file == null) {
            LOGGER.warn(fileName + "XmlFile:file is null");
            return null;
        }

        return getFileRoot(file);
    }

    private Element getFileRoot(File file) throws JDOMException, IOException {
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(file);
        return document.getRootElement();
    }

    private void parseMapFile() throws JDOMException, IOException {
        String filename = "*-monitor-map.xml";
        File[] files = FastFileSystem.getFiles(filename);
        if (files == null || files.length == 0) {
            LOGGER.error("No monitor-map.xml defined.");
            return;
        }
        for (File file : files) {
            parseMapFile(file);
        }
    }

    private void parseMapFile(File file) throws JDOMException, IOException {
        Element root = getFileRoot(file);
        List monitors = root.getChildren();
        for (Object monitor : monitors) {
            Element monitorElement = (Element) monitor;
            DaMonitorPerfInfo monitorInfo = parseMonitorInfo(monitorElement);
            monitorParserMaps.put(monitorInfo.getMonitorName(), monitorInfo);
        }
    }

    public DaMonitorPerfInfo getMonitorParserMapInfo(String monitorName) {
        return monitorParserMaps.get(monitorName);
    }

    private DaMonitorPerfInfo parseMonitorInfo(Element monitorElement) {
        String monitorName = getAttributeValue(monitorElement, "name");
        String command = getAttributeValue(monitorElement, "command");

        DaMonitorPerfInfo monitorInfo = new DaMonitorPerfInfo(monitorName, command);

        int i = 1;
        while (true) {
            String namei = getAttributeValue(monitorElement, "name" + i);
            if (namei == null) {
                break;
            }
            String valuei = getAttributeValue(monitorElement, "value" + i);
            monitorInfo.nameis.add(namei);
            monitorInfo.valueis.add(valuei);
            i++;
        }

        // In order to obtain multi command.
        int j = 1;
        while (true) {
            String commandj = getAttributeValue(monitorElement, "command" + j);
            if (commandj == null) {
                break;
            }
            monitorInfo.commands.add(commandj);
            j++;
        }

        String accepts = getAttributeValue(monitorElement, "accept");
        if (accepts != null) {
            StringTokenizer tokens = new StringTokenizer(accepts);
            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                monitorInfo.acceptTokens.add(token);
            }
        }

        String availablity = getAttributeValue(monitorElement, "availablity");
        if (availablity != null) {
        	ProtocalAvailability.getInstance().addAvailabilityMonitor(monitorName);
        }

        List perCounters = monitorElement.getChildren();
        for (Object perCounter : perCounters) {
            Element perCounterElement = (Element) perCounter;
            // name
            String name = getAttributeValue(perCounterElement, "name");
            // iflist
            boolean iflist = false;
            String iflistString = getAttributeValue(perCounterElement, "iflist");
            if (iflistString != null) {
                iflist = iflistString.equalsIgnoreCase("true");
            }

            // getnext
            boolean getnext = false;
            String getnextString = getAttributeValue(perCounterElement, "getnext");
            if (getnextString != null) {
                getnext = getnextString.equalsIgnoreCase("true");
            }

            // ifstring
            boolean ifstring = false;
            String getIfString = getAttributeValue(perCounterElement, "ifstring");
            if (getIfString != null) {
                ifstring = getIfString.equalsIgnoreCase("true");
            }

            // value
            String value = getAttributeValue(perCounterElement, "value");

            // specialProcess
            String specialProcess = getAttributeValue(perCounterElement, "specialProcess");

            DaPerfCounterInfo perCounterInfo =
                    new DaPerfCounterInfo(name, value, iflist, getnext, ifstring, specialProcess);
            monitorInfo.addPerfCounter(perCounterInfo);

            List parsers = perCounterElement.getChildren();
            for (Object parser : parsers) {
                Element parserInfoElement = (Element) parser;

                int line = 1;
                String lineString = getAttributeValue(parserInfoElement, "line");
                if (lineString != null) {
                    line = Integer.parseInt(lineString);
                }

                String tokenString = getAttributeValue(parserInfoElement, "token");
                boolean iftokenall = false;
                if (tokenString.indexOf("+") != -1) {
                    iftokenall = true;
                    tokenString = DacUtil.replace(tokenString, "+", "");
                }
                int token = Integer.parseInt(tokenString);

                String parseValue = getAttributeValue(parserInfoElement, "name");
                String unitValue = getAttributeValue(parserInfoElement, "unit");

                DaParserInfo parseInfo =
                        new DaParserInfo(parseValue, line, token, unitValue, iftokenall);
                perCounterInfo.addParserInfo(parseInfo);
            }
        }

        return monitorInfo;
    }

    public String getAttributeValue(Element monitorElement, String key) {
        return monitorElement.getAttributeValue(key);
    }

    public Hashtable<String, String> getHmQueueInfo(String queueName) {
        return hmQueueInfo.get(queueName);
    }
}
