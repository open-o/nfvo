/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
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
package org.openo.orchestrator.nfv.umc.pm.adpt.roc.service.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.openo.orchestrator.nfv.umc.pm.adpt.roc.entity.ResourceResponse;
import org.openo.orchestrator.nfv.umc.pm.adpt.roc.service.IPmResourceRestService;
import org.openo.orchestrator.nfv.umc.pm.common.RestRequestException;

@Path("/api/roc/v1/resource/vdus")
public interface IVduRestServiceResource extends IPmResourceRestService{

	@Path("/{vdu_id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public String getResourceList(@PathParam("vdu_id") String vduId) throws RestRequestException;


	@Path("/")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResourceResponse queryAllResource() throws RestRequestException;

}
