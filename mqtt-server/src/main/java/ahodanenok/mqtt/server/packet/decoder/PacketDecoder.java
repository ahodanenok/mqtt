package ahodanenok.mqtt.server.packet.decoder;

import java.nio.ByteBuffer;

public interface PacketDecoder<T> {

    T decode(ByteBuffer buf);
}
