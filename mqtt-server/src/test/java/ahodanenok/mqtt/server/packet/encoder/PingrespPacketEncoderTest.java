package ahodanenok.mqtt.server.packet.encoder;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import ahodanenok.mqtt.server.packet.PingrespPacket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PingrespPacketEncoderTest {

    @Test
    public void testEncode() {
        PingrespPacket packet = new PingrespPacket();
        ByteBuffer buf = ByteBuffer.allocate(2);
        new PingrespPacketEncoder().encode(packet, buf);

        buf.rewind();
        assertEquals((byte) 0xD0, buf.get());
        assertEquals((byte) 0x0, buf.get());
    }
}
