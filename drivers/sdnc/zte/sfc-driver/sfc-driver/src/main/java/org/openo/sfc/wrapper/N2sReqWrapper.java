/**
 * Copyright 2016 [ZTE] and others.
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

package org.openo.sfc.wrapper;

import org.openo.sfc.entity.ChainParameter;
import org.openo.sfc.entity.FlowClassfierReq;
import org.openo.sfc.entity.FlowClassfierReq4N;
import org.openo.sfc.entity.FlowClassifierReq4S;
import org.openo.sfc.entity.PortChainReq;
import org.openo.sfc.entity.PortChainReq4N;
import org.openo.sfc.entity.PortChainReq4S;
import org.openo.sfc.entity.PortPairGroupReq;
import org.openo.sfc.entity.PortPairGroupReq4N;
import org.openo.sfc.entity.PortPairGroupReq4S;
import org.openo.sfc.entity.portpair.PortPairReq;
import org.openo.sfc.entity.portpair.PortPairReq4N;
import org.openo.sfc.entity.portpair.PortPairReq4S;
import org.openo.sfc.entity.portpair.ServiceFunctionParameter;
import org.openo.sfc.utils.SfcDriverUtil;

import java.util.ArrayList;


public class N2sReqWrapper {
    public static PortPairReq4S convertPortPair(PortPairReq4N portPairReq4N) {
        PortPairReq portPairReq = new PortPairReq();
        portPairReq.setIngress(portPairReq4N.getIngress());
        portPairReq.setEgress(portPairReq4N.getEgress());
        ArrayList<ServiceFunctionParameter> serviceFunctionParameters = new ArrayList<ServiceFunctionParameter>();
        serviceFunctionParameters.addAll(SfcDriverUtil.generateSfParams(portPairReq4N.getSfParam()));
        portPairReq.setServiceFunctionParameters(serviceFunctionParameters);
        portPairReq.setUuid(SfcDriverUtil.generateUuid());
        ArrayList<PortPairReq> portPairList = new ArrayList<PortPairReq>();
        portPairList.add(portPairReq);
        PortPairReq4S portPairReq4S = new PortPairReq4S();
        portPairReq4S.setPortPair(portPairList);
        return portPairReq4S;
    }

    public static PortPairGroupReq4S convertPortPairGroup(PortPairGroupReq4N ppg4N) {
        PortPairGroupReq ppg4S = new PortPairGroupReq();
        ppg4S.setPortPairs(ppg4N.getPortPairs());
        ppg4S.setUuid(SfcDriverUtil.generateUuid());
        PortPairGroupReq4S portPairGroupReq4S = new PortPairGroupReq4S();
        ArrayList<PortPairGroupReq> portPairGroupReqs = new ArrayList<PortPairGroupReq>();
        portPairGroupReqs.add(ppg4S);
        portPairGroupReq4S.setPortPairGroupReqs(portPairGroupReqs);

        return portPairGroupReq4S;
    }

    public static FlowClassifierReq4S convertFlowClassfier(FlowClassfierReq4N flowClassfierReq4N) {
        ArrayList<FlowClassfierReq> flowClassifiers = new ArrayList();
        FlowClassfierReq flowClassfierReq = new FlowClassfierReq();
        flowClassfierReq.setProtocol(flowClassfierReq4N.getIp_proto());
//        flowClassfierReq.setSourcePortRangeMin(Integer.parseInt(flowClassfierReq4N.
//                getSource_port_range().split(",")[0]));
//        flowClassfierReq.setSourcePortRangeMax(Integer.parseInt(flowClassfierReq4N.
//                getSource_port_range().split(",")[1]));
//        flowClassfierReq.setDestinationPortRangeMin(Integer.parseInt(
//                flowClassfierReq4N.getDest_port_range().split(",")[0]));
//        flowClassfierReq.setDestinationPortRangeMax(Integer.parseInt(
//                flowClassfierReq4N.getDest_port_range().split(",")[1]));
        flowClassfierReq.setDestinationIpRange(flowClassfierReq4N.getDest_ip_range());
        flowClassfierReq.setSrcIpRange(flowClassfierReq4N.getSource_ip_range());
        flowClassfierReq.setIpDscp(flowClassfierReq4N.getDscp());
        flowClassfierReq.setUuid(SfcDriverUtil.generateUuid());
        flowClassifiers.add(flowClassfierReq);
        FlowClassifierReq4S flowClassifierReq4S = new FlowClassifierReq4S();
        flowClassifierReq4S.setSfcFlowClassifier(flowClassifiers);

        return flowClassifierReq4S;
    }

    public static PortChainReq4S converPortChain(PortChainReq4N portChainReq4N) {
        PortChainReq portChainReq = new PortChainReq();
        portChainReq.setPortPairGroups(portChainReq4N.getPortPairGroups());
        portChainReq.setFlowClassifiers(portChainReq4N.getFlowClassifiers());
        portChainReq.setUuid(SfcDriverUtil.generateUuid());

        ArrayList<ChainParameter> chainParam = new ArrayList<ChainParameter>();
        chainParam.add(SfcDriverUtil.generateChainParam("symmetric", portChainReq4N.isSymmetric() ? "true" : "false"));
        portChainReq.setChainParams(chainParam);
        ArrayList<PortChainReq> portChains = new ArrayList<PortChainReq>();
        portChains.add(portChainReq);
        PortChainReq4S portChainReq4S = new PortChainReq4S();
        portChainReq4S.setPortChainReqs(portChains);
        return portChainReq4S;
    }


}
