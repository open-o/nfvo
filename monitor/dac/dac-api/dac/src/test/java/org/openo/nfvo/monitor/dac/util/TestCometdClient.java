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
package org.openo.nfvo.monitor.dac.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;
import org.openo.nfvo.monitor.dac.common.bean.DataBean;

import java.io.IOException;
import java.util.*;

public class TestCometdClient {
    private String topic = "/upload/data";
    private final MsgListener msgListener = new MsgListener();

    private void run() {
        HttpClient httpClient = new HttpClient();

        try {
            httpClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Map<String, Object> options = new HashMap<>();
        ClientTransport transport = new LongPollingTransport(options, httpClient);
        final BayeuxClient client =
                new BayeuxClient("http://localhost:8206/api/dacnotification/v1", transport);
        client.handshake(new ClientSessionChannel.MessageListener() {
            public void onMessage(ClientSessionChannel channel, Message message) {
                if (message.isSuccessful()) {
                    // Subscribe
                    System.out.println("handshake success.");
                    client.getChannel(topic).subscribe(msgListener,
                            new ClientSessionChannel.MessageListener() {
                                public void onMessage(ClientSessionChannel channel, Message message) {
                                    if (message.isSuccessful()) {
                                        System.out.println("subscribe success.");
                                    } else {
                                        System.out.println("subscribe fail!");
                                    }
                                }
                            });
                } else {
                    System.out.println("handshake fail!");
                }
            }
        });
    }

    private class MsgListener implements ClientSessionChannel.MessageListener {
        public void onMessage(ClientSessionChannel channel, Message message) {
            String json = (String) message.getData();
            System.out.println("Message: " + json);
            ObjectMapper objectMapper = new ObjectMapper();
            DataBean data;
            try {
                data = objectMapper.readValue(json, DataBean.class);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            long taskId = data.getTaskId();
            Date collectTime = data.getCollectTime();
            long granularity = data.getGranularity();
            Properties[] values = data.getValues();
            System.out.println("taskId=" + taskId);
            System.out.println("collectTime=" + collectTime);
            System.out.println("granularity=" + granularity);
            System.out.println("values=" + Arrays.toString(values));
        }
    }

    public static void main(String[] args) throws IOException {
        TestCometdClient client = new TestCometdClient();
        client.run();
    }
}
