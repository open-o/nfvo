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
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.process;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class OperationExecutionProgress {
    public static final String STEP_STATUS_START="start";
    public static final String STEP_STATUS_END="end";
    public static final String STEP_STATUS_ERROR="error";
    public static final String ZERO_PERCENT = "0%";
    public static final String HUNDRED_PERCENT="100%";

    @XmlElement
    private String status;
    @XmlElement
    private CurrentStepInfo currentStepInfo;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public CurrentStepInfo getCurrentStepInfo() {
        return currentStepInfo;
    }
    public void setCurrentStepInfo(CurrentStepInfo currentStepInfo) {
        this.currentStepInfo = currentStepInfo;
    }
}