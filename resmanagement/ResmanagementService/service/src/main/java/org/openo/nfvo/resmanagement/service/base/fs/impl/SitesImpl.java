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

package org.openo.nfvo.resmanagement.service.base.fs.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.nfvo.resmanagement.common.constant.ParamConstant;
import org.openo.nfvo.resmanagement.common.util.JsonUtil;
import org.openo.nfvo.resmanagement.service.base.fs.inf.Sites;
import org.openo.nfvo.resmanagement.service.business.inf.SitesBusiness;
import org.openo.nfvo.resmanagement.service.entity.SitesEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * DC info interface.<br/>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Aug 24, 2016
 */
public class SitesImpl implements Sites {

    private static final Logger LOGGER = LoggerFactory.getLogger(SitesImpl.class);

    private SitesBusiness sitesBusiness;

    @Override
    public int add(JSONObject jsonObject) throws ServiceException {
        SitesEntity sitesEntity = SitesEntity.toEntity(jsonObject);
        if(StringUtils.isEmpty(sitesEntity.getId())) {
            sitesEntity.setId(UUID.randomUUID().toString());
            jsonObject.put(ParamConstant.PARAM_ID, sitesEntity.getId());
        }
        return sitesBusiness.addSite(sitesEntity);
    }

    @Override
    public int update(SitesEntity sitesEntity) throws ServiceException {
        return sitesBusiness.updateSiteSelective(sitesEntity);
    }

    @Override
    public int update(JSONObject jsonObject) throws ServiceException {
        JSONObject sitesObj = dataParse(jsonObject);
        return sitesBusiness.updateSiteSelective(SitesEntity.toEntity(sitesObj));
    }

    private JSONObject dataParse(JSONObject jsonObject) throws ServiceException {
        String id = jsonObject.getString("id");
        Map<String, Object> condition = new HashMap<>();
        condition.put("id", id);
        SitesEntity sitesEntity = get(condition);
        if(null == sitesEntity) {
            LOGGER.error("Get sites null, id={}", id);
            return null;
        }
        return computeSiteUsed(jsonObject, sitesEntity);
    }

    private JSONObject computeSiteUsed(JSONObject jsonObject, SitesEntity sitesEntity) {
        String action = JsonUtil.getJsonFieldStr(jsonObject, "action");
        String usedCpu = JsonUtil.getJsonFieldStr(jsonObject, "usedCPU");
        String usedMemory = JsonUtil.getJsonFieldStr(jsonObject, "usedMemory");
        String usedDisk = JsonUtil.getJsonFieldStr(jsonObject, "usedDisk");
        String oldCpu = sitesEntity.getUsedCPU();
        String oldMemory = sitesEntity.getUsedMemory();
        String oldDisk = sitesEntity.getUsedDisk();
        String newCpu = accumOrFreeRes(usedCpu, oldCpu, action);
        String newMemory = accumOrFreeRes(usedMemory, oldMemory, action);
        String newDisk = accumOrFreeRes(usedDisk, oldDisk, action);

        JSONObject resUsed = new JSONObject();
        resUsed.put("usedCPU", newCpu);
        resUsed.put("usedMemory", newMemory);
        resUsed.put("usedDisk", newDisk);
        resUsed.put("id", sitesEntity.getId());
        resUsed.put("name", sitesEntity.getName());
        resUsed.put("status", sitesEntity.getStatus());
        resUsed.put("location", sitesEntity.getLocation());
        resUsed.put("country", sitesEntity.getCountry());
        resUsed.put("vimId", sitesEntity.getVimId());
        resUsed.put("vimName", sitesEntity.getVimName());
        resUsed.put("totalCPU", sitesEntity.getTotalCPU());
        resUsed.put("totalMemory", sitesEntity.getTotalMemory());
        resUsed.put("totalDisk", sitesEntity.getTotalDisk());
        return resUsed;
    }

    private String accumOrFreeRes(String resUsed, String resOld, String action) {
        BigDecimal iResUsed = new BigDecimal(resUsed);
        BigDecimal iResOld = new BigDecimal(resOld);
        if("online".equals(action)) {
            return String.valueOf(iResOld.add(iResUsed));
        } else {
            return String.valueOf(iResOld.subtract(iResUsed));
        }
    }

    @Override
    public int updateResource(JSONObject jsonObject) throws ServiceException {
        return sitesBusiness.updateSiteResource(SitesEntity.toEntity(jsonObject));
    }

    @Override
    public int delete(String id) throws ServiceException {
        return sitesBusiness.deleteSite(id);
    }

    @Override
    public int updateStatusByVimId(JSONObject jsonObject) throws ServiceException {
        return sitesBusiness.updateSiteByVimId(SitesEntity.toEntity(jsonObject));
    }

    @Override
    public List<SitesEntity> getList(Map<String, Object> condition) throws ServiceException {
        return sitesBusiness.getSites(condition);
    }

    @Override
    public SitesEntity get(Map<String, Object> condition) throws ServiceException {
        List<SitesEntity> siteList = sitesBusiness.getSites(condition);
        if(null == siteList || siteList.isEmpty()) {
            return null;
        }
        return siteList.get(0);
    }

    public void setSitesBusiness(SitesBusiness sitesBusiness) {
        this.sitesBusiness = sitesBusiness;
    }

    @Override
    public int deleteResByVimId(String vimId) throws ServiceException {
        return 0;
    }

}
