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
package org.openo.orchestrator.nfv.umc.drill.wrapper.handler.layermonitor;

import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.NodeInformation;
import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.RootNode;
import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.VDUInformation;
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.exception.TopologyException;
import org.openo.orchestrator.nfv.umc.drill.wrapper.handler.AbstractTopologyHandler;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.IResourceServices;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.ResourceServicesStub;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VduData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.response.RestQueryListReturnMsg;

/**
 *       The concrete handler that process the list request of the VDU layer
 */
public class VDULayerMonitorHandler extends AbstractTopologyHandler {

    // get the resource service proxy instance
    IResourceServices serviceProxy = ResourceServicesStub.getServiceProxy();

    /**
     * the current node is system itself,so return the RootNode
     */
    @Override
    public NodeInformation queryCurrentNode(String resourceid) throws TopologyException {
        return new RootNode();
    }

    /**
     * the parent of the system is null
     */
    @Override
    public NodeInformation[] queryParentNodes(NodeInformation currentNodeDetail)
            throws TopologyException {
        return null;
    }

    /**
     * query all the VDU nodes of the system
     *
     */
    @Override
    public NodeInformation[] queryChildNodes(NodeInformation currentNodeDetail)
            throws TopologyException {
        RestQueryListReturnMsg<VduData> restResult = null;
        try {
            // query all the vdu list
            restResult = serviceProxy.queryAllVdus();
        } catch (Exception e) {
            this.handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        // assemble VDUInformation[] according to the VduData[]
        return assembleData(restResult, VduData.class, VDUInformation.class);
    }
}
