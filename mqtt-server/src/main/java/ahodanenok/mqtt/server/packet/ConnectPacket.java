package ahodanenok.mqtt.server.packet;

import java.nio.ByteBuffer;

import ahodanenok.mqtt.server.QoS;

public final class ConnectPacket extends MqttPacket {

    private int protocolLevel;
    private int keepAlive;
    private boolean willPresent;
    private boolean willRetain;
    private QoS willQoS;
    private String willTopic;
    private ByteBuffer willMessage;
    private String clientIdentifier;
    private boolean cleanSession;
    private String username;
    private ByteBuffer password;

    public ConnectPacket() {
        super(PacketType.CONNECT);
    }

    public void setProtocolLevel(int protocolLevel) {
        this.protocolLevel = protocolLevel;
    }

    public int getProtocolLevel() {
        return protocolLevel;
    }

    public void setKeepAlive(int n) {
        this.keepAlive = n;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public void setWillPresent(boolean willPresent) {
        this.willPresent = willPresent;
    }

    public boolean isWillPresent() {
        return willPresent;
    }

    public void setWillRetain(boolean willRetain) {
        this.willRetain = willRetain;
    }

    public boolean isWillRetain() {
        return willRetain;
    }

    public void setWillQoS(QoS willQoS) {
        this.willQoS = willQoS;
    }

    public QoS getWillQoS() {
        return willQoS;
    }

    public void setWillTopic(String willTopic) {
        this.willTopic = willTopic;
    }

    public String getWillTopic() {
        return willTopic;
    }

    public void setWillMessage(ByteBuffer willMessage) {
        this.willMessage = willMessage;
    }

    public ByteBuffer getWillMessage() {
        return willMessage;
    }

    public void setClientIdentifier(String clientIdentifier) {
        this.clientIdentifier = clientIdentifier;
    }

    public String getClientIdentifier() {
        return clientIdentifier;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(ByteBuffer password) {
        this.password = password;
    }

    public ByteBuffer getPassword() {
        return password;
    }
}
