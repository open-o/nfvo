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

public class ChangedResource {
	private int eventType;//1,add;2,delete,3,update
	private int operationType;
	private List<VnfChangedResource> vnfList;
	private List<VduChangedResource> vduList;
	private List<VnfcChangedResource> vnfcList;
	private List<NetworkChangedResource> networkList;
	private List<SubnetChangedResource> vlList;
	private List<CPChangedResource> cpList;
	public int getEventType() {
		return eventType;
	}
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}
	public int getOperationType() {
		return operationType;
	}
	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}
	public List<VnfChangedResource> getVnfList() {
		return vnfList;
	}
	public void setVnfList(List<VnfChangedResource> vnfList) {
		this.vnfList = vnfList;
	}
	public List<VduChangedResource> getVduList() {
		return vduList;
	}
	public void setVduList(List<VduChangedResource> vduList) {
		this.vduList = vduList;
	}
	public List<VnfcChangedResource> getVnfcList() {
		return vnfcList;
	}
	public void setVnfcList(List<VnfcChangedResource> vnfcList) {
		this.vnfcList = vnfcList;
	}
	public List<NetworkChangedResource> getNetworkList() {
		return networkList;
	}
	public void setNetworkList(List<NetworkChangedResource> networkList) {
		this.networkList = networkList;
	}
	public List<SubnetChangedResource> getVlList() {
		return vlList;
	}
	public void setVlList(List<SubnetChangedResource> vlList) {
		this.vlList = vlList;
	}
	public List<CPChangedResource> getCpList() {
		return cpList;
	}
	public void setCpList(List<CPChangedResource> cpList) {
		this.cpList = cpList;
	}

}
