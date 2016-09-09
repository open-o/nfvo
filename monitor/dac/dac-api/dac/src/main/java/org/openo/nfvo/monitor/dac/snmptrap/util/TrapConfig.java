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
package org.openo.nfvo.monitor.dac.snmptrap.util;

import java.io.File;
import java.io.IOException;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.openo.nfvo.monitor.dac.common.util.filescan.FastFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrapConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(TrapConfig.class);

	private static final String TRAPCONFIGFILE = "*-trap-config.xml";

	// default trap listen port
	private static int[] TRAP_PORT = new int[] { 162 };
	private static int trapSendQueueSize = 100;
	private static int trapReceiveQueueSize = 500;


	public static void loadConfig() {
		try {
            File[] files = FastFileSystem.getFiles(TRAPCONFIGFILE);
            for (File file : files)
            {
				Element rootElemt = TrapConfUtil.getElementFromXmlFile(file);

				Element tmpPort = (Element) rootElemt.getChildren("trapPort").get(0);
				String trapPort = tmpPort.getAttributeValue("value");
				String[] trapPortArray = trapPort.split(",");
				TRAP_PORT = new int[trapPortArray.length];
				for (int i = 0; i < trapPortArray.length; i++) {
					TRAP_PORT[i] = Integer.parseInt(trapPortArray[i]);
				}

				Element tmpTrapQueueSize = (Element) rootElemt.getChildren("trapSendQueueSize").get(0);
				String strapQueueSize = tmpTrapQueueSize.getAttributeValue("value");
				trapSendQueueSize = Integer.parseInt(strapQueueSize);

				tmpTrapQueueSize = (Element) rootElemt.getChildren("trapReceiveQueueSize").get(0);
				strapQueueSize = tmpTrapQueueSize.getAttributeValue("value");
				trapReceiveQueueSize = Integer.parseInt(strapQueueSize);
            }

		} catch (JDOMException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public static int[] getTrapPort() {
		return TRAP_PORT;
	}

	public static int getTrapSendQueueSize() {
		return trapSendQueueSize;
	}

	public static int getTrapReceiveQueueSize() {
		return trapReceiveQueueSize;
	}

}
