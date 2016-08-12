/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openo.orchestrator.nfv.umc.drill.resources.bean.response;

import org.openo.orchestrator.nfv.umc.drill.wrapper.common.TopologyConsts;

import lombok.Getter;

/**
 * @author 10188044
 * @date 2015-8-17
 *       <p/>
 *       the node representing system itself,use the name "root" temporarily
 */
public class RootNode extends NodeInformation {
    @Getter
    private final String rendertype = TopologyConsts.RENDERTYPE_ROOT;

    // call the constructor of parent class to set default value
    public RootNode() {
        super(TopologyConsts.NODE_ROOT_NAME, TopologyConsts.NODE_ROOT_NAME,
                TopologyConsts.NODE_ROOT_MOC, TopologyConsts.NODE_ROOT_MOC, 0);
    }
}
