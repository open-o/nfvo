/*
 * Copyright 2016, Huawei Technologies Co., Ltd.
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

import java.util.List;
import java.util.Map;

import org.openo.nfvo.vimadapter.service.openstack.resourcemgr.NetworkQuery;
import org.openo.nfvo.vimadapter.service.openstack.resourcemgr.RpQuery;
import org.openo.nfvo.vimadapter.service.openstack.resourcemgr.VendorQuery;
import org.openo.nfvo.vimadapter.service.openstack.resourcemgr.VmQuery;

import net.sf.json.JSONObject;


public class ResourceMgrOpenstack {

    public List<JSONObject> getRps(Map<String, String> conMap, JSONObject param) {
        RpQuery query = new RpQuery(conMap);
        return query.getRps();
    }

    public List<JSONObject> getVendors(Map<String, String> conMap, JSONObject param) {
        VendorQuery vendorQuery = new VendorQuery(conMap);
        return vendorQuery.getVendors();
    }

    public List<JSONObject> getVms(Map<String, String> conMap, JSONObject param) {
        VmQuery vQuery = new VmQuery(conMap);
        return vQuery.getVms();
    }

    public List<JSONObject> getNetworks(Map<String, String> conMap, JSONObject param) {
        NetworkQuery query = new NetworkQuery(conMap);
        return query.getNetworks();
    }
}
