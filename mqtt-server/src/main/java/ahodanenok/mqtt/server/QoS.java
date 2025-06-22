package ahodanenok.mqtt.server;

public enum QoS {

    AT_MOST_ONCE(0),
    AT_LEAST_ONCE(1),
    EXACTLY_ONCE(2);

    public static QoS from(int value) {
        for (QoS qos : values()) {
            if (qos.getValue() == value) {
                return qos;
            }
        }

        throw new IllegalArgumentException("Unknown QoS value: " + value);
    }

    private final int value;

    QoS(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
