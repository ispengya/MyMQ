package com.ispengya.core.api;

import com.ispengya.core.config.BrokerConfig;
import com.ispengya.mq.body.TopicConfigBody;
import com.ispengya.mq.constant.RequestCode;
import com.ispengya.mq.constant.ResponseCode;
import com.ispengya.mq.header.RegisterBrokerRequestHeader;
import com.ispengya.mq.util.MQSerializer;
import com.ispengya.server.common.exception.SimpleServerException;
import com.ispengya.server.netty.client.ClientConfig;
import com.ispengya.server.netty.client.SimpleClient;
import com.ispengya.server.procotol.SimpleServerTransContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: hanzhipeng
 * @create: 2024-11-30 17:41
 **/
public class MQOuterAPI {
    private static final Logger log = LoggerFactory.getLogger(MQOuterAPI.class);
    private final ClientConfig clientConfig;
    private final BrokerConfig brokerConfig;
    private SimpleClient client;

    public MQOuterAPI(ClientConfig clientConfig, BrokerConfig brokerConfig) {
        this.brokerConfig = brokerConfig;
        this.clientConfig = clientConfig;
    }

    public void start() {
        try {
            client = new SimpleClient(clientConfig);
            client.start();
        } catch (SimpleServerException e) {
            log.error("client start failed", e);
        }
    }

    public void shutdown() throws SimpleServerException {
        this.client.stop();
    }

    public void registerBrokerAll(
            final String brokerAddr,
            final String brokerName,
            final long brokerId,
            final TopicConfigBody topicConfigBody,
            final boolean oneway) throws Exception {
        String namesrvAddr = brokerConfig.getNamesrvAddr();
        final RegisterBrokerRequestHeader requestHeader = new RegisterBrokerRequestHeader();
        requestHeader.setBrokerAddr(brokerAddr);
        requestHeader.setBrokerId(brokerId);
        requestHeader.setBrokerName(brokerName);
        SimpleServerTransContext request = SimpleServerTransContext.createRequestSST(RequestCode.REGISTER_BROKER, requestHeader);

        request.setBody(MQSerializer.encode(topicConfigBody));

        if (oneway) {
            try {
                this.client.invokeOneway(namesrvAddr, request, 30 * 1000);
            } catch (Exception e) {
                // Ignore
            }
            return;
        }

        SimpleServerTransContext response = this.client.invokeSync(namesrvAddr, request, 30 * 1000);
        assert response != null;
        switch (response.getStatusCode()) {
            case ResponseCode.SUCCESS: {
                log.info("broker info [ {}-{}-{} ] successfully registered", brokerName, brokerAddr, brokerId);
            }
            default:
                break;
        }
    }
}
