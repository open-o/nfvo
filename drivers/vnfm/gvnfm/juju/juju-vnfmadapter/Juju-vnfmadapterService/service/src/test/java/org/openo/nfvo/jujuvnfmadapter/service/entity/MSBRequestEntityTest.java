/*
 * Copyright 2016 Huawei Technologies Co., Ltd.
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

package org.openo.nfvo.jujuvnfmadapter.service.entity;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openo.nfvo.jujuvnfmadapter.service.entity.MSBRequestEntity.Node;

public class MSBRequestEntityTest {
    MSBRequestEntity msbRequestEntity = new MSBRequestEntity();

    @Test
    public void testMSBRequestEntity() {
        List<Node> nodes = new ArrayList<>();
        msbRequestEntity.setNodes(nodes);
        String protocol = "protocol";
        msbRequestEntity.setProtocol(protocol);
        String serviceName = "serviceName";
        msbRequestEntity.setServiceName(serviceName);
        String status = "status";
        msbRequestEntity.setStatus(status);
        String url = "url";
        msbRequestEntity.setUrl(url);
        String version = "version";
        msbRequestEntity.setVersion(version);
        String visualRange = " visualRange";
        msbRequestEntity.setVisualRange(visualRange);
        msbRequestEntity.toString();
        assertEquals(msbRequestEntity.getNodes(), nodes);;
        assertEquals(msbRequestEntity.getNodes(), nodes);
        assertEquals(msbRequestEntity.getProtocol(), protocol);
        assertEquals(msbRequestEntity.getServiceName(), serviceName);
        assertEquals(msbRequestEntity.getStatus(), status);
        assertEquals(msbRequestEntity.getUrl(), url);
        assertEquals(msbRequestEntity.getVersion(), version);
        assertEquals(msbRequestEntity.getVisualRange(), visualRange);
    }
}
