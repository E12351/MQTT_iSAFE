package com.example.anjeldethwingz.mqtt_isafe;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class service extends Service {

    public static MqttClient client;
    public static String messageBody;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"service is created",Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"service is started",Toast.LENGTH_LONG).show();
        //------------------------------------------------------------------------------------------
        try {
            MemoryPersistence persistance = new MemoryPersistence();

            client = new MqttClient("tcp://" + "192.168.8.100" + ":1883", "Amila", persistance);

            client.setCallback(new MqttCallback(){
                @Override
                public void connectionLost(Throwable throwable) {

                }

                @Override
                public void messageArrived(String s, MqttMessage msg) throws Exception {
                    messageBody = new String(msg.getPayload());
                    Message message = Message.obtain();
                    message.arg1 = Integer.parseInt(messageBody) ;
                    MainActivity.handler.sendMessage(message);


                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
            client.connect();
            //client.setConnectTimeout(7000);

            Toast.makeText(this,"connected",Toast.LENGTH_LONG).show();

            //Subscribe to all subtopics of homeautomation
            //client.subscribe("test");
            Toast.makeText(this,"subcribed.",Toast.LENGTH_LONG).show();

        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
            //e.printStackTrace();
            System.out.println(e);
            Log.w("MyClassName", e);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        try {
            //client.disconnect(0);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
            //e.printStackTrace();
            System.out.println(e);
        }
        super.onDestroy();
        Toast.makeText(this,"service is destroyed",Toast.LENGTH_LONG).show();
    }
    public static boolean pub(String topic, String payload) {
        MqttMessage message = new MqttMessage(payload.getBytes());
        try {
            client.publish(topic, message);
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(e);
            Log.w("MyClassName", e);
        }

        return false;
    }


}
