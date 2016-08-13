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

package org.openo.nfvo.vimadapter.common;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.openo.baseservice.roa.util.restclient.Restful;
import org.openo.baseservice.roa.util.restclient.RestfulAsyncCallback;
import org.openo.baseservice.roa.util.restclient.RestfulFactory;
import org.openo.baseservice.roa.util.restclient.RestfulParametes;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public final class EventUtil {

    private static final Logger LOG = LoggerFactory.getLogger(EventUtil.class);

    private EventUtil() {

    }

    public static void sentEvtByRest(String servicePath, String methodName, JSONObject paramJson) {
        Restful rest = RestfulFactory.getRestInstance(RestfulFactory.PROTO_HTTP);
        RestfulParametes restParametes = new RestfulParametes();

        Map<String, String> headerMap = new HashMap<String, String>(2);
        headerMap.put(Constant.CONTENT_TYPE, Constant.APPLICATION);
        restParametes.setHeaderMap(headerMap);
        restParametes.setRawData(paramJson.toString());
        try {
            Method method = rest.getClass().getMethod(methodName,
                    new Class[] {String.class, RestfulParametes.class, RestfulAsyncCallback.class});
            method.invoke(rest, servicePath, restParametes, new VimAsyncCallback());
        } catch(ReflectiveOperationException e) {
            LOG.error("function=sentEvtByRest, msg=ReflectiveOperationException occurs, e={}.", e);
        }
    }
}
