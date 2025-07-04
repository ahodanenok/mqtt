package ahodanenok.mqtt.server.packet.decoder;

import org.junit.jupiter.api.Test;

import ahodanenok.mqtt.server.TestUtils;
import ahodanenok.mqtt.server.packet.PacketType;
import ahodanenok.mqtt.server.packet.PingreqPacket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PingreqPacketDecoderTest {

    @Test
    public void testDecode() {
        PingreqPacketDecoder decoder = new PingreqPacketDecoder();
        PingreqPacket packet = decoder.decode(TestUtils.buf(0xC0, 0x0));
        assertEquals(PacketType.PINGREQ, packet.getType());
    }
}
