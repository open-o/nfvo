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

package org.openo.nfvo.vnfmadapter.service.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openo.nfvo.vnfmadapter.common.servicetoken.VnfmRestfulUtil;
import org.openo.nfvo.vnfmadapter.service.constant.Constant;
import org.openo.nfvo.vnfmadapter.service.constant.ParamConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * Provide function of resource for VNFM.
 * <br/>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Aug 25, 2016
 */
public class VnfResourceMgr {

    private static final Logger LOG = LoggerFactory.getLogger(VnfResourceMgr.class);

    /**
     * Provide function of grant resource for VNFM.
     * <br/>
     *
     * @param vnfObj
     * @param vnfId
     * @param vnfmId
     * @return
     * @since NFVO 0.5
     */
    public JSONObject grantVnfResource(JSONObject vnfObj, String vnfId, String vnfmId) {
        LOG.warn("function=grantVnfResource, msg=enter to grant vnf resource, params: {}", vnfObj);
        JSONObject resultJson = new JSONObject();
        resultJson.put("retCode", Constant.REST_FAIL);
        try {
            String type = vnfObj.getString("type");
            String requestType = vnfObj.getString("operation_right");
            String vnfName = vnfObj.getString("vnf_name");

            if(StringUtils.isEmpty(type) || StringUtils.isEmpty(requestType) || StringUtils.isEmpty(vnfName)
                    || StringUtils.isEmpty(vnfId)) {
                LOG.error("function=grantVnfResource, msg=grant basic params error");
                resultJson.put("errorMsg", "basic params error");
                return resultJson;
            }

            JSONArray vmList = vnfObj.getJSONArray("vm_list");

            Map<String, Integer> resMap = calculateGrantRes(vmList);

            if(null == resMap) {
                LOG.error("function=grantVnfResource, msg=grant resource params error");
                resultJson.put("errorMsg", "resource params error");
                return resultJson;
            }

            JSONObject resParam = new JSONObject();
            JSONObject resInfo = new JSONObject();
            resInfo.put("id", vnfId);
            resInfo.put("name", vnfName);
            String action = getGrantAction(type, requestType);
            resInfo.put("action", action);

            if("online".equals(action)) {
                resInfo.put("version", vnfObj.getString("version"));
                resInfo.put("templateId", vnfObj.getString("template_id"));
                resInfo.put("templateName", vnfObj.getString("template_name"));
                resInfo.put("planId", vnfObj.getString("plan_id"));
                resInfo.put("planName", vnfObj.getString("plan_name"));
                resInfo.put("projectId", vnfObj.getString("project_id"));
                resInfo.put("projectName", vnfObj.getString("project_name"));
                resInfo.put("creator", vnfObj.getString("creator"));
                resInfo.put("status", vnfObj.getString("status"));
                resInfo.put("tenant", vnfObj.getString("tenant"));
                resInfo.put("parentTenant", vnfObj.getString("parent_tenant"));
                resInfo.put("type", vnfObj.getString("vnfd_id"));
                resInfo.put("location", vnfObj.getString("location"));
                resInfo.put("drLocation", vnfObj.getString("dr_location"));
                resInfo.put("soId", vnfObj.getString("nfvo_id"));
                resInfo.put("vnfmId", vnfmId);
            }

            JSONObject usedRes = new JSONObject();
            usedRes.put("vcpus", resMap.get("cpuNum").toString());
            usedRes.put("memory", resMap.get("memNum").toString());
            usedRes.put("disk", resMap.get("diskNum").toString());
            resInfo.put("used", usedRes);
            JSONObject drRes = new JSONObject();
            drRes.put("vcpus", "0");
            drRes.put("memory", "0");
            drRes.put("disk", "0");
            resInfo.put("drTotal", drRes);

            resParam.put("vapp", resInfo);

            resultJson = VnfmRestfulUtil.sendReqToApp(ParamConstants.RES_VNF, Constant.POST, resParam);
        } catch(JSONException e) {
            LOG.error("function=grantVnfResource, msg=parse params occoured JSONException e={}.", e);
            resultJson.put("errorMsg", "params parse exception");
        }

        return resultJson;
    }

    private Map<String, Integer> calculateGrantRes(JSONArray vmList) {
        Map<String, Integer> resMap = new HashMap<>(Constant.DEFAULT_COLLECTION_SIZE);
        int vmSize = vmList.size();
        int cpuNum = 0;
        int memNum = 0;
        int diskNum = 0;
        int diskSize = 0;
        int cpuTmp = 0;
        int memTmp = 0;
        int diskTmp = 0;
        int initNum = 0;

        try {
            for(int i = 0; i < vmSize; i++) {
                JSONObject resInfo = vmList.getJSONObject(i);
                JSONObject vmFlavor = resInfo.getJSONObject("vm_flavor");
                initNum = Integer.parseInt(resInfo.getString("init_number"));

                if(initNum == 0) {
                    continue;
                }

                JSONArray volumList = vmFlavor.getJSONArray("storage");
                diskSize = volumList.size();

                for(int j = 0; j < diskSize; j++) {
                    JSONObject volumeInfo = volumList.getJSONObject(j);
                    diskTmp += getDiskQuantity(volumeInfo);
                }

                cpuTmp = Integer.parseInt(vmFlavor.getString("num_cpus"));
                memTmp = Integer.parseInt(vmFlavor.getString("mem_size"));

                cpuNum += cpuTmp * initNum;
                memNum += memTmp * initNum;
                diskNum += diskTmp * initNum;

                diskTmp = 0;

            }
        } catch(JSONException e) {
            LOG.error("function=calculateGrantRes, msg=parse params occoured JSONException e={}.", e);
            return null;
        }

        resMap.put("cpuNum", cpuNum);
        resMap.put("memNum", memNum);
        resMap.put("diskNum", diskNum);
        return resMap;
    }

    private String getGrantAction(String type, String requestType) {
        String action = "unknown";

        if(("increase").equals(requestType)) {
            if(("instantiation").equals(type)) {
                action = "online";
            } else if(("scale").equals(type)) {
                action = "scaleOut";
            }

        } else if(("decrease").equals(requestType)) {
            if(("instantiation").equals(type)) {
                action = "offline";
            } else if(("scale").equals(type)) {
                action = "scaleIn";
            }
        }

        return action;
    }

    private int getDiskQuantity(JSONObject volumeObj) {
        int disk = 0;
        if(volumeObj.containsKey("vol_type")) {
            if("local_volume".equals(volumeObj.getString("vol_type"))) {
                disk = Integer.parseInt(volumeObj.getString("vol_size"));
            }
        } else if(volumeObj.containsKey("storage_type") && "local_image".equals(volumeObj.getString("storage_type"))) {

            disk = Integer.parseInt(volumeObj.getString("disk_size"));

        }
        return disk;
    }
}
