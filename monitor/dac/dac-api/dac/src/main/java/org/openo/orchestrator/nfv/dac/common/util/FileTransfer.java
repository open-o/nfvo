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

import org.openo.orchestrator.nfv.dac.common.DacConst;
import org.openo.orchestrator.nfv.dac.common.util.filescan.FastFileSystem;
import org.openo.orchestrator.nfv.dac.dataaq.common.ICollectorPara;
import org.openo.orchestrator.nfv.dac.dataaq.common.MonitorException;
import org.openo.orchestrator.nfv.dac.dataaq.datacollector.cli.AbstractClientSession;
import org.openo.orchestrator.nfv.dac.dataaq.datacollector.cli.ssh.SSHSession;
import org.openo.orchestrator.nfv.dac.dataaq.datacollector.cli.telnet.TelnetSession;
import org.openo.orchestrator.nfv.dac.dataaq.datacollector.para.SshCollectorPara;
import org.openo.orchestrator.nfv.dac.dataaq.datacollector.para.TelnetCollectorPara;
import org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common.MonitorTaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

public class FileTransfer {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileTransfer.class);
    private static String[] prompts = {"$", "#", "invalid login", "Login incorrect"};

    public static void checkAndTransferFile(ICollectorPara para, String destFileAndPath, String ip)
            throws MonitorException {
        checkAndTransferFile(para, destFileAndPath, ip, false);
    }

    public static void checkAndTransferFile(ICollectorPara para, String destFileAndPath, String ip, boolean ifLinux)
            throws MonitorException {
        // first check to see if the host is newly created or not, if yes and destFileAndPath
        // flag is not set, then set it and first delete the script file. Otherwise, don't first
        // delete the script file.
        boolean firstDeleteFlag = false;
        HashMap<String, String> ipMap = MonitorTaskInfo.getRemoteHostMap().get(ip);
        if (ipMap == null)
        {
            firstDeleteFlag = true;
            ipMap = new HashMap<>();
            MonitorTaskInfo.getRemoteHostMap().put(ip, ipMap);
        } else {
            if (ipMap.get(destFileAndPath) == null)
            {
                firstDeleteFlag = true;
            } else {
                firstDeleteFlag = false;
            }
        }

        TelnetCollectorPara telnetPara = (TelnetCollectorPara) para;
        AbstractClientSession clientSession = null;
        if (para instanceof SshCollectorPara) {
            clientSession = new SSHSession();
        } else {
            clientSession = new TelnetSession();
            clientSession.setPasswdPrompt(":");
        }

        clientSession.setCommandSuffix("\n");
        clientSession.setPromptList(prompts);
        clientSession.setCommandEcho(false);
        clientSession.setPromptEcho(false);

        try {
            clientSession.connect(telnetPara.getIp(), telnetPara.getPort());

            try {
                clientSession.setSocketTimeout(90000);
            } catch (Exception e) {
                LOGGER.error("Can't set socketTimeOut time.", e);
            }

            clientSession.login(telnetPara.getUserName(), telnetPara.getPassWord());

            if (clientSession.loginMessage != null) {
                if (clientSession.loginMessage.length() == 0
                        || clientSession.loginMessage.indexOf("invalid login") != -1
                        || clientSession.loginMessage.indexOf("Login incorrect") != -1) {
                    throw new MonitorException("Password incorrect for telnet login user " + telnetPara.getUserName());
                }
            }

            if (firstDeleteFlag == true) {
                clientSession.send("rm -f " + destFileAndPath);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.warn(e.getMessage(), e);
                }
            }

            String tempMessage = null;
            if (ifLinux == true) {
                tempMessage = clientSession.send("ls --color=none " + destFileAndPath);
            } else {
                tempMessage = clientSession.send("ls " + destFileAndPath);
            }

            if (tempMessage == null) {
                throw new MonitorException("ls return message is null.");
            }

            String[] messages = clientSession.splitByLine(tempMessage.trim());
            String firstMessage = messages[0];
            if ((firstMessage.startsWith("ls")) && (messages.length >= 2)) {
                firstMessage = messages[1];
            }
            StringTokenizer tokens = new StringTokenizer(firstMessage);
            if (tokens.countTokens() == 1) {
                return;
            }

            LOGGER.info("Now create directory and put file " + destFileAndPath);

            BufferedReader reader = null;
            String result = "";

            String[] pathes = destFileAndPath.split("/");
            String fileName = pathes[pathes.length - 1];
            File[] files = FastFileSystem.getFiles(fileName);
            if (files.length == 0)
            {
            	throw new MonitorException("FileNotFoundException, filename: " + fileName);
            }
            try {
                reader = new BufferedReader(new FileReader(files[0]));
            } catch (FileNotFoundException e) {
                throw new MonitorException("FileNotFoundException, filepath: " + files[0].getAbsolutePath(), e);
            }
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result = result + line + "\r\n";
                }
            } catch (IOException e) {
                throw new MonitorException("IOException", e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        LOGGER.warn(e.getMessage(), e);
                    }
                    reader = null;
                }
            }

            clientSession.send("cd /");
            for (int i = 1; i < pathes.length - 1; i++) {
                // String pwd = clientSession.send("pwd");
                clientSession.send("mkdir " + pathes[i]);
                clientSession.send("chmod 777 " + pathes[i]);
                clientSession.send("cd " + pathes[i]);
            }
            clientSession.putScript(fileName, result);
            // clientSession.send("");
            // clientSession.send("");

            ipMap.put(destFileAndPath, "true");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.warn(e.getMessage(), e);
            }
            // clientSession.send("chmod 755 " + fileName);
            // clientSession.send("");
        } catch (IOException e) {
            throw new MonitorException("Can't connect the target host ", e);
        } finally {
            if (clientSession != null) {
                try {
                    clientSession.disconnect();
                } catch (NullPointerException npe) {
                    clientSession = null;
                } catch (IOException e) {
                    LOGGER.warn(e.getMessage(), e);
                }
                clientSession = null;
            }
        }
    }
}
