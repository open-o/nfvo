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
package org.openo.nfvo.monitor.umc.db.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.openo.nfvo.monitor.umc.db.IEntityClass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PM_TASK_THRESHOLD", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"oid", "poid", "poattributeId"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PmTaskThreshold implements Serializable, IEntityClass {

    private static final long serialVersionUID = -1229608045170037824L;

    @Id
    @Column(name = "OID", nullable = false)
    private String oid;

    @Column(name = "NETYPEID", nullable = false)
    private String neTypeId;

    @Id
    @Column(name = "POID", nullable = false)
    private String poid;

    @Id
    @Column(name = "POATTRIBUTEID", nullable = false)
    private Integer poattributeId;

    @Column(name = "POATTRIBUTENAME", nullable = false)
    private String poattributeName;

    @Column(name = "ALARMCODE", nullable = false)
    private Integer alarmCode;

    @Column(name = "ISALARM", nullable = false)
    private Integer isAlarm;

    @Column(name = "DIRECT", nullable = false)
    private Integer direct;

    @Column(name = "CRITICAL", nullable = false)
    private Double critial;

    @Column(name = "MAJOR", nullable = false)
    private Double major;

    @Column(name = "MINOR", nullable = false)
    private Double minor;

    @Column(name = "WARNING", nullable = false)
    private Double warning;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PmTaskThreshold)) {
            return false;
        }

        final PmTaskThreshold that = (PmTaskThreshold) o;

        return Objects.equals(this.oid, that.oid) && Objects.equals(this.poid, that.poid)
                && Objects.equals(this.poattributeId, that.poattributeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oid, poid, poattributeId);
    }

}
