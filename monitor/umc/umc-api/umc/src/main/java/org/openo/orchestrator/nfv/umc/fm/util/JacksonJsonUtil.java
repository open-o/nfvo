/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.orchestrator.nfv.umc.fm.util;


import javax.ws.rs.NotAcceptableException;

import org.codehaus.jackson.map.ObjectMapper;
import org.openo.orchestrator.nfv.umc.util.DebugPrn;


public class JacksonJsonUtil {
    private static final DebugPrn dMsg = new DebugPrn(JacksonJsonUtil.class.getName());

    private static ObjectMapper mapper;

    /**
     * get ObjectMapper instance
     *
     * @param createNew :true new instance; false exist instance
     * @return
     */
    public static synchronized ObjectMapper getMapperInstance(boolean createNew) {
        if (createNew) {
            return new ObjectMapper();
        } else if (mapper == null) {
            mapper = new ObjectMapper();
        }
        return mapper;
    }

    /**
     * convert java object to json string
     */
    public static String beanToJson(Object obj) throws Exception {
        try {
            ObjectMapper objectMapper = getMapperInstance(false);
            String json = objectMapper.writeValueAsString(obj);
            return json;
        } catch (Exception e) {
            dMsg.warn("bean to json error.", e);
            throw new Exception(e.getMessage());
        }
    }

    /**
     * convert java object to json string
     */
    public static String beanToJson(Object obj, Boolean createNew) throws Exception {
        try {
            ObjectMapper objectMapper = getMapperInstance(createNew);
            String json = objectMapper.writeValueAsString(obj);
            return json;
        } catch (Exception e) {
            dMsg.warn("bean to json error.", e);
            throw new Exception(e.getMessage());
        }
    }

    /**
     * convert json sting to java object
     */
    public static Object jsonToBean(String json, Class<?> cls) throws Exception {
        try {
            ObjectMapper objectMapper = getMapperInstance(false);
            Object vo = objectMapper.readValue(json, cls);
            return vo;
        } catch (Exception e) {
            dMsg.warn("json to bean error.", e);
            throw e;
        }
    }

    /**
     * convert json sting to java object
     */
    public static Object jsonToBean(String json, Class<?> cls, Boolean createNew)
            throws NotAcceptableException {
        try {
            ObjectMapper objectMapper = getMapperInstance(createNew);
            Object vo = objectMapper.readValue(json, cls);
            return vo;
        } catch (Exception e) {
            dMsg.warn("json to bean error.", e);
            throw new NotAcceptableException(e);
        }
    }
}
