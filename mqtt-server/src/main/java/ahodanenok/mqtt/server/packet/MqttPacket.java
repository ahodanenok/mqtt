package ahodanenok.mqtt.server.packet;

public abstract class MqttPacket {

    public static final int MAX_LENGTH = 268435455;

    private PacketType type;
    private int length;

    protected MqttPacket(PacketType type) {
        this.type = type;
    }

    public PacketType getType() {
        return type;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}
