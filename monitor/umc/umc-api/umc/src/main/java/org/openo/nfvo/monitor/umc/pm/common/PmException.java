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
package org.openo.nfvo.monitor.umc.pm.common;


public class PmException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -795330776708118262L;

	public PmException(Throwable cause) {
		super(cause);
	}
		
	public PmException(int errorCode, String exceptionDescription,
			Throwable cause) {
		super(exceptionDescription, cause);
		m_errorCode = errorCode;
	}

	public PmException(String exceptionDescription, Throwable cause) {
		super(exceptionDescription, cause);
		m_errorCode = -1;
	}

	public int getErrorCode() {
		return m_errorCode;
	}

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

	// Exceptions can be returned through the object to the required information
	private Object m_PmInfoObj = null;
}
