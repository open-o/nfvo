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

import java.util.ArrayList;
import java.util.List;

public class DaPerfCounterInfo {
    private String name = null;
    private String value = null;

    public void setValue(String value) {
        this.value = value;
    }

    private boolean iflist = false;
    private boolean getnext = false;
    private boolean ifstring = false;
    private String specialProcess = null;
    private List counterParsers = null;

    public DaPerfCounterInfo(String name, String value, boolean iflist, boolean getnext,
            boolean ifstring, String specialProcess) {
        this.name = name;
        this.value = value;
        this.iflist = iflist;
        this.getnext = getnext;
        this.ifstring = ifstring;
        this.specialProcess = specialProcess;
        this.counterParsers = new ArrayList();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean iflist() {
        return iflist;
    }

    public boolean ifstring() {
        return ifstring;
    }

    public boolean getnext() {
        return getnext;
    }
    public void addParserInfo(DaParserInfo parserInfo) {
        counterParsers.add(parserInfo);
    }

    public List getCounterParsers() {
        return counterParsers;
    }


    public String getSpecialProcess() {
		return specialProcess;
	}

	public void setSpecialProcess(String specialProcess) {
		this.specialProcess = specialProcess;
	}

	@Override
    public String toString() {
        return "DaPerfCounterInfo [counterParsers=" + counterParsers + ", getnext=" + getnext
                + ", iflist=" + iflist + ", ifstring=" + ifstring + ", name=" + name
                + ", specialProcess=" + specialProcess + ", value=" + value + "]";
    }
}
