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

import java.util.Date;

import org.junit.Test;

public class JujuVnfmInfoTest {

    JujuVnfmInfo jujuVnInfo = new JujuVnfmInfo();

    @Test
    public void testJujuVnfmInfo() {
        String appName = "appName";
        jujuVnInfo.setAppName(appName);
        Date createTime = new Date();
        jujuVnInfo.setCreateTime(createTime);
        Date deleteTime = new Date();
        jujuVnInfo.setDeleteTime(deleteTime);
        String extend = "extends";
        jujuVnInfo.setExtend(extend );
        String id = "id";
        jujuVnInfo.setId(id );
        String jobId = "jobId";
        jujuVnInfo.setJobId(jobId);
        Date modifyTime = new Date();
        jujuVnInfo.setModifyTime(modifyTime);
        Integer status = 1;
        jujuVnInfo.setStatus(status);
        String vnfId = "vnfId";
        jujuVnInfo.setVnfId(vnfId);
        String vnfmId = "vnfmId";
        jujuVnInfo.setVnfmId(vnfmId );
        assertEquals(jujuVnInfo.getAppName(), appName);
        assertEquals(jujuVnInfo.getCreateTime(), createTime);
        assertEquals(jujuVnInfo.getDeleteTime(), deleteTime);
        assertEquals(jujuVnInfo.getExtend(), extend);
        assertEquals(jujuVnInfo.getId(), id);
        assertEquals(jujuVnInfo.getJobId(), jobId);
        assertEquals(jujuVnInfo.getModifyTime(), modifyTime);
        assertEquals(jujuVnInfo.getStatus(), status);
        assertEquals(jujuVnInfo.getVnfId(), vnfId);
        assertEquals(jujuVnInfo.getVnfmId(), vnfmId);

    }
}
