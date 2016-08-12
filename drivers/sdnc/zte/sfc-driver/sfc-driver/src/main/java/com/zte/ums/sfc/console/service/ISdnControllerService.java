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

package com.zte.ums.sfc.console.service;

import com.zte.ums.sfc.console.entity.PortPairGroupReq4S;
import com.zte.ums.sfc.console.entity.Result;
import com.zte.ums.sfc.console.entity.portpair.PortPairReq4S;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/restconf/config/neutron:neutron/")
public interface ISdnControllerService {

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Result querySdnController() throws Exception;

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String createPortPair(PortPairReq4S portPairReq4S) throws Exception;

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String createPortPairGroup(PortPairGroupReq4S ppg4S) throws Exception;

}
