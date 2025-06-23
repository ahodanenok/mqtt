package ahodanenok.mqtt.server.packet;

public final class DisconnectPacket extends MqttPacket {

    public DisconnectPacket() {
        super(PacketType.DISCONNECT);
    }
}
