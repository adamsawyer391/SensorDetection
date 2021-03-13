package com.cosmic.sensordetection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class Startup extends AppCompatActivity {

    private Context context;
    private boolean isFlashLightOn = false;
    private CameraManager cameraManager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        context = Startup.this;

        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.app_name));

        sharedPreferences = getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        if (sharedPreferences.contains("flashlight_status")){
            isFlashLightOn = sharedPreferences.getBoolean("flashlight_status", false);
        }

        initUI();
    }

    private void initUI(){
        RelativeLayout relativeLayout = findViewById(R.id.relLayout1);
        relativeLayout.setOnClickListener(v -> toggleFlashlight(isFlashLightOn));

        RelativeLayout relativeLayout2 = findViewById(R.id.relLayout2);
        relativeLayout2.setOnClickListener(v -> visitAtmo());

        RelativeLayout relativeLayout1 = findViewById(R.id.relLayout3);
        relativeLayout1.setOnClickListener(v -> visitSensorList());
    }

    private void toggleFlashlight(boolean flash){
        try {
            String cameraID = cameraManager.getCameraIdList()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (flash){
                    cameraManager.setTorchMode(cameraID, false);
                    isFlashLightOn = false;
                }else{
                    cameraManager.setTorchMode(cameraID, true);
                    isFlashLightOn = true;
                }
                updateSharedPreferences(isFlashLightOn);
            }else{
                Toast.makeText(context, "You cannot manually turn on the flashlight", Toast.LENGTH_LONG).show();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void visitSensorList(){
        Intent intent = new Intent(context, SensorList.class);
        startActivity(intent);
        Animatoo.animateCard(context);
    }

    private void visitAtmo(){
        Intent intent = new Intent(context, AtmoSensorClean.class);
        startActivity(intent);
        Animatoo.animateCard(context);
    }

    private void updateSharedPreferences(boolean flash){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("flashlight_status", flash);
        editor.apply();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateSharedPreferences(isFlashLightOn);
    }
}
