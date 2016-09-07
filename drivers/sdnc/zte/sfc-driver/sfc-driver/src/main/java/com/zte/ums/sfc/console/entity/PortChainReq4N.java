package com.zte.ums.sfc.console.entity;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: 10084662
 * Date: 16-8-13
 * Time: ÉÏÎç11:16
 * To change this template use File | Settings | File Templates.
 */
public class PortChainReq4N {
    private String sdnControllerId;
    private String url;
    private String name;
    private String description;
    private ArrayList<String> flowClassifiers;
    private ArrayList<String> portPairGroups;
    private boolean symmetric;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSdnControllerId() {
        return sdnControllerId;
    }

    public void setSdnControllerId(String sdnControllerId) {
        this.sdnControllerId = sdnControllerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getFlowClassifiers() {
        return flowClassifiers;
    }

    public void setFlowClassifiers(ArrayList<String> flowClassifiers) {
        this.flowClassifiers = flowClassifiers;
    }

    public ArrayList<String> getPortPairGroups() {
        return portPairGroups;
    }

    public void setPortPairGroups(ArrayList<String> portPairGroups) {
        this.portPairGroups = portPairGroups;
    }

    public boolean isSymmetric() {
        return symmetric;
    }

    public void setSymmetric(boolean symmetric) {
        this.symmetric = symmetric;
    }
}
