package ahodanenok.mqtt.server.packet.decoder;

import java.nio.ByteBuffer;

import ahodanenok.mqtt.server.packet.MqttPacket;

public interface PacketDecoder<T extends MqttPacket> {

    T decode(ByteBuffer buf);
}
