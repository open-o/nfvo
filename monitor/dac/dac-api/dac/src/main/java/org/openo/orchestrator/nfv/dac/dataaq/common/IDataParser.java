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
package org.openo.orchestrator.nfv.dac.dataaq.common;

import java.util.Vector;

import org.openo.orchestrator.nfv.dac.dataaq.monitor.bean.common.DaPerfCounterInfo;

/**
 * Data parser interface
 */
public interface IDataParser {
    /**
     * Parse the collected data
     * @param dataCollected Data acquisition result
     * @param perfCounterInfo Data acquisition index definition information
     * @return Parsed data
     * @throws MonitorException
     */
    Object parse(Vector dataCollected, DaPerfCounterInfo perfCounterInfo) throws MonitorException;
}
