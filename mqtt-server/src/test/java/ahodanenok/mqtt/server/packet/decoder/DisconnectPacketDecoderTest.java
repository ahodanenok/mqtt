package ahodanenok.mqtt.server.packet.decoder;

import org.junit.jupiter.api.Test;

import ahodanenok.mqtt.server.TestUtils;
import ahodanenok.mqtt.server.packet.DisconnectPacket;
import ahodanenok.mqtt.server.packet.PacketType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DisconnectPacketDecoderTest {

    @Test
    public void testDecode() {
        DisconnectPacketDecoder decoder = new DisconnectPacketDecoder();
        DisconnectPacket packet = decoder.decode(TestUtils.buf(0xE0, 0x0));
        assertEquals(PacketType.DISCONNECT, packet.getType());
    }
}
