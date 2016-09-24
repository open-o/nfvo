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

package org.openo.nfvo.resmanagement.common.constant;

/**
 * <br/>
 * <p>
 * Constant for REST URL.
 * </p>
 *
 * @author
 * @version NFVO 0.5 2016-3-17
 */
public class UrlConstant {

    /**
     * networks target.
     */
    public static final String LOCATION_TARGET = "location";

    /**
     * MSB register url.
     */
    public static final String REST_MSB_REGISTER = "/api/microservices/v1/services";

    /**
     * networks url.
     */
    public static final String LOCATION_URL = "/openoapi/resmgr/v1/locations";

    /**
     * port url.
     */
    public static final String PORT_URL = "/openoapi/resmgr/v1/ports";

    /**
     * host url.
     */
    public static final String HOST_URL = "/openoapi/resmgr/v1/hosts";

    /**
     * sites target.
     */
    public static final String SITES_TARGET = "sites";

    /**
     * sites url.
     */
    public static final String SITES_URL = "/openoapi/resmgr/v1/datacenters";

    /**
     * networks target.
     */
    public static final String NETWORKS_TARGET = "networks";

    /**
     * networks url.
     */
    public static final String NETWORKS_URL = "/openoapi/resmgr/v1/networks";

    /**
     * updateres.
     */
    public static final String MODRES_URL = "/updateres";

    /**
     * resoperate target.
     */
    public static final String RESOPERATE_TARGET = "resoperate";

    /**
     * APPLICATION_TYPE.
     */
    public static final String APPLICATION_TYPE = "application/json";

    /**
     * ADDRES.
     */
    public static final String ADDRES_URL = "/resmgr/vims";

    /**
     * updatebytenant.
     */
    public static final String UPDATE_BY_TENANT = "updatebytenant";

    /**
     * updatebyvapp.
     */
    public static final String UPDATE_BY_VAPP = "updatebyvapp";

    /**
     * getVenderURL.
     */
    public static final String GET_VENDOR_URL = "/rest/vimadapter/v1/res/tenants";

    /**
     * getVMSURL.
     */
    public static final String GET_VMS_URL = "/rest/vimadapter/v1/res/vms";

    /**
     * getSitesURL.
     */
    public static final String GET_SITES_URL = "/rest/vimadapter/v1/res/sites";

    /**
     * getNetworkURL.
     */
    public static final String GET_NETWORK_URL = "/openoapi/vimdriver/v1/networks";

    /**
     * getHostURL.
     */
    public static final String GET_HOST_URL = "/openoapi/vimdriver/v1/%s/hosts";

    /**
     * getHostDetailURL.
     */
    public static final String GET_HOSTDETAIL_URL = "/openoapi/vimdriver/v1/%s/hosts/%s";

    /**
     * getNetworkURL.
     */
    public static final String GET_PORT_URL = "/openoapi/vimdriver/v1/ports";

    /**
     * getLimitsDiskURL.
     */
    public static final String GET_LIMITSDISK_URL = "/openoapi/vimdriver/v1/limits/%s/disk";

    /**
     * getLimitsCpuURL.
     */
    public static final String GET_LIMITSCPU_URL = "/openoapi/vimdriver/v1/limits/%s/cpumemory";

    /**
     * template notify M url.
     */
    public static final String TEMPLATE_NOTIFY_M_URL = "";

    /**
     * tenantsite allot url.
     */
    public static final String TENANTSITE_ALLOT_URL = "/v1/resmanage/tenantsite/allot";

    /**
     * tenantsite allot target.
     */
    public static final String TENANTSITE_ALLOT_TARGET = "tenantsite/allot";

    /**
     * SO driver request URL.
     */
    public static final String SO_DRIVER_REQ_URL = "/rest/sodriver/v1/req";

    /**
     * All SO driver request URL.
     */
    public static final String ALL_SO_DRIVER_REQ_URL = "/rest/sodriver/v1/req/broadcast";

    /**
     * tenant url.
     */
    public static final String TENANT_URL = "/v1/resmanage/tenant";

    /**
     * tenant target.
     */
    public static final String TENANT_TARGET = "tenant";

    /**
     * rollback url.
     */
    public static final String ROLLBACK_URL = "/v1/resmanage/rollback";

    /**
     * rollback target.
     */
    public static final String ROLLBACK_TARGET = "rollback";

    /**
     * vms target.
     */
    public static final String VIM_TARGET = "vim";

    /**
     * vms url.
     */
    public static final String VIM_URL = "/v1/resmanage/vim";

    /**
     * https
     */
    public static final String GET_HTTPS = "https://";

    /**
     * get token
     */
    public static final String GET_IAM_TOKEN = "/v3/auth/tokens";

    /**
     * rest.
     */
    public static final String REST = "/rest";

    /**
     * donsdata url.
     */
    public static final String INSTALL_URL = "install";

    /**
     * donsdata url.
     */
    public static final String UNINSTALL_URL = "uninstall";

    /**
     * respool url.
     */
    public static final String RESOPERATE_URL = "/openoapi/resmgr/v1/resoperate";

    /**
     * limits url.
     */
    public static final String LIMITS_URL = "/openoapi/resmgr/v1/limits";

    private UrlConstant() {
        //private constructor
    }

}
