package ahodanenok.mqtt.server;

public class Client {

    private String id;
    private ClientConnection connection;

    public Client(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setConnection(ClientConnection connection) {
        this.connection = connection;
    }

    public ClientConnection getConnection() {
        return connection;
    }
}
