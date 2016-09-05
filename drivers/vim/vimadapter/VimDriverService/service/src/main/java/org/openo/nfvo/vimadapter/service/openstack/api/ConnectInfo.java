/*
 * Copyright 2016, Huawei Technologies Co., Ltd.
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
package org.openo.nfvo.vimadapter.service.openstack.api;

import java.util.Map;

import net.sf.json.JSONObject;

import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.entity.Vim;

public class ConnectInfo {

    private String url;

    private String domainName;

    private String userName;

    private String userPwd;

    private String authenticateMode;

    public ConnectInfo() {
        // constructor
    }

    public ConnectInfo(Map<String, String> conMap) {
        String extraInfo = conMap.get("extraInfo");
        JSONObject extraInfoJsonObject = JSONObject.fromObject(extraInfo);
        this.domainName = null == extraInfoJsonObject.get("domain") ? "" : extraInfoJsonObject.getString("domain");
        this.authenticateMode =
                null == extraInfoJsonObject.get("authenticMode") ? Constant.AuthenticationMode.ANONYMOUS
                        : extraInfoJsonObject.getString("authenticMode");
        this.url = null == conMap.get("url") ? "" : conMap.get("url");
        this.userName = null == conMap.get("userName") ? "" : conMap.get("userName");
        this.userPwd = null == conMap.get("userPwd") ? "" : conMap.get("userPwd");
    }

    public ConnectInfo(Vim vim) {
        JSONObject extraInfoJsonObject = JSONObject.fromObject(vim.getExtraInfo());

        this.url = null == vim.getUrl() ? "" : vim.getUrl();
        this.domainName = null == extraInfoJsonObject.get("domain") ? "" : extraInfoJsonObject.getString("domain");
        this.userName = null == vim.getUserName() ? "" : vim.getUserName();
        this.userPwd = null == vim.getPwd() ? "" : vim.getPwd();
        this.authenticateMode =
                null == extraInfoJsonObject.get("authenticMode") ? Constant.AuthenticationMode.ANONYMOUS
                        : extraInfoJsonObject.getString("authenticMode");
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((null == url) ? 0 : url.hashCode());
        return result;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public void setAuthenticateMode(String authenticateMode) {
        this.authenticateMode = authenticateMode;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(null == obj) {
            return false;
        }
        if(!(obj instanceof ConnectInfo)) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        ConnectInfo other = (ConnectInfo)obj;
        if(null == url) {
            if(null != other.url) {
                return false;
            }
        } else if(!url.equals(other.url)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ConnectInfo [AuthenticateMode: " + authenticateMode + ",url=" + url + ", domainName=" + domainName
                + ", userName=" + userName + ']';
    }

    public ConnectInfo generateConByMap(Map<String, String> conMap) {
        return new ConnectInfo(conMap);
    }

    public String getUrl() {
        return url;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public String getAuthenticateMode() {
        return authenticateMode;
    }

    public boolean isNeedRenewInfo(ConnectInfo info) {
        return !(url.equals(info.getUrl()) && domainName.equals(info.getDomainName())
                && userName.equals(info.getUserName()) && userPwd.equals(info.getUserPwd()) && authenticateMode
                    .equals(info.getAuthenticateMode()));
    }

}
