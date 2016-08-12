/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.orchestrator.nfv.umc.pm.task.bean;

import org.openo.orchestrator.nfv.umc.i18n.I18n;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class PmTaskInfo {
    private int taskId;
    private String taskName;
    private String neTypeId;
    private String neTypeName;
    private String mocId;
    private String poId;
    private String poName;
    private String taskStatus = "0";
    private String version = "-1";
    private String granularity;
    private long beginTime = 0;
    private long endTime = 0;

    public void translate(String language){
        I18n i18n = I18n.getInstance(language);

        if(i18n==null){
            return;
        }

        this.neTypeName = i18n.translate(this.neTypeName);
        this.taskName = i18n.translate(this.taskName);
        this.poName = i18n.translate(this.poName);
    }
}
