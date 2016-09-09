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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.cometd.bayeux.Channel;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;
import org.openo.nfvo.monitor.umc.cometdclient.bean.DacPmData;
import org.openo.nfvo.monitor.umc.pm.adpt.dac.DacConfiguration;
import org.openo.nfvo.monitor.umc.pm.common.PmException;
import org.openo.nfvo.monitor.umc.pm.datacollect.PmDataProcessor;
import org.openo.nfvo.monitor.umc.pm.datacollect.bean.PmData;
import org.openo.nfvo.monitor.umc.pm.services.PmService;
import org.openo.nfvo.monitor.umc.snmptrap.common.SnmpTrapQueue;
import org.openo.nfvo.monitor.umc.snmptrap.common.TrapMsgData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class DacCometdClient {
    private static Logger logger = LoggerFactory.getLogger(DacCometdClient.class);

    private ClientSessionChannel.MessageListener pmDataListener = new PmDataListener();
    private ClientSessionChannel.MessageListener snmpTrapListener = new SnmpTrapListener();
    private volatile BayeuxClient client;
    private String ip;
    private String port;
    private String url;
    private static String CHANNEL_PMDATA = "/upload/data";
    private static String CHANNEL_SNMPTRAP = "/upload/snmptrap";

    public DacCometdClient(String ip) {
        this(ip, DacConfiguration.getInstance().getDacServerPort(), "/api/dacnotification/v1");
    }

    private String fullUrl = "";

    public DacCometdClient(String ip, String port, String url) {
        super();
        this.ip = ip;
        this.port = port;
        this.url = url;
        fullUrl = "http://" + this.ip + ":" + this.port + this.url;
    }

    public void subscribe() {
        logger.info("subscribe cometd message. url=" + this.fullUrl);
        HttpClient httpClient = new HttpClient();

        try {
            httpClient.start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return;
        }
        Map<String, Object> options = new HashMap<>();
        ClientTransport transport = new LongPollingTransport(options, httpClient);
        client = new BayeuxClient(fullUrl, transport);
        client.getChannel(Channel.META_CONNECT).addListener(new ConnectionListener());
        client.handshake(new ClientSessionChannel.MessageListener() {
            public void onMessage(ClientSessionChannel channel, Message message) {
                if (message.isSuccessful()) {
                    // Subscribe
                    logger.info("handshake success.");
                    client.getChannel(CHANNEL_PMDATA).subscribe(pmDataListener,
                            new SubscribeListener(CHANNEL_PMDATA));

                    client.getChannel(CHANNEL_SNMPTRAP).subscribe(snmpTrapListener,
                            new SubscribeListener(CHANNEL_SNMPTRAP));
                } else {
                    logger.info("handshake fail! baseUrl=" + fullUrl);
                }
            }
        });
    }

    public void unsubscribe() {
        logger.info("unsubscribe cometd message. url=" + this.fullUrl);

        if (client == null) {
            return;
        }

        client.disconnect();
    }

    private class PmDataListener implements ClientSessionChannel.MessageListener {
        public void onMessage(ClientSessionChannel channel, Message message) {
            logger.debug("Receive Channel: " + channel.getId());
            logger.debug("Receive Message: " + message.getData());

            String json = (String) message.getData();
            ObjectMapper objectMapper = new ObjectMapper();
            DacPmData data;
            try {
                data = objectMapper.readValue(json, DacPmData.class);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return;
            }

            List<PmData> pmDataList = new ArrayList<>();
            pmDataList.add(convert2PmData(data));
            PmDataProcessor.getInstance().pmTaskResultProcess(ip, pmDataList);
        }

        private PmData convert2PmData(DacPmData data) {
            return new PmData(data.getCollectTime().getTime(), data.getGranularity(), data.getTaskId(),
                    data.getValues());
        }
    }

    private class SnmpTrapListener implements ClientSessionChannel.MessageListener {
        public void onMessage(ClientSessionChannel channel, Message message) {
            logger.debug("Receive Channel: " + channel.getId());
            logger.debug("Receive Message: " + message.getData());

            String json = (String) message.getData();
            ObjectMapper objectMapper = new ObjectMapper();
            TrapMsgData trapMsgData;
            try {
                trapMsgData = objectMapper.readValue(json, TrapMsgData.class);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return;
            }

            SnmpTrapQueue.putTrapMsgData(trapMsgData);
        }
    }

    private class SubscribeListener implements ClientSessionChannel.MessageListener {
        private String channelName;

        SubscribeListener(String channelName) {
            this.channelName = channelName;
        }

        public void onMessage(ClientSessionChannel channel, Message message) {
            if (message.isSuccessful()) {
                logger.info("subscribe " + this.channelName + " success. baseUrl=" + fullUrl);
            } else {
                logger.info("subscribe " + this.channelName + " fail! baseUrl=" + fullUrl);
            }
        }
    }

    private class ConnectionListener implements ClientSessionChannel.MessageListener {
        private boolean wasConnected = false;
        private boolean connected = false;

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
        logger.info("Connection to DAC Opened.");
        try {
            PmService.reStartAllPmTask(ip);
        } catch (PmException e) {
            logger.warn("Restart pm task failed on dac:" + ip, e);
        }
    }

    private void connectionBroken() {
        logger.warn("Connection to DAC Broken.");
    }
}
