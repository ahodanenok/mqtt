package ahodanenok.mqtt.server.packet.decoder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import ahodanenok.mqtt.server.QoS;
import ahodanenok.mqtt.server.exception.InvalidPacketFormatException;
import ahodanenok.mqtt.server.packet.PacketType;
import ahodanenok.mqtt.server.packet.SubscribePacket;

public final class SubscribePacketDecoder implements PacketDecoder<SubscribePacket> {

    @Override
    public SubscribePacket decode(ByteBuffer buf) {
        SubscribePacket packet = new SubscribePacket();

        int b = buf.get();
        if (((b & 0xF0) >> 4) != PacketType.SUBSCRIBE.getValue()) {
            throw new InvalidPacketFormatException(
                "Expected %s (%d) packet, got: %d".formatted(
                    PacketType.SUBSCRIBE,
                    PacketType.SUBSCRIBE.getValue(),
                    ((b & 0xF0) >> 4)));
        }
        if ((b & 0xF) != 2) {
            throw new InvalidPacketFormatException("Expected flags to be 2, got: " + (b & 0xF));
        }

        int length = DecodeUtils.decodeVerifyPacketLength(buf);
        packet.setPacketIdentifier(DecodeUtils.decodeInteger16(buf));
        length -= 2;

        int pos;
        List<SubscribePacket.Subscription> subscriptions = new ArrayList<>();
        while (length > 0) {
            pos = buf.position();

            SubscribePacket.Subscription subscription = new SubscribePacket.Subscription();
            subscription.setTopicFilter(DecodeUtils.decodeString(buf));

            b = buf.get();
            if ((b & 0xFC) != 0) {
                throw new InvalidPacketFormatException(
                    "The upper 6 bits of the Requested QoS byte must be 0");
            }
            try {
                subscription.setRequestedQoS(QoS.from(b & 0x3));
            } catch (IllegalArgumentException e) {
                throw new InvalidPacketFormatException(
                    "Invalid QoS value: " + ((b & 0x18) >> 3));
            }

            subscriptions.add(subscription);
            length -= buf.position() - pos;
            if (length < 0) {
                throw new InvalidPacketFormatException("Payload exceeds packet length");
            }
        }
        packet.setSubscriptions(subscriptions);

        return packet;
    }
}
