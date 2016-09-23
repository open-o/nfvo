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
package org.openo.nfvo.monitor.umc.util;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Global {

	public static boolean isEmpty(String str) {
		if (null == str || str.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public static String getID() {
		String id = UUID.randomUUID().toString();
		return id;
	}

	public static String getMsbApiRootDomain() {
		return "/openoapi/microservices/v1/services"; 
	}
	
	public static String getCreateVnfdApiRootDomain() {
		return ":8888/v1.0/device-templates.json"; 
	}

	public static String getGrantApiRootDomain() {
		//return "/api/vim/v1/vnfgrantinfo";
		return "http://127.0.0.1:80/openoapi/vim/v1";
	}

	public static String getRocApiDomain() {
		return "http://127.0.0.1:80/openoapi/roc/v1";
	}

	public static String getVnfCreateByTackerApiDomain(){
		return ":8888/v1.0/devices.json";
	}
}
