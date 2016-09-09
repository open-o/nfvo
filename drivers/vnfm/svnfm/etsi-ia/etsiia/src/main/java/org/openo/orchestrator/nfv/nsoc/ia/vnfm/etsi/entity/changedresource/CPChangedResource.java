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

public class CPChangedResource {
	private String cpInstanceId;
	private String cpName;
	private String nodeTemplateId;
	private String type;

	private String ipAddress;
	private String floatingIP;

	private String vlInstanceId;
	private String vduInstanceId;
	private String vnfInstanceId;
	public String getCpInstanceId() {
		return cpInstanceId;
	}
	public void setCpInstanceId(String cpInstanceId) {
		this.cpInstanceId = cpInstanceId;
	}
	public String getCpName() {
		return cpName;
	}
	public void setCpName(String cpName) {
		this.cpName = cpName;
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
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getFloatingIP() {
		return floatingIP;
	}
	public void setFloatingIP(String floatingIP) {
		this.floatingIP = floatingIP;
	}
	public String getVlInstanceId() {
		return vlInstanceId;
	}
	public void setVlInstanceId(String vlInstanceId) {
		this.vlInstanceId = vlInstanceId;
	}
	public String getVduInstanceId() {
		return vduInstanceId;
	}
	public void setVduInstanceId(String vduInstanceId) {
		this.vduInstanceId = vduInstanceId;
	}
	public String getVnfInstanceId() {
		return vnfInstanceId;
	}
	public void setVnfInstanceId(String vnfInstanceId) {
		this.vnfInstanceId = vnfInstanceId;
	}

}
