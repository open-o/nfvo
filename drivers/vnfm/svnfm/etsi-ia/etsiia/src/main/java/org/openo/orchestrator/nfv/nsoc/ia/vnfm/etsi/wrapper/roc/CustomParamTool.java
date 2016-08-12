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

package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.wrapper.roc;

public class CustomParamTool {
    public static String STR_OPERATING = "so.lifecycle.operating=";
    public static String CUSTOMPARAM_SEPARATOR = ";";

    public static String parseOperatingCustomParam(String customparam) {
        String vnfOperation = "";
        if (customparam == null) {
            return vnfOperation;
        }

        String[] customparams = customparam
                .split(CustomParamTool.CUSTOMPARAM_SEPARATOR);
        for (int i = 0; customparams != null && i < customparams.length; i++) {
            if (customparams[i].contains(CustomParamTool.STR_OPERATING)) {
                vnfOperation = customparams[i]
                        .substring(CustomParamTool.STR_OPERATING.length());
            }
        }

        return vnfOperation;
    }

    public static String assembleOperatingCustomParam(String vnfOperating,
            String customstr) {
        String newCustomStr = null;
        if (customstr == null) {
            newCustomStr = CustomParamTool.STR_OPERATING + vnfOperating;// null,set
        } else {
            if (customstr.contains(CustomParamTool.STR_OPERATING)) {
                String[] customparams = customstr
                        .split(CustomParamTool.CUSTOMPARAM_SEPARATOR);
                for (int i = 0; i < customparams.length; i++) {
                    if (customparams[i].contains(CustomParamTool.STR_OPERATING)) {
                        newCustomStr = customstr.replaceAll(customparams[i],
                                CustomParamTool.STR_OPERATING + vnfOperating);// have,replace
                    }
                }
            } else {
                newCustomStr = customstr + CustomParamTool.STR_OPERATING
                        + vnfOperating;// not have,add
            }
        }

        return newCustomStr;
    }
}
