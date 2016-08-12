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
package org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common;

public class DaParserInfo {
    private String name = null;
    private int token = 0;
    private int line = 0;
    private String unit = null;
    private boolean iftokenall = false;

    public DaParserInfo(String name, int line, int token, String unit, boolean iftokenall) {
        this.name = name;
        this.token = token;
        this.line = line;
        this.unit = unit;
        this.iftokenall = iftokenall;
    }

    public String getName() {
        return name;
    }

    public int getToken() {
        return token;
    }

    public int getLine() {
        return line;
    }

    public String getUnit() {
        return unit;
    }

    public boolean iftokenall() {
        return iftokenall;
    }

    @Override
    public String toString() {
        return "DaParserInfo [iftokenall=" + iftokenall + ", line=" + line + ", name=" + name
                + ", token=" + token + ", unit=" + unit + "]";
    }
}
