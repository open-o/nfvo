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
package org.openo.orchestrator.nfv.umc.drill.wrapper.handler.drill;

import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.NodeInformation;
import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.VDUInformation;
import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.VNFCInformation;
import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.VNFInformation;
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.exception.TopologyException;
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.util.ArrayUtils;
import org.openo.orchestrator.nfv.umc.drill.wrapper.handler.AbstractTopologyHandler;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.IResourceServices;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.ResourceServicesStub;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VduData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VnfData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VnfcData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.response.RestQueryListReturnMsg;

/**
 *       The concrete handler that process the drill request of the VNFC
 */
public class VNFCDrillHandler extends AbstractTopologyHandler {

    // get the resource service proxy instance
    IResourceServices serviceProxy = ResourceServicesStub.getServiceProxy();

    /**
     * query VNFC node info
     */
    @Override
    public NodeInformation queryCurrentNode(String resourceid) throws TopologyException {
        VNFCInformation vnfcInfo = null;
        RestQueryListReturnMsg<VnfcData> restResult = null;
        try {
            restResult = serviceProxy.queryVnfc(resourceid);
        } catch (Exception e) {
            this.handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        VnfcData[] vnfcs = restResult.getData();
        // whether the current node is null
        if (ArrayUtils.isArrayNotEmpty(vnfcs)) {
            vnfcInfo = new VNFCInformation(vnfcs[0]);
            vnfcInfo.setAlarmCount(queryAlarmCount(vnfcInfo.getId()));
        } else {
            LOGGER.warn("the node dose not exist!VNFCid:" + resourceid);
            // if the id dose not match any node, throw exception
            throw new TopologyException(TopologyException.ERR_MONITOR_TOPOLOGY_NODENOTFOUND,
                    "the node dose not exist!");
        }
        return vnfcInfo;
    }

    /**
     * query the VNF this VNFC belongs to
     */
    @Override
    public NodeInformation[] queryParentNodes(NodeInformation currentNodeDetail)
            throws TopologyException {
        RestQueryListReturnMsg<VnfData> restResult = null;
        try {
            // VNFC stores the id of VNF,use id to query
            VNFCInformation vnfcInformation = (VNFCInformation) currentNodeDetail;
            String vnfId = vnfcInformation.getVnf_id();
            restResult = serviceProxy.queryVnf(vnfId);
        } catch (Exception e) {
            this.handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble VNFInformation[] according to the VnfData[]
        VNFInformation[] VNFInformations = assembleData(restResult, VnfData.class, VNFInformation.class);

        for(int i=0;i<VNFInformations.length;i++){
            VNFInformations[i].setAlarmCount(queryAlarmCount(VNFInformations[i].getId()));
        }
        return VNFInformations;
    }

    /**
     * query vdu this vnfc deployed on
     */
    @Override
    public NodeInformation[] queryChildNodes(NodeInformation currentNodeDetail)
            throws TopologyException {
        RestQueryListReturnMsg<VduData> restResult = null;
        try {
            // VNFC stores the id of VDU,use id to query
            VNFCInformation vnfcInformation = (VNFCInformation) currentNodeDetail;
            String vduId = vnfcInformation.getVdu_id();
            restResult = serviceProxy.queryVdu(vduId);
        } catch (Exception e) {
            this.handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble VDUInformation[] according to the VduData[]
        VDUInformation[] VDUInformations = assembleData(restResult, VduData.class, VDUInformation.class);

        for(int i=0;i<VDUInformations.length;i++){
            VDUInformations[i].setAlarmCount(queryAlarmCount(VDUInformations[i].getId()));
        }
        return VDUInformations;
    }
}
