package com.ispengya.mq.header.req;

import com.ispengya.server.CustomHeader;

public class GetRouteInfoRequestHeader implements CustomHeader {
    private String topic;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
