/**
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
package org.openo.nfvo.monitor.umc.fm.resource.bean.response;

import org.openo.nfvo.monitor.umc.i18n.I18n;;

/**
 * result of query probable cause
 *
 */
public class ProbableCauseQueryResult {

    private int systemType;

    private long code;

    private String codeName;

    private String systemTypeName;

    private int defaultSeverity;

    private int customSeverity;

    public int getSystemType() {
        return systemType;
    }

    public void setSystemType(int systemType) {
        this.systemType = systemType;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getSystemTypeName() {
        return systemTypeName;
    }

    public void setSystemTypeName(String systemTypeName) {
        this.systemTypeName = systemTypeName;
    }

    public int getDefaultSeverity() {
        return defaultSeverity;
    }

    public void setDefaultSeverity(int defaultSeverity) {
        this.defaultSeverity = defaultSeverity;
    }

    public int getCustomSeverity() {
        return customSeverity;
    }

    public void setCustomSeverity(int customSeverity) {
        this.customSeverity = customSeverity;
    }

    public void translate(String language) {
        I18n i18n = I18n.getInstance(language);

        if(i18n==null){
            return;
        }

        this.codeName = i18n.translate(this.codeName);
        this.systemTypeName = i18n.translate(this.systemTypeName);
    }

}
