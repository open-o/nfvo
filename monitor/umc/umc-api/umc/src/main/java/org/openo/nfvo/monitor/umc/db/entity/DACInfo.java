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

import org.openo.nfvo.monitor.umc.db.IEntityClass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DAC_INFO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DACInfo implements Serializable, IEntityClass {
    private static final long serialVersionUID = -1605415555682593973L;

    @Id
    @Column(name = "OID", nullable = false)
    private String oid;

    @Column(name = "MOC", nullable = false)
    private String moc;

    @Column(name = "NODELABEL", nullable = false)
    private String nodeLabel;

    @Column(name = "IPADDRESS", nullable = false)
    private String ipAddress;

    @Column(name = "NOTE", nullable = true)
    private String note;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DACInfo)) {
            return false;
        }

        final DACInfo that = (DACInfo) o;

        return Objects.equals(this.oid, that.oid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oid);
    }
}
