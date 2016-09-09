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
package org.openo.nfvo.monitor.umc.i18n;

import java.util.Map;

/**
 * 
 * @date 2016/4/20 11:00:11
 * @description Opration interface define
 */
public interface I18nOpr {
    /**
     * Get dist-language value by input key from dict. if not found,
     * return orginal key.
     *
     * @param key
     * @return dist-language value
     */
    public String translate(String key);

    /**
     * Get vars_template by input key from dict. if not found, return
     * orginal key.
     *
     * and then return dist-language sentence by vars_template filled with
     * addition vars. ${var} in vars_template will be replaced by vars.get(var).
     *
     * @param key,
     *            vars
     * @return dist-language sentence
     */
    public String translate(String key, Map<String, String> vars);
}
