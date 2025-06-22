package ahodanenok.mqtt.server.packet.encoder;

import java.nio.ByteBuffer;

import ahodanenok.mqtt.server.packet.PubackPacket;

public final class PubackPacketEncoder implements PacketEncoder<PubackPacket> {

    @Override
    public void encode(PubackPacket packet, ByteBuffer buf) {
        buf.put((byte) (packet.getType().getValue() << 4));
        buf.put((byte) 2);
        EncodeUtils.encodeInteger16(packet.getPacketidentifier(), buf);
    }
}
