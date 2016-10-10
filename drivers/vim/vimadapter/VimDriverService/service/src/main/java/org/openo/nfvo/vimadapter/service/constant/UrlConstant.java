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

package org.openo.nfvo.vimadapter.service.constant;

/**
 * The url constant information.<br/>
 *
 * @author
 * @version NFVO 0.5 Aug 23, 2016
 */
public class UrlConstant {

    public static final String REST_MSB_REGISTER = "/openoapi/microservices/v1/services";

    public static final String ESR_GET_VIM_URL = "/openoapi/extsys/v1/vims/%s";

    public static final String ESR_GET_VIMS_URL = "/openoapi/extsys/v1/vims";

    public static final String POST_AUTH_TOKENS_V3 = "%s/v3/auth/tokens?nocatalog";

    public static final String POST_AUTH_TOKENS_V2 = "%s/v2.0/tokens";

    public static final String GET_SERVICES_V3 = "%s/v3/services";

    public static final String GET_NETWORK = "/v2.0/networks";

    public static final String GET_PORT = "/v2.0/ports";

    public static final String GET_PROJECT = "/v3/projects";

    public static final String GET_HOST = "/v2/%s/os-hosts";

    public static final String GET_VNETWORK_FORM_OPENSTACK_VLAN =
            "%s/v2.0/networks?provider:segmentation_id=%s&provider:physical_network=%s";

    public static final String GET_VNETWORK_FORM_OPENSTACK_FLAT =
            "%s/v2.0/networks?provider:network_type=flat&provider:physical_network=%s";

    private UrlConstant() {
        // Constructor
    }
}
