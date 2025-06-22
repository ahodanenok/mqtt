package ahodanenok.mqtt.server.packet.encoder;

import java.nio.ByteBuffer;

public final class EncodeUtils {

    public static void encodeInteger16(int n, ByteBuffer buf) {
        buf.put((byte) ((n & 0xFF00) >> 8));
        buf.put((byte) (n & 0xFF));
    }
}
