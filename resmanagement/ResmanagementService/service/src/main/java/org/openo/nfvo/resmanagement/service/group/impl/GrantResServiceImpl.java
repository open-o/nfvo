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

package org.openo.nfvo.resmanagement.service.group.impl;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.nfvo.resmanagement.common.VimUtil;
import org.openo.nfvo.resmanagement.service.base.openstack.inf.Sites;
import org.openo.nfvo.resmanagement.service.group.inf.GrantResService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version NFVO 0.5 Oct 29, 2016
 */
public class GrantResServiceImpl implements GrantResService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrantResServiceImpl.class);

    private Sites sites;

    /**
     * <br>
     * 
     * @param object
     * @return
     * @throws ServiceException
     * @since NFVO 0.5
     */
    @Override
    public JSONObject grantResource(JSONObject object) throws ServiceException {
        LOGGER.info("function=grantResource; object: {}", object.toString());
        JSONObject additionalparam = object.getJSONObject("additionalParam");
        String vimId = additionalparam.getString("vimid");
        JSONObject vimJson = VimUtil.getVimById(vimId);
        String tenant = vimJson.getString("tenant");
        JSONObject accessinfo = new JSONObject();
        accessinfo.put("tenant", tenant);
        JSONObject vim = new JSONObject();
        vim.put("vimid", vimId);
        vim.put("accessinfo", accessinfo);
        LOGGER.info("function=grantResource; vim: {}", vim.toString());
        JSONObject result = new JSONObject();
        result.put("vim", vim);
        return result;
    }

    public JSONObject grantResourceReal(JSONObject object) throws ServiceException {
        LOGGER.info("function=grantResource; object: {}", object.toString());
        String vimId = object.getString("vimid");
        JSONObject vimJson = VimUtil.getVimById(vimId);
        JSONObject vim = parseVim(vimJson);
        JSONArray addResource = parseAddResource(object);
        JSONObject resInfo = getResInfo(object);
        resInfo.put("vimId", vimId);
        sites.update(resInfo);

        JSONObject result = new JSONObject();
        result.put("vim", vim);
        result.put("zone", "");
        result.put("zoneGroup", "");
        result.put("addResource", addResource);
        result.put("tempResource", "");
        result.put("removeResource", "");
        result.put("updateResource", "");
        result.put("vimAssets", new JSONObject());
        result.put("additionalParam", "");
        LOGGER.info("function=grantResource; vim: {}", vim.toString());
        return vim;
    }

    /**
     * <br>
     * 
     * @param object
     * @return
     * @since NFVO 0.5
     */
    private JSONObject getResInfo(JSONObject object) {
        JSONArray oldResource = object.getJSONArray("addResource");
        LOGGER.info("function=getResInfo; Resource: {}", oldResource.toString());
        int cpuNum = 0;
        int memNum = 0;
        int diskNum = 0;
        for(int i = 0; i < oldResource.size(); i++) {
            JSONObject res = oldResource.getJSONObject(i);
            JSONObject vCpu = res.getJSONObject("resourceTemplate").getJSONObject("VirtualComputeDescriptor")
                    .getJSONObject("virtualCpu");
            int vCpuNum = vCpu.getInt("numVirtualCpu");
            JSONObject vMem = res.getJSONObject("resourceTemplate").getJSONObject("VirtualComputeDescriptor")
                    .getJSONObject("virtualMemory");
            int vMemNum = vMem.getInt("virtualMemSize");
            JSONObject vDisk = res.getJSONObject("resourceTemplate").getJSONObject("VirtualStorageDescriptor");
            int vDiskNum = vDisk.getInt("virtualMemSize");
            cpuNum = cpuNum + vCpuNum;
            memNum = memNum + vMemNum;
            diskNum = diskNum + vDiskNum;
        }
        JSONObject obj = new JSONObject();
        obj.put("usedCPU", cpuNum);
        obj.put("usedMemory", cpuNum);
        obj.put("usedDisk", cpuNum);
        obj.put("action", "online");
        LOGGER.info("function=getResInfo; resutl: {}", obj.toString());
        return obj;
    }

    /**
     * <br>
     * 
     * @param object
     * @return
     * @since NFVO 0.5
     */
    private JSONArray parseAddResource(JSONObject object) {
        JSONArray newResources = new JSONArray();
        JSONArray oldResource = object.getJSONArray("addResource");
        LOGGER.info("function=parseAddResource; Resource: {}", oldResource.toString());
        for(int i = 0; i < oldResource.size(); i++) {
            JSONObject res = oldResource.getJSONObject(i);
            JSONObject obj = new JSONObject();
            obj.put("reservationId", "");
            obj.put("resourceProviderId", "");
            obj.put("zoneId", "");
            obj.put("vimId", object.getString("vimid"));
            obj.put("resourceDefinitionId", res.getString("resourceDefinitionId"));
            newResources.add(obj);
        }
        LOGGER.info("function=parseAddResource; Parse Resource result: {}", newResources.toString());
        return newResources;
    }

    /**
     * <br>
     * 
     * @param vimJson
     * @return
     * @since NFVO 0.5
     */
    private JSONObject parseVim(JSONObject vimJson) {
        LOGGER.info("function=grantResource; vimJson: {}", vimJson.toString());
        JSONObject interfaceInfo = new JSONObject();
        interfaceInfo.put("vimType", vimJson.getString("type"));
        interfaceInfo.put("apiVersion", "v2");
        interfaceInfo.put("protocolType", "http");
        JSONObject accessInfo = new JSONObject();
        accessInfo.put("tenant", vimJson.getString("tenant"));
        accessInfo.put("usename", vimJson.getString("useName"));
        accessInfo.put("password", vimJson.getString("password"));
        JSONObject vim = new JSONObject();
        vim.put("vimInfoId", vimJson.getString("vimId"));
        vim.put("vimId", vimJson.getString("vimId"));
        vim.put("interfaceInfo", interfaceInfo);
        vim.put("accessInfo", accessInfo);
        vim.put("interfaceEndpoint", vimJson.getString("url"));
        return vim;
    }

    /**
     * @param sites The sites to set.
     */
    public void setSites(Sites sites) {
        this.sites = sites;
    }

}
