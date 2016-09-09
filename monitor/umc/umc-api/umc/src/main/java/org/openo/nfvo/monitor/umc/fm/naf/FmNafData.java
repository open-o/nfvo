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
package org.openo.nfvo.monitor.umc.fm.naf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * History performance data detail information reported by DAC.
 */
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class FmNafData {

	private String eventId;
	private int eventType;//1 alarm, 2 restore, 3 notification
	private long eventCode;
	private int eventSystemType;//20105 trap alarm,20104 status alarm
	private int eventSubType;//0 Communications, 3 Equipment
	private long eventRaiseTime;
//	private String eventName;
//	private String eventReason;
//	private String eventRepairAction;
	private int eventServerity;//1 critical, 2 major, 3 minor, 4 critical, 0 info
	private String eventDetailInfo;
    private String neIp;
    private String neType;
    private String neId;
}
