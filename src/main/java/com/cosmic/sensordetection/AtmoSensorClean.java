package com.cosmic.sensordetection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class AtmoSensorClean extends AppCompatActivity {

    private Context context;
    private SensorManager sensorManager;
    private TextView airPressure, tvAirPressure, airTemperature, tvAirTemperature, magField, tvMagField, gravField, tvGravField, humidity, tvHumidity;
    private SensorEventListener pressureListener, temperatureListener, magneticListener, gravitationListener, humidityListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmo_sensor);
        context = AtmoSensorClean.this;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.view_atmosphere));

        initUI();
        establishSensorEventListeners();
        conductAtmosphericPressureCheck();
        conductAirTemperatureCheck();
        conductMagneticFieldStrengthCheck();
        conductGravitationalFieldStrengthCheck();
        conductHumidityCheck();
    }

    private void initUI(){
        airPressure = findViewById(R.id.airPressure);
        tvAirPressure = findViewById(R.id.tvMillibars);
        airTemperature = findViewById(R.id.airTemperature);
        tvAirTemperature = findViewById(R.id.tvTemperature);
        magField = findViewById(R.id.magField);
        tvMagField = findViewById(R.id.tvMagField);
        gravField = findViewById(R.id.gravField);
        tvGravField = findViewById(R.id.tvGravField);
        humidity = findViewById(R.id.humidity);
        tvHumidity = findViewById(R.id.tvHumidity);
    }

    private  SensorEventListener sensorEventListener(TextView data, TextView measurement, String message){
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                displaySensorData(data, measurement, event, message);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void establishSensorEventListeners(){
        pressureListener = sensorEventListener(airPressure, tvAirPressure, getString(R.string.millibars)); //air pressure
        temperatureListener = sensorEventListener(airTemperature, tvAirTemperature, getString(R.string.farenheit)); //air temperature
        magneticListener = sensorEventListener(magField, tvMagField, getString(R.string.teslas));  //magnetic field
        gravitationListener = sensorEventListener(gravField, tvGravField, getString(R.string.newtons));  //gravitational field
        humidityListener = sensorEventListener(humidity, tvHumidity, getString(R.string.percentage));  //humidity
    }

    private void conductAtmosphericPressureCheck(){
        Sensor pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        initializeSensors(pressure, pressureListener, airPressure);
    }

    private void conductAirTemperatureCheck(){
        Sensor temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        initializeSensors(temperature, temperatureListener, airTemperature);
    }

    private void conductMagneticFieldStrengthCheck(){
        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        initializeSensors(magneticField, magneticListener, magField);
    }

    private void conductGravitationalFieldStrengthCheck(){
        Sensor gravitationalField = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        initializeSensors(gravitationalField, gravitationListener, gravField);
    }

    private void conductHumidityCheck(){
        Sensor humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        initializeSensors(humiditySensor, humidityListener, humidity);
    }

    private void initializeSensors(Sensor sensor, SensorEventListener eventListener, TextView textView){
        if (sensor == null){
            displayVoidMessage(textView);
        }else{
            sensorManager.registerListener(eventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void displaySensorData(TextView data, TextView measurement, SensorEvent event, String message){
        data.setText(String.valueOf(event.values[0]));
        measurement.setText(message);
    }

    private void displayVoidMessage(TextView textView){
        textView.setText(getString(R.string.no_sensor));
        textView.setTextSize(16);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    private void enableBackNavigation(){
        Intent intent = new Intent(context, Startup.class);
        startActivity(intent);
        finish();
        Animatoo.animateCard(context);
    }

    private void disconnectEventListeners(){
        sensorManager.unregisterListener(pressureListener);
        sensorManager.unregisterListener(temperatureListener);
        sensorManager.unregisterListener(magneticListener);
        sensorManager.unregisterListener(gravitationListener);
        sensorManager.unregisterListener(humidityListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        enableBackNavigation();
        disconnectEventListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disconnectEventListeners();
    }
}
