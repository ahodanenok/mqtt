package ahodanenok.mqtt.server.packet.encoder;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import ahodanenok.mqtt.server.packet.PubackPacket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PubackPacketEncoderTest {

    @Test
    public void testEncode() {
        PubackPacket packet = new PubackPacket();
        packet.setPacketIdentifier(123);
        ByteBuffer buf = ByteBuffer.allocate(4);
        new PubackPacketEncoder().encode(packet, buf);

        buf.rewind();
        assertEquals(0x40, buf.get());
        assertEquals(0x2, buf.get());
        assertEquals(0x0, buf.get());
        assertEquals(0x7B, buf.get());
    }
}
