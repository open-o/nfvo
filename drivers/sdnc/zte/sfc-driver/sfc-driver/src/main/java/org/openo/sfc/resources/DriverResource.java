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

import com.codahale.metrics.annotation.Timed;
import org.openo.sfc.entity.DelReqInfo;
import org.openo.sfc.entity.FlowClassfierReq4N;
import org.openo.sfc.entity.FlowClassifierReq4S;
import org.openo.sfc.entity.PortChainReq4N;
import org.openo.sfc.entity.PortChainReq4S;
import org.openo.sfc.entity.PortPairGroupReq4N;
import org.openo.sfc.entity.PortPairGroupReq4S;
import org.openo.sfc.entity.Result;
import org.openo.sfc.entity.portpair.PortPairReq4N;
import org.openo.sfc.entity.portpair.PortPairReq4S;
import org.openo.sfc.service.SdnServiceConsumer;
import org.openo.sfc.utils.SfcDriverUtil;
import org.openo.sfc.wrapper.N2sReqWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.DELETE;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class DriverResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverResource.class);
//
//    @POST
//    @Path("/checksdncontroller")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Timed
//    public Result checkSdnController(SdnControllerInfo sdnInfo)
//            throws Exception {
//        if (sdnInfo == null ) {
//            throw new NotFoundException("SdnControllerInfo is null");
//        }
//        return SdnServiceConsumer.getSdnConProxy(sdnInfo.getUrl()).querySdnController();
//    }

    @POST
    @Path("/createportpair")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Result createPortPair(PortPairReq4N portPairReq4N)
            throws Exception {
        if (portPairReq4N == null) {
            throw new NotFoundException("SdnControllerInfo is null");
        }
        LOGGER.info(SfcDriverUtil.toJson(portPairReq4N));
        PortPairReq4S portPairReq = N2sReqWrapper.convertPortPair(portPairReq4N);
        String portPairReqJson = SfcDriverUtil.toJson(portPairReq);
        LOGGER.info(portPairReqJson);
        SdnServiceConsumer.getSdnConProxy(portPairReq4N.getUrl()).createPortPair(SfcDriverUtil.generateAuthorization(), portPairReqJson);
//
//        if(rsp.getStatus() == SfcConst.HTTP_POST_OK)
//        {
//            return new Result(portPairReq.getPortPair().get(0).getUuid());
//        }
//        return rsp;

        return new Result(portPairReq.getPortPair().get(0).getUuid());
    }

    @POST
    @Path("/createportpairgroup")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Result createPortPairGroup(PortPairGroupReq4N portPairGroupReq4N)
            throws Exception {
        if (portPairGroupReq4N == null) {
            throw new NotFoundException("SdnControllerInfo is null");
        }
        LOGGER.info(SfcDriverUtil.toJson(portPairGroupReq4N));
        PortPairGroupReq4S portPairGroupReq4S = N2sReqWrapper.convertPortPairGroup(portPairGroupReq4N);
        String portPairGroupReqJson = SfcDriverUtil.toJson(portPairGroupReq4S);
        LOGGER.info(portPairGroupReqJson);
        SdnServiceConsumer.getSdnConProxy(portPairGroupReq4N.getUrl()).createPortPairGroup(
                SfcDriverUtil.generateAuthorization(), portPairGroupReqJson);
//        if(rsp.getStatus() == SfcConst.HTTP_POST_OK)
//        {
//            return new Result(portPairGroupReq4S.getPortPairGroupReqs().get(0).getUuid());
//        }
//
//        return rsp;

        return new Result(portPairGroupReq4S.getPortPairGroupReqs().get(0).getUuid());

    }

    @POST
    @Path("/createflowclassfier")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Result createFlowClassfier(FlowClassfierReq4N flowClassfierReq4N)
            throws Exception {
        if (flowClassfierReq4N == null) {
            throw new NotFoundException("FlowClassfierReq4N is null");
        }
        LOGGER.info(SfcDriverUtil.toJson(flowClassfierReq4N));
        FlowClassifierReq4S flowClassfierReq4S = N2sReqWrapper.
                convertFlowClassfier(flowClassfierReq4N);
        String uuid = flowClassfierReq4S.getSfcFlowClassifier().get(0).getUuid();
        String flowClassifierJson = SfcDriverUtil.toJson(flowClassfierReq4S);
        LOGGER.info(flowClassifierJson);
        SdnServiceConsumer.getSdnConProxy(flowClassfierReq4N.getUrl()).createFlowCla(SfcDriverUtil.generateAuthorization(), flowClassifierJson);
//        if(rsp.getStatus() == SfcConst.HTTP_POST_OK)
//        {
//            return new Result(uuid);
//        }
//        else
//            return rsp;

        return new Result(uuid);
    }

    @POST
    @Path("/createportchain")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Result creatPortChain(PortChainReq4N portChainReq4N)
            throws Exception {
        if (portChainReq4N == null) {
            throw new NotFoundException("PortChainReq4N is null");
        }
        LOGGER.info(SfcDriverUtil.toJson(portChainReq4N));
        PortChainReq4S portChainReq = N2sReqWrapper.converPortChain(portChainReq4N);
        String portChainReqJson = SfcDriverUtil.toJson(portChainReq);
        LOGGER.info(portChainReqJson);
        SdnServiceConsumer.getSdnConProxy(portChainReq4N.getUrl()).createPortChain(
                SfcDriverUtil.generateAuthorization(), portChainReqJson);
//        if(rsp.getStatus() == SfcConst.HTTP_POST_OK)
//        {
//            return new Result(portChainReq.getPortChainReqs().get(0).getUuid());
//        }
//        return rsp;

        return new Result(portChainReq.getPortChainReqs().get(0).getUuid());

    }

    @DELETE
    @Path("/delportpair")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public void delPortPair(DelReqInfo delReqInfo)
            throws Exception {
        if (delReqInfo == null) {
            throw new NotFoundException("DelReqInfo is null");
        }
        LOGGER.info(SfcDriverUtil.toJson(delReqInfo));
        SdnServiceConsumer.getSdnConProxy(delReqInfo.getUrl()).deletePortPair(SfcDriverUtil.generateAuthorization(), delReqInfo.getId());

    }

    @DELETE
    @Path("/delportpairgroup")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public void delPortPairGroup(DelReqInfo delReqInfo)
            throws Exception {
        if (delReqInfo == null) {
            throw new NotFoundException("DelReqInfo is null");
        }
        LOGGER.info(SfcDriverUtil.toJson(delReqInfo));
        SdnServiceConsumer.getSdnConProxy(delReqInfo.getUrl()).deletePortPairGroup(SfcDriverUtil.generateAuthorization(), delReqInfo.getId());

    }

    @DELETE
    @Path("/delclassifier")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public void delFlowClassfier(DelReqInfo delReqInfo)
            throws Exception {
        if (delReqInfo == null) {
            throw new NotFoundException("DelReqInfo is null");
        }
        LOGGER.info(SfcDriverUtil.toJson(delReqInfo));
        SdnServiceConsumer.getSdnConProxy(delReqInfo.getUrl()).deleteFlowClassifiers(SfcDriverUtil.generateAuthorization(), delReqInfo.getId());

    }

    @DELETE
    @Path("/delchain")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public void delPortChain(DelReqInfo delReqInfo)
            throws Exception {
        if (delReqInfo == null) {
            throw new NotFoundException("DelReqInfo is null");
        }
        LOGGER.info(SfcDriverUtil.toJson(delReqInfo));
        SdnServiceConsumer.getSdnConProxy(delReqInfo.getUrl()).deletePortChain(SfcDriverUtil.generateAuthorization(), delReqInfo.getId());

    }
}
