package ahodanenok.mqtt.server.packet;

public enum PacketType {

    CONNECT(1),
    CONNACK(2),
    PUBLISH(3),
    PUBACK(4),
    DISCONNECT(14);

    final int value;

    PacketType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
