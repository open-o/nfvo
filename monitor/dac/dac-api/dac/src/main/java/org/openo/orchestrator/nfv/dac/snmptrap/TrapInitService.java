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
package org.openo.orchestrator.nfv.dac.snmptrap;

import java.io.File;
import java.io.IOException;

import org.openo.orchestrator.nfv.dac.common.DacConst;
import org.openo.orchestrator.nfv.dac.common.util.GeneralFileLocaterImpl;
import org.openo.orchestrator.nfv.dac.snmptrap.processor.util.TrapProcessorListParser;
import org.openo.orchestrator.nfv.dac.snmptrap.queue.TrapGetMsgQueue;
import org.openo.orchestrator.nfv.dac.snmptrap.queue.TrapSendMsgQueue;
import org.openo.orchestrator.nfv.dac.snmptrap.util.MibFileResolver;
import org.openo.orchestrator.nfv.dac.snmptrap.util.TrapConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TrapInitService
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TrapInitService.class);

    public static void startService()
    {
    	TrapConfig.loadConfig();
        mibInitAndTrapProcessor();
        TrapGetMsgQueue.getInstance().startGetTrapEvent();
        TrapSendMsgQueue.getInstance().startSendTrapAlarm();
        TrapLisSrv.getInstance().trapLisStart(TrapConfig.getTrapPort());
    }


    private static void mibInitAndTrapProcessor()
    {
        try
        {
        	StringBuilder serializedFilePath = new StringBuilder();
        	serializedFilePath.append(GeneralFileLocaterImpl.getGeneralFileLocater().getFilePath(DacConst.SYSTEMDIR));
        	serializedFilePath.append(File.separator).append(DacConst.FMDIR);
        	serializedFilePath.append(File.separator).append(DacConst.MIBINFOFILE);

			MibFileResolver mfResolver = MibFileResolver.getInstance(serializedFilePath.toString());
//			TrapConfUtil.setMibNodeInfo(mfResolver.getNodeInfo());

            TrapProcessorListParser.getInstance().getTrapProcessor();

            LOGGER.info("MibServerService started!");
        }
        catch (IOException e)
        {
        	LOGGER.warn("MibServerService init failed! " + e.getMessage());
        }
    }
}
