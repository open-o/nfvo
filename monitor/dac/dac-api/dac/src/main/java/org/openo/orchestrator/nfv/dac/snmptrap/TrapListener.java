package org.openo.orchestrator.nfv.dac.snmptrap;

import org.openo.orchestrator.nfv.dac.snmptrap.queue.TrapGetMsgQueue;
import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;

public class TrapListener implements CommandResponder{
    public void processPdu(CommandResponderEvent event) {
    	TrapGetMsgQueue.getInstance().put(event);
    }
}
