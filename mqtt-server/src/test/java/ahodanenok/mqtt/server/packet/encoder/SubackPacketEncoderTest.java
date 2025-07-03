package ahodanenok.mqtt.server.packet.encoder;

import java.nio.ByteBuffer;
import java.util.List;

import org.junit.jupiter.api.Test;

import ahodanenok.mqtt.server.packet.SubackPacket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubackPacketEncoderTest {

    @Test
    public void testEncode_OneReturnCode() {
        SubackPacket packet = new SubackPacket();
        packet.setPacketIdentifier(123);
        packet.setReturnCodes(List.of(SubackPacket.ReturnCode.QOS_AT_LEAST_ONCE));
        ByteBuffer buf = ByteBuffer.allocate(5);
        new SubackPacketEncoder().encode(packet, buf);

        buf.rewind();
        assertEquals((byte) 0x90, buf.get());
        assertEquals((byte) 0x3, buf.get());
        assertEquals((byte) 0x00, buf.get());
        assertEquals((byte) 0x7B, buf.get());
        assertEquals((byte) 0x1, buf.get());
    }

    @Test
    public void testEncode_MultipleReturnCode() {
        SubackPacket packet = new SubackPacket();
        packet.setPacketIdentifier(456);
        packet.setReturnCodes(List.of(
            SubackPacket.ReturnCode.QOS_AT_LEAST_ONCE,
            SubackPacket.ReturnCode.QOS_AT_MOST_ONCE,
            SubackPacket.ReturnCode.FAILURE,
            SubackPacket.ReturnCode.QOS_EXACTLY_ONCE));
        ByteBuffer buf = ByteBuffer.allocate(8);
        new SubackPacketEncoder().encode(packet, buf);

        buf.rewind();
        assertEquals((byte) 0x90, buf.get());
        assertEquals((byte) 0x6, buf.get());
        assertEquals((byte) 0x01, buf.get());
        assertEquals((byte) 0xC8, buf.get());
        assertEquals((byte) 0x1, buf.get());
        assertEquals((byte) 0x0, buf.get());
        assertEquals((byte) 0x8, buf.get());
        assertEquals((byte) 0x2, buf.get());
    }
}
