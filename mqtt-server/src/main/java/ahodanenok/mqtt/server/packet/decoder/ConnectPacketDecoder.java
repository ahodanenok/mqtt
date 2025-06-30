package ahodanenok.mqtt.server.packet.decoder;

import java.nio.ByteBuffer;

import ahodanenok.mqtt.server.QoS;
import ahodanenok.mqtt.server.packet.PacketType;
import ahodanenok.mqtt.server.exception.InvalidPacketFormatException;
import ahodanenok.mqtt.server.exception.UnsupportedProtocolLevelException;
import ahodanenok.mqtt.server.packet.ConnectPacket;

public class ConnectPacketDecoder implements PacketDecoder<ConnectPacket> {

    public ConnectPacket decode(ByteBuffer buf) {
        try {
            return doDecode(buf);
        } catch (Exception e) {
            throw e; // todo: what exception to throw?
        }
    }

    private ConnectPacket doDecode(ByteBuffer buf) {
        ConnectPacket packet = new ConnectPacket();

        int b;
        int n;

        b = buf.get();
        if (((b & 0xF0) >> 4) != PacketType.CONNECT.getValue()) {
            throw new InvalidPacketFormatException(
                "Expected %s (%d) packet, got: %d".formatted(
                    PacketType.CONNECT,
                    PacketType.CONNECT.getValue(),
                    ((b & 0xF0) >> 4)));
        }
        if ((b & 0xF) != 0) {
            throw new InvalidPacketFormatException("Expected flags to be 0, got: " + (b & 0xF));
        }

        DecodeUtils.decodeVerifyPacketLength(buf);

        // protocol name
        String protocolName = DecodeUtils.decodeString(buf);
        if (!protocolName.equals("MQTT")) {
            throw new InvalidPacketFormatException("Incorrect protocol name: " + protocolName);
        }

        packet.setProtocolLevel(buf.get());

        boolean usernamePresent = false;
        boolean passwordPresent = false;

        // connect flags
        b = buf.get();
        if ((b & 0x1) != 0) {
            throw new InvalidPacketFormatException("Reserved flag must be 0");
        }
        if ((b & 0x2) != 0) {
            packet.setCleanSession(true);
        }
        if ((b & 0x4) != 0) {
            packet.setWillPresent(true);
            try {
                packet.setWillQoS(QoS.from((b & 0x18) >> 3));
            } catch (IllegalArgumentException e) {
                throw new InvalidPacketFormatException(
                    "Invalid QoS value: " + ((b & 0x18) >> 3));
            }
            if ((b & 0x20) != 0) {
                packet.setWillRetain(true);
            }
        } else {
            if ((b & 0x18) != 0) {
                throw new InvalidPacketFormatException("Will Present is 0, Will QoS must be 0");
            }
            if ((b & 0x20) != 0) {
                throw new InvalidPacketFormatException("Will Present is 0, Will Retain must be 0");
            }
        }
        if ((b & 0x40) != 0) {
            passwordPresent = true;
        }
        if ((b & 0x80) != 0) {
            usernamePresent = true;
        }
        if (!usernamePresent && passwordPresent) {
            throw new InvalidPacketFormatException("User Name Flag is 0, Password Flag must be 0");
        }

        // keep alive
        packet.setKeepAlive(DecodeUtils.decodeInteger16(buf));

        // client identifier
        // todo: auto assigned id if length is zero
        packet.setClientIdentifier(DecodeUtils.decodeString(buf));
        if (packet.getClientIdentifier().isEmpty()) {
            // if (!packet.isCleanSession()) {
            //     throw new InvalidPacketFormatException("ClientId is empty, Clean Session must be 1");
            // }
        }

        if (packet.isWillPresent()) {
            packet.setWillTopic(DecodeUtils.decodeString(buf));

            // will message
            n = DecodeUtils.decodeInteger16(buf);
            packet.setWillMessage(buf.slice(buf.position(), n));
            if (packet.getWillMessage().remaining() != n) {
                throw new InvalidPacketFormatException(
                    "Expected %d bytes in Will Message, got %d"
                        .formatted(n, packet.getWillMessage().remaining()));
            }

            buf.position(buf.position() + n);
        }

        if (usernamePresent) {
            packet.setUsername(DecodeUtils.decodeString(buf));
            if (passwordPresent) {
                n = DecodeUtils.decodeInteger16(buf);
                packet.setPassword(buf.slice(buf.position(), n));
                if (packet.getPassword().remaining() != n) {
                    throw new InvalidPacketFormatException(
                        "Expected %d bytes in Password, got %d"
                            .formatted(n, packet.getPassword().remaining()));
                }

                buf.position(buf.position() + n);
            }
        }

        return packet;
    }
}
