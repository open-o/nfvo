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

package org.openo.nfvo.vimadapter.service.adapter.impl;

import org.openo.nfvo.vimadapter.common.VimUtil;
import org.openo.nfvo.vimadapter.service.adapter.inf.InterfaceResourceManager;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.entity.Vim;
import org.openo.nfvo.vimadapter.service.openstack.entry.ResourceMgrOpenstack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * It is used to adapter resource management.<br/>
 *
 * @author
 * @version NFVO 0.5 Aug 24, 2016
 */
public class AdapterResourceManager implements InterfaceResourceManager {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterResourceManager.class);

    private JSONObject resultObj = new JSONObject();

    private AdapterResourceManager() {
        resultObj.put(Constant.RETCODE, Constant.REST_FAIL);
    }

    /**
     * Create vim resource management<br/>
     *
     * @param type the type of vim
     * @return resource management of openstack
     * @since NFVO 0.5
     */
    public static synchronized ResourceMgrOpenstack createResourceMgr(String type) {
        ResourceMgrOpenstack resourceMgr = null;
        if(Constant.OPENSTACK.equalsIgnoreCase(type)) {
            resourceMgr = new ResourceMgrOpenstack();
        } else {
            LOG.error("function=createResourceMgr, msg=Invalid VIM type");
        }

        return resourceMgr;
    }

    @Override
    public JSONObject getCpuLimits(JSONObject paramJson) {
        Vim vimInfo = VimUtil.getVimById(paramJson.getString(Constant.VIMID));
        return null == vimInfo ? resultObj
                : createResourceMgr(vimInfo.getType()).getCpuLimits(vimInfo.generateConMap(paramJson));

    }

    @Override
    public JSONObject getDiskLimits(JSONObject paramJson) {
        LOG.warn("function=getDiskLimits, paramJson:{}", paramJson);
        Vim vimInfo = VimUtil.getVimById(paramJson.getString(Constant.VIMID));
        return null == vimInfo ? resultObj
                : createResourceMgr(vimInfo.getType()).getDiskLimits(vimInfo.generateConMap(paramJson));
    }

    @Override
    public JSONObject getNetworks(JSONObject paramJson) {
        Vim vimInfo = VimUtil.getVimById(paramJson.getString(Constant.VIMID));
        return null == vimInfo ? resultObj
                : createResourceMgr(vimInfo.getType()).getNetworks(vimInfo.generateConMap(paramJson));
    }

    @Override
    public JSONObject getHosts(JSONObject paramJson) {
        Vim vimInfo = VimUtil.getVimById(paramJson.getString(Constant.VIMID));
        return null == vimInfo ? resultObj
                : createResourceMgr(vimInfo.getType()).getHosts(vimInfo.generateConMap(paramJson));
    }

    @Override
    public JSONObject getPorts(JSONObject paramJson) {
        Vim vimInfo = VimUtil.getVimById(paramJson.getString(Constant.VIMID));
        return null == vimInfo ? resultObj
                : createResourceMgr(vimInfo.getType()).getPorts(vimInfo.generateConMap(paramJson));
    }

    @Override
    public JSONObject getProjects(JSONObject paramJson) {
        Vim vimInfo = VimUtil.getVimById(paramJson.getString(Constant.VIMID));
        return null == vimInfo ? resultObj
                : createResourceMgr(vimInfo.getType()).getProjects(vimInfo.generateConMap(paramJson));
    }
}
