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
import org.openo.nfvo.vimadapter.service.adapter.inf.InterfaceNetworkManager;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.entity.Vim;
import org.openo.nfvo.vimadapter.service.openstack.entry.NetworkMgrOpenstack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * It is used to adapter network management.<br/>
 *
 * @author
 * @version NFVO 0.5 Aug 24, 2016
 */
public class AdapterNetworkManager implements InterfaceNetworkManager {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterNetworkManager.class);

    private NetworkMgrOpenstack createNetworkMgr(String type) {
        NetworkMgrOpenstack networkMgr = null;
        if(Constant.OPENSTACK.equalsIgnoreCase(type)) {
            networkMgr = new NetworkMgrOpenstack();
        } else {
            LOG.error("funtion=createNetworkMgr, msg=invalid VIM type!");
        }
        return networkMgr;
    }

    @Override
    public JSONObject addNetwork(JSONObject network, String vimId) {
        LOG.warn("function=addNetwork, msg=NetworkManager enter to add a network");
        JSONObject resultObj = new JSONObject();
        Vim vim = VimUtil.getVimById(vimId);
        if(null == vim) {
            LOG.error("function=addNetwork,msg=vim not exists.");
            resultObj.put(Constant.RETCODE, Constant.REST_FAIL);
            return resultObj;
        }

        return createNetworkMgr(vim.getType()).create(network, vim.generateConMap(null));
    }

    @Override
    public int deleteNetwork(String networkDn, String vimId) {
        LOG.warn("function=deletenetwork, msg=NetworkManager enter to delete a network, networkId={}.", networkDn);
        Vim vim = VimUtil.getVimById(vimId);
        if(null == vim) {
            LOG.error("function=deleteNetwork,msg=vim not exists.");
            return Constant.HTTP_BAD_REQUEST_STATUS_CODE;
        }

        return createNetworkMgr(vim.getType()).remove(networkDn, vim.generateConMap(null));
    }

}
