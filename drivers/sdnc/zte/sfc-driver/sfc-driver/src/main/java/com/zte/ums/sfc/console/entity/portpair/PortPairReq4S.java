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
package com.zte.ums.sfc.console.entity.portpair;

import java.util.Map;


public class PortPairReq4S {
    private PortInfo ingress;
    private PortInfo egress;
    private Map service_function_parameters;

    public Map getService_function_parameters() {
        return service_function_parameters;
    }

    public void setService_function_parameters(Map service_function_parameters) {
        this.service_function_parameters = service_function_parameters;
    }

    public PortInfo getIngress() {
        return ingress;
    }

    public void setIngress(PortInfo ingress) {
        this.ingress = ingress;
    }

    public PortInfo getEgress() {
        return egress;
    }

    public void setEgress(PortInfo egress) {
        this.egress = egress;
    }


}
