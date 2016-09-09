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

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrapBindInfo implements Serializable, Cloneable, Comparable
{
    private String trapOid;
    private String bindOid;
    private String bindDesc;
    private String bindFormula;
    private String bindValueDesc;

    public TrapBindInfo(String bindOid, String bindDesc)
    {
        this.bindOid = bindOid;
        this.bindDesc = bindDesc;
    }

    public boolean equals(TrapBindInfo commpare)
    {
        if (commpare.getBindOid() != null && commpare.getBindFormula() != null)
        {
            if (this.bindFormula.equals(commpare.getBindFormula()) && this.bindOid.equals(commpare.getBindOid()))
                return true;
        }
        if (commpare.getBindOid() != null
            && (commpare.getBindFormula() == null || commpare.getBindFormula().length() == 0))
        {
            if (this.bindOid.equals(commpare.getBindOid()))
            {
                return true;
            }
        }
        return false;
    }

    public int compareTo(Object o)
    {
        if (o == null)
            return 0;
        if (o instanceof TrapBindInfo)
        {
            TrapBindInfo trapBindInfo = (TrapBindInfo) o;
            String b1 = trapBindInfo.getBindOid();
            String b2 = getBindOid();
            return b2.compareTo(b1);
        }
        else
        {
            return 0;
        }
    }
}
