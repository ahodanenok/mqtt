package ahodanenok.mqtt.server.packet.decoder;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import ahodanenok.mqtt.server.TestUtils;
import ahodanenok.mqtt.server.exception.InvalidPacketFormatException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DecodeUtilsTest {

    @Test
    public void testdecodePacketLength_Valid() {
        assertEquals(0, DecodeUtils.decodePacketLength(TestUtils.buf(0x0)));
        assertEquals(1, DecodeUtils.decodePacketLength(TestUtils.buf(0x1)));
        assertEquals(47, DecodeUtils.decodePacketLength(TestUtils.buf(0x2F)));
        assertEquals(101, DecodeUtils.decodePacketLength(TestUtils.buf(0x65)));
        assertEquals(127, DecodeUtils.decodePacketLength(TestUtils.buf(0x7F)));
        assertEquals(128, DecodeUtils.decodePacketLength(TestUtils.buf(0x80, 0x01)));
        assertEquals(934, DecodeUtils.decodePacketLength(TestUtils.buf(0xA6, 0x07)));
        assertEquals(5783, DecodeUtils.decodePacketLength(TestUtils.buf(0x97, 0x2D)));
        assertEquals(16383, DecodeUtils.decodePacketLength(TestUtils.buf(0xFF, 0x7F)));
        assertEquals(16384, DecodeUtils.decodePacketLength(TestUtils.buf(0x80, 0x80, 0x01)));
        assertEquals(78249, DecodeUtils.decodePacketLength(TestUtils.buf(0xA9, 0xE3, 0x04)));
        assertEquals(391742, DecodeUtils.decodePacketLength(TestUtils.buf(0xBE, 0xF4, 0x17)));
        assertEquals(1420398, DecodeUtils.decodePacketLength(TestUtils.buf(0xEE, 0xD8, 0x56)));
        assertEquals(2097151, DecodeUtils.decodePacketLength(TestUtils.buf(0xFF, 0xFF, 0x7F)));
        assertEquals(2097152, DecodeUtils.decodePacketLength(TestUtils.buf(0x80, 0x80, 0x80, 0x01)));
        assertEquals(8412756, DecodeUtils.decodePacketLength(TestUtils.buf(0xD4, 0xBC, 0x81, 0x04)));
        assertEquals(57821453, DecodeUtils.decodePacketLength(TestUtils.buf(0x8D, 0x92, 0xC9, 0x1B)));
        assertEquals(125123561, DecodeUtils.decodePacketLength(TestUtils.buf(0xE9, 0xF7, 0xD4, 0x3B)));
        assertEquals(268435455, DecodeUtils.decodePacketLength(TestUtils.buf(0xFF, 0xFF, 0xFF, 0x7F)));
    }

    @Test
    public void testdecodePacketLength_Invalid() {
        InvalidPacketFormatException ex;

        ex = assertThrows(InvalidPacketFormatException.class,
            () -> DecodeUtils.decodePacketLength(TestUtils.buf()));
        assertEquals("Expected packet length, but the buffer is empty", ex.getMessage());

        ex = assertThrows(InvalidPacketFormatException.class,
            () -> DecodeUtils.decodePacketLength(TestUtils.buf(0x80)));
        assertEquals("Expected more length bytes, but the buffer has no more bytes", ex.getMessage());

        ex = assertThrows(InvalidPacketFormatException.class,
            () -> DecodeUtils.decodePacketLength(TestUtils.buf(0xFF, 0xFF, 0xFF, 0x8F, 0x01)));
        assertEquals("Packet length is greater than the maximum packet length", ex.getMessage());
    }
}
