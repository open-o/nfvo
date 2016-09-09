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
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm;

import java.util.List;

public class VduInfoFromVnfm {
    private String nodeTemplateId;
    private String vduName;
    private String vduType;
    private String vduState;
    private String vduId;
    private List<CpInfoFromVnfm> addresses;
    private String host;
    private String vimId;
    public String getNodeTemplateId() {
        return nodeTemplateId;
    }
    public void setNodeTemplateId(String nodeTemplateId) {
        this.nodeTemplateId = nodeTemplateId;
    }
    public String getVduName() {
        return vduName;
    }
    public void setVduName(String vduName) {
        this.vduName = vduName;
    }
    public String getVduType() {
        return vduType;
    }
    public void setVduType(String vduType) {
        this.vduType = vduType;
    }
    public String getVduState() {
        return vduState;
    }
    public void setVduState(String vduState) {
        this.vduState = vduState;
    }
    public String getVduId() {
        return vduId;
    }
    public void setVduId(String vduId) {
        this.vduId = vduId;
    }
    public List<CpInfoFromVnfm> getAddresses() {
        return addresses;
    }
    public void setAddresses(List<CpInfoFromVnfm> addresses) {
        this.addresses = addresses;
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getVimId() {
        return vimId;
    }
    public void setVimId(String vimId) {
        this.vimId = vimId;
    }

}
