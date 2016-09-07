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
package com.zte.ums.sfc.console.entity.portpair;

import java.util.Map;


public class PortPairReq4N {
    private String sdnControllerId;
    private String url;
    private String name;
    private String description;
    private String sfType;
    private boolean nshAware;
    private boolean requestReclassification;
    private PortInfo ingress;
    private PortInfo egress;
    private Map sfParam;

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

    public String getSfType() {
        return sfType;
    }

    public void setSfType(String sfType) {
        this.sfType = sfType;
    }

    public boolean isNshAware() {
        return nshAware;
    }

    public void setNshAware(boolean nshAware) {
        this.nshAware = nshAware;
    }

    public boolean isRequestReclassification() {
        return requestReclassification;
    }

    public void setRequestReclassification(boolean requestReclassification) {
        this.requestReclassification = requestReclassification;
    }

    public PortInfo getIngress() {
        return ingress;
    }

    public void setIngress(PortInfo ingress) {
        this.ingress = ingress;
    }

    public PortInfo getEgress() {
        return egress;
    }

    public void setEgress(PortInfo egress) {
        this.egress = egress;
    }

    public Map getSfParam() {
        return sfParam;
    }

    public void setSfParam(Map sfParam) {
        this.sfParam = sfParam;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
