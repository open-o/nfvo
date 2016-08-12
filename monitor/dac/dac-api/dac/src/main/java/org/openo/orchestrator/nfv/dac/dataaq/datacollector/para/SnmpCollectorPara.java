package org.openo.orchestrator.nfv.dac.dataaq.datacollector.para;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.openo.orchestrator.nfv.dac.dataaq.common.ICollectorPara;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnmpCollectorPara implements ICollectorPara {
	protected String hostNameOrIp = null;
	protected int port = 161;
	protected int version = 0;// 0:version 1, 1:version 2,2:version 3
	protected String readCommunity = null;
	protected String writeCommunity = null;
	protected String userName = null;
	protected String securityName = null;
	protected int securityLevel = 1; // 1: NOAUTH_NOPRIV, 2: AUTH_NOPRIV,3: AUTH_PRIV 
	protected int authProtocol = 1; // 1: MD5,2: SHA
	protected String authPassword = null;
	protected String privPassword = null;
}
