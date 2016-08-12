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
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.changedresource;

public class NetworkChangedResource {
	private String networkInstanceId;
	private String networkName;
	private String nodeTemplateId;
	private String type;//

	private String networkId;

	private String vimInstanceId;
	private String instanceId;
	public String getNetworkInstanceId() {
		return networkInstanceId;
	}
	public void setNetworkInstanceId(String networkInstanceId) {
		this.networkInstanceId = networkInstanceId;
	}
	public String getNetworkName() {
		return networkName;
	}
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
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
	public String getNetworkId() {
		return networkId;
	}
	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
	public String getVimInstanceId() {
		return vimInstanceId;
	}
	public void setVimInstanceId(String vimInstanceId) {
		this.vimInstanceId = vimInstanceId;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
}
