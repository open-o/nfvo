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
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.wrapper.roc;

import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.NFVType;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.ApplicationInstanceBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.CPBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.HostBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.NSBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.NetworkBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.SubnetBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.VDUBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.VNFBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.VNFCBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.VNFMBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.VimBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.roc.RocServiceConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @ClassName: RocWrapper
* @Description: access the persistent data stored in ROC module or in local file.
* @author huangleibo10186364
* @date 2016-02-15 16:21:45
*
*/
public class RocWrapper {
    private static final Logger logger = LoggerFactory
            .getLogger(RocWrapper.class);


    // /////////////////ALL RESOURCES BEGIN//////////////
    public static ApplicationInstanceBaseInfo getAppInstanceBaseInfo() {
        return RocServiceConsumer.retrieveAllAppInstance();
    }
    // /////////////////ALL RESOURCES END//////////////
    // /////////////////VNF BEGIN//////////////
    public static VNFBaseInfo[] getVnfResources() {
        VNFBaseInfo[] vnfs = RocServiceConsumer.retrieveVnfs("");
        logger.info("retrieve {} vnf resources from database.", vnfs.length);
        return vnfs;
    }

    public static VNFBaseInfo[] getVnfResources(String nsId)  {
        VNFBaseInfo[] vnfs = RocServiceConsumer.retrieveVnfs(null,
                nsId, null);
        logger.info("retrieve {} vnf resources from database by nsid:{}.", vnfs.length,nsId);
        return vnfs;
    }

    public static VNFBaseInfo getVnfResource(String instanceId) {
        if (instanceId == null || instanceId.length() == 0) {
            logger.warn("the instanceid:{} error.", instanceId);
            return null;
        }

        VNFBaseInfo[] vnfs = RocServiceConsumer.retrieveVnfs(instanceId);
        if (vnfs == null || vnfs.length == 0 || vnfs[0] == null) {
            logger.warn("the instance:{} is not exist.", instanceId);
            return null;
        }

        return vnfs[0];
    }

    public static String addVnf(VNFBaseInfo baseInfo)  {
        logger.debug("begin add vnf instance to database.");
        return RocServiceConsumer.createVnf(baseInfo);
    }

    public static boolean updateVnfStatus(String vnfId, String vnfStatus, String vnfOperating) {
        VNFBaseInfo vnf = getVnfResource(vnfId);
        if (vnf == null) {
            logger.warn("vnf resource does not exist .instanceId:{}", vnfId);
            return false;
        }

        VNFBaseInfo modifyvnf = new VNFBaseInfo();
        if (vnfStatus != null && !vnfStatus.equals("")) {
            modifyvnf.setStatus(vnfStatus);
        }

        if (vnfOperating != null) {
            modifyvnf.setCustomPara(CustomParamTool.assembleOperatingCustomParam(vnfOperating,
                    vnf.getCustomPara()));
        }

        return RocServiceConsumer.updateVnf(vnfId, modifyvnf);
    }

    public static boolean updateVnfVimId(String vnfId, String vimId)
    {
        if(vimId ==null || vimId.length()==0 )
        {
            logger.warn("vim id is null.");
            return false;
        }

        VNFBaseInfo vnf = getVnfResource(vnfId);
        if (vnf == null) {
            logger.warn("vnf resource does not exist .instanceId:{}", vnfId);
            return false;
        }

        VNFBaseInfo modifyvnf = new VNFBaseInfo();
        modifyvnf.setVimId(vimId);

        return RocServiceConsumer.updateVnf(vnfId, modifyvnf);
    }

    public static boolean updateVnf(VNFBaseInfo vnf)  {
        logger.debug("update vnf instance in database.");
        return RocServiceConsumer.updateVnf(vnf.getOid(), vnf);
    }

    public static boolean delVnf(String instanceId)  {
        logger.debug("delete vnf instance in database.");
        return RocServiceConsumer.deleteVnf(instanceId);
    }

    // //////////////VNF END//////////////////////
    // //////////////VDU BEGIN////////////////////
    public static VDUBaseInfo[] getVduResources(String vnfId)
    {
        logger.debug("get {}'s all vdu from database.", vnfId);
        VDUBaseInfo[] vdus = RocServiceConsumer.retrieveVdus(null,
                vnfId, null);
        logger.debug("finish get all vdu in database normal.");
        return vdus;
    }

    public static VDUBaseInfo getVduResource(String vduId)  {
        VDUBaseInfo[] vdus = RocServiceConsumer.retrieveVdus(vduId);
        if (vdus == null || vdus.length == 0 || vdus[0] == null) {
            logger.warn("the instance:{} is not exist.", vduId);
            return null;
        }
        return vdus[0];
    }

    public static String addVdu(VDUBaseInfo vduBaseInfo)  {
        logger.debug("add vdu to database.");
        return RocServiceConsumer.createVdu(vduBaseInfo);
    }

    public static boolean updateVdu(String vduId, VDUBaseInfo vdu)
    {
        return RocServiceConsumer.updateVdu(vduId, vdu);
    }

    public static boolean delVdus(String vnfId)  {
        logger.debug("del {}'s all vdu form database.", vnfId);
        return RocServiceConsumer.deleteVdus(null, vnfId, null);
    }

    public static boolean delVdu(String vnfId, String vduId)  {
        logger.debug("del vdu:{} form database.", vduId);
        return RocServiceConsumer.deleteVdu(vduId);
    }

    // //////////////VDU END//////////////////////
    // //////////////VNFC BEGIN////////////////////
    public static String addVnfc(VNFCBaseInfo vnfcBaseInfo)  {
        logger.debug("add vnfc to database.");
        return RocServiceConsumer.createVnfc(vnfcBaseInfo);
    }

    public static boolean updateVnfc(String vnfcId, VNFCBaseInfo vnfc)
    {
        return RocServiceConsumer.updateVnfc(vnfcId, vnfc);
    }

    public static boolean delVnfcs(String vnfId)  {
        logger.debug("del {}'s all vnfc form database.", vnfId);
        return RocServiceConsumer.deleteVnfcs(vnfId, null);
    }

    public static boolean delVnfc(String vnfId, String vnfcId)  {
        logger.debug("del vnfc:{} form database.", vnfcId);
        return RocServiceConsumer.deleteVnfc(vnfcId);
    }
    // //////////////VNFC END//////////////////////
    // //////////////VIM BEGIN////////////////////
    public static VimBaseInfo getVimResource(String vimId)  {
        logger.debug("begin retrieve vim:{} from database.", vimId);
        VimBaseInfo[] vims = RocServiceConsumer.retrieveVims(vimId);
        if (vims == null || vims.length == 0 || vims[0] == null) {
            logger.warn("the vim:{} is not exist.", vimId);
            return null;
        }
        logger.info("retrieve vim:{} from database.", vims[0].getName());
        logger.debug("finish retrieve vim from database normal.");
        return vims[0];
    }
    // //////////////VIM END//////////////////////
    // //////////////VNFM BEGIN////////////////////
    public static VNFMBaseInfo getVnfmResource(String vnfmId) {
        logger.debug("begin retrieve vnfm:{} from database.", vnfmId);
        VNFMBaseInfo[] vnfms = RocServiceConsumer.retrieveVNFMs(vnfmId);
        if (vnfms == null || vnfms.length == 0 || vnfms[0] == null) {
            logger.warn("the vnfm:{} is not exist.", vnfmId);
            return null;
        }
        logger.debug("finish retrieve vnfm from database normal.");
        return vnfms[0];
    }

    public static VNFMBaseInfo[] getVnfmResources() {
        logger.debug("begin retrieve vnfms from database.");
        VNFMBaseInfo[] vnfms = RocServiceConsumer.retrieveVNFMs("");
        if (vnfms == null || vnfms.length == 0 ) {
            logger.warn("vnfms does not exist.");
            return null;
        }
        logger.debug("finish retrieve vnfms from database normal.");
        return vnfms;
    }
    // //////////////VNFM END//////////////////////
    // //////////////NS BEGIN////////////////////
    public static NSBaseInfo[] getNsResources() {
        return RocServiceConsumer.retrieveNss("");
    }

    public static NSBaseInfo getNsResource(String instanceId)  {
        logger.debug("begin retrieve ns instance:{} from database.", instanceId);

        if(instanceId==null || instanceId.length()==0)
        {
            logger.warn("the instanceid error:{}", instanceId);
            return null;
        }
        NSBaseInfo[] nss = RocServiceConsumer.retrieveNss(instanceId);
        if (nss == null || nss.length == 0 || nss[0] == null) {
            logger.warn("the instance:{} is not exist.", instanceId);
            return null;
        }
        NSBaseInfo baseinfo = nss[0];
        logger.info("retrieve  ns instance:{} from database.",
                baseinfo.getName());
        logger.debug("finish retrieve ns instances from database normal.");
        return baseinfo;
    }

    public static String addNS(NSBaseInfo baseInfo)  {
        logger.debug("begin add vnf instance to database.");
        return RocServiceConsumer.createNs(baseInfo);
    }

    public static boolean updateNsStatus(String nsId, String nsStatus, String nsOperating) {
        NSBaseInfo ns = getNsResource(nsId);
        if (ns == null) {
            logger.warn("ns resource does not exist .instanceId:{}", nsId);
            return false;
        }

        if (nsStatus != null && !nsStatus.equals("")) {
            ns.setStatus(nsStatus);
        }

        if (nsOperating != null) {
            ns.setCustomPara(CustomParamTool.assembleOperatingCustomParam(nsOperating,
                    ns.getCustomPara()));
        }

        return RocServiceConsumer.updateNs(nsId, ns);
    }

    public static boolean updateNs(NSBaseInfo ns)  {
        return RocServiceConsumer.updateNs(ns.getOid(), ns);
    }

    public static boolean delNs(String instanceId)  {
        return RocServiceConsumer.deleteNs(instanceId);
    }
    // //////////////NS END//////////////////////
    // //////////////HOST BEGIN////////////////////
    public static HostBaseInfo getHostByName(String vimId, String hostName) {
        logger.debug("begin retrieve host instance by name:{} from database.", hostName);
        HostBaseInfo[] hosts = RocServiceConsumer.retrieveHosts(vimId, hostName);
        if (hosts == null || hosts.length == 0 || hosts[0] == null) {
            logger.warn("the instance:{} is not exist.", hostName);
            return new HostBaseInfo();
        }
        HostBaseInfo baseinfo = hosts[0];
        logger.info("retrieve  host instance:{} from database.", baseinfo.getName());
        logger.debug("finish retrieve host instances from database normal.");
        return baseinfo;
    }
    // //////////////HOST END//////////////////////
    // //////////////NETWORK BEGIN////////////////////
    public static NetworkBaseInfo getNetworkResource(String networkId) {
        NetworkBaseInfo[] nerworks = RocServiceConsumer.retrieveNetworks(networkId);
        if (nerworks == null || nerworks.length == 0 || nerworks[0] == null) {
            logger.warn("the instance:{} is not exist.", networkId);
            return null;
        }
        return nerworks[0];
    }

    public static NetworkBaseInfo[] getNetworkResourcesByInstanceOid(String instanceOid) {
        logger.debug("get {}'s all network from database.", instanceOid);
        return RocServiceConsumer.retrieveNetworks(null, null, instanceOid);
    }

    public static String addNetwork(NetworkBaseInfo networkBaseInfo) {
        logger.debug("add network to database.");
        return RocServiceConsumer.createNetwork(networkBaseInfo);
    }

    public static boolean updateNetwork(String networkId, NetworkBaseInfo network) {
        logger.debug("update network:{} to database.", networkId);
        return RocServiceConsumer.updateNetwork(networkId, network);
    }

    public static boolean delNetworksByInstanceOid(String instanceOid) {
        NetworkBaseInfo[] networks = getNetworkResourcesByInstanceOid(instanceOid);
        logger.debug("delete {}'s all network from database.", instanceOid);
        for (int i = 0; networks != null && i < networks.length; i++) {
            if (networks[i].getOid() != null && networks[i].getOid().length() != 0) {
                delSubnets(null, networks[i].getOid());
                delNetwork(networks[i].getOid());
            }
        }
        logger.debug("finish delete  networks in database normal.");
        return true;
    }

    public static boolean delNetwork(String networkId) {
        logger.debug("del network:{} form database.", networkId);
        return RocServiceConsumer.deleteNetwork(networkId);
    }
    // //////////////NETWORK END//////////////////////
    // //////////////SUBNET BEGIN////////////////////
    public static SubnetBaseInfo getSubnetResource(String subnetId) {
        SubnetBaseInfo[] subnets = RocServiceConsumer.retrieveSubnets(subnetId);
        if (subnets == null || subnets.length == 0 || subnets[0] == null) {
            logger.warn("the instance:{} is not exist.", subnetId);
            return null;
        }
        return subnets[0];
    }

    public static SubnetBaseInfo[] getSubnetResourcesByNetworkOid(String networkOid) {
        logger.debug("get {}'s all subnet from database.", networkOid);
        return RocServiceConsumer.retrieveSubnets(null, networkOid, null);
    }

    public static String addSubnet(SubnetBaseInfo subnetInfo)  {
        logger.debug("add subnet to database.");
        return RocServiceConsumer.createSubnet(subnetInfo);
    }

    public static boolean updateSubnet(String subnetId, SubnetBaseInfo subnet) {
        logger.debug("update subnet:{} to database.", subnetId);
        return RocServiceConsumer.updateSubnet(subnetId, subnet);
    }

    public static boolean delSubnets(String vimId, String networkOid)
    {
        return RocServiceConsumer.deleteSubnets(null, networkOid);
    }

    public static boolean delSubnet(String subnetId) {
        logger.debug("del subnet:{} form database.", subnetId);
        return RocServiceConsumer.deleteSubnet(subnetId);
    }
    // //////////////SUBNET END//////////////////////
    // //////////////CONNECTION POINT BEGIN////////////////////
    public static CPBaseInfo getCPResource(String cpId) {
        CPBaseInfo[] cps = RocServiceConsumer.retrieveConnectionPoints(cpId);
        if (cps == null || cps.length == 0 || cps[0] == null) {
            logger.warn("the instance:{} is not exist.", cpId);
            return null;
        }
        return cps[0];
    }

    public static CPBaseInfo[] getCPResourcesByInstanceOid(String instanceOid) {
        logger.debug("get {}'s all cp from database.", instanceOid);
        CPBaseInfo[] cps = RocServiceConsumer.retrieveConnectionPointsByInstanceId(instanceOid);
        logger.debug("finish get all cp in database normal.");
        return cps;
    }

    public static String addCP(CPBaseInfo cp) {
        return RocServiceConsumer.createConnectionPoint(cp);
    }

    public static boolean updateCP(String cpId, CPBaseInfo cp) {
        return RocServiceConsumer.updateConnectionPoint(cpId, cp);
    }

    public static boolean deleteCP(String cpId) {
        return RocServiceConsumer.deleteConnectionPoint(cpId);
    }

    public static boolean delCPsByInstanceOid(String instanceOid) {
        return RocServiceConsumer.deleteConnectionPointsByInstanceId(instanceOid);
    }
    // //////////////CONNECTION POINT END//////////////////////
    // //////////////EXTEND BEGIN////////////////////
    public static boolean delVnfNodeInstances(String vnfId) {
        logger.debug("begin del {}'s all node form database.", vnfId);
        delCPsByInstanceOid(vnfId);
        delVnfcs(vnfId);
        delVdus(vnfId);
        delNetworksByInstanceOid(vnfId);
        logger.debug("finish del all node form database normal.");
        return true;
    }

    public static boolean delNsNodeInstances(String nsId)  {
        VNFBaseInfo[] vnfs = getVnfResources(nsId);
        for (int i = 0; vnfs != null && i < vnfs.length; i++) {
            if (vnfs[i] != null && vnfs[i].getOid() != null) {
                delVnfNodeInstances(vnfs[i].getOid());
                delVnf(vnfs[i].getOid());
            }
        }
        delCPsByInstanceOid(nsId);
        delNetworksByInstanceOid(nsId);

        return true;
    }

    public static String getTamplateId(String instanceId, String nfvType) {
        if (nfvType == null || nfvType.equals("")) {
            return null;
        }

        if (nfvType.equals(NFVType.NS_TYPE)) {
            NSBaseInfo ns = getNsResource(instanceId);
            if (ns == null) {
                logger.warn("ns resource does not exist.instanceId:{}", instanceId);
                return null;
            }

            return ns.getNsd();
        }

        if (nfvType.equals(NFVType.VNF_TYPE)) {
            VNFBaseInfo vnf = getVnfResource(instanceId);
            if (vnf == null) {
                logger.warn("vnf resource does not exist.instanceId:{}", instanceId);
                return null;
            }
            return vnf.getVnfd();
        }

        return null;
    }

    public static String getTamplateIdByVnfId(String vnfId) {
        VNFBaseInfo vnfIns = getVnfResource(vnfId);
        if (vnfIns == null) {
            logger.warn("vnf resource does not exist.instanceId:{}", vnfId);
            return null;
        }

        return vnfIns.getVnfd();
    }

    public static String getNetworkNameBySubnetOid(String subnetOid) {
        if (subnetOid != null && subnetOid.length() != 0) {
            SubnetBaseInfo subnet = getSubnetResource(subnetOid);
            if (subnet != null && subnet.getNetworkId() != null
                    && subnet.getNetworkId().length() != 0) {
                NetworkBaseInfo network = getNetworkResource(subnet.getNetworkId());
                return network.getName();
            }
        }
        return "";
    }

    public static String getVnfmUrlByVnfmOid(String vnfmOid)
    {
        VNFMBaseInfo vnfmBaseInfo = getVnfmResource(vnfmOid);
        return vnfmBaseInfo!=null ? vnfmBaseInfo.getUrl() : null;
    }

    public static String getVnfmUrlByVnfInstanceOid(String vnfInstanceOid)
    {
        VNFBaseInfo vnfbaseinfo=getVnfResource(vnfInstanceOid);
        if(vnfbaseinfo == null || vnfbaseinfo.getVnfmId() == null || vnfbaseinfo.getVnfmId().length() == 0)
        {
            return null;
        }

        VNFMBaseInfo vnfmBaseInfo = getVnfmResource(vnfbaseinfo.getVnfmId());

        return vnfmBaseInfo!=null ? vnfmBaseInfo.getUrl() : null;
    }
    // //////////////EXTEND END//////////////////////
}
