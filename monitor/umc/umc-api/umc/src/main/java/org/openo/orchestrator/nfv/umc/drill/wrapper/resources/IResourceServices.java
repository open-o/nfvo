/*
 * Copyright 2016, CMCC Technologies Co., Ltd.
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
package org.openo.orchestrator.nfv.umc.drill.wrapper.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.HostData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.NsData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VduData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VimData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VnfData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VnfcData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VnfmData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.response.RestQueryListReturnMsg;

/**
 *       the resource service interface definition(used to build service request,not identical to
 *       the server-side)
 */
@Path("/resource")
public interface IResourceServices {

    /**
     * query all the NS nodes of the system
     *
     * @param conductorId
     * @return
     */
    @GET
    @Path("/nsrs")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<NsData> queryNssOfConductor(
            @QueryParam("conductorId") String conductorId) throws Exception;

    /**
     * query NS info by the nsId
     *
     * @param nsId
     * @return
     */
    @GET
    @Path("/nsrs/{ns_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<NsData> queryNs(@PathParam("ns_id") String nsId);

    /**
     * query the NS info the VNF identified by vnfId belongs to
     *
     * @param vnfId
     * @return
     */
    @GET
    @Path("/nsrs")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<NsData> queryNssOfVNF(@QueryParam("vnfId") String vnfId);

    /**
     * query the VNFs that not belong to any NS node
     *
     * @param conductorId
     * @return
     */
    @GET
    @Path("/vnfs")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<VnfData> queryVnfsOfConductor(
            @QueryParam("conductorId") String conductorId) throws Exception;


    /**
     * query all the VNFs that belongs to the NS identified by nsId
     *
     * @param nsId
     * @return
     */
    @GET
    @Path("/vnfs")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<VnfData> queryVnfsOfNS(@QueryParam("nsId") String nsId)
            throws Exception;

    /**
     * query the VNF info identified by vnfId
     *
     * @param vnfId
     * @return
     */
    @GET
    @Path("/vnfs/{vnfid}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<VnfData> queryVnf(@PathParam("vnfid") String vnfId)
            throws Exception;

    /**
     * query the VNFC list that belongs to the VNF identified by vnfId
     *
     * @param vnfId
     * @return
     */
    @GET
    @Path("/vnfcs")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<VnfcData> queryVnfcsOfVNF(@QueryParam("vnfId") String vnfId)
            throws Exception;

    /**
     * query the VNFC info by vnfcId
     *
     * @param vnfcId
     * @return
     */
    @GET
    @Path("/vnfcs/{vnfc_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<VnfcData> queryVnfc(@PathParam("vnfc_id") String vnfcId)
            throws Exception;

    /**
     * query the VNFC nodes deployed on the VDU identified by vduId
     *
     * @param vnfId
     * @return
     */
    @GET
    @Path("/vnfcs")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<VnfcData> queryVnfcsOfVDU(@QueryParam("vduId") String vduId)
            throws Exception;

    /**
     * query all the VDUs in the system
     *
     * @param vduId
     * @return
     */
    @GET
    @Path("/vdus")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<VduData> queryAllVdus() throws Exception;


    /**
     * query the vdu info by vduId
     *
     * @param vduId
     * @return
     */
    @GET
    @Path("/vdus/{vdu_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<VduData> queryVdu(@PathParam("vdu_id") String vduId)
            throws Exception;

    /**
     * query all the VDU nodes hosted on the Host identified by hostId
     *
     * @param request
     * @return
     */
    @GET
    @Path("/vdus")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<VduData> queryVdusOfHost(@QueryParam("hostId") String hostId)
            throws Exception;

    /**
     * query Host info by hostId
     *
     * @param hostId
     * @return
     */
    @GET
    @Path("/hosts/{host_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<HostData> queryHost(@PathParam("host_id") String hostId)
            throws Exception;

    /**
     * query all the host nodes in the system
     *
     * @param hostId
     * @return
     */
    @GET
    @Path("/hosts")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<HostData> queryAllHosts() throws Exception;

    /**
     * query the VNFM info by the vnfmId
     *
     * @param vnfmId
     * @return
     * @throws Exception
     */
    @GET
    @Path("/vnfms/{vnfm_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<VnfmData> queryVnfm(@PathParam("vnfm_id") String vnfmId)
            throws Exception;

    /**
     * query all the VNFM nodes in the system
     *
     * @return
     * @throws Exception
     */
    @GET
    @Path("/vnfms")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<VnfmData> queryAllVnfms() throws Exception;

    /**
     * query the VIM info by the vimId
     *
     * @param vimId
     * @return
     * @throws Exception
     */
    @GET
    @Path("/vims/{vim_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<VimData> queryVim(@PathParam("vim_id") String vimId)
            throws Exception;

    /**
     * query all the VIM nodes in the system
     *
     * @return
     * @throws Exception
     */
    @GET
    @Path("/vims")
    @Produces(MediaType.APPLICATION_JSON)
    public RestQueryListReturnMsg<VimData> queryAllVims() throws Exception;
}
