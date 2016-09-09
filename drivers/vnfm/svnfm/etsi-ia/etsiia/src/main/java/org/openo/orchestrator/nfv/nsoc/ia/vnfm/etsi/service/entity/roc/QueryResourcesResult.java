/*
 * Copyright 2016, CMCC Technologies Co., Ltd.
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
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import com.google.gson.Gson;

@SuppressWarnings("rawtypes")
public class QueryResourcesResult<T> {
    private T[] data;
    private String operationResult;

    public static QueryResourcesResult fromJson(String json, Class clazz) {
        Gson gson = new Gson();
        Type objectType = type(QueryResourcesResult.class, clazz);
        return gson.fromJson(json, objectType);
    }

    public static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

    public T[] getData() {
        return data;
    }

    public void setData(T[] data) {
        this.data = data;
    }

    public String getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(String operationResult) {
        this.operationResult = operationResult;
    }
}
