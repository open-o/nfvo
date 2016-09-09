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
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm;

public class VnfmEnumeration {
    public static final String OPERATE_START = "start";
    public static final String OPERATE_STOP = "stop";

    public static final String SCALE_IN = "scaleIn";
    public static final String SCALE_OUT = "scaleOut";
    public static final String SCALE_UP = "scaleUp";
    public static final String SCALE_DOWN = "scaleDown";

    public static final String SCALE_BY_TYPE = "byType";
    public static final String SCALE_BY_INSTANCE = "byInstance";

    public static final String TERMINATION_TYPE_FORCE = "forceful";
    public static final String TERMINATION_TYPE_GRACE = "graceful";

    public static final String IP_FIXED_TYPE = "fixed";
    public static final String IP_FLOATING_TYPE = "floating";
}
