package ahodanenok.mqtt.server.packet.decoder;

import org.junit.jupiter.api.Test;

import ahodanenok.mqtt.server.QoS;
import ahodanenok.mqtt.server.TestUtils;
import ahodanenok.mqtt.server.exception.InvalidPacketFormatException;
import ahodanenok.mqtt.server.packet.PacketType;
import ahodanenok.mqtt.server.packet.PublishPacket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PublishPacketDecoderTest {

    @Test
    public void testDecode_Duplicated_False() {
        PublishPacketDecoder decoder = new PublishPacketDecoder();
        PublishPacket packet = decoder.decode(TestUtils.buf(
            0x31,
            0x7, // length = 7
            0x0, 0x3, 0x61, 0x62, 0x63, // topic name = abc
            0x1, 0x2 // payload
        ));

        assertEquals(PacketType.PUBLISH, packet.getType());
        assertFalse(packet.isDuplicated());
        assertEquals(QoS.AT_MOST_ONCE, packet.getQoS());
        assertTrue(packet.isRetain());
        assertEquals("abc", packet.getTopicName());
        assertEquals(-1, packet.getPacketIdentifier());
        assertEquals(TestUtils.buf(0x1, 0x2), packet.getPayload());
    }

    @Test
    public void testDecode_Duplicated_True() {
        PublishPacketDecoder decoder = new PublishPacketDecoder();
        PublishPacket packet = decoder.decode(TestUtils.buf(
            0x39,
            0x7, // length = 7
            0x0, 0x3, 0x61, 0x62, 0x63, // topic name = abc
            0x1, 0x2 // payload
        ));

        assertEquals(PacketType.PUBLISH, packet.getType());
        assertTrue(packet.isDuplicated());
        assertEquals(QoS.AT_MOST_ONCE, packet.getQoS());
        assertTrue(packet.isRetain());
        assertEquals("abc", packet.getTopicName());
        assertEquals(-1, packet.getPacketIdentifier());
        assertEquals(TestUtils.buf(0x1, 0x2), packet.getPayload());
    }

    @Test
    public void testDecode_QoS_0() {
        PublishPacketDecoder decoder = new PublishPacketDecoder();
        PublishPacket packet = decoder.decode(TestUtils.buf(
            0x39,
            0xA, // length = 10
            0x0, 0x3, 0x61, 0x62, 0x63, // topic name = abc
            0x1, 0x2, 0x3, 0x4, 0x5 // payload
        ));

        assertEquals(PacketType.PUBLISH, packet.getType());
        assertTrue(packet.isDuplicated());
        assertEquals(QoS.AT_MOST_ONCE, packet.getQoS());
        assertTrue(packet.isRetain());
        assertEquals("abc", packet.getTopicName());
        assertEquals(-1, packet.getPacketIdentifier());
        assertEquals(TestUtils.buf(0x1, 0x2, 0x3, 0x4, 0x5), packet.getPayload());
    }

    @Test
    public void testDecode_QoS_1() {
        PublishPacketDecoder decoder = new PublishPacketDecoder();
        PublishPacket packet = decoder.decode(TestUtils.buf(
            0x3B,
            0xC, // length = 12
            0x0, 0x3, 0x61, 0x62, 0x63, // topic name = abc
            0x0, 0x1, // client identifier = 1
            0x1, 0x2, 0x3, 0x4, 0x5 // payload
        ));

        assertEquals(PacketType.PUBLISH, packet.getType());
        assertTrue(packet.isDuplicated());
        assertEquals(QoS.AT_LEAST_ONCE, packet.getQoS());
        assertTrue(packet.isRetain());
        assertEquals("abc", packet.getTopicName());
        assertEquals(1, packet.getPacketIdentifier());
        assertEquals(TestUtils.buf(0x1, 0x2, 0x3, 0x4, 0x5), packet.getPayload());
    }

    @Test
    public void testDecode_QoS_2() {
        PublishPacketDecoder decoder = new PublishPacketDecoder();
        PublishPacket packet = decoder.decode(TestUtils.buf(
            0x3D,
            0xC, // length = 12
            0x0, 0x3, 0x61, 0x62, 0x63, // topic name = abc
            0x0, 0x1, // client identifier = 1
            0x1, 0x2, 0x3, 0x4, 0x5 // payload
        ));

        assertEquals(PacketType.PUBLISH, packet.getType());
        assertTrue(packet.isDuplicated());
        assertEquals(QoS.EXACTLY_ONCE, packet.getQoS());
        assertTrue(packet.isRetain());
        assertEquals("abc", packet.getTopicName());
        assertEquals(1, packet.getPacketIdentifier());
        assertEquals(TestUtils.buf(0x1, 0x2, 0x3, 0x4, 0x5), packet.getPayload());
    }

    @Test
    public void testDecode_QoS_3() {
        PublishPacketDecoder decoder = new PublishPacketDecoder();
        InvalidPacketFormatException ex = assertThrows(
            InvalidPacketFormatException.class,
            () -> decoder.decode(TestUtils.buf(
                0x3F,
                0xC, // length = 12
                0x0, 0x3, 0x61, 0x62, 0x63, // topic name = abc
                0x0, 0x1, // client identifier = 1
                0x1, 0x2, 0x3, 0x4, 0x5 // payload
            )));
        assertEquals("Invalid QoS value: 3", ex.getMessage());
    }

    @Test
    public void testDecode_Retain_False() {
        PublishPacketDecoder decoder = new PublishPacketDecoder();
        PublishPacket packet = decoder.decode(TestUtils.buf(
            0x38,
            0x4, // length = 4
            0x0, 0x1, 0x61, // topic name = a
            0x1 // payload
        ));

        assertEquals(PacketType.PUBLISH, packet.getType());
        assertTrue(packet.isDuplicated());
        assertEquals(QoS.AT_MOST_ONCE, packet.getQoS());
        assertFalse(packet.isRetain());
        assertEquals("a", packet.getTopicName());
        assertEquals(-1, packet.getPacketIdentifier());
        assertEquals(TestUtils.buf(0x1), packet.getPayload());
    }

    @Test
    public void testDecode_Retain_True() {
        PublishPacketDecoder decoder = new PublishPacketDecoder();
        PublishPacket packet = decoder.decode(TestUtils.buf(
            0x39,
            0x4, // length = 4
            0x0, 0x1, 0x61, // topic name = a
            0x1 // payload
        ));

        assertEquals(PacketType.PUBLISH, packet.getType());
        assertTrue(packet.isDuplicated());
        assertEquals(QoS.AT_MOST_ONCE, packet.getQoS());
        assertTrue(packet.isRetain());
        assertEquals("a", packet.getTopicName());
        assertEquals(-1, packet.getPacketIdentifier());
        assertEquals(TestUtils.buf(0x1), packet.getPayload());
    }

    @Test
    public void testDecode_Payload_Empty() {
        PublishPacketDecoder decoder = new PublishPacketDecoder();
        PublishPacket packet = decoder.decode(TestUtils.buf(
            0x30,
            0x3, // length = 3
            0x0, 0x1, 0x61 // topic name = a
        ));

        assertEquals(PacketType.PUBLISH, packet.getType());
        assertFalse(packet.isDuplicated());
        assertEquals(QoS.AT_MOST_ONCE, packet.getQoS());
        assertFalse(packet.isRetain());
        assertEquals("a", packet.getTopicName());
        assertEquals(-1, packet.getPacketIdentifier());
        assertEquals(TestUtils.buf(), packet.getPayload());
    }
}
