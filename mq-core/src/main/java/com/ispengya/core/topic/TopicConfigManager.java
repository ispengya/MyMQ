package com.ispengya.core.topic;

import com.ispengya.core.MQCoreController;
import com.ispengya.mq.config.ConfigManager;
import com.ispengya.mq.core.TopicConfig;
import com.ispengya.mq.core.common.DataVersion;
import com.ispengya.mq.core.common.TopicConfigSerializeWrapper;
import com.ispengya.server.procotol.SimpleServerSerializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TopicConfigManager extends ConfigManager {

    private static final long LOCK_TIMEOUT_MILLIS = 3000;
    private static final Logger log = LoggerFactory.getLogger(TopicConfigManager.class);
    private transient final Lock lockTopicConfigTable = new ReentrantLock();

    private final ConcurrentMap<String, TopicConfig> topicConfigTable =
            new ConcurrentHashMap<String, TopicConfig>(1024);
    private final DataVersion dataVersion = new DataVersion();
    private final Set<String> systemTopicList = new HashSet<String>();
    private transient MQCoreController brokerController;

    public TopicConfigManager() {
    }

    public TopicConfigManager(MQCoreController brokerController) {
        this.brokerController = brokerController;
        //init topic.json
        this.load();
    }

    public boolean isSystemTopic(final String topic) {
        return this.systemTopicList.contains(topic);
    }

    public Set<String> getSystemTopic() {
        return this.systemTopicList;
    }


    public TopicConfigSerializeWrapper buildTopicConfigSerializeWrapper() {
        TopicConfigSerializeWrapper topicConfigSerializeWrapper = new TopicConfigSerializeWrapper();
        topicConfigSerializeWrapper.setTopicConfigTable(this.topicConfigTable);
        topicConfigSerializeWrapper.setDataVersion(this.dataVersion);
        return topicConfigSerializeWrapper;
    }

    @Override
    public String encode() {
        return encode(false);
    }

    @Override
    public String configFilePath() {
        return System.getenv("CONFIG_HOME") + "/store/topic-config.json";
    }

    @Override
    public void decode(String jsonString) {
        if (jsonString != null) {
            TopicConfigSerializeWrapper topicConfigSerializeWrapper =
                    SimpleServerSerializable.fromJson(jsonString, TopicConfigSerializeWrapper.class);
            if (topicConfigSerializeWrapper != null) {
                this.topicConfigTable.putAll(topicConfigSerializeWrapper.getTopicConfigTable());
                this.dataVersion.assignNewOne(topicConfigSerializeWrapper.getDataVersion());
                this.printLoadDataWhenFirstBoot(topicConfigSerializeWrapper);
            }
        }
    }

    public String encode(final boolean prettyFormat) {
        TopicConfigSerializeWrapper topicConfigSerializeWrapper = new TopicConfigSerializeWrapper();
        topicConfigSerializeWrapper.setTopicConfigTable(this.topicConfigTable);
        topicConfigSerializeWrapper.setDataVersion(this.dataVersion);
        return SimpleServerSerializable.toJson(topicConfigSerializeWrapper, prettyFormat);
    }

    private void printLoadDataWhenFirstBoot(final TopicConfigSerializeWrapper tcs) {
        Iterator<Entry<String, TopicConfig>> it = tcs.getTopicConfigTable().entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, TopicConfig> next = it.next();
            log.info("load exist local topic, {}", next.getValue().toString());
        }
    }

    public DataVersion getDataVersion() {
        return dataVersion;
    }

    public ConcurrentMap<String, TopicConfig> getTopicConfigTable() {
        return topicConfigTable;
    }
}
