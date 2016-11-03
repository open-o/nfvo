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
package org.openo.nfvo.monitor.umc.pm.db.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openo.nfvo.monitor.umc.db.UmcDao;
import org.openo.nfvo.monitor.umc.pm.db.entity.NfvVduLinuxNic;
import org.openo.nfvo.monitor.umc.pm.db.process.IPmDbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NfvVduLinuxNicDao extends UmcDao<NfvVduLinuxNic> implements IPmDbQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(NfvVduLinuxNicDao.class);

    public NfvVduLinuxNicDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<NfvVduLinuxNic> findAll() {
        List<NfvVduLinuxNic> list = null;
        try {
            beginTransaction();
            list = list(session.createQuery("from NfvVduLinuxNic t"));
        } catch (HibernateException ex) {
            LOGGER.error("NfvVduLinuxNicDao.findAll throw exception!", ex);
        } finally {
            closeTransaction();
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NfvVduLinuxNic> queryDataFromDB(Date beginTime, Date endTime, int granularity,
            List<String> neDns) {
        List<NfvVduLinuxNic> list = null;
        try {
            String hsql =
                    "FROM NfvVduLinuxNic t WHERE t.beginTime >= :beginTime AND t.beginTime < :endTime "
                            + "AND t.granularity = :granularity AND t.nedn IN( :nednList )";
            if(neDns.isEmpty()){
            	hsql =
                        "FROM NfvVduLinuxNic t WHERE t.beginTime >= :beginTime AND t.beginTime < :endTime "
                                + "AND t.granularity = :granularity";
            }

            beginTransaction();
            Query query = session.createQuery(hsql);
            query.setParameter("beginTime", new Timestamp(beginTime.getTime()));
            query.setParameter("endTime", new Timestamp(endTime.getTime()));
            query.setParameter("granularity", granularity);
            if(!neDns.isEmpty()){
                query.setParameterList("nednList", neDns);            	
            }

            list = query.list();

        } catch (HibernateException ex) {
            LOGGER.error("NfvVduLinuxNicDao.queryDataFromDB throw exception!", ex);
        } finally {
            closeTransaction();
        }

        return list;
    }

}
