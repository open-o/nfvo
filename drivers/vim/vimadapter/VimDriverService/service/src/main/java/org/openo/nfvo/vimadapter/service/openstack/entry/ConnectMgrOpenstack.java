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

package org.openo.nfvo.vimadapter.service.openstack.entry;

import org.openo.nfvo.vimadapter.service.constant.Constant;
import org.openo.nfvo.vimadapter.service.entity.Vim;
import org.openo.nfvo.vimadapter.service.openstack.connectmgr.ConnectionMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Openstack connection management.<br/>
 * 
 * @author
 * @version NFVO 0.5 Aug 24, 2016
 */
public class ConnectMgrOpenstack {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectMgrOpenstack.class);

    /**
     * Connect to openstack.<br/>
     * 
     * @param vim the information of vim
     * @param type the operation type of vim connection
     * @return the status of connection
     * @since NFVO 0.5
     */
    public int connect(Vim vim, String type) {
        ConnectionMgr connectionMgr = ConnectionMgr.getConnectionMgr();

        if(connectionMgr != null) {
            if(Constant.POST.equals(type)) {
                return connectionMgr.addConnection(vim);
            } else if(Constant.DEL.equals(type)) {
                connectionMgr.disConnect(vim);
                return Constant.HTTP_OK_STATUS_CODE;
            } else {
                LOG.warn("function=connect, msg=type error.");
                return Constant.TYPE_PARA_ERROR_STATUS_CODE;
            }
        }
        return Constant.INTERNAL_EXCEPTION_STATUS_CODE;
    }
}
