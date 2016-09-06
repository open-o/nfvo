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

public class InstantiateVnfRequest {
    private String vnfInstanceName;
    private String vnfPackageId;
    private String vnfDescriptorId;
    private String flavorId;
    private Object additionalParam;
    public String getVnfInstanceName() {
        return vnfInstanceName;
    }
    public void setVnfInstanceName(String vnfInstanceName) {
        this.vnfInstanceName = vnfInstanceName;
    }
    public String getVnfPackageId() {
        return vnfPackageId;
    }
    public void setVnfPackageId(String vnfPackageId) {
        this.vnfPackageId = vnfPackageId;
    }
    public String getVnfDescriptorId() {
        return vnfDescriptorId;
    }
    public void setVnfDescriptorId(String vnfDescriptorId) {
        this.vnfDescriptorId = vnfDescriptorId;
    }
    public String getFlavorId() {
        return flavorId;
    }
    public void setFlavorId(String flavorId) {
        this.flavorId = flavorId;
    }
    public Object getAdditionalParam() {
        return additionalParam;
    }
    public void setAdditionalParam(Object additionalParam) {
        this.additionalParam = additionalParam;
    }

}
