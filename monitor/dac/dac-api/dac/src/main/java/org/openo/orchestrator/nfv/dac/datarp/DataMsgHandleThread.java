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
package org.openo.orchestrator.nfv.dac.datarp;

import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.openo.orchestrator.nfv.dac.cometd.CometdService;
import org.openo.orchestrator.nfv.dac.common.bean.DataBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataMsgHandleThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataMsgHandleThread.class);
    private Vector<Object> taskParaVector = null;

    public DataMsgHandleThread(Vector<Object> taskParaVector) {
        this.taskParaVector = taskParaVector;

    }

    public void run() {
        if (taskParaVector.size() != 0) {
            int taskId = (Integer) taskParaVector.get(0);
            Date collectTime = (Date) taskParaVector.get(1);
            int granularity = (Integer) taskParaVector.get(2);
            String[] columnNames = (String[]) taskParaVector.get(3);
            Map<String, Vector<String>> result = (Map) taskParaVector.get(4);
            if (result.size() != 0)
            {
	            try {
	                DataBean data = new DataBean();
	                data.setTaskId(taskId);
	                data.setCollectTime(collectTime);
	                data.setGranularity(granularity);
	                int valueSize = result.get(columnNames[0]).size();
	                Properties[] values = new Properties[valueSize];
	                for (int i = 0; i < valueSize; i++) {
	                    values[i] = new Properties();// init values array
	                }
	                for (String columnName : columnNames) {
	                    Vector<String> v_value = result.get(columnName);
	                    int i = 0;
	                    for (String value : v_value) {
	                        values[i++].put(columnName, value);
	                    }
	                }
	                data.setValues(values);
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
            LOGGER.warn("taskParaVector has no data");
        }
    }
}
