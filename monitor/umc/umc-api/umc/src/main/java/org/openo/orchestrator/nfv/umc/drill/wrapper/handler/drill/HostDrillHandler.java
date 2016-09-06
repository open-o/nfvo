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
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.exception.TopologyException;
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.util.ArrayUtils;
import org.openo.orchestrator.nfv.umc.drill.wrapper.handler.AbstractTopologyHandler;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.IResourceServices;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.ResourceServicesStub;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.HostData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.VduData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.response.RestQueryListReturnMsg;

/**
 *       The concrete handler that process the drill request of the HOST
 */
public class HostDrillHandler extends AbstractTopologyHandler {

    // get the resource service proxy instance
    IResourceServices serviceProxy = ResourceServicesStub.getServiceProxy();

    /**
     * query the Host info
     */
    @Override
    public NodeInformation queryCurrentNode(String resourceid) throws TopologyException {
        HostInformation hostinfo = null;
        RestQueryListReturnMsg<HostData> restResult = null;
        try {
            restResult = serviceProxy.queryHost(resourceid);
        } catch (Exception e) {
            this.handleRestserviceException(e);
        }
        // check whether calling resource service succeed,throw exception directly if failed
        checkRestserviceResult(restResult);
        HostData[] hosts = restResult.getData();
        // whether the current node is null
        if (ArrayUtils.isArrayNotEmpty(hosts)) {
            hostinfo = new HostInformation(hosts[0]);
            hostinfo.setAlarmCount(queryAlarmCount(hostinfo.getId()));
        } else {
            LOGGER.warn("the node dose not exist!Hostid:" + resourceid);
            // if the id dose not match any node, throw exception
            throw new TopologyException(TopologyException.ERR_MONITOR_TOPOLOGY_NODENOTFOUND,
                    "the node dose not exist!");
        }
        return hostinfo;
    }

    /**
     * query all the VDU nodes deployed on the host
     */
    @Override
    public NodeInformation[] queryParentNodes(NodeInformation currentNodeDetail)
            throws TopologyException {
        RestQueryListReturnMsg<VduData> restResult = null;
        try {
            HostInformation hostInformation = (HostInformation) currentNodeDetail;
            String hostId = hostInformation.getId();
            // query all the VduData using the hostId
            restResult = serviceProxy.queryVdusOfHost(hostId);
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

    /**
     * Host node is the leaf,dose not have child,return null directly
     */
    @Override
    public NodeInformation[] queryChildNodes(NodeInformation currentNodeDetail)
            throws TopologyException {
        return null;
    }

}
