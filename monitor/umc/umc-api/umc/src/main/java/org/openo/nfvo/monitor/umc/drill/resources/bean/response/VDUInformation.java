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
package org.openo.nfvo.monitor.umc.drill.resources.bean.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.openo.nfvo.monitor.umc.drill.wrapper.common.TopologyConsts;
import org.openo.nfvo.monitor.umc.drill.wrapper.handler.ResManagerService;
import org.openo.nfvo.monitor.umc.drill.wrapper.resources.bean.VduData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"host_id"})
public class VDUInformation extends NodeInformation {
    private String vdu_image;
    private String ip_addresses;
    private String host_id;
    private String createtime;
    private String vim_id;
    private String vim_name;
    private final String rendertype = TopologyConsts.RENDERTYPE_VDU;

    public VDUInformation(VduData vduData) {
        this(vduData.getVduImage(), vduData.getIpAddress(), vduData.getHostId(), vduData
                .getCreateTime(), vduData.getVimId(), ResManagerService.getVimName(vduData
                .getVimId()));
        this.setId(vduData.getOid());
        this.setName(vduData.getName());
        this.setMoc(vduData.getMoc());
        this.setMoc_name(vduData.getMocName());
    }
}
