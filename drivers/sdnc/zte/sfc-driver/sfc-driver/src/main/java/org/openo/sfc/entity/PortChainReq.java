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

import java.util.ArrayList;
import java.util.Map;

public class PortChainReq {
    @SerializedName("port-pair-groups")
    private ArrayList<String> portPairGroups;
    @SerializedName("flow-classifiers")
    private ArrayList<String> flowClassifiers;
    private String uuid;
    @SerializedName("chain-parameters")
    private ArrayList<ChainParameter> chainParams;


    public ArrayList<ChainParameter> getChainParams() {
        return chainParams;
    }

    public void setChainParams(ArrayList<ChainParameter> chainParams) {
        this.chainParams = chainParams;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private Map chainParameters;

    public ArrayList<String> getPortPairGroups() {
        return portPairGroups;
    }

    public void setPortPairGroups(ArrayList<String> portPairGroups) {
        this.portPairGroups = portPairGroups;
    }

    public ArrayList<String> getFlowClassifiers() {
        return flowClassifiers;
    }

    public void setFlowClassifiers(ArrayList<String> flowClassifiers) {
        this.flowClassifiers = flowClassifiers;
    }
}
