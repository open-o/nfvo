package org.openo.orchestrator.nfv.umc.pm.common;

/**
 * @author 10090474
 *
 */
public class RestRequestException extends Exception {
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * @param exceptDesc
     */
    public RestRequestException(String exceptDesc) {
        super(exceptDesc);
    }
    
    /**
     * @param exceptDesc
     * @param cause
     */
    public RestRequestException(String exceptDesc, Throwable cause) {
        super(exceptDesc, cause);
    }

}
