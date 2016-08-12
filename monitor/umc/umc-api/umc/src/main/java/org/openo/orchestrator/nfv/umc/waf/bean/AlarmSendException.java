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
package org.openo.orchestrator.nfv.umc.waf.bean;

import org.openo.orchestrator.nfv.umc.drill.wrapper.common.exception.ErrorCodeException;

public class AlarmSendException extends ErrorCodeException {
	 /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public AlarmSendException (int code, String debugMessage)
	    {
	        super(code, debugMessage);
	    }

	    public AlarmSendException(int code, String debugMessage, String[] arguments)
	    {
	        super(code, debugMessage, arguments);
	    }

	    public AlarmSendException(Throwable source, int code)
	    {
	        super(source, code);
	    }

	    public AlarmSendException(Throwable source, int code, String[] arguments)
	    {
	        super(source, code, arguments);
	    }

	    public AlarmSendException(Throwable source, int code, String debugMessage)
	    {
	        super(source, code, debugMessage);
	    }

	    public AlarmSendException(Throwable source, int code, String debugMessage, String[] arguments)
	    {
	        super(source, code, debugMessage, arguments);
	    }

}
