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
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VnfcData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"vdu_id", "vnf_id"})
public class VNFCInformation extends NodeInformation {
    private String vdu_id;
    private String vnf_id;
    private String type;
    private String vendor;
    private final String rendertype = TopologyConsts.RENDERTYPE_VNFC;

    public VNFCInformation(VnfcData vnfcData) {
        this(vnfcData.getVduId(), vnfcData.getVnfId(), vnfcData.getType(), vnfcData.getVendor());
        this.setId(vnfcData.getOid());
        this.setName(vnfcData.getName());
        this.setMoc(vnfcData.getMoc());
        this.setMoc_name(vnfcData.getMocName());
    }

}
