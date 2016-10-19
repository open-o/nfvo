/**
 * Copyright 2016 [ZTE] and others.
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
package org.openo.sfc.resources;

import com.google.gson.Gson;
import org.openo.sfc.entity.MsbRegisterEntity;
import org.openo.sfc.service.ConfigInfo;
import org.openo.sfc.service.SdnServiceConsumer;
import org.openo.sfc.utils.SfcDriverUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsbServiceRegister implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(MsbServiceRegister.class);
    MsbRegisterEntity entity;
    public MsbServiceRegister()
    {
        initInfo();
    }

    private void initInfo()
    {
        entity = SfcDriverUtil.getMsbRegisterInfo();

    }

    @Override
    public void run() {
        boolean flag = false;
        int retryTimes=0;
        while (!flag && retryTimes<20)
        {
            try {
                LOGGER.info("Register Msb start:");
                LOGGER.info(SfcDriverUtil.toJson(entity));
                SdnServiceConsumer.getMsbRegisterService(ConfigInfo.getConfig().getMsbServiceUrl()).
                        registerServce("false",entity);
                LOGGER.info("Register Msb end:");
                flag = true;
                break;
            } catch (Exception e) {
                LOGGER.info("Register Msb failed");
                e.printStackTrace();
                threadSleep(30000);
            }
        }

    }

    private void threadSleep(int second) {
        LOGGER.info("start sleep ....");
        try {
            Thread.sleep(second);
        } catch (InterruptedException error) {
            LOGGER.error("thread sleep error.errorMsg:" + error.getMessage());
        }
        LOGGER.info("sleep end .");
    }
}
