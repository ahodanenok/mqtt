package ahodanenok.mqtt.server;

import ahodanenok.mqtt.server.packet.ConnackPacket;
import ahodanenok.mqtt.server.packet.ConnectPacket;
import ahodanenok.mqtt.server.packet.DisconnectPacket;
import ahodanenok.mqtt.server.packet.PacketType;
import ahodanenok.mqtt.server.packet.PublishPacket;
import ahodanenok.mqtt.server.packet.MqttPacket;

public class MqttProtocol {

    private final ClientConnection connection;

    public MqttProtocol(ClientConnection connection) {
        this.connection = connection;
    }

    public void onPacket(MqttPacket packet) {
        switch (packet.getType()) {
            case CONNECT -> onConnect((ConnectPacket) packet);
            case PUBLISH -> onPublish((PublishPacket) packet);
            case DISCONNECT -> onDisconnect((DisconnectPacket) packet);
            default -> throw new IllegalStateException();
        }
    }

    private void onConnect(ConnectPacket packet) {
        System.out.println("!!! ConnectPacket");
        System.out.println("!!!   keepAlive=" + packet.getKeepAlive());
        System.out.println("!!!   willPresent=" + packet.isWillPresent());
        System.out.println("!!!   willRetain=" + packet.isWillRetain());
        System.out.println("!!!   willQoS=" + packet.getWillQoS());
        System.out.println("!!!   willTopic=" + packet.getWillTopic());
        System.out.println("!!!   willMessage=" + packet.getWillMessage());
        System.out.println("!!!   clientIdentifier=" + packet.getClientIdentifier());
        System.out.println("!!!   cleanSession=" + packet.isCleanSession());
        System.out.println("!!!   username=" + packet.getUsername());
        System.out.println("!!!   password=" + packet.getPassword());

        Client client = new Client();
        client.setConnection(connection);

        ConnackPacket response = new ConnackPacket();
        response.setReturnCode(ReturnCode.ACCEPTED);
        response.setSessionPresent(false);
        client.getConnection().send(response);
    }

    private void onDisconnect(DisconnectPacket packet) {
        //client.getConnection().close();
    }

    private void onPublish(PublishPacket packet) {
        System.out.println("!!! PublishPacket");
        System.out.println("!!!   duplicated=" + packet.isDuplicated());
        System.out.println("!!!   qos=" + packet.getQoS());
        System.out.println("!!!   retain=" + packet.isRetain());
        System.out.println("!!!   topicName=" + packet.getTopicName());
        System.out.println("!!!   packetId=" + packet.getPacketIdentifier());
        System.out.println("!!!   payload=" + packet.getPayload());
    }
}
