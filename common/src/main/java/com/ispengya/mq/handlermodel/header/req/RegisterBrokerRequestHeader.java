package com.ispengya.mq.handlermodel.header.req;

import com.ispengya.server.CustomHeader;

public class RegisterBrokerRequestHeader implements CustomHeader {
    private String brokerName;
    private String brokerAddr;
    private Long brokerId;

    public Long getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(Long brokerId) {
        this.brokerId = brokerId;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public String getBrokerAddr() {
        return brokerAddr;
    }

    public void setBrokerAddr(String brokerAddr) {
        this.brokerAddr = brokerAddr;
    }

}