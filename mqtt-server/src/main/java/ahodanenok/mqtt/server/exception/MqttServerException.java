package ahodanenok.mqtt.server.exception;

public class MqttServerException extends RuntimeException {

    public MqttServerException(String msg) {
        super(msg);
    }

    public MqttServerException(String msg, Exception ex) {
        super(msg, ex);
    }
}
