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

public class VnfcInfoFromVnfm {
    private String vnfcInstanceId;
    private String nodeTemplateId;
    private String vnfcName;
    private String vduId;
    public String getVnfcInstanceId() {
        return vnfcInstanceId;
    }
    public void setVnfcInstanceId(String vnfcInstanceId) {
        this.vnfcInstanceId = vnfcInstanceId;
    }
    public String getNodeTemplateId() {
        return nodeTemplateId;
    }
    public void setNodeTemplateId(String nodeTemplateId) {
        this.nodeTemplateId = nodeTemplateId;
    }
    public String getVnfcName() {
        return vnfcName;
    }
    public void setVnfcName(String vnfcName) {
        this.vnfcName = vnfcName;
    }
    public String getVduId() {
        return vduId;
    }
    public void setVduId(String vduId) {
        this.vduId = vduId;
    }

}
