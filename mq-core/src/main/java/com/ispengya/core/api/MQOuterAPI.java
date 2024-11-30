package com.ispengya.core.api;

import com.ispengya.mq.RegisterBrokerResult;
import com.ispengya.mq.constant.RequestCode;
import com.ispengya.mq.constant.ResponseCode;
import com.ispengya.mq.header.RegisterBrokerRequestHeader;
import com.ispengya.server.common.exception.SimpleServerException;
import com.ispengya.server.netty.client.ClientConfig;
import com.ispengya.server.netty.client.SimpleClient;
import com.ispengya.server.procotol.SimpleServerTransContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: hanzhipeng
 * @create: 2024-11-30 17:41
 **/
public class MQOuterAPI {
    private static final Logger log = LoggerFactory.getLogger(MQOuterAPI.class);
    private final ClientConfig clientConfig;
    private SimpleClient client;

    public MQOuterAPI(ClientConfig clientConfig) {
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

    public List<RegisterBrokerResult> registerBrokerAll(
            final String brokerAddr,
            final String brokerName,
            final long brokerId,
            final boolean oneway) throws Exception {
        final List<RegisterBrokerResult> registerBrokerResultList = new ArrayList<>();
        //TODO 获取space地址
        String namesrvAddr = "127.0.0.1:6666";
        final RegisterBrokerRequestHeader requestHeader = new RegisterBrokerRequestHeader();
        requestHeader.setBrokerAddr(brokerAddr);
        requestHeader.setBrokerId(brokerId);
        requestHeader.setBrokerName(brokerName);
        SimpleServerTransContext request = SimpleServerTransContext.createRequestSST(RequestCode.REGISTER_BROKER, requestHeader);

        if (oneway) {
            try {
                this.client.invokeOneway(namesrvAddr, request, 30 * 1000);
            } catch (Exception e) {
                // Ignore
            }
            return null;
        }

        SimpleServerTransContext response = this.client.invokeSync(namesrvAddr, request, 30 * 1000);
        assert response != null;
        switch (response.getStatusCode()) {
            case ResponseCode.SUCCESS: {
                System.out.println("成功"+ response);
            }
            default:
                break;
        }
        return registerBrokerResultList;
    }
}
