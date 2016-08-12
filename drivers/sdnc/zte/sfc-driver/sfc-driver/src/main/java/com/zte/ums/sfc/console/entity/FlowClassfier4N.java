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
package com.zte.ums.sfc.console.entity;

public class FlowClassfier4N {
    private String url;
    private String sdnControllerId;
    private String name;
    private String description;
    private int dscp;
    private int ip_proto;
    private String source_port_range;
    private String dest_port_range;
    private String source_ip_range;
    private String dest_ip_range;

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

    public int getDscp() {
        return dscp;
    }

    public void setDscp(int dscp) {
        this.dscp = dscp;
    }

    public int getIp_proto() {
        return ip_proto;
    }

    public void setIp_proto(int ip_proto) {
        this.ip_proto = ip_proto;
    }

    public String getSource_port_range() {
        return source_port_range;
    }

    public void setSource_port_range(String source_port_range) {
        this.source_port_range = source_port_range;
    }

    public String getDest_port_range() {
        return dest_port_range;
    }

    public void setDest_port_range(String dest_port_range) {
        this.dest_port_range = dest_port_range;
    }

    public String getSource_ip_range() {
        return source_ip_range;
    }

    public void setSource_ip_range(String source_ip_range) {
        this.source_ip_range = source_ip_range;
    }

    public String getDest_ip_range() {
        return dest_ip_range;
    }

    public void setDest_ip_range(String dest_ip_range) {
        this.dest_ip_range = dest_ip_range;
    }
}
