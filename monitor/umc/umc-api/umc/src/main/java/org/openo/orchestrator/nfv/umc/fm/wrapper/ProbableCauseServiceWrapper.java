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
package org.openo.orchestrator.nfv.umc.fm.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openo.orchestrator.nfv.umc.fm.adpt.resources.ResourceRestServiceProxy;
import org.openo.orchestrator.nfv.umc.fm.adpt.resources.bean.DBProbableCauseTree;
import org.openo.orchestrator.nfv.umc.fm.cache.FmCacheProcess;
import org.openo.orchestrator.nfv.umc.fm.db.entity.ProbableCause;
import org.openo.orchestrator.nfv.umc.fm.db.entity.SystemType;
import org.openo.orchestrator.nfv.umc.fm.db.entity.SystemTypeMocRelation;
import org.openo.orchestrator.nfv.umc.fm.db.process.FmDBProcess;
import org.openo.orchestrator.nfv.umc.fm.resource.bean.request.ProbableCauseCond;
import org.openo.orchestrator.nfv.umc.fm.resource.bean.response.ProbableCauseQueryResult;
import org.openo.orchestrator.nfv.umc.fm.resource.bean.response.ProbableCauseTreeNode;
import org.openo.orchestrator.nfv.umc.fm.util.BasicDataTypeConvertTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * Wrap query,insert,update,delete operation for ProbableCause
 */
public class ProbableCauseServiceWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProbableCauseServiceWrapper.class);
    private static ProbableCauseServiceWrapper instance = new ProbableCauseServiceWrapper();

    public synchronized static ProbableCauseServiceWrapper getInstance() {
        return instance;
    }

    public ProbableCauseQueryResult[] getProbableCause(ProbableCauseCond req, String language) {
        List<ProbableCauseQueryResult> result = queryByCond(req, language);
        return result.toArray(new ProbableCauseQueryResult[result.size()]);
    }

    public ProbableCauseTreeNode getRootNode(String language) throws Exception {
        ProbableCauseTreeNode probableCausesTreeResult = new ProbableCauseTreeNode();
        probableCausesTreeResult.setId("root");
        probableCausesTreeResult.setParentId("");
        probableCausesTreeResult.setDesc("i18n.fm.common.word.resource_type");
        probableCausesTreeResult.setType(ProbableCauseTreeNode.MOC);
        probableCausesTreeResult.setIconPath("");
        probableCausesTreeResult.setValue("root");
        probableCausesTreeResult.translate(language);
        return probableCausesTreeResult;
    }

    public ArrayList<ProbableCauseTreeNode> getAllTreeNodes() throws Exception {
        ArrayList<ProbableCauseTreeNode> treeNodes = new ArrayList<ProbableCauseTreeNode>();
        String tree = ResourceRestServiceProxy.getProbableCauseTree("moc");
        DBProbableCauseTree[] mocNodes = new Gson().fromJson(tree, DBProbableCauseTree[].class);
        String[] mocIds = new String[mocNodes.length];
        for (int i = 0; i < mocNodes.length; i++) {
            mocIds[i] = mocNodes[i].getParentId();
        }
        treeNodes.addAll(getMocNodes(mocNodes));
        HashMap<String, ArrayList<SystemType>> moc2systemTypes =
                constructMoc2SystemTypesMap(mocIds);
        treeNodes.addAll(getSystemTypeNodes(moc2systemTypes));
        treeNodes.removeAll(getInvalidNodes(treeNodes));
        treeNodes.removeAll(getMocNodesThoseNotContainSystemTypeChild(treeNodes));
        treeNodes.addAll(getProbableCauseNodes(moc2systemTypes));
        return treeNodes;
    }

    private ArrayList<ProbableCauseTreeNode> getMocNodes(DBProbableCauseTree[] mocNodes) throws Exception {
        ArrayList<ProbableCauseTreeNode> treeNodes = new ArrayList<ProbableCauseTreeNode>();
        String[] mocIds = new String[mocNodes.length];
        for (int i = 0; i < mocNodes.length; i++) {
            ProbableCauseTreeNode mocNode = new ProbableCauseTreeNode();
            mocNode.setId(mocNodes[i].getId());
            mocNode.setParentId(mocNodes[i].getParentId());
            mocNode.setDesc(mocNodes[i].getName());
            mocNode.setType(ProbableCauseTreeNode.MOC);
            mocNode.setIconPath("");
            mocNode.setValue(mocNodes[i].getId());
            mocIds[i] = mocNodes[i].getParentId();
            treeNodes.add(mocNode);
        }

        String productTree = ResourceRestServiceProxy.getProbableCauseTree("product");
        DBProbableCauseTree[] productNodes = new Gson().fromJson(productTree, DBProbableCauseTree[].class);
        for (int i = 0; i < productNodes.length; i++) {
            ProbableCauseTreeNode productNode = new ProbableCauseTreeNode();
            productNode.setId(productNodes[i].getId());
            productNode.setParentId(productNodes[i].getParentId());
            productNode.setDesc(productNodes[i].getName());
            productNode.setType(ProbableCauseTreeNode.MOC);
            productNode.setIconPath("");
            productNode.setValue(productNodes[i].getId());
            treeNodes.add(productNode);
        }
        return treeNodes;
    }

    public ProbableCauseTreeNode[] getChildren(String parentId, String language) throws Exception {
        ArrayList<ProbableCauseTreeNode> childrenList = new ArrayList<ProbableCauseTreeNode>();
        ArrayList<ProbableCauseTreeNode> treeNodes = new ArrayList<ProbableCauseTreeNode>();
        treeNodes = getAllTreeNodes();
        for (ProbableCauseTreeNode node : treeNodes) {
            if ((node.getParentId() != null) && (node.getParentId().equals(parentId))) {
                node.translate(language);
                childrenList.add(node);
            }
        }
        ProbableCauseTreeNode[] childrenArray = new ProbableCauseTreeNode[childrenList.size()];
        childrenList.toArray(childrenArray);
        return childrenArray;
    }

    private List<ProbableCauseQueryResult> queryByCond(ProbableCauseCond req, String language) {
        List<ProbableCause> probableCauses = FmCacheProcess.queryProbableCause(req);
        ArrayList<ProbableCauseQueryResult> resultList = new ArrayList<ProbableCauseQueryResult>();
        for (ProbableCause probableCause : probableCauses) {
            resultList.add(convert2ProbableCauseQueryResult(probableCause, language));
        }
        return resultList;
    }

    private ProbableCauseQueryResult convert2ProbableCauseQueryResult(ProbableCause probableCause, String language) {
        ProbableCauseQueryResult queryResult = new ProbableCauseQueryResult();
        String systemTypeName = FmCacheProcess.querySystemType(probableCause.getSystemType()).getName();
        queryResult.setCode(probableCause.getCode());
        queryResult.setCodeName(probableCause.getCodeName());
        //queryResult.setCustomSeverity(probableCause.getUseraiseAlarmSeverity());
        queryResult.setDefaultSeverity(probableCause.getPerceivedSeverity());
        queryResult.setSystemType(probableCause.getSystemType());
        queryResult.setSystemTypeName(systemTypeName);
        queryResult.translate(language);
        return queryResult;
    }

    private ArrayList<ProbableCauseTreeNode> getProbableCauseNodes(
            HashMap<String, ArrayList<SystemType>> moc2systemTypes) {
        ArrayList<ProbableCauseTreeNode> probableCauseNodes =
                new ArrayList<ProbableCauseTreeNode>();
        for (String mocId : moc2systemTypes.keySet()) {
            ArrayList<SystemType> systemTypes = moc2systemTypes.get(mocId);
            for (SystemType systemType : systemTypes) {
                probableCauseNodes.addAll(getAllProbableCauseNodesOfOneSystemType(systemType));
            }
        }
        return probableCauseNodes;
    }

    private ArrayList<ProbableCauseTreeNode> getAllProbableCauseNodesOfOneSystemType(
            SystemType systemType) {
        ArrayList<ProbableCauseTreeNode> probableCauseNodes =
                new ArrayList<ProbableCauseTreeNode>();
        List<ProbableCause> probableCauseList = FmCacheProcess.queryProbableCause(systemType.getSystemType());
        ProbableCause[] probableCauses =
                probableCauseList.toArray(new ProbableCause[probableCauseList.size()]);
        for (ProbableCause probableCause : probableCauses) {
            probableCauseNodes.add(constructProbableCauseNode(systemType, probableCause));
        }
        return probableCauseNodes;
    }

    private ProbableCauseTreeNode constructProbableCauseNode(SystemType systemType,
            ProbableCause probableCause) {
        ProbableCauseTreeNode node = new ProbableCauseTreeNode();
        node.setId(String.valueOf(+probableCause.getCode()));
        node.setValue(String.valueOf(probableCause.getCode()));
        node.setParentId(String.valueOf(systemType.getSystemType()));
        node.setType(ProbableCauseTreeNode.PROBABLECAUSE);
        node.setDesc(probableCause.getCodeName());
        return node;
    }

    private ArrayList<ProbableCauseTreeNode> getSystemTypeNodes(
            HashMap<String, ArrayList<SystemType>> moc2systemTypes) {
        ArrayList<ProbableCauseTreeNode> systemTypeNodes = new ArrayList<ProbableCauseTreeNode>();
        for (String mocId : moc2systemTypes.keySet()) {
            ArrayList<SystemType> systemTypes = moc2systemTypes.get(mocId);
            for (SystemType systemType : systemTypes) {
                ProbableCauseTreeNode systemTypeNode = constructSystemTypeNode(mocId, systemType);
                systemTypeNodes.add(systemTypeNode);
            }
        }
        return systemTypeNodes;
    }

    private ProbableCauseTreeNode constructSystemTypeNode(String mocId, SystemType systemType) {
        ProbableCauseTreeNode systemTypeNode = new ProbableCauseTreeNode();
        systemTypeNode.setId(String.valueOf(systemType.getSystemType()));
        systemTypeNode.setParentId(mocId);
        systemTypeNode.setType(ProbableCauseTreeNode.SYSTEMTYPE);
        systemTypeNode.setDesc(systemType.getName());
        systemTypeNode.setValue(String.valueOf(systemType.getSystemType()));
        return systemTypeNode;
    }

    private HashMap<String, ArrayList<SystemType>> constructMoc2SystemTypesMap(String[] mocIds) {
        HashMap<String, ArrayList<SystemType>> moc2systemTypes =
                new HashMap<String, ArrayList<SystemType>>();
        for (String moc : mocIds) {
            ArrayList<SystemType> systemTypeList = new ArrayList<SystemType>();
            String[] mocArray = new String[1];
            mocArray[0] = moc;
            List<SystemTypeMocRelation> relationList = FmCacheProcess.querySystemTypeByMoc(mocArray);
            SystemTypeMocRelation[] relations =
                    relationList.toArray(new SystemTypeMocRelation[relationList.size()]);
            int[] systemTypes = new int[relationList.size()];
            for (int i = 0; i < relations.length; i++) {
                systemTypes[i] = relations[i].getSystemType();
            }
            systemTypeList = BasicDataTypeConvertTool.list2ArrayList(FmCacheProcess.querySystemTypes(systemTypes));
            moc2systemTypes.put(moc, systemTypeList);
        }
        return moc2systemTypes;
    }

    private ArrayList<ProbableCauseTreeNode> getInvalidNodes(
            ArrayList<ProbableCauseTreeNode> treeNodes) {
        ArrayList<ProbableCauseTreeNode> invalidNodes = new ArrayList<ProbableCauseTreeNode>();
        for (ProbableCauseTreeNode node : treeNodes) {
            if (!isValidNode(node)) {
                invalidNodes.add(node);
            }
        }
        return invalidNodes;
    }

    private boolean isValidNode(ProbableCauseTreeNode node) {
        if (node.getId() == null) {
            return false;
        }
        if (node.getParentId() == null && !node.getId().equals("root")) {
            return false;
        }
        return true;
    }

    private ArrayList<ProbableCauseTreeNode> getMocNodesThoseNotContainSystemTypeChild(
            ArrayList<ProbableCauseTreeNode> treeNodes) {
        ArrayList<ProbableCauseTreeNode> mocNodesThoseNotContainSystemTypeChild =
                new ArrayList<ProbableCauseTreeNode>();
        for (ProbableCauseTreeNode node : treeNodes) {
            if (node.getType() == ProbableCauseTreeNode.MOC
                    && (!isContainSystemTypeChildNode(node, treeNodes))) {
                mocNodesThoseNotContainSystemTypeChild.add(node);
            }
        }
        return mocNodesThoseNotContainSystemTypeChild;
    }

    private boolean isContainSystemTypeChildNode(ProbableCauseTreeNode node,
            ArrayList<ProbableCauseTreeNode> treeNodes) {
        for (ProbableCauseTreeNode temp : treeNodes) {
            if (temp.isChildOf(node)) {
                if ((temp.getType() == ProbableCauseTreeNode.SYSTEMTYPE)
                        || (isContainSystemTypeChildNode(temp, treeNodes))) {
                    return true;
                }
            }
        }
        return false;
    }

}
