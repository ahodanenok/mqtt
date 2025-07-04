package ahodanenok.mqtt.server.packet.encoder;

import java.nio.ByteBuffer;

import ahodanenok.mqtt.server.packet.PingrespPacket;

public final class PingrespPacketEncoder implements PacketEncoder<PingrespPacket> {

    @Override
    public void encode(PingrespPacket packet, ByteBuffer buf) {
        buf.put((byte) (packet.getType().getValue() << 4));
        buf.put((byte) 0);
    }
}
