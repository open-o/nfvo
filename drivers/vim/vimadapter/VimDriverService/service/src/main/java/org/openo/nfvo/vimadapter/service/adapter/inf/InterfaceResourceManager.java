/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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

package org.openo.nfvo.vimadapter.service.adapter.inf;

import net.sf.json.JSONObject;

/**
 * Resource management interface.<br/>
 * 
 * @author
 * @version NFVO 0.5 Aug 24, 2016
 */
public interface InterfaceResourceManager {

    /**
     * It is used to query respool.<br/>
     * 
     * @param paramJson the information of VIM
     * @return the result of querying respool
     * @since NFVO 0.5
     */
    JSONObject getCpuLimits(JSONObject paramJson);

    /**
     * It is used to query limit.<br/>
     * 
     * @param paramJson the information of VIM
     * @return the result of querying limit
     * @since NFVO 0.5
     */
    JSONObject getDiskLimits(JSONObject paramJson);

    /**
     * It is used to query networks.<br/>
     * 
     * @param paramJson the information of VIM
     * @return the result of querying networks
     * @since NFVO 0.5
     */
    JSONObject getNetworks(JSONObject paramJson);

    /**
     * It is used to query hosts.<br/>
     * 
     * @param paramJson the information of VIM
     * @return the result of querying hosts
     * @since NFVO 0.5
     */
    JSONObject getHosts(JSONObject paramJson);

    /**
     * It is used to query ports.<br/>
     * 
     * @param paramJson the information of VIM
     * @return the result of querying ports
     * @since NFVO 0.5
     */
    JSONObject getPorts(JSONObject paramJson);

    /**
     * Retrieves projects
     * @param paramJson JSONObject
     * @return JSONObject project JSON
     * @since NFVO 0.5
     */
    JSONObject getProjects(JSONObject paramJson);
}
