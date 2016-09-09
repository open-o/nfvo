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
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.roc;

import javax.ws.rs.WebApplicationException;

import org.glassfish.jersey.client.ClientConfig;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.common.MSBUtil;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.ApplicationInstanceBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.CPBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.CreateResourceResult;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.HostBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.NSBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.NetworkBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.QueryResourcesResult;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.SubnetBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.VDUBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.VNFBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.VNFCBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.VNFMBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.VimBaseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;
import com.google.gson.Gson;

/**
* @ClassName: RocServiceConsumer
* @Description: import OSGi-JAX-RS Connector plugin.use ClientConfig and ConsumerFactory to access REST interface and get ROC data.
* @author huangleibo10186364
* @date 2016-01-05 10:46:59
*
*/
@SuppressWarnings("unchecked")
public class RocServiceConsumer {
    private static final Logger logger = LoggerFactory
            .getLogger(RocServiceConsumer.class);

    public static final String REQUEST_MSG_BODY_PREFIX = "";
    private static IRocServiceRest resourcecenterproxy;

    private static IRocServiceRest getResourceCenterProxy() {
        if (resourcecenterproxy == null) {
            ClientConfig config = new ClientConfig();
            resourcecenterproxy = ConsumerFactory.createConsumer(MSBUtil.getRocBaseUrl(),
                    config, IRocServiceRest.class);
        }
        return resourcecenterproxy;
    }

    public static ApplicationInstanceBaseInfo retrieveAllAppInstance() {
        String result =  getResourceCenterProxy().retrieveAllInstances("0,6",null);
        Gson gson = new Gson();
        ApplicationInstanceBaseInfo allApp = gson.fromJson(result,
                ApplicationInstanceBaseInfo.class);
        return allApp;
    }

    public static ApplicationInstanceBaseInfo retrieveResourcesByInstanceId(String typeId,String instanceId)
    {
        if(typeId==null || typeId.length()==0 || instanceId==null || instanceId.length()==0)
        {
            return null;
        }

        String result =  getResourceCenterProxy().retrieveAllInstances(typeId,instanceId);
        ApplicationInstanceBaseInfo resouece = new Gson().fromJson(result,ApplicationInstanceBaseInfo.class);
        return resouece;
    }


    public static VNFBaseInfo[] retrieveVnfs(String vnfid) {
        // null,get all
        if (vnfid == null) {
            vnfid = "";
        }
        String result =  getResourceCenterProxy().retrieveVnfs(vnfid);
        logger.debug("query vnfs:{}", result);
        QueryResourcesResult<VNFBaseInfo> vnfRslt = QueryResourcesResult
                .fromJson(result, VNFBaseInfo.class);
        return vnfRslt != null ? vnfRslt.getData() : null;
    }

    public static VNFBaseInfo[] retrieveVnfs(String vnfmId, String nsId,
            String conductorId) {
        if (vnfmId == null && nsId == null && conductorId == null) {
            logger.info("vnfmId==null  nsId==null  conductorId==null,query all.forbidden.");
            return null;
        }
        String result =  getResourceCenterProxy().retrieveVnfs(vnfmId, nsId,
                conductorId);
        logger.debug("query vnfs:{}", result);
        QueryResourcesResult<VNFBaseInfo> vnfRslt = QueryResourcesResult
                .fromJson(result, VNFBaseInfo.class);
        return vnfRslt != null ? vnfRslt.getData() : null;
    }

    public static String createVnf(VNFBaseInfo vnf) {
        Gson gson = new Gson();
        String vnfJsonInfo = gson.toJson(vnf, VNFBaseInfo.class);
        logger.debug("crate vnf request:{}", vnfJsonInfo);
        String result =  getResourceCenterProxy().createVnf(REQUEST_MSG_BODY_PREFIX
                + vnfJsonInfo);
        logger.debug("crate vnf response:{}", result);
        CreateResourceResult object = CreateResourceResult.fromJson(result);
        if (object.getOperationResult() != null
                && object.getOperationResult().equals("SUCCESS")) {
            return object.getOid();
        }

        throw new WebApplicationException("create vnf to database failed.");
    }

    public static boolean deleteVnf(String vnfId) {
        if (vnfId == null)// vnfid=null,delete all vnfs.forbidden
        {
            logger.info("vnfid=null,delete all vnfs.forbidden.");
            return false;
        }
        String result =  getResourceCenterProxy().deleteVnf(vnfId);
        logger.debug("delete vnf response:{}", result);
        return result.contains("SUCCESS");
    }

    public static boolean updateVnf(String vnfId, VNFBaseInfo vnf) {
        Gson gson = new Gson();
        String vnfJsonInfo = gson.toJson(vnf, VNFBaseInfo.class);
        logger.debug("update vnf request:{}", vnfJsonInfo);
        String result =  getResourceCenterProxy().updateVnf(vnfId,
                REQUEST_MSG_BODY_PREFIX + vnfJsonInfo);
        logger.info("update vnf response:{}", result);
        return result.contains("SUCCESS");
    }

    public static VNFCBaseInfo[] retrieveVnfcs(String vnfId, String vduId) {
        if (vnfId == null && vduId == null){// vnfid=null,query all; vnfcs.forbidden
            logger.info("vnfid=null vduId==null,query all vnfcs.forbidden.");
            return null;
        }
        String result =  getResourceCenterProxy().retrieveVnfcs(vnfId, vduId);
        logger.debug("query vnfcs:{}", result);
        QueryResourcesResult<VNFCBaseInfo> vnfcsRslt = QueryResourcesResult
                .fromJson(result, VNFCBaseInfo.class);
        return vnfcsRslt != null ? vnfcsRslt.getData() : null;
    }

    public static String createVnfc(VNFCBaseInfo vnfc) {
        Gson gson = new Gson();
        String vnfcJsonInfo = gson.toJson(vnfc, VNFCBaseInfo.class);
        logger.debug("create vnfc request:{}", vnfcJsonInfo);
        String result =  getResourceCenterProxy().createVnfc(REQUEST_MSG_BODY_PREFIX
                + vnfcJsonInfo);
        logger.debug("create vnfc response:{}", result);
        CreateResourceResult object = CreateResourceResult.fromJson(result);
        return (object.getOperationResult() != null && object
                .getOperationResult().equals("SUCCESS")) ? object.getOid()
                : null;
    }

    public static boolean deleteVnfc(String vnfcId) {
        if (vnfcId == null)// vnfcId=null,delete all vnfcs.forbidden
        {
            logger.info("vnfcId=null,delete all vnfcs.forbidden.");
            return false;
        }
        String result =  getResourceCenterProxy().deleteVnfc(vnfcId);
        logger.debug("delete vnfc:{}", result);
        return result.contains("SUCCESS");
    }

    public static boolean deleteVnfcs(String vnfId, String vduId) {
        if (vnfId == null && vduId == null) {
            logger.info("vnfid=null,vduId==null,delete all vnfcs.forbidden.");
            return false;
        }
        String result =  getResourceCenterProxy().deleteVnfcs(vnfId, vduId);
        logger.debug("delete vnfcs:{}", result);
        return result.contains("SUCCESS");
    }

    public static boolean updateVnfc(String vnfcId, VNFCBaseInfo vnfc)
    {
        Gson gson = new Gson();
        String vnfcJsonInfo = gson.toJson(vnfc, VNFCBaseInfo.class);
        logger.debug("update vnfc request:{}", vnfcJsonInfo);
        String result =  getResourceCenterProxy().updateVnfc(vnfcId,
                REQUEST_MSG_BODY_PREFIX + vnfcJsonInfo);
        logger.debug("update vnfc response:{}", result);
        return result.contains("SUCCESS");
    }

    public static VDUBaseInfo[] retrieveVdus(String vduId) {
        if (vduId == null) {
            vduId = "";
        }
        String result =  getResourceCenterProxy().retrieveVdus(vduId);
        logger.debug("query vdu:{}", result);
        QueryResourcesResult<VDUBaseInfo> vdusRslt = QueryResourcesResult
                .fromJson(result, VDUBaseInfo.class);
        return vdusRslt != null ? vdusRslt.getData() : null;
    }

    public static VDUBaseInfo[] retrieveVdus(String vimId, String vnfId,
            String hostId) {
        if (vimId == null && vnfId == null && hostId == null)// query all
                                                                // vdus.forbidden.
        {
            logger.info("vnfid=null vimId==null hostId==null,query all vdus.forbidden.");
            return null;
        }
        String result =  getResourceCenterProxy().retrieveVdus(vimId, vnfId, hostId);
        logger.debug("query vdus:{}", result);
        QueryResourcesResult<VDUBaseInfo> vdusRslt = QueryResourcesResult
                .fromJson(result, VDUBaseInfo.class);
        return vdusRslt != null ? vdusRslt.getData() : null;
    }

    public static String createVdu(VDUBaseInfo vdu) {
        Gson gson = new Gson();
        String vduJsonInfo = gson.toJson(vdu, VDUBaseInfo.class);
        logger.debug("create vdu request:{}", vduJsonInfo);
        String result =  getResourceCenterProxy().createVdu(REQUEST_MSG_BODY_PREFIX
                + vduJsonInfo);
        logger.debug("create vdu response:{}", result);
        CreateResourceResult object = CreateResourceResult.fromJson(result);
        return (object.getOperationResult() != null && object
                .getOperationResult().equals("SUCCESS")) ? object.getOid()
                : null;
    }

    public static boolean updateVdu(String vduId, VDUBaseInfo vdu) {
        Gson gson = new Gson();
        String vduJsonInfo = gson.toJson(vdu, VDUBaseInfo.class);
        logger.debug("update vdu request:{}", vduJsonInfo);
        String result =  getResourceCenterProxy().updateVdu(vduId,
                REQUEST_MSG_BODY_PREFIX + vduJsonInfo);
        logger.debug("update vdu response:{}", result);
        return result.contains("SUCCESS");
    }

    public static boolean deleteVdu(String vduId) {
        if (vduId == null) {
            logger.info("vduId=null,delete all vdus.forbidden.");
            return false;
        }
        String result =  getResourceCenterProxy().deleteVdu(vduId);
        logger.debug("delete vdu:{}", result);
        return result.contains("SUCCESS");
    }

    public static boolean deleteVdus(String vimId, String vnfId, String hostId) {
        if (vnfId == null && vimId == null && hostId == null) {
            logger.info("vnfId=null vimId==null hostId==null,delete all vdus.forbidden.");
            return false;
        }
        String result =  getResourceCenterProxy().deleteVdus(vimId, vnfId, hostId);
        logger.debug("delete vdus:{}", result);
        return result.contains("SUCCESS");
    }

    public static VimBaseInfo[] retrieveVims(String vimId) {
        // null,get all
        if (vimId == null) {
            vimId = "";
        }
        String result =  getResourceCenterProxy().queryVims(vimId);
        logger.info("query vims:{}", result);
        QueryResourcesResult<VimBaseInfo> vimRslt = QueryResourcesResult
                .fromJson(result, VimBaseInfo.class);
        return vimRslt != null ? vimRslt.getData() : null;
    }

    public static VNFMBaseInfo[] retrieveVNFMs(String vnfmId) {
        // null,get all
        if (vnfmId == null) {
            vnfmId = "";
        }
        String result =  getResourceCenterProxy().retrieveVnfms(vnfmId);
        logger.info("query vnfms:{}", result);
        QueryResourcesResult<VNFMBaseInfo> vnfmRslt = QueryResourcesResult
                .fromJson(result, VNFMBaseInfo.class);
        return vnfmRslt != null ? vnfmRslt.getData() : null;
    }

    public static NSBaseInfo[] retrieveNss(String nsId) {
        // null,get all
        if (nsId == null) {
            nsId = "";
        }
        String result =  getResourceCenterProxy().retrieveNss(nsId);
        logger.debug("query nss:{}", result);
        QueryResourcesResult<NSBaseInfo> nsRslt = QueryResourcesResult
                .fromJson(result, NSBaseInfo.class);
        return nsRslt != null ? nsRslt.getData() : null;
    }

    public static String createNs(NSBaseInfo ns) {
        Gson gson = new Gson();
        String nsJsonInfo = gson.toJson(ns, NSBaseInfo.class);
        logger.debug("crate ns request:{}", nsJsonInfo);
        String result =  getResourceCenterProxy().createNs(REQUEST_MSG_BODY_PREFIX
                + nsJsonInfo);
        logger.debug("crate ns response:{}", result);
        CreateResourceResult object = CreateResourceResult.fromJson(result);
        if (object.getOperationResult() != null
                && object.getOperationResult().equals("SUCCESS")) {
            return object.getOid();
        }

        throw new WebApplicationException("create ns to database failed.");
    }

    public static boolean updateNs(String nsId, NSBaseInfo ns) {
        Gson gson = new Gson();
        String nsJsonInfo = gson.toJson(ns, NSBaseInfo.class);
        logger.debug("update ns request:{}", nsJsonInfo);
        String result =  getResourceCenterProxy().updateNs(nsId,
                REQUEST_MSG_BODY_PREFIX + nsJsonInfo);
        logger.debug("update ns response:{}", result);
        return result.contains("SUCCESS");
    }

    public static boolean deleteNs(String nsId) {
        if (nsId == null)// nsId=null,delete all nss.forbidden
        {
            logger.info("nsId=null,delete all nss.forbidden.");
            return false;
        }
        String result =  getResourceCenterProxy().deleteNs(nsId);
        logger.debug("delete ns response:{}", result);
        return result.contains("SUCCESS");
    }

    public static String createNetwork(NetworkBaseInfo network) {
        Gson gson = new Gson();
        String networkJson = gson.toJson(network, NetworkBaseInfo.class);
        logger.debug("crate network request:{}", networkJson);
        String result =  getResourceCenterProxy()
                .createNetwork(REQUEST_MSG_BODY_PREFIX + networkJson);
        logger.debug("crate network response:{}", result);
        CreateResourceResult object = CreateResourceResult.fromJson(result);
        if (object.getOperationResult() != null
                && object.getOperationResult().equals("SUCCESS")) {
            return object.getOid();
        }

        throw new WebApplicationException("create network to database failed.");
    }

    public static NetworkBaseInfo[] retrieveNetworks(String networkOid) {
        // null,get all
        if (networkOid == null) {
            networkOid = "";
        }
        String result =  getResourceCenterProxy().retrieveNetworks(networkOid);
        logger.debug("query networks:{}", result);
        QueryResourcesResult<NetworkBaseInfo> networkRslt = QueryResourcesResult
                .fromJson(result, NetworkBaseInfo.class);
        return networkRslt != null ? networkRslt.getData() : null;
    }

    public static NetworkBaseInfo[] retrieveNetworks(String vimId,
            String networkIdInVim, String instanceOid) {
        if (vimId == null && networkIdInVim == null && instanceOid == null) {
            logger.info("vimId=null networkIdInVim==null instanceId==null,query all networks.forbidden.");
            return null;
        }
        String result =  getResourceCenterProxy().retrieveNetworks(vimId,
                networkIdInVim, instanceOid);
        logger.debug("query networks:{}", result);
        QueryResourcesResult<NetworkBaseInfo> networksRslt = QueryResourcesResult
                .fromJson(result, NetworkBaseInfo.class);
        return networksRslt != null ? networksRslt.getData() : null;
    }

    public static boolean updateNetwork(String networkOid,
            NetworkBaseInfo network) {
        Gson gson = new Gson();
        String networkJson = gson.toJson(network, NetworkBaseInfo.class);
        logger.debug("update network request:{}", networkJson);
        String result =  getResourceCenterProxy().updateVnf(networkOid,
                REQUEST_MSG_BODY_PREFIX + networkJson);
        logger.debug("update network response:{}", result);
        return result.contains("SUCCESS");
    }

    public static boolean deleteNetwork(String networkOid) {
        if (networkOid == null)// networkOid=null,delete all networks.forbidden
        {
            logger.info("networkOid=null,delete all networks.forbidden.");
            return false;
        }
        String result =  getResourceCenterProxy().deleteNetwork(networkOid);
        logger.debug("delete network response:{}", result);
        return result.contains("SUCCESS");
    }

    public static String createSubnet(SubnetBaseInfo subnet) {
        Gson gson = new Gson();
        String subnetJson = gson.toJson(subnet, SubnetBaseInfo.class);
        logger.debug("crate subnet request:{}", subnetJson);
        String result =  getResourceCenterProxy()
                .createSubnet(REQUEST_MSG_BODY_PREFIX + subnetJson);
        logger.debug("crate subnet response:{}", result);
        CreateResourceResult object = CreateResourceResult.fromJson(result);
        if (object.getOperationResult() != null
                && object.getOperationResult().equals("SUCCESS")) {
            return object.getOid();
        }

        throw new WebApplicationException("create subnet to database failed.");
    }

    public static SubnetBaseInfo[] retrieveSubnets(String subnetOid) {
        // null,get all
        if (subnetOid == null) {
            subnetOid = "";
        }
        String result =  getResourceCenterProxy().retrieveSubnets(subnetOid);
        logger.debug("query sunnets:{}", result);
        QueryResourcesResult<SubnetBaseInfo> subnetRslt = QueryResourcesResult
                .fromJson(result, SubnetBaseInfo.class);
        return subnetRslt != null ? subnetRslt.getData() : null;
    }

    public static SubnetBaseInfo[] retrieveSubnets(String vimId,
            String networkOid, String subnetIdInVim) {
        if (vimId == null && networkOid == null && subnetIdInVim == null) {
            logger.info("vimId==null  networkOid==null  subnetId==null,query all subnets.forbidden.");
            return null;
        }
        String result =  getResourceCenterProxy().retrieveSubnets(vimId, networkOid,
                subnetIdInVim);
        logger.debug("query subnets:{}", result);
        QueryResourcesResult<SubnetBaseInfo> subnetsRslt = QueryResourcesResult
                .fromJson(result, SubnetBaseInfo.class);
        return subnetsRslt != null ? subnetsRslt.getData() : null;
    }

    public static boolean updateSubnet(String subnetOid, SubnetBaseInfo subnet) {
        Gson gson = new Gson();
        String subnetJson = gson.toJson(subnet, SubnetBaseInfo.class);
        logger.debug("update subnet request:{}", subnetJson);
        String result =  getResourceCenterProxy().updateVnf(subnetOid,
                REQUEST_MSG_BODY_PREFIX + subnetJson);
        logger.debug("update subnet response:{}", result);
        return result.contains("SUCCESS");
    }

    public static boolean deleteSubnet(String subnetOid) {
        if (subnetOid == null) {// vnfid=null,delete all vnfs.forbidden
            logger.info("subnetOid=null,delete all subnets.forbidden.");
            return false;
        }
        String result =  getResourceCenterProxy().deleteSubnet(subnetOid);
        logger.debug("delete subnet response:{}", result);
        return result.contains("SUCCESS");
    }

    public static boolean deleteSubnets(String vimId, String networkOid) {
        if (vimId == null && networkOid == null)// delete all .forbidden
        {
            logger.info("vimId=null networkOid==null,delete all subnets.forbidden.");
            return false;
        }
        String result =  getResourceCenterProxy().deleteSubnets(vimId, networkOid);
        logger.debug("delete subnets response:{}", result);
        return result.contains("SUCCESS");
    }

    public static HostBaseInfo[] retrieveHosts(String vimId, String hostName) {
        String result =  getResourceCenterProxy().retrieveHosts(vimId, hostName);
        logger.debug("query hosts by name:{},result:{}", hostName, result);
        QueryResourcesResult<HostBaseInfo> hostRslt = QueryResourcesResult
                .fromJson(result, HostBaseInfo.class);
        return hostRslt != null ? hostRslt.getData() : null;
    }

    public static String createConnectionPoint(CPBaseInfo cp)
    {
        Gson gson = new Gson();
        String cpJson = gson.toJson(cp, CPBaseInfo.class);
        logger.debug("crate cp request:{}", cpJson);
        String result =  getResourceCenterProxy().createConnectionPoint(REQUEST_MSG_BODY_PREFIX + cpJson);
        logger.debug("crate cp response:{}", result);
        CreateResourceResult object = CreateResourceResult.fromJson(result);
        if (object.getOperationResult() != null && object.getOperationResult().equals("SUCCESS"))
        {
            return object.getOid();
        }

        throw new WebApplicationException("create vnf to database failed.");
    }

    public static CPBaseInfo[] retrieveConnectionPoints(String cpOid)
    {
        // null,get all
        if (cpOid == null) {
            cpOid = "";
        }

        String result =  getResourceCenterProxy().retrieveConnectionPoints(cpOid);
        logger.debug("query cps:{}", result);
        QueryResourcesResult<CPBaseInfo> cpRslt = QueryResourcesResult.fromJson(result, CPBaseInfo.class);
        return cpRslt != null ? cpRslt.getData() : null;
    }


    public static CPBaseInfo[] retrieveConnectionPointsByInstanceId(String instanceId)
    {
        if (instanceId == null) {
            logger.info("instanceId==null,query all.forbidden.");
            return null;
        }
        String result =  getResourceCenterProxy().retrieveConnectionPointsByInstanceId(instanceId);
        logger.debug("query cps:{}", result);
        QueryResourcesResult<CPBaseInfo> cpRslt = QueryResourcesResult.fromJson(result, CPBaseInfo.class);
        return cpRslt != null ? cpRslt.getData() : null;
    }

    public static boolean updateConnectionPoint(String cpOid,CPBaseInfo cp)
    {
        Gson gson = new Gson();
        String cpJson = gson.toJson(cp, CPBaseInfo.class);
        logger.debug("update cp request:{}", cpJson);
        String result =  getResourceCenterProxy().updateConnectionPoint(cpOid, REQUEST_MSG_BODY_PREFIX +cpJson);
        logger.debug("update cp response:{}", result);
        return result.contains("SUCCESS");
    }

    public static boolean deleteConnectionPoint(String cpOid)
    {
        if (cpOid == null)// cpOid=null,delete all cps.forbidden
        {
            logger.info("cpOid=null,delete all cps.forbidden.");
            return false;
        }
        String result =  getResourceCenterProxy().deleteConnectionPoint(cpOid);
        logger.debug("delete cp response:{}", result);
        return result.contains("SUCCESS");
    }

    public static boolean deleteConnectionPointsByInstanceId(String instanceId)
    {
        if (instanceId == null)// instanceId=null
        {
            logger.info("instanceId=null,delete all cps.forbidden.");
            return false;
        }

        String result =  getResourceCenterProxy().deleteConnectionPointsByInstanceId(instanceId);
        logger.debug("delete cps response:{}", result);
        return result.contains("SUCCESS");
    }
}
