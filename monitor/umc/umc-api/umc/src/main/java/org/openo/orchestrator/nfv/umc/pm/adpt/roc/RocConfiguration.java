package org.openo.orchestrator.nfv.umc.pm.adpt.roc;

public class RocConfiguration {
    protected static String rocAddress;
    
    public static void setRocServerAddr(String address){
    	rocAddress = "http://" + address;
    }
    
    public static String getRocServerAddr(){
    	return rocAddress;
    }
    
}
