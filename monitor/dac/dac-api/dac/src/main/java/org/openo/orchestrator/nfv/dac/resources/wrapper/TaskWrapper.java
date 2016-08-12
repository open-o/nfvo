/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.orchestrator.nfv.dac.resources.wrapper;

import org.openo.orchestrator.nfv.dac.common.bean.TaskBean;
import org.openo.orchestrator.nfv.dac.common.util.DacUtil;
import org.openo.orchestrator.nfv.dac.dataaq.common.DataAcquireException;
import org.openo.orchestrator.nfv.dac.resources.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

/**
 * Task rest interface processing class
 */
public class TaskWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskWrapper.class);

    /**
     * Create data acquisition task
     * @param taskBean task detail
     * @return status code 201(success) or 500(fail)
     */
    public static Response taskCreate(TaskBean taskBean) {
        LOGGER.info("Receive create task request.taskBean:" + DacUtil.convertBeanToJson(taskBean));
        try {
            TaskService.taskCreate(taskBean);
        } catch (DataAcquireException e) {
            LOGGER.error("create task fail.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Modify data acquisition task
     * @param taskId the task id
     * @param taskBean task detail
     * @return status code 201(success) or 500(fail)
     */
    public static Response taskModify(int taskId, TaskBean taskBean){
        LOGGER.info("Receive modify task request.taskId:" + taskId + " taskBean:" + DacUtil.convertBeanToJson(taskBean));
        try {
            TaskService.taskModify(taskId, taskBean);
        } catch (DataAcquireException e) {
            LOGGER.error("modify task fail.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Delete data acquisition task
     * @param taskId the task id
     * @return status code 204(success) or 500(fail)
     */
    public static Response taskDelete(int taskId) {
        LOGGER.info("Receive delete task request.taskId:" + taskId);
        try {
            TaskService.taskDelete(taskId);
        } catch (DataAcquireException e) {
            LOGGER.error("delete task fail.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
