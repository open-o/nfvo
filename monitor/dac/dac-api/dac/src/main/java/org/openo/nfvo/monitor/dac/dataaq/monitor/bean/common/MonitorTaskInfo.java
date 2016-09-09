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
package org.openo.nfvo.monitor.dac.dataaq.monitor.bean.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.codehaus.jackson.map.ObjectMapper;
import org.openo.nfvo.monitor.dac.common.DacConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Monitor task details
 */
@Data
@NoArgsConstructor
public abstract class MonitorTaskInfo implements IMonitorTaskInfo{
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorTaskInfo.class);

    protected int jobId = 0;
    protected int granularity;
    protected String monitorName;
    protected String[] columnName;
    protected String provider;

    protected Map<Object, Object> monitorProperty = new HashMap<>();
    protected static ConcurrentHashMap<String, HashMap<String, String>> remoteHostMap =
            new ConcurrentHashMap<>();
    public String cachedMessage = "";   //for debug

    protected void setCreateHostMap(String ip) {
        remoteHostMap.put(ip, new HashMap<String, String>());
    }

    public static Map<String, HashMap<String, String>> getRemoteHostMap() {
        return remoteHostMap;
    }

    public void addMonitorProperty(Object key, Object value) {
        monitorProperty.put(key, value);
    }

    public Object getMonitorProperty(Object key) {
        return monitorProperty.get(key);
    }

    public Map<Object, Object> getMonitorProperty() {
        return monitorProperty;
    }

    /**
     * Extract custom parameters.The custom parameter is JSON string format
     * @param paras custom parameters
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void mergeCustomPara(Properties paras) {
        Properties newParas = new Properties();
        String customPara = (String) paras.get(DacConst.CUSTOMPARA);
        if (customPara != null && !customPara.equals("")) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map paraMap = objectMapper.readValue(customPara, Map.class);
                Set<String> keys = paraMap.keySet();
                for (String key : keys) {
                    newParas.put(key, paraMap.get(key));
                }
            } catch (Exception e) {
                LOGGER.warn(e.getMessage());
            }
        }
        paras.putAll(newParas);
    }

    public void setMonitor(String monitorString)
    {
    	// monitorname
    	String[] tmp = monitorString.split(":");
    	if (tmp.length == 1)
    	{
    		setMonitorName(tmp[0]);
    	}
    	// provider:monitorname
    	else
    	{
        	setProvider(tmp[0]);
        	setMonitorName(tmp[1]);
    	}

    }

    @Override
    public void setCommPara(Properties paras) {
    	mergeCustomPara(paras);
    	addMonitorProperty(DacConst.HOSTTYPE, getHostType());
        addMonitorProperty(DacConst.MONITORNAME, monitorName);
        addMonitorProperty(DacConst.NETYPEID, paras.getProperty(DacConst.NETYPEID));
        addMonitorProperty(DacConst.RESID, paras.getProperty(DacConst.RESID));
        addNeMonitorProperty(paras);
    }

    protected abstract void addNeMonitorProperty(Properties paras);

	protected abstract String getHostType() ;

}


