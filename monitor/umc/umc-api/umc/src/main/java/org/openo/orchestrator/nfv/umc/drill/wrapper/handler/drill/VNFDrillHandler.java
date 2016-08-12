/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.umc.drill.wrapper.handler.drill;

import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.NSInformation;
import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.NodeInformation;
import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.VNFCInformation;
import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.VNFInformation;
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.exception.TopologyException;
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.util.ArrayUtils;
import org.openo.orchestrator.nfv.umc.drill.wrapper.handler.AbstractTopologyHandler;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.IResourceServices;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.ResourceServicesStub;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.NsData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VnfData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VnfcData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.response.RestQueryListReturnMsg;

/**
 * @author 10188044
 * @date 2015-8-13
 *       <p/>
 *       The concrete handler that process the drill request of the VNF
 */
public class VNFDrillHandler extends AbstractTopologyHandler {

    // get the resource service proxy instance
    IResourceServices serviceProxy = ResourceServicesStub.getServiceProxy();

    /**
     * query the VNF node info
     */
    @Override
    public NodeInformation queryCurrentNode(String resourceid) throws TopologyException {
        VNFInformation vnfInfo = null;
        RestQueryListReturnMsg<VnfData> restResult = null;
        try {
            restResult = serviceProxy.queryVnf(resourceid);
        } catch (Exception e) {
            this.handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        VnfData[] vnfs = restResult.getData();
        // whether the current node is null
        if (ArrayUtils.isArrayNotEmpty(vnfs)) {
            vnfInfo = new VNFInformation(vnfs[0]);
            vnfInfo.setAlarmCount(queryAlarmCount(vnfInfo.getId()));
        } else {
            LOGGER.warn("the node dose not exist!VNFid:" + resourceid);
            // if the id dose not match any node, throw exception
            throw new TopologyException(TopologyException.ERR_MONITOR_TOPOLOGY_NODENOTFOUND,
                    "the node dose not exist!");
        }
        return vnfInfo;
    }

    /**
     * query the NS info this VNF belongs to
     */
    @Override
    public NodeInformation[] queryParentNodes(NodeInformation currentNodeDetail)
            throws TopologyException {
        RestQueryListReturnMsg<NsData> restResult = null;
        try {
            VNFInformation vnfInformation = (VNFInformation) currentNodeDetail;
            String vnfId = vnfInformation.getId();
            restResult = serviceProxy.queryNssOfVNF(vnfId);
        } catch (Exception e) {
            this.handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble NSInformation[] according to the NsData[]
        NSInformation[] NSInformations = assembleData(restResult, NsData.class, NSInformation.class);

        for(int i=0;i<NSInformations.length;i++){
            NSInformations[i].setAlarmCount(queryAlarmCount(NSInformations[i].getId()));
        }
        return NSInformations;
    }

    /**
     * query the child VNFC list
     */
    @Override
    public NodeInformation[] queryChildNodes(NodeInformation currentNodeDetail)
            throws TopologyException {
        RestQueryListReturnMsg<VnfcData> restResult = null;
        try {
            VNFInformation vnfInformation = (VNFInformation) currentNodeDetail;
            String vnfId = vnfInformation.getId();
            restResult = serviceProxy.queryVnfcsOfVNF(vnfId);
        } catch (Exception e) {
            this.handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble VNFCInformation[] according to the VnfcData[]
        VNFCInformation[] VNFCInformations =assembleData(restResult, VnfcData.class, VNFCInformation.class);

        for(int i=0;i<VNFCInformations.length;i++){
            VNFCInformations[i].setAlarmCount(queryAlarmCount(VNFCInformations[i].getId()));
        }
        return VNFCInformations;
    }
}
