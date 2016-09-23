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
package org.openo.nfvo.monitor.umc.res.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.openo.nfvo.monitor.umc.pm.common.PmConst;
import org.openo.nfvo.monitor.umc.res.ResMsgQueue;
import org.openo.nfvo.monitor.umc.res.bean.NotificationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResHandler {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());
	
	private static ResHandler instance = new ResHandler();
	private static ResMsgQueue resMsgQueue ;
	private static final String DELETE = "delete";
	private static final String CREATE = "create";
	private static final String SUCCESS = "SUCCESS";
	private static final String FAIL = "FAIL";
	private static final String LOCALPROXY = "127.0.0.1";
	
	private ResHandler(){
		resMsgQueue = new ResMsgQueue();
		resMsgQueue.start();
	}
	public static ResHandler getInstance(){
		return instance;
	}
	public NotificationResult dealResInfo(String resInfo){
		LOGGER.info("Receive  RocNotification data: " + resInfo);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		NotificationResult result = new NotificationResult();
		Map resData;
		try {
			resData = objectMapper.readValue(resInfo, Map.class);
		} catch (IOException e) {
			LOGGER.warn("ObjectMapper RocNotification data fail!" + e.getMessage(), e);
			result.setResult(FAIL);
			result.setInfo("ObjectMapper RocNotification data fail");
			result.setOid(new String[]{});
			return result;
		}
		String operationType = (String)resData.get(PmConst.operationType);
		String resourceType = (String)resData.get(PmConst.resourceType);
		List<String> oids = new ArrayList<String>();
		if (resourceType.equals(PmConst.VDU_TYPE) || resourceType.equals(PmConst.HOST_TYPE))
		{   
			if (operationType.equals(CREATE))
			{   
				Object dataStr = resData.get(PmConst.data);
				JSONArray datas = JSONArray.fromObject(dataStr);
				//List<String> datas = (List<String>)resData.get(PmConst.data);
				for(int i=0;i<datas.size();i++)
				{    
					JSONObject data = datas.getJSONObject(i);
					String oid = (String)data.get(PmConst.oid);
					String moc = (String)data.get(PmConst.moc);
					LOGGER.info("Receive create message, moc: " + moc + " oid:" + oid);
					oids.add(oid);
				}
			}
			else if (operationType.equals(DELETE))
			{
				List neIds = (List)resData.get(PmConst.deleteIds);
				LOGGER.info("Receive delete message, moc: " + resourceType + " oid:" + neIds.get(0));
				oids.addAll(neIds);
			}
			resMsgQueue.put(resInfo);
		}
		String[] resOids = new String[oids.size()];
		result.setResult(SUCCESS);
		result.setInfo("resource monitor info add ok");
		result.setOid(oids.toArray(resOids));
		return result;
	}

}
