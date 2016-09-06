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

import org.hibernate.SessionFactory;
import org.openo.orchestrator.nfv.umc.db.UmcDao;
import org.openo.orchestrator.nfv.umc.db.entity.DACInfo;


/**
 * @ClassName: DACInfoDao
 * @Description: TODO(DAC data store Class)
 * @author tanghua10186366
 * @date 2015.12.24
 *
 */
public class DACInfoDao extends UmcDao<DACInfo> {

    public DACInfoDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * @Title findAll
     * @Description TODO(query all DACInfo from database)
     * @return List<DACInfo>
     */
    public List<DACInfo> findAll() {
        beginTransaction();
        List<DACInfo> list = list(session.createQuery("from DACInfo t"));
        closeTransaction();

        return list;
    }

    /**
     * @Title findByOId
     * @Description TODO(query single DACInfo from database by oid)
     * @param oid
     * @return DACInfo
     */
    public DACInfo findByOId(String oid) {
        beginTransaction();
        List<DACInfo> list = list(
                session.createQuery("from DACInfo t where t.oid = :oid").setParameter("oid", oid));
        closeTransaction();

        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * @Title deleteByOid
     * @Description TODO(delete single DACInfo by oid)
     * @param oid
     * @return void
     */
    public void deleteByOid(String oid) {
        beginTransaction();
        session.createQuery("delete from DACInfo t where t.oid = :oid").setParameter("oid", oid)
                .executeUpdate();
        closeTransaction();
    }

    /**
     * @Title findByIp
     * @Description TODO(query single DACInfo from database by ipaddress)
     * @param oid
     * @return DACInfo
     */
    public DACInfo findByIp(String ipAddress) {
        beginTransaction();
        DACInfo dacInfo = (DACInfo) session.createQuery("from DACInfo t where t.ipAddress = :ipAddress")
                .setParameter("ipAddress", ipAddress).uniqueResult();
        closeTransaction();

        return dacInfo;
    }

}
