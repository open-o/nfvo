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
package org.openo.nfvo.monitor.umc.pm.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Detail information of performance history data querying condition.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PmQueryConditionBean {
    private Resource[] resources;

    private String resourceTypeId;

    private String moTypeId;

    private String[] counterOrIndexId;

    private int granularity = 900;

    private long beginTime;

    private long endTime;

    private int pageNo;

    private int pageSize;

}
