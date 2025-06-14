package ahodanenok.mqtt.server;

import java.nio.ByteBuffer;

public class TestUtils {

    public static ByteBuffer buf(int... bytes) {
        ByteBuffer buf = ByteBuffer.allocate(bytes.length);
        for (int b : bytes) {
            buf.put((byte) (b & 0xFF));
        }
        buf.rewind();

        return buf;
    }
}
