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
package org.openo.orchestrator.nfv.umc.pm.db.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openo.orchestrator.nfv.umc.pm.db.dao.IHistoryPmDataPo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NFV_HOST_LINUX_NIC")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NfvHostLinuxNic implements IHistoryPmDataPo, Serializable {

    private static final long serialVersionUID = 4698994889161462902L;

    @Id
    @Column(name = "BEGINTIME", nullable = false)
    private Timestamp beginTime;

    @Id
    @Column(name = "ENDTIME")
    private Timestamp endTime;

    @Id
    @Column(name = "GRANULARITY", nullable = false)
    private Integer granularity;

    @Id
    @Column(name = "NEDN", nullable = false)
    private String nedn;

    @Id
    @Column(name = "INTERFACENAME")
    private String interfaceName;

    @Column(name = "SENTERRORS")
    private Double sentErrors;

    @Column(name = "RECEIVEDERRORS")
    private Double receivedErrors;

    @Column(name = "PACKETSENT")
    private Double packetSent;

    @Column(name = "PACKETRECEIVED")
    private Double packetReceived;

    @Column(name = "RECEIVEDERRORRATIO")
    private Double receivedErrorRatio;

    @Column(name = "SENTERRORRATIO")
    private Double sentErrorRatio;

    @Column(name = "BYTESINRATIO")
    private Double bytesInRatio;

    @Column(name = "BYTESOUTRATIO")
    private Double bytesOutRatio;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NfvHostLinuxNic)) {
            return false;
        }

        final NfvHostLinuxNic that = (NfvHostLinuxNic) o;

        return Objects.equals(this.beginTime, that.beginTime)
                && Objects.equals(this.endTime, that.endTime)
                && Objects.equals(this.granularity, that.granularity)
                && Objects.equals(this.nedn, that.nedn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beginTime, endTime, granularity, nedn);
    }

    @Override
    public String getValueByColumnName(String ColumnName) {
        if ("INTERFACENAME".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getInterfaceName());
        }

        if ("SENTERRORS".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getSentErrors());
        }

        if ("RECEIVEDERRORS".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getReceivedErrors());
        }

        if ("PACKETSENT".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getPacketSent());
        }

        if ("PACKETRECEIVED".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getPacketReceived());
        }

        if ("RECEIVEDERRORRATIO".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getReceivedErrorRatio());
        }

        if ("SENTERRORRATIO".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getSentErrorRatio());
        }

        if ("BYTESINRATIO".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getBytesInRatio());
        }

        if ("BYTESOUTRATIO".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getBytesOutRatio());
        }

        return "";
    }

    @Override
    public void initValue(Map<String, Object> columnName2ValueMap) {
        this.beginTime = new Timestamp((Long) columnName2ValueMap.get("BEGINTIME"));
        this.endTime = new Timestamp((Long) columnName2ValueMap.get("ENDTIME"));
        this.granularity = (Integer) columnName2ValueMap.get("GRANULARITY");
        this.nedn = (String) columnName2ValueMap.get("NEDN");
        this.interfaceName = (String) columnName2ValueMap.get("INTERFACENAME");
        this.sentErrors = (Double) columnName2ValueMap.get("SENTERRORS");
        this.receivedErrors = (Double) columnName2ValueMap.get("RECEIVEDERRORS");
        this.packetSent = (Double) columnName2ValueMap.get("PACKETSENT");
        this.packetReceived = (Double) columnName2ValueMap.get("PACKETRECEIVED");
        this.receivedErrorRatio = (Double) columnName2ValueMap.get("RECEIVEDERRORRATIO");
        this.sentErrorRatio = (Double) columnName2ValueMap.get("SENTERRORRATIO");
        this.bytesInRatio = (Double) columnName2ValueMap.get("BYTESINRATIO");
        this.bytesOutRatio = (Double) columnName2ValueMap.get("BYTESOUTRATIO");
    }
}
