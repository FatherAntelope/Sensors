package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textViewX, textViewY, textViewZ, textViewLight, textViewDonut, textViewDistance;
    RotateTorus rotateTorus;
    String textDonut, HEX;
    float X, Y, Z, Light, Distance;
    float stepRotX;
    float stepRotY;

    boolean isSensorPositionPresent, isSensorLightPresent, isSensorDistancePresent, isRotateY, isRotateX;

    //Отвечает за все сенсоры в устройстве
    SensorManager sensorManager;
    //Отвечает за конкретный сенсор устройства
    Sensor sensorPosition, sensorLight, sensorDistance;
    //Мой класс для хранения данных датчиков
    SensorsData sensorsData = new SensorsData(0, 0, 0, 0);



    //считывает сенсоры
    @Override
    protected void onResume() {
        super.onResume();
        //проверяем наличие датчиков

        if(isSensorPositionPresent && isSensorLightPresent && isSensorDistancePresent) {
            sensorManager.registerListener(sensorEventListenerPosition, sensorPosition, SensorManager.SENSOR_DELAY_UI );
            sensorManager.registerListener(sensorEventListenerLight, sensorLight, SensorManager.SENSOR_DELAY_UI );
            sensorManager.registerListener(sensorEventListenerDistance, sensorDistance, SensorManager.SENSOR_DELAY_UI );
        }
    }

    //останавливает сенсоры
    @Override
    protected void onPause() {
        super.onPause();
        //проверяем наличие датчиков
        if(isSensorPositionPresent && isSensorLightPresent) {
            sensorManager.unregisterListener(sensorEventListenerPosition);
            sensorManager.unregisterListener(sensorEventListenerLight);
            sensorManager.unregisterListener(sensorEventListenerDistance);
        }
    }

    //прослушивает датчик акселерометр
    SensorEventListener sensorEventListenerPosition = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            X = sensorEvent.values[0];
            Y = sensorEvent.values[1];
            Z = sensorEvent.values[2];

            textViewX.setText("X: " + X);
            textViewY.setText("Y: " + Y);
            textViewZ.setText("Z: " + Z);
            if(isRotateX) {
                stepRotX += 0.04f;
            } else if (isRotateY) {
                stepRotY += 0.04f;
            }

            textDonut = rotateTorus.getTorus(-X, -Y, stepRotX, stepRotY);
            textViewDonut.setText(textDonut);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {}
    };

    //прослушивает сенсор света
    SensorEventListener sensorEventListenerLight = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            Light = sensorEvent.values[0];
            //sensorsData.setBrightness(Light);


            textViewLight.setText("Light: " + Light);
            if(Light != 0) {
                HEX = "#" + Integer.toHexString((int)Light * (-1));
            } else {
                HEX = "#FFFFFF";
            }


            textViewDonut.setTextColor(Color.parseColor(HEX));
            textViewDonut.setTextSize(20);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {}
    };

    //прослушивает сенсор дистанции
    SensorEventListener sensorEventListenerDistance = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            Distance = sensorEvent.values[0];
            //sensorsData.setBrightness(Light);

            if(Distance == 5) {
                isRotateY = true;
                isRotateX = false;
            } else if (Distance == 0) {
                isRotateY = false;
                isRotateX = true;
            } else {
                isRotateY = false;
                isRotateX = false;
            }

            textViewDistance.setText("Distance: " + Distance);
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {}
    };

    //сборка программы
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewX = findViewById(R.id.textViewX);
        textViewY = findViewById(R.id.textViewY);
        textViewZ = findViewById(R.id.textViewZ);
        textViewLight = findViewById(R.id.textViewLight);
        textViewDistance = findViewById(R.id.textViewDistance);
        textViewDonut = findViewById(R.id.textViewDonut);

        rotateTorus = new RotateTorus();
        //достаем все датчики
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //достаем сенсор акселерометр
        sensorPosition = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //достаем сенсор освещенности
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        //достаем сенсор дистанции
        sensorDistance = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //если датчики отсутствуют, то грустим
        if(sensorPosition != null && sensorLight != null) {
            stepRotX = 0; stepRotY = 0;
            rotateTorus = new RotateTorus();
            isSensorPositionPresent = true;
            isSensorLightPresent = true;
            isSensorDistancePresent= true;
        }

    }
}