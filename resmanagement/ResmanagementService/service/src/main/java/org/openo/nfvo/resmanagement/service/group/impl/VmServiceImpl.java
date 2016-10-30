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

package org.openo.nfvo.resmanagement.service.group.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.nfvo.resmanagement.service.dao.inf.VmDao;
import org.openo.nfvo.resmanagement.service.entity.VmEntity;
import org.openo.nfvo.resmanagement.service.group.inf.VmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * <br>
 * <p>
 * </p>
 * 
 * @author
 * @version NFVO 0.5 Oct 29, 2016
 */
public class VmServiceImpl implements VmService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VmServiceImpl.class);

    private VmDao vmDao;

    /**
     * <br>
     * 
     * @param vmEntity
     * @return
     * @throws ServiceException
     * @since NFVO 0.5
     */
    @Override
    public JSONObject addVm(VmEntity vmEntity) throws ServiceException {
        int result;
        if(!checkId(vmEntity.getVmId())) {
            result = vmDao.updateVm(vmEntity);
        } else {
            if(StringUtils.isEmpty(vmEntity.getVmId())) {
                vmEntity.setVmId(UUID.randomUUID().toString());
            }
            result = vmDao.addVm(vmEntity);
        }
        JSONObject restJson = new JSONObject();
        if(result > 0) {
            restJson.put("id", vmEntity.getVmId());
            restJson.put("name", vmEntity.getVmName());
        } else {
            LOGGER.error("function=addVm; msg=add vm into DB error.");
            restJson.put("message", "Add vm into DB error.");
        }
        return restJson;
    }

    /**
     * <br>
     * 
     * @param vmId
     * @return
     * @since NFVO 0.5
     */
    private boolean checkId(String vmId) {
        VmEntity vm = vmDao.getVm(vmId);
        if(null == vm) {
            return true;
        }
        return false;
    }

    /**
     * <br>
     * 
     * @param map
     * @return
     * @throws ServiceException
     * @since NFVO 0.5
     */
    @Override
    public List<VmEntity> getList(Map<String, Object> map) throws ServiceException {
        return vmDao.getVms(map);
    }

    /**
     * <br>
     * 
     * @param id
     * @return
     * @throws ServiceException
     * @since NFVO 0.5
     */
    @Override
    public int delete(String id) throws ServiceException {
        return vmDao.deleteVmById(id);
    }

    public void setVmDao(VmDao vmDao) {
        this.vmDao = vmDao;
    }
}
