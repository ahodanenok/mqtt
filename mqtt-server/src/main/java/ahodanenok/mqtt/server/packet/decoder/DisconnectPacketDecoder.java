package ahodanenok.mqtt.server.packet.decoder;

import java.nio.ByteBuffer;

import ahodanenok.mqtt.server.exception.InvalidPacketFormatException;
import ahodanenok.mqtt.server.packet.DisconnectPacket;
import ahodanenok.mqtt.server.packet.PacketType;

public final class DisconnectPacketDecoder implements PacketDecoder<DisconnectPacket> {

    @Override
    public DisconnectPacket decode(ByteBuffer buf) {
        int b = buf.get();
        if (((b & 0xF0) >> 4) != PacketType.DISCONNECT.getValue()) {
            throw new InvalidPacketFormatException(
                "Expected %s (%d) packet, got: %d".formatted(
                    PacketType.DISCONNECT,
                    PacketType.DISCONNECT.getValue(),
                    ((b & 0xF0) >> 4)));
        }
        if ((b & 0xF) != 0) {
            throw new InvalidPacketFormatException("Expected flags to be 0, got: " + (b & 0xF));
        }
        if (DecodeUtils.decodeVerifyPacketLength(buf) != 0) {
            throw new InvalidPacketFormatException("DISCONNECT packet length must be 0");
        }

        return new DisconnectPacket();
    }
}
