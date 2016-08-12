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
package org.openo.orchestrator.nfv.umc.i18n;

import java.util.List;
import java.util.Map;

import org.openo.orchestrator.nfv.umc.i18n.common.I18nCacheBuilder;
import org.openo.orchestrator.nfv.umc.i18n.dict.EnUsDict;
import org.openo.orchestrator.nfv.umc.i18n.dict.ZhCnDict;

/**
 * @author wangjiangping
 *
 */
public class I18n implements I18nOpr{
    private static List<Map<String, String>> dictCache = null;

    protected I18n() {

    }

    public static void init(){
        dictCache = new I18nCacheBuilder().buildI18nCache();
    }

    public static I18n getInstance(String language) {
            if (language == null) {
                return EnUsDict.getInstance(dictCache);
            }

            if (I18nCacheBuilder.EN_US.equalsIgnoreCase(language)) {
                return EnUsDict.getInstance(dictCache);
            } else if (I18nCacheBuilder.ZH_CN.equalsIgnoreCase(language)) {
                return ZhCnDict.getInstance(dictCache);
            }

            return EnUsDict.getInstance(dictCache);
    }

    @Override
    public String translate(String key) {
        return "";
    }

    @Override
    public String translate(String key, Map<String, String> vars) {
        return "";
    }

}
