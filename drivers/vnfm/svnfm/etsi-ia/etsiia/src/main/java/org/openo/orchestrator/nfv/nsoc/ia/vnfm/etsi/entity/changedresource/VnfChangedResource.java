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
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.changedresource;

public class VnfChangedResource {
	private String vnfInstanceId;
	private String vnfName;
	private String nodeTemplateId;
	private String type;
	private String vimInstanceId;
	private String vnfmInstanceId;
    public String getVnfInstanceId() {
        return vnfInstanceId;
    }
    public void setVnfInstanceId(String vnfInstanceId) {
        this.vnfInstanceId = vnfInstanceId;
    }
    public String getVnfName() {
        return vnfName;
    }
    public void setVnfName(String vnfName) {
        this.vnfName = vnfName;
    }
    public String getNodeTemplateId() {
        return nodeTemplateId;
    }
    public void setNodeTemplateId(String nodeTemplateId) {
        this.nodeTemplateId = nodeTemplateId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getVimInstanceId() {
        return vimInstanceId;
    }
    public void setVimInstanceId(String vimInstanceId) {
        this.vimInstanceId = vimInstanceId;
    }
    public String getVnfmInstanceId() {
        return vnfmInstanceId;
    }
    public void setVnfmInstanceId(String vnfmInstanceId) {
        this.vnfmInstanceId = vnfmInstanceId;
    }


}
