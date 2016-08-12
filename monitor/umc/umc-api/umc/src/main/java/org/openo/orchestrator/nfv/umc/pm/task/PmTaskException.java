package org.openo.orchestrator.nfv.umc.pm.task;

/**
 * Exception of performance task operation.
 */
public class PmTaskException extends Exception {
	public PmTaskException(String message) {
		super(message);
	}
	
	public PmTaskException(Throwable cause) {
		super(cause);
	}
	
	public PmTaskException(String message, Throwable cause) {
		super(message, cause);
	}
}
