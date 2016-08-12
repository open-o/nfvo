/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.orchestrator.nfv.dac.dataaq.datacollector.cli.telnet;

import org.apache.commons.net.telnet.InvalidTelnetOptionException;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.WindowSizeOptionHandler;
import org.openo.orchestrator.nfv.dac.dataaq.datacollector.cli.AbstractClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.SocketException;

public class TelnetSession extends AbstractClientSession {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelnetSession.class);

    private TelnetClient telnetClient = null;
    private InputStream in = null;
    private OutputStream out = null;
    private static WindowSizeOptionHandler windowSize = new WindowSizeOptionHandler(300, 300, true, true, true, true);

    private int timeout;

    public TelnetSession() {
        cmdEcho = true;
        promptEcho = true;

        telnetClient = new TelnetClient();
        try {
            telnetClient.addOptionHandler(windowSize);
        } catch (InvalidTelnetOptionException | IOException e) {
            LOGGER.error("Failed to add WindowSizeOptionHandler.", e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see cli.telnet.ClientSession#connect(java.lang.String, int)
     */
    public void connect(String s, int i) throws IOException {
        telnetClient.connect(s, i);
        in = telnetClient.getInputStream();
        out = telnetClient.getOutputStream();
    }

    /*
     * (non-Javadoc)
     *
     * @see cli.telnet.ClientSession#connect(java.lang.String)
     */
    public void connect(String s) throws IOException {
        telnetClient.connect(s, 23);
    }

    /*
     * (non-Javadoc)
     *
     * @see cli.telnet.ClientSession#disconnect()
     */
    public void disconnect() throws IOException {
        if (telnetClient.isConnected()) {
            telnetClient.disconnect();
        }
    }

    protected InputStream getInputStream() {
        return in;
    }

    protected OutputStream getOutputStream() {
        return out;
    }

    /*
     * (non-Javadoc)
     *
     * @see cli.telnet.ClientSession#login(java.lang.String, java.lang.String)
     */
    public void login(String s, String s1) throws IOException {
        if ((s == null || s.length() == 0) && (s1 == null || s1.length() == 0)) {
            loginMessage = waitfor(prompt);
            return;
        }
        if (s != null && s.length() > 0 && (s1 == null || s1.length() == 0)) {
            waitfor(loginPrompt);
            loginMessage = send(s, prompt);
        } else if (s1 != null && s1.length() > 0 && (s == null || s.length() == 0)) {
            waitfor(passwdPrompt);
            loginMessage = send(s1, prompt);
        } else {
            waitfor(loginPrompt);
            send(s, passwdPrompt, true);
            loginMessage = send(s1, prompt);
        }
    }

    public void setSocketTimeout(int i) throws SocketException {
        telnetClient.setSoTimeout(i);
        this.timeout = i;
    }

    /*
     * (non-Javadoc)
     *
     * @see cli.telnet.ClientSession#send(java.lang.String)
     */
    public String send(String s) throws IOException {
        return send(s, prompt);
    }

    private String send(String s, String s1, boolean waitForS1) throws IOException {
        lastCommand = s;

        if (cmdSuffix != null)
            write((s + cmdSuffix).getBytes());
        else
            write(s.getBytes());

        log("Message sent:" + s);
        if (waitForS1) {
            return waitfor(s1);
        }
        if (promptStrings != null)
            return waitfor(promptStrings);
        if (s1 != null)
            return waitfor(s1);
        else
            return null;
    }

    private String send(String s, String s1) throws IOException {
        lastCommand = s;

        if (cmdSuffix != null) {
            write(s.getBytes());
            write(cmdSuffix.getBytes());
        } else
            write(s.getBytes());

        log("Message sent:" + s);
        if (promptStrings != null)
            return waitfor(promptStrings);
        if (s1 != null)
            return waitfor(s1);
        else
            return null;
    }

    /**
     * Write data to the socket.
     *
     * @param b the buffer to be written
     */
    private void write(byte[] b) throws IOException {
        out.write(b);
        out.flush();
    }

    @Override
    protected String waitfor(String s) throws IOException {
        boolean flag = false;
        if (s.length() != 0)
            scriptHandler.setup(s);
        else
            throw new IOException(" prompt may be empty");
        byte abyte0[] = new byte[readBufferLength];
        int i = 0;
        String s1 = "";
        log("Parameter to match:" + s);
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeout) {
            if (getInputStream().available() > 0) {
                try {
                    i = getInputStream().read(abyte0);
                    log("Message rcvd:" + new String(abyte0));
                } catch (InterruptedIOException interruptedioexception) {
                    if (i > 0)
                        s1 = s1 + new String(abyte0, 0, i);
                    if (!cmdEcho || !promptEcho)
                        s1 = truncateResponse(s1);
                    if (flag) {
                        return s1;
                    } else {
                        partialData = s1;
                        throw new IOException(interruptedioexception.getMessage());
                    }
                }

                s1 = s1 + new String(abyte0, 0, i, "UTF-8");
                if (scriptHandler.match(s1, s)) {

                    log("Parameter match SUCCESSFUL:" + s);
                    partialData = null;
                    if (getInputStream().available() > 0) {
                        flag = true;
                    } else {
                        if (!cmdEcho || !promptEcho)
                            s1 = truncateResponse(s1);
                        return s1;
                    }
                }
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new IOException(e.getMessage());
                }
            }
        }
        return null;
    }

    @Override
    protected String waitfor(String[] as) throws IOException {
        byte abyte0[] = new byte[readBufferLength];
        int j = 0;
        String s = "";
        long starttime = System.currentTimeMillis();
        while (System.currentTimeMillis() - starttime < timeout) {
            if (getInputStream().available() > 0) {
                try {
                    j = getInputStream().read(abyte0);
                } catch (InterruptedIOException interruptedioexception) {
                    if (j > 0)
                        s = s + new String(abyte0, 0, j);
                    if (!cmdEcho || !promptEcho)
                        s = truncateResponse(s);
                    partialData = s;
                    throw new IOException(interruptedioexception.getMessage());
                }
                s = s + new String(abyte0, 0, j);
                int k = match(s, as);
                if (k >= 0) {
                    partialData = null;
                    if (!cmdEcho || !promptEcho) {
                        prompt = as[k];
                        s = truncateResponse(s);
                    }
                    return s;
                }
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new IOException(e.getMessage());
                }
            }
        }
        return null;
    }

    private int match(String source, String[] target) {
        for (int k = 0; k < target.length; k++) {
            scriptHandler.setup(target[k]);
            if (scriptHandler.match(source, target[k])) {
                return k;
            }
        }
        return -1;
    }

    @Override
    public void putScript(String fileName, String fileContent) throws IOException {
        super.putScript(fileName, fileContent);
        if (promptStrings != null)
            waitfor(promptStrings);
    }
}
