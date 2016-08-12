package org.openo.orchestrator.nfv.umc.fm.adpt.resources;

public class RocResourceConfig {
    
    protected static String rocResourceAddr;
    
    public static void setRocResourceAddr(String address){
        rocResourceAddr = "http://" + address;
    }
    
    public static String getRocResourceAddr(){
        return rocResourceAddr;
    }
}
