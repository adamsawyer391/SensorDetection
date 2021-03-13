package com.cosmic.sensordetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;
import java.util.List;

public class SensorList extends AppCompatActivity {

    private Context context;
    private RecyclerView recyclerView;
    private final List<Sensor> allSensors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_list);
        context = SensorList.this;
        recyclerView = findViewById(R.id.recycler_view);

        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.sensor_list));

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        allSensors.addAll(sensorList);

        fireUpRecyclerView();
    }

    private void fireUpRecyclerView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        SensorAdapter sensorAdapter = new SensorAdapter(context, allSensors);
        recyclerView.setAdapter(sensorAdapter);
        recyclerView.addOnItemTouchListener(new RecycleClick(context, new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemCLick(View view, int position) {
                Intent intent = new Intent(context, SensorDetail.class);
                intent.putExtra("position", position);
                startActivity(intent);
                Animatoo.animateCard(context);
            }
        }));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, Startup.class);
        startActivity(intent);
        finish();
        Animatoo.animateCard(context);
    }
}