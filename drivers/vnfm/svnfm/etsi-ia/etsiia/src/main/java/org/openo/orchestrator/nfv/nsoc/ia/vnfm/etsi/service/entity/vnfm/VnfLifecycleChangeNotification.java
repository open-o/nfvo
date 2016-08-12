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

import java.util.List;

public class VnfLifecycleChangeNotification {
    private String eventType;
    private String operation;
    private List<VnfInfoFromVnfm> vnfList;
    private List<VduInfoFromVnfm> vduList;
    private List<VnfcInfoFromVnfm> vnfcList;
    private List<VirtualLinkFromVnfm> vlList;
    private List<CpInfoFromVnfm> cpList;
    private List<NetworkInfoFromVnfm> networkList;

    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }
    public List<VnfInfoFromVnfm> getVnfList() {
        return vnfList;
    }
    public void setVnfList(List<VnfInfoFromVnfm> vnfList) {
        this.vnfList = vnfList;
    }
    public List<VduInfoFromVnfm> getVduList() {
        return vduList;
    }
    public void setVduList(List<VduInfoFromVnfm> vduList) {
        this.vduList = vduList;
    }
    public List<VnfcInfoFromVnfm> getVnfcList() {
        return vnfcList;
    }
    public void setVnfcList(List<VnfcInfoFromVnfm> vnfcList) {
        this.vnfcList = vnfcList;
    }
    public List<VirtualLinkFromVnfm> getVlList() {
        return vlList;
    }
    public void setVlList(List<VirtualLinkFromVnfm> vlList) {
        this.vlList = vlList;
    }
    public List<CpInfoFromVnfm> getCpList() {
        return cpList;
    }
    public void setCpList(List<CpInfoFromVnfm> cpList) {
        this.cpList = cpList;
    }
    public List<NetworkInfoFromVnfm> getNetworkList() {
        return networkList;
    }
    public void setNetworkList(List<NetworkInfoFromVnfm> networkList) {
        this.networkList = networkList;
    }

}
