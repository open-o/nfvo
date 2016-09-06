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

import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.HostInformation;
import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.NodeInformation;
import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.VDUInformation;
import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.VNFCInformation;
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.exception.TopologyException;
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.util.ArrayUtils;
import org.openo.orchestrator.nfv.umc.drill.wrapper.handler.AbstractTopologyHandler;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.IResourceServices;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.ResourceServicesStub;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.HostData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VduData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VnfcData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.response.RestQueryListReturnMsg;

/**
 *       The concrete handler that process the drill request of the VDU
 */
public class VDUDrillHandler extends AbstractTopologyHandler {

    // get the resource service proxy instance
    IResourceServices serviceProxy = ResourceServicesStub.getServiceProxy();

    /**
     * query the VDU node info
     */
    @Override
    public NodeInformation queryCurrentNode(String resourceid) throws TopologyException {
        VDUInformation vduinfo = null;
        RestQueryListReturnMsg<VduData> restResult = null;
        try {
            restResult = serviceProxy.queryVdu(resourceid);
        } catch (Exception e) {
            this.handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        VduData[] vdus = restResult.getData();
        // whether the current node is null
        if (ArrayUtils.isArrayNotEmpty(vdus)) {
            vduinfo = new VDUInformation(vdus[0]);
            vduinfo.setAlarmCount(queryAlarmCount(vduinfo.getId()));
        } else {
            LOGGER.warn("the node dose not exist!VDUid:" + resourceid);
            // if the id dose not match any node, throw exception
            throw new TopologyException(TopologyException.ERR_MONITOR_TOPOLOGY_NODENOTFOUND,
                    "the node dose not exist!");
        }
        return vduinfo;
    }

    /**
     * query VNFC list deployed on this VDU
     */
    @Override
    public NodeInformation[] queryParentNodes(NodeInformation currentNodeDetail)
            throws TopologyException {
        RestQueryListReturnMsg<VnfcData> restResult = null;
        try {
            VDUInformation vduInformation = (VDUInformation) currentNodeDetail;
            String vduId = vduInformation.getId();
            restResult = serviceProxy.queryVnfcsOfVDU(vduId);
        } catch (Exception e) {
            this.handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble VNFCInformation[] according to the VnfcData[]
        VNFCInformation[] VNFCInformations = assembleData(restResult, VnfcData.class, VNFCInformation.class);

        for(int i=0;i<VNFCInformations.length;i++){
            VNFCInformations[i].setAlarmCount(queryAlarmCount(VNFCInformations[i].getId()));
        }
        return VNFCInformations;

    }

    /**
     * query the host this VDU hosted
     */
    @Override
    public NodeInformation[] queryChildNodes(NodeInformation currentNodeDetail)
            throws TopologyException {
        RestQueryListReturnMsg<HostData> restResult = null;
        try {
            VDUInformation vduInformation = (VDUInformation) currentNodeDetail;
            String hostId = vduInformation.getHost_id();
            restResult = serviceProxy.queryHost(hostId);
        } catch (Exception e) {
            this.handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble HostInformation[] according to the HostData[]
        HostInformation[] HostInformations = assembleData(restResult, HostData.class, HostInformation.class);

        for(int i=0;i<HostInformations.length;i++){
            HostInformations[i].setAlarmCount(queryAlarmCount(HostInformations[i].getId()));
        }
        return HostInformations;
    }


}
