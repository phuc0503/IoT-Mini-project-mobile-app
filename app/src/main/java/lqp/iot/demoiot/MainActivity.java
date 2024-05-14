package lqp.iot.demoiot;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.util.Debug;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    MQTTHelper mqttHelper;
    TextView txtTemp, txtHumi, txtBright;
    LabeledSwitch btnLED, btnTV, btnAirConditioner, btnWirelessSpeaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTemp = findViewById(R.id.txtTemperature);
        txtHumi = findViewById(R.id.txtHumidity);
        txtBright = findViewById(R.id.txtBrightness);

        btnLED = findViewById(R.id.btnLED);
        btnTV = findViewById(R.id.btnTV);
        btnAirConditioner = findViewById(R.id.btnAirConditioner);
        btnWirelessSpeaker = findViewById(R.id.btnWirelessSpeaker);

        btnLED.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn) {
                    sendDataMQTT(mqttHelper.arrayTopics[3], "1");
                } else {
                    sendDataMQTT(mqttHelper.arrayTopics[3], "0");
                }
            }
        });

        btnTV.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn) {
                    sendDataMQTT(mqttHelper.arrayTopics[4], "1");
                } else {
                    sendDataMQTT(mqttHelper.arrayTopics[4], "0");
                }
            }
        });

        btnAirConditioner.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn) {
                    sendDataMQTT(mqttHelper.arrayTopics[5], "1");
                } else {
                    sendDataMQTT(mqttHelper.arrayTopics[5], "0");
                }
            }
        });

        btnWirelessSpeaker.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if (isOn) {
                    sendDataMQTT(mqttHelper.arrayTopics[6], "1");
                } else {
                    sendDataMQTT(mqttHelper.arrayTopics[6], "0");
                }
            }
        });


        startMQTT();
    }

    public void sendDataMQTT(String topic, String value) {
        MqttMessage msg = new MqttMessage();
        msg.setId(123);
        msg.setQos(0);
        msg.setRetained(false);

        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        } catch (MqttException e) {
        }
    }

    public void startMQTT() {
        mqttHelper = new MQTTHelper(this);
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("TESTMP", topic + "***" + message.toString());
                if (topic.contains("bbc-temp")) {
                    txtTemp.setText(message.toString() + "°C");
                } else if (topic.contains("bbc-humid")) {
                    txtHumi.setText(message.toString() + "%");
                } else if (topic.contains("bbc-lux")) {
                    txtBright.setText(message.toString() + "lx");
                } else if (topic.contains("bbc-led")) {
                    if (message.toString().equals("1")) {
                        btnLED.setOn(true);
                    } else {
                        btnLED.setOn(false);
                    }
                } else if (topic.contains("bbc-tv")) {
                    if (message.toString().equals("1")) {
                        btnTV.setOn(true);
                    } else {
                        btnTV.setOn(false);
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}