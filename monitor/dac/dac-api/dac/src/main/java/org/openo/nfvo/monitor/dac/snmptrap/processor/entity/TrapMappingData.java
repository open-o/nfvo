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
package org.openo.nfvo.monitor.dac.snmptrap.processor.entity;

import java.util.Hashtable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrapMappingData
{
    private String trapOid;
    private Hashtable<String, BindOidData> bindDatas;
    private Hashtable<String, AlarmMappingData> alarmMappingDatas;
    private List<String> entityOids;



    public BindOidData getByBindOid(String bindOid)
    {
        return bindDatas.get(bindOid);
    }

    public boolean containsBindOid(String bindOid)
    {
        return bindDatas.containsKey(bindOid);
    }

    public AlarmMappingData getByBindsValue(String bindsValue)
    {
        return alarmMappingDatas.get(bindsValue);
    }

    public boolean containsBindsValue(String bindsValue)
    {
        return alarmMappingDatas.containsKey(bindsValue);
    }

    public void setBindData(BindOidData bindData)
    {
        this.bindDatas.put(bindData.getBindOid(), bindData);
    }

    public void setAlarmMappingData(AlarmMappingData alarmMappingData)
    {
        this.alarmMappingDatas.put(alarmMappingData.getBindsValue(), alarmMappingData);
    }
}
