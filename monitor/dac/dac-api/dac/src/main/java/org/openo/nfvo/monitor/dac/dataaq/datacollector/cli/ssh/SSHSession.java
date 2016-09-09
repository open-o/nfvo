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
package org.openo.nfvo.monitor.dac.dataaq.datacollector.cli.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import org.openo.nfvo.monitor.dac.dataaq.datacollector.cli.AbstractClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

public class SSHSession extends AbstractClientSession {

    private static final Logger LOGGER = LoggerFactory.getLogger(SSHSession.class);
    private static JSch jsch = new JSch();

    private class ITUserInfo implements UserInfo {
        String password;

        public ITUserInfo(String password) {
            super();
            this.password = password;
        }

        public String getPassword() {
            return password;
        }

        public boolean promptYesNo(String str) {
            return true;
        }

        public String getPassphrase() {
            return null;
        }

        public boolean promptPassphrase(String message) {
            return true;
        }

        public boolean promptPassword(String message) {
            return true;
        }

        public void showMessage(String message) {}
    }

    private Session session = null;
    private Channel channel = null;
    private String hostName = null;
    private int timeout = 60 * 1000;
    private final int sessionTimeout = 30 * 1000;
    private final int connectFailSleep = 2 * 1000;
    private final int connectFailRetry = 3;
    private int port = 0;
    OutputStream output = null;
    InputStream input = null;

    public SSHSession() {

    }

    public void connect(String hostname) throws IOException {
        connect(hostname, 22);
    }

    public void connect(String hostname, int port) throws IOException {
        this.hostName = hostname;
        this.port = port;
    }

    public void disconnect() throws IOException {
        boolean disConnectSuc = true;
        if (channel != null) {
            try {
                channel.disconnect();
            } catch (Exception e) {
                disConnectSuc = false;
            } finally {
                channel = null;
            }
        }
        if (session != null) {
            try {
                session.disconnect();
            } catch (Exception e) {
                disConnectSuc = false;
            } finally {
                session = null;
            }
        }
        input = null;
        output = null;
        if (!disConnectSuc) {
            throw new IOException("Close connect error at " + this.hostName);
        }
    }

    public void setSocketTimeout(int timeout) throws SocketException {
        this.timeout = timeout;
    }

    public void login(String username, String password) throws IOException {
        if (this.session == null) {
            try {
                this.session = jsch.getSession(username, this.hostName, this.port);
            } catch (JSchException e) {
                LOGGER.info("JSchException:Get session failed." + e.getMessage());
                throw new IOException("Get session failed." + this.hostName);
            }
        }
        ITUserInfo userInfo = new ITUserInfo(password);
        this.session.setUserInfo(userInfo);

        boolean sessionConnectSuc = false;
        int retryTime = 0;
        while (retryTime < connectFailRetry) {
            try {
                session.connect(sessionTimeout);
                sessionConnectSuc = true;
                break;
            } catch (JSchException e) {
                retryTime++;
                LOGGER.info("JSchException:Connect session failed." + e.getMessage());
                try {
                    Thread.sleep(connectFailSleep);
                } catch (InterruptedException ignored) {}
            }
        }
        if (!sessionConnectSuc) {
            throw new IOException("Failed to open session at " + this.hostName);
        }
        try {
            session.setTimeout(timeout);
            session.setServerAliveCountMax(0);
            channel = session.openChannel("shell");
            ((ChannelShell) channel).setPtySize(300, 300, 640, 480);
        } catch (JSchException e) {
            LOGGER.info("JSchException:Failed to open Channel." + e.getMessage());
            throw new IOException("Failed to open Channel." + this.hostName);
        }
        output = channel.getOutputStream();
        input = channel.getInputStream();
        try {
            channel.connect(this.timeout);
        } catch (JSchException e) {
            LOGGER.info("JSchException:Failed to connect channel." + e.getMessage());
            throw new IOException("Failed to connect channel." + this.hostName);
        }
        if (promptStrings != null) {
            waitfor(promptStrings);
        } else {
            if (getPrompt() != null) waitfor(getPrompt());
        }
    }

    protected InputStream getInputStream() {
        return input;
    }

    protected OutputStream getOutputStream() {
        return output;
    }

    /**
     * Send a command to the remote host. A newline is appended and if a prompt is set it will
     * return the resulting data until the prompt is encountered.
     *
     * @param cmd the command
     * @return output of the command or null if no prompt is set
     */
    public String send(String cmd) throws IOException {
        byte arr[];

        lastCommand = cmd;
        arr = (cmd + "\n").getBytes();

        input.skip(input.available());
        // no write until authorization is done
        for (int i = 0; i < arr.length; i++) {
            switch (arr[i]) {
                case 10: /* \n -> \r */
                    arr[i] = 13;
                    break;
            }
        }

        write(arr);
        if (promptStrings != null) return waitfor(promptStrings);
        if (getPrompt() != null) return waitfor(getPrompt());
        return null;
    }

    private void write(byte abyte0[]) throws IOException {
        String s = "\n";
        String s1 = new String(abyte0);
        String s2 = "\r";
        if (s1.equals(s)) abyte0 = s2.getBytes();
        output.write(abyte0);
        output.flush();
    }
}
