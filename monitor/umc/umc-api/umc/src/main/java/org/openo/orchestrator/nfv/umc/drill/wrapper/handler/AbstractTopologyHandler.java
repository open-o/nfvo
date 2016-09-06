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
package org.openo.orchestrator.nfv.umc.drill.wrapper.handler;

import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.NodeInformation;
import org.openo.orchestrator.nfv.umc.drill.resources.bean.response.TopologyResp;
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.TopologyConsts;
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.exception.TopologyException;
import org.openo.orchestrator.nfv.umc.drill.wrapper.common.util.ArrayUtils;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.NfvoData;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.response.RestQueryListReturnMsg;
import org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean.response.RestReturnMsg;
import org.openo.orchestrator.nfv.umc.fm.wrapper.CurrentAlarmServiceWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *       the abstract handler that defines the template of all the concrete handler
 */
public abstract class AbstractTopologyHandler {
    // logger name is set at run time,using the name of sub class
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * the template method defines the sequence of processing request
     *
     */
    public TopologyResp handleTopologyReq(String resourceid) {
        TopologyResp topologyResp = new TopologyResp();
        try {
            NodeInformation currentNodeDetail = queryCurrentNode(resourceid);
            NodeInformation[] parents = queryParentNodes(currentNodeDetail);
            NodeInformation[] childs = queryChildNodes(currentNodeDetail);
            // construct the response using the infos above
            topologyResp.setSelf(currentNodeDetail);
            topologyResp.setParents(parents);
            topologyResp.setChilds(childs);
            // add the success flag identifying the operation result
            topologyResp.setOperationresult(TopologyConsts.OPERATIONS_RESULT_SUCCESS);
        } catch (TopologyException e) {
            // add the fail flag identifying the operation result
            topologyResp.setOperationresult(TopologyConsts.OPERATIONS_RESULT_FAIL);
            // add the detailed error info
            topologyResp.setErrorinfo(e.getErrorCode() + e.getMessage());
        }
        return topologyResp;
    }

    protected int queryAlarmCount(String resourceid) {
        return new CurrentAlarmServiceWrapper().queryCurAlarmsCount(resourceid);
    }

    /**
     * the assemble array data util convert the NfvoData[](resource service returns) to
     * NodeInformation[](node infos used on the package)
     *
     * @param restResult the result of calling the roc services
     * @param sourceType the class type of the restResult
     * @param targetType the target class type of the array
     * @return
     * @throws TopologyException
     */
    protected <S extends NfvoData, T extends NodeInformation> T[] assembleData(
            RestQueryListReturnMsg<S> restResult, Class<S> sourceType, Class<T> targetType)
            throws TopologyException {
        S[] source = restResult.getData();
        T[] result = null;
        try {
            result = ArrayUtils.convertArrayType(source, sourceType, targetType);
        } catch (Exception e) {
            LOGGER.error("assemble result data from restresult throw exception!", e);
            // conver to the business exception
            throw new TopologyException(TopologyException.ERR_MONITOR_TOPOLOGY_ASSEMBLEDATAFAIL,
                    "assemble result data from restresult throw exception!Cause:" + e.getMessage());
        }
        return result;
    }

    /**
     * check whether calling resource service succeed,throw exception directly if failed
     *
     * @param restResult
     * @throws TopologyException
     *
     */
    protected void checkRestserviceResult(RestReturnMsg restResult) throws TopologyException {
        if (null == restResult
                || !(TopologyConsts.OPERATIONS_RESULT_SUCCESS.equals(restResult
                        .getOperationResult()))) {
            LOGGER.warn("call resource service fail!Cause:" + restResult.getException());
            // conver to the business exception and throw
            throw new TopologyException(TopologyException.ERR_MONITOR_TOPOLOGY_CALLSERVICEFAULT,
                    "call resource service fail!Cause:" + restResult.getException());
        }
    }

    /**
     * process the exception throwed when calling the back roc services
     *
     * @param e
     * @throws TopologyException
     */
    protected void handleRestserviceException(Exception e) throws TopologyException {
        LOGGER.error("call resource service Error!throw exception!", e);
        // conver to the business exception and throw
        throw new TopologyException(TopologyException.ERR_MONITOR_TOPOLOGY_CALLSERVICEEXCEPTION,
                "call resource service throw exception!Cause:" + e.getMessage());
    }

    /**
     * abstract method,query the node info identified by id
     *
     * @param resourceid
     * @return
     * @throws TopologyException
     */
    public abstract NodeInformation queryCurrentNode(String resourceid) throws TopologyException;

    /**
     * abstract method,query the parent nodes of the current node
     *
     * @param currentNodeDetail
     * @return
     * @throws TopologyException
     */
    public abstract NodeInformation[] queryParentNodes(NodeInformation currentNodeDetail)
            throws TopologyException;

    /**
     * abstract method,query the child nodes of the current node
     *
     * @param currentNodeDetail
     * @return
     * @throws TopologyException
     */
    public abstract NodeInformation[] queryChildNodes(NodeInformation currentNodeDetail)
            throws TopologyException;

}
