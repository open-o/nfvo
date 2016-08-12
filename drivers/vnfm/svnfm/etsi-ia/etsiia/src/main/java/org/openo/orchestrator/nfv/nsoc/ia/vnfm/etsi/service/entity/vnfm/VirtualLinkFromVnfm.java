/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm;

public class VirtualLinkFromVnfm {
    private String vlInstanceId;
    private String vlId;
    private String vlName;
    private String vimId;
    private String nodeTemplateId;
    private String networkId;
    public String getVlInstanceId() {
        return vlInstanceId;
    }
    public void setVlInstanceId(String vlInstanceId) {
        this.vlInstanceId = vlInstanceId;
    }
    public String getVlId() {
        return vlId;
    }
    public void setVlId(String vlId) {
        this.vlId = vlId;
    }
    public String getVlName() {
        return vlName;
    }
    public void setVlName(String vlName) {
        this.vlName = vlName;
    }
    public String getVimId() {
        return vimId;
    }
    public void setVimId(String vimId) {
        this.vimId = vimId;
    }
    public String getNodeTemplateId() {
        return nodeTemplateId;
    }
    public void setNodeTemplateId(String nodeTemplateId) {
        this.nodeTemplateId = nodeTemplateId;
    }
    public String getNetworkId() {
        return networkId;
    }
    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

}
