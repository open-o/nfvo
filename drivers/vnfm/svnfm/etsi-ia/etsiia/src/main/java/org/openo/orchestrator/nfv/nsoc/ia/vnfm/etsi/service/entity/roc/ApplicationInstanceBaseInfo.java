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
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc;

import java.util.ArrayList;

public class ApplicationInstanceBaseInfo {
    private ArrayList<VNFBaseInfo>  vnf;
    private ArrayList<VDUBaseInfo> vdu;
    private ArrayList<VNFCBaseInfo> vnfc;
    private ArrayList<NSBaseInfo>  ns;
    private ArrayList<NetworkBaseInfo> network;
    private ArrayList<SubnetBaseInfo> subnet;
    private ArrayList<CPBaseInfo> connectPoint;
    public ArrayList<VNFBaseInfo> getVnf() {
        return vnf;
    }
    public void setVnf(ArrayList<VNFBaseInfo> vnf) {
        this.vnf = vnf;
    }
    public ArrayList<VDUBaseInfo> getVdu() {
        return vdu;
    }
    public void setVdu(ArrayList<VDUBaseInfo> vdu) {
        this.vdu = vdu;
    }
    public ArrayList<VNFCBaseInfo> getVnfc() {
        return vnfc;
    }
    public void setVnfc(ArrayList<VNFCBaseInfo> vnfc) {
        this.vnfc = vnfc;
    }
    public ArrayList<NSBaseInfo> getNs() {
        return ns;
    }
    public void setNs(ArrayList<NSBaseInfo> ns) {
        this.ns = ns;
    }
    public ArrayList<NetworkBaseInfo> getNetwork() {
        return network;
    }
    public void setNetwork(ArrayList<NetworkBaseInfo> network) {
        this.network = network;
    }
    public ArrayList<SubnetBaseInfo> getSubnet() {
        return subnet;
    }
    public void setSubnet(ArrayList<SubnetBaseInfo> subnet) {
        this.subnet = subnet;
    }
    public ArrayList<CPBaseInfo> getConnectPoint() {
        return connectPoint;
    }
    public void setConnectPoint(ArrayList<CPBaseInfo> connectPoint) {
        this.connectPoint = connectPoint;
    }


}
