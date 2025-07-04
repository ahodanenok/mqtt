package ahodanenok.mqtt.server.packet;

public final class PingreqPacket extends MqttPacket {

    public PingreqPacket() {
        super(PacketType.PINGREQ);
    }
}
