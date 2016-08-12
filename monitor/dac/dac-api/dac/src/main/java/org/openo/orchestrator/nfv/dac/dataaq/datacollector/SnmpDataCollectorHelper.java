package org.openo.orchestrator.nfv.dac.dataaq.datacollector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import org.openo.orchestrator.nfv.dac.dataaq.common.MonitorException;
import org.openo.orchestrator.nfv.dac.dataaq.datacollector.para.SnmpCollectorPara;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SnmpDataCollectorHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(SnmpDataCollectorHelper.class);
	private static final int TIMEMOUT = 5000; //5s
	private static final int RETRYTIMES = 1; 
	private static final int MD5 = 1; 
	
	public static Snmp getSnmp(int version, SnmpCollectorPara para) throws IOException
	{
		Snmp snmp = new Snmp(new DefaultUdpTransportMapping()); 
		if (version == SnmpConstants.version3)
		{
	        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);  
	        SecurityModels.getInstance().addSecurityModel(usm);  
	        snmp.listen();  
	        // Add User  
	        OID oid = null;
	        if (para.getAuthProtocol() == MD5)
	        {
	        	oid = AuthMD5.ID;
	        }
	        else
	        {
	        	oid = AuthSHA.ID;
	        }
	        	
	        UsmUser user = new UsmUser(  
	                new OctetString(para.getSecurityName()),  
	                oid, new OctetString(para.getAuthPassword()),  
	                PrivDES.ID, new OctetString(para.getPrivPassword()));  
	        snmp.getUSM().addUser(new OctetString(para.getUserName()), user);  
		}
		else
		{
			snmp.listen();  
		}
		return snmp;
	}
	public static Target getTarget(int version, SnmpCollectorPara para)
	{
		UdpAddress udpAddress = null;
		try {
			udpAddress = new UdpAddress(InetAddress.getByName(para.getHostNameOrIp()), para.getPort());
		} catch (UnknownHostException e1) {
			udpAddress = new UdpAddress(para.getHostNameOrIp() + "/" + para.getPort());
		}
		if (version == SnmpConstants.version3)
		{
	        UserTarget target = new UserTarget();  
	        target.setVersion(SnmpConstants.version3);  
	        target.setAddress(udpAddress);  
	        target.setSecurityLevel(para.getSecurityLevel());  
	        target.setSecurityName(new OctetString(para.getSecurityName()));  
	        target.setTimeout(TIMEMOUT);    //3s  
	        target.setRetries(RETRYTIMES); 
	        return target;
		}
		else
		{
			CommunityTarget target = new CommunityTarget();
			target.setCommunity(new OctetString(para.getReadCommunity()));
			target.setVersion(version);
			target.setAddress(udpAddress);
			target.setTimeout(TIMEMOUT);
			target.setRetries(RETRYTIMES);
			return target;
		}
	}
	
	public static PDU createGetPdu(String oid, int version) {  
	    PDU pdu = null;
	    if (version == SnmpConstants.version3)
	    {
	    	pdu = new ScopedPDU();
	    }
	    else
	    {
	    	pdu = new PDU();  
	    }
	    pdu.setType(PDU.GET);  
	    pdu.add(new VariableBinding(new OID(oid)));  
	    return pdu;  
	}

	public static PDU createGetNextPdu(String oid, int version) {  
	    PDU pdu = null;
	    if (version == SnmpConstants.version3)
	    {
	    	pdu = new ScopedPDU();
	    }
	    else
	    {
	    	pdu = new PDU();  
	    }
	    pdu.setType(PDU.GETNEXT);  
	    pdu.add(new VariableBinding(new OID(oid)));
	    return pdu;  
	}

	public static PDU createGetBulkPdu(String oid, int version) {  
	    PDU pdu = null;
	    if (version == SnmpConstants.version3)
	    {
	    	pdu = new ScopedPDU();
	    }
	    else
	    {
	    	pdu = new PDU();  
	    } 
	    pdu.setType(PDU.GETBULK);  
	    pdu.setMaxRepetitions(10);  //must set it, default is 0  
	    pdu.setNonRepeaters(0);  
	    pdu.add(new VariableBinding(new OID(oid)));     //system  
	    return pdu;  
	}

	@SuppressWarnings("unchecked")
	public static Vector<VariableBinding> sendRequest(String requestOid,  PDU pdu, Snmp snmp, Target target)  
	    throws IOException, MonitorException {  
	        ResponseEvent responseEvent = snmp.send(pdu, target);  
	        PDU response = responseEvent.getResponse();  
	        Vector<VariableBinding> allVbs = new Vector<VariableBinding>(); 
	        if (response == null) {  
				throw new MonitorException("sendRequest timeout to" + " Hostip:" + target.getAddress() + " ReqOid:"
						+ requestOid.substring(0, requestOid.length() - 1));
	        } else {  
	            if (response.getErrorStatus() == PDU.noError) {  
	                Vector<VariableBinding> vbs = response.getVariableBindings();  
	                VariableBinding lastVb = vbs.get(vbs.size() - 1);
	                String oid = lastVb.getOid().toString();
	//                
	                if (oid.indexOf(requestOid) != -1)
	                {
	                	pdu.remove(0);
	                	pdu.add(new VariableBinding(new OID(oid)));
	                	allVbs.addAll(vbs);
	                	allVbs.addAll(sendRequest(requestOid, pdu, snmp, target));
	                }
	                else
	                {
	                	 Vector<VariableBinding> newVbs = new Vector<VariableBinding>(); 
	                	for (VariableBinding vb : vbs)
	                	{
	                		String toid = vb.getOid().toString();
	                		 if (toid.indexOf(requestOid) != -1)
	                		 {
	                			 newVbs.add(vb);
	                		 }
	                	}
	                	allVbs.addAll(newVbs);
	                }
	            } else {  
	            	LOGGER.warn("Error:" + response.getErrorStatusText());  
					throw new MonitorException("sendRequest failed" + " Hostip:" + target.getAddress() + " ReqOid:"
							+ requestOid.substring(0, requestOid.length() - 1));
	            }  
	        }  
	        return allVbs;
	    }

	@SuppressWarnings("unchecked")
	public static Vector<VariableBinding> sendRequest(PDU pdu, Snmp snmp, Target target)  
	throws IOException, MonitorException {  
		Vector<VariableBinding> allVbs = new Vector<VariableBinding>(); 
	    ResponseEvent responseEvent = snmp.send(pdu, target);  
	    PDU response = responseEvent.getResponse();  
	    VariableBinding vb = (VariableBinding)pdu.getVariableBindings().get(0);
	    if (response == null) {  
			throw new MonitorException("sendRequest timeout to" + " Hostip:" + target.getAddress() + " ReqOid:"
					+ vb.getOid());
	    	} 
	    else {  
	        if (response.getErrorStatus() == PDU.noError) {  
	        	allVbs = response.getVariableBindings();  
	            
	        } else { 
	        	LOGGER.warn("Error:" + response.getErrorStatusText());
				throw new MonitorException("sendRequest failed" + " Hostip:" + target.getAddress() + " ReqOid:"
						+ vb.getOid());
	        	  
	        }  
	    }  
	    return allVbs;
	}

}
