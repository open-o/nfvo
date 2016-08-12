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
package org.openo.orchestrator.nfv.umc.drill.wrapper.common;

/**
 * @author 10188044
 * @date 2015-8-13
 *       <p/>
 *       the constants used in the drill module
 */
public class TopologyConsts {

    /** operation result **/
    public static final String OPERATIONS_RESULT_SUCCESS = "SUCCESS";
    public static final String OPERATIONS_RESULT_FAIL = "FAIL";

    /** render types used on the page **/
    // the system itself
    public static final String RENDERTYPE_ROOT = "root";
    public static final String RENDERTYPE_NS = "ns";
    public static final String RENDERTYPE_VNF = "vnf";
    public static final String RENDERTYPE_VNFC = "vnfc";
    public static final String RENDERTYPE_VDU = "vdu";
    public static final String RENDERTYPE_HOST = "host";

    /** the index of the Handlers **/
    // drill handler
    public static final int HANDlER_DRILL_NS = 0;
    public static final int HANDlER_DRILL_VNF = 1;
    public static final int HANDlER_DRILL_VNFC = 2;
    public static final int HANDlER_DRILL_VDU = 3;
    public static final int HANDlER_DRILL_HOST = 4;
    // layer handler
    public static final int HANDlER_LAYERMONITOR_NS = 5;
    public static final int HANDlER_LAYERMONITOR_VDU = 6;
    public static final int HANDlER_LAYERMONITOR_HOST = 7;

    /** the name and moc of the system itself **/
    public static final String NODE_ROOT_NAME = "root";
    public static final String NODE_ROOT_MOC = "nfv.root";

}
