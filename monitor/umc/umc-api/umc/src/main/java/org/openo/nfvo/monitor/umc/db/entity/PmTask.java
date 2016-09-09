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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.openo.nfvo.monitor.umc.db.IEntityClass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PM_TASK", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"taskId", "jobId"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PmTask implements Serializable, IEntityClass{

    private static final long serialVersionUID = -690705987810409232L;

    @Id
    @Column(name = "TASKID", nullable = false)
    private int taskId;

    @Id
    @Column(name = "JOBID", nullable = false)
    private int jobId;

    @Column(name = "PROXY")
    private String proxy;

    @Column(name = "NETYPEID", nullable = false)
    private String netTypeId;

    @Column(name = "OID")
    private String oid;

    @Column(name = "POID", nullable = false)
    private String pOid;

    @Column(name = "TASKSTATUS", nullable = false)
    private String taskStatus;

    @Column(name = "GRANULARITY", nullable = false)
    private String granularity;

    @Column(name = "BEGINTIME")
    private long beginTime;

    @Column(name = "ENDTIME")
    private long endTime = System.currentTimeMillis();

    @Temporal(TemporalType.TIMESTAMP)
    public long getBeginTime() {
        return beginTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public long getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PmTask)) {
            return false;
        }

        final PmTask that = (PmTask) o;

        return Objects.equals(this.taskId, that.taskId) && Objects.equals(this.jobId, that.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, jobId);
    }

}
