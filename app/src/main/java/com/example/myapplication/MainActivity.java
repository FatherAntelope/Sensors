package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textViewX, textViewY, textViewZ, textViewLight, textViewDonut;
    RotateTorus rotateTorus;
    String textDonut;
    float X, Y, Z, Light;
    float stepRotX;
    float stepRotY;

    //Отвечает за все сенсоры в устройстве
    SensorManager sensorManager;
    //Отвечает за конкретный сенсор устройства
    Sensor sensorPosition, sensorDistance;
    //Мой класс для хранения данных датчиков
    SensorsData sensorsData = new SensorsData(0, 0, 0, 0);
    boolean isSensorPositionPresent, isSensorDistancePresent, isRotateY, isRotateX;

    //считывает сенсоры
    @Override
    protected void onResume() {
        super.onResume();
        //проверяем наличие датчиков

        if(isSensorPositionPresent && isSensorDistancePresent) {
            sensorManager.registerListener(sensorEventListenerPosition, sensorPosition, SensorManager.SENSOR_DELAY_UI );
            sensorManager.registerListener(sensorEventListenerDistance, sensorDistance, SensorManager.SENSOR_DELAY_UI );
        }
    }

    //останавливает сенсоры
    @Override
    protected void onPause() {
        super.onPause();
        //проверяем наличие датчиков
        if(isSensorPositionPresent && isSensorDistancePresent) {
            sensorManager.unregisterListener(sensorEventListenerPosition);
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
            //sensorsData.setX(X);
            //sensorsData.setY(Y);
            //sensorsData.setZ(Z);

            System.out.println("\n");
            System.out.println("X: " + X);
            System.out.println("Y: " + Y);
            System.out.println("Z: " + Z);

            textViewX.setText("X: " + X);
            textViewY.setText("Y: " + Y);
            textViewZ.setText("Z: " + Z);
            if(isRotateX) {
                stepRotX += 0.04f;
            } else if (isRotateY) {
                stepRotY += 0.04f;
            }



            textDonut = rotateTorus.getTorus(-X, -Y, stepRotX, stepRotY);


            // Z < 0 == за нами находится, я хз, это же тело вращения, ща придумаю


            textViewDonut.setText(textDonut);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {}
    };

    //прослушивает сенсор света
    SensorEventListener sensorEventListenerDistance = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            Light = sensorEvent.values[0];
            //sensorsData.setBrightness(Light);

            if(Light == 5) {
                isRotateY = true;
                isRotateX = false;
            } else if (Light == 0) {
                isRotateY = false;
                isRotateX = true;
            }

            System.out.println("Distance: " + Light);
            textViewLight.setText("Distance: " + Light);

            textViewDonut.setTextSize(20);
            //textViewDonut.setText("Пончик");
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
        textViewDonut = findViewById(R.id.textViewDonut);
        
        rotateTorus = new RotateTorus();
        //достаем все датчики
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //достаем сенсор магнитного поля
        sensorPosition = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //достаем сенсор освещенности
        sensorDistance = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //если датчики отсутствуют, то грустим
        if(sensorPosition != null && sensorDistance != null) {
            stepRotX = 0; stepRotY = 0;
            rotateTorus = new RotateTorus();
            isSensorPositionPresent = true;
            isSensorDistancePresent = true;

        }

    }
}