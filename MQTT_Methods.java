package com.example.anjeldethwingz.mqtt_isafe;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTT_Methods {

    public static boolean connect(String url,MqttClient client) {
        try {
            MemoryPersistence persistance = new MemoryPersistence();
            client = new MqttClient("tcp://" + url + ":1883", "client1", persistance);
            client.connect();


            return true;
        } catch (MqttException e) {
            //e.printStackTrace();
            System.out.println(e);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(e);
        }
        return false;
    }

    public static boolean pub(String topic, String payload,MqttClient client) {
        MqttMessage message = new MqttMessage(payload.getBytes());
        try {
            client.publish(topic, message);
            return true;
        } catch (MqttPersistenceException e) {
            //e.printStackTrace();
            System.out.println(e);
        } catch (MqttException e) {
            //e.printStackTrace();
            System.out.println(e);
        }
        return false;
    }
}
