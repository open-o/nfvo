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

import java.util.List;

public class VnfInfoFromVnfm {
    private String vnfInstanceId;
    private String vnfInstanceName;
    private String vnfDescriptorId;
    private String vnfType;
    private String vnfmID;
    private String instantiatedAt;
    private String status;
    private int  runningTime;
    private String[] errMsg;
    private String self;
    private List<VduInfoFromVnfm> vduList;

    public String getVnfInstanceId() {
        return vnfInstanceId;
    }
    public void setVnfInstanceId(String vnfInstanceId) {
        this.vnfInstanceId = vnfInstanceId;
    }
    public String getVnfInstanceName() {
        return vnfInstanceName;
    }
    public void setVnfInstanceName(String vnfInstanceName) {
        this.vnfInstanceName = vnfInstanceName;
    }
    public String getVnfDescriptorId() {
        return vnfDescriptorId;
    }
    public void setVnfDescriptorId(String vnfDescriptorId) {
        this.vnfDescriptorId = vnfDescriptorId;
    }
    public String getVnfType() {
        return vnfType;
    }
    public void setVnfType(String vnfType) {
        this.vnfType = vnfType;
    }
    public String getVnfmID() {
        return vnfmID;
    }
    public void setVnfmID(String vnfmID) {
        this.vnfmID = vnfmID;
    }
    public String getInstantiatedAt() {
        return instantiatedAt;
    }
    public void setInstantiatedAt(String instantiatedAt) {
        this.instantiatedAt = instantiatedAt;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getRunningTime() {
        return runningTime;
    }
    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }
    public String[] getErrMsg() {
        return errMsg;
    }
    public void setErrMsg(String[] errMsg) {
        this.errMsg = errMsg;
    }
    public String getSelf() {
        return self;
    }
    public void setSelf(String self) {
        this.self = self;
    }
    public List<VduInfoFromVnfm> getVduList() {
        return vduList;
    }
    public void setVduList(List<VduInfoFromVnfm> vduList) {
        this.vduList = vduList;
    }

}
