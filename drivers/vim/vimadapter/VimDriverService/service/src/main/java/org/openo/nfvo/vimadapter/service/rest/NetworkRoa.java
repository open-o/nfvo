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

package org.openo.nfvo.vimadapter.service.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.nfvo.vimadapter.service.adapter.impl.AdapterNetworkManager;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * The rest interface of network.<br/>
 *
 * @author
 * @version NFVO 0.5 Aug 23, 2016
 */
@Path("/openoapi/vimdriver/v1")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class NetworkRoa {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkRoa.class);

    private AdapterNetworkManager adapter;

    public void setAdapter(AdapterNetworkManager adapter) {
        this.adapter = adapter;
    }

    /**
     * create a virtual network in vim.<br/>
     *
     * @param resp HttpServletResponse
     * @param vimId String
     * @param context
     *            The necessary info of create a network.
     * @return The info about virtual network that had been created.
     * @throws ServiceException
     *             common exception.
     * @since NFVO 0.5
     */
    @POST
    @Path("/networks")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addNetwork(@Context HttpServletRequest context, @Context HttpServletResponse resp,
            @QueryParam("vimId") String vimId) throws ServiceException {
        JSONObject restJson = new JSONObject();

        String data = null;
        try {
            data = IOUtils.toString(context.getInputStream());
        } catch(IOException e) {
            LOG.error("function=addNetwork, msg=IOException, e={}", e);
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }
        JSONObject networkJsonObject = JSONObject.fromObject(data);

        if(null == vimId || vimId.isEmpty() || null == networkJsonObject || networkJsonObject.isEmpty()) {
            resp.setStatus(Constant.HTTP_INNERERROR);
            LOG.error("function=addNetwork, msg=networkJsonObject is null");
            return restJson.toString();
        }

        restJson = adapter.addNetwork(networkJsonObject, vimId);
        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("function=addNetwork, msg=addNetwork fail");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson.remove(Constant.RETCODE);
        return restJson.toString();
    }

    /**
     * delete a virtual network create by NFVO from vim.<br/>
     *
     * @param resp HttpServletResponse
     * @param vimId String
     * @param context
     *            HttpServletRequest Object
     * @param networkId
     *            identifier of the network to be deleted.
     * @return Status of deleting network.
     * @throws ServiceException
     *             common exception of the service.
     * @since NFVO 0.5
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/networks/{networkId}")
    public String delNetwork(@Context HttpServletRequest context, @Context HttpServletResponse resp,
            @PathParam("networkId") String networkId, @QueryParam("vimId") String vimId) throws ServiceException {
        JSONObject restJson = new JSONObject();

        if(null == networkId || networkId.isEmpty() || null == vimId || vimId.isEmpty()) {
            resp.setStatus(Constant.HTTP_INNERERROR);
            LOG.error("function=delNetwork, msg=id is null");
            return restJson.toString();
        }

        int ret = adapter.deleteNetwork(networkId, vimId);
        resp.setStatus(ret);
        return restJson.toString();
    }
}
