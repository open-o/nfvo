/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 * */
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
