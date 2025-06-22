package ahodanenok.mqtt.server.packet.decoder;

import ahodanenok.mqtt.server.TestUtils;
import ahodanenok.mqtt.server.QoS;
import ahodanenok.mqtt.server.packet.ConnectPacket;
import ahodanenok.mqtt.server.packet.PacketType;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConnectPacketDecoderTest {

    @Test
    public void testDecode_Valid_AllFieldsPresent() {
        ConnectPacketDecoder decoder = new ConnectPacketDecoder();
        ConnectPacket packet = decoder.decode(TestUtils.buf(
            0x10,
            0x23,
            0x00, 0x04, 0x4D, 0x51, 0x54, 0x54, // protocol name
            0x04, // protocol level
            0xEE, // connect flags
            0x0F, 0xF0, // keep alive = 4080
            0x00, 0x02, 0x43, 0x31, // client identifier = C1
            0x00, 0x04, 0x77, 0x74, 0x2D, 0x31, // will topic = wt-1
            0x00, 0x03, 0x01, 0x02, 0x03, // will message
            0x00, 0x05, 0x61, 0x64, 0x6D, 0x69, 0x6E, // user name = admin
            0x00, 0x01, 0x31 // password
        ));

        assertEquals(PacketType.CONNECT, packet.getType());
        assertEquals(4080, packet.getKeepAlive());
        assertTrue(packet.isWillPresent());
        assertTrue(packet.isWillRetain());
        assertEquals(QoS.AT_LEAST_ONCE, packet.getWillQoS());
        assertEquals("wt-1", packet.getWillTopic());
        assertEquals(TestUtils.buf(0x01, 0x02, 0x03), packet.getWillMessage());
        assertEquals("C1", packet.getClientIdentifier());
        assertTrue(packet.isCleanSession());
        assertEquals("admin", packet.getUsername());
        assertEquals(TestUtils.buf(0x31), packet.getPassword());
    }
}
