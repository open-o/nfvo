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

package org.openo.nfvo.vnfmdriver.common.restfulutil;

import java.util.HashMap;
import java.util.Map;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.baseservice.roa.util.restclient.Restful;
import org.openo.baseservice.roa.util.restclient.RestfulFactory;
import org.openo.baseservice.roa.util.restclient.RestfulOptions;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vnfmdriver.common.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Feb 15, 2017
 */
public final class HttpRestfulAPIUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HttpRestfulAPIUtil.class.getName());

    private HttpRestfulAPIUtil() {

    }

    /**
     * <br>
     *
     * @param path
     * @param uri
     * @param methodType
     * @param params
     * @return
     * @since NFVO 0.5
     */
    public static RestfulResponse getRemoteResponse(String path, String uri, String methodType, String params) {


        RestfulResponse rsp = null;
        Restful rest = RestfulFactory.getRestInstance(RestfulFactory.PROTO_HTTP);
        try {
            RestfulOptions opt = new RestfulOptions();
            String[] strs = path.split("(http(s)?://)|:");

            opt.setHost(strs[1]);
            opt.setPort(Integer.parseInt(strs[2]));

            RestfulParametes restfulParametes = new RestfulParametes();
            Map<String, String> headerMap = new HashMap<String, String>(3);
            headerMap.put(Constant.CONTENT_TYPE, Constant.APPLICATION);
            restfulParametes.setHeaderMap(headerMap);
            if(null != params) {
                restfulParametes.setRawData(params);
            }

            if(null != rest) {
                if(Constant.GET.equalsIgnoreCase(methodType)) {
                    rsp = rest.get(uri, restfulParametes, opt);
                } else if(Constant.POST.equalsIgnoreCase(methodType)) {
                    rsp = rest.post(uri, restfulParametes, opt);
                } else {
                    LOG.error("Method type not support.");
                    return null;
                }
            }
        } catch(ServiceException e) {
            LOG.error("ServiceException!" + e.getMessage());
            return null;
        }

        return rsp;
    }

    /**
     * <br>
     *
     * @param url
     * @param methodType
     * @param params
     * @return
     * @since NFVO 0.5
     */
    public static RestfulResponse getRemoteResponse(String url, String methodType, String params) {


        RestfulResponse rsp = null;
        Restful rest = RestfulFactory.getRestInstance(RestfulFactory.PROTO_HTTP);
        try {
            RestfulParametes restfulParametes = new RestfulParametes();
            Map<String, String> headerMap = new HashMap<String, String>(3);
            headerMap.put(Constant.CONTENT_TYPE, Constant.APPLICATION);
            restfulParametes.setHeaderMap(headerMap);
            if(null != params) {
                restfulParametes.setRawData(params);
            }

            if(null != rest) {
                if(Constant.GET.equalsIgnoreCase(methodType)) {
                    rsp = rest.get(url, restfulParametes);
                } else if(Constant.POST.equalsIgnoreCase(methodType)) {
                    rsp = rest.post(url, restfulParametes);
                } else {
                    LOG.error("Method type not support.");
                    return null;
                }
            }
        } catch(ServiceException e) {
            LOG.error("ServiceException!, {}", e.getMessage());
            return null;
        }

        return rsp;
    }

    /**
     * <br>
     *
     * @param paramsMap
     * @param params
     * @return
     * @since NFVO 0.5
     */
    public static RestfulResponse getRemoteResponse(Map<String, String> paramsMap, String params) {
        String url = paramsMap.get("url");
        String methodType = paramsMap.get("methodType");

        RestfulResponse rsp = getRemoteResponse(url, methodType, params);

        return rsp;
    }
}
