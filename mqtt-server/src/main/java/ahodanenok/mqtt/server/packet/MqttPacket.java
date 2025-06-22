package ahodanenok.mqtt.server.packet;

public abstract class MqttPacket {

    public static final int MAX_LENGTH = 268435455;

    private PacketType type;

    protected MqttPacket(PacketType type) {
        this.type = type;
    }

    public PacketType getType() {
        return type;
    }
}
