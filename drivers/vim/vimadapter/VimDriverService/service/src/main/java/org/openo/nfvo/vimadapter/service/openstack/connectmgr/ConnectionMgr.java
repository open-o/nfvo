/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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

package org.openo.nfvo.vimadapter.service.openstack.connectmgr;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.security.auth.login.LoginException;

import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.entity.Vim;
import org.openo.nfvo.vimadapter.service.openstack.api.ConnectInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * It provides connection management of OpenstackConnection.<br/>
 *
 * @author
 * @version NFVO 0.5 Aug 23, 2016
 */
public final class ConnectionMgr {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionMgr.class);

    private static ConnectionMgr conSingleton = null;

    private static Map<ConnectInfo, OpenstackConnection> conMap =
            new ConcurrentHashMap<>(Constant.DEFAULT_COLLECTION_SIZE);

    private ConnectionMgr() {
    }

    public static synchronized ConnectionMgr getConnectionMgr() {
        if(null == conSingleton) {
            conSingleton = new ConnectionMgr();
        }
        return conSingleton;
    }

    /**
     * Add connection to openstack.<br/>
     *
     * @param vim the information of vim
     * @return the status about connect openstack
     * @throws LoginException when login failed
     * @since NFVO 0.5
     */
    public synchronized int addConnection(Vim vim) {
        LOG.info("funtion=addConnection, msg=create a new connection.");

        ConnectInfo info = new ConnectInfo(vim);
        OpenstackConnection connect = new OpenstackConnection(info);
        int status = connect.connect();
        conMap.put(info, connect);
        return status;
    }

    /**
     * Get connection with openstack.<br/>
     *
     * @param info ConnectInfo
     * @return The openstack connection
     * @throws LoginException when login failed
     * @since NFVO 0.5
     */
    public OpenstackConnection getConnection(ConnectInfo info) {
        OpenstackConnection connect = null;
        if(conMap.get(info) != null) {
            connect = conMap.get(info);
            if(connect.isNeedRenewInfo(info) || (connect.isConnected() != Constant.HTTP_OK_STATUS_CODE)) {
                connect.setConnInfo(info);
                connect.connect();
                conMap.put(info, connect);
            }
        } else {
            LOG.info("funtion=getConnection, msg=Connection is not exist, new one .");
            connect = new OpenstackConnection(info);
            connect.connect();
            conMap.put(info, connect);
        }
        return connect;
    }

    /**
     * Remove the connection with openstack.<br/>
     *
     * @param vim the information of vim
     * @since NFVO 0.5
     */
    public synchronized void disConnect(Vim vim) {
        ConnectInfo info = new ConnectInfo(vim);
        OpenstackConnection connect = conMap.get(info);
        if(connect != null) {
            connect.disconnect();
            conMap.remove(info);
        }
    }

    /**
     * Judge the connection is present.<br/>
     *
     * @param vim the information of vim
     * @return the status of connection
     * @throws LoginException when login failed
     * @since NFVO 0.5
     */
    public int isConnected(Vim vim) {
        ConnectInfo info = new ConnectInfo(vim);
        LOG.debug("funtion=isConnected, msg=isConnected, connection info:", info.toString());

        OpenstackConnection connect = conMap.get(info);
        if(connect != null) {
            return connect.isConnected();
        }

        LOG.warn("funtion=isConnected, msg=isConnected is null, connection info:", info.toString());
        return Constant.CONNECT_FAIL_STATUS_CODE;
    }
}
