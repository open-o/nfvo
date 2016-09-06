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
package org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DaMonitorPerfInfo {
    private String monitorName = null;
    private String command = null;
    private List perfCounters = null;

    public Vector nameis = new Vector();
    public Vector valueis = new Vector();
    public Vector commands = new Vector();
    public Vector acceptTokens = new Vector();

    public DaMonitorPerfInfo(String monitorName, String command) {
        this.monitorName = monitorName;
        this.command = command;
        this.perfCounters = new ArrayList();
    }

    public String getMonitorName() {
        return monitorName;
    }

    public String getCommand() {
        return command;
    }

    public void addPerfCounter(DaPerfCounterInfo perfCounter) {
        perfCounters.add(perfCounter);
    }

    public List getPerfCounters() {
        return perfCounters;
    }

    @Override
    public String toString() {
        return "DaMonitorPerfInfo [acceptTokens=" + acceptTokens + ", command=" + command
                + ", commands=" + commands + ", monitorName=" + monitorName + ", nameis=" + nameis
                + ", perfCounters=" + perfCounters + ", valueis=" + valueis + "]";
    }
}
