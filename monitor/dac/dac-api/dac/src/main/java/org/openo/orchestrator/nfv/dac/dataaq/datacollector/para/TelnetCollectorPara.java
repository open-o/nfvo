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
package org.openo.orchestrator.nfv.dac.dataaq.datacollector.para;

import org.openo.orchestrator.nfv.dac.dataaq.common.ICollectorPara;

public class TelnetCollectorPara implements ICollectorPara {
    protected String ip = null;
    protected int port = 0;
    protected String userName = null;
    protected String passWord = null;
    private String para = null;

    public TelnetCollectorPara(String ip, int port, String userName, String passWord) {
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.passWord = passWord;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    @Override
    public String toString() {
        return "TelnetCollectorPara [ip=" + ip + ", passWord=" + passWord + ", port=" + port
                + ", userName=" + userName + "]";
    }
}
