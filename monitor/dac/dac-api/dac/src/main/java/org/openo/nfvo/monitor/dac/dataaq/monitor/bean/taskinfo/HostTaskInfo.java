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
package org.openo.nfvo.monitor.dac.dataaq.monitor.bean.taskinfo;

import java.util.Properties;

import org.openo.nfvo.monitor.dac.common.DacConst;
import org.openo.nfvo.monitor.dac.dataaq.monitor.bean.common.MonitorTaskInfo;

/**
 * 
 *
 */
public class HostTaskInfo extends MonitorTaskInfo {

	private String HOSTTYPE = "NFV_OS";

	@Override
	protected void addNeMonitorProperty(Properties paras) {
        addMonitorProperty(DacConst.IPADDRESS, paras.getProperty(DacConst.IPADDRESS));
        //todo 9-19
/*        addMonitorProperty(DacConst.PORT, paras.getProperty("LOGINPORT"));
        addMonitorProperty(DacConst.PROTOCOL, paras.getProperty("LOGINPROTOCOL"));
        addMonitorProperty(DacConst.USERNAME, paras.getProperty("LOGINUSER"));
        addMonitorProperty(DacConst.PASSWORD,  paras.getProperty(DacConst.PASSWORD));*/
        
        addMonitorProperty(DacConst.PORT, paras.getProperty(DacConst.PORT));
        addMonitorProperty(DacConst.PROTOCOL, paras.getProperty(DacConst.PROTOCOL));
        addMonitorProperty(DacConst.USERNAME, paras.getProperty(DacConst.USERNAME));
        addMonitorProperty(DacConst.PASSWORD,  paras.getProperty(DacConst.PASSWORD));
        

        if (!remoteHostMap.containsKey(paras.getProperty(DacConst.IPADDRESS))) {
            setCreateHostMap(paras.getProperty(DacConst.IPADDRESS));
        }

        if (provider == null || provider.length() == 0)
        {
	        //String loginProtocol = paras.getProperty("LOGINPROTOCOL");
        	String loginProtocol = paras.getProperty(DacConst.PROTOCOL);
	        setProvider(loginProtocol);
        }

	}
	@Override
	protected String getHostType() {
		return HOSTTYPE;
	}
}
