/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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

package org.openo.nfvo.jujuvnfmadapter.service.adapter.impl;

import java.util.Map;

import org.openo.nfvo.jujuvnfmadapter.common.servicetoken.JujuVnfmRestfulUtil;
import org.openo.nfvo.jujuvnfmadapter.service.adapter.inf.IResourceManager;
import org.openo.nfvo.jujuvnfmadapter.service.constant.Constant;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * 
 * Adapter resource manager class.<br>
 * <p>
 * </p>
 * 
 * @author
 * @version     NFVO 0.5  Sep 12, 2016
 */
public class AdapterResourceManager implements IResourceManager {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterResourceManager.class);


	@Override
	public JSONObject getJujuVnfmInfo(Map<String, String> paramsMap) {
        JSONObject resultObj = new JSONObject();
        //verify url,reserve
        
		RestfulResponse rsp = JujuVnfmRestfulUtil.getRemoteResponse(paramsMap,"");
        if(null == rsp) {
            LOG.error("function=getJujuVnfmInfo,  RestfulResponse is null");
            resultObj.put("reason", "RestfulResponse is null.");
            resultObj.put("retCode", Constant.ERROR_STATUS_CODE);
            return resultObj;
        }
        String resultCreate = rsp.getResponseContent();

        if(rsp.getStatus() == Constant.HTTP_OK) {
            LOG.warn("function=getJujuVnfmInfo, msg= status={}, result={}.", rsp.getStatus(), resultCreate);
            resultObj = JSONObject.fromObject(resultCreate);
            resultObj.put("retCode", Constant.HTTP_OK);
            return resultObj;
        } else {
            LOG.error("function=getJujuVnfmInfo, msg=ESR return fail,status={}, result={}.", rsp.getStatus(),
                    resultCreate);
            resultObj.put("reason", "ESR return fail.");
        }
        resultObj.put("retCode", Constant.ERROR_STATUS_CODE);
		return resultObj;
	}

	@Override
	public JSONObject getVnfdInfo(Map<String, String> paramsMap) {
        JSONObject resultObj = new JSONObject();
        //verify url,reserve
        
		RestfulResponse rsp = JujuVnfmRestfulUtil.getRemoteResponse(paramsMap,"");
        if(null == rsp) {
            LOG.error("function=getVnfdInfo,  RestfulResponse is null");
            resultObj.put("reason", "RestfulResponse is null.");
            resultObj.put("retCode", Constant.ERROR_STATUS_CODE);
            return resultObj;
        }
        String resultCreate = rsp.getResponseContent();

        if(rsp.getStatus() == Constant.HTTP_OK) {
            LOG.warn("function=getVnfdInfo, msg= status={}, result={}.", rsp.getStatus(), resultCreate);
            resultObj = JSONObject.fromObject(resultCreate);
            resultObj.put("retCode", Constant.HTTP_OK);
            return resultObj;
        } else {
            LOG.error("function=getVnfdInfo, msg=catalog return fail,status={}, result={}.", rsp.getStatus(),
                    resultCreate);
            resultObj.put("reason", "catalog return fail.");
        }
        resultObj.put("retCode", Constant.ERROR_STATUS_CODE);
		return resultObj;
	}
}
