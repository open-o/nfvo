/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.wrapper;

import java.util.List;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;

import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.common.ConversionTool;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.InstantiateRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.OperateRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.ScaleRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.TerminalRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.InstantiateVnfRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.InstantiateVnfResponse;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.OperateVnfRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.QueryVnfListResponse;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.ScaleVnfRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.TerminalVnfRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.VnfInfoFromVnfm;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.VnfInstanceProcessDetailResponse;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.VnfInstanceProcessResponse;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.vnfm.VnfmServiceConsumer;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.wrapper.roc.RocWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * VNFM wrapper
 *
 * @author 10189609
 *
 */
public class VnfmAdapterServiceWrapper {
    private static final Logger logger = LoggerFactory.getLogger(VnfmAdapterServiceWrapper.class);
    private static VnfmAdapterServiceWrapper wrapper = null;

    public static VnfmAdapterServiceWrapper getInstance() {
        if (wrapper == null) {
            wrapper = new VnfmAdapterServiceWrapper();
        }
        return wrapper;
    }

    public List<VnfInfoFromVnfm> queryVnfs(String vnfmId) {
        // check vnfmid
        if (vnfmId == null || vnfmId.length() == 0) {
            logger.error("the query request was checked out parameter error:the vnfm id is wrong.");
            throw new NotFoundException("parameter error.the vnfm id is wrong.");
        }

        // get vnfm url
        String vnfmBaseUrl = RocWrapper.getVnfmUrlByVnfmOid(vnfmId);
        if (vnfmBaseUrl == null || vnfmBaseUrl.length() == 0) {
            logger.error("the vnfm url which is corresponded by the vnfm id-{} is invalid.", vnfmId);
            throw new InternalServerErrorException(
                    "the vnfm url which is corresponded by the vnfm id-" + vnfmId + "is invalid.");
        }

        // send request
        VnfmServiceConsumer vnfmInstance = new VnfmServiceConsumer(vnfmBaseUrl);
        QueryVnfListResponse vnfmresponse = vnfmInstance.QueryVnfs();

        return vnfmresponse.getVnfs();
    }

    public VnfInfoFromVnfm queryVnf(String vnfInstanceId) {
        // check vnfinstanceid
        if (vnfInstanceId == null || vnfInstanceId.length() == 0) {
            logger.error("the query process request was checked out parameter error:the vnf instance id is wrong.");
            throw new NotFoundException("parameter error.the vnf instance id is wrong.");
        }

        // get vnfm url
        String vnfmBaseUrl = RocWrapper.getVnfmUrlByVnfInstanceOid(vnfInstanceId);
        if (vnfmBaseUrl == null || vnfmBaseUrl.length() == 0) {
            logger.error("the vnfm url which is corresponded by the vnf id-{} is invalid.",
                    vnfInstanceId);
            throw new InternalServerErrorException(
                    "the vnfm url which is corresponded by the vnf id-" + vnfInstanceId
                            + "is invalid.");
        }

        // send request
        VnfmServiceConsumer vnfmInstance = new VnfmServiceConsumer(vnfmBaseUrl);
        VnfInfoFromVnfm sourceVnfInfo = vnfmInstance.QueryVnf(vnfInstanceId);

        return sourceVnfInfo;
    }

    public VnfInfoFromVnfm queryVnf(String vnfInstanceId,String vnfmId) {
        // check vnfinstanceid
        if (vnfInstanceId == null || vnfInstanceId.length() == 0) {
            logger.error("the query process request was checked out parameter error:the vnf instance id is wrong.");
            throw new NotFoundException("parameter error.the vnf instance id is wrong.");
        }

        // get vnfm url
        String vnfmBaseUrl = RocWrapper.getVnfmUrlByVnfmOid(vnfmId);
        if (vnfmBaseUrl == null || vnfmBaseUrl.length() == 0) {
            logger.error("the vnfm url which is corresponded by the vnfm id-{} is invalid.", vnfmId);
            throw new InternalServerErrorException(
                    "the vnfm url which is corresponded by the vnfm id-" + vnfmId + "is invalid.");
        }

        // send request
        VnfmServiceConsumer vnfmInstance = new VnfmServiceConsumer(vnfmBaseUrl);
        VnfInfoFromVnfm sourceVnfInfo = vnfmInstance.QueryVnf(vnfInstanceId);

        return sourceVnfInfo;
    }

    public InstantiateVnfResponse instantiateVnf(InstantiateRequest request) {
        // check vnfmid
        if (request == null || request.getVnfmId() == null || request.getVnfmId().length() == 0) {
            logger.error("the instantiation request was checked out parameter error:the vnfm id is wrong.");
            throw new NotFoundException("parameter error.the vnfm id is wrong.");
        }

        // get vnfm url
        String vnfmBaseUrl = RocWrapper.getVnfmUrlByVnfmOid(request.getVnfmId());
        if (vnfmBaseUrl == null || vnfmBaseUrl.length() == 0) {
            logger.error("the vnfm url which corresponded by the vnfm id-{} is invalid.",
                    request.getVnfmId());
            throw new InternalServerErrorException(
                    "the vnfm url which corresponded by the vnfm id-" + request.getVnfmId()
                            + "is invalid.");
        }

        // prepare parameter
        InstantiateVnfRequest vnfmRequest = ConversionTool.convertInstantiateVnfRequest(request);

        // send request
        VnfmServiceConsumer vnfmInstance = new VnfmServiceConsumer(vnfmBaseUrl);
        InstantiateVnfResponse vnfmResponse = vnfmInstance.instantiateVnf(vnfmRequest);

        return vnfmResponse;
    }

    public void operateVnf(String vnfInstanceId, OperateRequest request) {
        // check vnfinstanceid
        if (vnfInstanceId == null || vnfInstanceId.length() == 0) {
            logger.error("the query process request was checked out parameter error:the vnf instance id is wrong.");
            throw new NotFoundException("parameter error.the vnf instance id is wrong.");
        }

        // get vnfm url
        String vnfmBaseUrl = RocWrapper.getVnfmUrlByVnfInstanceOid(vnfInstanceId);
        if (vnfmBaseUrl == null || vnfmBaseUrl.length() == 0) {
            logger.error("the vnfm url which corresponded by the vnf id-{} is invalid.",
                    vnfInstanceId);
            throw new InternalServerErrorException("the vnfm url which corresponded by the vnf id-"
                    + vnfInstanceId + "is invalid.");
        }

        // prepare parameter
        OperateVnfRequest vnfmRequest = ConversionTool.convertOperateVnfRequest(request);

        // send request
        VnfmServiceConsumer vnfmInstance = new VnfmServiceConsumer(vnfmBaseUrl);
        vnfmInstance.operateVnf(vnfInstanceId, vnfmRequest);

        return;
    }

    public void scaleVnf(String vnfInstanceId, ScaleRequest request) {
        // check vnfinstanceid
        if (vnfInstanceId == null || vnfInstanceId.length() == 0) {
            logger.error("the query process request was checked out parameter error:the vnf instance id is wrong.");
            throw new NotFoundException("parameter error.the vnf instance id is wrong.");
        }

        // get vnfm url
        String vnfmBaseUrl = RocWrapper.getVnfmUrlByVnfInstanceOid(vnfInstanceId);
        if (vnfmBaseUrl == null || vnfmBaseUrl.length() == 0) {
            logger.error("the vnfm url which corresponded by the vnf id-{} is invalid.",
                    vnfInstanceId);
            throw new InternalServerErrorException("the vnfm url which corresponded by the vnf id-"
                    + vnfInstanceId + "is invalid.");
        }

        // prepare parameter
        ScaleVnfRequest vnfmRequest = ConversionTool.convertScaleVnfRequest(request);

        // send request
        VnfmServiceConsumer vnfmInstance = new VnfmServiceConsumer(vnfmBaseUrl);
        vnfmInstance.scaleVnf(vnfInstanceId, vnfmRequest);

        return;
    }

    public void terminalVnf(String vnfInstanceId, TerminalRequest request) {
        // check vnfinstanceid
        if (vnfInstanceId == null || vnfInstanceId.length() == 0) {
            logger.error("the query process request was checked out parameter error:the vnf instance id is wrong.");
            throw new NotFoundException("parameter error.the vnf instance id is wrong.");
        }

        // get vnfm url
        String vnfmBaseUrl = RocWrapper.getVnfmUrlByVnfInstanceOid(vnfInstanceId);
        if (vnfmBaseUrl == null || vnfmBaseUrl.length() == 0) {
            logger.error("the vnfm url which corresponded by the vnf id-{} is invalid.",
                    vnfInstanceId);
            throw new InternalServerErrorException("the vnfm url which corresponded by the vnf id-"
                    + vnfInstanceId + "is invalid.");
        }

        // prepare parameter
        TerminalVnfRequest vnfmRequest = ConversionTool.convertTerminalVnfRequest(request);

        // send request
        VnfmServiceConsumer vnfmInstance = new VnfmServiceConsumer(vnfmBaseUrl);
        vnfmInstance.terminalVnf(vnfInstanceId, vnfmRequest);

        return;
    }

    public VnfInstanceProcessResponse queryVnfInstanceProcess(String vnfInstanceId) {
        // check vnfinstanceid
        if (vnfInstanceId == null || vnfInstanceId.length() == 0) {
            logger.error("the query process request was checked out parameter error:the vnf instance id is wrong.");
            throw new NotFoundException("parameter error.the vnf instance id is wrong.");
        }

        // get vnfm url
        String vnfmBaseUrl = RocWrapper.getVnfmUrlByVnfInstanceOid(vnfInstanceId);
        if (vnfmBaseUrl == null || vnfmBaseUrl.length() == 0) {
            logger.error("the vnfm url which corresponded by the vnf id-{} is invalid.",
                    vnfInstanceId);
            throw new InternalServerErrorException("the vnfm url which corresponded by the vnf id-"
                    + vnfInstanceId + "is invalid.");
        }

        // send request
        VnfmServiceConsumer vnfmInstance = new VnfmServiceConsumer(vnfmBaseUrl);
        VnfInstanceProcessResponse vnfmReponse =
                vnfmInstance.queryVnfInstanceProcess(vnfInstanceId);

        return vnfmReponse;
    }

    public VnfInstanceProcessDetailResponse queryVnfInstanceProcessDetail(String vnfInstanceId) {
        // check vnfinstanceid
        if (vnfInstanceId == null || vnfInstanceId.length() == 0) {
            logger.error("the query process request was checked out parameter error:the vnf instance id is wrong.");
            throw new NotFoundException("parameter error.the vnf instance id is wrong.");
        }

        // get vnfm url
        String vnfmBaseUrl = RocWrapper.getVnfmUrlByVnfInstanceOid(vnfInstanceId);
        if (vnfmBaseUrl == null || vnfmBaseUrl.length() == 0) {
            logger.error("the vnfm url which corresponded by the vnf id-{} is invalid.",
                    vnfInstanceId);
            throw new InternalServerErrorException("the vnfm url which corresponded by the vnf id-"
                    + vnfInstanceId + "is invalid.");
        }

        // send request
        VnfmServiceConsumer vnfmInstance = new VnfmServiceConsumer(vnfmBaseUrl);
        VnfInstanceProcessDetailResponse vnfmReponse =
                vnfmInstance.queryVnfInstanceProcessDetail(vnfInstanceId);

        return vnfmReponse;
    }
}
