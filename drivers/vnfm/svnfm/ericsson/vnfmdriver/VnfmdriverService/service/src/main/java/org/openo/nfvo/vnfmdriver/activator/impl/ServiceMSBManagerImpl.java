/*
 * Copyright (c) 2017 Ericsson (China) Communication Co. Ltd.
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

package org.openo.nfvo.vnfmdriver.activator.impl;

import java.util.Map;

import net.sf.json.JSONObject;

import org.openo.baseservice.roa.util.restclient.RestfulResponse;
import org.openo.nfvo.vnfmdriver.activator.inf.InfServiceMSBManager;
import org.openo.nfvo.vnfmdriver.common.constant.Constant;
import org.openo.nfvo.vnfmdriver.common.restfulutil.HttpRestfulAPIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Feb 15, 2017
 */
public class ServiceMSBManagerImpl implements InfServiceMSBManager {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceMSBManagerImpl.class.getName());

    /**
     * <br>
     *
     * @param paramsMap
     * @param driverInfo
     * @return
     * @since NFVO 0.5
     */
    public int register(Map<String, String> paramsMap, JSONObject driverInfo) {

        LOG.info("Ericsson VNFM Driver Register...", ServiceMSBManagerImpl.class);

        int ret = Constant.DRIVER_REGISTER_NG;
        RestfulResponse rsp = HttpRestfulAPIUtil.getRemoteResponse(paramsMap, driverInfo.toString());

        if(null == rsp) {
            LOG.error("Ericsson VNFM Driver Register Failed", ServiceMSBManagerImpl.class);
            return ret;
        }

        if(rsp.getStatus() == Constant.HTTP_INNERERROR) {
            LOG.info("MSB Internal Error, Wait and Repeat to Register", ServiceMSBManagerImpl.class);
            ret = Constant.DRIVER_REGISTER_REPEAT;
        } else if(rsp.getStatus() == Constant.HTTP_INVALID_PARAMETERS) {
            LOG.info("Register Param is Invalid", ServiceMSBManagerImpl.class);
            ret = Constant.DRIVER_REGISTER_NG;
        } else if(rsp.getStatus() == Constant.HTTP_CREATED) {
            LOG.info("Register Successfully", ServiceMSBManagerImpl.class);
            ret = Constant.DRIVER_REGISTER_OK;
        } else {
            LOG.error("Unkonw Result, Please Check it", ServiceMSBManagerImpl.class);
            ret = Constant.DRIVER_REGISTER_NG;
        }
        return ret;
    }

    /**
     * <br>
     *
     * @param paramsMap
     * @param driverInfo
     * @return
     * @since NFVO 0.5
     */
    public int unRegister(Map<String, String> paramsMap, JSONObject driverInfo) {
        LOG.info("Ericsson VNFM Driver Unregister...", ServiceMSBManagerImpl.class);

        return Constant.DRIVER_UNREGISTER_OK;
    }
}
