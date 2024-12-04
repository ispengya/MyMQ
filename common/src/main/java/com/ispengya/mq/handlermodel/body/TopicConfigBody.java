package com.ispengya.mq.handlermodel.body;

import com.ispengya.mq.basemodel.DataVersion;
import com.ispengya.mq.basemodel.TopicConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TopicConfigBody {
    private ConcurrentMap<String, TopicConfig> topicConfigTable =
        new ConcurrentHashMap<String, TopicConfig>();
    private DataVersion dataVersion = new DataVersion();

    public ConcurrentMap<String, TopicConfig> getTopicConfigTable() {
        return topicConfigTable;
    }

    public void setTopicConfigTable(ConcurrentMap<String, TopicConfig> topicConfigTable) {
        this.topicConfigTable = topicConfigTable;
    }

    public DataVersion getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(DataVersion dataVersion) {
        this.dataVersion = dataVersion;
    }
}