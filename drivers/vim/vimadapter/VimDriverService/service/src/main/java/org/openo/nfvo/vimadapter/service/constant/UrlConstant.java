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

package org.openo.nfvo.vimadapter.service.constant;

public class UrlConstant {

    private UrlConstant() {
        // Constructor
    }

    public static final String GET_NETWORK_8ID_NEUTRON = "%s/v2.0/networks/%s";

    public static final String POST_AUTH_TOKENS_V3 = "%s/v3/auth/tokens?nocatalog";

    public static final String POST_AUTH_TOKENS_V2 = "%s/v2.0/tokens";

    public static final String GET_SERVICES_V3 = "%s/v3/services";

    public static final String GET_RP = "%s/v2/%s/os-hypervisors/statistics";

    public static final String GET_RPSTATUS = "%s/v2/%s/os-hypervisors";

    public static final String GET_VM = "/v2/%s/servers/detail";

    public static final String GET_NETWORK = "/v2.0/networks";

    public static final String GET_HOSTS = "%s/cps/v1/hosts";

    public static final String GET_VENDOR = "%s/v2.0/tenants";

    public static final String POST_VNETWORK = "%s/v2.0/networks";

    public static final String POST_VSUBNETWORK = "%s/v2.0/subnets";

    public static final String DEL_VNETWORK = "%s/v2.0/networks/%s";

    public static final String DEL_VSUBNETWORK = "%s/v2.0/subnets/%s";


    public static final String GET_VNETWORK_FORM_OPENSTACK_VLAN =
            "%s/v2.0/networks?provider:segmentation_id=%s&provider:physical_network=%s";


    public static final String GET_VNETWORK_FORM_OPENSTACK_FLAT =
            "%s/v2.0/networks?provider:network_type=flat&provider:physical_network=%s";


}
