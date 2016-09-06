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
package org.openo.orchestrator.nfv.dac.snmptrap;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

public class TrapLisSrv {
	private static final Logger LOGGER = LoggerFactory.getLogger(TrapLisSrv.class);
    private static TrapLisSrv instance = new TrapLisSrv();
    private static Set<String> trapListenPorts = new HashSet<String>();
    private static final String TRAPADDRESS = "udp:0.0.0.0/";
    private static final String TRAPPOOLNAME = "TrapPool";
    private static final String SNMP4JADDRESS  = "snmp4j.listenAddress";
    public static TrapLisSrv getInstance()
    {
    	return instance;
    }
    
    public void trapLisStart(int[] ports)
    {
    	for(int port : ports)
    	{
    		String sPort = String.valueOf(port);
            if (!trapListenPorts.contains(sPort))
            {
            	String trapAddress = TRAPADDRESS + port;
            	ThreadPool threadPool = ThreadPool.create(TRAPPOOLNAME, 1);
            	MultiThreadedMessageDispatcher dispatcher = new MultiThreadedMessageDispatcher(threadPool,
                        new MessageDispatcherImpl());
            	Address listenAddress = GenericAddress.parse(System.getProperty(
                		SNMP4JADDRESS, trapAddress));
                TransportMapping transport;
                try
                {
	                if (listenAddress instanceof UdpAddress) {
	                    transport = new DefaultUdpTransportMapping(
	                            (UdpAddress) listenAddress);
	                } else {
	                    transport = new DefaultTcpTransportMapping(
	                            (TcpAddress) listenAddress);
	                }
	                Snmp snmp = new Snmp(dispatcher, transport);
	                snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
	                snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
	                snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
	                USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(
	                        MPv3.createLocalEngineID()), 0);
	                SecurityModels.getInstance().addSecurityModel(usm);
	                snmp.listen();
	                TrapListener listener = new TrapListener();
	                snmp.addCommandResponder(listener);
                }
                catch (Exception e)
                {
                	LOGGER.warn("Trap listen on port:" + port + " failed! " + e.getMessage(), e);
                }
            }
    	}
    }
}
