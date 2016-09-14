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
 * The constant of http_status_code, rest_status, etc.<br/>
 * 
 * @author
 * @version NFVO 0.5 Aug 23, 2016
 */
public class Constant {

    public static final int DEFAULT_COLLECTION_SIZE = 10;

    public static final int REST_SUCCESS = 1;

    public static final int REST_PART_SUCCESS = 0;

    public static final int REST_FAIL = -1;

    public static final int HTTP_OK_STATUS_CODE = 200;

    public static final int HTTP_CREATED_STATUS_CODE = 201;

    public static final int HTTP_ACCEPTED_STATUS_CODE = 202;

    public static final int HTTP_NOCONTENT_STATUS_CODE = 204;

    public static final int HTTP_BAD_REQUEST_STATUS_CODE = 400;

    public static final int HTTP_UNAUTHORIZED_STATUS_CODE = 401;

    public static final int HTTP_NOTFOUND_STATUS_CODE = 404;

    public static final int HTTP_CONFLICT_STATUS_CODE = 409;

    public static final int HTTP_INVALID_PARAMETERS = 415;

    public static final int HTTP_INNERERROR = 500;

    public static final int INTERNAL_EXCEPTION_STATUS_CODE = 600;

    public static final int TOKEN_USER_NULL_STATUS_CODE = 602;

    public static final int SERVICE_URL_ERROR_STATUS_CODE = 603;

    public static final int ACCESS_OBJ_NULL_STATUS_CODE = 604;

    public static final int CONNECT_NOT_FOUND_STATUS_CODE = 605;

    public static final int TYPE_PARA_ERROR_STATUS_CODE = 606;

    public static final int CONNECT_FAIL_STATUS_CODE = 607;

    public static final int CONNECT_TMOUT_STATUS_CODE = 608;

    public static final int REPEAT_REG_TIME = 60 * 1000;

    public static final String OPENSTACK = "openstack";

    public static final String POST = "POST";

    public static final String PUT = "PUT";

    public static final String DEL = "DEL";

    public static final String GET = "GET";

    public static final String DELETE = "DELETE";

    public static final String WRAP_TOKEN = "token";

    public static final String WRAP_ACCESS = "access";

    public static final String WRAP_TENANT = "tenant";

    public static final String WRAP_TENANTS = "tenants";

    public static final String WRAP_ENDPOINTS = "endpoints";

    public static final String WRAP_HOST = "host";

    public static final String WRAP_HOSTS = "hosts";

    public static final String WRAP_PORT = "port";

    public static final String WRAP_PORTS = "ports";

    public static final String WRAP_PROJECT = "project";

    public static final String WRAP_PROJECTS = "projects";

    public static final String WRAP_NETWORKS = "networks";

    public static final String WRAP_NETWORK = "network";

    public static final String WRAP_LIMITS = "limits";

    public static final String WRAP_QUOTASET = "quota_set";

    public static final String ID = "id";

    public static final String SERVICE_CATALOG = "serviceCatalog";

    public static final String REGION = "region";

    public static final String PUBLICURL = "publicURL";

    public static final String SERVICENAME = "name";

    public static final String ANONYMOUS = "Anonymous";

    public static final String ADMIN = "admin";

    public static final String CERTIFICATE = "Certificate";

    public static final String GLANCE = "glance";

    public static final String NEUTRON = "neutron";

    public static final String NOVA = "nova";

    public static final String KEYSTONE = "keystone";

    public static final String CEILOMETER = "ceilometer";

    public static final String CINDER = "cinder";

    public static final String RETCODE = "retCode";

    public static final String VIMID = "vimId";

    private Constant() {
        // Constructor
    }

}
