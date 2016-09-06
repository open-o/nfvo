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

import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.NSInformation;
import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.NodeInformation;
import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.RootNode;
import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.VNFInformation;
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.exception.TopologyException;
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.util.ArrayUtils;
import org.openo.orchestrator.nfv.umc.drill.wrapper.handler.AbstractTopologyHandler;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.IResourceServices;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.ResourceServicesStub;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.NsData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VnfData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.response.RestQueryListReturnMsg;

/**
 *       The concrete handler that process the drill request of the NS(Network Service)
 */
public class NSDrillHandler extends AbstractTopologyHandler {

    // get the resource service proxy instance
    IResourceServices serviceProxy = ResourceServicesStub.getServiceProxy();

    /**
     * query the NS node info
     */
    @Override
    public NodeInformation queryCurrentNode(String resourceid) throws TopologyException {
        NSInformation nsInfo = null;
        RestQueryListReturnMsg<NsData> restResult = null;
        try {
            restResult = serviceProxy.queryNs(resourceid);
        } catch (Exception e) {
            this.handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        NsData[] nss = restResult.getData();
        // whether the current node is null
        if (ArrayUtils.isArrayNotEmpty(nss)) {
            nsInfo = new NSInformation(nss[0]);
        } else {
            LOGGER.warn("the node dose not exist!Hostid:" + resourceid);
            // if the id dose not match any node, throw exception
            throw new TopologyException(TopologyException.ERR_MONITOR_TOPOLOGY_NODENOTFOUND,
                    "the node dose not exist!");
        }
        return nsInfo;
    }

    /**
     * NS node is the tree root,return the RootNode
     */
    @Override
    public NodeInformation[] queryParentNodes(NodeInformation currentNodeDetail)
            throws TopologyException {
        NodeInformation[] parents = {new RootNode()};
        return parents;
    }

    /**
     * query the VNF list included in this NS node
     */
    @Override
    public NodeInformation[] queryChildNodes(NodeInformation currentNodeDetail)
            throws TopologyException {
        RestQueryListReturnMsg<VnfData> restResult = null;
        try {
            NSInformation nsInformation = (NSInformation) currentNodeDetail;
            String nsId = nsInformation.getId();
            restResult = serviceProxy.queryVnfsOfNS(nsId);
        } catch (Exception e) {
            this.handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble VNFInformation[] according to the VnfData[]
        return assembleData(restResult, VnfData.class, VNFInformation.class);
    }
}
