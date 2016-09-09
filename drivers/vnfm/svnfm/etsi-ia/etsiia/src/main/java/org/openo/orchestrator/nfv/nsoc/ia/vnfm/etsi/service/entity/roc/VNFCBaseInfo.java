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

public class VNFCBaseInfo {
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

    private String vduId;
    private String vnfId;
    private String createTime;
    private String customPara;
    private String status;
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
    public String getVduId() {
        return vduId;
    }
    public void setVduId(String vduId) {
        this.vduId = vduId;
    }
    public String getVnfId() {
        return vnfId;
    }
    public void setVnfId(String vnfId) {
        this.vnfId = vnfId;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getCustomPara() {
        return customPara;
    }
    public void setCustomPara(String customPara) {
        this.customPara = customPara;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}
