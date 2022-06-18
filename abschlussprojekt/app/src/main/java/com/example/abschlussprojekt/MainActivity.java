package com.example.abschlussprojekt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MainActivity extends AppCompatActivity {

    MqttAndroidClient mqttAndroidClient;
    String serverUri;
    String user;
    String password = "";
    String topic = "lohn";
    String clientId = "lohnr1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        serverUri = getString(R.string.hivemq_uri);
        user = getString(R.string.hivemq_user);
        password = getString(R.string.hivemq_password);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.e("MQTT", "Connection lost");
                Log.e("MQTT", cause.getMessage());
                Log.e("MQTT", cause.getStackTrace().toString());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                Log.i("MQTT", "messageArrived");
                TextView rec = findViewById(R.id.text);
                rec.append(message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.i("MQTT", "deliveryComplete");
            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(user);
        mqttConnectOptions.setPassword(password.toCharArray());

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("MQTT", "Connection established");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e("MQTT", "Connect failed");
                    Log.e("MQTT", exception.getMessage());
                    Log.e("MQTT", exception.getStackTrace().toString());
                }
            });


        } catch (MqttException ex) {
            ex.printStackTrace();
        }

        subscribe();
    }
    public void subscribe()
    {
            try {

                mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.i("MQTT", "Subscribed!");
                        MqttMessage message = new MqttMessage();
                        String mes = new String(message.getPayload());
                        ((TextView)findViewById(R.id.text)).setText(mes);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.e("MQTT", "onFailure!");
                    }
                });

            } catch (MqttException ex) {
                System.err.println("Exception whilst subscribing");
                ex.printStackTrace();
            }
    }
}