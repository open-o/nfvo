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
