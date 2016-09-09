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
package org.openo.nfvo.monitor.umc.pm.services;

import org.openo.nfvo.monitor.umc.pm.common.PmException;
import org.openo.nfvo.monitor.umc.pm.common.PmOsfUtil;

public class PmService{
//    private static final DebugPrn dMsg = new DebugPrn(PmService.class.getName());

    public static void reStartAllPmTask(String proxyIp) throws PmException {
        PmOsfUtil.reStartAllPmTask(proxyIp);
    }




}
