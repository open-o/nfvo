/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc;

public class NetworkBaseInfo {
    private String oid;
    private String moc;
    private String mocName;
    private String parentOid;
    private String name;
    private String manageBy;
    private String ipAddress;
    private double positionX;
    private double positionY;
    private String vendor;
    private String version;
    private String type;
    private String[] belongToOids;
    private String[] manageByOids;
    private String[] deployOnOids;
    private int childrenNum;

    private String networkId;
    private String vimId;
    private boolean share;
    private boolean extranet;
    private String provider;
    private String instanceId;
    private String createTime;
    public String getOid() {
        return oid;
    }
    public void setOid(String oid) {
        this.oid = oid;
    }
    public String getMoc() {
        return moc;
    }
    public void setMoc(String moc) {
        this.moc = moc;
    }
    public String getMocName() {
        return mocName;
    }
    public void setMocName(String mocName) {
        this.mocName = mocName;
    }
    public String getParentOid() {
        return parentOid;
    }
    public void setParentOid(String parentOid) {
        this.parentOid = parentOid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getManageBy() {
        return manageBy;
    }
    public void setManageBy(String manageBy) {
        this.manageBy = manageBy;
    }
    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public double getPositionX() {
        return positionX;
    }
    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }
    public double getPositionY() {
        return positionY;
    }
    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }
    public String getVendor() {
        return vendor;
    }
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String[] getBelongToOids() {
        return belongToOids;
    }
    public void setBelongToOids(String[] belongToOids) {
        this.belongToOids = belongToOids;
    }
    public String[] getManageByOids() {
        return manageByOids;
    }
    public void setManageByOids(String[] manageByOids) {
        this.manageByOids = manageByOids;
    }
    public String[] getDeployOnOids() {
        return deployOnOids;
    }
    public void setDeployOnOids(String[] deployOnOids) {
        this.deployOnOids = deployOnOids;
    }
    public int getChildrenNum() {
        return childrenNum;
    }
    public void setChildrenNum(int childrenNum) {
        this.childrenNum = childrenNum;
    }
    public String getNetworkId() {
        return networkId;
    }
    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }
    public String getVimId() {
        return vimId;
    }
    public void setVimId(String vimId) {
        this.vimId = vimId;
    }
    public boolean isShare() {
        return share;
    }
    public void setShare(boolean share) {
        this.share = share;
    }
    public boolean isExtranet() {
        return extranet;
    }
    public void setExtranet(boolean extranet) {
        this.extranet = extranet;
    }
    public String getProvider() {
        return provider;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }
    public String getInstanceId() {
        return instanceId;
    }
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
