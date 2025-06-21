package ahodanenok.mqtt.server;

public enum ReturnCode {

    ACCEPTED(0),
    UNACCEPTABLE_PROTOCOL_VERSION(1),
    IDENTIFIER_REJECTED(2),
    SERVER_UNAVAILABLE(3),
    BAD_CREDENTIALS(4),
    NOT_AUTHORIZED(5);

    private final int value;

    ReturnCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
