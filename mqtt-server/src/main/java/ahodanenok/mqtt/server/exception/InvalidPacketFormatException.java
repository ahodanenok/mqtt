package ahodanenok.mqtt.server.exception;

public class InvalidPacketFormatException extends MqttServerException {

    public InvalidPacketFormatException(String msg) {
        super(msg);
    }
}
