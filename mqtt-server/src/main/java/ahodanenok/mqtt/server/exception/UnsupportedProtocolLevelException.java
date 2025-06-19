package ahodanenok.mqtt.server.exception;

public class UnsupportedProtocolLevelException extends MqttServerException {

    public UnsupportedProtocolLevelException(int protocolLevel) {
        super("Procol level is not supported: " + protocolLevel);
    }
}
