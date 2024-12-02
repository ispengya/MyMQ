package com.ispengya.space.processor;

import com.ispengya.mq.core.BrokerData;
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

    public void registerBroker(
            final String brokerAddr,
            final String brokerName,
            final long brokerId,
            final Channel channel) {
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
    }


}

class BrokerLiveInfo {
    private long lastUpdateTimestamp;
    private Channel channel;
    private String haServerAddr;

    public BrokerLiveInfo(long lastUpdateTimestamp, Channel channel,
        String haServerAddr) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
        this.channel = channel;
        this.haServerAddr = haServerAddr;
    }

    public long getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(long lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
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
        return "BrokerLiveInfo{" +
                "lastUpdateTimestamp=" + lastUpdateTimestamp +
                ", channel=" + channel +
                ", haServerAddr='" + haServerAddr + '\'' +
                '}';
    }
}
