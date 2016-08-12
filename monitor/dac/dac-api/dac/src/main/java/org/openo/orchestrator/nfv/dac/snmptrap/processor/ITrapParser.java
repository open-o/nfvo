package org.openo.orchestrator.nfv.dac.snmptrap.processor;

import org.openo.orchestrator.nfv.dac.snmptrap.entity.TrapData;
import org.snmp4j.PDU;

public interface ITrapParser {
	public void parser(TrapData trapData, PDU trapPDU);
}
