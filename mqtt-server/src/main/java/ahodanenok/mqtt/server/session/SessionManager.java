package ahodanenok.mqtt.server.session;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    // todo: synchronization

    private Map<String, Session> sessions = new HashMap<>();

    public Session newSession(String clientIdentifier) {
        return null;
    }

    public Session getSession(String clientIdentifier) {
        return null;
    }

    public void cleanSession(String clientIdentifier) {

    }
}
