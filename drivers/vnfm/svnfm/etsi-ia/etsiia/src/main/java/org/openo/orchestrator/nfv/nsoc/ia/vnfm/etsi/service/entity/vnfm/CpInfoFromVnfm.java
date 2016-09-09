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

public class CpInfoFromVnfm {
    private String nodeTemplateId;
    private String cpName;
    private IpaddressFromVnfm address;
    private String vlId;
    public String getNodeTemplateId() {
        return nodeTemplateId;
    }
    public void setNodeTemplateId(String nodeTemplateId) {
        this.nodeTemplateId = nodeTemplateId;
    }
    public String getCpName() {
        return cpName;
    }
    public void setCpName(String cpName) {
        this.cpName = cpName;
    }
    public IpaddressFromVnfm getAddress() {
        return address;
    }
    public void setAddress(IpaddressFromVnfm address) {
        this.address = address;
    }
    public String getVlId() {
        return vlId;
    }
    public void setVlId(String vlId) {
        this.vlId = vlId;
    }

}
