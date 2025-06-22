package ahodanenok.mqtt.server.packet;

public class PubackPacket extends MqttPacket {

    private int packetIdentifier;

    public PubackPacket() {
        super(PacketType.PUBACK);
    }

    public int getPacketidentifier() {
        return packetIdentifier;
    }

    public void setPacketIdentifier(int packetIdentifier) {
        this.packetIdentifier = packetIdentifier;
    }
}
