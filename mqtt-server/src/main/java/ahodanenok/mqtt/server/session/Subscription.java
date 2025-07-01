package ahodanenok.mqtt.server.session;

public final class Subscription {

    public String topicPattern;

    public Subscription(String topicPattern) {
        this.topicPattern = topicPattern;
    }

    public String getTopicPattern() {
        return topicPattern;
    }
}
