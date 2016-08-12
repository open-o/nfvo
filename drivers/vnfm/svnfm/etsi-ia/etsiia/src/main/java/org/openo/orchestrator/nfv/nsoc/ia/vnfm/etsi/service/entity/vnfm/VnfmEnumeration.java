/**
 * Copyright (C) 2015 CMCC, Inc. and others. All rights reserved. (CMCC)
 * */
package org.openo.orchestrator.nfv.nsoc.ia.vnfm.etsi.service.entity.vnfm;

public class VnfmEnumeration {
    public static final String OPERATE_START = "start";
    public static final String OPERATE_STOP = "stop";

    public static final String SCALE_IN = "scaleIn";
    public static final String SCALE_OUT = "scaleOut";
    public static final String SCALE_UP = "scaleUp";
    public static final String SCALE_DOWN = "scaleDown";

    public static final String SCALE_BY_TYPE = "byType";
    public static final String SCALE_BY_INSTANCE = "byInstance";

    public static final String TERMINATION_TYPE_FORCE = "forceful";
    public static final String TERMINATION_TYPE_GRACE = "graceful";

    public static final String IP_FIXED_TYPE = "fixed";
    public static final String IP_FLOATING_TYPE = "floating";
}
