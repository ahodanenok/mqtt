package ahodanenok.mqtt.server;

import java.nio.ByteBuffer;

public interface DataConnection {

    void send(ByteBuffer buf);

    public void close();
}
