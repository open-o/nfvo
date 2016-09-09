/**
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
package org.openo.nfvo.monitor.umc.fm.resource.bean.request;


import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * condition of query current alarm
 *
 */

public class QueryCond implements IJsonRequest {

    private int[] severities = new int[0];

    private int[] ackStates = new int[0];

    private PositionCond[] positions = new PositionCond[0];

    private JsonProbableCauseCond[] probableCauses = new JsonProbableCauseCond[0];

    private TimeCond ackTime;

    private TimeCond alarmRaisedTime;

    private long[] ids = new long[0];

    private String[] mocs = new String[0];

    public long[] getIds() {
        return ids;
    }

    public void setIds(long[] ids) {
        this.ids = ids;
    }

    public int[] getSeverities() {
        return severities;
    }

    public void setSeverities(int[] severities) {
        this.severities = severities;
    }

    public int[] getAckStates() {
        return ackStates;
    }

    public void setAckStates(int[] ackStates) {
        this.ackStates = ackStates;
    }

    public PositionCond[] getPositions() {
        return positions;
    }

    public void setPositions(PositionCond[] positions) {
        this.positions = positions;
    }

    public JsonProbableCauseCond[] getProbableCauses() {
        return probableCauses;
    }

    public void setProbableCauses(JsonProbableCauseCond[] jsonProbableCauseConds) {
        this.probableCauses = jsonProbableCauseConds;
    }

    public TimeCond getAckTime() {
        return ackTime;
    }

    public void setAckTime(TimeCond ackTime) {
        this.ackTime = ackTime;
    }

    public TimeCond getAlarmRaisedTime() {
        return alarmRaisedTime;
    }

    public void setAlarmRaisedTime(TimeCond alarmRaisedTime) {
        this.alarmRaisedTime = alarmRaisedTime;
    }

    public String[] getMocs() {
        return mocs;
    }

    public void setMocs(String[] mocs) {
        this.mocs = mocs;
    }

    @Override
    public boolean isValid() {
        return isTimeCondValid(alarmRaisedTime) && isTimeCondValid(ackTime);

    }

    public QueryCond fromRuleDataStr(String ruleData) throws JDOMException, IOException {
        ArrayList<String> mocs = new ArrayList<String>();
        ArrayList<PositionCond> positions = new ArrayList<PositionCond>();
        ArrayList<JsonProbableCauseCond> probableCauses = new ArrayList<JsonProbableCauseCond>();

        StringReader reader = new StringReader(ruleData);
        Element root = new SAXBuilder().build(reader).getRootElement();
        List children = root.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Element child = (Element) children.get(i);
            if (child.getName().equals("Moc")) {
                updatMocList(mocs, child);
            }
            if (child.getName().equals("PerceivedSeverity")) {
                setSeverities(parseAckStateOrSeverity(child));
            }
            if (child.getName().equals("AckState")) {
                setAckStates(parseAckStateOrSeverity(child));
            }
            if (child.getName().equals("AckTime")) {
                setAckTime(new TimeCond().fromRuleData(child));
            }
            if (child.getName().equals("RaisedTime")) {
                setAlarmRaisedTime(new TimeCond().fromRuleData(child));
            }
            if (child.getName().equals("NewPositionCond")) {
                positions.add(new PositionCond().fromRuleDataStr(child));
            }
            if (child.getName().equals("ProbableCause")) {
                updateProbableCauses(probableCauses, child);
            }
        }
        setMocs(mocs.toArray(new String[mocs.size()]));
        setPositions(positions.toArray(new PositionCond[positions.size()]));
        setProbableCauses(probableCauses.toArray(new JsonProbableCauseCond[probableCauses.size()]));
        return this;
    }

    private void updatMocList(ArrayList<String> mocs, Element child) {
        Attribute attr = child.getAttribute("Moc");
        if (attr != null) {
            String mocTemp = attr.getValue();
            if (!mocTemp.equals("NA")) {
                mocs.add(mocTemp);
            }
        }
    }

    private void updateProbableCauses(ArrayList<JsonProbableCauseCond> probableCauses, Element child) {
        short systemType = Short.valueOf(child.getAttribute("SYSTEMTYPE").getValue());
        JsonProbableCauseCond probableCauseCond =
                getProbableCauseCondBySystemType(systemType, probableCauses);
        if (probableCauseCond != null) {
            long code = Long.valueOf(child.getAttribute("CODE").getValue());
            probableCauseCond.addOneCode(code);
        } else {
            probableCauses.add(new JsonProbableCauseCond().fromRuleDataStr(child));
        }
    }

    private static JsonProbableCauseCond getProbableCauseCondBySystemType(short systemType,
            ArrayList<JsonProbableCauseCond> probableCauses) {
        for (JsonProbableCauseCond temp : probableCauses) {
            if (temp.getSystemType() == systemType) {
                return temp;
            }
        }
        return null;
    }

    private static int[] parseAckStateOrSeverity(Element child) {
        Attribute attr = child.getAttribute("value");
        String[] ackStateOrSeverityStr = attr.getValue().split(",");
        int[] ackStatesOrSeverities = new int[ackStateOrSeverityStr.length];
        for (int j = 0; j < ackStateOrSeverityStr.length; j++) {
            ackStatesOrSeverities[j] = Byte.valueOf(ackStateOrSeverityStr[j]);
        }
        return ackStatesOrSeverities;
    }

    private boolean isTimeCondValid(TimeCond timeCond) {
        return timeCond == null || timeCond.isValid();
    }
}
