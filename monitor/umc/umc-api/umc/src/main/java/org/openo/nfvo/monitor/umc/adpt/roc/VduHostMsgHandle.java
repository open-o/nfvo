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
package org.openo.nfvo.monitor.umc.adpt.roc;

import java.util.List;
import java.util.Map;

import org.openo.nfvo.monitor.umc.monitor.bean.MonitorParamInfo;
import org.openo.nfvo.monitor.umc.monitor.wrapper.MonitorServiceWrapper;
import org.openo.nfvo.monitor.umc.pm.common.PmConst;
import org.openo.nfvo.monitor.umc.util.ExtensionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionImpl(entensionId = IRocMsgHandle.EXTENSIONID, keys = {IRocMsgHandle.KEY}, isSingleton = true)
public class VduHostMsgHandle implements IRocMsgHandle {
	private static final Logger LOGGER = LoggerFactory.getLogger(VduHostMsgHandle.class);
	private static final String DELETE = "delete";
	private static final String CREATE = "create";
	private static final String LOCALPROXY = "127.0.0.1";
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void process(Map<String, ?> rocData) {

		String operationType = (String)rocData.get(PmConst.operationType);
		String resourceType = (String)rocData.get(PmConst.resourceType);
		String customPara = (String)rocData.get(PmConst.customPara);
		String label = (String)rocData.get(PmConst.LABEL);
		if(label == null){
		    label = "unset";
		}
		if(customPara == null){
		    customPara = "{\"PORT\":\"\",\"USERNAME\":\"\",\"PASSWORD\":\"\",\"PROTOCOL\":\"SSH\"}";
		}
		if (resourceType.equals(PmConst.VDU_TYPE) || resourceType.equals(PmConst.HOST_TYPE))
		{
			if (operationType.equals(CREATE))
			{
				List<Map> datas = (List<Map>)rocData.get(PmConst.data);
				for (Map data : datas)
				{
					String oid = (String)data.get(PmConst.oid);
					String moc = (String)data.get(PmConst.moc);//nfv.host.linux
					LOGGER.info("Receive create message, moc: " + moc + " oid:" + oid);
					
					MonitorParamInfo paramInfo = new MonitorParamInfo();
					paramInfo.setOid(oid);
					paramInfo.setNeTypeId(moc);
					paramInfo.setIpAddress(LOCALPROXY);
					paramInfo.setLabel(label);
					paramInfo.setCustomPara(customPara);
					paramInfo.setOrigin("roc");
					
					MonitorServiceWrapper.getInstance().addMonitorInfo(paramInfo);
				}
			} 
			else if (operationType.equals(DELETE))
			{
				List<String> neIds = (List<String>)rocData.get(PmConst.deleteIds);
				LOGGER.info("Receive delete message, moc: " + resourceType + " oid:" + neIds.get(0));
				
				MonitorServiceWrapper.getInstance().deleteMonitorInfo(neIds.get(0));
			}
		}
	}
}
