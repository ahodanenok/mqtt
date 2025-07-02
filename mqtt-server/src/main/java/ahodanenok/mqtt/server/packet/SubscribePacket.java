package ahodanenok.mqtt.server.packet;

import java.util.List;

import ahodanenok.mqtt.server.QoS;

public final class SubscribePacket extends MqttPacket {

    private int packetIdentifier;
    private List<Subscription> subscriptions;

    public SubscribePacket() {
        super(PacketType.SUBSCRIBE);
    }

    public int getPacketIdentifier() {
        return packetIdentifier;
    }

    public void setPacketIdentifier(int packetIdentifier) {
        this.packetIdentifier = packetIdentifier;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public static class Subscription {

        private String topicFilter;
        private QoS requestedQoS;

        public String getTopicFilter() {
            return topicFilter;
        }

        public void setTopicFilter(String topicFilter) {
            this.topicFilter = topicFilter;
        }

        public QoS getRequestedQoS() {
            return requestedQoS;
        }

        public void setRequestedQoS(QoS requestedQoS) {
            this.requestedQoS = requestedQoS;
        }
    }
}
