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
package org.openo.nfvo.monitor.dac.dataaq.datacollector.para;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.openo.nfvo.monitor.dac.dataaq.common.ICollectorPara;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnmpCollectorPara implements ICollectorPara {
	protected String hostNameOrIp = null;
	protected int port = 161;
	protected int version = 0;// 0:version 1, 1:version 2,2:version 3
	protected String readCommunity = null;
	protected String writeCommunity = null;
	protected String userName = null;
	protected String securityName = null;
	protected int securityLevel = 1; // 1: NOAUTH_NOPRIV, 2: AUTH_NOPRIV,3: AUTH_PRIV 
	protected int authProtocol = 1; // 1: MD5,2: SHA
	protected String authPassword = null;
	protected String privPassword = null;
}
