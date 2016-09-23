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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openo.nfvo.monitor.dac.common.DacConst;
import org.openo.nfvo.monitor.dac.common.util.DacUtil;
import org.openo.nfvo.monitor.dac.common.util.ExtensionImpl;
import org.openo.nfvo.monitor.dac.common.util.FileTransfer;
import org.openo.nfvo.monitor.dac.dataaq.common.DataCollector;
import org.openo.nfvo.monitor.dac.dataaq.common.ICollectorPara;
import org.openo.nfvo.monitor.dac.dataaq.common.IDataCollector;
import org.openo.nfvo.monitor.dac.dataaq.common.MonitorException;
import org.openo.nfvo.monitor.dac.dataaq.datacollector.cli.AbstractClientSession;
import org.openo.nfvo.monitor.dac.dataaq.datacollector.cli.ssh.SSHSession;
import org.openo.nfvo.monitor.dac.dataaq.datacollector.cli.telnet.TelnetSession;
import org.openo.nfvo.monitor.dac.dataaq.datacollector.para.SshCollectorPara;
import org.openo.nfvo.monitor.dac.dataaq.datacollector.para.TelnetCollectorPara;
import org.openo.nfvo.monitor.dac.dataaq.monitor.bean.common.MonitorTaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionImpl(keys = {DacConst.MOC_NFV_VDU_LINUX, DacConst.MOC_NFV_HOST_LINUX}, entensionId = IDataCollector.EXTENSIONID)
public class UnixPerformDataCollector extends DataCollector {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnixPerformDataCollector.class);
    private static String[] prompts = {"$", "#", "invalid login", "Login incorrect"};


    public MonitorTaskInfo getTaskInfo() {
        return taskInfo;
    }

    @Override
    public Map collectData(ICollectorPara para, Map commands) throws MonitorException {
        taskInfo.cachedMessage = "";
        TelnetCollectorPara telnetPara = (TelnetCollectorPara) para;
        List<String> acceptTokens = null;
        String shFileName = "";
        String command = "";
        Hashtable<String, List<String>> result = new Hashtable<>();

        String monitorname = (String) taskInfo.getMonitorProperty(DacConst.REALMONITORNAME);
        boolean ifLinux = false;
        if (monitorname.contains("NFV_OS")) {
            ifLinux = true;
        }

        for (Object o : commands.keySet()) {
            command = (String) o;
            acceptTokens = (List<String>) commands.get(command);
            if (command.endsWith(".sh")) {//sh /usr/wg/klinux_cpu.sh
                String ip = telnetPara.getIp();
                StringTokenizer tokens = new StringTokenizer(command);
                String[] array = new String[tokens.countTokens()];
                for (int i = 0; i < array.length; i++) {
                    array[i] = tokens.nextToken();
                }
                shFileName = array[array.length - 1];
                if (ifLinux) {
                    shFileName = DacUtil.changeSHFilePath(shFileName, telnetPara.getUserName());
                    command = "sh " + shFileName;
                }
                FileTransfer.checkAndTransferFile(para, shFileName, ip, ifLinux);
            }
        }

        LOGGER.info("TaskId: " + taskInfo.getJobId() + " Telnet session User: " + telnetPara.getUserName()
                + " Password: xxxx Command: " + command);

        // create ClientSession
        AbstractClientSession clientSession;
        if (para instanceof SshCollectorPara) {
            clientSession = new SSHSession();
        } else {
            clientSession = new TelnetSession();
            clientSession.setPasswdPrompt(":");// 1.Password for xxx: 2.Password:
        }

        clientSession.setCommandSuffix("\n");
        if (monitorname.contains("PROCESS")) {
            clientSession.setPromptList(new String[] {"#", "invalid login", "Login incorrect"});
        } else {
            clientSession.setPromptList(prompts);
        }

        clientSession.setCommandEcho(false);
        clientSession.setPromptEcho(false);

        try {
            clientSession.connect(telnetPara.getIp(), telnetPara.getPort());

            try {
                clientSession.setSocketTimeout(60000);
            } catch (Exception e) {
                LOGGER.error("Can't set socketTimeOut time.", e);
            }

            clientSession.login(telnetPara.getUserName(), telnetPara.getPassWord());

            if (clientSession.loginMessage != null) {
                if (clientSession.loginMessage.length() == 0
                        || clientSession.loginMessage.contains("invalid login")
                        || clientSession.loginMessage.contains("Login incorrect")) {
                    throw new MonitorException(
                            "Password incorrect for telnet login user " + telnetPara.getUserName()
                                    + ". Login Message:" + clientSession.loginMessage);
                }
            }
            // send command
            if (command.endsWith(".sh")) {
                clientSession.send("chmod 755 " + shFileName);
                try {
                    // Let the unix perform the chmod command or it can not
                    // complete before we exec it.
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.warn(e.getMessage());
                }
            }
            clientSession.send("LANG=en_US");
            clientSession.send("export LANG");
            command = merageCommandPara(telnetPara, command);
            String tempMessage = clientSession.send(command);
            if (tempMessage == null) {
                throw new MonitorException("Error:execute unix data collected null.");
            }

            String[] messages = clientSession.splitByLine(tempMessage.trim());

            List<String> list = new ArrayList<>();
            int startLine = 0;

            // Keep the number of filtered messages.
            StringBuilder sb = new StringBuilder();

            for (int i = startLine; i < messages.length; i++) {
                boolean promptflag = false;
                String thisMessage = messages[i];
                StringTokenizer tokens = new StringTokenizer(thisMessage);
                if (tokens.hasMoreTokens()) {
                    String firstToken = tokens.nextToken();
                    String secondToken = "";
                    if (tokens.hasMoreTokens()) {
                        secondToken = tokens.nextToken();
                    }
                    for (String prompt : prompts) {
                        if (firstToken.contains(prompt)) {
                            promptflag = true;
                        }
                        if (secondToken.contains(prompt)) {
                            promptflag = true;
                        }
                    }
                }
                if (acceptTokens != null) {
                    if (acceptTokens.size() != 0) {
                        if (!acceptThisLine(acceptTokens, messages[i])) {
                            promptflag = true;
                        }
                    }
                }
                if (promptflag) {
                    sb.append("Filtered the following message: ").append(messages[i]).append("\n");
                }
                if (!promptflag) {
                    list.add(messages[i]);
                }
            }

            result.put(command, list);

            for (int i = 0, size = list.size(); i < size; i++) {
                sb.append("TaskId: ").append(taskInfo.getJobId()).append(" Telnet session result [").append(i)
                        .append("]:").append(list.get(i)).append("\n");
            }
            sb.append("TaskId: ").append(taskInfo.getJobId()).append(" Telnet session result total [")
                    .append(list.size()).append("]").append("\n");

            taskInfo.cachedMessage = sb.toString();
            return result;
        } catch (IOException e) {
            throw new MonitorException("Can't connect the target host ", e);
        } finally {
            try {
                if (clientSession != null) {
                    clientSession.disconnect();
                }
            } catch (Exception e) {
                LOGGER.warn(e.getMessage());
            }
        }

    }

    private String merageCommandPara(TelnetCollectorPara telnetPara, String command) {
        String commandPara = telnetPara.getPara();
        if (commandPara != null && commandPara.length() != 0) {
            command = command + " " + commandPara;
        }
        return command;
    }

    private boolean acceptThisLine(List<String> acceptTokens, String message) {
        StringTokenizer tokens = new StringTokenizer(message);
        int acceptSize = acceptTokens.size();
        int nowSize = tokens.countTokens();
        String[] nowTokens = new String[nowSize];
        for (int i = 0; i < nowSize; i++) {
            nowTokens[i] = tokens.nextToken();
        }
        String lastAcceptToken = acceptTokens.get(acceptSize - 1);
        if (!lastAcceptToken.endsWith("+")) {
            if (acceptSize != nowSize) {
                return false;
            }
        } else {
            if (acceptSize > nowSize) {
                return false;
            }
        }

        for (int i = 0; i < acceptSize - 1; i++) {
            if (!isAccept(acceptTokens.get(i), nowTokens[i])) {
                return false;
            }
        }
        if (!lastAcceptToken.endsWith("+")) {
            if (!isAccept(acceptTokens.get(acceptSize - 1), nowTokens[acceptSize - 1])) {
                return false;
            }
        } else {
            for (int i = acceptSize; i < nowTokens.length; i++) {
                if (!isAccept(acceptTokens.get(acceptSize - 1), nowTokens[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isAccept(String type, String nowToken) {
        String acceptType = type;
        if (acceptType.startsWith("number")) {
            return (isNumber(nowToken));
        }
        if (acceptType.startsWith("time")) {
            return (isTime(nowToken));
        }
        if (acceptType.startsWith("plus")) {
            return (nowToken.equals("+"));
        }
        if (!acceptType.startsWith("string")) {
            if (acceptType.endsWith("*")) {
                acceptType = DacUtil.replace(acceptType, "*", "");
                return (nowToken.startsWith(acceptType));
            } else {
                return (nowToken.equals(acceptType));
            }
        }
        return true;
    }

    private boolean isTime(String token) {
        if (token.length() == 0) {
            return false;
        }
        Pattern p = Pattern.compile("[:|\\d]*");
        Matcher m = p.matcher(token);
        return m.matches();

    }

    private boolean isNumber(String token) {
        if (token.length() == 0) {
            return false;
        }
        Pattern p = Pattern.compile("[\\.|\\d]*");
        Matcher m = p.matcher(token);
        return m.matches();
    }


}
