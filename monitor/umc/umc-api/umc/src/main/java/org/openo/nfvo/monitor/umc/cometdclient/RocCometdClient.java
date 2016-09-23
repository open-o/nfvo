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
package org.openo.nfvo.monitor.umc.cometdclient;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.cometd.bayeux.Channel;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;
import org.openo.nfvo.monitor.umc.adpt.roc.RocMsgQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RocCometdClient {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());
	
	private String topic = "/resChangeNotification";
	private final MsgListener msgListener = new MsgListener();
	private String rocUrl = "http://{0}:8204/openoapi/rocnotification/v1";
	private static RocCometdClient instance = new RocCometdClient();
	private volatile BayeuxClient client;
	private static RocMsgQueue rocMsgQueue;
	public static RocCometdClient getInstance()
	{
		return instance;
	}
	
	public void subscribe(String rocIp) {
		if (rocIp == null || rocIp.length() == 0)
		{
			return;
		}
		String ip = rocIp.split(":")[0];
		HttpClient httpClient = new HttpClient();
			try {
				httpClient.start();
			} catch (Exception e) {
				LOGGER.warn("ROC cometd subscribe fail!" + e.getMessage(), e);
			}

		Map<String, Object> options = new HashMap<>();
		ClientTransport transport = new LongPollingTransport(options,
				httpClient);
		
		rocMsgQueue = new RocMsgQueue();
        rocMsgQueue.start();
		
		client = new BayeuxClient(MessageFormat.format(rocUrl, ip), transport);
		client.getChannel(Channel.META_CONNECT).addListener(new ConnectionListener());
		client.handshake(new ClientSessionChannel.MessageListener() {
			@Override
            public void onMessage(ClientSessionChannel channel, Message message) {
				if (message.isSuccessful()) {
					// Subscribe
					LOGGER.info("handshake success.");
					client.getChannel(topic).unsubscribe(msgListener);
					client.getChannel(topic).subscribe(msgListener,
							new ClientSessionChannel.MessageListener() {
								@Override
                                public void onMessage(
										ClientSessionChannel channel,
										Message message) {
									if (message.isSuccessful()) {
										LOGGER.info("subscribe success.");
									} else {
										LOGGER.warn("subscribe fail!");
									}
								}
							});
				} else {
					LOGGER.warn("handshake fail!");
				}
			}
		});
	}

	private class MsgListener implements ClientSessionChannel.MessageListener {
		@Override
		public void onMessage(ClientSessionChannel channel, Message message) {
			rocMsgQueue.put(message);
		}
	}
	
	private class ConnectionListener implements ClientSessionChannel.MessageListener {
		private boolean wasConnected = false;
		private boolean connected = false;

		@Override
        public void onMessage(ClientSessionChannel channel, Message message) {

			wasConnected = connected;
			connected = message.isSuccessful();
			if (!wasConnected && connected) {
				connectionEstablished();
			} else if (wasConnected && !connected) {
				connectionBroken();
			}
		}
	}
	
	private void connectionEstablished() {
		LOGGER.info("Connection to ROC Opened.");
	}

	private void connectionBroken() {
		LOGGER.warn("Connection to ROC Broken.");
	}
	
	public static void main(String[] args)
	{
		RocCometdClient.getInstance().subscribe("127.0.0.1");
	}
}
