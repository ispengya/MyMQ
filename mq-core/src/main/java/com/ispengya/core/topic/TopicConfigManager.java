package com.ispengya.core.topic;

import com.ispengya.core.MQCoreController;
import com.ispengya.mq.handlermodel.body.TopicConfigBody;
import com.ispengya.mq.config.ConfigManager;
import com.ispengya.mq.basemodel.DataVersion;
import com.ispengya.mq.basemodel.TopicConfig;
import com.ispengya.mq.util.MQSerializer;
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

    private static final Logger log = LoggerFactory.getLogger(TopicConfigManager.class);
    private static final long LOCK_TIMEOUT_MILLIS = 3000;
    private transient final Lock lockTopicConfigTable = new ReentrantLock();

    private final ConcurrentMap<String, TopicConfig> topicConfigTable =
            new ConcurrentHashMap<String, TopicConfig>(1024);
    private final Set<String> systemTopicList = new HashSet<String>();
    private final DataVersion dataVersion = new DataVersion();
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


    public TopicConfigBody buildTopicConfigSerializeWrapper() {
        TopicConfigBody topicConfigBody = new TopicConfigBody();
        topicConfigBody.setTopicConfigTable(this.topicConfigTable);
        return topicConfigBody;
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
            TopicConfigBody topicConfigBody =
                    MQSerializer.fromJson(jsonString, TopicConfigBody.class);
            if (topicConfigBody != null) {
                this.topicConfigTable.putAll(topicConfigBody.getTopicConfigTable());
                this.dataVersion.assignNewOne(topicConfigBody.getDataVersion());
                this.printLoadDataWhenFirstBoot(topicConfigBody);
            }
        }
    }

    public String encode(final boolean prettyFormat) {
        TopicConfigBody topicConfigBody = new TopicConfigBody();
        topicConfigBody.setTopicConfigTable(this.topicConfigTable);
        topicConfigBody.setDataVersion(this.dataVersion);
        return MQSerializer.toJson(topicConfigBody, prettyFormat);
    }

    public ConcurrentMap<String, TopicConfig> getTopicConfigTable() {
        return topicConfigTable;
    }

    private void printLoadDataWhenFirstBoot(final TopicConfigBody tcs) {
        Iterator<Entry<String, TopicConfig>> it = tcs.getTopicConfigTable().entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, TopicConfig> next = it.next();
            log.info("load exist local topic, {}", next.getValue().toString());
        }
    }
}
