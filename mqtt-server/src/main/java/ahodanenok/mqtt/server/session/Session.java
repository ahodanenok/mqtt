package ahodanenok.mqtt.server.session;

import java.util.ArrayList;
import java.util.List;

/**
 * The Session state in the Server consists of:
 * - The existence of a Session, even if the rest of the Session state is empty.
 * - The Client’s subscriptions.
 * - QoS 1 and QoS 2 messages which have been sent to the Client, but have not been completely acknowledged.
 * - QoS 1 and QoS 2 messages pending transmission to the Client.
 * - QoS 2 messages which have been received from the Client, but have not been completely acknowledged.
 * - Optionally, QoS 0 messages pending transmission to the Client.
 */
public final class Session {

    boolean isNew;
    List<Subscription> subscriptions = new ArrayList<>();

    public boolean isNew() {
        return isNew;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }
}
