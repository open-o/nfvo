/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.umc.sm.wrapper;

import org.openo.orchestrator.nfv.umc.sm.bean.FrameCommInfo;
import org.openo.orchestrator.nfv.umc.sm.bean.VersionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @ClassName: UiFrameService
* @Description: TODO(UiFrameInfo implementation class)
* @author tanghua10186366
*
*/
public class UiFrameServiceWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UiFrameServiceWrapper.class);

    private static UiFrameServiceWrapper instance = new UiFrameServiceWrapper();

    private FrameCommInfo frameconfig = new FrameCommInfo();
    private VersionInfo info = new VersionInfo();

    public synchronized static UiFrameServiceWrapper getInstance() {
        return instance;
    }

    /**
    * @Title getFrameconfig
    * @Description TODO(get UiFrame configInfo )
    * @return
    * @return FrameCommInfo
    */
    public FrameCommInfo getFrameconfig() {
        return frameconfig;
    }

    public void setFrameconfig(FrameCommInfo frameconfig) {
        this.frameconfig = frameconfig;
    }

    /**
    * @Title getVersioninfo
    * @Description TODO(get UiFrame VersionInfo )
    * @return
    * @return VersionInfo
    */
    public VersionInfo getVersioninfo() {
        return info;
    }

    public void setVersioninfo(VersionInfo info) {
        this.info = info;
    }

}
