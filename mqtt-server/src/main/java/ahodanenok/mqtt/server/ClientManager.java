package ahodanenok.mqtt.server;

import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {

    private final ConcurrentHashMap<String, Client> clients = new ConcurrentHashMap<>();


    public void registerClient(String clientIdentifier, Client client) {

    }

    public Client getClient(String clientIdentifier) {
        return null;
    }
}