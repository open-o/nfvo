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
package org.openo.nfvo.monitor.umc.fm.adpt.resources.bean;

import org.openo.nfvo.monitor.umc.fm.resource.bean.response.NeMap;

/**
 * net elements from resource
 *
 */
public class RocInstances {

    private NeMap[] vnf;
    
    private NeMap[] vnfc;
    
    private NeMap[] host;
    
    private NeMap[] vim;
    
    private NeMap[] vdu;

    public NeMap[] getVnf() {
        return vnf;
    }

    public void setVnf(NeMap[] vnf) {
        this.vnf = vnf;
    }

    public NeMap[] getVnfc() {
        return vnfc;
    }

    public void setVnfc(NeMap[] vnfc) {
        this.vnfc = vnfc;
    }

    public NeMap[] getHost() {
        return host;
    }

    public void setHost(NeMap[] host) {
        this.host = host;
    }

    public NeMap[] getVim() {
        return vim;
    }

    public void setVim(NeMap[] vim) {
        this.vim = vim;
    }

    public NeMap[] getVdu() {
        return vdu;
    }

    public void setVdu(NeMap[] vdu) {
        this.vdu = vdu;
    }

}
