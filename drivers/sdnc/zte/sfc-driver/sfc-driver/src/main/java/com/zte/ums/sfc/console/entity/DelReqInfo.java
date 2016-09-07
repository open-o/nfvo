package com.zte.ums.sfc.console.entity;

/**
 * Created with IntelliJ IDEA.
 * User: 10084662
 * Date: 16-8-13
 * Time: ÉÏÎç11:29
 * To change this template use File | Settings | File Templates.
 */
public class DelReqInfo {
    private String sdnControllerId;
    private String url;
    private String id;

    public String getSdnControllerId() {
        return sdnControllerId;
    }

    public void setSdnControllerId(String sdnControllerId) {
        this.sdnControllerId = sdnControllerId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
