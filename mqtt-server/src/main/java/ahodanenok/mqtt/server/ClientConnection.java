package ahodanenok.mqtt.server;

import java.nio.ByteBuffer;

import ahodanenok.mqtt.server.packet.MqttPacket;
import ahodanenok.mqtt.server.packet.encoder.PacketEncoders;

public class ClientConnection {

    private final DataConnection connection;
    private final PacketEncoders packetEncoders;
    // todo: adjust buffer size dynamically
    private ByteBuffer outputBuf = ByteBuffer.allocate(128);

    public ClientConnection(
            DataConnection connection,
            PacketEncoders packetEncoders) {
        this.connection = connection;
        this.packetEncoders = packetEncoders;
    }

    public void send(MqttPacket packet) {
        outputBuf.clear();
        packetEncoders.encode(packet, outputBuf);
        outputBuf.flip();
        connection.send(outputBuf);
    }

    public void close() {
        connection.close();
    }
}
