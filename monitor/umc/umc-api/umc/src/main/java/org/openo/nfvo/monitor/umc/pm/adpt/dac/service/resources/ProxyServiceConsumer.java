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
package org.openo.nfvo.monitor.umc.pm.adpt.dac.service.resources;

import lombok.Data;
import net.sf.json.JSONObject;

import org.openo.nfvo.monitor.umc.pm.adpt.dac.DacConfiguration;
import org.openo.nfvo.monitor.umc.pm.adpt.dac.bean.TaskCreateAndModifyInfo;
import org.openo.nfvo.monitor.umc.pm.common.DebugPrn;
import org.openo.nfvo.monitor.umc.pm.common.RestRequestException;
import org.openo.nfvo.monitor.umc.util.APIHttpClient;

/**
 * 
 */
@Data
public class ProxyServiceConsumer {
    private static final DebugPrn logger = new DebugPrn(ProxyServiceConsumer.class.getName());
    private String proxyIp;

    public ProxyServiceConsumer(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    /**
     * @param proxyTaskInfo
     * @throws RestRequestException
     */
    public void addProxyTask(TaskCreateAndModifyInfo proxyTaskInfo) throws RestRequestException {
        logger.info("addProxyTask.");
        String url =  "http://" + this.proxyIp + ":" + DacConfiguration.getInstance().getDacServerPort()+"/api/dac/v1/tasks";
        try {
        	JSONObject taskObj = JSONObject.fromObject(proxyTaskInfo);
        	APIHttpClient.doPost(url, taskObj, "");
            logger.info("addProxyTask end.");
            return;
        } catch (Exception e) {
            throw new RestRequestException("proxy rest request error.", e);
        }
    }

    /**
     * @param proxyTaskInfo
     * @throws RestRequestException
     */
    public void modifyProxyTask(TaskCreateAndModifyInfo proxyTaskInfo) throws RestRequestException {
        logger.info("modifyProxyTask.");
        String url =  "http://" + this.proxyIp + ":" + DacConfiguration.getInstance().getDacServerPort()+"/"+proxyTaskInfo.getTaskId() + "";
        
        try {
        	JSONObject taskObj = JSONObject.fromObject(proxyTaskInfo);
        	APIHttpClient.doPut(url, taskObj, "");
            logger.info("modifyProxyTask end.");
            return;
        } catch (Exception e) {
            throw new RestRequestException("proxy rest request error.", e);
        }
    }

    /**
     * @param taskId
     * @throws RestRequestException
     */
    public void deleteProxyTask(String taskId) throws RestRequestException {
        logger.info("deleteProxyTask.");
        String url =  "http://" + this.proxyIp + ":" + DacConfiguration.getInstance().getDacServerPort()+"/"+taskId;
        try {
        	APIHttpClient.doDelete(url, "");
            logger.info("deleteProxyTask end.");
            return;
        } catch (Exception e) {
            throw new RestRequestException("proxy rest request error.", e);
        }
    }


}
