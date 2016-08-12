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
package org.openo.orchestrator.nfv.umc.fm.db.dao;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openo.orchestrator.nfv.umc.db.UmcDao;
import org.openo.orchestrator.nfv.umc.fm.db.entity.ProbableCause;
import org.openo.orchestrator.nfv.umc.fm.util.BasicDataTypeConvertTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Probable cause database access object
 */
public class ProbableCauseDao extends UmcDao<ProbableCause> {
    private static final Logger logger = LoggerFactory.getLogger(ProbableCauseDao.class);
    private final SessionFactory sessionFactory;

    public ProbableCauseDao(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = checkNotNull(sessionFactory);
    }

    @Override
    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<ProbableCause> findAll() {
        List<ProbableCause> list = null;
        try {
            beginTransaction();
            list = list(namedQuery("org.openo.orchestrator.nfv.umc.fm.bean.ProbableCause.findAll"));
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
        } finally {
            closeTransaction();
        }

        return list;
    }

    public List<ProbableCause> findProbableCauseBySystemType(int[] systemTypes) {
        List<ProbableCause> result = null;
        try {
            beginTransaction();
            result = null;
            if (systemTypes.length > 0) {
                result =
                        list(session.createQuery(
                                "SELECT probableCause FROM ProbableCause probableCause"
                                        + " where probableCause.systemType IN :systemTypes")
                                .setParameterList("systemTypes",
                                        Arrays.asList(BasicDataTypeConvertTool.convert2IntegerObjectArray(systemTypes))));
            } else {
                result =
                        list(session
                                .createQuery("SELECT probableCause FROM ProbableCause probableCause"));
            }
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
        } finally {
            closeTransaction();
        }
        return result;
    }

    public List<ProbableCause> queryProbableCauseByCode(long code) {
        List<ProbableCause> result = null;
        try {
            beginTransaction();
            result = list(session.createQuery(
                "SELECT probableCause FROM ProbableCause probableCause"
                        + " WHERE probableCause.code = :code").setParameter(
                "code", code));
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
        } finally {
            closeTransaction();
        }
        return result;
    }

    public List<ProbableCause> queryProbableCause(int sysType) {
        List<ProbableCause> result = null;
        try {
            beginTransaction();
            result = list(session.createQuery(
                    "SELECT probableCause FROM ProbableCause probableCause"
                            + " WHERE probableCause.systemType = :sysType").setParameter(
                    "sysType", sysType));
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
        } finally {
            closeTransaction();
        }
        return result;
    }

}
