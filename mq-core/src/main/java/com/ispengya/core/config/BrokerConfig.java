package com.ispengya.core.config;

import com.ispengya.mq.util.AllUtil;

public class BrokerConfig {
    private String mqHome = System.getProperty(AllUtil.MYMQ_HOME_PROPERTY, System.getenv(AllUtil.MYMQ_HOME_ENV));
    private String namesrvAddr;
    private String brokerName = "DEFAULT_BROKER";
    private String brokerClusterName = "DefaultCluster";
    private String brokerAddr = AllUtil.getLocalAddress();
    private long brokerId = 0l;
    private int listenPort = 0;

    public String getMqHome() {
        return mqHome;
    }

    public void setMqHome(String mqHome) {
        this.mqHome = mqHome;
    }

    public void setBrokerAddr(String brokerAddr) {
        this.brokerAddr = brokerAddr;
    }

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