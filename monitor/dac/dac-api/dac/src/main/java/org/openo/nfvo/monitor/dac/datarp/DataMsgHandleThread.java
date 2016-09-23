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
package org.openo.nfvo.monitor.dac.datarp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.openo.nfvo.monitor.dac.cometd.CometdService;
import org.openo.nfvo.monitor.dac.common.bean.DataBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataMsgHandleThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataMsgHandleThread.class);
    private List<Object> taskParaList = null;

    public DataMsgHandleThread(List<Object> taskParaList) {
        this.taskParaList = taskParaList;

    }

    @Override
    public void run() {
        if (taskParaList.size() != 0) {
            int taskId = (Integer) taskParaList.get(0);
            Date collectTime = (Date) taskParaList.get(1);
            int granularity = (Integer) taskParaList.get(2);
            Map<String, List<String>> result = (Map) taskParaList.get(3);
            if (result.size() != 0)
            {
	            try {
	                DataBean data = new DataBean();
	                data.setTaskId(taskId);
	                data.setCollectTime(collectTime);
	                data.setGranularity(granularity);
	                List<Properties> values = new ArrayList<Properties>();
	                Iterator<Entry<String, List<String>>> it = result.entrySet().iterator();
	                while (it.hasNext())
	                {
	                	Entry<String, List<String>> entry = it.next();
	                	List<String> v_value = entry.getValue();
	                	adjustPropertiesList(values, v_value.size());
	                	String columnName = entry.getKey();
	                    int i = 0;//
	                    for (String value : v_value) {
	                    	values.get(i++).put(columnName, value);
	                    }
	                }
	                data.setValues(values.toArray(new Properties[0]));
	                CometdService.getInstance().publish(CometdService.DATA_UPLOAD_CHANNEL, data);
	            } catch (Throwable e) {
	                LOGGER.warn(e.getMessage(), e);
	            }
            }
            else
            {
            	LOGGER.warn("TaskId:" + taskId + " has no data in resultMap!");
            }
            
        } else {
            LOGGER.warn("taskParaList has no data");
        }
    }
    private void adjustPropertiesList(List<Properties> values, int size)
    {
    	if (values.size() < size)
    	{
    		values.add(new Properties());
    		adjustPropertiesList(values, size);
    	}
    }
}
