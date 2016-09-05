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
package org.openo.nfvo.vimadapter.service.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface Constant {

    int DEFAULT_COLLECTION_SIZE = 10;

    int MIN_VLANID = 0;

    int MAX_VLANID = 4094;

    int CPUMHZ = 2600;

    int OPENSTACK_NOVAURL_MIN_LENTH = 3;

    int TIME_EXCEPT_VALUE = 0;

    int REST_SUCCESS = 1;

    int REST_PART_SUCCESS = 0;

    int REST_FAIL = -1;

    int HTTP_OK_STATUS_CODE = 200;

    int HTTP_CREATED_STATUS_CODE = 201;

    int HTTP_ACCEPTED_STATUS_CODE = 202;

    int HTTP_NOCONTENT_STATUS_CODE = 204;

    int HTTP_BAD_REQUEST_STATUS_CODE = 400;

    int HTTP_UNAUTHORIZED_STATUS_CODE = 401;

    int HTTP_NOTFOUND_STATUS_CODE = 404;

    int HTTP_CONFLICT_STATUS_CODE = 409;

    int HTTP_INNERERROR = 500;

    int INTERNAL_EXCEPTION_STATUS_CODE = 600;

    int TOKEN_HEAD_NULL_STATUS_CODE = 601;

    int TOKEN_USER_NULL_STATUS_CODE = 602;

    int SERVICE_URL_ERROR_STATUS_CODE = 603;

    int ACCESS_OBJ_NULL_STATUS_CODE = 604;

    int CONNECT_NOT_FOUND_STATUS_CODE = 605;

    int TYPE_PARA_ERROR_STATUS_CODE = 606;

    int CONNECT_FAIL_STATUS_CODE = 607;

    int CONNECT_TMOUT_STATUS_CODE = 608;

    int DEFLAUT_SECURE_PORT = 443;

    int ERROR_CODE = -1;

    int MIN_URL_LENGTH = 7;

    int MAX_VIM_NAME_LENGTH = 64;

    int MIN_VIM_NAME_LENGTH = 1;

    int MAX_URL_LENGTH = 256;

    int MAX_SAMPLE_NUM = 1;

    int INTERVAL_SECOND = 20;

    String VIM_DB = "vimdb";

    String PARAM_MODULE = "VIMDriverService";

    String OPENSTACK = "openstack";

    String POST = "POST";

    String PUT = "PUT";

    String DEL = "DEL";

    String GET = "GET";

    String DELETE = "DELETE";

    String ASYNCPOST = "asyncPost";

    String ASYNCGET = "asyncGet";

    String ASYNCPUT = "asyncPut";

    String ASYNCDELETE = "asyncDelete";

    String HANDSHAKE = "handShake";

    String FIRST_HANDSHAKE = "first_handShake";

    String ENCODEING = "utf-8";

    String CONTENT_TYPE = "Content-type";

    String APPLICATION = "application/json";

    String HEADER_SUBJECT_TOKEN = "X-Subject-Token";

    String HEADER_AUTH_TOKEN = "X-Auth-Token";

    String WRAP_ERROR = "error";

    String ACTIVE = "active";

    String INACTIVE = "inactive";

    String DELETE_FAIL_NETWORK = "delete";

    String MATAIN_CONFLICT_NETWORK = "matain";


    String WRAP_ACCESS = "access";

    String ISSUED_AT = "issued_at";

    String WRAP_TENANT = "tenant";

    String WRAP_TENANTS = "tenants";

    String WRAP_METADATA = "metadata";

    String WRAP_ENDPOINTS = "endpoints";

    String WRAP_SERVERS = "servers";

    String WRAP_SERVER = "server";

    String WRAP_HOST = "host";

    String WRAP_HOSTS = "hosts";

    String WRAP_NETWORKS = "networks";

    String WRAP_NETWORK = "network";

    String WRAP_HYPERVISOR_STATS = "hypervisor_statistics";

    String WRAP_HYPERVISOR = "hypervisors";

    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    String ID = "id";

    String SERVICE_CATALOG = "serviceCatalog";

    String REGION = "region";

    String PUBLICURL = "publicURL";

    String SERVICENAME = "name";

    String ADD_VIM_EVENT = "org.openo.nfvo.vim.add";

    String UPDATE_VIM_EVENT = "org.openo.nfvo.vim.update";

    String STATUS_CHANGE_VIM_EVENT = "org.open	.nfvo.vim.status";

    List<String> VIMTYPELIST = Collections.unmodifiableList(Arrays.asList(OPENSTACK));

    List<String> AUTHLIST = Collections.unmodifiableList(Arrays.asList(AuthenticationMode.ANONYMOUS,
            AuthenticationMode.CERTIFICATE));

    List<String> TENANTS_NAME_LIST = Collections.unmodifiableList(Arrays.asList("elb", "router", "service"));

    List<String> ENABLEDHCP_LIST = Collections.unmodifiableList(Arrays.asList("False", "True"));

	String WRAP_TOKEN = null;

    public interface ServiceName {

        String GLANCE = "glance";

        String NEUTRON = "neutron";

        String NOVA = "nova";

        String KEYSTONE = "keystone";

        String CEILOMETER = "ceilometer";

        String CINDER = "cinder";
    }

    public interface AuthenticationMode {

        String ANONYMOUS = "Anonymous";

        String CERTIFICATE = "Certificate";
    }
}
