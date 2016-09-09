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
/**
 * @version 1.0
 * 
 */
package org.openo.nfvo.monitor.umc.waf.bean;

public class MockAlarmConst {
    private MockAlarmConst() {
    }
    // Definition of the type of alarm event reporting
    public static final byte EVENTTYPE_ALARM = 1;
    // Definition of the type of alarm recovery event reporting
    public static final byte EVENTTYPE_RESTORE = 2;
    // Definition of notification type
    public static final byte EVENTTYPE_NOTICE = 3;

    //System type
    public static final short SYSTEM_TYPE = 6;
    //Alarm code array
    public static final long[] alarmCodes = {1000,1001,1006,1009,1010,1011,1012,1013,1014,1015,1019,1021,1024,1028,1029,1030,1031,1032,1034,1037,1038,1039,1040,1041,1042,1050,1053,1054};

    //Alarm level
    public static final byte SEVERITY_INDETERMINATE = 0;
	public static final byte SEVERITY_CRITICAL = 1;
	public static final byte SEVERITY_MAJOR = 2;
	public static final byte SEVERITY_MINOR = 3;
	public static final byte SEVERITY_WARNING = 4;
	public static final byte SEVERITY_CLEARED = 5;

}
