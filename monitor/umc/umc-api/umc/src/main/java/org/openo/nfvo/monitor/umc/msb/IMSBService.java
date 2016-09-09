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
package org.openo.nfvo.monitor.umc.msb;

import io.swagger.annotations.ApiParam;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.openo.nfvo.monitor.umc.msb.bean.MsbRegisterBean;

@Path("/openoapi/microservices/v1/services")
public interface IMSBService {
    @POST
    @Path("")
    public String registerService(@ApiParam(value = "service register info") MsbRegisterBean registerInfo);

    @DELETE
    @Path("/{serviceName}/version/{version}/nodes/{ip}/{port}")
    public void unRegisterService(@PathParam("serviceName") String serviceName,@PathParam("version") String version,@PathParam("ip") String ip,@PathParam("port") String port);

    
}
