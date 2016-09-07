/**
 * Copyright 2016 [ZTE] and others.
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
package com.zte.ums.sfc.console.entity;

public class FlowClassfierReq4N {
    private String url;
    private String sdnControllerId;
    private String name;
    private String description;
    private int dscp;
    private int ipProto;
    private String sourcePortRange;
    private String destPortRange;
    private String sourceIpRange;
    private String destIpRange;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSdnControllerId() {
        return sdnControllerId;
    }

    public void setSdnControllerId(String sdnControllerId) {
        this.sdnControllerId = sdnControllerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDscp() {
        return dscp;
    }

    public void setDscp(int dscp) {
        this.dscp = dscp;
    }

    public int getIpProto() {
        return ipProto;
    }

    public void setIpProto(int ipProto) {
        this.ipProto = ipProto;
    }

    public String getSourcePortRange() {
        return sourcePortRange;
    }

    public void setSourcePortRange(String sourcePortRange) {
        this.sourcePortRange = sourcePortRange;
    }

    public String getDestPortRange() {
        return destPortRange;
    }

    public void setDestPortRange(String destPortRange) {
        this.destPortRange = destPortRange;
    }

    public String getSourceIpRange() {
        return sourceIpRange;
    }

    public void setSourceIpRange(String sourceIpRange) {
        this.sourceIpRange = sourceIpRange;
    }

    public String getDestIpRange() {
        return destIpRange;
    }

    public void setDestIpRange(String destIpRange) {
        this.destIpRange = destIpRange;
    }
}
