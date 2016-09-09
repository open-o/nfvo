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
package org.openo.nfvo.monitor.umc.db;

import java.util.List;

import org.hibernate.SessionFactory;
import org.openo.nfvo.monitor.umc.pm.common.DebugPrn;
import org.openo.nfvo.monitor.umc.pm.db.dao.IHistoryPmDataPo;
import org.openo.nfvo.monitor.umc.pm.db.process.IPmDbQuery;
import org.openo.nfvo.monitor.umc.util.ExtensionAccess;

/**
 * 
 *
 */
public class UmcDbUtil {
    private static final DebugPrn dMsg = new DebugPrn(UmcDbUtil.class.getName());

    private static SessionFactory sessionFactory = null;

    /**
     * 
     * @date 2016/4/7 10:04:19
     * @description Commmon method, Get dao by tablename.
     * System table dao is defined at conf/extend/systemtable-extendsimpl.xml,
     * Extend Device dao is defined at conf/extend/[device*]-extendsimpl.xml.
     *
     */
    @SuppressWarnings({"unused", "rawtypes"})
    public static UmcDao getDao(String tableName){
        Class<?> daoClass = null;
        String SysExtensionID = IUmcDao.class.getName();
        String extExtensionID = IPmDbQuery.class.getName();

        if(ExtensionAccess.hasExtensionKey(SysExtensionID, tableName)){
            return buildDao(SysExtensionID, tableName);
        }else if(ExtensionAccess.hasExtensionKey(extExtensionID, tableName)){
            return buildDao(extExtensionID, tableName);
        }

        return null;
    }

    @SuppressWarnings("rawtypes")
    private static UmcDao buildDao(String extensionID, String tableName) {
        Class<?> daoClass = ExtensionAccess.getExtensionClass(extensionID, tableName);
        UmcDao dao = null;

        try {
            dao = (UmcDao) daoClass.getConstructor(SessionFactory.class)
                    .newInstance(sessionFactory);

        } catch (Exception ex) {
            ex.printStackTrace();
            dMsg.error("Get Dao Error, Please check tableName, tableName:" + tableName);
        }

        return dao;
    }

    /**
     * System Po Class is defined at conf/extend/system-extendsimpl.xml
     */
    public static List<String> getSystemPo() {
        return ExtensionAccess.getExtensionClassName(IEntityClass.class.getName());
    }

    /**
     * Extend Device Po Class is defined at conf/extend/[device*]-extendsimpl.xml
     */
    public static List<String> getExtendPo() {
        return ExtensionAccess.getExtensionClassName(IHistoryPmDataPo.class.getName());
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void setSessionFactory(SessionFactory sessionFactory) {
        UmcDbUtil.sessionFactory = sessionFactory;
    }
}
