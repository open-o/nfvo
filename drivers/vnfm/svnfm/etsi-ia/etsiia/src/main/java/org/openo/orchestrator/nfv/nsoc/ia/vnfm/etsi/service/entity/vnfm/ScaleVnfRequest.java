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
