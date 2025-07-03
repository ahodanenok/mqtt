package ahodanenok.mqtt.server.packet;

import java.util.ArrayList;
import java.util.List;

public final class SubackPacket extends MqttPacket {

    public enum ReturnCode {

        QOS_AT_MOST_ONCE(0),
        QOS_AT_LEAST_ONCE(1),
        QOS_EXACTLY_ONCE(2),
        FAILURE(8);

        private final int value;

        ReturnCode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private int packetIdentifier;
    private List<ReturnCode> returnCodes = new ArrayList<>();

    public SubackPacket() {
        super(PacketType.SUBACK);
    }

    public int getPacketIdentifier() {
        return packetIdentifier;
    }

    public void setPacketIdentifier(int packetIdentifier) {
        this.packetIdentifier = packetIdentifier;
    }

    public List<ReturnCode> getReturnCodes() {
        return returnCodes;
    }
}
