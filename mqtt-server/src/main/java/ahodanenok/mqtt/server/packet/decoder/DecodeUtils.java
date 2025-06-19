package ahodanenok.mqtt.server.packet.decoder;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import ahodanenok.mqtt.server.exception.InvalidPacketFormatException;
import ahodanenok.mqtt.server.packet.MqttPacket;

public final class DecodeUtils {

    public static int decodePacketLength(ByteBuffer buf) {
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

    public static int decodeLength16(ByteBuffer buf) {
        return ((buf.get() & 0xFF) << 8) | (buf.get() & 0xFF);
    }

    public static String decodeString(ByteBuffer buf) {
        int length = (buf.get() << 8) | buf.get();
        // todo: implement utf8 decoding?
        ByteBuffer chars = ByteBuffer.allocate(length);
        for (int n = 0; n < length; n++) {
            chars.put(buf.get());
        }

        return new String(chars.array(), StandardCharsets.UTF_8);
    }

    public static void expect(ByteBuffer buf, byte... bytes) {
        for (byte b : bytes) {
            if (!buf.hasRemaining()) {
                throw new InvalidPacketFormatException("Unexpected end of packet");
            }

            if (buf.get() != b) {
                throw new InvalidPacketFormatException("todo");
            }
        }
    }
}
