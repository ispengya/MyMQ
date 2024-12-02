package com.ispengya.core.config;

import com.ispengya.mq.util.IOUtil;

public class BrokerConfig {
    private String namesrvAddr;
    private String brokerName = "DEFAULT_BROKER";
    private String brokerClusterName = "DefaultCluster";
    private String brokerAddr = IOUtil.getLocalAddress();
    private long brokerId = 0l;
    private int listenPort = 0;

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public String getBrokerClusterName() {
        return brokerClusterName;
    }

    public void setBrokerClusterName(String brokerClusterName) {
        this.brokerClusterName = brokerClusterName;
    }

    public long getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(long brokerId) {
        this.brokerId = brokerId;
    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public String getBrokerAddr() {
        return this.brokerAddr + ":" + this.listenPort;
    }
}