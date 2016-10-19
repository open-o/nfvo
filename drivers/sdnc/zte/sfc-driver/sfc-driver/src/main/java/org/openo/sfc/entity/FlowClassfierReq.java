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
package org.openo.sfc.entity;

import com.google.gson.annotations.SerializedName;

public class FlowClassfierReq {
    private String protocol;
    @SerializedName("source-port-range-min")
    private int sourcePortRangeMin;
    @SerializedName("source-port-range-max")
    private int sourcePortRangeMax;
    @SerializedName("destination-port-range-min")
    private int destinationPortRangeMin;
    @SerializedName("destination-port-range-max")
    private int destinationPortRangeMax;
    @SerializedName("source-ip-range")
    private String sourceIpRange;
    @SerializedName("destination-ip-range")
    private String destinationIpRange;
    @SerializedName("ip-dscp")
    private long ipDscp;
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getIpDscp() {
        return ipDscp;
    }

    public void setIpDscp(long ipDscp) {
        this.ipDscp = ipDscp;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getSourcePortRangeMin() {
        return sourcePortRangeMin;
    }

    public void setSourcePortRangeMin(int sourcePortRangeMin) {
        this.sourcePortRangeMin = sourcePortRangeMin;
    }

    public int getSourcePortRangeMax() {
        return sourcePortRangeMax;
    }

    public void setSourcePortRangeMax(int sourcePortRangeMax) {
        this.sourcePortRangeMax = sourcePortRangeMax;
    }

    public int getDestinationPortRangeMin() {
        return destinationPortRangeMin;
    }

    public void setDestinationPortRangeMin(int destinationPortRangeMin) {
        this.destinationPortRangeMin = destinationPortRangeMin;
    }

    public int getDestinationPortRangeMax() {
        return destinationPortRangeMax;
    }

    public void setDestinationPortRangeMax(int destinationPortRangeMax) {
        this.destinationPortRangeMax = destinationPortRangeMax;
    }


    public String getSrcIpRange() {
        return sourceIpRange;
    }

    public void setSrcIpRange(String srcIpRange) {
        this.sourceIpRange = srcIpRange;
    }

    public String getDestinationIpRange() {
        return destinationIpRange;
    }

    public void setDestinationIpRange(String destinationIpRange) {
        this.destinationIpRange = destinationIpRange;
    }
}
