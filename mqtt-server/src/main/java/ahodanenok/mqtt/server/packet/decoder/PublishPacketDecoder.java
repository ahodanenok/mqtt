package ahodanenok.mqtt.server.packet.decoder;

import java.nio.ByteBuffer;

import ahodanenok.mqtt.server.QoS;
import ahodanenok.mqtt.server.exception.InvalidPacketFormatException;
import ahodanenok.mqtt.server.packet.PacketType;
import ahodanenok.mqtt.server.packet.PublishPacket;

public final class PublishPacketDecoder implements PacketDecoder<PublishPacket> {

    @Override
    public PublishPacket decode(ByteBuffer buf) {
        PublishPacket packet = new PublishPacket();

        int b = buf.get();
        if (((b & 0xF0) >> 4) != PacketType.PUBLISH.getValue()) {
            throw new InvalidPacketFormatException(
                "Expected %s (%d) packet, got: %d".formatted(
                    PacketType.PUBLISH,
                    PacketType.PUBLISH.getValue(),
                    ((b & 0xF0) >> 4)));
        }

        packet.setDuplicated((b & 0x8) != 0);
        try {
            packet.setQoS(QoS.from((b & 0x6) >> 1));
        } catch (IllegalArgumentException e) {
            throw new InvalidPacketFormatException(
                "Invalid QoS value: " + ((b & 0x6) >> 1));
        }
        packet.setRetain((b & 0x1) != 0);

        int remainingLength = DecodeUtils.decodeVerifyPacketLength(buf);
        int fixedHeaderLength = buf.position();
        packet.setTopicName(DecodeUtils.decodeString(buf));
        if (packet.getQoS() == QoS.AT_LEAST_ONCE || packet.getQoS() == QoS.EXACTLY_ONCE) {
            packet.setPacketIdentifier(DecodeUtils.decodeInteger16(buf));
        } else {
            packet.setPacketIdentifier(-1);
        }
        packet.setPayload(buf.slice(
            buf.position(),
            remainingLength - buf.position() + fixedHeaderLength));

        return packet;
    }
}
