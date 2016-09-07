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

import java.util.ArrayList;

public class PortChainReq4N {
    private String sdnControllerId;
    private String url;
    private String name;
    private String description;
    private ArrayList<String> flowClassifiers;
    private ArrayList<String> portPairGroups;
    private boolean symmetric;

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

    public ArrayList<String> getFlowClassifiers() {
        return flowClassifiers;
    }

    public void setFlowClassifiers(ArrayList<String> flowClassifiers) {
        this.flowClassifiers = flowClassifiers;
    }

    public ArrayList<String> getPortPairGroups() {
        return portPairGroups;
    }

    public void setPortPairGroups(ArrayList<String> portPairGroups) {
        this.portPairGroups = portPairGroups;
    }

    public boolean isSymmetric() {
        return symmetric;
    }

    public void setSymmetric(boolean symmetric) {
        this.symmetric = symmetric;
    }
}
