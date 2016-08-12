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
package org.openo.orchestrator.nfv.umc.drill.wrapper.handler.layermonitor;

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
 * @author 10188044
 * @date 2015-8-17
 *       <p/>
 *       The concrete handler that process the list request of the NS layer
 */
public class NSLayerMonitorHandler extends AbstractTopologyHandler {

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
     * query all the NS nodes of the system and the VNFs that not belong to any NS node
     */
    @Override
    public NodeInformation[] queryChildNodes(NodeInformation currentNodeDetail)
            throws TopologyException {
        // the result array that save NSs and VNFs
        NodeInformation[] nodeInfos = null;
        // temp array that save the NS nodes
        NodeInformation[] nsInfos = null;
        // temp array that save the VNF nodes
        NodeInformation[] vnfInfos = null;

        // get the id or root
        RootNode rootNode = (RootNode) currentNodeDetail;
        String rootId = rootNode.getId();

        /** start process the NS nodes of the system **/
        RestQueryListReturnMsg<NsData> nsRestResult = null;
        try {
            // query the NS nodes of the system
            nsRestResult = serviceProxy.queryNssOfConductor(rootId);
        } catch (Exception e) {
            this.handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(nsRestResult);
        NsData[] nss = nsRestResult.getData();
        if (ArrayUtils.isArrayNotEmpty(nss)) {
            nsInfos = new NodeInformation[nss.length];
            for (int i = 0; i < nss.length; i++) {
                nsInfos[i] = new NSInformation(nss[i]);
            }
        }
        /** end process the NS nodes of the system **/
        /** start process the VNFs that not belong to any NS node **/
        RestQueryListReturnMsg<VnfData> vnfRestResult = null;
        try {
            // query the VNFs that belong to the system
            vnfRestResult = serviceProxy.queryVnfsOfConductor(rootId);
        } catch (Exception e) {
            this.handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(vnfRestResult);
        VnfData[] vnfs = vnfRestResult.getData();
        if (ArrayUtils.isArrayNotEmpty(vnfs)) {
            vnfInfos = new NodeInformation[vnfs.length];
            for (int i = 0; i < vnfs.length; i++) {
                vnfInfos[i] = new VNFInformation(vnfs[i]);
            }
        }
        /** end process the VNFs that not belong to any NS node **/

        // combine the temp NS[] and VNF[]
        nodeInfos = ArrayUtils.addAll(nsInfos, vnfInfos);

        return nodeInfos;
    }

}
