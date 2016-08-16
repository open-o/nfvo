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
package org.openo.nfvo.resmanagement.common.constant;

public interface Constant {

    String RES_MANAGEMENT_DB = "resmanagementdb";

    /**
     * Database Delete/Modify/Add fail.
     */
    int ERROR_CODE = -1;

    /**
     * Database Delete/Modify/Add success.
     */
    int OK_CODE = 1;

    /**
     * Lack of resource.
     */
    int RES_NOT_ENOUGH_CODE = -2;
    
    /**
     * Module name.
     */
    static final String MODULE_NAME = "Resmanagement";

    static final String RESPONSE_CONTENT = "responseContent";

    static final String STATUS_CODE = "statusCode";

    /**
     * Format Time
     */
    static final String DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";

    static final String DATE_UTC_FORMATE = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    static final String UTC_FORMATE = "UTC";

    static final String DATE_DAY_FORMATE = "yyyy-MM-dd 00:00:00";

    /**
     * IAM
     */

    String HTTP_CONTENT_TYPE = "Content-Type";

    String HTTP_CONTENT_TYPE_VALUE = "application/json;charset=UTF-8";

    static final String X_TENANT_ID = "X-Tenant-Id";

    String IAM_TOKEN = "x-auth-token";

    String IAM_USER_ID = "X-User-Id";

    String IAM_USER_NAME = "X-User-Name";

    String IAM_DOMAIN_NAME = "X-Domain-Name";

    String HEADER_SUBJECT_TOKEN = "X-Subject-Token";

}
