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

package org.openo.nfvo.jujuvnfmadapter.service.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.nfvo.jujuvnfmadapter.common.EntityUtils;
import org.openo.nfvo.jujuvnfmadapter.common.JujuConfigUtil;
import org.openo.nfvo.jujuvnfmadapter.common.StringUtil;
import org.openo.nfvo.jujuvnfmadapter.service.adapter.inf.IJujuClientManager;
import org.openo.nfvo.jujuvnfmadapter.service.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author quanzhong@huawei.com
 * @version NFVO 0.5 Aug 18, 2016
 */
@Path("/openoapi/juju/v1/vnfms")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JujuClientRoa {

    private static Logger logger = LoggerFactory.getLogger(JujuClientRoa.class);

    private IJujuClientManager jujuClientManager;

    /**
     * @return Returns the jujuClientManager.
     */
    public IJujuClientManager getJujuClientManager() {
        return jujuClientManager;
    }

    /**
     * @param jujuClientManager The jujuClientManager to set.
     */
    public void setJujuClientManager(IJujuClientManager jujuClientManager) {
        this.jujuClientManager = jujuClientManager;
    }
    

    /**
     * Set Charm url for juju deployment
     * <br/>
     * 
     * @param resp
     * @param context
     *            parameter : charmUrl
     * @return "{"charmUrl":"http://dld_url"}
     * @since NFVO 0.5
     */
    @POST
    @Path("/setCharmUrl")
    public String setCharmUrl(@Context HttpServletRequest context, @Context HttpServletResponse resp)
            throws ServiceException {
        JSONObject result = new JSONObject();
        result.put("retCode", Constant.REST_FAIL);
        JSONObject reqJsonObject = StringUtil.getJsonFromContexts(context);
        logger.debug(reqJsonObject + ":");
        return result.toString();
    }

    /**
     * Get VNF status
     * parameter: vnfInstanceId
     * <br/>
     * 
     * @param appName
     * @param resp
     * @param context
     * @return Depends on juju's return
     * @throws ServiceException
     * @since NFVO 0.5
     */
    @GET
    @Path("/{appName}/status")
    public String getVnfStatus(@PathParam("appName") String appName, @Context HttpServletRequest context,
            @Context HttpServletResponse resp) throws ServiceException {
        JSONObject result = jujuClientManager.getStatus(appName);
        logger.debug("status json str:"+result.toString());
        return result.toString();

    }

    /**
     * Instance VNF to juju-client
     * <br/>
     * deployParam: depend on juju require
     * 
     * @param resp
     * @param context
     * @return status: deplay result <br>
     *         the return data must be include "{
     *         app_info:{"vnfId":123344}
     *         }"
     * @throws ServiceException
     * @since NFVO 0.5
     */
    @POST
    @Path("/deploy")
    public String deploySerivce(@Context HttpServletRequest context, @Context HttpServletResponse resp)
            throws ServiceException {
        JSONObject result = new JSONObject();
        String msg;
        try {
            result.put(EntityUtils.RESULT_CODE_KEY, EntityUtils.ExeRes.FAILURE);
            JSONObject reqJsonObject = StringUtil.getJsonFromContexts(context);
            if(reqJsonObject == null || reqJsonObject.get("appName") == null){
                result.put(EntityUtils.MSG_KEY, "the param 'appName' can't be null");
                resp.setStatus(Constant.HTTP_INNERERROR); 
                return result.toString();
            }
            String charmPath = (String)reqJsonObject.get("charmPath");
            Object memObj = reqJsonObject.get("mem");
            int mem = StringUtils.isEmpty((String)memObj)?0:(Integer)memObj;
            String appName = reqJsonObject.getString("appName");
            if(StringUtils.isBlank(charmPath)) {
                charmPath = JujuConfigUtil.getValue("charmPath");
            }
            result = jujuClientManager.deploy(charmPath, mem, appName);
            if(result.getInt(EntityUtils.RESULT_CODE_KEY) == EntityUtils.ExeRes.SUCCESS){
                resp.setStatus(Constant.HTTP_CREATED); 
            }
            return result.toString();
        } catch(Exception e) {
            msg = e.getMessage();
           logger.error("deploy fail in method deployService",e);
        }
        resp.setStatus(Constant.HTTP_INNERERROR); 
        result.put(EntityUtils.MSG_KEY, msg);
        return result.toString();
    }

    /**
     * <br/>
     * 
     * @param resp
     * @param context
     * @return
     * @throws ServiceException
     * @since NFVO 0.5
     */
    @POST
    @Path("/destroy")
    public String destroySerivce(@Context HttpServletRequest context, @Context HttpServletResponse resp)
            throws ServiceException {
        JSONObject result = new JSONObject();
        result.put(EntityUtils.RESULT_CODE_KEY, EntityUtils.ExeRes.FAILURE);
        String msg;
        try {
            JSONObject reqJsonObject = StringUtil.getJsonFromContexts(context);
            if(reqJsonObject == null || reqJsonObject.get("appName") == null){
                result.put(EntityUtils.MSG_KEY, "the param 'appName' can't be null");
                resp.setStatus(Constant.HTTP_INNERERROR); 
                return result.toString();
            }
            String appName = reqJsonObject.getString("appName");
            result = jujuClientManager.destroy(appName);
            resp.setStatus(Constant.UNREG_SUCCESS);
            return result.toString();
        } catch(Exception e) {
            msg = e.getMessage();
            logger.error("destory fail in method destroyService",e);
         
        }
        resp.setStatus(Constant.HTTP_INNERERROR); 
        result.put(EntityUtils.MSG_KEY, msg);
        return result.toString();
    }

}
