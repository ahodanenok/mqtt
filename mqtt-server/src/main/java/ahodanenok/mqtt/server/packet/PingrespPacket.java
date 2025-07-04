package ahodanenok.mqtt.server.packet;

public final class PingrespPacket extends MqttPacket {

    public PingrespPacket() {
        super(PacketType.PINGRESP);
    }
}
