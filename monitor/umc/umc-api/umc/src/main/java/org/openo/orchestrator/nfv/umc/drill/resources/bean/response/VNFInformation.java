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
package org.openo.orchestrator.nfv.umc.drill.resources.bean.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.openo.orchestrator.nfv.umc.drill.wrapper.common.TopologyConsts;
import org.openo.orchestrator.nfv.umc.drill.wrapper.handler.ResManagerService;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VnfData;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class VNFInformation extends NodeInformation {
    private String status;
    private String vendor;
    private String version;
    private String vnf_address;
    private String vnfm_id;
    private String vnfm_name;
    private final String rendertype = TopologyConsts.RENDERTYPE_VNF;

    /**
     * create VNF Node using the VnfData info
     *
     * @param vnfData
     */
    public VNFInformation(VnfData vnfData) {
        this(vnfData.getStatus(), vnfData.getVendor(), vnfData.getVersion(),
                vnfData.getIpAddress(), vnfData.getVnfmid(), ResManagerService.getVnfmName(vnfData
                        .getVnfmid()));
        this.setId(vnfData.getOid());
        this.setName(vnfData.getName());
        this.setMoc(vnfData.getMoc());
        this.setMoc_name(vnfData.getMocName());
    }

}
