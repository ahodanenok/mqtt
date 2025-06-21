package ahodanenok.mqtt.server.packet;

import ahodanenok.mqtt.server.ReturnCode;

public final class ConnackPacket extends MqttPacket {

    private boolean sessionPresent;
    private ReturnCode returnCode;

    public ConnackPacket() {
        super(PacketType.CONNACK);
    }

    public void setSessionPresent(boolean sessionPresent) {
        this.sessionPresent = sessionPresent;
    }

    public boolean isSessionPresent() {
        return sessionPresent;
    }

    public void setReturnCode(ReturnCode returnCode) {
        this.returnCode = returnCode;
    }

    public ReturnCode getReturnCode() {
        return returnCode;
    }
}
