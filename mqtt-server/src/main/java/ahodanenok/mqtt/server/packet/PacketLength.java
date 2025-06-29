package ahodanenok.mqtt.server.packet;

public final class PacketLength {

    private final int value;
    private final int bytes;

    public PacketLength(int value, int bytes) {
        this.value = value;
        this.bytes = bytes;
    }

    public int getValue() {
        return value;
    }

    public int getBytes() {
        return bytes;
    }

    @Override
    public int hashCode() {
        return 31 * value + bytes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != PacketLength.class) {
            return false;
        }

        PacketLength other = (PacketLength) obj;
        return value == other.value && bytes == other.bytes;
    }

    @Override
    public String toString() {
        return "PacketLength(value=%s, bytes=%s)".formatted(value, bytes);
    }
}
