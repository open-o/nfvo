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
package org.openo.nfvo.monitor.umc.fm.common;


public class FmException extends Exception {

    private static final long serialVersionUID = 1L;

    public FmException(Throwable cause) {
        super(cause);
    }

    /**
     * Construction method
     *
     * @param errorCode
     * @param exceptionDescription
     * @param cause
     */
    public FmException(int errorCode, String exceptionDescription, Throwable cause) {
        super(exceptionDescription, cause);
        m_errorCode = errorCode;
    }

    /**
     * Construction method
     *
     * @param exceptionDescription
     * @param cause
     */
    public FmException(String exceptionDescription, Throwable cause) {
        super(exceptionDescription, cause);
        m_errorCode = -1;
    }

    /**
     * get errorCode
     *
     * @return errorCode
     */
    public int getErrorCode() {
        return m_errorCode;
    }

    /**
     * set errorCode
     *
     * @param inErrorCode errorCode
     */
    public void setErrorCode(int inErrorCode) {
        m_errorCode = inErrorCode;
    }


    public void setPmInfoObj(Object obj) {
        m_PmInfoObj = obj;
    }


    public Object getPmInfoObj() {
        return m_PmInfoObj;
    }

    private int m_errorCode;

    private Object m_PmInfoObj = null;
}
