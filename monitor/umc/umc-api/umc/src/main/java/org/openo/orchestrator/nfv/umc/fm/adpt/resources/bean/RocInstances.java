package org.openo.orchestrator.nfv.umc.fm.adpt.resources.bean;

import org.openo.orchestrator.nfv.umc.fm.resource.bean.response.NeMap;

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
