package ahodanenok.mqtt.server.packet.encoder;

import java.nio.ByteBuffer;

import ahodanenok.mqtt.server.packet.MqttPacket;

public interface PacketEncoder<T extends MqttPacket> {

    void encode(T packet, ByteBuffer buf);
}
