package ahodanenok.mqtt.server.packet.decoder;

import java.nio.ByteBuffer;

import ahodanenok.mqtt.server.exception.InvalidPacketFormatException;
import ahodanenok.mqtt.server.packet.PacketType;
import ahodanenok.mqtt.server.packet.PingreqPacket;

public final class PingreqPacketDecoder implements PacketDecoder<PingreqPacket> {

    @Override
    public PingreqPacket decode(ByteBuffer buf) {
        int b = buf.get();
        if (((b & 0xF0) >> 4) != PacketType.PINGREQ.getValue()) {
            throw new InvalidPacketFormatException(
                "Expected %s (%d) packet, got: %d".formatted(
                    PacketType.PINGREQ,
                    PacketType.PINGREQ.getValue(),
                    ((b & 0xF0) >> 4)));
        }
        if ((b & 0xF) != 0) {
            throw new InvalidPacketFormatException("Expected flags to be 0, got: " + (b & 0xF));
        }

        int length = DecodeUtils.decodeVerifyPacketLength(buf);
        if (length != 0) {
            throw new InvalidPacketFormatException("Expected length to be 0, got: " + length);
        }

        return new PingreqPacket();
    }
}
