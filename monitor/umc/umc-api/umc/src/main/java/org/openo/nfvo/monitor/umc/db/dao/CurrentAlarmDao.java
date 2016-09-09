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
package org.openo.nfvo.monitor.umc.db.dao;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openo.nfvo.monitor.umc.db.UmcDao;
import org.openo.nfvo.monitor.umc.db.entity.CurrentAlarm;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.CurAlarmQueryCond;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.UpdateAckStateParam;
import org.openo.nfvo.monitor.umc.fm.util.BasicDataTypeConvertTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Current alarm database access object
 */
public class CurrentAlarmDao extends UmcDao<CurrentAlarm> {

    private static final String ACK_BEGIN_TIME = "ackBeginTime";
    private static final Logger logger = LoggerFactory.getLogger(CurrentAlarmDao.class);
    private final SessionFactory sessionFactory;

    public CurrentAlarmDao(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = checkNotNull(sessionFactory);
    }

    @Override
    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<CurrentAlarm> findAll() {
        List<CurrentAlarm> result = null;
        try {
            beginTransaction();
            result = list(session.createQuery("SELECT p FROM CurrentAlarm p"));
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
        }finally{
            closeTransaction();
        }

        return result;
    }

    public CurrentAlarm findByAlarmKey(String alarmKey) {
        CurrentAlarm result = null;
        try {
            beginTransaction();
            result = (CurrentAlarm) session.createQuery("FROM CurrentAlarm t where t.alarmKey =:alarmKey")
                .setParameter("alarmKey", alarmKey).uniqueResult();
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
        }finally{
            closeTransaction();
        }

        return result;
    }

    public List<CurrentAlarm> findByCondition(CurAlarmQueryCond condition) {
        StringBuilder builder = new StringBuilder();
        HashMap<Object, Object> map = new HashMap<>();
        if (condition != null) {
            ArrayList<String> positions = new ArrayList<String>();
            if (condition.getSeverities().length > 0) {
                builder.append(" curAlarm.perceivedSeverity IN :serverities ");
                map.put("serverities",
                        Arrays.asList(BasicDataTypeConvertTool.convert2IntegerObjectArray(condition.getSeverities())));
            }
            if (condition.getAckStates().length > 0) {
                if (condition.getSeverities().length > 0) {
                    builder.append("AND");
                }
                builder.append(" curAlarm.ackState IN :ackStates ");
                map.put("ackStates",
                        Arrays.asList(BasicDataTypeConvertTool.convert2IntegerObjectArray(condition.getAckStates())));
            }
            if (condition.getIsVisibles().length > 0) {
                if (condition.getAckStates().length > 0 || condition.getSeverities().length > 0) {
                    builder.append("AND");
                }
                builder.append(" curAlarm.visible IN :isVisibles ");
                map.put("isVisibles",
                        Arrays.asList(BasicDataTypeConvertTool.convert2IntegerObjectArray(condition.getIsVisibles())));
            }
            if (condition.getIds().length > 0) {
                if (condition.getAckStates().length > 0 || condition.getSeverities().length > 0
                        || condition.getIsVisibles().length > 0) {
                    builder.append("AND");
                }
                builder.append(" curAlarm.id IN :ids ");
                map.put("ids", Arrays.asList(BasicDataTypeConvertTool.convert2LongObjectArray(condition.getIds())));
            }
            if (condition.getMocs().length > 0) {
                if (condition.getAckStates().length > 0 || condition.getSeverities().length > 0
                        || condition.getIsVisibles().length > 0 || condition.getIds().length > 0) {
                    builder.append("AND");
                }
                builder.append(" curAlarm.moc IN :mocs ");
                map.put("mocs", Arrays.asList(condition.getMocs()));
            }
            if (condition.getPositions().length > 0) {
                if (condition.getAckStates().length > 0 || condition.getSeverities().length > 0
                        || condition.getIsVisibles().length > 0 || condition.getIds().length > 0
                        || condition.getMocs().length > 0) {
                    builder.append("AND");
                }
                for (int i = 0; i < condition.getPositions().length; i++) {
                    positions.add(condition.getPositions()[i].getOid());
                }
                builder.append(" curAlarm.position1 IN :positions ");
                map.put("positions", positions);
            }
            if (condition.getAlarmRaisedTime() != null) {
                Date alarmRaisedBeginTime;
                Date alarmRaisedEndTime;
                if (condition.getAlarmRaisedTime().getTimeMode() == 0) {
                    alarmRaisedBeginTime = new Date(condition.getAlarmRaisedTime().getBeginTime());
                    alarmRaisedEndTime = new Date(condition.getAlarmRaisedTime().getEndTime());
                } else {
                    alarmRaisedEndTime = new Date();
                    long beginTime =
                            alarmRaisedEndTime.getTime()
                                    - condition.getAlarmRaisedTime().getRelativeTime();
                    alarmRaisedBeginTime = new Date(beginTime);

                }
                if (condition.getAckStates().length > 0 || condition.getSeverities().length > 0
                        || condition.getIsVisibles().length > 0 || condition.getIds().length > 0
                        || condition.getMocs().length > 0 || condition.getPositions().length > 0) {
                    builder.append("AND");
                }
                builder.append(" curAlarm.alarmRaisedTime BETWEEN :alarmRaisedBeginTime AND :alarmRaisedEndTime ");
                map.put("alarmRaisedBeginTime", alarmRaisedBeginTime);
                map.put("alarmRaisedEndTime", alarmRaisedEndTime);
            }
            if (condition.getAckTime() != null) {
                Date ackBeginTime;
                Date ackEndTime;
                if (condition.getAckTime().getTimeMode() == 0) {
                    ackBeginTime = new Date(condition.getAckTime().getBeginTime());
                    ackEndTime = new Date(condition.getAckTime().getEndTime());
                } else {
                    ackEndTime = new Date();
                    long beginTime =
                            ackEndTime.getTime() - condition.getAckTime().getRelativeTime();
                    ackBeginTime = new Date(beginTime);

                }
                if (condition.getAckStates().length > 0 || condition.getSeverities().length > 0
                        || condition.getIsVisibles().length > 0 || condition.getIds().length > 0
                        || condition.getMocs().length > 0 || condition.getPositions().length > 0
                        || condition.getAlarmRaisedTime() != null) {
                    builder.append("AND");
                }
                builder.append(" curAlarm.ackTime BETWEEN :" + ACK_BEGIN_TIME + " AND :ackEndTime ");
                map.put(ACK_BEGIN_TIME, ackBeginTime);
                map.put("ackEndTime", ackEndTime);
            }

            if (condition.getProbableCauses().length != 0) {
                int[] systemType = new int[condition.getProbableCauses().length];
                if (condition.getAckStates().length > 0 || condition.getSeverities().length > 0
                        || condition.getIsVisibles().length > 0 || condition.getIds().length > 0
                        || condition.getMocs().length > 0 || condition.getPositions().length > 0
                        || condition.getAlarmRaisedTime() != null || condition.getAckTime() != null) {
                    builder.append(" AND ");
                }
                for (int i = 0; i < condition.getProbableCauses().length; i++) {
                    String systemTypevariableName = "systemType" + String.valueOf(i);
                    String codesVariableName = "codes" + String.valueOf(i);
                    systemType[i] = condition.getProbableCauses()[i].getSystemType();
                    builder.append(" curAlarm.systemType = :" + systemTypevariableName);
                    map.put(systemTypevariableName, Integer.valueOf(systemType[i]));
                    if (condition.getProbableCauses()[i].getCodes().length > 0) {
                        builder.append(" AND curAlarm.code IN :" + codesVariableName);
                        map.put(codesVariableName,
                            BasicDataTypeConvertTool.convert2LongObjectArray(condition.getProbableCauses()[i].getCodes()));
                    }
                    if (i != condition.getProbableCauses().length - 1) {
                        builder.append(" OR ");
                    }
                }
            }
        }
        String sql = "SELECT curAlarm FROM CurrentAlarm curAlarm ";
        String order = "order by curAlarm.alarmRaisedTime asc";
        if (builder.toString().length() > 0) {
            sql = sql + "WHERE " + builder.toString() + order;
        }

        List<CurrentAlarm> list = null;
        try {
            beginTransaction();
            list = list(session.createQuery(sql).setProperties(map));
        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
        }finally{
            closeTransaction();
        }

        return list;
    }

    public void save(CurrentAlarm currentAlarm) {
        try {
            beginTransaction();
            session.save(currentAlarm);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            closeTransaction();
        }
    }

    public void update(UpdateAckStateParam updateParam, String ackUser) {
        int newAckState = updateParam.getAckState();
        Date ackTime = new Date();
        try {
            beginTransaction();
            session.createQuery(
                    "update CurrentAlarm curAlarm "
                            + "set curAlarm.ackState=:newAckState, curAlarm.ackTime=:ackTime, curAlarm.ackUserId=:ackUserId where id IN :ackIds ")
                    .setParameter("newAckState", newAckState)
                    .setParameter("ackTime", ackTime)
                    .setParameter("ackUserId", ackUser)
                    .setParameterList("ackIds",
                            Arrays.asList(BasicDataTypeConvertTool.convert2LongObjectArray(updateParam.getIds())))
                    .executeUpdate();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            closeTransaction();
        }
    }

    public void updateCurAlarm(CurrentAlarm curAlarm) {
        String[] excludeProperties = null;
        try {
            beginTransaction();
            StringBuffer hql = new StringBuffer();
            HashMap<Object, Object> map = new HashMap<>();
            String objName = curAlarm.getClass().getSimpleName();
            hql.append("update ");
            hql.append(objName);
            hql.append(" set ");
            Field[] fields = curAlarm.getClass().getDeclaredFields();
            if (curAlarm.getClass().getGenericSuperclass() != null) {
                Field[] parentFields = curAlarm.getClass().getSuperclass().getDeclaredFields();
                fields = concat(fields, parentFields);
            }
            for (Field field : fields) {
                String name = field.getName();
                Method method = null;
                Object value = null;
                if (!contain(excludeProperties, name)) {
                    String upperName = name.substring(0, 1).toUpperCase() + name.substring(1);
                    try {
                        method = curAlarm.getClass().getMethod("get" + upperName);
                        value = method.invoke(curAlarm);
                        if (value != null) {
                            if (value instanceof String) {
                                hql.append(name);
                                hql.append("=");
                                hql.append("'");
                                hql.append(value);
                                hql.append("'");
                                hql.append(",");
                            } else if(value instanceof Date){
                                hql.append(name);
                                hql.append("=:");
                                hql.append(name);
                                hql.append(",");
                                map.put(name, value);
                            } else {
                                hql.append(name);
                                hql.append("=");
                                hql.append(value);
                                hql.append(",");
                            }
                        }
                    } catch (Exception e) {
                        logger.error("error while creating update hql", e);
                    }
                }
            }

            String sql = hql.toString();
            sql = sql.substring(0, sql.lastIndexOf(","));
            if (curAlarm.getAlarmKey() != null) sql = sql + " where alarmKey = '" + curAlarm.getAlarmKey() + "'";;
            logger.info("update hql is : " + sql);
            session.createQuery(sql).setProperties(map).executeUpdate();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            closeTransaction();
        }
    }

    public void deleteByAlarmKey(String alarmKey) {
        try {
            beginTransaction();
            session.createQuery("delete from CurrentAlarm curAlarm where curAlarm.alarmKey = :alarmKey")
                    .setParameter("alarmKey", alarmKey).executeUpdate();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            closeTransaction();
        }
    }

    public void deleteById(long curAlarmId) {
        try {
            beginTransaction();
            session.createQuery("DELETE FROM CurrentAlarm curAlarm WHERE curAlarm.id = :curAlarmId")
                    .setParameter("curAlarmId", curAlarmId).executeUpdate();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            closeTransaction();
        }
    }

    public static boolean contain(String[] src, String target) {
        if (src == null || src.length == 0 || target == null)
            return false;
        else {
            for (String str : src) {
                if (str.equals(target)) return true;
            }
        }
        return false;
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static String getAlarmKeyFilter(CurrentAlarm data) {
        return "alarmKey = '" + data.getAlarmKey() + "'";
    }

    public int getAlarmsCountByOid(String oid) {
        BigInteger count = null;
        try {
            beginTransaction();
            count = (BigInteger) session
                    .createSQLQuery("SELECT count(id) FROM CurrentAlarm curAlarm WHERE curAlarm.position1 = :oid")
                    .setParameter("oid", oid)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            closeTransaction();
        }

        return (count != null) ? count.intValue() : 0;
    }

}
