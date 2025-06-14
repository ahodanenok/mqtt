package ahodanenok.mqtt.server.packet.decoder;

import java.nio.ByteBuffer;

import ahodanenok.mqtt.server.exception.InvalidPacketFormatException;
import ahodanenok.mqtt.server.packet.MqttPacket;

public final class DecodeUtils {

    public static int decodeLength(ByteBuffer buf) {
        int length = 0;
        int offset = 0;
        byte b;

        if (!buf.hasRemaining()) {
            throw new InvalidPacketFormatException("Expected packet length, but the buffer is empty");
        }

        do {
            b = buf.get();
            if (b < 0 && !buf.hasRemaining()) {
                throw new InvalidPacketFormatException("Expected more length bytes, but the buffer has no more bytes");
            }

            length |= (b & 0x7F) << offset;
            if (length > MqttPacket.MAX_LENGTH) {
                throw new InvalidPacketFormatException("Packet length is greater than the maximum packet length");
            }

            offset += 7;
            // todo: check and throw if the offset > 21?
        } while (b < 0);

        return length;
    }
}
