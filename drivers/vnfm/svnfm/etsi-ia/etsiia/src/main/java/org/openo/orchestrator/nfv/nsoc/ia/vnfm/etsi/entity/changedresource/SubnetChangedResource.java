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

public class SubnetChangedResource {
	private String vlInstanceId;
	private String vlName;
	private String nodeTemplateId;
	private String type;

	private String subnetId;

	private String refNetwork;
	private String vnfInstanceId;
	private String vimInstanceId;
	public String getVlInstanceId() {
		return vlInstanceId;
	}
	public void setVlInstanceId(String vlInstanceId) {
		this.vlInstanceId = vlInstanceId;
	}
	public String getVlName() {
		return vlName;
	}
	public void setVlName(String vlName) {
		this.vlName = vlName;
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
	public String getSubnetId() {
		return subnetId;
	}
	public void setSubnetId(String subnetId) {
		this.subnetId = subnetId;
	}
	public String getRefNetwork() {
		return refNetwork;
	}
	public void setRefNetwork(String refNetwork) {
		this.refNetwork = refNetwork;
	}
	public String getVnfInstanceId() {
		return vnfInstanceId;
	}
	public void setVnfInstanceId(String vnfInstanceId) {
		this.vnfInstanceId = vnfInstanceId;
	}
	public String getVimInstanceId() {
		return vimInstanceId;
	}
	public void setVimInstanceId(String vimInstanceId) {
		this.vimInstanceId = vimInstanceId;
	}

}
