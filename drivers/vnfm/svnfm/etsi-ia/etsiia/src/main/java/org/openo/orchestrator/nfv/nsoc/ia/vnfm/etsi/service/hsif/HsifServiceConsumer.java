/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.hsif;

import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.common.MSBUtil;

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * resource service consumer, encapsulating resource interfaces
 * @author 10186401
 *
 */
public class HsifServiceConsumer {

    public static void authVnfm(String vnfmUrl) {
        String baseURL = MSBUtil.getHsifBaseUrl();
        IHsifServiceRest hsifserviceproxy =
                ConsumerFactory.createConsumer(baseURL, IHsifServiceRest.class);
        String reg = ".*\\/\\/([^\\/\\:]*).*";
        String ip = vnfmUrl.replaceAll(reg, "$1");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("passIp", ip);
        hsifserviceproxy.authVnfm(new Gson().toJson(jsonObject));
    }
}
