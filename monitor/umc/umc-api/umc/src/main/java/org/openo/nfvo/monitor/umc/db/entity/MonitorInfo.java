/**
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
@Table(name = "MONITOR_INFO", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"oid"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorInfo implements IEntityClass, Serializable {

    private static final long serialVersionUID = 8238353816514916929L;

    @Id
    @Column(name = "OID", nullable = false)
    private String oid;

    @Column(name = "NETYPEID", nullable = false)
    private String neTypeId;

    @Column(name = "IPADDRESS")
    private String ipAddress;

    @Column(name = "LABEL", nullable = false)
    private String label;

    @Column(name = "CUSTOMPARA", nullable = false)
    private String customPara;

    @Column(name = "EXTENDPARA")
    private String extendPara;

    @Column(name = "ORIGIN", nullable = false)
    private String origin;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonitorInfo)) {
            return false;
        }

        final MonitorInfo that = (MonitorInfo) o;

        return Objects.equals(this.oid, that.oid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oid);
    }
}
