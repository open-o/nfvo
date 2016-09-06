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
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.vnfm;

import org.glassfish.jersey.client.ClientConfig;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.InstantiateVnfRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.InstantiateVnfResponse;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.OperateVnfRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.QueryVnfListResponse;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.VnfInfoFromVnfm;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.ScaleVnfRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.TerminalVnfRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.VnfInstanceProcessDetailResponse;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.VnfInstanceProcessResponse;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.hsif.HsifServiceConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;
import com.google.gson.Gson;

public class VnfmServiceConsumer {

    private static final Logger logger = LoggerFactory.getLogger(VnfmServiceConsumer.class);

    private IVnfmService vnfmServiceProxy = null;

    public VnfmServiceConsumer(String baseUrl) {
        ClientConfig config = new ClientConfig();
        HsifServiceConsumer.authVnfm(baseUrl);
        vnfmServiceProxy = ConsumerFactory.createConsumer(baseUrl, config, IVnfmService.class);
    }

    public InstantiateVnfResponse instantiateVnf(InstantiateVnfRequest request) {

        logger.info("instantiate request:"+new Gson().toJson(request));
        String result = vnfmServiceProxy.instantiateVnf(new Gson().toJson(request));
        logger.info("instantiate vnf result:{}", result);

        return new Gson().fromJson(result, InstantiateVnfResponse.class);
    }

    public VnfInstanceProcessResponse queryVnfInstanceProcess(String vnfInstanceId) {
        String result = vnfmServiceProxy.queryVnfInstanceProcess(vnfInstanceId);
        logger.info("query process result:{}", result);
        return new Gson().fromJson(result, VnfInstanceProcessResponse.class);
    }

    public VnfInstanceProcessDetailResponse queryVnfInstanceProcessDetail(String vnfInstanceId) {

        String result =
                vnfmServiceProxy.queryVnfInstanceProcessDetail(vnfInstanceId);
        logger.info("query vnf {} process detail result:{}",vnfInstanceId, result);
        return new Gson().fromJson(result, VnfInstanceProcessDetailResponse.class);
    }

    public void operateVnf(String vnfInstanceId, OperateVnfRequest request) {
        logger.info("operate vnf request:{}", new Gson().toJson(request));
        vnfmServiceProxy.operateVnf(vnfInstanceId, new Gson().toJson(request));
        return;
    }

    public void scaleVnf(String vnfInstanceId, ScaleVnfRequest request) {
        logger.info("scale vnf request:{}", new Gson().toJson(request));
        vnfmServiceProxy.scaleVnf(vnfInstanceId, new Gson().toJson(request));
        return;
    }

    public void terminalVnf(String vnfInstanceId, TerminalVnfRequest request) {
        logger.info("terminate vnf request:{}", new Gson().toJson(request));
        vnfmServiceProxy.terminalVnf(vnfInstanceId, new Gson().toJson(request));
        return;
    }

    public VnfInfoFromVnfm QueryVnf(String vnfInstanceId) {
        String result = vnfmServiceProxy.QueryVnf(vnfInstanceId);
        logger.info("query vnf id {} result:{}",vnfInstanceId, result);
        return new Gson().fromJson(result, VnfInfoFromVnfm.class);
    }

    public QueryVnfListResponse QueryVnfs() {
        String result = vnfmServiceProxy.QueryVnfs();
        logger.info("query vnf list result:{}", result);
        return new Gson().fromJson(result, QueryVnfListResponse.class);
    }
}
