package com.zte.ums.sfc.console.entity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 10084662
 * Date: 16-8-13
 * Time: ÉÏÎç11:22
 * To change this template use File | Settings | File Templates.
 */
public class PortChainReq4S {
    private ArrayList<String> portPairGroups;
    private ArrayList<String> flowClassifiers;
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private Map chainParameters;

    public ArrayList<String> getPortPairGroups() {
        return portPairGroups;
    }

    public void setPortPairGroups(ArrayList<String> portPairGroups) {
        this.portPairGroups = portPairGroups;
    }

    public ArrayList<String> getFlowClassifiers() {
        return flowClassifiers;
    }

    public void setFlowClassifiers(ArrayList<String> flowClassifiers) {
        this.flowClassifiers = flowClassifiers;
    }

    public Map getChainParameters() {
        return chainParameters;
    }

    public void setChainParameters(Map chainParameters) {
        this.chainParameters = chainParameters;
    }
}
