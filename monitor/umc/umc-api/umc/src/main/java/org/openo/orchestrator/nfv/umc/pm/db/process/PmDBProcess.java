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

import org.openo.orchestrator.nfv.umc.db.UmcDbUtil;
import org.openo.orchestrator.nfv.umc.db.dao.MonitorInfoDao;
import org.openo.orchestrator.nfv.umc.db.dao.PmTaskDao;
import org.openo.orchestrator.nfv.umc.db.dao.PmTaskThresholdDao;
import org.openo.orchestrator.nfv.umc.db.entity.PmTask;
import org.openo.orchestrator.nfv.umc.db.entity.PmTaskThreshold;
import org.openo.orchestrator.nfv.umc.pm.common.DebugPrn;
import org.openo.orchestrator.nfv.umc.pm.common.PmConst;
import org.openo.orchestrator.nfv.umc.pm.common.PmException;

/**
 * Implementation of database CURD operation of performance module(PM)
 * @author 10179660
 *
 */
public class PmDBProcess extends PmCommonProcess {
    private static final DebugPrn dMsg = new DebugPrn(PmDBProcess.class.getName());

    public static List<PmTaskThreshold> queryThreshold() throws PmException {
        PmTaskThresholdDao dao =
                (PmTaskThresholdDao) UmcDbUtil.getDao(PmConst.PM_TASK_THRESHOLD);
        return dao.findAll();
    }

    public static List<PmTask> queryTaskInfos(String taskId) throws PmException {
        PmTaskDao dao = (PmTaskDao) UmcDbUtil.getDao(PmConst.PM_TASK);

        if (taskId == null) {
            return dao.findAll();
        }

        return dao.findByTaskId(taskId);
    }


    public static int queryMaxPmJobId() throws PmException {
        PmTaskDao dao = (PmTaskDao) UmcDbUtil.getDao(PmConst.PM_TASK);
        return dao.getMaxPmJobId();
    }

    public static void deleteDbByOid(String tableName, String oid) throws PmException {
        if (tableName == null || oid == null) {
            dMsg.warn("Param:tablename is null or Param:oid is null! ignore to delete.");
            return;
        }

        if (tableName.equals(PmConst.PM_TASK)) {
            PmTaskDao dao = (PmTaskDao) UmcDbUtil.getDao(tableName);
            dao.deleteByOid(oid);
        } else if (tableName.equals(PmConst.PM_TASK_THRESHOLD)) {
            PmTaskThresholdDao dao = (PmTaskThresholdDao) UmcDbUtil.getDao(tableName);
            dao.deleteByOid(oid);
        } else if ( tableName.equals(PmConst.MONITOR_INFO)) {
            MonitorInfoDao dao = (MonitorInfoDao) UmcDbUtil.getDao(tableName);
            dao.deleteByOid(oid);
        }
    }

    public static Object queryDbByOid(String tableName, String oid) throws PmException {
        if (tableName == null || oid == null) {
            dMsg.warn("Param:tablename is null or Param:oid is null! ignore to delete.");
            return null;
        }

        if (tableName.equals(PmConst.PM_TASK)) {
            // TODO
        } else if (tableName.equals(PmConst.PM_TASK_THRESHOLD)) {
            // TODO
        } else if ( tableName.equals(PmConst.MONITOR_INFO)) {
            MonitorInfoDao dao = (MonitorInfoDao) UmcDbUtil.getDao(tableName);
            return dao.queryByOid(oid);
        }

        return null;
    }
}
