package com.ispengya.space.processor;

import com.ispengya.mq.QueueData;
import com.ispengya.mq.core.BrokerData;
import com.ispengya.mq.core.TopicConfig;
import com.ispengya.mq.core.common.TopicConfigWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RouteInfoManager {
    private final static long BROKER_CHANNEL_EXPIRED_TIME = 1000 * 60 * 2;
    private static final Logger log = LoggerFactory.getLogger(RouteInfoManager.class);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final HashMap<String/* brokerName */, BrokerData> brokerAddrTable;
    private final HashMap<String/* topic */, List<QueueData>> topicQueueTable;

    public RouteInfoManager() {
        this.brokerAddrTable = new HashMap<String, BrokerData>(128);
        this.topicQueueTable = new HashMap<String, List<QueueData>>(1024);
    }

    public void registerBroker(
            final String brokerAddr,
            final String brokerName,
            final long brokerId,
            final TopicConfigWrapper topicConfigWrapper) {
        try {
            try {
                this.lock.writeLock().lockInterruptibly();
                BrokerData brokerData = this.brokerAddrTable.get(brokerName);
                if (null == brokerData) {
                    brokerData = new BrokerData(brokerName, new HashMap<Long, String>());
                    this.brokerAddrTable.put(brokerName, brokerData);
                }
                brokerData.getBrokerAddrs().put(brokerId, brokerAddr);
                //init queuedata
                ConcurrentMap<String, TopicConfig> tcTable =
                        topicConfigWrapper.getTopicConfigTable();
                if (tcTable != null) {
                    for (Map.Entry<String, TopicConfig> entry : tcTable.entrySet()) {
                        this.createAndUpdateQueueData(brokerName, entry.getValue());
                    }
                }
            } finally {
                this.lock.writeLock().unlock();
            }
        } catch (Exception e) {
            log.error("registerBroker Exception", e);
        }
    }

    private void createAndUpdateQueueData(final String brokerName, final TopicConfig topicConfig) {
        QueueData queueData = new QueueData();
        queueData.setBrokerName(brokerName);
        queueData.setWriteQueueNums(topicConfig.getWriteQueueNums());
        queueData.setReadQueueNums(topicConfig.getReadQueueNums());
        queueData.setTopicSynFlag(topicConfig.getTopicSysFlag());

        List<QueueData> queueDataList = this.topicQueueTable.get(topicConfig.getTopicName());
        if (null == queueDataList) {
            queueDataList = new LinkedList<QueueData>();
            queueDataList.add(queueData);
            this.topicQueueTable.put(topicConfig.getTopicName(), queueDataList);
            log.info("new topic registered, {} {}", topicConfig.getTopicName(), queueData);
        } else {
            boolean addNewOne = true;

            Iterator<QueueData> it = queueDataList.iterator();
            while (it.hasNext()) {
                QueueData qd = it.next();
                if (qd.getBrokerName().equals(brokerName)) {
                    if (qd.equals(queueData)) {
                        addNewOne = false;
                    } else {
                        log.info("topic changed, {} OLD: {} NEW: {}", topicConfig.getTopicName(), qd,
                                queueData);
                        it.remove();
                    }
                }
            }

            if (addNewOne) {
                queueDataList.add(queueData);
            }
        }
    }


}
