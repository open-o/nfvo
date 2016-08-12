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
package org.openo.orchestrator.nfv.umc.fm.resource.bean.request;

/**
 * request of query current alarm
 *
 */

public class CurAlarmQueryRequest implements IJsonRequest {

    private int pageSize = -1;

    private int pageNumber = -1;

    private CurAlarmQueryCond condition = null;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public CurAlarmQueryCond getCondition() {
        return condition;
    }

    public void setCondition(CurAlarmQueryCond condition) {
        this.condition = condition;
    }

    @Override
    public boolean isValid() {
        return (pageSize != -1 && pageNumber != -1 && condition.isValid());
    }

}
