/*
 * Copyright Ericsson AB. 2017
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

package org.openo.nfvo.vnfmdriver.common.constant;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Feb 15, 2017
 */
public class Constant {

    // URL Define
    public static final String DRIVER_REGISTER_MSB_URL = "/openoapi/microservices/v1/services";

    public static final int DRIVER_REGISTER_OK = 0;

    public static final int DRIVER_UNREGISTER_OK = 0;

    public static final int DRIVER_REGISTER_REPEAT = 1;

    public static final int DRIVER_REGISTER_NG = -1;

    public static final int DRIVER_UNREGISTER_NG = -1;

    public static final String VNFM_DRIVER_INFO = "driver-info.json";

    public static final String HTTP_PROTOCOL = "http://";

    public static final String POST = "post";

    public static final String PUT = "put";

    public static final String DELETE = "delete";

    public static final String GET = "get";

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String APPLICATION = "application/json";

    public static final String HEADER_SUBJECT_TOKEN = "X-Subject-Token";

    public static final int HTTP_OK = 200;

    public static final int HTTP_CREATED = 201;

    public static final int HTTP_ACCEPTED = 202;

    public static final int HTTP_NOCONTENT = 204;

    public static final int HTTP_BAD_REQUEST = 400;

    public static final int HTTP_UNAUTHORIZED = 401;

    public static final int HTTP_NOTFOUND = 404;

    public static final int HTTP_CONFLICT = 409;

    public static final int HTTP_INVALID_PARAMETERS = 415;

    public static final int HTTP_INNERERROR = 500;

    public static final int DRIVER_REGISTER_TIMEER = 60 * 1000;

    public static final int REST_FAIL = -1;

    public static final String RETCODE = "retCode";

    public static final String STATUS = "status";

    public static final String ADD_VNF_URL = "/%s/vnfs";

    public static final String DEL_VNF_URL = "/%s/vnfs/%s/terminate";

    public static final String QEURY_VNF_URL = "/%s/vnfs/%s";

    public static final String GET_VNF_STATUS_URL = "/%s/jobs/%s/&responseId=%s";

    public static final String NSLCM_URL_BASE = "/openoapi/nslcm/v1";

    public static final String VNF_URL_BASE = "/openoapi/ericsson-vnfm/v1";

    public static final String GRANT_VNF_URL = "/grantvnf";

    public static final String GET_VNFM_ID_URL = "/openoapi/extsys/v1/vnfms/%s";

    /**
     * Constructor<br>
     * <p>
     * </p>
     *
     * @since NFVO 0.5
     */
    private Constant() {
        // private constructor
    }
}
