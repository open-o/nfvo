
/**
 *       Copyright (C) 2015 ZTE, Inc. and others. All rights reserved. (ZTE)
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

import com.zte.ums.sfc.console.entity.PortPairGroupReq4N;
import com.zte.ums.sfc.console.entity.PortPairGroupReq4S;
import com.zte.ums.sfc.console.entity.portpair.PortPairReq4N;
import com.zte.ums.sfc.console.entity.portpair.PortPairReq4S;

import java.util.HashMap;
import java.util.Map;


public class N2sReqWrapper {
    public static PortPairReq4S convertPortPair(PortPairReq4N portPairReq4N)
    {
        PortPairReq4S portPairReq4S = new PortPairReq4S();
        portPairReq4S.setIngress(portPairReq4N.getIngress());
        portPairReq4S.setEgress(portPairReq4N.getEgress());

        Map serviceParameters = new HashMap<String,String>();
        serviceParameters.put("ServiceFunctionType",portPairReq4N.getSfType());
        serviceParameters.put("nshAware",portPairReq4N.isNshAware());
        serviceParameters.put("requestReclassification",portPairReq4N.isRequestReclassification());
        if(portPairReq4N.getSfParam()!= null)
        {
            serviceParameters.putAll(portPairReq4N.getSfParam());
        }

        return portPairReq4S;
    }

    public static PortPairGroupReq4S convertPortPairGroup(PortPairGroupReq4N ppg4N)
    {
        PortPairGroupReq4S ppg4S = new PortPairGroupReq4S();
        ppg4S.setPortPairs(ppg4N.getPortPairs());

        return ppg4S;
    }

}
