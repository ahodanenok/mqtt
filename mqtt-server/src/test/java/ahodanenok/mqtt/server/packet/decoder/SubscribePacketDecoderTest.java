package ahodanenok.mqtt.server.packet.decoder;

import org.junit.jupiter.api.Test;

import ahodanenok.mqtt.server.QoS;
import ahodanenok.mqtt.server.TestUtils;
import ahodanenok.mqtt.server.packet.PacketType;
import ahodanenok.mqtt.server.packet.SubscribePacket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubscribePacketDecoderTest {

    @Test
    public void testDecode_OneTopicFilter() {
        SubscribePacketDecoder decoder = new SubscribePacketDecoder();
        SubscribePacket packet = decoder.decode(TestUtils.buf(
            0x82,
            0x8, // packetLength = 8
            0x0, 0xF, // packetIdentifier = 15
            0x0, 0x3, 0x61, 0x2F, 0x62, // topicFilter = a/b
            0x1 // requestedQoS = AT_LEAST_ONCE
            // 0x0, 0x3, 0x63, 0x2F, 0x64,
        ));

        assertEquals(PacketType.SUBSCRIBE, packet.getType());
        assertEquals(15, packet.getPacketIdentifier());
        assertEquals(1, packet.getSubscriptions().size());
        assertEquals("a/b", packet.getSubscriptions().get(0).getTopicFilter());
        assertEquals(QoS.AT_LEAST_ONCE, packet.getSubscriptions().get(0).getRequestedQoS());
    }

    @Test
    public void testDecode_MultipleTopicFilters() {
        SubscribePacketDecoder decoder = new SubscribePacketDecoder();
        SubscribePacket packet = decoder.decode(TestUtils.buf(
            0x82,
            0x15, // packetLength = 21
            0x0, 0x1, // packetIdentifier = 1
            0x0, 0x3, 0x61, 0x2F, 0x62, // topicFilter = a/b
            0x1, // requestedQoS = AT_LEAST_ONCE
            0x0, 0x3, 0x63, 0x2F, 0x64, // topicFilter = c/d
            0x2, // requestedQoS = EXACTLY_ONCE
            0x0, 0x4, 0x61, 0x62, 0x63, 0x64, // topicFilter = abcd
            0x0 // requestedQoS = AT_MOST_ONCE
        ));

        assertEquals(PacketType.SUBSCRIBE, packet.getType());
        assertEquals(1, packet.getPacketIdentifier());
        assertEquals(3, packet.getSubscriptions().size());
        assertEquals("a/b", packet.getSubscriptions().get(0).getTopicFilter());
        assertEquals(QoS.AT_LEAST_ONCE, packet.getSubscriptions().get(0).getRequestedQoS());
        assertEquals("c/d", packet.getSubscriptions().get(1).getTopicFilter());
        assertEquals(QoS.EXACTLY_ONCE, packet.getSubscriptions().get(1).getRequestedQoS());
        assertEquals("abcd", packet.getSubscriptions().get(2).getTopicFilter());
        assertEquals(QoS.AT_MOST_ONCE, packet.getSubscriptions().get(2).getRequestedQoS());
    }
}
