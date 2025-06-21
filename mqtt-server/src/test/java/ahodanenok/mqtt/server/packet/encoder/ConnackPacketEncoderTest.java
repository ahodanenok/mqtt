package ahodanenok.mqtt.server.packet.encoder;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import ahodanenok.mqtt.server.ReturnCode;
import ahodanenok.mqtt.server.packet.ConnackPacket;
import ahodanenok.mqtt.server.packet.encoder.ConnackPacketEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConnackPacketEncoderTest {

    @Test
    public void testEncode() {
        ConnackPacket packet = new ConnackPacket();
        packet.setSessionPresent(true);
        packet.setReturnCode(ReturnCode.ACCEPTED);

        ByteBuffer buf = ByteBuffer.allocate(4);
        new ConnackPacketEncoder().encode(packet, buf);

        buf.rewind();
        assertEquals(0x20, buf.get());
        assertEquals(0x2, buf.get());
        assertEquals(0x1, buf.get());
        assertEquals(0x0, buf.get());
    }
}