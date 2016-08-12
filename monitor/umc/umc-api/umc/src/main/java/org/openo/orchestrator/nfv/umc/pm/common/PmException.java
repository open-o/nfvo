package org.openo.orchestrator.nfv.umc.pm.common;


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