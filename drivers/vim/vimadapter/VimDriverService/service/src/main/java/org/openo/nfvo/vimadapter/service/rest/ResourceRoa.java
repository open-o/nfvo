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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.baseservice.remoteservice.exception.ServiceException;
import org.openo.nfvo.vimadapter.common.Constants;
import org.openo.nfvo.vimadapter.service.adapter.impl.AdapterResourceManager;
import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * The rest interface of querying resources.<br/>
 *
 * @author
 * @version NFVO 0.5 Aug 23, 2016
 */
@Path("/v1")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class ResourceRoa {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceRoa.class);

    private AdapterResourceManager adapter;

    public void setAdapter(AdapterResourceManager adapter) {
        this.adapter = adapter;
    }

    private JSONObject getParameterJson(String vimId) {
        JSONObject paramJson = new JSONObject();
        paramJson.put(Constant.VIMID, vimId);
        return paramJson;
    }

    /**
     * Get CPU limit.<br/>
     *
     * @param context HttpServletRequest
     * @param resp HttpServletResponse
     * @param tenantId String
     * @param vimId String
     * @return CPU limit
     * @throws ServiceException
     */
    @GET
    @Path("/limits/{" + Constants.TENANT_ID + "}/cpumemory")
    @Consumes({MediaType.APPLICATION_JSON})
    public String getCpuLimit(@Context HttpServletRequest context, @Context HttpServletResponse resp,
            @PathParam(Constants.TENANT_ID) String tenantId, @QueryParam(Constants.VIM_ID) String vimId) throws ServiceException {
        JSONObject paramJson = this.getParameterJson(vimId);
        paramJson.put("id", tenantId);

        JSONObject restJson = adapter.getCpuLimits(paramJson);

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("function=getCpuLimits, msg=getCpuLimits fail");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson.remove(Constant.RETCODE);
        return restJson.toString();
    }

    /**
     * Get disk limit.<br/>
     *
     * @param context HttpServletRequest
     * @param resp HttpServletResponse
     * @param tenantId String
     * @param vimId String
     * @return Disk limit
     * @throws ServiceException
     */
    @GET
    @Path("/limits/{" + Constants.TENANT_ID + "}/disk")
    @Consumes({MediaType.APPLICATION_JSON})
    public String getDiskLimit(@Context HttpServletRequest context, @Context HttpServletResponse resp,
            @PathParam(Constants.TENANT_ID) String tenantId, @QueryParam(Constants.VIM_ID) String vimId) throws ServiceException {
        JSONObject paramJson = this.getParameterJson(vimId);
        paramJson.put("id", tenantId);

        JSONObject restJson = adapter.getDiskLimits(paramJson);

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("function=getDiskLimits, msg=getDiskLimits fail");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson.remove(Constant.RETCODE);
        return restJson.toString();
    }

    /**
     * Get Networks.<br/>
     *
     * @param context HttpServletRequest
     * @param resp HttpServletResponse
     * @param vimId String
     * @return List of network in json format
     * @throws ServiceException
     */
    @GET
    @Path("/networks")
    @Consumes({MediaType.APPLICATION_JSON})
    public String getNetworks(@Context HttpServletRequest context, @Context HttpServletResponse resp,
            @QueryParam(Constants.VIM_ID) String vimId) throws ServiceException {
        JSONObject paramJson = this.getParameterJson(vimId);

        JSONObject restJson = adapter.getNetworks(paramJson);

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("function=getNetworks, msg=getNetworks fail");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson.remove(Constant.RETCODE);
        return restJson.toString();
    }

    /**
     * Get Network.<br/>
     *
     * @param context HttpServletRequest
     * @param resp HttpServletResponse
     * @param networkId String
     * @param vimId String
     * @return Network in json format
     * @throws ServiceException
     */
    @GET
    @Path("/networks/{networkId}")
    @Consumes({MediaType.APPLICATION_JSON})
    public String getNetwork(@Context HttpServletRequest context, @Context HttpServletResponse resp,
            @PathParam("networkId") String networkId, @QueryParam(Constants.VIM_ID) String vimId) throws ServiceException {
        JSONObject paramJson = this.getParameterJson(vimId);
        paramJson.put("id", networkId);

        JSONObject restJson = adapter.getNetworks(paramJson);

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("function=getNetwork, msg=getNetwork fail");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson.remove(Constant.RETCODE);
        return restJson.toString();
    }

    /**
     * Rest interface to perform query hosts operation.<br/>
     *
     * @param context HttpServletRequest Object
     * @param resp HttpServletResponse
     * @param tenantId String
     * @param vimId information
     * @param id hostId information
     * @return The object of ResultRsp contains networks information
     * @throws ServiceException When query hosts failed
     * @since NFVO 0.5
     */
    @GET
    @Path("/{" + Constants.TENANT_ID + "}/hosts")
    @Consumes({MediaType.APPLICATION_JSON})
    public String getHosts(@Context HttpServletRequest context, @Context HttpServletResponse resp,
            @PathParam(Constants.TENANT_ID) String tenantId, @QueryParam(Constants.VIM_ID) String vimId) throws ServiceException {
        JSONObject paramJson = this.getParameterJson(vimId);
        paramJson.put("tenantId", tenantId);

        JSONObject restJson = adapter.getHosts(paramJson);

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("function=getHosts, msg=getHosts fail");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson.remove(Constant.RETCODE);
        return restJson.toString();
    }

    /**
     * Get Host. <br/>
     *
     * @param context HttpServletRequest
     * @param resp HttpServletResponse
     * @param hostName String
     * @param vimId String
     * @param tenantId String
     * @return List of hosts in json format
     * @throws ServiceException
     */
    @GET
    @Path("/{" + Constants.TENANT_ID + "}/hosts/{host_name}")
    @Consumes({MediaType.APPLICATION_JSON})
    public String getHost(@Context HttpServletRequest context, @Context HttpServletResponse resp,
            @PathParam(Constants.TENANT_ID) String tenantId, @PathParam("host_name") String hostName,
            @QueryParam(Constants.VIM_ID) String vimId) throws ServiceException {
        JSONObject paramJson = this.getParameterJson(vimId);
        paramJson.put("tenantId", tenantId);
        paramJson.put("id", hostName);

        JSONObject restJson = adapter.getHosts(paramJson);

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("function=getHost, msg=getHost fail");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson.remove(Constant.RETCODE);
        return restJson.toString();
    }

    /**
     * Get ports.<br/>
     *
     * @param context
     * @param resp
     * @param vimId
     * @return
     * @throws ServiceException
     */
    @GET
    @Path("/ports")
    @Consumes({MediaType.APPLICATION_JSON})
    public String getPorts(@Context HttpServletRequest context, @Context HttpServletResponse resp,
            @QueryParam(Constants.VIM_ID) String vimId) throws ServiceException {
        JSONObject paramJson = this.getParameterJson(vimId);
        JSONObject restJson = adapter.getPorts(paramJson);

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("function=getPorts, msg=getPorts fail");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson.remove(Constant.RETCODE);
        return restJson.toString();

    }

    /**
     * Get Port.<br/>
     *
     * @param context HttpServletRequest
     * @param resp HttpServletResponse
     * @param id String port id
     * @param vimId String VIM id
     * @return Port in json format
     * @throws ServiceException
     */
    @GET
    @Path("/ports/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public String getPort(@Context HttpServletRequest context, @Context HttpServletResponse resp,
            @PathParam("id") String id, @QueryParam(Constants.VIM_ID) String vimId) throws ServiceException {
        JSONObject paramJson = this.getParameterJson(vimId);
        paramJson.put("id", id);

        JSONObject restJson = adapter.getPorts(paramJson);

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("function=getPort, msg=getPort fail");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson.remove(Constant.RETCODE);
        return restJson.toString();
    }

    /**
     * Get Projects.</br>
     *
     * @param context HttpServletRequest
     * @param resp HttpServletResponse
     * @param vimId String
     * @return List of projects in json format
     * @throws ServiceException
     */
    @GET
    @Path("/tenants")
    @Consumes({MediaType.APPLICATION_JSON})
    public String getProjects(@Context HttpServletRequest context, @Context HttpServletResponse resp,
            @QueryParam(Constants.VIM_ID) String vimId) throws ServiceException {
        JSONObject paramJson = this.getParameterJson(vimId);

        JSONObject restJson = adapter.getProjects(paramJson);

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("function=getProjects, msg=getProjects fail");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson.remove(Constant.RETCODE);
        return restJson.toString();
    }

    /**
     * Get Project.</br>
     *
     * @param context HttpServletRequest
     * @param resp HttpServletResponse
     * @param id String project id
     * @param vimId String vim id
     * @return Project in json format
     * @throws ServiceException
     */
    @GET
    @Path("/tenants/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public String getProject(@Context HttpServletRequest context, @Context HttpServletResponse resp,
            @PathParam("id") String id, @QueryParam(Constants.VIM_ID) String vimId) throws ServiceException {
        JSONObject paramJson = this.getParameterJson(vimId);
        paramJson.put("id", id);

        JSONObject restJson = adapter.getProjects(paramJson);

        if(restJson.getInt(Constant.RETCODE) == Constant.REST_FAIL) {
            LOG.error("function=getProject, msg=getProject fail");
            resp.setStatus(Constant.HTTP_INNERERROR);
            return restJson.toString();
        }

        restJson.remove(Constant.RETCODE);
        return restJson.toString();
    }
}
