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

import org.openo.nfvo.vimadapter.service.openstack.resourcemgr.CpuLimitQuery;
import org.openo.nfvo.vimadapter.service.openstack.resourcemgr.DiskLimitQuery;
import org.openo.nfvo.vimadapter.service.openstack.resourcemgr.HostQuery;
import org.openo.nfvo.vimadapter.service.openstack.resourcemgr.NetworkQuery;
import org.openo.nfvo.vimadapter.service.openstack.resourcemgr.PortQuery;
import org.openo.nfvo.vimadapter.service.openstack.resourcemgr.ProjectQuery;

import net.sf.json.JSONObject;

/**
 * Openstack resource management.<br/>
 * 
 * @author
 * @version NFVO 0.5 Aug 24, 2016
 */
public class ResourceMgrOpenstack {

    /**
     * Get limits information from openstack.<br/>
     * 
     * @param conMap the openstack info map
     * @param param the limits param
     * @return the limits information of openstack
     * @since NFVO 0.5
     */
    public JSONObject getCpuLimits(Map<String, String> conMap) {
        CpuLimitQuery query = new CpuLimitQuery(conMap);
        return query.getLimits();
    }

    /**
     * Get limits information from openstack.<br/>
     * 
     * @param conMap the openstack info map
     * @param param the limits param
     * @return the limits information of openstack
     * @since NFVO 0.5
     */
    public JSONObject getDiskLimits(Map<String, String> conMap) {
        DiskLimitQuery query = new DiskLimitQuery(conMap);
        return query.getLimits();
    }

    /**
     * Get networks information from openstack.<br/>
     * 
     * @param conMap the openstack info map
     * @param param the networks param
     * @return the networks information of openstack
     * @since NFVO 0.5
     */
    public JSONObject getNetworks(Map<String, String> conMap) {
        NetworkQuery query = new NetworkQuery(conMap);
        return query.getNetworks();
    }

    /**
     * Get list of hosts.<br/>
     *
     * @param conMap
     * @param param
     * @return
     * @since NFVO 0.5
     */
    public JSONObject getHosts(Map<String, String> conMap) {
        HostQuery hostQuery = new HostQuery(conMap);
        return hostQuery.getHosts();
    }

    /**
     * Get List of Ports.<br/>
     *
     * @param conMap
     * @param param
     * @return
     * @since NFVO 0.5
     */
    public JSONObject getPorts(Map<String, String> conMap) {
        PortQuery portQuery = new PortQuery(conMap);
        return portQuery.getPorts();
    }

    /**
     * Retrieve projects.
     * @param conMap
     * @return
     */
    public JSONObject getProjects(Map<String, String> conMap) {
        ProjectQuery projectQuery = new ProjectQuery(conMap);
        return projectQuery.getProjects();
    }
}
