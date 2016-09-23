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
package org.openo.nfvo.monitor.umc.monitor.wrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.openo.nfvo.monitor.umc.cometdclient.DacCometdClient;
import org.openo.nfvo.monitor.umc.db.UmcDbUtil;
import org.openo.nfvo.monitor.umc.db.dao.DACInfoDao;
import org.openo.nfvo.monitor.umc.db.dao.MonitorInfoDao;
import org.openo.nfvo.monitor.umc.db.entity.DACInfo;
import org.openo.nfvo.monitor.umc.db.entity.MonitorInfo;
import org.openo.nfvo.monitor.umc.monitor.bean.MonitorResult;
import org.openo.nfvo.monitor.umc.monitor.common.HttpClientUtil;
import org.openo.nfvo.monitor.umc.monitor.common.MonitorConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @ClassName: DACService
* @Description: TODO(DAC CRDU implementation class)
* @author tanghua10186366
*
*/
public class DACServiceWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DACServiceWrapper.class);

    private static DACServiceWrapper instance = new DACServiceWrapper();

    private DACServiceWrapper() {}

    /**
    * @Title getInstance
    * @Description TODO(get DACService Singleton Instance)
    * @return DACService
    */
    public static DACServiceWrapper getInstance() {
        return instance;
    }

    public Map<String, DacCometdClient> ip2CometdClientMap = new HashMap<>();
    /**
    * @Title initDACcometdClient
    * @Description TODO(Microservice startup initialization All DAC's cometdClient from database)
    * @return void
    */
    public void initDACcometdClient() {
        DACInfoDao dao = (DACInfoDao) UmcDbUtil.getDao(MonitorConst.DAC_INFO_TABLE);
        List<DACInfo> dacList = dao.findAll();
        for (DACInfo dacInfo : dacList) {
            DacCometdClient cometdClient = new DacCometdClient(dacInfo.getIpAddress());
            cometdClient.subscribe();
            ip2CometdClientMap.put(dacInfo.getIpAddress(), cometdClient);
            LOGGER.info(dacInfo.getIpAddress() + " create DAC cometdClient");
        }
    }

    /**
    * @Title getDACInfoInstances
    * @Description TODO(get all DACInfo from database)
    * @return List<DACInfo>
    */
    public List<DACInfo> getDACInfoInstances() {
        DACInfoDao dao = (DACInfoDao) UmcDbUtil.getDao(MonitorConst.DAC_INFO_TABLE);
        return dao.findAll();
    }

    public void initLocalDac(String localDacIp)
    {
    	if (!ip2CometdClientMap.containsKey(localDacIp))
    	{
            DACInfo dacInfo = new DACInfo();
            String newId = generateOid();
            dacInfo.setOid(newId);
            dacInfo.setMoc("it.dac");
            dacInfo.setNodeLabel("Localhost");
            dacInfo.setIpAddress(localDacIp);
            DACInfoDao dao = (DACInfoDao) UmcDbUtil.getDao(MonitorConst.DAC_INFO_TABLE);
            dao.save(dacInfo);
            DacCometdClient cometdClient = new DacCometdClient(localDacIp);
            cometdClient.subscribe();
            ip2CometdClientMap.put(localDacIp, cometdClient);
            LOGGER.info(dacInfo.getIpAddress() + " create DAC cometdClient");
    	}

    }
    /**
    * @Title saveDACInfoInstance
    * @Description TODO(save single DACInfo )
    * @param dacInfo
    * @return
    * @return MonitorResult
    */
    public MonitorResult saveDACInfoInstance(DACInfo dacInfo) {
        MonitorResult monitorResult = new MonitorResult();

        try {
            //1.check dac apiRest Whether can be accessed by dac IPAddress
            String dacUrl =
                    (new StringBuilder().append("http://").append(dacInfo.getIpAddress())
                            .append(":").append(MonitorConst.DAC_PORT)
                            .append(MonitorConst.DAC_API_ROOTDOMAIN).append("/dacs")).toString();

            int responseStatusCode = HttpClientUtil.httpPost(dacUrl);
            if (responseStatusCode == MonitorConst.DAC_CREATE_SUCCESS_CODE) {
                DACInfoDao dao = (DACInfoDao) UmcDbUtil.getDao(MonitorConst.DAC_INFO_TABLE);
                DACInfo existDacInfo = dao.findByIp(dacInfo.getIpAddress());
                if(existDacInfo != null){
                    dao.delete(existDacInfo);
                }

                String newId = generateOid();
                dacInfo.setOid(newId);
                //2.save DACInfo to database
                dao.save(dacInfo);

                //3.create new Pm CometdClient connector
                DacCometdClient cometdClient = new DacCometdClient(dacInfo.getIpAddress());
                cometdClient.subscribe();
                ip2CometdClientMap.put(dacInfo.getIpAddress(), cometdClient);
                LOGGER.info(dacInfo.getIpAddress() + " create DAC cometdClient");

                monitorResult.setResult(MonitorConst.REQUEST_SUCCESS);
                monitorResult.setInfo(newId);
            } else {
                monitorResult.setResult(MonitorConst.REQUEST_FAIL);
                monitorResult.setInfo("Failure to connect " + dacInfo.getIpAddress());
            }


        } catch (Exception e) {
            monitorResult.setResult(MonitorConst.REQUEST_FAIL);
            monitorResult.setInfo(e.getMessage());
            LOGGER.error(dacInfo.getOid() + " DAC create fail:" + e.getMessage());

        }
        return monitorResult;
    }

    /**
    * @Title getDACInfoInstance
    * @Description TODO(get single DACInfo from database by oid)
    * @param oid
    * @return
    * @return DACInfo
    */
    public DACInfo getDACInfoInstance(String oid) {
        DACInfoDao dao = (DACInfoDao) UmcDbUtil.getDao(MonitorConst.DAC_INFO_TABLE);
        return dao.findByOId(oid);
    }

    /**
    * @Title updateDACInfoInstance
    * @Description TODO(update single DACInfo)
    * @param dacInfo
    * @return
    * @return MonitorResult
    */
    public MonitorResult updateDACInfoInstance(DACInfo dacInfo) {
        MonitorResult monitorResult = new MonitorResult();

        try {
            DACInfoDao dao = (DACInfoDao) UmcDbUtil.getDao(MonitorConst.DAC_INFO_TABLE);
            dao.update(dacInfo);

            monitorResult.setResult(MonitorConst.REQUEST_SUCCESS);
            monitorResult.setInfo(dacInfo.getOid() + " DAC updated ok");
        } catch (Exception e) {
            monitorResult.setResult(MonitorConst.REQUEST_FAIL);
            monitorResult.setInfo(e.getMessage());
            LOGGER.error(dacInfo.getOid() + " DAC updated fail:" + e.getMessage());
        }
        return monitorResult;
    }

    /**
    * @Title deleteDACInfoInstance
    * @Description TODO(delete single DACInfo by oid)
    * @param oid
    * @return
    * @return MonitorResult
    */
    public MonitorResult deleteDACInfoInstance(String oid) {
        MonitorResult monitorResult = new MonitorResult();

        try {
            DACInfo dacInfo = getDACInfoInstance(oid);

            //1.check dac apiRest Whether can be accessed by dac IPAddress
            String dacUrl =
                    (new StringBuilder().append("http://").append(dacInfo.getIpAddress())
                            .append(":").append(MonitorConst.DAC_PORT)
                            .append(MonitorConst.DAC_API_ROOTDOMAIN).append("/dacs")).toString();
            int responseStatusCode = HttpClientUtil.httpDelete(dacUrl);
            if (responseStatusCode == MonitorConst.DAC_DELETE_SUCCESS_CODE) {
                //2.delete DACInfo to database
                DACInfoDao dao = (DACInfoDao) UmcDbUtil.getDao(MonitorConst.DAC_INFO_TABLE);
                dao.deleteByOid(oid);

                //3.unsubscribe Pm CometdClient connector
                DacCometdClient cometdClient = ip2CometdClientMap.get(dacInfo.getIpAddress());
                if (cometdClient != null) {
                    cometdClient.unsubscribe();
                }
                LOGGER.info(dacInfo.getIpAddress() + " unsubscribe Pm CometdClient connector");

                monitorResult.setResult(MonitorConst.REQUEST_SUCCESS);
                monitorResult.setInfo(oid + " DAC delete ok");
            } else {
                monitorResult.setResult(MonitorConst.REQUEST_FAIL);
                monitorResult.setInfo("Failure to connect " + dacInfo.getIpAddress());
            }


        } catch (Exception e) {
            monitorResult.setResult(MonitorConst.REQUEST_FAIL);
            monitorResult.setInfo(e.getMessage());
            LOGGER.error(oid + " DAC delete fail:" + e.getMessage());
        }
        return monitorResult;
    }

    /** 
    * @Title generateOid 
    * @Description generate only a Oid by UUID
    * @return      
    * @return String    
    */
    private String generateOid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 
     * @date 2016/5/19 16:17:28
     * @description Get all Monitor infos from table.
     */
    public List<MonitorInfo> getMonitorInfos() {
        MonitorInfoDao dao = (MonitorInfoDao) UmcDbUtil.getDao(MonitorConst.MONITOR_INFO_TABLE);
        return dao.findAll();
    }

    /**
     * 
     * @date 2016/5/30 15:20:07
     * @description Get Monitor info by oid from table. 
     */
    public MonitorInfo getMonitorInfoByOid(String oid) {
        MonitorInfoDao dao = (MonitorInfoDao) UmcDbUtil.getDao(MonitorConst.MONITOR_INFO_TABLE);
        return dao.queryByOid(oid);
    }
    
    /**
     * 
     * @date 2016/5/19 16:18:08
     * @description Get Monitor infos by neTypeId from table. 
     */
    public List<MonitorInfo> getMonitorInfoByNeTypeId(String neTypeId) {
        MonitorInfoDao dao = (MonitorInfoDao) UmcDbUtil.getDao(MonitorConst.MONITOR_INFO_TABLE);
        return dao.queryByNeTypeId(neTypeId);
    }
}
