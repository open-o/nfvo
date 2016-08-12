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

public class ScheduleResponse {
    private int nextStepNo;
    private String status;
    private String progressJson;
    public int getNextStepNo() {
        return nextStepNo;
    }
    public void setNextStepNo(int nextStepNo) {
        this.nextStepNo = nextStepNo;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getProgressJson() {
        return progressJson;
    }
    public void setProgressJson(String progressJson) {
        this.progressJson = progressJson;
    }

}
