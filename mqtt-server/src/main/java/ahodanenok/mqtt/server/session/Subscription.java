package ahodanenok.mqtt.server.session;

import ahodanenok.mqtt.server.QoS;

public final class Subscription {

    private final String topicFilter;
    private final QoS maxQoS;

    public Subscription(String topicFilter, QoS maxQoS) {
        this.topicFilter = topicFilter;
        this.maxQoS = maxQoS;
    }

    public String getTopicFilter() {
        return topicFilter;
    }

    public QoS getMaxQos() {
        return maxQoS;
    }
}
