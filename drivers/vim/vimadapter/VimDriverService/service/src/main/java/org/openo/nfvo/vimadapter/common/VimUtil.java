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

package org.openo.nfvo.vimadapter.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vimadapter.common.servicetoken.VimRestfulUtil;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.constant.UrlConstant;
import org.openo.nfvo.vimadapter.service.entity.Vim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author
 * @version NFVO 0.5 Sep 1, 2016
 */
public final class VimUtil {

    private static final Logger LOG = LoggerFactory.getLogger(VimUtil.class);

    private VimUtil() {

    }

    /**
     * Get VIMs.
     * @return
     */
    public static List<Vim> getVims() {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("url", UrlConstant.ESR_GET_VIMS_URL);
        paramsMap.put("methodType", Constant.GET);
        RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null);
        if(rsp == null || rsp.getStatus() != Constant.HTTP_OK_STATUS_CODE) {
            LOG.error("ESR return fail.");
            return null;
        }
        LOG.warn("funtion=getVims, status={}, content={}", rsp.getStatus(), rsp.getResponseContent());
        JSONArray responseContent = JSONArray.fromObject(rsp.getResponseContent());
        List<Vim> vimList = new ArrayList<Vim>(Constant.DEFAULT_COLLECTION_SIZE);
        for(int i = 0; i < responseContent.size(); i++) {
            JSONObject obj = (JSONObject)responseContent.get(i);
            Vim vim = getVimFromResponseContent(obj);
            vimList.add(vim);
        }
        return vimList;
    }

    /**
     * Get VIM.
     * @param vimId
     * @return
     */
    public static Vim getVimById(String vimId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("url", UrlConstant.ESR_GET_VIM_URL);
        paramsMap.put("methodType", Constant.GET);
        RestfulResponse rsp = VimRestfulUtil.getRemoteResponse(paramsMap, null);
        if(rsp == null || rsp.getStatus() != Constant.HTTP_OK_STATUS_CODE) {
            LOG.error("ESR return fail.");
            return null;
        }
        LOG.warn("funtion=getVimById, status={}, content={}", rsp.getStatus(), rsp.getResponseContent());
        JSONObject responseContent = JSONObject.fromObject(rsp.getResponseContent());
        return getVimFromResponseContent(responseContent);
    }

    /**
     * <br/>
     * 
     * @param responseContent
     * @return
     * @since NFVO 0.5
     */
    private static Vim getVimFromResponseContent(JSONObject responseContent) {
        Vim vim = new Vim();
        vim.setId(responseContent.getString("vimId"));
        vim.setName(responseContent.getString("name"));
        vim.setUrl(responseContent.getString("url"));
        vim.setUserName(responseContent.getString("userName"));
        vim.setPwd(responseContent.getString("password"));
        vim.setType(responseContent.getString("type"));
        vim.setVersion(responseContent.getString("version"));
        LOG.warn("funtion=getVimFromResponseContent, vim={}.", vim);
        return vim;
    }
}
