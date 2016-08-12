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
package org.openo.orchestrator.nfv.umc.drill.resources.bean.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author 10188044
 * @date 2015-8-13
 *       <p>
 *       the response type of all the rest request
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TopologyResp {
    // the current node
    private NodeInformation self;
    // parent nodes of the current node
    private NodeInformation[] parents;
    // child nodes of the current node
    private NodeInformation[] childs;
    // (SUCCESS or FAIL)represent the process result
    private String operationresult;
    // the error info(if errorinfo is null,not inclued in the json result info)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorinfo;
}
