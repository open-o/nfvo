/*
 * Copyright (c) 2017 Ericsson (China) Communication Co. Ltd.
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

package org.openo.nfvo.vnfmdriver.common;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vnfmdriver.common.restfulutil.HttpRestfulAPIUtil;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Feb 28, 2017
 */
public class HttpRestfulApiUtilTest {

    /**
     * <br>
     *
     * @since NFVO 0.5
     */
    @Test
    public void testGetRemoteResponse() {
        String path = "http://localhost:8080";
        String methodType = "get";
        String url = "/";
        String params = null;
        RestfulResponse rest = HttpRestfulAPIUtil.getRemoteResponse(path, url, methodType, params);
        assertNull(rest);
    }

    /**
     * <br>
     *
     * @since NFVO 0.5
     */
    @Test
    public void testGetRemoteResponse2() {
        String path = "http://localhost:8080";
        String methodType = "get";
        String url = null;
        String params = null;
        RestfulResponse rest = HttpRestfulAPIUtil.getRemoteResponse(path, url, methodType, params);
        assertNull(rest);
    }

}
