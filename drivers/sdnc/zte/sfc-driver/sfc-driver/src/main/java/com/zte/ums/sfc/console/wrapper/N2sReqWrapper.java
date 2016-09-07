 /**
 *       Copyright (C) 2016 ZTE, Inc. and others. All rights reserved. (ZTE)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zte.ums.sfc.console.wrapper;

import com.zte.ums.sfc.console.entity.*;
import com.zte.ums.sfc.console.entity.portpair.PortPairReq4N;
import com.zte.ums.sfc.console.entity.portpair.PortPairReq4S;
import com.zte.ums.sfc.console.utils.SfcDriverUtil;

import java.util.HashMap;
import java.util.Map;


public class N2sReqWrapper {
    public static PortPairReq4S convertPortPair(PortPairReq4N portPairReq4N)
    {
        PortPairReq4S portPairReq4S = new PortPairReq4S();
        portPairReq4S.setIngress(portPairReq4N.getIngress());
        portPairReq4S.setEgress(portPairReq4N.getEgress());

        Map serviceParameters = new HashMap<String,String>();
        serviceParameters.put("SfType",portPairReq4N.getSfType());
        serviceParameters.put("NshAware",portPairReq4N.isNshAware());
        serviceParameters.put("RequestReclassification",portPairReq4N.isRequestReclassification());
        if(portPairReq4N.getSfParam()!= null)
        {
            serviceParameters.putAll(portPairReq4N.getSfParam());
        }
        portPairReq4S.setServiceFunctionParameters(serviceParameters);

        return portPairReq4S;
    }

    public static PortPairGroupReq4S convertPortPairGroup(PortPairGroupReq4N ppg4N)
    {
        PortPairGroupReq4S ppg4S = new PortPairGroupReq4S();
        ppg4S.setPortPairs(ppg4N.getPortPairs());

        return ppg4S;
    }

    public static FlowClassfierReq4S convertFlowClassfier(FlowClassfierReq4N flowClassfierReq4N)
    {
        FlowClassfierReq4S flowClassfierReq4S = new FlowClassfierReq4S();
        flowClassfierReq4S.setProtocol(String.valueOf(flowClassfierReq4N.getIpProto()));
        flowClassfierReq4S.setSourcePortRangeMin(Integer.parseInt(flowClassfierReq4N.
                getSourcePortRange().split(",")[0]));
        flowClassfierReq4S.setSourcePortRangeMax(Integer.parseInt(flowClassfierReq4N.
                getSourcePortRange().split(",")[1]));
        flowClassfierReq4S.setDestinationPortRangeMin(Integer.parseInt(
                flowClassfierReq4N.getDestPortRange().split(",")[0]));
        flowClassfierReq4S.setDestinationPortRangeMax(Integer.parseInt(
                flowClassfierReq4N.getDestPortRange().split(",")[1]));
        flowClassfierReq4S.setIpDscp(flowClassfierReq4N.getDscp());


        return flowClassfierReq4S;
    }

    public static PortChainReq4S converPortChain(PortChainReq4N portChainReq4N)
    {
        PortChainReq4S portChainReq4S = new PortChainReq4S();
        portChainReq4S.setPortPairGroups(portChainReq4N.getPortPairGroups());
        portChainReq4S.setFlowClassifiers(portChainReq4N.getFlowClassifiers());

        return portChainReq4S;
    }
}
