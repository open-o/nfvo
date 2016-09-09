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
package org.openo.nfvo.monitor.umc.fm.resource.bean.response;
/**
 * exception by exceed the number of entities
 *
 */

public class ExceedLimitException extends Exception {
    private static final long serialVersionUID = -6832193963340263992L;

    private int limit = 0;

    private int actual = 0;

    public ExceedLimitException(int limit, int actual) {
        this.limit = limit;
        this.actual = actual;
    }

    public int getLimit() {
        return limit;
    }

    public int getActual() {
        return actual;
    }
}
