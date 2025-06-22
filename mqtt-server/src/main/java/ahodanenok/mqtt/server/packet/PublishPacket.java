package ahodanenok.mqtt.server.packet;

import java.nio.ByteBuffer;

import ahodanenok.mqtt.server.QoS;

public final class PublishPacket extends MqttPacket {

    private boolean duplicated;
    private QoS qos;
    private boolean retain;
    private String topicName;
    private int packetIdentifier;
    private ByteBuffer payload;

    public PublishPacket() {
        super(PacketType.PUBLISH);
    }

    public boolean isDuplicated() {
        return duplicated;
    }

    public void setDuplicated(boolean duplicated) {
        this.duplicated = duplicated;
    }

    public QoS getQoS() {
        return qos;
    }

    public void setQoS(QoS qos) {
        this.qos = qos;
    }

    public boolean isRetain() {
        return retain;
    }

    public void setRetain(boolean retain) {
        this.retain = retain;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getPacketIdentifier() {
        return packetIdentifier;
    }

    public void setPacketIdentifier(int packetIdentifier) {
        this.packetIdentifier = packetIdentifier;
    }

    public ByteBuffer getPayload() {
        return payload;
    }

    public void setPayload(ByteBuffer payload) {
        this.payload = payload;
    }
}
