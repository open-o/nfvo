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
package org.openo.nfvo.monitor.umc.sm.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResult {

    public static final int SUCCESS = 0;
    public static final int FAILED = 1;

    private int result;
    private LoginError detail;

    public LoginResult(int result, String errorCode, String message) {
        this.result = result;
        this.detail = new LoginError(errorCode, message);
    }



}


@Data
@NoArgsConstructor
@AllArgsConstructor
class LoginError {
    public static final String SUCCESS = "0";
    public static final String FAILED = "4";
    private String code;
    private String message;
}
