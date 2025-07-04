package ahodanenok.mqtt.server.packet;

public enum PacketType {

    CONNECT(1),
    CONNACK(2),
    PUBLISH(3),
    PUBACK(4),
    SUBSCRIBE(8),
    SUBACK(9),
    PINGREQ(12),
    PINGRESP(13),
    DISCONNECT(14);

    public static PacketType from(int value) {
        for (PacketType type : values()) {
            if (type.value == value) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown packet type value: " + value);
    }

    final int value;

    PacketType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
