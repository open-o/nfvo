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
package org.openo.nfvo.monitor.dac.dataaq.common;

import java.util.Map;

import org.openo.nfvo.monitor.dac.dataaq.monitor.bean.common.MonitorTaskInfo;

/**
 * Data Collector interface
 */
public interface IDataCollector {
    /**
     * Perform data collection
     * @param para Data acquisition parameter
     * @param commands Data acquisition commands
     * @return Data acquisition result
     * @throws MonitorException
     */
    Map collectData(ICollectorPara para, Map commands) throws MonitorException;

    void setMonitorTaskInfo(MonitorTaskInfo monitorTaskInfo);
}
