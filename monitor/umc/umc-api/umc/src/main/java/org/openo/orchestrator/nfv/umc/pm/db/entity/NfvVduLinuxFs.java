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
@Table(name = "NFV_VDU_LINUX_FS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NfvVduLinuxFs implements IHistoryPmDataPo, Serializable {

    private static final long serialVersionUID = -2766943223023345611L;

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
    @Column(name = "LOGICVOLUMNPATH")
    private String logicVolumnPath;

    @Id
    @Column(name = "FILESYSTEMNAME")
    private String fileSystemName;

    @Column(name = "LOGICVOLUMNSIZE")
    private Double logicVolumnSize;

    @Column(name = "LOGICVOLUMNAVAILABLE")
    private Double logicVolumnAvailable;

    @Column(name = "FILESYSTEMCAPACITY")
    private Double fileSystemCapacity;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NfvVduLinuxFs)) {
            return false;
        }

        final NfvVduLinuxFs that = (NfvVduLinuxFs) o;

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
        if ("LOGICVOLUMNPATH".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getLogicVolumnPath());
        }

        if ("FILESYSTEMNAME".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getFileSystemName());
        }

        if ("LOGICVOLUMNSIZE".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getLogicVolumnSize());
        }

        if ("LOGICVOLUMNAVAILABLE".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getLogicVolumnAvailable());
        }

        if ("FILESYSTEMCAPACITY".equalsIgnoreCase(ColumnName)) {
            return String.valueOf(this.getFileSystemCapacity());
        }

        return "";
    }

    @Override
    public void initValue(Map<String, Object> columnName2ValueMap) {
        this.beginTime = new Timestamp((Long) columnName2ValueMap.get("BEGINTIME"));
        this.endTime = new Timestamp((Long) columnName2ValueMap.get("ENDTIME"));
        this.granularity = (Integer) columnName2ValueMap.get("GRANULARITY");
        this.nedn = (String) columnName2ValueMap.get("NEDN");
        this.logicVolumnPath = (String) columnName2ValueMap.get("LOGICVOLUMNPATH");
        this.fileSystemName = (String) columnName2ValueMap.get("FILESYSTEMNAME");
        this.logicVolumnSize = (Double) columnName2ValueMap.get("LOGICVOLUMNSIZE");
        this.logicVolumnAvailable = (Double) columnName2ValueMap.get("LOGICVOLUMNAVAILABLE");
        this.fileSystemCapacity = (Double) columnName2ValueMap.get("FILESYSTEMCAPACITY");
    }
}
