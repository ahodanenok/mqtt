package ahodanenok.mqtt.server;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {

    private final ConcurrentHashMap<String, Client> clients = new ConcurrentHashMap<>();


    public void registerClient(String clientIdentifier, Client client) {

    }

    public Collection<Client> listClients() {
        return clients.values();
    }

    public Client getClient(String clientIdentifier) {
        return null;
    }
}