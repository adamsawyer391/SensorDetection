package com.cosmic.sensordetection;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;
import java.util.List;

public class SensorDetail extends AppCompatActivity {

    private static final String TAG = "SensorDetail";

    private Context context;
    private final List<Sensor> allSensors = new ArrayList<>();
    private Sensor sensor;
    TextView name, vendor, type, fifoMaxCount, fifoReserveEventCount, resolution, highestDirectReportRate,
            ID, maxDelay, maxRange, minDelay, power, reportingMode, stringType, version, dynamic, wakeup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);
        context = SensorDetail.this;

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        allSensors.addAll(sensors);

        initUI();
        getSensor();
        extractSensorInformation();
    }

    private void initUI(){
        name = findViewById(R.id.name);
        vendor = findViewById(R.id.vendor);
        type = findViewById(R.id.typeS);
        fifoMaxCount = findViewById(R.id.fifoMaxCount);
        fifoReserveEventCount = findViewById(R.id.fifoReserveEventCount);
        resolution = findViewById(R.id.resolution);
        highestDirectReportRate = findViewById(R.id.highestDirectReportRateLevel);
        ID = findViewById(R.id.ID);
        maxDelay = findViewById(R.id.maxDelay);
        maxRange = findViewById(R.id.maximumRange);
        minDelay = findViewById(R.id.minDelay);
        power = findViewById(R.id.power);
        reportingMode = findViewById(R.id.reportingMode);
        stringType = findViewById(R.id.stringType);
        version = findViewById(R.id.version);
        dynamic = findViewById(R.id.dynamic);
        wakeup = findViewById(R.id.wakeup);
    }

    private void getSensor(){
        Intent intent = getIntent();
        int position = intent.getExtras().getInt("position");
        sensor = allSensors.get(position);
        Log.d(TAG, "getSensor: this is the : " + sensor);
        TextView title = findViewById(R.id.title);
        title.setText(sensor.getName());
    }

    private void extractSensorInformation(){
        name.setText(sensor.getName());
        vendor.setText(sensor.getVendor());
        type.setText(String.valueOf(sensor.getType()));
        fifoMaxCount.setText(String.valueOf(sensor.getFifoMaxEventCount()));
        fifoReserveEventCount.setText(String.valueOf(sensor.getFifoReservedEventCount()));
        resolution.setText(String.valueOf(sensor.getResolution()));
        maxDelay.setText(String.valueOf(sensor.getMaxDelay()));
        maxRange.setText(String.valueOf(sensor.getMaximumRange()));
        minDelay.setText(String.valueOf(sensor.getMinDelay()));
        power.setText(String.valueOf(sensor.getPower()));
        reportingMode.setText(String.valueOf(sensor.getReportingMode()));
        stringType.setText(sensor.getStringType());
        version.setText(String.valueOf(sensor.getVersion()));
        wakeup.setText(String.valueOf(sensor.isWakeUpSensor()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            highestDirectReportRate.setText(String.valueOf(sensor.getHighestDirectReportRateLevel()));
        }else{
            highestDirectReportRate.setText(getString(R.string.not_available));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ID.setText(String.valueOf(sensor.getId()));
            dynamic.setText(String.valueOf(sensor.isDynamicSensor()));
        }else{
            ID.setText(getString(R.string.not_available));
            dynamic.setText(getString(R.string.not_available));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, SensorList.class);
        startActivity(intent);
        finish();
        Animatoo.animateCard(context);
    }
}
