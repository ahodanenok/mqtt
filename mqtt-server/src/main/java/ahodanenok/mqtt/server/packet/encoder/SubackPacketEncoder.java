package ahodanenok.mqtt.server.packet.encoder;

import java.nio.ByteBuffer;

import ahodanenok.mqtt.server.packet.SubackPacket;

public class SubackPacketEncoder implements PacketEncoder<SubackPacket> {

    @Override
    public void encode(SubackPacket packet, ByteBuffer buf) {
        buf.put((byte) (packet.getType().getValue() << 4));
        buf.put((byte) (2 + packet.getReturnCodes().size()));
        EncodeUtils.encodeInteger16(packet.getPacketIdentifier(), buf);
        for (SubackPacket.ReturnCode returnCode : packet.getReturnCodes()) {
            buf.put((byte) returnCode.getValue());
        }
    }
}
