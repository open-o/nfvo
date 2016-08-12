/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
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
import org.openo.orchestrator.nfv.umc.db.entity.MonitorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangjiangping
 * @date 2016/4/13 10:55:13
 * @description
 */
public class MonitorInfoDao extends UmcDao<MonitorInfo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorInfoDao.class);

    public MonitorInfoDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<MonitorInfo> findAll() {
        List<MonitorInfo> list = null;
        try {
            beginTransaction();
            list = list(session.createQuery("from MonitorInfo t"));
        } catch (HibernateException ex) {
            LOGGER.error("MonitorInfo.findAll throw exception!", ex);
        } finally {
            closeTransaction();
        }
        return list;
    }

    public void deleteByOid(String oid) {
        try {
            beginTransaction();
            session.createQuery("delete from MonitorInfo t where t.oid = :oid")
                    .setParameter("oid", oid).executeUpdate();
        } catch (HibernateException ex) {
            LOGGER.error("MonitorInfoDao.deleteByOid throw exception!", ex);
        } finally {
            closeTransaction();
        }
    }

    public MonitorInfo queryByOid(String oid) {
        MonitorInfo obj = null;

        try {
            beginTransaction();
            obj = (MonitorInfo) session.createQuery("from MonitorInfo t where t.oid = :oid")
                    .setParameter("oid", oid).uniqueResult();
        } catch (HibernateException ex) {
            LOGGER.error("MonitorInfo.findAll throw exception!", ex);
        } finally {
            closeTransaction();
        }
        return obj;
    }
}
