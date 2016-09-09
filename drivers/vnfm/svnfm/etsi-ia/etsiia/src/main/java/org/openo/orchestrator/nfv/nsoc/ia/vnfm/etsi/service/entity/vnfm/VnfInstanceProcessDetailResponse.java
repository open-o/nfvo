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

import java.util.List;

public class VnfInstanceProcessDetailResponse {
    private List<RelLink> links;
    private List<ProcessEntity> entries;

    public List<RelLink> getLinks() {
        return links;
    }

    public void setLinks(List<RelLink> links) {
        this.links = links;
    }

    public List<ProcessEntity> getEntries() {
        return entries;
    }

    public void setEntries(List<ProcessEntity> entries) {
        this.entries = entries;
    }

}
