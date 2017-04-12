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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

import org.openo.nfvo.vnfmdriver.activator.inf.InfServiceController;
import org.openo.nfvo.vnfmdriver.activator.inf.InfServiceMSBManager;
import org.openo.nfvo.vnfmdriver.common.FileUtil;
import org.openo.nfvo.vnfmdriver.common.constant.Constant;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openo.baseservice.util.impl.SystemEnvVariablesFactory;

/**
 * <br>
 * <p>
 * </p>
 *
 * @author
 * @version NFVO 0.5 Feb 15, 2017
 */
@Component
public class ServiceControllerImpl implements InfServiceController {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceControllerImpl.class);

    /**
     * <br>
     *
     * @since NFVO 0.5
     */
    public void start() {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("url", Constant.DRIVER_REGISTER_MSB_URL);
        paramsMap.put("methodType", Constant.POST);

        // get vnfm driver info and registration it
        try {
            LOG.info("Ericsson VNFM Driver Get Driver Info!");

            String driverInfo = FileUtil.read(getDriverInfoFilePath());
            LOG.debug("The Vnfm Driver info:" + driverInfo);

            if(driverInfo.equals("")) {
                LOG.error("DriverInfo file is empty or not found");
            } else {
                JSONObject driverObject = JSONObject.fromObject(driverInfo);
                VnfmDriverRegisterThread vnfmDriverThread = new VnfmDriverRegisterThread(paramsMap, driverObject);
                Executors.newSingleThreadExecutor().submit(vnfmDriverThread);
            }

        } catch(IOException e) {
            LOG.error("Failed to read Vnfm Driver info! " + e.getMessage(), e);
        }
    }

    /**
     * <br>
     *
     * @since NFVO 0.5
     */
    public void stop() {
    }

    public static String getDriverInfoFilePath() {

        String filePath = SystemEnvVariablesFactory.getInstance().getAppRoot() + System.getProperty("file.separator")
                + "etc" + System.getProperty("file.separator") + "driverInfo" + System.getProperty("file.separator")
                + Constant.VNFM_DRIVER_INFO;

        LOG.debug("The Path of Vnfm Driver info File Path:" + filePath);
        return filePath;
    }

    private static class VnfmDriverRegisterThread implements Runnable {

        // Thread lock Object
        private final Object lockObject = new Object();

        // Driver MSB Manager
        private InfServiceMSBManager msbManager = new ServiceMSBManagerImpl();

        // url and mothedtype
        private Map<String, String> paramsMap;

        // driver body
        private JSONObject driverInfo;

        public VnfmDriverRegisterThread(Map<String, String> paramsMap, JSONObject driverInfo) {
            this.paramsMap = paramsMap;
            this.driverInfo = driverInfo;
        }

        /**
         * <br>
         *
         * @since NFVO 0.5
         */
        public void run() {

            if(null == paramsMap || null == driverInfo) {
                LOG.error("Please Check The Parameter!", VnfmDriverRegisterThread.class);
                return;
            }

            try {
                int driverRegisterResult = Constant.DRIVER_REGISTER_REPEAT;
                while(driverRegisterResult == Constant.DRIVER_REGISTER_REPEAT) {
                    driverRegisterResult = msbManager.register(paramsMap, driverInfo);

                    if(driverRegisterResult != Constant.DRIVER_REGISTER_REPEAT) {
                        break;
                    }

                    synchronized(lockObject) {
                        lockObject.wait(Constant.DRIVER_REGISTER_TIMEER);
                    }
                }
            } catch(RuntimeException e) {
                LOG.error(e.getMessage(), e);
            } catch(InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }

        }
    }
}
