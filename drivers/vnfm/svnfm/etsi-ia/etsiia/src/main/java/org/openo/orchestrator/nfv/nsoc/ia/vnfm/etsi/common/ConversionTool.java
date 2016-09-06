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
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.common;

import java.util.ArrayList;
import java.util.List;

import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.InstantiateRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.OperateRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.ScaleRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.TerminalRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.changedresource.CPChangedResource;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.changedresource.ChangedResource;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.changedresource.IpObj;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.changedresource.VduChangedResource;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.changedresource.VnfChangedResource;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.entity.process.OperationExecutionProgress;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.roc.VDUBaseInfo;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.CpInfoFromVnfm;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.InstantiateVnfRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.InstantiateVnfResponse;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.OperateVnfRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.ScaleVnfRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.TerminalVnfRequest;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.VduInfoFromVnfm;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm.VnfInfoFromVnfm;
import org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.wrapper.roc.RocWrapper;

public class ConversionTool {
    public static InstantiateVnfRequest convertInstantiateVnfRequest(InstantiateRequest sourceRequst) {
        InstantiateVnfRequest request = new InstantiateVnfRequest();
        request.setAdditionalParam(sourceRequst.getAdditionalParam());
        request.setFlavorId(sourceRequst.getFlavorId());
        request.setVnfDescriptorId(sourceRequst.getVnfDescriptorId());
        request.setVnfInstanceName(sourceRequst.getVnfInstanceName());
        request.setVnfPackageId(sourceRequst.getVnfPackageId());
        return request;
    }


    public static OperateVnfRequest convertOperateVnfRequest(OperateRequest sourceRequest) {
        OperateVnfRequest request = new OperateVnfRequest();
        request.setOperationtype(sourceRequest.getOperationtype());
        return request;
    }

    public static ScaleVnfRequest convertScaleVnfRequest(ScaleRequest sourceRequest) {
        ScaleVnfRequest request = new ScaleVnfRequest();
        request.setAdditionalParam(sourceRequest.getAdditionalParam());
        request.setType(sourceRequest.getType());
        request.setAspect(sourceRequest.getAspect());
        return request;
    }

    public static TerminalVnfRequest convertTerminalVnfRequest(TerminalRequest sourceRequest) {
        TerminalVnfRequest request = new TerminalVnfRequest();
        request.setGracefulTerminationTimeout(sourceRequest.getGracefulTerminationTimeout());
        request.setTerminationType(sourceRequest.getTerminationType());
        return request;
    }

    public static ChangedResource convertFromVnfInfoFromVnfmToChangedResource(String vnfInstanceId,
            VnfInfoFromVnfm sourceVnfInfo, int operation) {
        if (operation == 2) {
            return extractDeleteChangedResource(vnfInstanceId, sourceVnfInfo, operation);
        }

        ChangedResource cr = new ChangedResource();
        // vnf
        List<VnfChangedResource> vnfList = new ArrayList<VnfChangedResource>();
        VnfChangedResource vnf = new VnfChangedResource();
        vnf.setNodeTemplateId(sourceVnfInfo.getVnfDescriptorId());
        vnf.setType("VNF");
        vnf.setVnfInstanceId(vnfInstanceId);
        vnf.setVnfName(sourceVnfInfo.getVnfInstanceName());
        vnf.setVnfmInstanceId(sourceVnfInfo.getVnfmID());
        vnfList.add(vnf);
        cr.setVnfList(vnfList);

        // vdu
        List<VduChangedResource> vduList = new ArrayList<VduChangedResource>();
        // cp
        List<CPChangedResource> cpList = new ArrayList<CPChangedResource>();

        for (int i = 0; sourceVnfInfo.getVduList() != null && i < sourceVnfInfo.getVduList().size(); i++) {
            if (sourceVnfInfo.getVduList().get(i) == null) {
                continue;
            }

            VduInfoFromVnfm vduInVnfm = sourceVnfInfo.getVduList().get(i);
            VduChangedResource vdu = new VduChangedResource();
            vdu.setHostName(vduInVnfm.getHost());
            // vdu.setIpAddresses(vduInVnfm.getAddresses());
            vdu.setNodeTemplateId(vduInVnfm.getNodeTemplateId());
            vdu.setType("VDU");
            vdu.setVduInstanceId(vduInVnfm.getVduId());
            vdu.setVduName(vduInVnfm.getVduName());
            vdu.setVimInstanceId(vduInVnfm.getVimId());
            vdu.setVmId(vduInVnfm.getVimId());
            vdu.setVnfInstanceId(vnfInstanceId);

            List<IpObj> ipAddresses = new ArrayList<IpObj>();

            if (vduInVnfm.getAddresses() != null && vduInVnfm.getAddresses().size() > 0) {
                for (int j = 0; j < vduInVnfm.getAddresses().size(); j++) {
                    if (vduInVnfm.getAddresses().get(j) == null) {
                        continue;
                    }

                    CpInfoFromVnfm cpInVnfm = vduInVnfm.getAddresses().get(j);

                    // IP
                    IpObj ip = new IpObj();
                    ip.setFloatingIP(cpInVnfm.getAddress() != null ? cpInVnfm.getAddress()
                            .getAddr() : "");
                    ipAddresses.add(ip);

                    // cp
                    /*
                     * CPChangedResource cp = new CPChangedResource();
                     * //cp.setCpInstanceId(cpInVnfm.get); cp.setCpName(cpInVnfm.getCpName());
                     * cp.setFloatingIP
                     * (cpInVnfm.getAddress()!=null?cpInVnfm.getAddress().getAddr():"");
                     * cp.setIpAddress
                     * (cpInVnfm.getAddress()!=null?cpInVnfm.getAddress().getAddr():"");
                     * cp.setNodeTemplateId(cpInVnfm.getNodeTemplateId()); cp.setType("CP");
                     * cp.setVduInstanceId(vduInVnfm.getVduId());
                     * cp.setVlInstanceId(cpInVnfm.getVlId()); cp.setVnfInstanceId(vnfInstanceId);
                     *
                     * cpList.add(cp);
                     */
                }
            }

            vdu.setIpAddresses(ipAddresses);
            vduList.add(vdu);
        }

        cr.setVduList(vduList);
        cr.setCpList(cpList);

        // set event type
        cr.setEventType(operation);

        return cr;
    }

    public static ChangedResource extractDeleteChangedResource(String vnfInstanceId,
            VnfInfoFromVnfm sourceVnfInfo, int operation) {
        ChangedResource cr = new ChangedResource();

        // vdu
        {
            List<VduChangedResource> vduList = new ArrayList<VduChangedResource>();
            VDUBaseInfo[] vdus = RocWrapper.getVduResources(vnfInstanceId);
            for (int i = 0; vdus != null && i < vdus.length; i++) {
                boolean find = false;
                for (int j = 0; sourceVnfInfo.getVduList() != null
                        && j < sourceVnfInfo.getVduList().size(); j++) {
                    if (vdus[i].getOid().equals(sourceVnfInfo.getVduList().get(j).getVduId())) {
                        find = true;
                        break;
                    }
                }

                if (!find) {
                    VduChangedResource vduChange = new VduChangedResource();
                    vduChange.setVduInstanceId(vdus[i].getOid());
                    vduList.add(vduChange);
                }
            }

            cr.setVduList(vduList);
        }

        // vnf
        {
            List<VnfChangedResource> vnfList = new ArrayList<VnfChangedResource>();
            if (sourceVnfInfo.getVduList() != null && sourceVnfInfo.getVduList().size() == 0) {
                VnfChangedResource vnfChange = new VnfChangedResource();
                vnfChange.setVnfInstanceId(vnfInstanceId);
            }

            cr.setVnfList(vnfList);
        }

        // set event type
        cr.setEventType(operation);

        return cr;
    }

    public static ChangedResource assembleChangedResource(InstantiateRequest request,
            InstantiateVnfResponse response) {
        ChangedResource cr = new ChangedResource();
        // vnf
        List<VnfChangedResource> vnfList = new ArrayList<VnfChangedResource>();
        VnfChangedResource vnf = new VnfChangedResource();
        vnf.setNodeTemplateId(request.getVnfDescriptorId());
        vnf.setType("VNF");
        vnf.setVnfInstanceId(response.getVnfInstanceId());
        vnf.setVnfName(request.getVnfInstanceName());
        vnf.setVnfmInstanceId(request.getVnfmId());
        vnfList.add(vnf);
        cr.setVnfList(vnfList);

        return cr;
    }

    public static OperationExecutionProgress assembleOperationExecutionProgress() {
        OperationExecutionProgress oep = new OperationExecutionProgress();
        return oep;
    }

    public static String convertProgressStatus(String source) {

        if (source != null && source.trim().equals("success")) {
            return "end";
        }

        if (source != null && source.trim().equals("failure")) {
            return "failed";
        }

        return source;
    }
}
