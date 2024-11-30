package com.ispengya.space.processor;

import com.ispengya.mq.BrokerData;
import com.ispengya.mq.DataVersion;
import com.ispengya.mq.RegisterBrokerResult;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RouteInfoManager {
    private final static long BROKER_CHANNEL_EXPIRED_TIME = 1000 * 60 * 2;
    private static final Logger log = LoggerFactory.getLogger(RouteInfoManager.class);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final HashMap<String/* brokerName */, BrokerData> brokerAddrTable;
    private final HashMap<String/* brokerAddr */, BrokerLiveInfo> brokerLiveTable;

    public RouteInfoManager() {
        this.brokerAddrTable = new HashMap<String, BrokerData>(128);
        this.brokerLiveTable = new HashMap<String, BrokerLiveInfo>(256);
    }

    public RegisterBrokerResult registerBroker(
            final String brokerAddr,
            final String brokerName,
            final long brokerId,
            final Channel channel) {
        RegisterBrokerResult result = new RegisterBrokerResult();
        try {
            try {
                this.lock.writeLock().lockInterruptibly();
                BrokerData brokerData = this.brokerAddrTable.get(brokerName);
                if (null == brokerData) {
                    brokerData = new BrokerData(brokerName, new HashMap<Long, String>());
                    this.brokerAddrTable.put(brokerName, brokerData);
                }
            } finally {
                this.lock.writeLock().unlock();
            }
        } catch (Exception e) {
            log.error("registerBroker Exception", e);
        }
        return result;
    }


}

class BrokerLiveInfo {
    private long lastUpdateTimestamp;
    private DataVersion dataVersion;
    private Channel channel;
    private String haServerAddr;

    public BrokerLiveInfo(long lastUpdateTimestamp, DataVersion dataVersion, Channel channel,
        String haServerAddr) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
        this.dataVersion = dataVersion;
        this.channel = channel;
        this.haServerAddr = haServerAddr;
    }

    public long getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(long lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public DataVersion getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(DataVersion dataVersion) {
        this.dataVersion = dataVersion;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getHaServerAddr() {
        return haServerAddr;
    }

    public void setHaServerAddr(String haServerAddr) {
        this.haServerAddr = haServerAddr;
    }

    @Override
    public String toString() {
        return "BrokerLiveInfo [lastUpdateTimestamp=" + lastUpdateTimestamp + ", dataVersion=" + dataVersion
            + ", channel=" + channel + ", haServerAddr=" + haServerAddr + "]";
    }
}
