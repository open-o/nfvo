package org.openo.orchestrator.nfv.umc.fm.resource.bean.response;
/**
 * net element map by oid and name
 *
 */
public class NeMap {
    
    private String oid;
    
    private String name;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
