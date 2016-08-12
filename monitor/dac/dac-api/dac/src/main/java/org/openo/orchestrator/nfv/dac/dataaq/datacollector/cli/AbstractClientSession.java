/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.dac.dataaq.datacollector.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.SocketException;

public abstract class AbstractClientSession implements ClientSession {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractClientSession.class);

    protected String loginPrompt = "login:";

	protected String passwdPrompt = "Password:";

    protected String prompt;

    public String loginMessage;

    protected String promptStrings[];

    protected boolean cmdEcho = false;

    protected boolean promptEcho = true;

    protected int readBufferLength = 256;

    protected String lastCommand;

    protected String cmdSuffix = "\n";

    protected static boolean debugFlag = false;

    protected ScriptHandler scriptHandler = new ScriptHandler();

    public String partialData;

    public abstract void connect(String s, int i) throws IOException;

    public abstract void connect(String s) throws IOException;

    public abstract void disconnect() throws IOException;

    public abstract void login(String s, String s1) throws IOException;

    // public abstract String Login(String s, String s1) throws IOException;

    public abstract String send(String s) throws IOException;

    protected abstract InputStream getInputStream();

    protected abstract OutputStream getOutputStream();

    public abstract void setSocketTimeout(int i) throws SocketException;

    public void setPasswdPrompt(String s) {
        passwdPrompt = s;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPromptList(String as[]) {
        promptStrings = as;
    }

    public void setCommandEcho(boolean flag) {
        cmdEcho = flag;
    }

    public void setPromptEcho(boolean flag) {
        promptEcho = flag;
    }

    public void setCommandSuffix(String s) {
        cmdSuffix = s;
    }

    public void setDebug(boolean flag) {
        debugFlag = flag;
    }

    public void putScript(String fileName, String fileContent) throws IOException {
        StringBuffer commandBuffer = new StringBuffer();
        commandBuffer.append("awk '{print}' > ").append(fileName).append("\r\n");
        // commandBuffer.append(fileContent).append( "\r\n\4");
        getOutputStream().write(commandBuffer.toString().getBytes());
        getOutputStream().flush();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        commandBuffer = new StringBuffer(fileContent).append("\r\n\4");
        getOutputStream().write(commandBuffer.toString().getBytes());
        getOutputStream().flush();
        // send( "chmod 0755 " + fileName);
    }

    protected String waitfor(String s) throws IOException {
        boolean flag = false;
        if (s.length() != 0)
            scriptHandler.setup(s);
        else
            throw new IOException(" prompt may be empty");
        byte abyte0[] = new byte[readBufferLength];
        int i = 0;
        StringBuilder s1 = new StringBuilder();
        String bufStr;
        String finalStr = "";
        log("Parameter to match:" + s);

        while (i >= 0) {
            try {
                i = getInputStream().read(abyte0);
                log("Message rcvd:" + new String(abyte0));
            } catch (InterruptedIOException interruptedioexception) {
                if (i > 0) {
                    bufStr = new String(abyte0, 0, i);
                    s1.append(bufStr);
                }
                if (!cmdEcho || !promptEcho) {
                    finalStr = truncateResponse(s1.toString());
                }
                if (flag) {
                    return finalStr;
                } else {
                    partialData = finalStr;
                    throw new IOException(interruptedioexception.getMessage());
                }
            }
            if (i > 0) {
                bufStr = new String(abyte0, 0, i);
                s1.append(bufStr);
                if (scriptHandler.match(s1.toString(), s)) {
                    LOGGER.debug("session prompt: " + "[" + s + "]" + bufStr);
                    partialData = null;
                    if (getInputStream().available() > 0) {
                        flag = true;
                    } else {
                        if (!cmdEcho || !promptEcho) finalStr = truncateResponse(s1.toString());
                        return finalStr;
                    }
                }
            }
        }
        return null;
    }

    protected String waitfor(String as[]) throws IOException {
        byte abyte0[] = new byte[readBufferLength];
        int j = 0;
        StringBuilder s = new StringBuilder();
        String bufStr;
        String finalStr = "";
        while (j >= 0) {
            try {
                j = getInputStream().read(abyte0);
            } catch (InterruptedIOException interruptedioexception) {
                if (j > 0) {
                    bufStr = new String(abyte0, 0, j);
                    s.append(bufStr);
                }
                if (!cmdEcho || !promptEcho) {
                    finalStr = truncateResponse(s.toString());
                }
                partialData = finalStr;
                throw new IOException(interruptedioexception.getMessage());
            }
            if (j > 0) {
                bufStr = new String(abyte0, 0, j);
                s.append(bufStr);
                for (String a : as) {
                    scriptHandler.setup(a);
                    if (scriptHandler.match(s.toString(), a)) {
                        if (j == readBufferLength) {
                            String matcher = "]" + a;
                            scriptHandler.setup(matcher);
                            if (!scriptHandler.match(s.toString(), matcher)) {
                                LOGGER.info("Not real prompt end: " + "[" + a + "]" + "[" + j
                                        + "]" + bufStr);
                                continue;
                            }
                        }
                        LOGGER.debug(
                                "session prompt: " + "[" + a + "]" + "[" + j + "]" + bufStr);
                        partialData = null;

                        if (!cmdEcho || !promptEcho) {
                            prompt = a;
                            finalStr = truncateResponse(s.toString());
                        }
                        return finalStr;
                    }
                }

            }
        }
        return null;
    }

    protected void log(String message) {
        if (debugFlag) {
            LOGGER.debug(message);
        }
    }

    protected String truncateResponse(String s) {
        String s1 = s.trim();
        if (!cmdEcho && lastCommand != null && s.startsWith(lastCommand)) {
            s1 = s1.substring(lastCommand.length());
        }
        if ((!promptEcho) && (prompt != null) && s1.endsWith(prompt)) {
            int lastReturn = s1.lastIndexOf(cmdSuffix);
            if (lastReturn >= 0) {
                s1 = s1.substring(0, lastReturn);
            } else {
                s1 = s1.substring(0, s1.length() - prompt.length());
            }
            // s1 = s1.substring(0, s1.length() - prompt.length());
        }

        return s1;
    }

    public String[] splitByLine(String str) {
        return str.split("\r\n");
    }
    public String getLoginPrompt() {
		return loginPrompt;
	}

	public void setLoginPrompt(String loginPrompt) {
		this.loginPrompt = loginPrompt;
	}
}
