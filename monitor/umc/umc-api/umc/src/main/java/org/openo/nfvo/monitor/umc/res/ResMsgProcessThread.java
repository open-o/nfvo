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
package org.openo.nfvo.monitor.umc.res;

import java.io.IOException;
import java.util.Map;

import org.cometd.bayeux.Message;
import org.openo.nfvo.monitor.umc.util.ExtensionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @date 2016/3/7 14:28:32
 * 
 */
public class ResMsgProcessThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResMsgProcessThread.class);
    private String message = null;

    public ResMsgProcessThread(String message) {
        this.message = message;

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void run() {
        if (message != null) {
			String json = message;
			LOGGER.info("Receive  ResMgr Notification data: " + json);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(Include.NON_NULL);  
			Map resData;
			try {
				resData = objectMapper.readValue(json, Map.class);
			} catch (IOException e) {
				LOGGER.warn("ObjectMapper ResMgr message data fail!" + e.getMessage(), e);
				return;
			}
			Object[] resMsgHandles = ExtensionUtil.getInstances(IResMsgHandle.EXTENSIONID, IResMsgHandle.KEY);		
			if (resMsgHandles != null && resMsgHandles.length > 0)
			{
				for (Object resMsgHandle : resMsgHandles) {			
					((IResMsgHandle)resMsgHandle).process(resData);
				}
			}
        }
    }

}
