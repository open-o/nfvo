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

import java.io.IOException;

import java.util.ArrayList;

import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.openo.nfvo.vimadapter.service.constant.Constant;

public final class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtil() {
        // Constructor
    }

    static {
        MAPPER.setDeserializationConfig(MAPPER.getDeserializationConfig().without(
                org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    public static String getJsonFieldStr(JSONObject jsonObj, String fieldName) {
        if(null == jsonObj || null == jsonObj.get(fieldName) || "null".equals(jsonObj.getString(fieldName))) {
            return "";
        }

        return jsonObj.getString(fieldName);
    }

    public static Integer getJsonFieldInt(JSONObject jsonObj, String fieldName) {
        if(null == jsonObj || null == jsonObj.get(fieldName)) {
            return 0;
        }
        return jsonObj.getInt(fieldName);
    }

    public static Long getJsonFieldLong(JSONObject jsonObj, String fieldName) {
        if(null == jsonObj || null == jsonObj.get(fieldName)) {
            return 0L;
        }
        return jsonObj.getLong(fieldName);
    }

    public static <T> T unMarshal(String jsonstr, Class<T> type) throws IOException {
        return MAPPER.readValue(jsonstr, type);
    }

    public static <T> T unMarshal(String jsonstr, TypeReference<T> type) throws IOException {
        return MAPPER.readValue(jsonstr, type);
    }

    public static String marshal(Object srcObj) throws IOException {
        if(srcObj instanceof JSON) {
            return srcObj.toString();
        }
        return MAPPER.writeValueAsString(srcObj);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> jsonToList(String jsonString, Class<T> pojoClass) {
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        JSONObject jsonObject = null;
        List<T> list = new ArrayList<T>(Constant.DEFAULT_COLLECTION_SIZE);
        int jsonSize = jsonArray.size();
        for(int i = 0; i < jsonSize; i++) {
            jsonObject = jsonArray.getJSONObject(i);
            list.add((T)JSONObject.toBean(jsonObject, pojoClass));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T> T toBean(JSONObject jsonObject, Class<T> clazz) {
        return (T)JSONObject.toBean(jsonObject, clazz);
    }

    public static JSONObject objectToJson(Object javaObj) {
        JSONObject json = JSONObject.fromObject(javaObj);
        return json;
    }
}
