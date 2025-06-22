package ahodanenok.mqtt.server.packet.encoder;

import java.nio.ByteBuffer;

import ahodanenok.mqtt.server.packet.ConnackPacket;

public final class ConnackPacketEncoder implements PacketEncoder<ConnackPacket> {

    @Override
    public void encode(ConnackPacket packet, ByteBuffer buf) {
        int b;

        buf.put((byte) (packet.getType().getValue() << 4));
        buf.put((byte) 2);
        if (packet.isSessionPresent()) {
            buf.put((byte) 1);
        } else {
            buf.put((byte) 0);
        }
        buf.put((byte) packet.getReturnCode().getValue());
    }
}
