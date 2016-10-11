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

package org.openo.nfvo.jujuvnfmadapter.service.process;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.jujuvnfmadapter.common.EntityUtils;
import org.openo.nfvo.jujuvnfmadapter.common.VnfmUtil;
import org.openo.nfvo.jujuvnfmadapter.common.servicetoken.VnfmRestfulUtil;
import org.openo.nfvo.jujuvnfmadapter.service.constant.Constant;
import org.openo.nfvo.jujuvnfmadapter.service.constant.UrlConstant;
import org.openo.nfvo.jujuvnfmadapter.service.entity.JujuVnfmInfo;
import org.openo.nfvo.jujuvnfmadapter.service.entity.JujuVnfmInfoExample;
import org.openo.nfvo.jujuvnfmadapter.service.mapper.JujuVnfmInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * Provide function for instantiate or terminate VNF
 * <br/>
 * 
 * @author
 * @version NFVO 0.5 Aug 24, 2016
 */
public class VnfMgr {

    private static final Logger LOG = LoggerFactory.getLogger(VnfMgr.class);
    private JujuVnfmInfoMapper jujuVnfmInfoMapper;
    
    
    /**
     * @return Returns the jujuVnfmInfoMapper.
     */
    public JujuVnfmInfoMapper getJujuVnfmInfoMapper() {
        return jujuVnfmInfoMapper;
    }

    
    /**
     * @param jujuVnfmInfoMapper The jujuVnfmInfoMapper to set.
     */
    public void setJujuVnfmInfoMapper(JujuVnfmInfoMapper jujuVnfmInfoMapper) {
        this.jujuVnfmInfoMapper = jujuVnfmInfoMapper;
    }

    /**
     * Provide function for instantiate VNF
     * <br/>
     * 
     * @param vnfObject
     * @param vnfmId
     * @return
     * @since NFVO 0.5
     */
    public JSONObject addVnf(JSONObject vnfObject, String vnfmId) {
        JSONObject restJson = new JSONObject();
        restJson.put(EntityUtils.RESULT_CODE_KEY, Constant.REST_FAIL);
        try {

            if(vnfObject.isNullObject() || vnfObject.isEmpty()) {
                return restJson;
            }

            JSONObject vnfmObject = VnfmUtil.getVnfmById(vnfmId);

            if(vnfmObject == null || vnfmObject.isNullObject()) {
                return restJson;
            }
            String vnfInstanceName = vnfObject.getString("vnfInstanceName");

            JSONObject params = new JSONObject();
            params.put(Constant.VNFM_ID, vnfmId);
            params.put("appName", vnfInstanceName);
            params.put("charmPath", "");
            params.put("mem", "");

            String url = vnfmObject.getString("url");
            Map<String, String> paramsMap = new HashMap<>(6);
            paramsMap.put("url", url);
            paramsMap.put(Constant.METHOD_TYPE, Constant.POST);
            paramsMap.put("path", UrlConstant.REST_JUJU_CLIENT_DEPLOY);
            paramsMap.put(Constant.AUTH_MODE, Constant.AuthenticationMode.ANONYMOUS);
            RestfulResponse rsp = VnfmRestfulUtil.getRemoteResponse(paramsMap, params.toString(), null);
            if(rsp == null) {
                LOG.error("function=addVnf, msg=send create vnf msg to csm get wrong results");
                return restJson;
            }

            int statusCode = rsp.getStatus();
            if(statusCode == Constant.HTTP_CREATED) {

                String vnfId = UUID.randomUUID().toString();
                saveJujuVnfmInfo(vnfInstanceName,vnfId,vnfId,vnfmId);
                restJson.put(EntityUtils.RESULT_CODE_KEY, Constant.REST_SUCCESS);
                JSONObject resultObj = new JSONObject();
                resultObj.put("vnfInstanceId", vnfId);
                resultObj.put("jobId", vnfId + "_" + Constant.POST);
                restJson.put("data", resultObj);
            } else {
                LOG.error("function=createVnf, msg=send create vnf msg to csm get wrong status: " + statusCode);
            }

        } catch(JSONException e) {
            LOG.error("function=addVnf, msg=JSONException occurs, e={}.", e);
        }
        LOG.info("request:{},response:{}", vnfmId, restJson.toString());
        return restJson;
    }
    
    /**
     * save object to db
     * <br/>
     * 
     * @param appName
     * @param jobId
     * @param vnfId
     * @param vnfmId
     * @since  NFVO 0.5
     */
    private void saveJujuVnfmInfo(String appName,String jobId,String vnfId,String vnfmId){
        JujuVnfmInfo record = new JujuVnfmInfo();
        record.setId(UUID.randomUUID().toString());
        record.setAppName(appName);
        record.setJobId(jobId);
        record.setVnfId(vnfId);
        record.setVnfmId(vnfmId);
        record.setStatus(0);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
        jujuVnfmInfoMapper.insert(record);
    }
    /**
     * delete the object by vnfid
     * <br/>
     * 
     * @param vnfId
     * @since  NFVO 0.5
     */
    private void delJujuVnfmInfo(String vnfId){
        JujuVnfmInfoExample example = new JujuVnfmInfoExample();
        example.createCriteria().andVnfIdEqualTo(vnfId);
        jujuVnfmInfoMapper.deleteByExample(example);
    }
    
    /**
     * findByVnfId from db
     * <br/>
     * 
     * @param vnfId
     * @return
     * @since  NFVO 0.5
     */
    private JujuVnfmInfo findByVnfId(String vnfId){
        JujuVnfmInfoExample example = new JujuVnfmInfoExample();
        example.createCriteria().andVnfIdEqualTo(vnfId);
        List<JujuVnfmInfo> list = jujuVnfmInfoMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(list) && !list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    /**
     * Provide function for terminate VNF
     * <br/>
     * 
     * @param vnfId
     * @param vnfmId
     * @param vnfObject
     * @return
     * @since NFVO 0.5
     */
    public JSONObject deleteVnf(String vnfId, String vnfmId, JSONObject vnfObject) {
        LOG.warn("function=deleteVnf ,msg=enter to delete a vnf, vnfId:{}, vnfmId:{}", vnfId, vnfmId);
        JSONObject restJson = new JSONObject();
        restJson.put(EntityUtils.RESULT_CODE_KEY, Constant.REST_FAIL);
        try {
            JSONObject vnfmObject = VnfmUtil.getVnfmById(vnfmId);
            if(vnfmObject==null || vnfmObject.isNullObject()) {
                LOG.error("function=deleteVnf, msg=vnfm not exists, vnfmId: {}", vnfmId);
                return restJson;
            }

            String vnfInstanceName = "";
            JujuVnfmInfo jujuInfo = findByVnfId(vnfId);
            if(jujuInfo != null){
                vnfInstanceName = jujuInfo.getAppName();
            }
            JSONObject params = new JSONObject();
            params.put(Constant.VNFM_ID, vnfmId);
            params.put("appName", vnfInstanceName);
            params.put("vnfId", vnfId);

            String url = vnfmObject.getString("url");
            Map<String, String> paramsMap = new HashMap<>(6);
            paramsMap.put("url", url);
            paramsMap.put(Constant.METHOD_TYPE, Constant.POST);
            paramsMap.put("path", UrlConstant.REST_JUJU_CLIENT_DESTORY);
            paramsMap.put(Constant.AUTH_MODE, Constant.AuthenticationMode.ANONYMOUS);
            RestfulResponse rsp = VnfmRestfulUtil.getRemoteResponse(paramsMap, params.toString(), null);
            if(rsp == null) {
                LOG.error("function=deleteVnf, msg=send create vnf msg to csm get wrong results");
                return restJson;
            }

            int statusCode = rsp.getStatus();
            if(statusCode == Constant.UNREG_SUCCESS) {
                restJson.put(EntityUtils.RESULT_CODE_KEY, Constant.REST_SUCCESS);
                JSONObject resultObj = new JSONObject();
                resultObj.put("jobId", vnfId + "_" + Constant.DELETE);
                restJson.put("data", resultObj);
                delJujuVnfmInfo(vnfId);
            } else {
                LOG.error("function=removeVnf, msg=send remove vnf msg to csm get wrong status: {}", statusCode);
            }

        } catch(JSONException e) {
            LOG.error("function=deleteVnf, msg=JSONException occurs, e={}.", e);
        }
        return restJson;
    }

    /**
     * Provide function for get VNF
     * <br/>
     * 
     * @param vnfId
     * @param vnfmId
     * @return
     * @since NFVO 0.5
     */
    public JSONObject getVnf(String vnfId, String vnfmId) {
        LOG.warn("function=getVnf ,msg=enter to get a vnf, vnfId:{}, vnfmId:{}", vnfId, vnfmId);
        JSONObject restJson = new JSONObject();
        restJson.put(EntityUtils.RESULT_CODE_KEY, Constant.REST_FAIL);
        try {
            JSONObject vnfmObject = VnfmUtil.getVnfmById(vnfmId);
            if(vnfmObject==null || vnfmObject.isNullObject()) {
                LOG.error("function=getVnf, msg=vnfm not exists, vnfmId: {}", vnfmId);
                return restJson;
            }
            
            String appName = "";
            JujuVnfmInfo jujuInfo = findByVnfId(vnfId);
            if(jujuInfo != null){
                appName = jujuInfo.getAppName();
            }
            JSONObject params = new JSONObject();
            params.put(Constant.VNFM_ID, vnfmId);
            params.put("vnfId", vnfId);

            String url = vnfmObject.getString("url");
            Map<String, String> paramsMap = new HashMap<>(6);
            paramsMap.put("url", url);
            paramsMap.put(Constant.METHOD_TYPE, Constant.GET);
            paramsMap.put("path", String.format(UrlConstant.REST_JUJU_CLIENT_GET,appName));
            paramsMap.put(Constant.AUTH_MODE, Constant.AuthenticationMode.ANONYMOUS);
            RestfulResponse rsp = VnfmRestfulUtil.getRemoteResponse(paramsMap, params.toString(), null);
            if(rsp == null) {
                LOG.error("function=getVnf, msg=send create vnf msg to csm get wrong results");
                return restJson;
            }
            JSONObject queryResult = JSONObject.fromObject(rsp.getResponseContent());
            int statusCode = rsp.getStatus();
            if(statusCode == Constant.HTTP_OK || statusCode == Constant.HTTP_CREATED) {
                if(null == (queryResult.get("data"))) {
                    LOG.warn("function=getVnf, msg=query is null {}", queryResult.get("data"));
                    return restJson;
                }
                restJson.put(EntityUtils.RESULT_CODE_KEY, Constant.REST_SUCCESS);
                restJson.put("data", JSONObject.fromObject(queryResult.getString("data")));
            } else {
                LOG.error("function=getVnf, msg=send get vnf msg to csm get wrong status: {}", statusCode);
            }
            return restJson.getInt(EntityUtils.RESULT_CODE_KEY) == Constant.REST_FAIL ? restJson
                    : getVnfBody(vnfId,appName, vnfmObject);

        } catch(JSONException e) {
            LOG.error("function=getVnf, msg=JSONException occurs, e={}.", e);
            restJson.put(EntityUtils.RESULT_CODE_KEY, Constant.REST_FAIL);
        }
        return restJson;
    }

    private JSONObject getVnfBody(String vnfId,String appName, JSONObject vnfmObject) {
        JSONObject vnfInfoJson = new JSONObject();
        JSONObject basicInfoJson = new JSONObject();

        basicInfoJson.put("vnfInstanceId", vnfId);
        basicInfoJson.put("vnfInstanceName", appName);
        basicInfoJson.put("vnfInstanceDescription", "vFW");
        basicInfoJson.put("vnfdId", vnfmObject == null ? "" : vnfmObject.getString("vnfdId"));
        basicInfoJson.put("vnfdPackageId", vnfmObject == null ? "" : vnfmObject.getString("vnfPackageId"));
        basicInfoJson.put("version", vnfmObject == null ? "" : vnfmObject.getString("version"));
        basicInfoJson.put("vnfProvider", "hw");
        basicInfoJson.put("vnfType", appName);
        basicInfoJson.put("vnfStatus", "activie");

        vnfInfoJson.put("vnfInfo", basicInfoJson);
        vnfInfoJson.put(EntityUtils.RESULT_CODE_KEY, Constant.REST_SUCCESS);
        return vnfInfoJson;
    }

    /**
     * Provide function for get job
     * <br/>
     * 
     * @param jobId
     * @param vnfmId
     * @return
     * @since NFVO 0.5
     */
    public JSONObject getJob(String jobId, String vnfmId) {
        LOG.warn("function=getJob ,msg=enter to get a job, vnfId:{}", jobId);
        JSONObject restJson = new JSONObject();
        restJson.put(EntityUtils.RESULT_CODE_KEY, Constant.REST_FAIL);
        try {
            JSONObject vnfmObject = VnfmUtil.getVnfmById(vnfmId);
            if(vnfmObject==null || vnfmObject.isNullObject()) {
                LOG.error("function=getJob, msg=vnfm not exists, vnfmId: {}", vnfmId);
                return restJson;
            }
            String url = vnfmObject.getString("url");
            Map<String, String> paramsMap = new HashMap<>(6);
            paramsMap.put("url", url);
            paramsMap.put(Constant.METHOD_TYPE, Constant.GET);
            paramsMap.put("path", String.format(UrlConstant.REST_JUJU_CLIENT_GET,jobId));
            paramsMap.put(Constant.AUTH_MODE, Constant.AuthenticationMode.ANONYMOUS);
            RestfulResponse rsp = VnfmRestfulUtil.getRemoteResponse(paramsMap, null, null);
            if(rsp == null) {
                LOG.error("function=getVnf, msg=send create vnf msg to csm get wrong results");
                return restJson;
            }
            JSONObject queryResult = JSONObject.fromObject(rsp.getResponseContent());
            int statusCode = rsp.getStatus();
            if(statusCode == Constant.HTTP_OK || statusCode == Constant.HTTP_CREATED) {

                if((queryResult.get("data")) == null) {
                    LOG.warn("function=getJob, msg=query is null {}", queryResult.get("data"));
                    return restJson;
                }
                restJson.put(EntityUtils.RESULT_CODE_KEY, Constant.REST_SUCCESS);
                restJson.put("data", JSONObject.fromObject(queryResult.getString("data")));
            } else {
                LOG.error("function=getJob, msg=send get vnf msg to csm get wrong status: {}", statusCode);
            }
        } catch(JSONException e) {
            LOG.error("function=getJob, msg=JSONException occurs, e={}.", e);
            restJson.put(EntityUtils.RESULT_CODE_KEY, Constant.REST_FAIL);
        }
        return restJson;
    }
}
