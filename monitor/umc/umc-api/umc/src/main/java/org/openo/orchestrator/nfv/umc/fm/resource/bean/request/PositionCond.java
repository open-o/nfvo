/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.orchestrator.nfv.umc.fm.resource.bean.request;

/**
 * position condition in request of query current alarm
 */

import javax.xml.bind.annotation.XmlRootElement;

import org.jdom.Element;

@XmlRootElement
public class PositionCond {

    private String oid;

    private String locationId;

    private boolean group;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public PositionCond fromRuleDataStr(Element e) {
        if (e == null) {
            return null;
        }
        PositionCond cond = new PositionCond();
        cond.setOid(e.getAttribute("POSITION1").getValue());
        return cond;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }
}
