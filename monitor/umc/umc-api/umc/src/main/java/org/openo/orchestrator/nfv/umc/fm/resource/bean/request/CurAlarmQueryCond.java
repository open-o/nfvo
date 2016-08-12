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
 * condition of query current alarm
 */
import java.io.IOException;
import java.io.StringReader;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class CurAlarmQueryCond extends QueryCond {
    // 1:visible； 2：invisible
    private String[] isVisibles = new String[0];

    public String[] getIsVisibles() {
        return isVisibles;
    }

    public void setIsVisibles(String[] strings) {
        this.isVisibles = strings;
    }

    protected String getSpecialCondRuleDataStr() {
        return "<FilterState value=\"1\" />";
    }

    public CurAlarmQueryCond fromRuleDataStr(String ruleData) throws JDOMException, IOException {
        super.fromRuleDataStr(ruleData);
        StringReader reader = new StringReader(ruleData);
        Element root = new SAXBuilder().build(reader).getRootElement();
        Element visibleState = root.getChild("FilterState");
        if (visibleState != null) {
            Attribute attr = visibleState.getAttribute("value");
            setIsVisibles(new String[] {attr.getValue().toString()});
        }
        return this;
    }
}
