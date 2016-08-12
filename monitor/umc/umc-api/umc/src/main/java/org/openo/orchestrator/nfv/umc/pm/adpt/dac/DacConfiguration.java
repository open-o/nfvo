/**
 * Copyright (C) 2016 CMCC, Inc. and others. All rights reserved. (CMCC)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.umc.pm.adpt.dac;

public class DacConfiguration {
    private static DacConfiguration instance;

    private String dacIp = null;
    private String dacServerPort = "8206";

    public static DacConfiguration getInstance() {
        if (instance == null) {
            instance = new DacConfiguration();
        }
        return instance;
    }

    private DacConfiguration() {
    }


    public String getDacServerPort() {
        return dacServerPort;
    }

    public void setDacServerPort(String dacServerPort) {
        this.dacServerPort = dacServerPort;
    }

    public String getDacIp() {
        return dacIp;
    }

    public void setDacIp(String dacIp) {
        this.dacIp = dacIp;
    }


}
