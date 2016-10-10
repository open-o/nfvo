/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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

package org.openo.nfvo.vimadapter.service.entity;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openo.nfvo.vimadapter.common.Constants;
import org.openo.nfvo.vimadapter.service.constant.Constant;

import net.sf.json.JSONObject;

/**
 * VIM entity.</br>
 * @author
 *
 * @version NFVO 0.5 Sep 10, 2016
 *
 */
public class Vim {

    private String id;

    private String name;

    private String type;

    private String version;

    private String userName;

    private String pwd;

    private String url;

    private String tenant;

    public Vim() {
        // constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);

    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(null == obj) {
            return false;
        }
        if(!(obj instanceof Vim)) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        Vim other = (Vim)obj;
        if(null == id) {
            if(null != other.id) {
                return false;
            }
        } else if(!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((null == id) ? 0 : id.hashCode());
        return result;
    }

    /**
     * Generate Connection Map.<br/>
     *
     * @param paramJson
     * @return
     * @since NFVO 0.5
     */
    public Map<String, String> generateConMap(JSONObject paramJson) {
        Map<String, String> conMap = new HashMap<>(Constant.DEFAULT_COLLECTION_SIZE);
        conMap.put("url", url);
        conMap.put("userName", userName);
        conMap.put("userPwd", pwd);
        conMap.put("type", type);
        conMap.put("version", version);
        conMap.put("tenant", tenant);
        conMap.put(Constants.VIM_ID, id);
        conMap.put(Constants.VIM_NAME, name);
        conMap.put("queryId", (null == paramJson || null == paramJson.get("id")) ? null : paramJson.getString("id"));
        conMap.put(Constants.TENANT_ID_CAMEL, (null == paramJson || null == paramJson.get(Constants.TENANT_ID_CAMEL))
                ? null : paramJson.getString(Constants.TENANT_ID_CAMEL));
        return conMap;
    }
}
