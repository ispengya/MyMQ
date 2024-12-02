package com.ispengya.core;

import com.ispengya.core.api.MQOuterAPI;
import com.ispengya.core.config.BrokerConfig;
import com.ispengya.core.topic.TopicConfigManager;
import com.ispengya.server.common.exception.SimpleServerException;
import com.ispengya.server.netty.client.ClientConfig;
import com.ispengya.server.netty.client.SimpleClient;

/**
 * @description:
 * @author: hanzhipeng
 * @create: 2024-11-30 17:29
 **/
public class MQCoreController {
    private final ClientConfig clientConfig;
    private final BrokerConfig brokerConfig;
    private final MQOuterAPI mqOuterAPI;
    private SimpleClient simpleClient;
    private TopicConfigManager topicConfigManager;

    public MQCoreController(BrokerConfig brokerConfig) {
        this.clientConfig = new ClientConfig();
        this.brokerConfig = brokerConfig;
        this.mqOuterAPI = new MQOuterAPI(clientConfig, brokerConfig);
        this.topicConfigManager = new TopicConfigManager(this);
    }

    public void start() {
        if (this.mqOuterAPI != null) {
            this.mqOuterAPI.start();
        }
        this.registerBrokerAll();
    }

    private void registerBrokerAll() {
        try {
            this.mqOuterAPI.registerBrokerAll(brokerConfig.getBrokerAddr(), brokerConfig.getBrokerName(), brokerConfig.getBrokerId(), false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() throws SimpleServerException {
        if (this.mqOuterAPI != null) {
            this.mqOuterAPI.shutdown();
        }
    }
}
