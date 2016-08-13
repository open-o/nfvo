/*
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.nfvo.vimadapter.common;

import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public final class AdapterUtil {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterUtil.class);

    private AdapterUtil() {

    }

    public static boolean getResponseFromResmgr(RestfulResponse dbResponse) {
        if(dbResponse != null && dbResponse.getStatus() == Constant.HTTP_OK_STATUS_CODE) {
            JSONObject rpJson = JSONObject.fromObject(dbResponse.getResponseContent());
            return rpJson.getInt("retCode") == Constant.REST_SUCCESS;
        }
        return false;
    }

    public static boolean checkAddNetworkData(JSONObject network) {
        String name = network.getString("name");
        String id = network.getString("id");
        String type = network.getString("type");
        String physicalnet = network.getString("physicalNet");
        String rpid = network.getString("rpId");
        String segmentation = network.getString("segmentation");
        String projectId = network.getString("projectId");

        if(!checkBasicInfo(id, name, type, physicalnet, rpid, projectId)) {
            LOG.error("function=checkBasicInfo.msg=The basic info is invalid.");
            return false;
        }
        if(!checkVlanId(type, segmentation)) {
            LOG.error("function=checkVlanId.msg=The segmentation is invalid.");
            return false;
        }

        return true;
    }

    public static boolean checkBasicInfo(String id, String name, String type, String physicalNet, String rpId,
            String projectId) {
        if(StringUtils.isEmpty(id) || StringUtils.isEmpty(name) || StringUtils.isEmpty(type)
                || StringUtils.isEmpty(physicalNet) || StringUtils.isEmpty(rpId) || StringUtils.isEmpty(projectId)) {
            String output = id + ' ' + name + ' ' + type + ' ' + physicalNet + ' ' + rpId;
            LOG.error(
                    "function=checkBasicInfo.msg=ID,missing required info.id,name ,type ,physicalNe,rpId is {}",output);
            return false;
        }
        return true;
    }

	public static boolean checkVlanId(String type, String segmentation) {
		boolean flag = true;
		if (("vlan").equals(type)) {
			if (StringUtils.isEmpty(segmentation)) {
				return false;
			}
			if (!NumberUtil.isNumeric(segmentation)) {
				return false;
			}
			int vlanid = Integer.parseInt(segmentation);
			if ((vlanid < Constant.MIN_VLANID) || (vlanid > Constant.MAX_VLANID)) {
				return false;
			}
		}
		return flag;
	}

}
