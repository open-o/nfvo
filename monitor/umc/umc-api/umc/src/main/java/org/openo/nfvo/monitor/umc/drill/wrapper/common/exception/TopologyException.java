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
package org.openo.nfvo.monitor.umc.drill.wrapper.common.exception;

/**
 *       the customized exception used in the UMC-drill module
 */
public class TopologyException extends ErrorCodeException implements ITopologyException {

    private static final long serialVersionUID = 8589090816325188915L;

    /**
     *
     * @param code int
     * @param debugMessage String
     * @see ErrorCodeException#ErrorCodeException(int, String)
     */
    public TopologyException(int code, String debugMessage) {
        super(code, debugMessage);
    }

    /**
     *
     * @param source Throwable
     * @param code int
     * @see ErrorCodeException#ErrorCodeException(Throwable, int)
     */
    public TopologyException(Throwable source, int code) {
        super(source, code);
    }

    /**
     *
     * @param code int
     * @param debugMessage String
     * @param arguments String[]
     * @see ErrorCodeException#ErrorCodeException(int, String, String[])
     */
    public TopologyException(int code, String debugMessage, String[] arguments) {
        super(code, debugMessage, arguments);
    }

    /**
     *
     * @param source Throwable
     * @param code
     * @param arguments
     * @see ErrorCodeException#ErrorCodeException(Throwable, int, String[])
     */
    public TopologyException(Throwable source, int code, String[] arguments) {
        super(source, code, arguments);
    }

    /**
     *
     * @param source
     * @param code
     * @param debugMessage
     * @see ErrorCodeException#ErrorCodeException(Throwable, int, String[])
     */
    public TopologyException(Throwable source, int code, String debugMessage) {
        super(source, code, debugMessage);
    }

    /**
     *
     * @param source
     * @param code
     * @param debugMessage
     * @param arguments
     * @see ErrorCodeException#ErrorCodeException(Throwable, int, String, String[])
     */
    public TopologyException(Throwable source, int code, String debugMessage, String[] arguments) {
        super(source, code, debugMessage, arguments);
    }
}
