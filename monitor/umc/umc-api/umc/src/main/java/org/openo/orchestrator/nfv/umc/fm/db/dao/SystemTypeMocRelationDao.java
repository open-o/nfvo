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
package org.openo.orchestrator.nfv.umc.fm.db.dao;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openo.orchestrator.nfv.umc.db.UmcDao;
import org.openo.orchestrator.nfv.umc.fm.db.entity.SystemTypeMocRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * System type moc relation database access object
 */
public class SystemTypeMocRelationDao extends UmcDao<SystemTypeMocRelation> {
    private static final Logger logger = LoggerFactory.getLogger(SystemTypeMocRelationDao.class);
    private final SessionFactory sessionFactory;

    public SystemTypeMocRelationDao(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = checkNotNull(sessionFactory);
    }

    @Override
    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<SystemTypeMocRelation> findAll() {
        List<SystemTypeMocRelation> list = null;
        try {
            beginTransaction();
            list =
                    list(namedQuery("org.openo.orchestrator.nfv.umc.fm.bean.SystemTypeMocRelation.findAll"));
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
        } finally {
            closeTransaction();
        }
        return list;
    }

    public List<SystemTypeMocRelation> findMocBySystemType(int systemType) {
        List<SystemTypeMocRelation> list = null;
        try {
            beginTransaction();
            list =
                    list(namedQuery(
                            "org.openo.orchestrator.nfv.umc.fm.bean.SystemTypeMocRelation.findMocBySystemType")
                            .setParameter("systemType", systemType));
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
        } finally {
            closeTransaction();
        }

        return list;
    }

    public List<SystemTypeMocRelation> querySystemTypeByMoc(String[] mocIds) {
        List<SystemTypeMocRelation> result = null;
        try {
            beginTransaction();
            result = null;
            if (mocIds.length > 0) {
                result =
                        list(session.createQuery(
                                "SELECT relation FROM SystemTypeMocRelation relation"
                                        + " WHERE relation.mocId IN :mocIds").setParameterList(
                                "mocIds", Arrays.asList(mocIds)));
            } else {
                result =
                        list(session
                                .createQuery("SELECT relation FROM SystemTypeMocRelation relation"));
            }
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
        } finally {
            closeTransaction();
        }
        return result;
    }

}
