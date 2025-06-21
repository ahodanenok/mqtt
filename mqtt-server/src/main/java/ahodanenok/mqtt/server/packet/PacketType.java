package ahodanenok.mqtt.server.packet;

public enum PacketType {

    CONNECT(1),
    CONNACK(2);

    final int code;

    PacketType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
