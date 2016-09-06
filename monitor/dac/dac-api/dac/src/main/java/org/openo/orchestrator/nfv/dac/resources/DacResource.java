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
package org.openo.orchestrator.nfv.dac.resources;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.openo.orchestrator.nfv.dac.resources.wrapper.DacWrapper;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * rest interface definition of DAC register/unregister
 */
@Path("/dacs")
@Api(tags = {" DacResource "})
public class DacResource {
    @POST
    @Path("/")
    @ApiOperation(value = "register dac")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Response registerDAC() {
        return DacWrapper.registerDAC();
    }

    @DELETE
    @Path("/")
    @ApiOperation(value = "unregister dac")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    public Response unregisterDAC() {
        return DacWrapper.unregisterDAC();
    }
}
