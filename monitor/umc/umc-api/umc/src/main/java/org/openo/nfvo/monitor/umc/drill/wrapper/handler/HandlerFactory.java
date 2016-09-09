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
package org.openo.nfvo.monitor.umc.drill.wrapper.handler;

import org.openo.nfvo.monitor.umc.drill.wrapper.common.TopologyConsts;
import org.openo.nfvo.monitor.umc.drill.wrapper.handler.drill.HostDrillHandler;
import org.openo.nfvo.monitor.umc.drill.wrapper.handler.drill.NSDrillHandler;
import org.openo.nfvo.monitor.umc.drill.wrapper.handler.drill.VDUDrillHandler;
import org.openo.nfvo.monitor.umc.drill.wrapper.handler.drill.VNFCDrillHandler;
import org.openo.nfvo.monitor.umc.drill.wrapper.handler.drill.VNFDrillHandler;
import org.openo.nfvo.monitor.umc.drill.wrapper.handler.layermonitor.HostLayerMonitorHandler;
import org.openo.nfvo.monitor.umc.drill.wrapper.handler.layermonitor.NSLayerMonitorHandler;
import org.openo.nfvo.monitor.umc.drill.wrapper.handler.layermonitor.VDULayerMonitorHandler;

/**
 *       The factory that holds all the handler instances
 */
public class HandlerFactory {

    private static HandlerFactory instance = new HandlerFactory();
    /** array used to save all the handlers **/
    public AbstractTopologyHandler[] handlers = new AbstractTopologyHandler[8];

    private HandlerFactory() {
        // drill handler
        handlers[TopologyConsts.HANDlER_DRILL_NS] = new NSDrillHandler();
        handlers[TopologyConsts.HANDlER_DRILL_VNF] = new VNFDrillHandler();
        handlers[TopologyConsts.HANDlER_DRILL_VNFC] = new VNFCDrillHandler();
        handlers[TopologyConsts.HANDlER_DRILL_VDU] = new VDUDrillHandler();
        handlers[TopologyConsts.HANDlER_DRILL_HOST] = new HostDrillHandler();
        // layer monitor handler
        handlers[TopologyConsts.HANDlER_LAYERMONITOR_NS] = new NSLayerMonitorHandler();
        handlers[TopologyConsts.HANDlER_LAYERMONITOR_VDU] = new VDULayerMonitorHandler();
        handlers[TopologyConsts.HANDlER_LAYERMONITOR_HOST] = new HostLayerMonitorHandler();
    }

    /**
     * singleton
     *
     * @return
     */
    public static HandlerFactory getInstance() {
        return instance;
    }

    /**
     * return the specified handler instance
     *
     * @param index
     * @return
     */
    public AbstractTopologyHandler getHandler(int index) {
        return handlers[index];
    }
}
