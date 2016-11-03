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

package org.openo.nfvo.jujuvnfmadapter.service.adapter.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.jujuvnfmadapter.common.EntityUtils;
import org.openo.nfvo.jujuvnfmadapter.common.EntityUtils.ExeRes;
import org.openo.nfvo.jujuvnfmadapter.common.JujuConfigUtil;
import org.openo.nfvo.jujuvnfmadapter.common.UnCompressUtil;
import org.openo.nfvo.jujuvnfmadapter.common.YamlUtil;
import org.openo.nfvo.jujuvnfmadapter.common.servicetoken.VnfmRestfulUtil;
import org.openo.nfvo.jujuvnfmadapter.service.adapter.inf.IJujuClientManager;
import org.openo.nfvo.jujuvnfmadapter.service.constant.Constant;
import org.openo.nfvo.jujuvnfmadapter.service.constant.UrlConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSON;
import net.sf.json.JSONObject;


/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author        
 * @version     NFVO 0.5  Sep 7, 2016
 */
public class JujuClientManager implements IJujuClientManager {
    private static final Logger LOG = LoggerFactory.getLogger(JujuClientManager.class);

    /**
     * <br/>
     * 
     * @param charmPath
     * @param mem
     * @param appName
     * @return
     * @since   NFVO 0.5
     */
    @Override
    public JSONObject deploy(String charmPath, String appName) {
        JSONObject result = new JSONObject();
        if(charmPath == null || appName == null){
            String msg = "the 'charmPath' or 'appName' can not be null";
            result.put(EntityUtils.RESULT_CODE_KEY, -1);
            result.put(EntityUtils.MSG_KEY, msg);
            LOG.error(msg);
            return result;
        }
        //add-model
        this.addModel(appName);//use appName as modelName
        //switch model
        this.switchModel(appName);
        //deploy service
        List<String> commands = new ArrayList<>();
        commands.add("juju");
        commands.add("deploy");
        if(StringUtils.isNotBlank(charmPath)){
            String fullPath = charmPath+appName;
            if(appName.endsWith(".yaml")){
                String folderName = appName.substring(0, appName.lastIndexOf("."));//unzip folder name
                fullPath = charmPath+folderName+File.separator+appName;
            }
            commands.add(fullPath);
        }else{
            commands.add(appName);
        }
        commands.add("-m"); 
        commands.add(appName);
        ExeRes exeRes = EntityUtils.execute(null,commands);
        if(exeRes.getCode() == ExeRes.SUCCESS){
            LOG.info("deploy success. command:"+EntityUtils.formatCommand(commands));
            result.put(EntityUtils.RESULT_CODE_KEY, 0);
            result.put(EntityUtils.DATA_KEY, exeRes.getBody());
        }else{
            LOG.error("deploy failed. command:"+EntityUtils.formatCommand(commands)+"\n"+exeRes);
            result.put(EntityUtils.RESULT_CODE_KEY, -1);
            result.put(EntityUtils.MSG_KEY, "deploy failed:"+exeRes.getBody());
        }
        
        return result;
    }
    
    /**
     * 
     * <br/>
     * 
     * @param modelName
     * @return
     * @since  NFVO 0.5
     */
    private JSONObject addModel(String modelName) {
        JSONObject result = new JSONObject();
        List<String> commands = new ArrayList<>();
        commands.add("juju");
        commands.add("add-model");
        commands.add(modelName);
        ExeRes exeRes = EntityUtils.execute(null,commands);
        if(exeRes.getCode() == ExeRes.SUCCESS){
            LOG.info("addModel success. command:"+EntityUtils.formatCommand(commands));
            result.put(EntityUtils.RESULT_CODE_KEY, 0);
            result.put(EntityUtils.DATA_KEY, exeRes.getBody());
        }else{
            LOG.error("addModel failed. command:"+EntityUtils.formatCommand(commands)+"\n"+exeRes);
            result.put(EntityUtils.RESULT_CODE_KEY, -1);
            result.put(EntityUtils.MSG_KEY, "addModel failed:"+exeRes.getBody());
        }
        
        return result;
    }
    
    /**
     * 
     * <br/>
     * 
     * @param modelName
     * @return
     * @since  NFVO 0.5
     */
    private JSONObject switchModel(String modelName) {
        JSONObject result = new JSONObject();
        List<String> commands = new ArrayList<>();
        commands.add("juju");
        commands.add("switch");
        commands.add(modelName);
        ExeRes exeRes = EntityUtils.execute(null,commands);
        if(exeRes.getCode() == ExeRes.SUCCESS){
            LOG.info("switchModel success. command:"+EntityUtils.formatCommand(commands));
            result.put(EntityUtils.RESULT_CODE_KEY, 0);
            result.put(EntityUtils.DATA_KEY, exeRes.getBody());
        }else{
            LOG.error("switchModel failed. command:"+EntityUtils.formatCommand(commands)+"\n"+exeRes);
            result.put(EntityUtils.RESULT_CODE_KEY, -1);
            result.put(EntityUtils.MSG_KEY, "addModel failed:"+exeRes.getBody());
        }
        
        return result;
    }

    /**
     * <br/>
     * 
     * @param appName
     * @return
     * @since   NFVO 0.5
     */
    @Override
    public JSONObject destroy(String appName) {
        JSONObject result = new JSONObject();
        List<String> commands = new ArrayList<>();
        commands.add("juju");
        commands.add("destroy-model");
        commands.add("-y");
        commands.add(appName);
        
        ExeRes exeRes = EntityUtils.execute(null,commands);
        if(exeRes.getCode() == ExeRes.SUCCESS){
            LOG.info("remove success. command:"+EntityUtils.formatCommand(commands));
            result.put(EntityUtils.RESULT_CODE_KEY, 0);
            result.put(EntityUtils.DATA_KEY, exeRes.getBody());
        }else{
            LOG.error("remove failed. command:"+EntityUtils.formatCommand(commands)+"\n"+exeRes);
            result.put(EntityUtils.RESULT_CODE_KEY, -1);
            result.put(EntityUtils.MSG_KEY, "remove failed:"+exeRes.getBody());
        }
        
        return result;
       
    }

    /**
     * <br/>
     * 
     * @param modelName
     * @return
     * @since   NFVO 0.5
     */
    @Override
    public JSONObject getStatus(String modelName) {
        JSONObject result = new JSONObject();
        List<String> commands = new ArrayList<>();
        commands.add("juju");
        commands.add("status");
        if(StringUtils.isNotBlank(modelName)){
            commands.add("-m"); 
            commands.add(modelName);
        }
        commands.add("--format=json");
        
        ExeRes exeRes = EntityUtils.execute(null,commands);
        if(exeRes.getCode() == ExeRes.SUCCESS){
            LOG.info("getStatus success. command:"+EntityUtils.formatCommand(commands));
            result.put(EntityUtils.RESULT_CODE_KEY, 0);
            JSONObject dataObj = buildDataObj(exeRes);
            result.put(EntityUtils.DATA_KEY, dataObj);
        }else{
            LOG.error("getStatus failed. command:"+EntityUtils.formatCommand(commands)+"\n"+exeRes);
            result.put(EntityUtils.RESULT_CODE_KEY, -1);
            result.put(EntityUtils.MSG_KEY, "getStatus failed:"+exeRes.getBody());
        }
        
        return result;
    }

    /**
     * <br/>
     * 
     * @param exeRes
     * @return
     * @since  NFVO 0.5
     */
    private JSONObject buildDataObj(ExeRes exeRes) {
        JSONObject dataObj = null;
        if(StringUtils.isNotBlank(exeRes.getBody())){
            dataObj = JSONObject.fromObject(exeRes.getBody());
            //according to appName to select
        }
        return dataObj;
    }
    
    
    /**
     * call the juju vnfm to grant resource(disk,mem,cpu)
     * <br/>
     * @param params (fields:cpu,mem,disk,action(addResource/removeResource))
     * @param vnfId
     * @since  NFVO 0.5
     * @return
     */
    public boolean grantResource(String charmPath, String appName,String action , String vnfId){
        //1. unzip file and parse yaml file
        JSONObject params = this.unzipFileAndParseYaml(charmPath, appName, action);
        if(params == null){
            LOG.error("unzipFileAndParseYaml error,please check it.");
            return false;
        }
        
        //2. call grant service
        String url = JujuConfigUtil.getValue("grant_jujuvnfm_url");
        Map<String, String> paramsMap = new HashMap<>(6);
        paramsMap.put("url", url);
        paramsMap.put(Constant.METHOD_TYPE, Constant.PUT);
        paramsMap.put("path", String.format(UrlConstant.REST_JUJU_VNFM_GRANT_URL,vnfId));
        paramsMap.put(Constant.AUTH_MODE, Constant.AuthenticationMode.ANONYMOUS);
        RestfulResponse rsp = VnfmRestfulUtil.getRemoteResponse(paramsMap, params.toString(), null);
        if(rsp == null || rsp.getStatus() != Constant.HTTP_OK) {
            LOG.error("function=grantResource, msg=send grantResource msg to juju-vnfm get wrong results");
            return false;
        }
        //3.process result
        String response = rsp.getResponseContent();
        if(response == null){
            return false;
        }
        return true;
    }
    
    public JSONObject unzipFileAndParseYaml(String charmPath, String appName,String action){
        JSONObject compute = new JSONObject();
        compute.put("action", action);
        if(StringUtils.isBlank(charmPath)){
            LOG.error("the charmPath can't be null! [in unzipFileAndParseYaml]");
            return null;
        }
        if(!appName.endsWith(".yaml")){//set default values for non 'yaml' type;
            compute.put("cpu", 4);
            compute.put("mem", 2);
            compute.put("disk", 40);
            return compute;
        }
        String realAppName = appName.substring(0, appName.lastIndexOf(".yaml"));
        String outputDirectory = realAppName;
        String zipfileName = null;
        if(StringUtils.isNotBlank(charmPath)){
            outputDirectory = charmPath+ outputDirectory;
        }
        if(new File(outputDirectory+".tar.xz").exists()){
            zipfileName = outputDirectory+".tar.xz";
        }else if(new File(outputDirectory+".tar.gz").exists()){
            zipfileName = outputDirectory+".tar.gz";
        }else if(new File(outputDirectory+".zip").exists()){
            zipfileName = outputDirectory+".zip";
        }else{
            LOG.error("can't find the charm file package!path="+charmPath+", appName="+appName);
            return null;
        } 
        boolean result = UnCompressUtil.unCompress(zipfileName, outputDirectory, new ArrayList<String>());
        LOG.info("unzip file result:"+result);
        File yamlFile = new File(outputDirectory+File.separator+appName);
        if(yamlFile.exists()){
            JSON json = YamlUtil.yamlToJson(yamlFile.getAbsolutePath());
            json.isArray();
            //TODO:parse yaml here
        }else{
            LOG.error("the yaml file not exist!file="+yamlFile); 
            return null;
        }
        return compute;
    }
   
    
  

    
}
