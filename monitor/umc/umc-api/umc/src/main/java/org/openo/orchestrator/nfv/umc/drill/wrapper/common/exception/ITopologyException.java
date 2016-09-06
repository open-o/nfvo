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
package org.openo.orchestrator.nfv.umc.drill.wrapper.common.exception;

/**
 *       define the error codes used in this module
 */
public interface ITopologyException {

    /** the base code of the drill module **/
    public static final int ERR_MONITOREXCEPTION_BASE = 22000;

    /** the network error **/
    public static final int ERR_MONITOR_TOPOLOGY_NETWORKERROR = ERR_MONITOREXCEPTION_BASE + 1;

    /** the timeout error **/
    public static final int ERR_MONITOR_TOPOLOGY_READTIMEOUT = ERR_MONITOREXCEPTION_BASE + 2;

    /** call the back resource service throws exception **/
    public static final int ERR_MONITOR_TOPOLOGY_CALLSERVICEEXCEPTION =
            ERR_MONITOREXCEPTION_BASE + 3;

    /** the back resource services return result , but the flag is FAIL **/
    public static final int ERR_MONITOR_TOPOLOGY_CALLSERVICEFAULT = ERR_MONITOREXCEPTION_BASE + 4;

    /** the id in the request dose not match any node,it means the node dose not exist **/
    public static final int ERR_MONITOR_TOPOLOGY_NODENOTFOUND = ERR_MONITOREXCEPTION_BASE + 5;

    /** while assembing the drill response, it throws exceptiong **/
    public static final int ERR_MONITOR_TOPOLOGY_ASSEMBLEDATAFAIL = ERR_MONITOREXCEPTION_BASE + 6;


}
