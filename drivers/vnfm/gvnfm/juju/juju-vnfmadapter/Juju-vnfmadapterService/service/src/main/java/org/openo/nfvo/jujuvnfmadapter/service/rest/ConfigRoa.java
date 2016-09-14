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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.nfvo.jujuvnfmadapter.common.EntityUtils;
import org.openo.nfvo.jujuvnfmadapter.common.SwitchController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br/>
 * <p>
 * </p>
 * 
 * @author		quanzhong@huawei.com
 * @version     NFVO 0.5  Sep 13, 2016
 */
@Path("/config")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConfigRoa {
    private static Logger logger = LoggerFactory.getLogger(ConfigRoa.class);
    
    /**
     * 
     * <br/>
     * 
     * @param context
     * @param resp
     * @return
     * @since  NFVO 0.5
     */
    @GET
    @Path("/")
    public String initUI(@Context HttpServletRequest context, @Context HttpServletResponse resp){
        SwitchController sw = new SwitchController();
        return EntityUtils.toString(sw, SwitchController.class);
    }
    /**
     * 
     * <br/>
     * 
     * @param type
     * @param context
     * @param resp
     * @return
     * @throws ServiceException
     * @since  NFVO 0.5
     */
    @GET
    @Path("/debug/{type}")
    public boolean setDebugModel(@PathParam("type") int type,@Context HttpServletRequest context, @Context HttpServletResponse resp)
            throws ServiceException {
        if(type == 1){
            SwitchController.setDebugModel(true);
        }else{
            SwitchController.setDebugModel(false);
        }
        logger.debug("change to debug model:"+SwitchController.isDebugModel());
        return SwitchController.isDebugModel();
    }
}
