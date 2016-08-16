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

public interface HttpConstant {

    int ERROR_CODE = -1;

    int OK_CODE = 1;

    int HTTP_OK = 200;

    int HTTP_CREATED = 201;

    int HTTP_ACCEPTED = 202;

    int HTTP_NOCONTENT = 204;

    int HTTP_UNAUTHORIZED = 401;

    int HTTP_BAD_REQUEST = 400;

    int HTTP_NOTFOUND_CODE = 404;

    int HTTP_CONFLICT_CODE = 409;

    int HTTP_INNERERROR_CODE = 500;

    int INTERNAL_EXCEPTION_CODE = 600;

    int TOKEN_HEAD_NULL_CODE = 601;

    int TOKEN_USER_NULL_CODE = 602;

    int SERVICE_URL_ERROR_CODE = 603;

    int ACCESS_OBJ_NULL_CODE = 604;

    int CONNECT_NOT_FOUND_CODE = 605;

    int VCENTER_PARA_ERROR_CODE = 606;

    int TYPE_PARA_ERROR_CODE = 607;

    int CONNECT_FAIL_CODE = 608;

    int DIS_CONNECT_FAIL_CODE = 609;

    int HANDSHAKE_FAIL_CODE = 610;

}
