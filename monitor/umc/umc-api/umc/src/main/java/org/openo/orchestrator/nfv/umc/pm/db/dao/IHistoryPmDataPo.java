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
package org.openo.orchestrator.nfv.umc.pm.db.dao;

import java.sql.Timestamp;
import java.util.Map;

/**
 * @author 10090474
 *
 */
public interface IHistoryPmDataPo {

    public Timestamp getBeginTime();

    public Timestamp getEndTime();

    public Integer getGranularity();

    public String getNedn();

    /**
     * get counter value by counter column name.
     *
     * @param ColumnName counter column name
     * @return
     */
    public String getValueByColumnName(String ColumnName);

    /**
     * initialize property value according Column-Name-to-Value-Map
     *
     * @param columnName2ValueMap Column Name to Column Value Map
     */
    public void initValue(Map<String, Object> columnName2ValueMap);
}
