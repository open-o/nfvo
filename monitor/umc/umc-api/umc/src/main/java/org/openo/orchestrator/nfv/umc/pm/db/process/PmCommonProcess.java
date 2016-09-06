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
package org.openo.orchestrator.nfv.umc.pm.db.process;

import java.util.List;

import org.openo.orchestrator.nfv.umc.db.UmcDao;
import org.openo.orchestrator.nfv.umc.db.UmcDbUtil;
import org.openo.orchestrator.nfv.umc.pm.common.DebugPrn;
import org.openo.orchestrator.nfv.umc.pm.common.PmException;

public class PmCommonProcess {
    private static final DebugPrn dMsg = new DebugPrn(PmCommonProcess.class.getName());

    @SuppressWarnings("rawtypes")
    public static void save(String tablename, Object entity) throws PmException {
        if (tablename == null || entity == null) {
            dMsg.warn("Param:tablename is null or Param:entity is null! ignore to insert.");
            return;
        }

        UmcDao dao = UmcDbUtil.getDao(tablename);
        dao.save(entity);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void save(String tablename, List entity) throws PmException {
        if (tablename == null || entity == null) {
            dMsg.warn("Param:tablename is null or Param:entity is null! ignore to insert.");
            return;
        }

        UmcDao dao = UmcDbUtil.getDao(tablename);
        dao.save(entity);
    }

    @SuppressWarnings("rawtypes")
    public static void delete(String tablename, Object entity) throws PmException {
        if (tablename == null || entity == null) {
            dMsg.warn("Param:tablename is null or Param:entity is null! ignore to delete.");
            return;
        }

        UmcDao dao = UmcDbUtil.getDao(tablename);
        dao.delete(entity);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void delete(String tablename, List<Object> entity) throws PmException {
        if (tablename == null || entity == null) {
            dMsg.warn("Param:tablename is null or Param:entity is null! ignore to delete.");
            return;
        }

        UmcDao dao = UmcDbUtil.getDao(tablename);
        dao.delete(entity);
    }

    @SuppressWarnings("rawtypes")
    public static void update(String tablename, Object entity) throws PmException {
        if (tablename == null || entity == null) {
            dMsg.warn("Param:tablename is null or Param:entity is null! ignore to delete.");
            return;
        }

        UmcDao dao = UmcDbUtil.getDao(tablename);
        dao.update(entity);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void update(String tablename, List<Object> entity) throws PmException {
        if (tablename == null || entity == null) {
            dMsg.warn("Param:tablename is null or Param:entity is null! ignore to delete.");
            return;
        }

        UmcDao dao = UmcDbUtil.getDao(tablename);
        dao.update(entity);
    }
}
