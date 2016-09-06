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
package org.openo.orchestrator.nfv.umc.db.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.openo.orchestrator.nfv.umc.db.UmcDao;
import org.openo.orchestrator.nfv.umc.db.entity.PmTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PmTaskDao extends UmcDao<PmTask> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmTaskDao.class);

    public PmTaskDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<PmTask> findAll() {
        List<PmTask> list = null;
        try {
            beginTransaction();
            list = list(session.createQuery("from PmTask t"));
        } catch (HibernateException ex) {
            LOGGER.error("PmTaskDao.findAll throw exception!", ex);
        } finally {
            closeTransaction();
        }
        return list;
    }

    public List<PmTask> findByTaskId(String taskId) {
        List<PmTask> list = null;
        try {
            beginTransaction();
            list = list(session.createQuery("from PmTask t where t.taskId = :taskId")
                    .setParameter("taskId", Integer.valueOf(taskId)));
        } catch (HibernateException ex) {
            LOGGER.error("PmTaskDao.findByTaskId throw exception!", ex);
        } finally {
            closeTransaction();
        }
        return list;
    }

    public void deleteByOid(String oid) {
        try {
            beginTransaction();
            session.createQuery("delete from PmTask t where t.oid = :oid")
                    .setParameter("oid", oid).executeUpdate();
        } catch (HibernateException ex) {
            LOGGER.error("PmTaskDao.deleteByOid throw exception!", ex);
        } finally {
            closeTransaction();
        }
    }

    public int getMaxPmJobId() {
        Integer MaxJobId = null;
        try {
            beginTransaction();
            MaxJobId = (Integer) session
                    .createSQLQuery("select max(convert(t.jobid,int)) from Pm_Task t")
                    .uniqueResult();
        } catch (HibernateException ex) {
            LOGGER.error("PmTaskDao.getMaxPmJobId throw exception!", ex);
        } finally {
            closeTransaction();
        }
        return (MaxJobId != null) ? MaxJobId.intValue() : -1;
    }

}
