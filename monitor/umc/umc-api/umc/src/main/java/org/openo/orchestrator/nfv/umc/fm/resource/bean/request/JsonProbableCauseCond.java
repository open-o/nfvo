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
package org.openo.orchestrator.nfv.umc.fm.resource.bean.request;

/**
 * probable cause condition in request of query current alarm
 */

import java.util.Arrays;

import org.jdom.Element;

public class JsonProbableCauseCond {

    private static final String PROBABLECAUSE_COND_PREFIX = "<ProbableCause CODE=\"%s\"";

    private static final String PROBABLECAUSE_COND_SUFFIX =
            " SYSTEMTYPE=\"%s\" NEGATIONAL=\"false\" />";

    private short systemType;

    private long[] codes = new long[0];

    public short getSystemType() {
        return systemType;
    }

    public void setSystemType(short systemType) {
        this.systemType = systemType;
    }

    public long[] getCodes() {
        return codes;
    }

    public void setCodes(long[] codes) {
        this.codes = codes;
    }

    public String getRuleDataStr() {
        StringBuilder builder = new StringBuilder("");
        if (codes != null && codes.length > 0) {
            for (long code : codes) {

                builder.append(String.format(PROBABLECAUSE_COND_PREFIX, code)).append(
                        String.format(PROBABLECAUSE_COND_SUFFIX, systemType));
            }
        } else {
            builder.append(String.format(PROBABLECAUSE_COND_PREFIX, -1)).append(
                    String.format(PROBABLECAUSE_COND_SUFFIX, systemType));
        }
        return builder.toString();
    }

    public void addOneCode(long code) {
        if (this.getCodes() == null || this.getCodes().length == 0) {
            return;
        }
        int length = this.getCodes().length;
        long[] codes = new long[length + 1];
        for (int j = 0; j < length; j++) {
            codes[j] = this.getCodes()[j];
        }
        codes[length] = code;
        this.setCodes(codes);
    }

    public JsonProbableCauseCond fromRuleDataStr(Element e) {
        if (e == null) {
            return null;
        }
        JsonProbableCauseCond probableCause = new JsonProbableCauseCond();
        probableCause.setSystemType(Short.valueOf(e.getAttribute("SYSTEMTYPE").getValue()));
        long code = Long.valueOf(e.getAttribute("CODE").getValue());
        if (code != -1l) {
            probableCause.setCodes(new long[] {code});
        }
        return probableCause;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(codes);
        result = prime * result + systemType;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        JsonProbableCauseCond other = (JsonProbableCauseCond) obj;
        if (!Arrays.equals(codes, other.codes)) return false;
        if (systemType != other.systemType) return false;
        return true;
    }
}
