package ahodanenok.mqtt.server;

public class Client {

    private ClientConnection connection;

    public Client() { }

    public void setConnection(ClientConnection connection) {
        this.connection = connection;
    }

    public ClientConnection getConnection() {
        return connection;
    }
}
