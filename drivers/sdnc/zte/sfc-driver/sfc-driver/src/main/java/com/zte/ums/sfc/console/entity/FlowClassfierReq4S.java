/**
 *       Copyright (C) 2016 ZTE, Inc. and others. All rights reserved. (ZTE)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zte.ums.sfc.console.entity;

public class FlowClassfierReq4S {
    private String protocol;
    private int sourcePortRangeMin;
    private int sourcePortRangeMax;
    private int destinationPortRangeMin;
    private int destinationPortRangeMax;
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
}
