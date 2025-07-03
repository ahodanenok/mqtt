package ahodanenok.mqtt.server;

import java.nio.ByteBuffer;
import java.util.UUID;

import ahodanenok.mqtt.server.packet.ConnackPacket;
import ahodanenok.mqtt.server.packet.ConnectPacket;
import ahodanenok.mqtt.server.packet.DisconnectPacket;
import ahodanenok.mqtt.server.packet.PacketType;
import ahodanenok.mqtt.server.packet.SubscribePacket;
import ahodanenok.mqtt.server.packet.SubackPacket;
import ahodanenok.mqtt.server.packet.PublishPacket;
import ahodanenok.mqtt.server.packet.MqttPacket;
import ahodanenok.mqtt.server.session.Session;
import ahodanenok.mqtt.server.session.SessionManager;
import ahodanenok.mqtt.server.session.Subscription;

public class MqttProtocol {

    private final ClientManager clientManager;
    private final SessionManager sessionManager;
    private ClientConnection connection;
    private Client client;
    private boolean connected;

    public MqttProtocol(
            ClientManager clientManager,
            SessionManager sessionManager,
            ClientConnection connection) {
        this.clientManager = clientManager;
        this.sessionManager = sessionManager;
        this.connection = connection;
    }

    public void onPacket(MqttPacket packet) {
        switch (packet.getType()) {
            case CONNECT -> onConnect((ConnectPacket) packet);
            case PUBLISH -> onPublish((PublishPacket) packet);
            case SUBSCRIBE -> onSubscribe((SubscribePacket) packet);
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

        if (connected) {
            connection.close();
            return;
        }

        if (packet.getProtocolLevel() != 4) {
            ConnackPacket response = new ConnackPacket();
            response.setReturnCode(ReturnCode.UNACCEPTABLE_PROTOCOL_VERSION);
            connection.send(response);
            connection.close();
            return;
        }

        // 2. The Server MUST validate that the CONNECT Packet conforms to section 3.1 and close the Network Connection without sending a CONNACK if it does not conform [MQTT-3.1.4-1].
        String clientIdentifier = packet.getClientIdentifier();
        if (clientIdentifier.isEmpty()) {
            if (!packet.isCleanSession()) {
                ConnackPacket response = new ConnackPacket();
                response.setReturnCode(ReturnCode.IDENTIFIER_REJECTED);
                connection.send(response);
                connection.close();
                return;
            }

            clientIdentifier = UUID.randomUUID().toString();
        }

        // 3. The Server MAY check that the contents of the CONNECT Packet meet any further restrictions and MAY perform authentication and authorization checks.
        // todo: additional actions

        // If validation is successful the Server performs the following steps.
        // 1. If the ClientId represents a Client already connected to the Server then the Server MUST disconnect the existing Client [MQTT-3.1.4-2].
        Client existingClient = clientManager.getClient(clientIdentifier);
        if (existingClient != null) {
            existingClient.getConnection().close();
        }
        // 2. The Server MUST perform the processing of CleanSession that is described in section 3.1.2.4
        if (packet.isCleanSession()) {
            sessionManager.cleanSession(clientIdentifier);
        }
        SessionManager.InitSessionResult initSession =
            sessionManager.initSession(clientIdentifier);
        Session session = initSession.getSession();

        client = new Client(clientIdentifier);
        client.setConnection(connection);
        connected = true;
        // 3. The Server MUST acknowledge the CONNECT Packet with a CONNACK Packet containing a zero return code [MQTT-3.1.4-4].
        ConnackPacket response = new ConnackPacket();
        response.setReturnCode(ReturnCode.ACCEPTED);
        response.setSessionPresent(!packet.isCleanSession() && !initSession.isNew());
        client.getConnection().send(response);

        // 4. Start message delivery and keep alive monitoring.
        // todo: impl
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

        // The DUP flag MUST be set to 0 for all QoS 0 messages [MQTT-3.3.1-2].
        if (packet.isDuplicated() && packet.getQoS() == QoS.AT_MOST_ONCE) {
            disconnect();
            return;
        }

        // todo: The Topic Name in the PUBLISH Packet MUST NOT contain wildcard characters [MQTT-3.3.2-2].
        switch (packet.getQoS()) {
            case AT_MOST_ONCE -> {
                deliverMessage(packet.getTopicName(), packet.getPayload());
                // expected response: none
            }
            case AT_LEAST_ONCE -> {
                // PubackPacket response = new PubackPacket();
                // response.setPacketIdentifier(packet.getPacketIdentifier());
                // connection.send(response);
            }
            case EXACTLY_ONCE -> { } // todo: send pubrec packet
        }
    }

    private void onSubscribe(SubscribePacket packet) {
        System.out.println("!!! SubscribePacket");
        System.out.println("!!!   packetIdentifier=" + packet.getPacketIdentifier());
        for (SubscribePacket.Subscription sub : packet.getSubscriptions()) {
            System.out.println("!!!   topicFilter=%s, reqQoS=%s".formatted(sub.getTopicFilter(), sub.getRequestedQoS()));
        }

        // The payload of a SUBSCRIBE packet MUST contain at least one Topic Filter / QoS pair.
        // A SUBSCRIBE packet with no payload is a protocol violation [MQTT-3.8.3-3]
        if (packet.getSubscriptions().isEmpty()) {
            disconnect();
            return;
        }

        SubackPacket response = new SubackPacket();
        response.setPacketIdentifier(packet.getPacketIdentifier());

        Session session = sessionManager.getSession(client.getId());
        for (SubscribePacket.Subscription subscription : packet.getSubscriptions()) {
            session.getSubscriptions().add(new Subscription(
                subscription.getTopicFilter(), subscription.getRequestedQoS()));
            response.getReturnCodes().add(switch (subscription.getRequestedQoS()) {
                case AT_MOST_ONCE -> SubackPacket.ReturnCode.QOS_AT_MOST_ONCE;
                case AT_LEAST_ONCE -> SubackPacket.ReturnCode.QOS_AT_LEAST_ONCE;
                case EXACTLY_ONCE -> SubackPacket.ReturnCode.QOS_EXACTLY_ONCE;
            });
        }

        connection.send(response);
    }

    private void disconnect() {
        if (client != null) {
            // todo: remove session on disconnect when isCleanSession=1
            client = null;
        }

        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    private void deliverMessage(String topicName, ByteBuffer message) {
        for (Client client : clientManager.listClients()) {
            Session session = sessionManager.getSession(client.getId());
            for (Subscription subscription : session.getSubscriptions()) {
                if (subscription.getTopicFilter().equals(topicName)) {
                    PublishPacket packet = new PublishPacket();
                    packet.setDuplicated(false);
                    packet.setQoS(QoS.AT_MOST_ONCE);
                    packet.setRetain(false);
                    packet.setTopicName(topicName);
                    packet.setPacketIdentifier(0);
                    packet.setPayload(message);
                    client.getConnection().send(packet);
                }
            }
        }
    }
}
