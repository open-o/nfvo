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
package org.openo.orchestrator.nfv.umc.pm.bean;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detail information of performance measure task.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PmMeaTaskBean implements Comparable<Object> {
    private String id = null;
    private String name = null;
	private ResourceType resourceType;
    private PmMoType moType;
    private Resource [] resources;
    private int activeStatus;
    private int granularity = 900;
    private long beginTime = new Date().getTime();
    private long endTime = new Date().getTime();
    
    @Override
    public int compareTo(Object o) {
        return this.getId().compareTo(((PmMeaTaskBean) o).getId());
    }

}
