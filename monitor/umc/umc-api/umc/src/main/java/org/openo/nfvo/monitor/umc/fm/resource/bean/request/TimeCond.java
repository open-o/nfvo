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
/**
 * time condition of query current alarm
 */

import org.jdom.Attribute;
import org.jdom.Element;

public class TimeCond /* implements JsonRequest */
{
    private long beginTime = -1l;

    private long endTime = -1l;

    private long relativeTime = -1L;

    /**
     * The time of inquiry: set the default time with this parameter
     */
    private int timeMode = 0;

    /**
     * The time of inquiry: start or end time
     */
    public static final byte MODE_TIMERANGE = 0;

    /**
     * The time of inquiry: The relative time（n days from now on）
     */
    public static final byte MODE_RELATIVETIME_IN = 1;

    public long getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(long relativeTime) {
        this.relativeTime = relativeTime;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getTimeMode() {
        return timeMode;
    }

    public void setTimeMode(int timeMode) {
        this.timeMode = timeMode;
    }

    public boolean isValid() {
        if (timeMode == MODE_TIMERANGE) {
            return isTimeRangeValid();
        } else if (timeMode == MODE_RELATIVETIME_IN) {
            return isRelativeTimeValid();
        } else {
            return false;
        }
    }

    public TimeCond fromRuleData(Element e) {
        Attribute attr = e.getAttribute("model");
        byte mode = Byte.valueOf(attr.getValue());
        if (mode == MODE_TIMERANGE) {
            setTimeMode(MODE_TIMERANGE);
            attr = e.getAttribute("start");
            setBeginTime(Long.valueOf(attr.getValue()));
            attr = e.getAttribute("end");
            setEndTime(Long.valueOf(attr.getValue()));
        } else {
            setTimeMode(MODE_RELATIVETIME_IN);
            attr = e.getAttribute("start");
            setRelativeTime(Long.valueOf(attr.getValue()));
        }
        return this;
    }

    private boolean isRelativeTimeValid() {
        return relativeTime != -1L;
    }

    private boolean isTimeRangeValid() {
        return beginTime != -1 && endTime != -1;
    }
}
