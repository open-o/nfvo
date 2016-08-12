/**
 *       Copyright (C) 2015 ZTE, Inc. and others. All rights reserved. (ZTE)
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

package com.zte.ums.sfc.console.resources;

import com.codahale.metrics.annotation.Timed;
import com.zte.ums.sfc.console.entity.PortPairGroupReq4N;
import com.zte.ums.sfc.console.entity.Result;
import com.zte.ums.sfc.console.entity.SdnControllerInfo;
import com.zte.ums.sfc.console.entity.portpair.PortPairReq4N;
import com.zte.ums.sfc.console.service.SdnServiceConsumer;
import com.zte.ums.sfc.console.wrapper.N2sReqWrapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class SfcDriverResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(SfcDriverResource.class);

    @GET
    @Path("/checksdncontroller")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Result checkSdnController(SdnControllerInfo sdnInfo)
            throws Exception {
        if (sdnInfo == null ) {
            throw new NotFoundException("SdnControllerInfo is null");
        }
        return SdnServiceConsumer.getSdnConProxy(sdnInfo.getUrl()).querySdnController();
    }

    @GET
    @Path("/createportpair")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public String createPortPair(PortPairReq4N portPairReq4N)
            throws Exception {
        if (portPairReq4N == null ) {
            throw new NotFoundException("SdnControllerInfo is null");
        }
        return SdnServiceConsumer.getSdnConProxy(portPairReq4N.getSdnControllerId()).createPortPair(N2sReqWrapper.convertPortPair(portPairReq4N));
    }

    @GET
    @Path("/createportpairgroup")
   @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public String createPortPairGroup(PortPairGroupReq4N portPairGroupReq4N)
            throws Exception {
        if (portPairGroupReq4N == null ) {
            throw new NotFoundException("SdnControllerInfo is null");
        }
        return SdnServiceConsumer.getSdnConProxy(portPairGroupReq4N.getUrl()).createPortPairGroup(
                N2sReqWrapper.convertPortPairGroup(portPairGroupReq4N));

    }
	
}
