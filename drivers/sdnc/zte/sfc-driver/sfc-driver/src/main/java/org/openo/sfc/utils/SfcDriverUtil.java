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
package org.openo.sfc.utils;

import com.google.gson.Gson;
import org.openo.sfc.entity.ChainParameter;
import org.openo.sfc.entity.MsbRegisterEntity;
import org.openo.sfc.entity.NodeEntity;
import org.openo.sfc.entity.portpair.ServiceFunctionParameter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SfcDriverUtil {

    public static String generateUuid() {
        return UUID.randomUUID().toString();

    }

    public static ArrayList<ServiceFunctionParameter> generateSfParams(Map sfHashParams) {
        ArrayList<ServiceFunctionParameter> serviceParameters = new ArrayList<ServiceFunctionParameter>();
        if (sfHashParams != null) {
            //serviceParameters.putAll(portPairReq4N.getSfParam());
            Set keySet = sfHashParams.keySet();
            Iterator iter = keySet.iterator();
            while (iter.hasNext()) {
                String keyName = iter.next().toString();
                ServiceFunctionParameter sfParam = new ServiceFunctionParameter();
                sfParam.setServiceFunctionParamter(keyName);
                sfParam.setGetServiceFunctionParamterValue(sfHashParams.get(keyName).toString());
                serviceParameters.add(sfParam);
            }
        }
        return serviceParameters;
    }

    public static ChainParameter generateChainParam(String key, String value) {
        ChainParameter cp = new ChainParameter();
        cp.setChainParameter(key);
        cp.setChainParamValue(value);
        return cp;
    }

    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static String generateAuthorization() {
        String userName = "admin";
        String password = "admin";
        String usernameAndPassword = userName + ":" + password;
//        String headName = "Authorization";
        return "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(usernameAndPassword.getBytes());
    }

    public static MsbRegisterEntity getMsbRegisterInfo() {
        MsbRegisterEntity entity = new MsbRegisterEntity();
        entity.setServiceName(SfcConst.SERVICE_NAME);
        entity.setVersion("v1");
        entity.setUrl(SfcConst.SERVICE_URL);
        entity.setProtocol("REST");
        entity.setVisualRange("1");
        ArrayList<NodeEntity> nodes = new ArrayList<NodeEntity>();
        NodeEntity node = new NodeEntity();
        node.setIp("");
        node.setPort("8411");
        node.setTtl("1");
        nodes.add(node);
        entity.setNodes(nodes);
        return entity;
    }

}
