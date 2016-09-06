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
@Table(name = "NFV_HOST_LINUX_RAM")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NfvHostLinuxRam implements IHistoryPmDataPo, Serializable {

    private static final long serialVersionUID = 7230417635609082055L;

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

    @Column(name = "USEDMEMRATIO")
    private Double usedMemRatio;

    @Column(name = "TOTALSWAP")
    private Double totalSwap;

    @Column(name = "SWAPUSEDRATIO")
    private Double swapUsedRatio;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NfvHostLinuxRam)) {
            return false;
        }

        final NfvHostLinuxRam that = (NfvHostLinuxRam) o;

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
        if ("USEDMEMRATIO".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getUsedMemRatio());
        }

        if ("TOTALSWAP".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getTotalSwap());
        }

        if ("SWAPUSEDRATIO".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getSwapUsedRatio());
        }

        return "";
    }

    @Override
    public void initValue(Map<String, Object> columnName2ValueMap) {
        this.beginTime = new Timestamp((Long) columnName2ValueMap.get("BEGINTIME"));
        this.endTime = new Timestamp((Long) columnName2ValueMap.get("ENDTIME"));
        this.granularity = (Integer) columnName2ValueMap.get("GRANULARITY");
        this.nedn = (String) columnName2ValueMap.get("NEDN");
        this.usedMemRatio = (Double) columnName2ValueMap.get("USEDMEMRATIO");
        this.totalSwap = (Double) columnName2ValueMap.get("TOTALSWAP");
        this.swapUsedRatio = (Double) columnName2ValueMap.get("SWAPUSEDRATIO");
    }
}
