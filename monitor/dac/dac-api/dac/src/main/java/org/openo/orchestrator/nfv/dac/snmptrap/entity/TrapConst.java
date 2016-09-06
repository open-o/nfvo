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
package org.openo.orchestrator.nfv.dac.snmptrap.entity;

public class TrapConst {
	private TrapConst() {
	}

	public static final byte EVENTTYPE_ALARM = 1;

	public static final byte EVENTTYPE_RESTORE = 2;

	public static final byte EVENTTYPE_NOTICE = 3;

	public static final byte TYPE_ALARM = 0;

	public static final byte TYPE_NOTIFICATION = 1;

	public static final short TRAP_SYSTEM_TYPE = 20105;
}
