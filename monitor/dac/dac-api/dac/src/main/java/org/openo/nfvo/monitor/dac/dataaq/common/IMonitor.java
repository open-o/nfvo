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
import java.util.Vector;

/**
 * Interface for performing data acquisition tasks
 */
public interface IMonitor {
    /**
     * Perform data collection and parse
     * @param paras Data acquisition parameter
     * @param dataCollector Data collector
     * @param dataParser Data parser
     * @return Data acquisition result
     * @throws DataAcquireException
     */
    @SuppressWarnings("rawtypes")
	Map<String, Vector<String>> perform(Map paras, IDataCollector dataCollector, IDataParser dataParser) throws DataAcquireException;
}
