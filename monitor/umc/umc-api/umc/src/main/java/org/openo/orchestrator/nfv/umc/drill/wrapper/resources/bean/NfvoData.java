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
package org.openo.orchestrator.nfv.umc.drill.wrapper.resources.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author 10188044
 * @date 2015-8-13
 *       <p/>
 *       the parent entity type defines the common fields all the entities have,all the other
 *       entities extend it
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NfvoData {
    private String oid;
    private String moc;
    private String mocName;
    private String parentOid;
    private String name;
    private String manageBy;
    private String ipAddress;
}
