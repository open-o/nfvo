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
public class TrapDescInfo implements Serializable
{

	private static final long serialVersionUID = -4402291351391735763L;
	private byte eventType;//1 alarm 2 restore 3 notification
    private long alarmCode;
    private byte alarmSeverity; // 1 critical,2 major,3 minor,4 warning
    private long reasonCode;
    private String trapOid;
//    private String snmpVersion;
    private String eventEntity;
//    private String status; // 1 active

}
