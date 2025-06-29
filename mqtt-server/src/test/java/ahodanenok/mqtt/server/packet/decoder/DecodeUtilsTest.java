package ahodanenok.mqtt.server.packet.decoder;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import ahodanenok.mqtt.server.TestUtils;
import ahodanenok.mqtt.server.packet.PacketLength;
import ahodanenok.mqtt.server.exception.InvalidPacketFormatException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DecodeUtilsTest {

    @Test
    public void testdecodePacketLength_Valid() {
        assertEquals(new PacketLength(0, 1), DecodeUtils.decodePacketLength(TestUtils.buf(0x0)));
        assertEquals(new PacketLength(1, 1), DecodeUtils.decodePacketLength(TestUtils.buf(0x1)));
        assertEquals(new PacketLength(47, 1), DecodeUtils.decodePacketLength(TestUtils.buf(0x2F)));
        assertEquals(new PacketLength(101, 1), DecodeUtils.decodePacketLength(TestUtils.buf(0x65)));
        assertEquals(new PacketLength(127, 1), DecodeUtils.decodePacketLength(TestUtils.buf(0x7F)));
        assertEquals(new PacketLength(128, 2), DecodeUtils.decodePacketLength(TestUtils.buf(0x80, 0x01)));
        assertEquals(new PacketLength(934, 2), DecodeUtils.decodePacketLength(TestUtils.buf(0xA6, 0x07)));
        assertEquals(new PacketLength(5783, 2), DecodeUtils.decodePacketLength(TestUtils.buf(0x97, 0x2D)));
        assertEquals(new PacketLength(16383, 2), DecodeUtils.decodePacketLength(TestUtils.buf(0xFF, 0x7F)));
        assertEquals(new PacketLength(16384, 3), DecodeUtils.decodePacketLength(TestUtils.buf(0x80, 0x80, 0x01)));
        assertEquals(new PacketLength(78249, 3), DecodeUtils.decodePacketLength(TestUtils.buf(0xA9, 0xE3, 0x04)));
        assertEquals(new PacketLength(391742, 3), DecodeUtils.decodePacketLength(TestUtils.buf(0xBE, 0xF4, 0x17)));
        assertEquals(new PacketLength(1420398, 3), DecodeUtils.decodePacketLength(TestUtils.buf(0xEE, 0xD8, 0x56)));
        assertEquals(new PacketLength(2097151, 3), DecodeUtils.decodePacketLength(TestUtils.buf(0xFF, 0xFF, 0x7F)));
        assertEquals(new PacketLength(2097152, 4), DecodeUtils.decodePacketLength(TestUtils.buf(0x80, 0x80, 0x80, 0x01)));
        assertEquals(new PacketLength(8412756, 4), DecodeUtils.decodePacketLength(TestUtils.buf(0xD4, 0xBC, 0x81, 0x04)));
        assertEquals(new PacketLength(57821453, 4), DecodeUtils.decodePacketLength(TestUtils.buf(0x8D, 0x92, 0xC9, 0x1B)));
        assertEquals(new PacketLength(125123561, 4), DecodeUtils.decodePacketLength(TestUtils.buf(0xE9, 0xF7, 0xD4, 0x3B)));
        assertEquals(new PacketLength(268435455, 4), DecodeUtils.decodePacketLength(TestUtils.buf(0xFF, 0xFF, 0xFF, 0x7F)));
    }

    @Test
    public void testdecodePacketLength_Invalid() {
        InvalidPacketFormatException ex;

        // ex = assertThrows(InvalidPacketFormatException.class,
        //     () -> DecodeUtils.decodePacketLength(TestUtils.buf()));
        // assertEquals("Expected packet length, but the buffer is empty", ex.getMessage());
        assertNull(DecodeUtils.decodePacketLength(TestUtils.buf()));

        // ex = assertThrows(InvalidPacketFormatException.class,
        //     () -> DecodeUtils.decodePacketLength(TestUtils.buf(0x80)));
        // assertEquals("Expected more length bytes, but the buffer has no more bytes", ex.getMessage());
        assertNull(DecodeUtils.decodePacketLength(TestUtils.buf(0x80)));

        ex = assertThrows(InvalidPacketFormatException.class,
            () -> DecodeUtils.decodePacketLength(TestUtils.buf(0xFF, 0xFF, 0xFF, 0x8F, 0x01)));
        assertEquals("Packet length is greater than the maximum packet length", ex.getMessage());
    }
}
