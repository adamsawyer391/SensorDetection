package com.cosmic.sensordetection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

/**
 * This activity demonstrates the variety of ways you can check the output of a device's sensors.
 * You can:
 *      --implement the Sensor Event Listener as we have done here. This implements the onSensorChanged() and onAccuracyChanged() override methods.
 *      This is simple and direct but ideal if you only intend to check the output of one sensor. Since this activity checks values produced by multiple sensors, we have demonstrated other
 *      ways to accomplish the same task. This is how we check the atmospheric pressure sensor.
 *
 *      --With air temperature and magnetic field strength, we get a reference to the sensorManager and call .registerListener() on it and create
 *      a new SensorEventListener and perform all the necessary logic within it.
 *
 *      --We did almost the same thing with gravitational field strength except we created a separate instance of a sensor event listener object
 *      with the name 'gravityEventListener' and perform our logic within it. When we call registerListener() on our sensorManager object, we simply pass
 *      the gravityEventListener object to it.
 *
 *      --Alternatively, as we did with checking the humidity, you could create a separate method that crates instances of each sensor event listener and perform your logic within it.
 *      This way all you have to do is call that method in onCreate() and pass the event listener to the registerListener() method. This created the most optimized and
 *      separation of concerns code.
 *
 *  For a cleaner version of this same code, check out AtmoSensorClean.java
 */

public class AtmoSensor extends AppCompatActivity implements SensorEventListener {

    private Context context;
    private SensorManager sensorManager;
    private TextView tvAirPressure, tvAirTemperature, temp, millibars, magField, tvMagField, gravField, tvGravField, humidity, tvHumidity;
    private SensorEventListener gravityEventListener, humidtyEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atmo_sensor);
        context = AtmoSensor.this;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor airPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensorManager.registerListener(this, airPressure, SensorManager.SENSOR_DELAY_NORMAL);

        initUI();
        createSensorEventListeners();
        conductAirTemperatureCheck();
        conductMagneticFieldStrength();
        conductGravitationalFieldStrengthCheck();
        conductHumidityCheck();
    }

    private void initUI(){
        tvAirPressure = findViewById(R.id.airPressure);
        millibars = findViewById(R.id.tvMillibars);

        tvAirTemperature = findViewById(R.id.airTemperature);
        temp = findViewById(R.id.tvTemperature);

        magField = findViewById(R.id.magField);
        tvMagField = findViewById(R.id.tvMagField);

        gravField = findViewById(R.id.gravField);
        tvGravField = findViewById(R.id.tvGravField);

        humidity = findViewById(R.id.humidity);
        tvHumidity = findViewById(R.id.tvHumidity);
    }

    private void createSensorEventListeners(){
        humidtyEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                humidity.setText(String.valueOf(event.values[0]));
                tvHumidity.setText("%");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @SuppressLint("ObsoleteSdkInt")
    private void conductAirTemperatureCheck(){
        Sensor airTemperature = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            airTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        }
        if (airTemperature == null){
            tvAirTemperature.setText(getString(R.string.no_temperature));
            tvAirTemperature.setTextSize(16);
            tvAirTemperature.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }else{
            sensorManager.registerListener(new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    tvAirTemperature.setText(String.valueOf(event.values[0]));
                    StringBuilder stringBuilder = new StringBuilder();
                    temp.setText(stringBuilder.append(" ").append(getString(R.string.farenheit)));
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            }, airTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void conductMagneticFieldStrength(){
        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField == null){
            magField.setText(getString(R.string.no_magnetic_field));
            magField.setTextSize(16);
            magField.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }else{
            sensorManager.registerListener(new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    magField.setText(String.valueOf(event.values[0]));
                    StringBuilder stringBuilder = new StringBuilder();
                    tvMagField.setText(stringBuilder.append(" ").append(getString(R.string.teslas)));
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            }, magneticField, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void conductGravitationalFieldStrengthCheck(){
        gravityEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                gravField.setText(String.valueOf(event.values[0]));
                StringBuilder stringBuilder = new StringBuilder();
                tvGravField.setText(stringBuilder.append(" ").append(getString(R.string.newtons)));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        Sensor gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (gravitySensor == null){
            gravField.setText(getString(R.string.no_gravity));
            gravField.setTextSize(16);
            gravField.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }else{
            sensorManager.registerListener(gravityEventListener, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void conductHumidityCheck(){
        Sensor humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (humiditySensor == null){
            deliverVoidMessage(humidity);
        }else{
            sensorManager.registerListener(humidtyEventListener, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        tvAirPressure.setText(String.valueOf(event.values[0]));
        millibars.setText(getString(R.string.millibars));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void deliverVoidMessage(TextView textView){
        textView.setText(getString(R.string.no_sensor));
        textView.setTextSize(16);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    private void unregisterSensorListeners(){
        sensorManager.unregisterListener(this);
        sensorManager.unregisterListener(gravityEventListener);
        sensorManager.unregisterListener(humidtyEventListener);
    }

    private void enableBackNavigation(){
        Intent intent = new Intent(context, Startup.class);
        startActivity(intent);
        finish();
        Animatoo.animateCard(context);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        enableBackNavigation();
        unregisterSensorListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterSensorListeners();
    }
}
