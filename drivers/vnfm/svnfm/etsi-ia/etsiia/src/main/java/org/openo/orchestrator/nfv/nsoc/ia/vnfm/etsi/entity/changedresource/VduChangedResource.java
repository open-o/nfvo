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

import java.util.List;

public class VduChangedResource {
	private String vduInstanceId;
	private String vduName;
	private String nodeTemplateId;
	private String type;

	private String vmId;

	private String vnfInstanceId;
	private String vimInstanceId;
	private String hostName;
	private List<IpObj> ipAddresses;
	public String getVduInstanceId() {
		return vduInstanceId;
	}
	public void setVduInstanceId(String vduInstanceId) {
		this.vduInstanceId = vduInstanceId;
	}
	public String getVduName() {
		return vduName;
	}
	public void setVduName(String vduName) {
		this.vduName = vduName;
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
	public String getVmId() {
		return vmId;
	}
	public void setVmId(String vmId) {
		this.vmId = vmId;
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
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public List<IpObj> getIpAddresses() {
		return ipAddresses;
	}
	public void setIpAddresses(List<IpObj> ipAddresses) {
		this.ipAddresses = ipAddresses;
	}

}
