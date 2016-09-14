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

package org.openo.nfvo.vimadapter.service.openstack.entry;

import java.util.Map;

import org.openo.nfvo.vimadapter.service.openstack.networkmgr.OpenstackNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * Openstack network management.<br/>
 * 
 * @author
 * @version NFVO 0.5 Aug 24, 2016
 */
public class NetworkMgrOpenstack {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkMgrOpenstack.class);

    /**
     * Create network to openstack.<br/>
     * 
     * @param network the JSONObject of network information
     * @param conInfoMap the openstack info map
     * @return the result of creating network to openstack
     * @since NFVO 0.5
     */
    public JSONObject create(JSONObject network, Map<String, String> conInfoMap) {
        LOG.warn("funtion=create, msg=create new one.");
        OpenstackNetwork openStackNetMgr = new OpenstackNetwork(conInfoMap);
        return openStackNetMgr.createNetwork(network);
    }

    /**
     * Delete network to openstack.<br/>
     * 
     * @param network the JSONObject of network information
     * @param conInfoMap the openstack info map
     * @param networkId String network id
     * @return the result of deleting network to openstack
     * @since NFVO 0.5
     */
    public int remove(String networkId, Map<String, String> conInfoMap) {
        LOG.warn("function=remove, msg=remove one network.");
        OpenstackNetwork openStackNetMgr = new OpenstackNetwork(conInfoMap);
        return openStackNetMgr.removeNetwork(networkId);
    }
}
