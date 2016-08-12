package org.openo.orchestrator.nfv.umc.pm.datacollect;

import java.util.Map;

public interface IPmDataConsumer {
	String ROLE = IPmDataConsumer.class.getName();
	
	void consume(Map<String, Object> attrList);
}
