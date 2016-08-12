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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.openo.orchestrator.nfv.umc.drill.wrapper.common.TopologyConsts;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.HostData;

/**
 * @author 10188044
 * @date 2015-8-12
 *       <p/>
 *       Detail information of Host
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class HostInformation extends NodeInformation {
    private String ip_addresses;
    private String user;
    private String protocol;
    private String port;
    private final String rendertype = TopologyConsts.RENDERTYPE_HOST;

    public HostInformation(HostData hostData) {
        this(hostData.getIpAddress(), hostData.getUser(), hostData.getProtocol(), hostData
                .getPort());
        this.setId(hostData.getOid());
        this.setName(hostData.getName());
        this.setMoc(hostData.getMoc());
        this.setMoc_name(hostData.getMocName());
    }

}
