/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 * */
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm;

public class ScaleVnfRequest {
    private String type;
    private Object aspect;
    private Object additionalParam;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Object getAspect() {
        return aspect;
    }
    public void setAspect(Object aspect) {
        this.aspect = aspect;
    }
    public Object getAdditionalParam() {
        return additionalParam;
    }
    public void setAdditionalParam(Object additionalParam) {
        this.additionalParam = additionalParam;
    }



}
