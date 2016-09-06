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
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.common;


public class MSBUtil {

    public static String getRocBaseUrl() {
        return Config.getConfigration().getMsbServerAddr() + getRocApiRootDomain();
    }

    private static String getRocApiRootDomain() {
        return "/api/roc/v1";
    }

    public static String getNsocBaseUrl() {
        return Config.getConfigration().getMsbServerAddr() + getNsocApiRootDomain();
    }

    private static String getNsocApiRootDomain() {
        return "/api/nsoc/v1";
    }

    public static String getHsifBaseUrl()
    {
        return Config.getConfigration().getMsbServerAddr() + getHsifApiRootDomain();
    }

    private static String getHsifApiRootDomain() {

        return "/api/hsif/v1";
    }
}
