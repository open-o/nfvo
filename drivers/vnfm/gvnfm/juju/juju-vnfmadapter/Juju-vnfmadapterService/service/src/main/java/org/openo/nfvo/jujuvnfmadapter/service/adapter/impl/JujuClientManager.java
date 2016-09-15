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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openo.nfvo.jujuvnfmadapter.common.EntityUtils;
import org.openo.nfvo.jujuvnfmadapter.common.EntityUtils.ExeRes;
import org.openo.nfvo.jujuvnfmadapter.service.adapter.inf.IJujuClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;


/**
 * <br/>
 * <p>
 * </p>
 *
 * @author        quanzhong@huawei.com
 * @version     NFVO 0.5  Sep 7, 2016
 */
public class JujuClientManager implements IJujuClientManager {
    private static Logger log = LoggerFactory.getLogger(JujuClientManager.class);

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
    public JSONObject deploy(String charmPath, int mem, String appName) {
        JSONObject result = new JSONObject();
        int finalMem = mem;
        if(finalMem < 2){
            finalMem = 2;
        }
        if(charmPath == null || appName == null){
            String msg = "the 'charmPath' or 'appName' can not be null";
            result.put(EntityUtils.RESULT_CODE_KEY, -1);
            result.put(EntityUtils.MSG_KEY, msg);
            log.error(msg);
            return result;
        }
        List<String> commands = new ArrayList<>();
        commands.add("juju");
        commands.add("deploy");
        commands.add(charmPath);
        commands.add("--series");
        commands.add("trusty");
        commands.add("--constraints");
        commands.add("mem="+finalMem+"G");
        commands.add("--config");
        commands.add(appName);
        ExeRes exeRes = EntityUtils.execute(null,commands);
        if(exeRes.getCode() == ExeRes.SUCCESS){
            log.info("deploy success. command:"+EntityUtils.formatCommand(commands));
            result.put(EntityUtils.RESULT_CODE_KEY, 0);
            result.put(EntityUtils.DATA_KEY, exeRes.getBody());
        }else{
            log.error("deploy failed. command:"+EntityUtils.formatCommand(commands)+"\n"+exeRes);
            result.put(EntityUtils.RESULT_CODE_KEY, -1);
            result.put(EntityUtils.MSG_KEY, "deploy failed:"+exeRes.getBody());
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
        commands.add("remove-application");
        commands.add(appName);

        ExeRes exeRes = EntityUtils.execute(null,commands);
        if(exeRes.getCode() == ExeRes.SUCCESS){
            log.info("remove success. command:"+EntityUtils.formatCommand(commands));
            result.put(EntityUtils.RESULT_CODE_KEY, 0);
            result.put(EntityUtils.DATA_KEY, exeRes.getBody());
        }else{
            log.error("remove failed. command:"+EntityUtils.formatCommand(commands)+"\n"+exeRes);
            result.put(EntityUtils.RESULT_CODE_KEY, -1);
            result.put(EntityUtils.MSG_KEY, "remove failed:"+exeRes.getBody());
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
    public JSONObject getStatus(String appName) {
        JSONObject result = new JSONObject();
        List<String> commands = new ArrayList<>();
        commands.add("juju");
        commands.add("status");
        if(StringUtils.isNotBlank(appName)){
            commands.add(appName);
        }
        commands.add("--format=json");

        ExeRes exeRes = EntityUtils.execute(null,commands);
        if(exeRes.getCode() == ExeRes.SUCCESS){
            log.info("getStatus success. command:"+EntityUtils.formatCommand(commands));
            result.put(EntityUtils.RESULT_CODE_KEY, 0);
            JSONObject dataObj = buildDataObj(exeRes,appName);
            result.put(EntityUtils.DATA_KEY, dataObj);
        }else{
            log.error("getStatus failed. command:"+EntityUtils.formatCommand(commands)+"\n"+exeRes);
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
    private JSONObject buildDataObj(ExeRes exeRes,String appName) {
        JSONObject dataObj = null;
        if(StringUtils.isNotBlank(exeRes.getBody())){
            dataObj = JSONObject.fromObject(exeRes.getBody());
            //according to appName to select
        }
        return dataObj;
    }


}
