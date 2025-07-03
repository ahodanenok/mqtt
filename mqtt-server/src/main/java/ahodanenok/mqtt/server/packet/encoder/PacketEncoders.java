package ahodanenok.mqtt.server.packet.encoder;

import java.nio.ByteBuffer;

import ahodanenok.mqtt.server.exception.MqttServerException;
import ahodanenok.mqtt.server.packet.PacketType;
import ahodanenok.mqtt.server.packet.ConnackPacket;
import ahodanenok.mqtt.server.packet.MqttPacket;
import ahodanenok.mqtt.server.packet.PubackPacket;
import ahodanenok.mqtt.server.packet.SubackPacket;

public final class PacketEncoders {

    private final ConnackPacketEncoder connackPacketEncoder;
    private final PubackPacketEncoder pubackPacketEncoder;
    private final SubackPacketEncoder subackPacketEncoder;

    public PacketEncoders() {
        this.connackPacketEncoder = new ConnackPacketEncoder();
        this.pubackPacketEncoder = new PubackPacketEncoder();
        this.subackPacketEncoder = new SubackPacketEncoder();
    }

    public void encode(MqttPacket packet, ByteBuffer buf) {
        PacketType packetType = packet.getType();
        switch (packetType) {
            case CONNACK -> connackPacketEncoder.encode((ConnackPacket) packet, buf);
            case PUBACK -> pubackPacketEncoder.encode((PubackPacket) packet, buf);
            case SUBACK -> subackPacketEncoder.encode((SubackPacket) packet, buf);
            default -> throw new MqttServerException(
                "Unexpected packet of type '%d'".formatted(packetType));
        }
    }
}
