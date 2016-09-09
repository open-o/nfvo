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
package org.openo.nfvo.monitor.umc.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.openo.nfvo.monitor.umc.pm.task.bean.PmTaskInfo;
import org.openo.nfvo.monitor.umc.pm.task.bean.PoCounterThreshold;
import org.openo.nfvo.monitor.umc.pm.task.bean.PoTypeInfo;
import org.openo.nfvo.monitor.umc.snmptrap.common.TrapInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class NeModel {

	private String neTypeId;
	private String neTypeName;
	private PoTypeInfo[] poTypeInfo;
	private PoCounterThreshold[] poCounterThreshold;
	private PmTaskInfo[] pmTaskInfo;
	private TrapInfo trapInfo;

}
