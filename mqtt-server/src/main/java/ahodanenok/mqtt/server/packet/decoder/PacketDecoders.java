package ahodanenok.mqtt.server.packet.decoder;

import java.nio.ByteBuffer;

import ahodanenok.mqtt.server.exception.MqttServerException;
import ahodanenok.mqtt.server.packet.PacketType;
import ahodanenok.mqtt.server.packet.MqttPacket;

public final class PacketDecoders {

    private final ConnectPacketDecoder connectPacketDecoder;
    private final DisconnectPacketDecoder disconnectPacketDecoder;
    private final PublishPacketDecoder publishPacketDecoder;

    public PacketDecoders() {
        this.connectPacketDecoder = new ConnectPacketDecoder();
        this.disconnectPacketDecoder = new DisconnectPacketDecoder();
        this.publishPacketDecoder = new PublishPacketDecoder();
    }

    public MqttPacket decode(ByteBuffer buf) {
        PacketType packetType = PacketType.from((buf.get(0) & 0xF0) >> 4);
        return switch (packetType) {
            case CONNECT -> connectPacketDecoder.decode(buf);
            case DISCONNECT -> disconnectPacketDecoder.decode(buf);
            case PUBLISH -> publishPacketDecoder.decode(buf);
            default -> throw new MqttServerException(
                "Unexpected packet of type '%d'".formatted(packetType));
        };
    }
}
