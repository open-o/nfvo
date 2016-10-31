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
package org.openo.nfvo.monitor.umc.dac;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.openo.nfvo.monitor.umc.dac.bean.DacBean;
import org.openo.nfvo.monitor.umc.monitor.wrapper.DACServiceWrapper;

import com.codahale.metrics.annotation.Timed;

@Path("")
@Api(tags = {"dac notification Interface"})
@Produces(MediaType.APPLICATION_JSON)
public class DacResource {
    @POST
    @Path("/dacinfo")
    @ApiOperation(value = "receive dac notification ")
    @ApiResponses(value = {@ApiResponse(code = 500, message = " receive resource error ")})
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public void  receiveNotification(
            @ApiParam(value = "dac Info", required = true) DacBean dacBean) {
    	DACServiceWrapper.getInstance().receiveDacNotification(dacBean.getIp(), dacBean.getLabelIndex());
    }

}
