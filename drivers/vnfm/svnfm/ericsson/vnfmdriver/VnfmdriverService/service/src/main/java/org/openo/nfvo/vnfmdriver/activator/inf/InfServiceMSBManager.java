/*
 * Copyright (c) 2017 Ericsson (China) Communication Co. Ltd.
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

package org.openo.nfvo.vnfmdriver.activator.inf;

import java.util.Map;

import net.sf.json.JSONObject;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Feb 15, 2017
 */
public interface InfServiceMSBManager {

    /**
     * <br>
     *
     * @param paramsMap
     * @param driverInfo
     * @return
     * @since NFVO 0.5
     */
    int register(Map<String, String> paramsMap, JSONObject driverInfo);

    /**
     * <br>
     *
     * @param paramsMap
     * @param driverInfo
     * @return
     * @since NFVO 0.5
     */
    int unRegister(Map<String, String> paramsMap, JSONObject driverInfo);
}
