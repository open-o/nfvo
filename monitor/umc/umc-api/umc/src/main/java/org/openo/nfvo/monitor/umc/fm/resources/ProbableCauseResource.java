/**
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
package org.openo.nfvo.monitor.umc.fm.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.openo.nfvo.monitor.umc.fm.common.FMConsts;
import org.openo.nfvo.monitor.umc.fm.db.entity.ProbableCause;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.ProbableCauseCond;
import org.openo.nfvo.monitor.umc.fm.resource.bean.request.ProbableCauseTreeRequest;
import org.openo.nfvo.monitor.umc.fm.resource.bean.response.ProbableCauseQueryResult;
import org.openo.nfvo.monitor.umc.fm.resource.bean.response.ProbableCauseTreeNode;
import org.openo.nfvo.monitor.umc.fm.util.CommonTools;
import org.openo.nfvo.monitor.umc.fm.util.JacksonJsonUtil;
import org.openo.nfvo.monitor.umc.fm.wrapper.ProbableCauseServiceWrapper;

import com.codahale.metrics.annotation.Timed;

/**
 * API for FM ProbableCause
 *
 */
@Path("/umcfm/v1")
@Api(tags = {"FM ProbableCause Interface"})
@Produces(MediaType.APPLICATION_JSON)
public class ProbableCauseResource {

    @GET
    @UnitOfWork
    @ApiOperation(value = "get all probableCause by systemType ", response = ProbableCause.class, responseContainer = "List")
    @Path("/probablecause")
    @Timed
    public ProbableCauseQueryResult[] listProbableCause(@Context HttpServletRequest request, @HeaderParam("language-option") String language) {
        ProbableCauseCond req =
                (ProbableCauseCond) JacksonJsonUtil.jsonToBean(
                        request.getParameter(FMConsts.REQUEST_PARAM_NAME), ProbableCauseCond.class, true);
        return new ProbableCauseServiceWrapper().getProbableCause(req, language);
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "get all probableCause by systemType ", response = ProbableCause.class, responseContainer = "List")
    @Path("/probablecausestree")
    @Timed
    public ProbableCauseTreeNode[] listProbableCauseTree(@Context HttpServletRequest request, @HeaderParam("language-option") String language)
            throws Exception {
        ProbableCauseTreeRequest req =
                (ProbableCauseTreeRequest) JacksonJsonUtil.jsonToBean(
                        request.getParameter(FMConsts.REQUEST_PARAM_NAME),
                        ProbableCauseTreeRequest.class, true);
        String parentId = req.getParentId();
        if (CommonTools.isTrimedEmptyString(parentId)) {
            return new ProbableCauseTreeNode[] {new ProbableCauseServiceWrapper().getRootNode(language)};
        }
        return new ProbableCauseServiceWrapper().getChildren(parentId, language);
    }

}
