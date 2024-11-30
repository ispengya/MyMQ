package com.ispengya.core;

import com.ispengya.core.api.MQOuterAPI;
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
    private final MQOuterAPI mqOuterAPI;
    private SimpleClient simpleClient;

    public MQCoreController(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        this.mqOuterAPI = new MQOuterAPI(clientConfig);
    }

    public void start() {
        if (this.mqOuterAPI != null) {
            this.mqOuterAPI.start();
        }
        this.registerBrokerAll();
    }

    private void registerBrokerAll() {
        try {
            this.mqOuterAPI.registerBrokerAll("1111","11111",1111, false);
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
