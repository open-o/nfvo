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
import org.openo.orchestrator.nfv.umc.db.entity.PmTaskThreshold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PmTaskThresholdDao extends UmcDao<PmTaskThreshold> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmTaskThresholdDao.class);

    public PmTaskThresholdDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<PmTaskThreshold> findAll() {
        List<PmTaskThreshold> list = null;
        try {
            beginTransaction();
            list = list(session.createQuery("from PmTaskThreshold t"));
        } catch (HibernateException ex) {
            LOGGER.error("PmTaskThresholdDao.findAll throw exception!", ex);
        } finally {
            closeTransaction();
        }
        return list;
    }

    public void deleteByOid(String oid) {
        try {
            beginTransaction();
            session.createQuery("delete from PmTaskThreshold t where t.oid = :oid")
                    .setParameter("oid", oid).executeUpdate();
        } catch (HibernateException ex) {
            LOGGER.error("PmTaskThresholdDao.deleteByOid throw exception!", ex);
        } finally {
            closeTransaction();
        }
    }
}
