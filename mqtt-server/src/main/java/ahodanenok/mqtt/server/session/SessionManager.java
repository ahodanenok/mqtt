package ahodanenok.mqtt.server.session;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    public static class InitSessionResult {

        private final Session session;
        private final boolean isNew;

        InitSessionResult(Session session, boolean isNew) {
            this.session = session;
            this.isNew = isNew;
        }

        public Session getSession() {
            return session;
        }

        public boolean isNew() {
            return isNew;
        }
    }

    // todo: synchronization

    private Map<String, Session> sessions = new HashMap<>();

    public InitSessionResult initSession(String clientIdentifier) {
        if (sessions.containsKey(clientIdentifier)) {
            return new InitSessionResult(sessions.get(clientIdentifier), false);
        } else {
            Session session = new Session();
            sessions.put(clientIdentifier, session);
            return new InitSessionResult(session, true);
        }
    }

    public Session getSession(String clientIdentifier) {
        return sessions.get(clientIdentifier);
    }

    public void cleanSession(String clientIdentifier) {

    }
}
