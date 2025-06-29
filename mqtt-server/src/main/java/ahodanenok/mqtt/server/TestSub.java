package ahodanenok.mqtt.server;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class TestSub {

    public static void main(String... args) throws Exception {
        String topic        = "data/in";
        String broker       = "tcp://localhost:8095";
        String clientId     = "client_sub";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            sampleClient.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {}

                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Message: " + message.toString());
                }

                public void deliveryComplete(IMqttDeliveryToken token) {}
            });


            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(1);
            // connOpts.setUserName("admin");
            // connOpts.setPassword(new char[] { 'p', 'w', 'd' });
            // connOpts.setWill("will_topic", new byte[] { (byte) 10, (byte) 20 }, 1, true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Subscribing to: " + topic);
            sampleClient.subscribe(topic);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
}
