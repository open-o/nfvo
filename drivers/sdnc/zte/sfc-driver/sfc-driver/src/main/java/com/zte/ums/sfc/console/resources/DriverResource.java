package com.zte.ums.sfc.console.resources;

import com.codahale.metrics.annotation.Timed;
import com.zte.ums.sfc.console.entity.*;
import com.zte.ums.sfc.console.entity.portpair.PortPairReq4N;
import com.zte.ums.sfc.console.entity.portpair.PortPairReq4S;
import com.zte.ums.sfc.console.service.SdnServiceConsumer;
import com.zte.ums.sfc.console.utils.SfcDriverUtil;
import com.zte.ums.sfc.console.wrapper.N2sReqWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created with IntelliJ IDEA.
 * User: 10084662
 * Date: 16-9-1
 * Time: ÏÂÎç7:16
 * To change this template use File | Settings | File Templates.
 */
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
        if (portPairReq4N == null ) {
            throw new NotFoundException("SdnControllerInfo is null");
        }
        PortPairReq4S portPairReq4S = N2sReqWrapper.convertPortPair(portPairReq4N);
        portPairReq4S.setUuid(SfcDriverUtil.generateUuid());
        SdnServiceConsumer.getSdnConProxy(portPairReq4N.getSdnControllerId()).createPortPair(portPairReq4S);
        return new Result(portPairReq4S.getUuid());
    }

    @POST
    @Path("/createportpairgroup")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Result createPortPairGroup(PortPairGroupReq4N portPairGroupReq4N)
            throws Exception {
        if (portPairGroupReq4N == null ) {
            throw new NotFoundException("SdnControllerInfo is null");
        }
        PortPairGroupReq4S portPairGroupReq4S = N2sReqWrapper.convertPortPairGroup(portPairGroupReq4N);
        portPairGroupReq4S.setUuid(SfcDriverUtil.generateUuid());
        SdnServiceConsumer.getSdnConProxy(portPairGroupReq4N.getUrl()).createPortPairGroup(
                portPairGroupReq4S);
        return new Result(portPairGroupReq4S.getUuid());

    }

    @POST
    @Path("/createflowclassfier")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Result createFlowClassfier(FlowClassfierReq4N flowClassfierReq4N)
            throws Exception {
        if (flowClassfierReq4N == null ) {
            throw new NotFoundException("FlowClassfierReq4N is null");
        }
        FlowClassfierReq4S flowClassfierReq4S = N2sReqWrapper.
                convertFlowClassfier(flowClassfierReq4N);
        flowClassfierReq4S.setUuid(SfcDriverUtil.generateUuid());
        SdnServiceConsumer.getSdnConProxy(flowClassfierReq4N.getUrl()).createFlowCla(flowClassfierReq4S);
        return new Result(flowClassfierReq4S.getUuid());
    }

    @POST
    @Path("/createportchain")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Result creatPortChain(PortChainReq4N portChainReq4N)
            throws Exception {
        if (portChainReq4N == null ) {
            throw new NotFoundException("PortChainReq4N is null");
        }
        PortChainReq4S portChainReq4S = N2sReqWrapper.converPortChain(portChainReq4N);
        portChainReq4S.setUuid(SfcDriverUtil.generateUuid());
        SdnServiceConsumer.getSdnConProxy(portChainReq4N.getUrl()).createPortChain(portChainReq4S);
        return new Result(portChainReq4S.getUuid());

    }

    @DELETE
    @Path("/delportpair")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public String delPortPair(DelReqInfo delReqInfo)
            throws Exception {
        if (delReqInfo == null ) {
            throw new NotFoundException("DelReqInfo is null");
        }
        return SdnServiceConsumer.getSdnConProxy(delReqInfo.getUrl()).deletePortPair(delReqInfo.getId());

    }

    @DELETE
    @Path("/delportpairgroup")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public String delPortPairGroup(DelReqInfo delReqInfo)
            throws Exception {
        if (delReqInfo == null ) {
            throw new NotFoundException("DelReqInfo is null");
        }
        return SdnServiceConsumer.getSdnConProxy(delReqInfo.getUrl()).deletePortPairGroup(delReqInfo.getId());

    }

    @DELETE
    @Path("/delclassifier")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public String delFlowClassfier(DelReqInfo delReqInfo)
            throws Exception {
        if (delReqInfo == null ) {
            throw new NotFoundException("DelReqInfo is null");
        }
        return SdnServiceConsumer.getSdnConProxy(delReqInfo.getUrl()).deleteFlowClassifiers(delReqInfo.getId());

    }

    @DELETE
    @Path("/delchain")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public String delPortChain(DelReqInfo delReqInfo)
            throws Exception {
        if (delReqInfo == null ) {
            throw new NotFoundException("DelReqInfo is null");
        }
        return SdnServiceConsumer.getSdnConProxy(delReqInfo.getUrl()).deletePortChain(delReqInfo.getId());

    }
}
